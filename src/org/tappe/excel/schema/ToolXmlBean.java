package org.tappe.excel.schema;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

public class ToolXmlBean implements Cloneable {
	
	private String id;
	
	private Integer index;
	
	private String path;
	
	
	private Integer splitLine;
	
	private String fileName;
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
 
	 

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSplitLine() {
		return splitLine;
	}

	public void setSplitLine(Integer splitLine) {
		this.splitLine = splitLine;
	}
	
	 
	public ToolXmlBean() {
	}

}
