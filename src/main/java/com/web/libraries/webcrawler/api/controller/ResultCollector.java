package com.web.libraries.webcrawler.api.controller;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Interface that realize crawl results report in the specifc format
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see ResultFormatter
 * @see WebCrawlerException
 */
public interface ResultCollector extends CommandParameter {
	/**
	 * Export a Web Site Page in a report format
	 * @param content Result Web Page to be exported in report 
	 * @throws WebCrawlerException Exception occurred during the Site Export
	 */
	void exportPage(WebPage content) throws WebCrawlerException;

	/**
	 * Set the Site Page formatter
	 * @param formatter Result Formatter used to Format the Web Page output 
	 */
	void setFormatter(ResultFormatter formatter);

}
