package com.web.libraries.webcrawler.command.formatters;

import com.web.libraries.webcrawler.api.controller.ResultFormatter;
import com.web.libraries.webcrawler.api.exception.WebCrawlerException;
import com.web.libraries.webcrawler.api.model.WebPage;
import com.web.libraries.webcrawler.api.utils.Logger;

/**
 * Formtter that formats the Web Site Page for the export
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 * @see ResultFormatter
 */
public class PlainTextFormatter implements ResultFormatter {

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.ResultFormatter#format(com.web.libraries.webcrawler.api.model.WebPage)
	 */
	@Override
	public Object format(WebPage content) throws WebCrawlerException {
		try {
			if(content!=null) {
				StringBuffer buffer = new StringBuffer();
				if (content.getLevel()==0)
					buffer.append(content.getFullUrlString());
				else {
					for(int i=0;i<content.getLevel();i++)
						buffer.append("  ");
					buffer.append("[-] ");
					buffer.append(content.getFullUrlString());
					buffer.append(" ("+(content.isVisited() ? "" : "NOT ") + "VISITED)");
				}
				return buffer.toString();
			}
			return "";
		} catch (Exception exception) {
			Logger.error("Exception during the format of a site ...", exception, PlainTextFormatter.class);
			throw new  WebCrawlerException("Exception during the format of a site ...", exception);
		}
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#getSelector()
	 */
	@Override
	public String getSelector() {
		return "plain";
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Plain Text Site Map Format";
	}

	/* (non-Javadoc)
	 * @see com.web.libraries.webcrawler.api.controller.CommandParameter#isActive()
	 */
	@Override
	public Boolean isActive() {
		return Boolean.TRUE;
	}

}
