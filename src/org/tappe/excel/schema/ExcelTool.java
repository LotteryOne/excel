package org.tappe.excel.schema;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelTool {

	private Logger logger = Logger.getLogger(this.getClass());

	private static Map<Integer, ToolXmlBean> template = LoadXmlInfo.getData();

	private static DecimalFormat df = new DecimalFormat("0.00");

	private static String dataFormat = "yyyy-MM-dd";

	private Workbook book;

	private static SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);

	public static void setDataFormat(String dataFormat) {
		ExcelTool.sdf = new SimpleDateFormat(dataFormat);
	}

	private static CellStyle cellStyle = null;
	private static String stringCode = "UTF-8";

	public static void setEncodeType(String code) {
		stringCode = code;
	}

	public static void setCellStyle(CellStyle style) {
		cellStyle = style;
	}

	private static String exp = "(\\{\\s*(\\w*[.]{0,1})\\w+\\s*\\})";
	private static Pattern pattern = Pattern.compile(exp);
	private static String blank = "";

	private Map<String, Object> titleMap = new HashMap<String, Object>();

	private Map<Integer, Object> bodyMap = new HashMap<Integer, Object>();

	public ByteArrayOutputStream export(Object title, List<?> data, int index) {

		ToolXmlBean bean = template.get(index);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			init(title, data, bean);
			setData(title, data, bean);
			book.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;

	}

	private void init(Object title, List<?> data, ToolXmlBean bean) {
		long st1 = System.currentTimeMillis();

		try {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(bean.getPath());
			book = WorkbookFactory.create(stream);
		} catch (Exception e1) {
			logger.error(e1.getMessage() + "\t" + e1.getCause());
			e1.printStackTrace();
		}
		long st2 = System.currentTimeMillis();
		System.out.println((st2 - st1) + "ms ::: init time");

		if (title != null) {
			Field[] fieldTiles = title.getClass().getDeclaredFields();
			for (Field field : fieldTiles) {
				try {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), title.getClass());
					Method readMethod = pd.getReadMethod();
					Object value = readMethod.invoke(title);
					titleMap.put(field.getName(), valueToString(pd.getReadMethod(), value));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			}
		}

		if (bean.getPath() != null) {
			Row row = book.getSheetAt(0).getRow(bean.getSplitLine());
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null && hasField(cell)) {
					bodyMap.put(i, getFieldExp(cell.getStringCellValue()));
					cell.setCellValue(blank);
				}

			}

		}

	}

	private String valueToString(Method method, Object obj) {
		if (obj == null)
			return "";

		Class<?> getClass = method.getReturnType();

		if (String.class.equals(getClass)) {
			return (String) obj;
		}
		if (Long.class.equals(getClass) || Integer.class.equals(getClass) || Boolean.class.equals(getClass)) {
			return obj.toString();
		}
		if (Double.class.equals(getClass)) {
			return convertDouble((Double) obj).toString();
		}
		if (Date.class.equals(getClass)) {
			return sdf.format(obj);
		} else {
			return obj + blank;
		}

	}

	private String valueToString(Object obj) {

		if (obj == null) {
			return blank;
		}

		Class<?> getClass = obj.getClass();
		if (String.class.equals(getClass)) {
			return (String) obj;
		}
		if (Long.class.equals(getClass) || Integer.class.equals(getClass) || Boolean.class.equals(getClass)) {
			return obj.toString();
		}
		if (Double.class.equals(getClass)) {
			return convertDouble((Double) obj).toString();
		}
		if (Date.class.equals(getClass)) {
			return sdf.format(obj);
		} else {
			return obj + blank;
		}

	}

	private Double convertDouble(Double value) {
		long l1 = Math.round(value * 100);
		Double ret = l1 / 100.0;
		return ret;
	}

	private Object getFieldExp(String stringCellValue) {
		int point;
		String half = "\\w";
		if ((point = stringCellValue.indexOf(".")) > 0) {
			half = stringCellValue.substring(point + 1, stringCellValue.indexOf("}")).trim();
		} else {
			half = stringCellValue.substring(stringCellValue.indexOf("{") + 1, stringCellValue.indexOf("}")).trim();

		}
		// inField.put(half, "(\\{\\s*(\\w*[.]{0,1})(" + half + ")\\s*\\})");
		return half;
	}

	private String getField(String orglStr) {
		int point = orglStr.indexOf(".");
		if (point > 0) {
			orglStr = orglStr.substring(point + 1, orglStr.indexOf("}")).trim();
		} else {
			orglStr = orglStr.substring(orglStr.indexOf("{") + 1, orglStr.indexOf("}")).trim();
		}
		return orglStr;
	}

	private boolean hasField(Cell cell) {
		String value = cell.getStringCellValue();
		if (value == null || blank.equals(value)) {
			return false;
		}
		Matcher matcher = pattern.matcher(value);

		return matcher.find();
	}

	private void setData(Object title, List<?> data, ToolXmlBean bean) {
		long st1 = System.currentTimeMillis();
		if (bean == null || bean.getPath() == null)
			return;

		int line = bean.getSplitLine();
		Sheet sheet = book.getSheetAt(0);
		// setTitle
		for (int i = 0; i < line; i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				if (cell != null) {
					replaceValue(cell);
				}
			}
		}

		// setBody
		if (data != null && data.size() > 0) {

			Field[] fields = data.get(0).getClass().getDeclaredFields();
			Map<String, Method> fieldMap = new HashMap<String, Method>();
			try {
				for (Field field : fields) {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), data.get(0).getClass());
					fieldMap.put(field.getName(), pd.getReadMethod());

				}
				for (int i = line; i < data.size() + line; i++) {
					Row row = sheet.createRow(i);
					for (Integer index : bodyMap.keySet()) {
						Cell cell = row.createCell(index);
						Object dataObj = data.get(i - line);
						String fieldName = (String) bodyMap.get(index);
						Method readMethod = fieldMap.get(fieldName);
						String value = valueToString(readMethod, readMethod.invoke(dataObj));
						// put list data
						cell.setCellValue(value);
						cell.setCellStyle(cellStyle);

					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		long st2 = System.currentTimeMillis();
		System.out.println((st2 - st1) + "ms :::: setData time!!!");

	}

	private void replaceValue(Cell cell) {
		if (hasField(cell)) {
			String field = getField(cell.getStringCellValue());
			Object value = titleMap.get(field);
			if (value == null) {
				value = blank;
			}
			String newValue = cell.getStringCellValue().replaceAll("(\\{\\s*(\\w*[.]{0,1})(" + field + ")\\s*\\})",
					(String) value);

			cell.setCellValue(newValue);
		}

	}

	public void exportByMap(Object title, List<Map<?, ?>> data, int index, HttpServletResponse response)
			throws IOException {

		ToolXmlBean bean = template.get(index);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Long initTime = System.currentTimeMillis();
			init(title, data, bean);
			setMapData(title, data, bean);
			book.write(os);
			Long endTime = System.currentTimeMillis();
			logger.info("export excel in " + (df.format((endTime - initTime) * 1.0 / 1000)) + " S ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setContentLength(os.size());
		response.setCharacterEncoding(stringCode);
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + URLEncoder.encode(bean.getFileName(), stringCode));
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(os.toByteArray());
		outputStream.close();
	}

	private void setMapData(Object title, List<Map<?, ?>> data, ToolXmlBean bean) {
		long st1 = System.currentTimeMillis();
		if (bean == null || bean.getPath() == null)
			return;

		int line = bean.getSplitLine();
		Sheet sheet = book.getSheetAt(0);
		// setTitle
		for (int i = 0; i < line; i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				if (cell != null) {
					replaceValue(cell);
				}
			}
		}

		// setBody
		if (data != null && data.size() > 0) {

			Field[] fields = data.get(0).getClass().getDeclaredFields();
			Map<String, Method> fieldMap = new HashMap<String, Method>();
			try {
//				for (Field field : fields) {
//					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), data.get(0).getClass());
//					fieldMap.put(field.getName(), pd.getReadMethod());
//
//				}
				for (int i = line; i < data.size() + line; i++) {
					Row row = sheet.createRow(i);
					Map<?, ?> rowsData = data.get(i-line);
					for (Integer index : bodyMap.keySet()) {
						Cell cell = row.createCell(index);
						String fieldName = (String) bodyMap.get(index);
						Object value = rowsData.get(fieldName);
						String stringValue = valueToString(value);
						cell.setCellValue(stringValue);
						cell.setCellStyle(cellStyle);

					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		long st2 = System.currentTimeMillis();
		System.out.println((st2 - st1) + "ms :::: setData time!!!");
	}

	public void export(Object title, List<?> data, int index, HttpServletResponse response) throws IOException {

		ToolXmlBean bean = template.get(index);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Long initTime = System.currentTimeMillis();
			init(title, data, bean);
			setData(title, data, bean);
			book.write(os);
			Long endTime = System.currentTimeMillis();
			logger.info("export excel in " + (df.format((endTime - initTime) * 1.0 / 1000)) + " S ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setContentLength(os.size());
		response.setCharacterEncoding(stringCode);
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + URLEncoder.encode(bean.getFileName(), stringCode));
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(os.toByteArray());
		outputStream.close();
	}

	public static String PropertyStr(Class<?> beanClass) {

		Field[] fields = beanClass.getDeclaredFields();
		String result = blank;
		for (Field field : fields) {
			result += "{" + field.getName() + "} \t";
		}

		return result;

	}

}
