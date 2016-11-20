package com.web.libraries.webcrawler.api.controller;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;

/**
 * Interface that realize a base command parameter functions. Any implementing parameter realize a specific Web Crawler feature.
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebPage
 * @see ResultFormatter
 * @see WebCrawlerException
 */
public interface CommandParameter {
	/**
	 * Retrieve the parameter selector name
	 * @return (String) The parameter selector name 
	 */
	String getSelector();
	
	/**
	 * Retrieve the parameter description
	 * @return (String) The parameter description 
	 */
	String getDescription();

	/**
	 * Retrieve the active state of the parameter object
	 * @return (Boolean) The parameter active state flag
	 */
	Boolean isActive();

}
