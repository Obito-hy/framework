package com.yanfeitech.application.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * <p>
 * Title: File
 * </p>
 * <p>
 * Description: 文件实体类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Entity
@Getter
@Setter
@ToString
@Table(indexes = { @Index(columnList = "id") })
public class File extends BaseEntity {
	// 原文件名
	private String originalName;
	// 新文件名
	private String fileName;
	// 文件大小
	private String fileSize;
	// 文件相对BaseDir的路径
	private String relativePath;
	// 文件关联表名
	private String assicuateName;
	// 文件关联表主键
	private String assicuateId;
	//文件关联表关键字
	private String assicuateKey;
}
