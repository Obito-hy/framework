package com.yanfeitech.application.vo;


import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/23 13:22
 */
public class ListC {
    public static void main(String[] args) {
        List list = new ArrayList() ;
        list.add("Obito");
        list.add("Obito");
        list.add("Obito");
        list.add("ItaChi,ita");
        List<String> myList = (List<String>) list.stream().distinct().collect(Collectors.toList());
        String s1 = myList.toString().replaceAll("(?:\\[|null|\\]| +)", "");
        System.out.println(s1);
    }
}
