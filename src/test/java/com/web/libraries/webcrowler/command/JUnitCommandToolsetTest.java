package com.web.libraries.webcrowler.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.SystemUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;

import com.web.libraries.webcrawler.api.WebCrawlerHelper;
import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfiguration;
import com.web.libraries.webcrawler.api.configuration.WebCrawlerConfigurationBuilder;
import com.web.libraries.webcrawler.api.controller.ResultCollector;
import com.web.libraries.webcrawler.api.controller.ResultFormatter;
import com.web.libraries.webcrawler.api.controller.SitePageFilter;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;
import com.web.libraries.webcrawler.command.collectors.StandarOutputResultCollector;
import com.web.libraries.webcrawler.command.filters.VisitedFilter;
import com.web.libraries.webcrawler.command.formatters.PlainTextFormatter;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnitCommandToolsetTest {

	@Test
	public void testStandardOutputResultCollector() throws Exception {
		ResultFormatter formatter = new PlainTextFormatter();
		ResultCollector collector = new StandarOutputResultCollector();
		collector.setFormatter(formatter);
		WebPage parentPage = WebPage.fromUrlString("http://www.facebook.com");
		WebPage childPage = WebPage.fromUrlString("http://www.google.com?s=java", parentPage);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        collector.exportPage(parentPage);
        String printed = new String(bo.toByteArray(), "utf-8");
        assertEquals("Web Parent Page must be well exported", parentPage.getFullUrlString() + (SystemUtils.IS_OS_WINDOWS ? "\r" : "")+"\n" + "  [-] http://www.google.com?s=java (NOT VISITED)" + (SystemUtils.IS_OS_WINDOWS ? "\r" : "") + "\n", printed );
        bo.reset();
        collector.exportPage(childPage);
        printed = new String(bo.toByteArray(), "utf-8");
        assertEquals("Web Parent Page must be well exported", "  [-] http://www.google.com?s=java (NOT VISITED)" + (SystemUtils.IS_OS_WINDOWS ? "\r" : "") + "\n", printed );
        bo.reset();
        childPage.setVisited(true);
        collector.exportPage(childPage);
        printed = new String(bo.toByteArray(), "utf-8");
        assertEquals("Web Parent Page must be well exported", "  [-] http://www.google.com?s=java (VISITED)" + (SystemUtils.IS_OS_WINDOWS ? "\r" : "") + "\n", printed );

	}
	@Test
	public void testPlainTextFormatter() throws WebCrawlerException {
		ResultFormatter formatter = new PlainTextFormatter();
		WebPage parentPage = WebPage.fromUrlString("http://www.facebook.com");
		WebPage childPage = WebPage.fromUrlString("http://www.google.com?s=java", parentPage);
		assertEquals("Web Parent Page must be well formatted", parentPage.getFullUrlString(), formatter.format(parentPage));
		assertEquals("Web Child Page must be well formatted", "  [-] http://www.google.com?s=java (NOT VISITED)", formatter.format(childPage));
		childPage.setVisited(true);
		assertEquals("Web Child Page must be well formatted when VISITED", "  [-] http://www.google.com?s=java (VISITED)", formatter.format(childPage));
		//assertTrue("A valid candidate must ");
	}
	@Test
	public void testASkipDuplicatesVisitedFilter() throws WebCrawlerException {
		WebCrawlerConfiguration config = new WebCrawlerConfigurationBuilder().webSite("http://www.facebook.com").skipDuplicates(true).build();
		String projectID = WebCrawlerHelper.regiterWebCrawerProject(config);
		SitePageFilter filter = new VisitedFilter();
		filter.configure(projectID);
		WebPage rootPage = WebPage.fromUrlString("http://www.facebook.com");
		config.addVisitedURL("http://www.facebook.com");
		config.addVisitedURL("http://www.google.com");
		WebPage candidatePage1 = WebPage.fromUrlString("http://www.tweetter.com");
		WebPage candidatePage2 = WebPage.fromUrlString("http://www.google.com?s=java");
		assertTrue("A valid candidate must match the visited site", filter.accept(candidatePage1, rootPage));
		assertFalse("A NOT valid candidate must not match the visited site", filter.accept(candidatePage2, rootPage));
	}
	@Test
	public void testANoSkipDupliacatesVisitedFilter() throws WebCrawlerException {
		WebCrawlerConfiguration config = new WebCrawlerConfigurationBuilder().webSite("http://www.facebook.com").skipDuplicates(false).build();
		String projectID = WebCrawlerHelper.regiterWebCrawerProject(config);
		SitePageFilter filter = new VisitedFilter();
		filter.configure(projectID);
		WebPage rootPage = WebPage.fromUrlString("http://www.facebook.com");
		config.addVisitedURL("http://www.facebook.com");
		config.addVisitedURL("http://www.google.com");
		WebPage candidatePage1 = WebPage.fromUrlString("http://www.tweetter.com");
		WebPage candidatePage2 = WebPage.fromUrlString("http://www.google.com?s=java");
		assertTrue("A valid candidate must match the visited site", filter.accept(candidatePage1, rootPage));
		assertTrue("A NOT valid candidate must not match the visited site", filter.accept(candidatePage2, rootPage));
	}
}
