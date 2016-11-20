
package com.web.libraries.webcrawler.command.collectors;

import com.web.libraries.webcrawler.api.controller.ResultCollector;
import com.web.libraries.webcrawler.api.controller.ResultFormatter;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;
import com.web.libraries.webcrawler.api.utils.Logger;

/**
 * Collector that prints the formatted Web Site Page result on the standard output
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see ResultCollector
 */
public class StandarOutputResultCollector implements ResultCollector {
	
	private ResultFormatter formatter;

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.ResultCollector#exportPage(com.web.libraries.webcrawler.api.model.WebPage)
	 */
	@Override
	public void exportPage(WebPage content) throws WebCrawlerException {
		try {
			System.out.println(formatter.format(content));
			for(WebPage child: content.getChildren()) {
				exportPage(child);
			}
		} catch (final Exception exception) {
			Logger.error("Exception during the export of a site ...", exception, StandarOutputResultCollector.class);
			throw new  WebCrawlerException("Exception during the export of a site ...", exception);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.ResultCollector#setFormatter(com.web.libraries.webcrawler.api.controller.ResultFormatter)
	 */
	@Override
	public void setFormatter(ResultFormatter formatter) {
		this.formatter = formatter;
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#getSelector()
	 */
	@Override
	public String getSelector() {
		return "stdout";
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Standard Output Writer";
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#isActive()
	 */
	@Override
	public Boolean isActive() {
		return Boolean.TRUE;
	}
	

}
