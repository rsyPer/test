package com.rsy.per.util.replaceComment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrintUtil {

    void printFlieListName(List<File> fileList) {
        // 返回文件名数组
        ArrayList<String> fileNameList = new ArrayList<String>();
        for (int i = 0; i < fileList.size(); i++) {
            // 获取文件路径
            String curpath = fileList.get(i).getPath();
            // 将文件名加入数组
            fileNameList.add(curpath.substring(curpath.lastIndexOf("\\") + 1));
        }
        printListString(fileNameList);
    }

    void printListString(List<String> stringList) {
        for (String string : stringList) {
            System.out.println(string);
        }
    }

    void printListStringPrefix(String prefixString, List<String> stringList) {

        if (prefixString == null || prefixString.equals("")) {
            printListString(stringList);
        } else {
            for (String string : stringList) {
                System.out.println(prefixString.concat(string));
            }
        }
    }

    void printString(String string) {
        System.out.println(string);
    }
}
