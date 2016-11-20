package com.web.libraries.webcrawler.api.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;

/**
 * Model class that realize the Visited page base information
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 */
public class WebPage {
	private long level=0L;
	private String fullUrlString;
	private URL url = null;
	private boolean visited;
	private WebPage parent=null;
	private List<WebPage> children=new ArrayList<WebPage>(0);
	
	
	private WebPage(WebPage parent, String fullUrlString, URL url) {
		super();
		this.fullUrlString = fullUrlString;
		this.url = url;
		this.parent = parent;
		if (parent!=null) {
			this.level=parent.getLevel()+1;
			parent.addChildren(this);
		}
	}

	/**
	 * Retrieve the current page depth level in the site hierarchy
	 * @return (int) Site level depth in hierarchy
	 */
	public long getLevel() {
		return level;
	}

	/**
	 * Retrieve the current page URL protocol
	 * @return (String) Page URL protocol
	 */
	public String getProtocol() {
		return url!=null ? url.getProtocol() : "";
	}

	/**
	 * Retrieve the current page URL host name
	 * @return (String) Page URL host name
	 */
	public String getHost() {
		return url!=null ? url.getHost() : "";
	}

	/**
	 * Retrieve the current page URL path
	 * @return (String) Page URL path
	 */
	public String getPath() {
		return url!=null ? url.getPath() : "";
	}

	/**
	 * Retrieve the current page URL port
	 * @return (int) Page URL port
	 */
	public int getPort() {
		return url!=null ? url.getPort() : -1;
	}

	/**
	 * Retrieve the current page full URL
	 * @return (String) Page full URL
	 */
	public String getFullUrlString() {
		return fullUrlString;
	}

	/**
	 * Retrieve the parent page in the site hierarchy
	 * @return (String) Parent Page
	 */
	public WebPage getParent() {
		return parent;
	}

	/**
	 * Retrieve the page children pages in the site hierarchy
	 * @return (List) Children Page List
	 */
	public List<WebPage> getChildren() {
		return children;
	}

	/**
	 * Retrieve the visited flag
	 * @return (boolean) Page visited flag
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Set the visited flag
	 * @param visited Page visited flag
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	private void addChildren(WebPage child) {
		children.add(child);
	}
	
	/**
	 * Retrieve the page site base URL
	 * @return (String) Base site URL
	 */
	public String getSiteBaseURLString() {
		if (this.getPort() != -1) {
		    return String.format("%s://%s", this.getProtocol(), this.getHost());
		} else {
		    return String.format("%s://%s:%d", this.getProtocol(), this.getHost(), this.getPort());
		}
		
	}

	/**
	 * Compares a Web Page with the children ones matching the full URL truncated at the query
	 * string, if present in one or both the comparison sides. 
	 * @param aPage Page to be compared within the children ones
	 * @return (boolean) flag representing if the page is found by URL except the query string
	 */
	public boolean containsPartially(WebPage aPage) {
		boolean contains = false;
		for(WebPage aChildPage: this.children) {
			if (aChildPage.partialUrlMatch(aPage)) {
				contains = true;
				break;
			}
			
		}
		return contains;
	}

	/**
	 * Compares a Web Page with the children ones matching the full URL, if present in one or both the comparison sides. 
	 * @param aPage Page to be compared within the children ones
	 * @return (boolean) flag representing if the page is found by URL except the query string
	 */
	public boolean contains(WebPage aPage) {
		boolean contains = false;
		for(WebPage aChildPage: this.children) {
			if (aChildPage.fullUrlMatch(aPage)) {
				contains = true;
				break;
			}
			
		}
		return contains;
	}

	
	/**
	 * Compares a Web Page with the current one matching the full URL truncated at the query
	 * string, if present in one or both the comparison sides. 
	 * @param childPage Page to be searched in the children
	 * @return (boolean) flag representing if the page is found by URL except the query string
	 */
	protected boolean partialUrlMatch(WebPage childPage) {
		String proposedChildURL = childPage.getFullUrlString();
		String aChildPageURL = this.getFullUrlString();
		
		if (proposedChildURL.indexOf("?")>0)
			proposedChildURL = proposedChildURL.substring(0, proposedChildURL.indexOf("?"));
		if (aChildPageURL.indexOf("?")>0)
			aChildPageURL = aChildPageURL.substring(0, aChildPageURL.indexOf("?"));

		if (aChildPageURL.equals(proposedChildURL)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Compares a Web Page with the current one matching the full URL, if present in one or both the comparison sides. 
	 * @param childPage Page to be searched in the children
	 * @return (boolean) flag representing if the page is found by URL except the query string
	 */
	protected boolean fullUrlMatch(WebPage childPage) {
		String proposedChildURL = childPage.getFullUrlString();

		if (this.getFullUrlString().equals(proposedChildURL)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieve if a Web Page has been visited in the Site Map hierarchy
	 * @param aPage Web Page to search for
	 * @partialMatch State that defines if the match should be on a partial URL
	 * @return the visited state of the page
	 */
	public boolean checkVisitedInHierarchy(WebPage aPage, boolean partialMatch) {
		for(WebPage child: children) {
			if (partialMatch && child.partialUrlMatch(aPage)) {
				return true;
			}
			else if (!partialMatch && child.fullUrlMatch(aPage)) {
				return true;
			}
		}
		if (parent!=null) {
			return parent.checkVisitedInHierarchy(aPage, partialMatch);
		}
		return false;
	}

	/**
	 * Parse a URL ad define a root page
	 * @param url Page URL string
	 * @return (WebPage) Site Root Page
	 * @throws WebCrawlerException URL Parse exception
	 * @see WebPage#fromUrlString(String, WebPage)
	 */
	public static final WebPage fromUrlString(String url) throws WebCrawlerException {
		return fromUrlString(url, null);
	}

	/**
	 * Parse a URL ad define a page
	 * @param url Page URL string
	 * @param parent Parent {@link WebPage} in the site hierarchy
	 * @return (WebPage) Site Page
	 * @throws WebCrawlerException URL Parse exception
	 */
	public static final WebPage fromUrlString(String url, WebPage parent) throws WebCrawlerException {
		try {
			URL urlEncoded = new URL(url);
			return new WebPage(parent, url, urlEncoded);
		} catch (Exception e) {
			throw new WebCrawlerException(e);
		}
	}

}
