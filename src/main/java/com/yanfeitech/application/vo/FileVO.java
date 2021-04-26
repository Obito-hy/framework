package com.yanfeitech.application.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/25 11:41
 */
@Getter
@Setter
public class FileVO  extends BaseVO implements Serializable {
    // 原文件名
    private String originalName;
    // 新文件名
    private String fileName;
    // 文件大小
    private String fileSize;
    // 文件相对BaseDir的路径
    private String relativePath;
}
