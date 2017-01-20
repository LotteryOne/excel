package org.tappe.excel.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadXmlInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1799767295087022640L;
	private List<ToolXmlBean> target;

	private static Map<Integer, ToolXmlBean> workMap = new HashMap<Integer, ToolXmlBean>();

	public void setTarget(List<ToolXmlBean> target) {
		this.target = target;
		if (target != null) {
			for (ToolXmlBean toolXmlBean : target) {
				workMap.put(toolXmlBean.getIndex(), toolXmlBean);
			}
		}
	}

	public static Map<Integer, ToolXmlBean> getData() {
		return workMap;
	}

}
