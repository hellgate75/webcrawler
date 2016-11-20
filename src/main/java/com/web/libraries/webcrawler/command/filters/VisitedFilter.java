/**
 * 
 */
package com.web.libraries.webcrawler.command.filters;

import com.web.libraries.webcrawler.api.WebCrawlerHelper;
import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfiguration;
import com.web.libraries.webcrawler.api.controller.SitePageFilter;
import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Partial/Full URL Duplicates Filter Class
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 */
public class VisitedFilter implements SitePageFilter {
	
	private String webCrawlProjectID;

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.SitePageFilter#accept(com.web.libraries.webcrawler.api.model.WebPage, java.util.List)
	 */
	@Override
	public boolean accept(WebPage childPage, WebPage parent) {
		WebCrawlerConfiguration configuration = WebCrawlerHelper.getProjectConfiguration(this.webCrawlProjectID);
		boolean skipDuplicates = false;
		if (configuration!=null) {
			skipDuplicates = configuration.getSkipDuplicates().booleanValue();
//			System.out.println("Site Page : "+childPage.getFullUrlString()+" - Visited : " + parent.checkVisitedInHierarchy(childPage, configuration.getSkipDuplicates().booleanValue()));
//			return !parent.checkVisitedInHierarchy(childPage, configuration.getSkipDuplicates().booleanValue());
		}
		for(String url : configuration.getVisitedURLs()) {
			if (skipDuplicates) {
				String childURL = childPage.getFullUrlString();
				if (childURL.indexOf("?")>0)
					childURL = childURL.substring(0, childURL.indexOf("?"));
				String visitedURL = url;
				if (visitedURL.indexOf("?")>0)
					visitedURL = visitedURL.substring(0, visitedURL.indexOf("?"));
				if (childURL.equalsIgnoreCase(visitedURL)) {
					return false;
				}
			}
			else {
				if (childPage.getFullUrlString().equalsIgnoreCase(url)) {
					return false;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.SitePageFilter#configure(java.lang.String)
	 */
	@Override
	public void configure(String webCrawlProjectID) {
		this.webCrawlProjectID = webCrawlProjectID;
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.SitePageFilter#isActive()
	 */
	@Override
	public Boolean isActive() {
		return Boolean.TRUE;
	}


}
