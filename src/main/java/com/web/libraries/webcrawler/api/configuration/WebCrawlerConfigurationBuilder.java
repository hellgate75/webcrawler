package com.web.libraries.webcrawler.api.configuration;

import com.web.libraries.webcrawler.api.WebCrawler;
import com.web.libraries.webcrawler.api.WebCrawlerHelper;

/**
 * Web Crawler Congifuration Builder 
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see WebCrawler
 * @see WebCrawlerConfiguration
 */
public class WebCrawlerConfigurationBuilder {
	private String webSite = WebCrawlerHelper.getDefultWebSite();
	
	private String formatter = WebCrawlerHelper.getDefultFormatter();
	
	private String outputType = WebCrawlerHelper.getDefultOutputType();
	
	private Long maxSurfingLevel = WebCrawlerHelper.getDefultMaxSurfingLevel();
	
	private Integer thredExtends = WebCrawlerHelper.getDefultThredExtends();
	
	private Long interceptTimeout = WebCrawlerHelper.getDefultAntiInterceptTimeout();

	private Boolean skipDuplicates = WebCrawlerHelper.getDefultSkipDuplicates();

	/**
	 * Default Constructor
	 */
	public WebCrawlerConfigurationBuilder() {
		super();
	}
	
	/**
	 * Define the Web Site URL to visit (Default : {@link WebCrawlerHelper#getDefultWebSite()})
	 * @param webSite Web Site URL
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder webSite(String webSite) {
		this.webSite = webSite;
		return this;
	}
	
	/**
	 * Define the Web Site Map Format type (Default : {@link WebCrawlerHelper#getDefultFormatter()})
	 * @param formatter The formatter selector
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder formatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	
	/**
	 * Define the Web Site Map Output device type (Default : {@link WebCrawlerHelper#getDefultOutputType()})
	 * @param outputType The output type selector
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder outputType(String outputType) {
		this.outputType = outputType;
		return this;
	}
	
	/**
	 * Define the Web Site hierarchy descendant max surfing level (Default : {@link WebCrawlerHelper#getDefultMaxSurfingLevel()})
	 * @param maxSurfingLevel Max Site hierarchy descendant surfing level
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder maxSurfingLevel(Long maxSurfingLevel) {
		this.maxSurfingLevel = maxSurfingLevel;
		return this;
	}
	
	/**
	 * Define the Web Site surfing maximum thread extends (Default : {@link WebCrawlerHelper#getDefultThredExtends()})
	 * @param thredExtends Minimum operative threads during the site surfing operations
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder thredExtends(Integer thredExtends) {
		this.thredExtends = thredExtends;
		return this;
	}
	
	/**
	 * Define the Web Site URL duplicates visiting skip status (Default : {@link WebCrawlerHelper#getDefultSkipDuplicates()})
	 * @param skipDuplicates Status of skip partial similar URL in the Web Site hierarchy
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder skipDuplicates(Boolean skipDuplicates) {
		this.skipDuplicates = skipDuplicates;
		return this;
	}
	
	/**
	 * Define the Web Site delay timeout before surfing, this allow to prevent robot site rule blockers (Default : {@link WebCrawlerHelper#getDefultAntiInterceptTimeout()})
	 * @param interceptTimeout Activation of an anti-blocking thread delay, this will delay the project execution
	 * @return the builder instance
	 */
	public WebCrawlerConfigurationBuilder interceptTimeout(Long interceptTimeout) {
		this.interceptTimeout = interceptTimeout;
		return this;
	}
	
	/**
	 * Build and return the {@link WebCrawlerConfiguration}}
	 * @return The Web Crawler Configuration
	 */
	public WebCrawlerConfiguration build() {
		return new WebCrawlerConfiguration(webSite, formatter, outputType, maxSurfingLevel, thredExtends, interceptTimeout, skipDuplicates);
	}

}
