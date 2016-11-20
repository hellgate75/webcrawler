package com.web.libraries.webcrawler.api.utils;

import com.web.libraries.webcrawler.api.controller.ResultCollector;
import com.web.libraries.webcrawler.api.controller.ResultFormatter;

/**
 * Constant Names Class
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 */
public class Constants {

	/*
	 * WEB CRAWLER DEFAULT CONFIGURATIONS
	 */
	
	/**
	 * Identify a system property for the default result report formatter
	 * @see ResultFormatter
	 */
	public static final String PROPERTIES_DEFAULT_REPORT_FORMATTER = "com.web.libraries.webcrawler.defaultFormatter";
	/**
	 * Identify a system property for the default result report collector type
	 * @see ResultCollector
	 */
	public static final String PROPERTIES_DEFAULT_REPORT_COLLECTOR = "com.web.libraries.webcrawler.defaultCollector";
	/**
	 * Identify a system property for the default timeout in milliseconds used to delay calls and prevent the crawler is identified
	 * by the security systems
	 */
	public static final String PROPERTIES_DEFAULT_CRAWLER_ANTI_IDENTIFY_TIMEOUT = "com.web.libraries.webcrawler.crowlerIdentifyTimeout";
	/**
	 * Identify a system property for the number of parallel threads extended to surf the web site and descend the children
	 */
	public static final String PROPERTIES_DEFAULT_PARALLEL_THREADS = "com.web.libraries.webcrawler.parallelThreadsExtend";
	/**
	 * Identify a system property for the maximum surf descendant of the web site or zero in case of no limit
	 */
	public static final String PROPERTIES_DEFAULT_MAX_SURFING_LEVEL = "com.web.libraries.webcrawler.maxSurfLevel";
	/**
	 * Identify a system property for skipping partial duplicate urls in the children of the same parent (e.g. ..../a.php is similar to ..../a.php?category=1)
	 */
	public static final String PROPERTIES_DEFAULT_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN = "com.web.libraries.webcrawler.skipChildrenPartialURLDuplicates";
	/**
	 * Identify a system property for surfing as default web site to surf within
	 */
	public static final String PROPERTIES_DEFAULT_WEBSITE = "com.web.libraries.webcrawler.defaultWebSite";

	/*
	 * WEB CRAWLER COMMAND LINE PROPERTIES
	 */

	/**
	 * Identify a command line parsed property for the default result report formatter
	 * @see ResultFormatter
	 */
	public static final String COMMAND_PROPERTY_REPORT_FORMATTER = "format";
	/**
	 * Identify a command line parsed property for the default result report collector type
	 * @see ResultCollector
	 */
	public static final String COMMAND_PROPERTY_REPORT_COLLETOR = "output";
	/**
	 * Identify a command line parsed property for the default timeout in milliseconds used to delay calls and prevent the crawler is identified
	 * by the security systems
	 */
	public static final String COMMAND_PROPERTY_CRAWLER_ANTI_IDENTIFY_TIMEOUT = "crawldelay";
	/**
	 * Identify a command line parsed property for the number of parallel threads extended to surf the web site and descend the children
	 */
	public static final String COMMAND_PROPERTY_PARALLEL_THREADS = "extends";
	/**
	 * Identify a command line parsed property for the maximum surf descendant of the web site or zero in case of no limit
	 */
	public static final String COMMAND_PROPERTY_MAX_SURFING_LEVEL = "surflevel";
	/**
	 * Identify a command line parsed property for skipping partial duplicate urls in the children of the same parent (e.g. ..../a.php is similar to ..../a.php?category=1)
	 */
	public static final String COMMAND_PROPERTY_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN = "skipdupl";
	/**
	 * Identify a command line parsed property printing the help
	 */
	public static final String COMMAND_PROPERTY_HELP_REQUEST = "help";

}
