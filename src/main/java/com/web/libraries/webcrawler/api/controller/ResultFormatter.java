package com.web.libraries.webcrawler.api.controller;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Interface that realize crawl results report in the specifc format
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see WebCrawlerException
 */
public interface ResultFormatter extends CommandParameter {
	/**
	 * Provides the Web Site Page format feature
	 * @param content Result Web Page to be exported in report 
	 * @return (Object) The formatted Web Side Page
	 * @throws WebCrawlerException Exception occurred during the Site format
	 */
	Object format(WebPage content) throws WebCrawlerException;


}
