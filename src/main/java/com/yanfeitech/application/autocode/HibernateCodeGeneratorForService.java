package com.yanfeitech.application.autocode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

/**
 * 
 * <p>
 * Title: HibernateCodeGeneratorForService
 * </p>
 * <p>
 * Description: 根据数据库表，自动生成service代码，运行即可在桌面获得
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class HibernateCodeGeneratorForService {

	private static final String LINE = "\r\n";
	private static final String TAB = "\t";
	private static final String SPACE = " ";
	private static final String SEMICOLON = ";";
	private static Map<String, String> map;
	private String tableName;

	static {
		map = new HashMap<String, String>();
		map.put("char", "String");
		map.put("varchar", "String");
		map.put("varchar2", "String");
		map.put("nvarchar", "String");
		map.put("uniqueidentifier", "String");
		map.put("int", "Integer");
		map.put("number", "Long");
		map.put("bigint", "Long");
		map.put("float", "BigDecimal");
		map.put("double", "BigDecimal");
		map.put("decimal", "BigDecimal");
		map.put("date", "Date");
		map.put("datetime", "Date");
		map.put("timestamp", "Date");
		map.put("time", "Date");
		map.put("tinyint", "Boolean");
		map.put("clob", "String");
	}

	public HibernateCodeGeneratorForService() {

	}

	public HibernateCodeGeneratorForService(String tableName) {
		this.tableName = tableName;
	}

	public static String getPojoType(String dataType) {
		String tmp = dataType.toLowerCase();
		return map.get(tmp);
	}

	// 示例
	public static void main(String[] args) {
		// 生成一批表的实体类（使用通配符%）
		HibernateCodeGeneratorForService hibernateCodeGenerator = new HibernateCodeGeneratorForService();
		hibernateCodeGenerator.generatorEntities("%");
	}

	/**
	 * 生成一批表的实体类（使用通配符%）
	 *
	 * @param tableNamePattern 匹配表名的字符串（可以使用通配符%）
	 */
	public void generatorEntities(String tableNamePattern) {
		List<String> tableNames = DatabaseUtil.getTableNames(DatabaseUtil.URL, DatabaseUtil.USERNAME,
				DatabaseUtil.PASSWORD, tableNamePattern);
		for (String tableName : tableNames) {
			System.out.println(tableName);
			HibernateCodeGeneratorForService hibernateCodeGenerator = new HibernateCodeGeneratorForService(tableName);
			hibernateCodeGenerator.generatorEntity();
		}
	}

	/**
	 * 生成一个表的实体类
	 */
	public void generatorEntity() {
		String entityName = getEntityName();
		String daoName = entityName + "Dao";
		String serviceName = entityName + "Service";
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String path = desktopPath + "/service/" + serviceName + ".java";
		String entityNameForParam = getEntityNameForParam(entityName);
		String daoNameForParam = entityNameForParam + "Dao";
		String entityNameForParamList = getEntityNameForParamList(entityName);
		File file = new File(path);
		System.out.println(file.getParentFile());
		if (!file.getParentFile().exists()) {
			boolean result = file.getParentFile().mkdirs();
			if (!result) {
				System.out.println("路径创建失败");
			}
		}
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			file.createNewFile();
			os = new FileOutputStream(file, false);
			osw = new OutputStreamWriter(os, "utf-8");
			bw = new BufferedWriter(osw);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			StringBuilder sb = new StringBuilder();
			sb.append(getImports(entityName, daoName, serviceName));
			sb.append(LINE);
			sb.append("/**").append(LINE).append(SPACE).append("*").append(SPACE).append(entityName).append("的Service")
					.append(LINE).append(SPACE).append("*").append(SPACE).append(LINE).append(SPACE).append("*")
					.append(SPACE).append("@version 1.0.0").append(LINE).append(SPACE).append("*").append(SPACE)
					.append("@since").append(SPACE).append(sdf.format(new Date())).append(LINE).append(SPACE)
					.append("*").append(LINE).append(SPACE).append("*/");
			sb.append(LINE);
			sb.append(LINE);
			sb.append(getClassAnnoation(daoName));
			sb.append("public class ").append(serviceName).append(" {");
			sb.append(LINE);

			sb.append(LINE).append(TAB);
			sb.append("@Autowired");
			sb.append(LINE).append(TAB);
			sb.append("private ").append(daoName).append(SPACE).append(daoNameForParam).append(SEMICOLON);

			sb.append(LINE);

			// 自动生成查询单个实体的方法（根据ID）
			sb.append(LINE).append(TAB);
			sb.append("public ").append(entityName).append(SPACE).append("find(String id) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return ").append(daoNameForParam).append(".get(id);");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成查询所有实体的方法（不分页，默认查询isDeleted=1的数据）
			sb.append(LINE).append(TAB);
			sb.append("public List<").append(entityName).append("> findAll() {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("List<").append(entityName).append("> ").append(entityNameForParamList).append(" = ")
					.append(daoNameForParam).append(".selectAll();");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return ").append(entityNameForParamList).append(";");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成查询多个实体的方法（分页查询）
			sb.append(LINE).append(TAB);
			sb.append("public PageResult<").append(entityName).append("> findAllForPage(int pageNo, int pageSize) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("PageResult<").append(entityName).append("> ").append(entityNameForParamList).append(" = ")
					.append("new PageResult<>();");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(entityNameForParamList).append(" = ").append(daoNameForParam)
					.append(".selectAllForPage(pageNo, pageSize);");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return ").append(entityNameForParamList).append(";");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成新增单个实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void save(").append(entityName).append(SPACE).append(entityNameForParam).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(daoNameForParam).append(".insert(").append(entityNameForParam).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成批量新增实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void save(List<").append(entityName).append(">").append(SPACE)
					.append(entityNameForParamList).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(daoNameForParam).append(".insert(").append(entityNameForParamList).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成更新单一实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void modify(").append(entityName).append(SPACE).append(entityNameForParam).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(daoNameForParam).append(".update(").append(entityNameForParam).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成批量更新实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void modify(List<").append(entityName).append(">").append(SPACE)
					.append(entityNameForParamList).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(daoNameForParam).append(".update(").append(entityNameForParamList).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成删除单个实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void delete(String id) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(daoNameForParam).append(".delete(id);");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE).append(LINE);
			sb.append("}");
			// bw.write(LINE);
			bw.write(sb.toString());
			System.out.println(this.tableName + "created successfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != bw) {
				try {
					bw.flush();
					osw.flush();
					os.flush();

					bw.close();
					osw.close();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 实体类的import部分
	 *
	 * @return 实体类的import内容
	 */
	private String getImports(String entityName, String daoName, String serviceName) {
		StringBuilder sb = new StringBuilder();
		// 处理下划线情况，把下划线后一位的字母变大写；
		sb.append("package").append(SPACE).append(DatabaseUtil.PACKAGE).append(".service").append(SEMICOLON)
				.append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("java.util.List").append(SEMICOLON).append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.transaction.annotation.Transactional")
				.append(SEMICOLON).append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.transaction.annotation.Isolation")
				.append(SEMICOLON).append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.transaction.annotation.Propagation")
				.append(SEMICOLON).append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.beans.factory.annotation.Autowired")
				.append(SEMICOLON).append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.stereotype.Service").append(SEMICOLON)
				.append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append(DatabaseUtil.PACKAGE + ".dao.").append(daoName).append(SEMICOLON)
				.append(LINE);
		sb.append("import").append(SPACE).append(DatabaseUtil.PACKAGE + ".entity.").append(entityName).append(SEMICOLON)
				.append(LINE);
		sb.append("import").append(SPACE).append(DatabaseUtil.PACKAGE + ".common.page.PageResult").append(SEMICOLON)
				.append(LINE);
		return sb.toString();
	}

	/**
	 * 实体类的注解
	 *
	 * @param tableName 实体类对应的表名
	 * @return 实体类需要的注解（@Entity、@Table）
	 */
	private String getClassAnnoation(String daoFileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("@Service").append(LINE);
		sb.append(
				"@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)")
				.append(LINE);
		return sb.toString();
	}

	/**
	 * 根据表名获取对象的实体类名称
	 *
	 * @return 实体类名称
	 */
	private String getEntityName() {
		// 处理下划线情况，把下划线后一位的字母变大写；
		String entityName = formatEntityName(this.tableName, "_", DatabaseUtil.UNDERLINE_INDEX);
		entityName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1, entityName.length());
		return entityName;
	}

	/**
	 * 把数据库表名转为实体名（驼峰命名）
	 *
	 * @return 实体类名称
	 */
	@SuppressWarnings("unused")
	private String getEntityName(String split, int beginIndex) {
		// 处理下划线情况，把下划线后一位的字母变大写；
		String entityName = formatEntityName(this.tableName, split, beginIndex);
		entityName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1, entityName.length());
		return entityName;
	}

	/**
	 * 把数据库表名转为实体名（驼峰命名）
	 *
	 * @param name       数据库表名
	 * @param split      分隔符
	 * @param beginIndex 从第几个分隔符开始生成名字
	 * @return 属性名（驼峰命名）
	 */
	private String formatEntityName(String name, String split, int beginIndex) {
		name = name.toLowerCase();
		if (name.contains(split)) {
			StringBuffer names = new StringBuffer();
			String arrayName[] = name.split(split);
			for (int i = beginIndex; i < arrayName.length; i++) {
				String arri = arrayName[i];
				String tmp = arri.substring(0, 1).toUpperCase() + arri.substring(1, arri.length());
				names.append(tmp);
			}
			name = names.toString();
		}
		return name;
	}

	public String getEntityNameForParam(String entityName) {
		entityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1, entityName.length());
		return entityName;
	}

	public String getEntityNameForParamList(String entityName) {
		entityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1, entityName.length()) + "s";
		return entityName;
	}
}
