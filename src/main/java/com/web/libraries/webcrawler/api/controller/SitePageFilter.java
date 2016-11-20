package com.web.libraries.webcrawler.api.controller;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Interface that realize a web page url filter to allow/deny the surfing in the hyper links.
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see ResultFormatter
 * @see WebCrawlerException
 */
public interface SitePageFilter {
	/**
	 * Evaluates the Web Page model element and accepts the as candidate for the surfing
	 * @param sitePage Site Page Model to be evaluated
	 * @param pages Parent Page Model
	 * @return acceptance status
	 */
	boolean accept(WebPage sitePage, WebPage parent);
	/**
	 * Configure the registered Web Crawling project ID for the current process.
	 * @param webCrawlProjectID Unique identifier of a project ID
	 */
	void configure(String webCrawlProjectID);
	/**
	 * Retrieve the active state of the filter
	 * @return (Boolean) The filter active state flag
	 */
	Boolean isActive();
}
