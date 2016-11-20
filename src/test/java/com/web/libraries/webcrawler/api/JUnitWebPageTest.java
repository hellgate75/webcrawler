package com.web.libraries.webcrawler.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;

import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnitWebPageTest {

	@Test
	public void testRootWebPageAccessor() throws WebCrawlerException {
		WebPage aPage = WebPage.fromUrlString("http://www.google.com");
		assertEquals(aPage.getFullUrlString(), "http://www.google.com");
		assertEquals("Protocol must be the same that the original", "http", aPage.getProtocol());
		assertEquals("Port must be -1 (not assigned)", -1, aPage.getPort());
		assertEquals("Host name must be the same that the original", "www.google.com", aPage.getHost());
		assertNull("Parent of a root page must be null", aPage.getParent());
	}
	@Test
	public void testWebPageAccessor() throws WebCrawlerException {
		WebPage rootPage = WebPage.fromUrlString("http://www.facebook.com");
		WebPage childPage = WebPage.fromUrlString("http://www.google.com", rootPage);
		assertEquals(childPage.getFullUrlString(), "http://www.google.com");
		assertEquals("Protocol must be the same that the original", "http", childPage.getProtocol());
		assertEquals("Port must be -1 (not assigned)", -1, childPage.getPort());
		assertEquals("Host name must be the same that the original", "www.google.com", childPage.getHost());
		assertEquals("Parent of a root page must be facebook.com", rootPage, childPage.getParent());
	}
	@Test
	public void testWebPageContainsAndContainsPartially() throws WebCrawlerException {
		WebPage rootPage = WebPage.fromUrlString("http://www.facebook.com");
		WebPage childPage = WebPage.fromUrlString("http://www.google.com?s=java", rootPage);
		assertEquals(childPage.getFullUrlString(), "http://www.google.com?s=java");
		assertEquals("Parent of a root page must be google.com", rootPage, childPage.getParent());
		assertTrue(rootPage.containsPartially(WebPage.fromUrlString("http://www.google.com?s=junit")));
		assertFalse(rootPage.contains(WebPage.fromUrlString("http://www.google.com?s=junit")));
	}
	@Test
	public void testWebPageCheckVisitedInHierarchy() throws WebCrawlerException {
		WebPage rootPage = WebPage.fromUrlString("http://www.facebook.com");
		WebPage childPage = WebPage.fromUrlString("http://www.google.com?s=java", rootPage);
		assertEquals(childPage.getFullUrlString(), "http://www.google.com?s=java");
		assertEquals("Parent of a root page must be google.com", rootPage, childPage.getParent());
		assertTrue(childPage.checkVisitedInHierarchy(WebPage.fromUrlString("http://www.google.com?s=junit"), true));
		assertFalse(childPage.checkVisitedInHierarchy(WebPage.fromUrlString("http://www.google.com?s=junit"), false));
	}
}
