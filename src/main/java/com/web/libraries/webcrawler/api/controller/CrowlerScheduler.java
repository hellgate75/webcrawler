package com.web.libraries.webcrawler.api.controller;

import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Interface that realize crawl scheduling feature for a Web Page
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 */
public interface CrowlerScheduler {
	/**
	 * Schedule a single Web SitePage crawling
	 * @param sitePage Site Page scheduled for the crawl
	 */
	void schedule(WebPage sitePage);
}
