package com.yanfeitech.application.enums;

public enum NoticeType {
     DATA_ISSUANCE(0, "资料下发"), COURSE_CHANGE(1, "课程变更"),HOMEWORK_REMINDER(2,"作业提醒");

    private Integer id;// 类型

    private String desc;// 描述

    private NoticeType(Integer id, String desc) {
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
        NoticeType[] values = NoticeType.values();
        for (NoticeType value : values) {
            if (value.getId().equals(id)) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static Integer getId(String desc) {
        NoticeType[] values = NoticeType.values();
        for (NoticeType value : values) {
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