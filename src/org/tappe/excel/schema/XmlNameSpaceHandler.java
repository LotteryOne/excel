package org.tappe.excel.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class XmlNameSpaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("xml", new XmlDefinitionParser());
	}

}
