package com.web.libraries.webcrawler.api;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfiguration;
import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfigurationBuilder;
import com.web.libraries.webcrawler.api.controller.CrowlerScheduler;
import com.web.libraries.webcrawler.api.controller.ResultCollector;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;
import com.web.libraries.webcrawler.api.utils.Logger;
import com.web.libraries.webcrawler.command.collectors.StandarOutputResultCollector;
import com.web.libraries.webcrawler.command.formatters.PlainTextFormatter;

/**
 * Web Crawler executor. It is the commander that schedule the processes and it is the responsible of the
 * requirements and profile information collection 
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see WebCrawlerProcess
 * @see WebCrawlerException
 */
public class WebCrawler implements CrowlerScheduler, IntConsumer {
	
	private WebPage rootSite;
	private String projectUID;
	
	private ResultCollector collector;
	private Queue<WebPage> siteQueue = new ConcurrentLinkedQueue<>() ;
	private WebCrawlerConfiguration configuration;
	
	/**
	 * Default Constructor
	 * @param collector Result Collector Instance
	 * @param configuration Web Crawling Project Configuration
	 * @throws WebCrawlerException Thrown when any crawling exception occures
	 */
	public WebCrawler(ResultCollector collector, WebCrawlerConfiguration configuration) throws WebCrawlerException {
		super();
		Logger.info("WEB SITE URL : " + configuration.getWebSite(), WebCrawler.class);
		Logger.info("MAX PARALLEL PROCESSES : " + configuration.getThredExtends(), WebCrawler.class);
		Logger.info("MAX VISIT LEVEL : " + configuration.getMaxSurfingLevel(), WebCrawler.class);
		Logger.info("MAX SKIP PARTIAL URL : " + configuration.getSkipDuplicates(), WebCrawler.class);
		Logger.debug("*****************************", WebCrawler.class);
		Logger.debug("STARTING URL CROWL ....", WebCrawler.class);
		Logger.debug("*****************************", WebCrawler.class);
		this.rootSite = WebPage.fromUrlString(configuration.getWebSite());
		this.projectUID = WebCrawlerHelper.regiterWebCrawerProject(configuration);
		this.collector = collector;
		this.configuration = configuration;
	}
	
	/**
	 * Start the crawling operations
	 */
	public void start() {
		startSiteCrowl(rootSite);
		configuration.increment();
		consumeQueue();
	}
	
	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CrowlerScheduler#schedule(com.web.libraries.webcrawler.api.model.WebPage)
	 */
	@Override
	public void schedule(WebPage site) {
		String fullPageURL = site.getFullUrlString();
		String pageURLNoQry = fullPageURL.contains("?") ? fullPageURL.substring(0,  fullPageURL.lastIndexOf("?")) : fullPageURL;
		if(!pageURLNoQry.equals(rootSite.getFullUrlString())&&
				!pageURLNoQry.equals(rootSite.getFullUrlString()+"/") && 
				(this.configuration.getMaxSurfingLevel()==0 || site.getLevel()<=this.configuration.getMaxSurfingLevel() ) ) {
			Logger.debug("Adding queue element ....", WebCrawler.class);
			Logger.debug("Adding (level : "+site.getLevel()+") : " + site.getFullUrlString(), WebCrawler.class);
			configuration.addVisitedURL(site.getFullUrlString());
			siteQueue.add(site);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.function.IntConsumer#accept(int)
	 */
	@Override
	public void accept(int index) {
		if (siteQueue.size()>0) {
			Logger.debug("Creating thread (index : "+index+") - queue size: " + siteQueue.size(), WebCrawler.class);
		}
		startSiteCrowl(siteQueue.poll());
	}

	private void startSiteCrowl(WebPage site) {
		if (site!= null) {
			Logger.debug("Found : " + site.getFullUrlString(), WebCrawler.class);
			WebCrawlerProcess process = new WebCrawlerProcess(this.projectUID, site, this);
			Logger.debug("Cowling (site : "+site.getFullUrlString()+") ...", WebCrawler.class);
			process.start();
		}
	}
	
	private void print() {
		printSite(this.rootSite, collector);
	}

	private static void printSite(WebPage sitePage, ResultCollector collector) {
		try {
			collector.exportPage(sitePage);
		} catch (WebCrawlerException e) {
			Logger.error("Error during the export of the Web Site Pages", WebCrawler.class);
		}
	}
	
	private synchronized void consumeQueue() {
		do {
			if(Thread.activeCount()-1<this.configuration.getThredExtends() ) {
				IntStream.range(0, this.configuration.getThredExtends()).forEach(this);
			}
			else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			Logger.debug(">>>>>>>>Processi attivi : " + Thread.activeCount());
		} while(!siteQueue.isEmpty() || Thread.activeCount()>1);
		Logger.debug("*****************************", WebCrawler.class);
		Logger.debug("COMPLETED URL CROWL !!", WebCrawler.class);
		Logger.debug("PRINTING SITE MAP", WebCrawler.class);
		Logger.debug("*****************************", WebCrawler.class);
		print();
	}
	
	public static void main(String[] args) throws Throwable {
		ResultCollector collector = new StandarOutputResultCollector();
		collector.setFormatter(new PlainTextFormatter());
		new WebCrawler(collector, new WebCrawlerConfigurationBuilder().webSite("http://localhost:8081/test-site/index.html").skipDuplicates(true).build()).start();
		//new WebCrowler("http://wiprodigital.com", System.out);
		//new WebCrowler("https://www.oracle.com/index.html", System.out);
		//new WebCrowler("http://www.posteitaliane.it", System.out);
		//new WebCrowler("https://www.fiat.it/", System.out);
		//new WebCrowler("http://www.eng.it/", System.out);
	}
	
}
