package com.yanfeitech.application.enums;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.vo.NoticeVO;

public enum NoticeStatus {
     NO_SENT(0, "未发送"), SENT(1, "已发送");

    private Integer id;// 类型

    private String desc;// 描述

    private NoticeStatus(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Integer id) {
        NoticeStatus[] values = NoticeStatus.values();
        for (NoticeStatus value : values) {
            if (value.getId() == id) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static Integer getId(String desc) {
        NoticeStatus[] values = NoticeStatus.values();
        for (NoticeStatus value : values) {
            if (value.getDesc().equals(desc)) {
                return value.getId();
            }
        }
        return null;
    }
    public static Boolean notValid(Integer id){
    	if (id != null){
    		 NoticeType[] values = NoticeType.values();
    	        for (NoticeType value : values) {
    	            if (value.getId().equals(id)) {
    	            	return false;
    	            }
    	        }
    	        return true;
        }
        return false;
    }

}