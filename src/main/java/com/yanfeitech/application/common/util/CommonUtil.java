package com.yanfeitech.application.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p>
 * Title: CommonUtil
 * </p>
 * <p>
 * Description: 常用工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class CommonUtil {

	public static Date getCurrentTime() {
		// 消除数据库毫秒进位导致的差异
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return simpleDateFormat
					.parse(simpleDateFormat.format(new Date((System.currentTimeMillis() / 1000) * 1000)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getCurrentDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat
					.parse(simpleDateFormat.format(new Date((System.currentTimeMillis() / 1000) * 1000)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCurrentTimeString() {
		return dateToTimeString(new Date((System.currentTimeMillis() / 1000) * 1000));
	}

	public static String getCurrentDateString() {
		return dateToDayString(new Date((System.currentTimeMillis() / 1000) * 1000));
	}

	public static Date getYesterdayDate() {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	public static Date getNextDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static Date getDateByDay(Date date, Integer day) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	public static Date getNextMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}

	public static String dateToTimeString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public static String dateToDayString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	public static String dateToDaySqlString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return "\'" + simpleDateFormat.format(date) + "\'";
	}

	public static String dateToDayChineseString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return simpleDateFormat.format(date);
	}

	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomNumberString(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public static List<Integer> stringConvertIntList(String value) {
		List<Integer> result = new ArrayList<Integer>();
		if (StringUtils.isBlank(value)) {
			return result;
		} else {
			String[] valueArr = value.split(",");
			for (int i = 0; i < valueArr.length; i++) {
				result.add(Integer.parseInt(valueArr[i]));
			}
		}
		return result;
	}

	public static List<String> stringConvertStringList(String value) {
		List<String> result = new ArrayList<String>();
		if (StringUtils.isBlank(value)) {
			return result;
		} else {
			String[] valueArr = value.split(",");
			for (int i = 0; i < valueArr.length; i++) {
				result.add(valueArr[i]);
			}
		}
		return result;
	}

	public static String listToString(List<String> list) {
		return StringUtils.join(list.toArray(), ",");
	}

	public static boolean checkText(String text, String regex) {
		if (StringUtils.isBlank(text)) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(text);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	public static String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> ObjectCovertToMap(Object object) {
		return JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
	}

	@SuppressWarnings("deprecation")
	public static String toChinese(int number) {
		String list[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
		List<String> chinList = Arrays.asList(list);
		if (number <= 10) {
			{
				return chinList.get(number - 1);
			}
		} else if (number <= 100) {
			if (number < 20) {
				return "十" + chinList.get((number % 10) - 1);
			}
			if (number % 10 == 0) {
				return chinList.get(new Double(Math.floor(number / 10)).intValue() - 1) + "十";
			} else {
				return chinList.get(new Double(Math.floor(number / 10)).intValue() - 1) + "十"
						+ chinList.get((number % 10) - 1);
			}
		}
		return null;
	}
}
