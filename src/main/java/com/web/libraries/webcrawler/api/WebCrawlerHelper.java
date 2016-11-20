package com.web.libraries.webcrawler.api;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfiguration;
import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfigurationBuilder;
import com.web.libraries.webcrawler.api.controller.ResultCollector;
import com.web.libraries.webcrawler.api.controller.ResultFormatter;
import com.web.libraries.webcrawler.api.controller.SitePageFilter;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.utils.Constants;
import com.web.libraries.webcrawler.api.utils.Logger;

/**
 * Web Crawler Helper Class
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 */
public class WebCrawlerHelper {
	private static Properties webCrawlerProperties = new Properties();
	private static Map<String, WebCrawlerConfiguration> projectsConfigurationMap = new LinkedHashMap<String, WebCrawlerConfiguration>(0);
	private final static Reflections filterReflections = new Reflections(new ConfigurationBuilder()
		     .setUrls(ClasspathHelper.forPackage("com.web.libraries.webcrawler.command.filters"))
		     .setScanners(new SubTypesScanner())
		     .filterInputsBy(new FilterBuilder().includePackage("com.web.libraries.webcrawler.command.filters")));

	private final static Reflections collectorsReflections = new Reflections(new ConfigurationBuilder()
		     .setUrls(ClasspathHelper.forPackage("com.web.libraries.webcrawler.command.collectors"))
		     .setScanners(new SubTypesScanner())
		     .filterInputsBy(new FilterBuilder().includePackage("com.web.libraries.webcrawler.command.collectors")));

	private final static Reflections formattersReflections = new Reflections(new ConfigurationBuilder()
		     .setUrls(ClasspathHelper.forPackage("com.web.libraries.webcrawler.command.formatters"))
		     .setScanners(new SubTypesScanner())
		     .filterInputsBy(new FilterBuilder().includePackage("com.web.libraries.webcrawler.command.formatters")));
	
	
	static {
		try {
			webCrawlerProperties.load(ClassLoader.getSystemResourceAsStream("web-crawler.properties"));
		} catch (IOException e) {
			Logger.error("Error loading default properties",e, WebCrawlerHelper.class);
		}
	}

	/**
	 * Retrieve the list of crawler default properties
	 * @return list of default properties
	 */
	public static final Properties getDefaultCrawlerProperties() {
		return webCrawlerProperties;
	}
	
