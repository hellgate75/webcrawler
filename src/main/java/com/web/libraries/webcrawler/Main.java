package com.web.libraries.webcrawler;

import com.web.libraries.webcrawler.api.WebCrawler;
import com.web.libraries.webcrawler.api.WebCrawlerHelper;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;

/**
 * Web Crawler Main class that allow to execute the Crawler by command line or behalf the invocation
 * of the main method. 
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebCrawler
 * @see WebCrawlerException
 */
public class Main {

	/**
	 * Main method
	 * @param arguments input arguments list
	 * @throws WebCrawlerException Any exception raised during the Web Crawler boostrap
	 */
	public static void main(String[] arguments) throws WebCrawlerException {
		WebCrawlerHelper.parseParametersAndRunCrawler(arguments);
	}

}
