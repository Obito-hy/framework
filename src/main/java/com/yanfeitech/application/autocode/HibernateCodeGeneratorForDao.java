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
 * Title: HibernateCodeGeneratorForDao
 * </p>
 * <p>
 * Description: 根据数据库表，自动生成dao代码，运行即可在桌面获得
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class
HibernateCodeGeneratorForDao {

	private static final String LINE = "\r\n";
	private static final String TAB = "\t";
	private static final String SPACE = " ";
	private static final String SEMICOLON = ";";
	private static final String EQ = "=";
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

	public HibernateCodeGeneratorForDao() {

	}

	public HibernateCodeGeneratorForDao(String tableName) {
		this.tableName = tableName;
	}

	public static String getPojoType(String dataType) {
		String tmp = dataType.toLowerCase();
		return map.get(tmp);
	}

	// 示例
	public static void main(String[] args) {
		HibernateCodeGeneratorForDao hibernateCodeGenerator = new HibernateCodeGeneratorForDao();
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
			HibernateCodeGeneratorForDao hibernateCodeGenerator = new HibernateCodeGeneratorForDao(tableName);
			hibernateCodeGenerator.generatorEntity();
		}
	}

	/**
	 * 生成一个表的实体类
	 */
	public void generatorEntity() {
		String entityName = getEntityName();
		String daoName = entityName + "Dao";
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String path = desktopPath + "/dao/" + daoName + ".java";
		File file = new File(path);
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
			sb.append(getImports(entityName));
			sb.append(LINE);
			sb.append("/**").append(LINE).append(SPACE).append("*").append(SPACE).append(entityName).append("的DAO")
					.append(LINE).append(SPACE).append("*").append(SPACE).append(LINE).append(SPACE).append(SPACE)
					.append("@version 1.0.0").append(LINE).append(SPACE).append("*").append(SPACE).append("@since")
					.append(SPACE).append(sdf.format(new Date())).append(LINE).append(SPACE).append("*").append(LINE)
					.append(SPACE).append("*/");
			sb.append(LINE);
			sb.append(LINE);
			sb.append(getClassAnnoation(daoName));
			sb.append("public class ").append(daoName).append(SPACE).append("extends BaseDao<").append(entityName)
					.append(", String> {");
			sb.append(LINE);

			String entityNameForParam = getEntityNameForParam(entityName);
			String entityNameForParamList = getEntityNameForParamList(entityName);

			// 自动生成查询单个实体的方法（根据ID）
			sb.append(LINE).append(TAB);
			sb.append("public ").append(entityName).append(SPACE).append("select(String id) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return get(id);");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成查询所有实体的方法
			sb.append(LINE).append(TAB);
			sb.append("@SuppressWarnings(\"unchecked\")");
			sb.append(LINE).append(TAB);
			sb.append("public List<").append(entityName).append("> selectAll() {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return (ArrayList<").append(entityName).append(">) ")
					.append("getListBySql(\"select * from `" + tableName + "`\",").append(entityName)
					.append(".class);");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成分页查询的方法
			sb.append(LINE).append(TAB);
			sb.append("@SuppressWarnings(\"unchecked\")");
			sb.append(LINE).append(TAB);
			sb.append("public PageResult<").append(entityName).append("> selectAllForPage(int pageNo, int pageSize) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("PageResult<").append(entityName).append("> ").append(entityNameForParamList).append(" = ")
					.append("new PageResult<>();");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append(entityNameForParamList).append(EQ).append("(PageResult<").append(entityName)
					.append(">) findPageBySql(\"select * from `" + tableName + "`\", pageNo, pageSize, ")
					.append(entityName).append(".class, null);");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return ").append(entityNameForParamList).append(";");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成新增单个实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void insert(").append(entityName).append(SPACE).append(entityNameForParam).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("save(").append(entityNameForParam).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成批量新增实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void insert(List<").append(entityName).append(">").append(SPACE)
					.append(entityNameForParamList).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("saveBatch(").append(entityNameForParamList).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成更新单一实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void update(").append(entityName).append(SPACE).append(entityNameForParam).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("super.update(").append(entityNameForParam).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成批量更新实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void update(List<").append(entityName).append(">").append(SPACE)
					.append(entityNameForParamList).append(") {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("updateBatch(").append(entityNameForParamList).append(");");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE);

			// 自动生成删除单个实体的方法
			sb.append(LINE).append(TAB);
			sb.append("public void delete(String id) {");
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("deleteById(id);");
			sb.append(LINE).append(TAB);
			sb.append("}");

			sb.append(LINE).append(LINE);
			sb.append("}");
			// bw.write(LINE);
			bw.write(sb.toString());
			System.out.println(this.tableName + " created successfully");
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
	private String getImports(String entityName) {

		StringBuilder sb = new StringBuilder();
		// 处理下划线情况，把下划线后一位的字母变大写；
		sb.append("package").append(SPACE).append(DatabaseUtil.PACKAGE).append(".dao").append(SEMICOLON).append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("java.util.ArrayList").append(SEMICOLON).append(LINE);
		sb.append("import").append(SPACE).append("java.util.List").append(SEMICOLON).append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("org.springframework.stereotype.Repository").append(SEMICOLON)
				.append(LINE);
		sb.append(LINE);
		sb.append("import").append(SPACE).append("com.yanfeitech.application.dao.config.BaseDao").append(SEMICOLON)
				.append(LINE);
		sb.append(LINE);
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
		sb.append("@Repository").append(LINE);
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
