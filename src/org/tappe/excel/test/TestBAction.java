package org.tappe.excel.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tappe.excel.schema.ExcelTool;

import com.wonders.UserBean;

@Controller
@Scope("prototype")
@RequestMapping("/tbt")
public class TestBAction {
	@Autowired
	private ExcelTool excelTool;

	@RequestMapping(value = "/cc")
	public String testDown(HttpServletResponse reponse, HttpServletRequest request) throws IOException {

		UserBean title = getUser();
		List<UserBean> data = getList();
		int index = 2;
		excelTool.export(title, data, index, reponse);

		return null;

	}

	public UserBean getUser() {
//		UserBean bean = new UserBean("长宁", "tom", "123123", new Date(), "男");
		UserBean bean = new UserBean("哈哈哈哈", "ccccccc", "123123", new Date(), "男");
		return bean;
	}

	public List<UserBean> getList() {
		List<UserBean> data = new ArrayList<UserBean>();
		for (int i = 0; i < 10000; i++) {
			data.add(new UserBean("长宁ccccc", "tomas" + i, "123123" + i, new Date(), "男"));
		}
		return data;
	}

	@RequestMapping(value = "/jc")
	@ResponseBody
	public TestBean testJson() {
//		TestBean tb = new TestBean("Tom", "addresssssss", "hawad");
		return null;
	}
	
	public static void main(String[] args) {
		double b=16925251.11;
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(b));
	}

}
