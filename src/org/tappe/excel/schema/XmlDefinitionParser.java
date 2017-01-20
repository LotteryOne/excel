package org.tappe.excel.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class XmlDefinitionParser extends AbstractSingleBeanDefinitionParser {
	Logger logger = Logger.getLogger(this.getClass());

	private static final String PropertyIndex = "index";
	private static final String PropertySplitLine = "splitLine";
	private static final String PropertyFileName = "fileName";
	private static final String PropertyId = "id";
	private static final String PropertyPath = "path";

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ToolXmlBean.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {

		String valueId = element.getAttribute(PropertyId);

		if (StringUtils.hasText(valueId)) {
			builder.addPropertyValue(PropertyId, valueId);
		}

		String valueIndex = element.getAttribute(PropertyIndex);

		if (StringUtils.hasText(valueIndex)) {
			builder.addPropertyValue(PropertyIndex, Integer.parseInt(valueIndex));
		}

		String valueSplit = element.getAttribute(PropertySplitLine);

		if (StringUtils.hasText(valueSplit)) {
			builder.addPropertyValue(PropertySplitLine, Integer.parseInt(valueSplit));
		}

		String valuePath = element.getAttribute(PropertyPath);

		if (StringUtils.hasText(valuePath)) {
			String valueFileName = valuePath.substring(valuePath.lastIndexOf("/") + 1);
			builder.addPropertyValue(PropertyFileName, valueFileName);
			builder.addPropertyValue(PropertyPath, valuePath);
			
		}

	}

	@SuppressWarnings("unused")
	private Workbook getExcelTemplate(String templateExcelFilePath) {
		Long initStart = System.currentTimeMillis();
		if (StringUtils.hasText(templateExcelFilePath)) {
			InputStream inputStream = null;
			try {
				inputStream=this.getClass().getClassLoader().getResourceAsStream(templateExcelFilePath);
				Workbook templateBook = WorkbookFactory.create(inputStream);
				logger.info("load excel model: " + templateExcelFilePath);

				return templateBook;
			} catch (Exception e) {
				throw new RuntimeException(
						"read templateExcelFile :[" + templateExcelFilePath + "] exception \n" + e.getMessage());
			} finally {
				if (inputStream != null) {
					try {
						Long endTime = System.currentTimeMillis();
						System.out.println(endTime - initStart+">>>>>");
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

}
