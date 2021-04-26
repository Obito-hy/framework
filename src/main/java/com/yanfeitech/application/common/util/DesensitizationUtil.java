package com.yanfeitech.application.common.util;

import org.apache.commons.lang3.StringUtils;

import com.yanfeitech.application.common.constant.CommonRegex;

/**
 * 
 * <p>
 * Title: DesensitizationUtil
 * </p>
 * <p>
 * Description: 数据脱敏工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class DesensitizationUtil {

	/**
	 * 姓名脱敏
	 *
	 * @param fullName
	 * @param index    1 为第index位开始脱敏
	 * @return
	 */
	public static String name(String fullName) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		}
		String name = StringUtils.left(fullName, 1);
		return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
	}

	/**
	 * 手机号脱敏
	 * 
	 * @param email
	 * @return
	 */
	public static String phone(String phone) {
		if (StringUtils.isEmpty(phone) || (phone.length() != 11)) {
			return phone;
		}
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	}

	/**
	 * 身份证脱敏
	 * 
	 * @param email
	 * @return
	 */
	public static String idCard(String idNumber) {
		if (StringUtils.isNotBlank(idNumber)) {
			if (idNumber.length() == 15) {
				idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
			}
			if (idNumber.length() == 18) {
				idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
			}
		}
		return idNumber;
	}

	public static String address(String address) {
		if (StringUtils.isNotBlank(address)) {
			return StringUtils.left(address, 3).concat(StringUtils.removeStart(StringUtils.leftPad(
					StringUtils.right(address, address.length() - 11), StringUtils.length(address), "*"), "***"));
		}
		return address;
	}

	/**
	 * 邮箱脱敏
	 * 
	 * @param email
	 * @return
	 */
	public static String email(String email) {
		if (!CommonUtil.checkText(email, CommonRegex.EMAIL)) {
			return email;
		}
		email = email.replaceAll("(^\\w{3})[^@]*(@.*$)", "$1****$2");
		return email;
	}

	/**
	 * 110****58，前面保留3位明文，后面保留2位明文
	 *
	 * @param name
	 * @param index 3
	 * @param end   2
	 * @return
	 */
	public static String around(String name, int index, int end) {
		if (StringUtils.isBlank(name)) {
			return "";
		}
		return StringUtils.left(name, index).concat(StringUtils
				.removeStart(StringUtils.leftPad(StringUtils.right(name, end), StringUtils.length(name), "*"), "***"));
	}

	/**
	 * 后四位，其他隐藏<例子：****1234>
	 *
	 * @param num
	 * @return
	 */
	public static String right(String num, int end) {
		if (StringUtils.isBlank(num)) {
			return "";
		}
		return StringUtils.leftPad(StringUtils.right(num, end), StringUtils.length(num), "*");
	}
}