	/**
	 * Parse the comand line arguments and create and execute the crawler or print the help.
	 * In case of needeing the usage is available typing as argument : --help
	 * @param arguments command line arguments
	 * @throws WebCrawlerException Exception occurring during the Crawler execution
	 */
	public static final void parseParametersAndRunCrawler(String[] arguments) throws WebCrawlerException {
		WebCrawlerConfigurationBuilder builder = new WebCrawlerConfigurationBuilder();
		for(String argument: arguments) {
			if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_HELP_REQUEST)>=0) {
				printWebCrawlerUsage(System.out);
				System.exit(0);
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_CRAWLER_ANTI_IDENTIFY_TIMEOUT + "=")>=0) {
				try {
					builder.interceptTimeout( Long.parseLong(argument.substring(argument.indexOf("=")+1)) );
				} catch (NumberFormatException|NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_CRAWLER_ANTI_IDENTIFY_TIMEOUT+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_MAX_SURFING_LEVEL + "=")>=0) {
				try {
					builder.maxSurfingLevel( Long.parseLong(argument.substring(argument.indexOf("=")+1)) );
				} catch (NumberFormatException|NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_MAX_SURFING_LEVEL+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_PARALLEL_THREADS + "=")>=0) {
				try {
					builder.thredExtends( Integer.parseInt(argument.substring(argument.indexOf("=")+1)) );
				} catch (NumberFormatException|NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_PARALLEL_THREADS+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN + "=")>=0) {
				try {
					builder.skipDuplicates( Boolean.parseBoolean(argument.substring(argument.indexOf("=")+1)) );
				} catch (NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_REPORT_FORMATTER + "=")>=0) {
				try {
					builder.formatter( argument.substring(argument.indexOf("=")+1) );
				} catch (NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_REPORT_FORMATTER+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else if (argument.toLowerCase().indexOf("--" + Constants.COMMAND_PROPERTY_REPORT_COLLETOR + "=")>=0) {
				try {
					builder.outputType( argument.substring(argument.indexOf("=")+1) );
				} catch (NullPointerException e) {
					String message = "Error parsing parameter '"+Constants.COMMAND_PROPERTY_REPORT_COLLETOR+"'";
					Logger.error(message, e, WebCrawlerHelper.class);
					printWebCrawlerUsage(System.out, message);
				}
			}
			else {
				builder.webSite(argument);
			}
		}
		WebCrawlerConfiguration configuration = builder.build();
		ResultCollector collector=getCollector(configuration.getOutputType(),configuration.getFormatter());
		new WebCrawler(collector, configuration).start();
	}
	
	/**
	 * Print the help usage text in a specific output stream
	 * @param output The select help stream
	 */
	public static final void printWebCrawlerUsage(PrintStream output)  {
		printWebCrawlerUsage(output, null);
	}
	/**
	 * Print the help usage text in a specific output stream
	 * @param output The select help stream
	 * @param message Message to display
	 */
	public static final void printWebCrawlerUsage(PrintStream output, String message)  {
		if(message!=null) {
			output.println("Message: " + message);
		}
		output.println("Usage: webcrawler [options] [website]");
		output.println("samples: webcrowler --format=simple --output=stdout http://www.google.com");
		output.println("         webcrowler http://www.facebook.com");
		output.println("options:");
		output.println("--"+Constants.COMMAND_PROPERTY_REPORT_FORMATTER+"\t\tIdentifies the site map format");
		output.println("     available formats :");
		for (ResultFormatter formatter: getFormatters())
			output.println("     " + formatter.getSelector() + "\t\t" + formatter.getDescription());
		output.println("default : " + getDefultFormatter());
		output.println("--"+Constants.COMMAND_PROPERTY_REPORT_COLLETOR+"\t\tIdentifies output device");
		output.println("     available output :");
		for (ResultCollector collector: getCollectors())
			output.println("     " + collector.getSelector() + "\t\t" + collector.getDescription());
		output.println("default : " + getDefultOutputType());
		output.println("--"+Constants.COMMAND_PROPERTY_CRAWLER_ANTI_IDENTIFY_TIMEOUT+"\t\tIdentifies the delay in milliseconds before a surf thread starts to prevent the site");
		output.println("\t\t\tsecurity system to intercept the crawling activity and block the access from the IP address");
		output.println("default : " + getDefultAntiInterceptTimeout());
		output.println("--"+Constants.COMMAND_PROPERTY_PARALLEL_THREADS+"\t\tIdentifies the number of threads extension on the surf. This is the minimum number of threads");
		output.println("\t\t\trunning on the site hierarchy discovery");
		output.println("default : " + getDefultThredExtends());
		output.println("--"+Constants.COMMAND_PROPERTY_MAX_SURFING_LEVEL+"\t\tIdentifies the meximum number of levels descending from the the website root (0 means unimited)");
		output.println("default : " + getDefultMaxSurfingLevel());
		output.println("--"+Constants.COMMAND_PROPERTY_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN+"\t\tIdentifies the will to skip parametied calls on the same pages of a parent page");
		output.println("default : " + getDefultSkipDuplicates());
		output.println("website:");
		output.println("Web Site to surf within (eg.: http://www.google.com or http://localhost:8085/mysite)");
		output.println("default : " + getDefultWebSite());
	}
	

	/**
	 * Retrieves the default web site URL
	 * @return The default web site URL
	 */
	public static final String getDefultWebSite() {
		return (String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_WEBSITE, "http://wiprodigital.com");
	}
	
	/**
	 * Retrieves the default site map response formatter
	 * @return The default site map response formatter
	 */
	public static final String getDefultFormatter() {
		return (String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_REPORT_FORMATTER, "plain");
	}
	
	/**
	 * Retrieves the default output device
	 * @return The default output device
	 */
	public static final String getDefultOutputType() {
		return (String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_REPORT_COLLECTOR, "stdout");
	}
	
	/**
	 * Retrieves the default max surfing level in the Web site hierarchy
	 * @return The default max surfing level
	 */
	public static final Long getDefultMaxSurfingLevel() {
		try {
			return Long.parseLong((String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_MAX_SURFING_LEVEL, "0"));
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	/**
	 * Retrieves the default thread extends to execute the Web Site Pages surfing.
	 * @return The default thread extends
	 */
	public static final Integer getDefultThredExtends() {
		try {
			return Integer.parseInt((String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_PARALLEL_THREADS, "10"));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	/**
	 * Retrieves the default timeout in milliseconds before any surfing thread starts
	 * @return The default anti-intercept timeout in milliseconds
	 */
	public static final Long getDefultAntiInterceptTimeout() {
		try {
			return Long.parseLong((String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_CRAWLER_ANTI_IDENTIFY_TIMEOUT, "1000"));
		} catch (NumberFormatException e) {
			return 1000L;
		}
	}

	/**
	 * Retrieves the default duplicates flag
	 * @return The default duplicates flag
	 */
	public static final Boolean getDefultSkipDuplicates() {
		return Boolean.parseBoolean((String)webCrawlerProperties.getOrDefault(Constants.PROPERTIES_DEFAULT_SKIP_PARTIAL_DIPLICATE_URL_IN_CHILDREN, "true"));
	}
	
	/**
	 * Register a project in the registry. 
	 * This feature at the moment is a simple in memory registry, it will be provided a persistent registry in the future 
	 * to allow the off line execution, the reschedule and the access from remote services.
	 * @param configuration Web Crawler Project Configuration
	 * @return The Project Unique ID
	 */
	public static String regiterWebCrawerProject(WebCrawlerConfiguration configuration) {
		String projectUID = UUID.randomUUID().toString();
		projectsConfigurationMap.put(projectUID, configuration);
		return projectUID;
	}
	
	/**
	 * Retrieves a Web Crawler Project configuration from the registry
	 * @param projectUID Project Unique ID
	 * @return The selected configuration or null i the UID doesn't exists in the registry
	 */
	public static final WebCrawlerConfiguration getProjectConfiguration(String projectUID) {
		return projectsConfigurationMap.get(projectUID);
	}
	
	private static final Set<Class<? extends SitePageFilter>> getFilterClasses() {
		return filterReflections.getSubTypesOf(SitePageFilter.class);
	}

	private static final Set<Class<? extends ResultCollector>> getCollectorClasses() {
		return collectorsReflections.getSubTypesOf(ResultCollector.class);
	}
	
	private static final Set<Class<? extends ResultFormatter>> getFormatterClasses() {
		return formattersReflections.getSubTypesOf(ResultFormatter.class);
	}
	
	/**
	 * Return the list of the available and active site page filters
	 * @param projectUID Project Unique Identifier
	 * @return the list of site page filters
	 * @see SitePageFilter
	 */
	public static final List<SitePageFilter> getFilters(String projectUID) {
		List<SitePageFilter> filters = new ArrayList<SitePageFilter>(0);
		for(Class<? extends SitePageFilter> clazz: getFilterClasses()) {
			try {
				SitePageFilter filter = clazz.newInstance();
				filter.configure(projectUID);
				if (filter.isActive())
					filters.add(filter);
			} catch (InstantiationException|IllegalAccessException e) {
				Logger.error("Error retring class" + clazz, e, WebCrawlerHelper.class);
			}
		}
		return filters;
	}

	/**
	 * Extract a complete ResultCollector, provided of Formatted according to the provided selectors. In case of mistakes 
	 * the system try to provide the default ones.
	 * @param collectorSelector Selector used to retrieve a ResultCollector
	 * @param formatterSelector Selector used to retrieve a ResultFormatter
	 * @return ResultCollector according to the selectors or the retry policies
	 * @see ResultCollector
	 * @see ResultFormatter
	 */
	public static final ResultCollector getCollector(String collectorSelector, String formatterSelector) {
		return getCollector(collectorSelector, formatterSelector, false);
	}
	
	private static final ResultCollector getCollector(String collectorSelector, String formatterSelector, boolean retry) {
		ResultFormatter formatter = null;
		for(ResultFormatter discoveredFormatter: getFormatters()) {
			if (discoveredFormatter.getSelector().equalsIgnoreCase(formatterSelector)) {
				formatter = discoveredFormatter;
				break;
			}
		}
		if (formatter==null) {
			if (!retry)
				return getCollector(collectorSelector, getDefultFormatter(), true);
			else 
				return null;
		}
		ResultCollector collector = null;
		for(ResultCollector discoveredCollector: getCollectors()) {
			if (discoveredCollector.getSelector().equalsIgnoreCase(collectorSelector)) {
				collector = discoveredCollector;
				collector.setFormatter(formatter);
				break;
			}
		}
		if (collector==null) {
			if (!retry) 
				return getCollector(getDefultOutputType(), formatterSelector, true);
			else 
				return null;
		}
		return collector;
	}
	
	/**
	 * Return the list of the available and active collectors
	 * @return the list of collectors
	 * @see ResultCollector
	 */
	public static final List<ResultCollector> getCollectors() {
		List<ResultCollector> collectors = new ArrayList<ResultCollector>(0);
		for(Class<? extends ResultCollector> clazz: getCollectorClasses()) {
			try {
				ResultCollector collector = clazz.newInstance();
				if (collector.isActive())
					collectors.add(collector);
			} catch (InstantiationException|IllegalAccessException e) {
				Logger.error("Error retring class" + clazz, e, WebCrawlerHelper.class);
			}
		}
		return collectors;
	}
	
	/**
	 * Return the list of the available and active formatters
	 * @return the list of formatters
	 * @see ResultFormatter
	 */
	public static final List<ResultFormatter> getFormatters() {
		List<ResultFormatter> formatters = new ArrayList<ResultFormatter>(0);
		for(Class<? extends ResultFormatter> clazz: getFormatterClasses()) {
			try {
				ResultFormatter formatter = clazz.newInstance();
				if (formatter.isActive())
					formatters.add(formatter);
			} catch (InstantiationException|IllegalAccessException e) {
				Logger.error("Error retring class" + clazz, e, WebCrawlerHelper.class);
			}
		}
		return formatters;
	}
	
}
