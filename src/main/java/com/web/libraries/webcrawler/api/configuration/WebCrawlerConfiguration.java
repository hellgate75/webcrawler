package com.web.libraries.webcrawler.api.configuration;

import java.util.LinkedList;
import java.util.List;

public class WebCrawlerConfiguration {

	private String webSite;
	
	private String formatter;
	
	private String outputType;
	
	private Long maxSurfingLevel;
	
	private Integer thredExtends;
	
	private Long interceptTimeout;

	private Boolean skipDuplicates;
	
	private List<String> visitedURLList = new LinkedList<String>();
	
	private long visitedSitePages=0L;

	/**
	 * Default Constructor
	 * @param webSite The current web site used to surf within
	 * @param formatter The current site map output formatter
	 * @param outputType The current output device to export the site map report
	 * @param maxSurfingLevel The maximum level of descendant in the site map hierarchy 
	 * @param thredExtends The number of threads used to extend the reading of the Web Site surfing queue
	 * @param interceptTimeout The milliseconds before to start surfing any web site page
	 * @param skipDuplicates Skip similar URL pages flag
	 */
	protected WebCrawlerConfiguration(String webSite, String formatter, String outputType, long maxSurfingLevel,
			int thredExtends, long interceptTimeout, boolean skipDuplicates) {
		super();
		this.webSite = webSite;
		this.formatter = formatter;
		this.outputType = outputType;
		this.maxSurfingLevel = maxSurfingLevel;
		this.thredExtends = thredExtends;
		this.interceptTimeout = interceptTimeout;
		this.skipDuplicates = skipDuplicates;
	}

	/**
	 * Retrieves the current Web Site to crawl within
	 * @return (String) The Web Site the URL for the Crawling
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * Retrieves the current formatter used to parse the result
	 * @return (String) The formatter
	 */
	public String getFormatter() {
		return formatter;
	}

	/**
	 * Retrieves the current output device to export the result report
	 * @return (String) The Output Type
	 */
	public String getOutputType() {
		return outputType;
	}

	/**
	 * Retrieves the current max surf level
	 * @return (Long) The Max Surfing Level
	 */
	public Long getMaxSurfingLevel() {
		return maxSurfingLevel;
	}

	/**
	 * Retrieves the current thread extends number
	 * @return The number of allowed Thred Extends
	 */
	public Integer getThredExtends() {
		return thredExtends;
	}

	/**
	 * Retrieves the milliseconds timeout before start crawling in a Web Page
	 * @return the sntiInterceptTimeout
	 */
	public Long getInterceptTimeout() {
		return interceptTimeout;
	}

	/**
	 * Retrieves the status to skip a web page with similar URLs
	 * @return The Skip Similar URL Duplicates flag
	 */
	public Boolean getSkipDuplicates() {
		return skipDuplicates;
	}

	/**
	 * Add a visited page to the configuration
	 * @param url the visited URL
	 */
	public void addVisitedURL(String url) {
		if (!this.visitedURLList.contains(url))
			this.visitedURLList.add(url);
	}

	/**
	 * Verify a candidate visited page from the configuration
	 * @param url the candidate visited URL
	 * @return the candidate visited URL state
	 */
	public boolean isVisitedURL(String url) {
		return this.visitedURLList.contains(url);
	}

	/**
	 * Retrieve the list of the visited pages URLs
	 * @return the visited page URLs list
	 */
	public List<String> getVisitedURLs() {
		return this.visitedURLList;
	}
	
	/**
	 * Increment the visited pages counter
	 */
	public void increment() {
		++visitedSitePages;
	}

	/**
	 * Increment the visited pages counter
	 * @param amount increment amount
	 */
	public void increment(long amount) {
		visitedSitePages+=amount;
	}

	/**
	 * Retrieve the visited pages number
	 * @return The visited pages number
	 */
	public long visited() {
		return visitedSitePages;
	}
}
