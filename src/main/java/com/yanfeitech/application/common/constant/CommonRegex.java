package com.yanfeitech.application.common.constant;

/**
 * 
 * <p>
 * Title: CommonRegex
 * </p>
 * <p>
 * Description: 常用正则表达式
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class CommonRegex {
	// 用户登录密码
	public static final String PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
	// 座机号码
	public static final String LANDLINE = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
	// 移动手机号，不需要输+86，只允许中国用户
	public static final String MOBILE_PHONE = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}";
	// 电子邮件
	public static final String EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	// 中国邮政编码（六位数字）
	public static final String MAIL = "[1-9]\\d{5}(?!\\d)";
	// 网页链接 HTTPS
	public static final String WEBSITE_URL_HTTPS = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
	// 网页链接 HTTP
	public static final String WEBSITE_URL_HTTP = "(http?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
	// 域名
	public static final String WEBSITE_HOST = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?";
	// 身份证
	public static final String ID_CARD = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
	// 中文
	public static final String CHINESE = "[\\u4e00-\\u9fa5]+";
	// 日期
	public static final String DATE = "\\d{4}-\\d{2}-\\d{2}";
	// 统一社会信用代码
	public static final String USCC = "^[0-9A-Z]{2}[0-9]{6}[0-9A-Z]{10}$";
}
