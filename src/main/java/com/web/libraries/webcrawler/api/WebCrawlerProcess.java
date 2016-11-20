package com.web.libraries.webcrawler.api;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfiguration;
import com.web.libraries.webcrawler.api.controller.CrowlerScheduler;
import com.web.libraries.webcrawler.api.controller.SitePageFilter;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;
import com.web.libraries.webcrawler.api.utils.Logger;

/**
 * Thread Class that operates the crawling of a single page and recover a list of suitable sub-pages. This class is
 * responsible for the re-schedule of the accepted child pages.
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see CrowlerScheduler
 * @see WebCrawler
 * @see WebCrawlerException
*/
public class WebCrawlerProcess extends Thread {

	private static HashMap<String, HashMap<String, String>> host2cookies = new HashMap<String, HashMap<String, String>>();

	private WebPage currentSitePage;
	private CrowlerScheduler scheduler;
	private List<SitePageFilter> filters = null;
	private WebCrawlerConfiguration configuration;

	/**
	 * WebCrowler process constructor
	 * @param projectUID Project Unique Identifier
	 * @param currentSitePage Web Site Page to crawl into
	 * @param scheduler Child Web Page crawling scheduler
	 */
	public WebCrawlerProcess(String projectUID, WebPage currentSitePage, CrowlerScheduler scheduler) {
		super();
		this.currentSitePage = currentSitePage;
		this.scheduler = scheduler;
		this.filters = WebCrawlerHelper.getFilters(projectUID);
		configuration = WebCrawlerHelper.getProjectConfiguration(projectUID);
	}


	private static void loadCookiesByHost(String host, Connection con) {
	    try {
	        if (host2cookies.containsKey(host)) {
	            HashMap<String, String> cookies = host2cookies.get(host);
	            for (Entry<String, String> cookie : cookies.entrySet()) {
	                con.cookie(cookie.getKey(), cookie.getValue());
	            }
	        }
	    } catch (Throwable t) {
	        Logger.error(t.toString()+":: Error loading cookies ... ", t, WebCrawler.class);
	    }
	}

	private static void storeCookiesByHost(String host, Connection con) {
	        try {
	            HashMap<String, String> cookies = host2cookies.get(host);
	            if (cookies == null) {
	                cookies = new HashMap<String, String>();
	                host2cookies.put(host, cookies);
	            }
	            cookies.putAll(con.response().cookies());
	        } catch (Throwable t) {
		        Logger.error("Error saving cookies ... " , t, WebCrawler.class);
	        }    
	}   

	private void crawl() throws IOException, InterruptedException {
		Thread.sleep(1000);
		Logger.debug("*****Page : " + currentSitePage.getFullUrlString(), WebCrawler.class);
		Document doc = null;
		for (int i = 1; i <= 3; i++) {
		      try{
		    	 Connection conn = Jsoup.connect(currentSitePage.getFullUrlString()).followRedirects(false).userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)").timeout(10000);
		    	 storeCookiesByHost(currentSitePage.getHost(), conn);
		    	 loadCookiesByHost(currentSitePage.getHost(), conn);
		  		 doc = conn.get();
		  		currentSitePage.setVisited(true);
		          break; // Break immediately if successful
		      }
		      catch (SocketTimeoutException e){
		          // Swallow exception and try again
		          Logger.warn("Retrieving URL <"+currentSitePage.getFullUrlString()+"> Timeout occurred " + i + " time(s)", WebCrawler.class);
		      }                 
		  }
		if(doc!=null) {
	 		Elements links = doc.select("a");
	 		configuration.increment(links.size());
			for (Element link : links) {
				String foundUrl = link.attr("abs:href").toLowerCase();
				Logger.debug("*****Found URL : " + foundUrl, WebCrawler.class);
				try {
					WebPage childPage = WebPage.fromUrlString(foundUrl);
					Logger.debug("*****As Page Host : " + currentSitePage.getHost(), WebCrawler.class);
					//Check if the page is in the same domain
					childPage = WebPage.fromUrlString(foundUrl, currentSitePage);
					if( childPage.getSiteBaseURLString().equals(currentSitePage.getSiteBaseURLString())) {
						//Check if the page has been visited and has all the requirement to be visited
						if (accepts(childPage, currentSitePage)) {
							configuration.addVisitedURL(childPage.getFullUrlString());
							Logger.debug("*****Accepted for surfing : " + foundUrl, WebCrawler.class);
							scheduler.schedule(childPage);
						}
						else {
							Logger.debug("*****Rejected as already visited for surfing : " + foundUrl, WebCrawler.class);
							childPage.setVisited(true);
						}
					}
					else {
						Logger.debug("*****Rejected as domain outsider for surfing : " + foundUrl, WebCrawler.class);
					}
				} catch (Exception e) {
					Logger.error("*****Rejected for error : " + foundUrl, e, WebCrawler.class);
				}
			}
		}
	}
	
	
	private boolean accepts(WebPage current, WebPage parent) {
		//We apply dinamically all the provided and active filters
		for(SitePageFilter filter: filters) {
			if(!filter.accept(current, parent)) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try {
			this.crawl();
		} catch (IOException|InterruptedException e) {
	        Logger.error("Error exeuting the crawl operations ... " , e, WebCrawler.class);
		}
	}

}
