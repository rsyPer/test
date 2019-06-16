package com.rsy.per.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileEditCopy {

    public static void main(String[] args) throws Exception {

        String tipString = "-----------------------";

        // 打印信息工具类
        PrintUtil printUtil = new PrintUtil();

        // 获取当前jar包路径目录
        String currentDirPath = new File(
                FileEditCopy.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

        printUtil.printString(tipString.concat("文件替换行注释开始"));
        printUtil.printString(currentDirPath);

        String editBeforePath = currentDirPath.concat("\\00_before");
        String editAfterPath = currentDirPath.concat("\\01_after");
        String editDiffPath = currentDirPath.concat("\\02_diff");

        printUtil.printString(tipString.concat("文件替换前文件夹路径"));
        // 打印
        printUtil.printString(editBeforePath);
        printUtil.printString(tipString.concat("文件替换后文件夹路径"));
        printUtil.printString(editAfterPath);
        printUtil.printString(tipString.concat("文件替换后diff文件夹路径"));
        printUtil.printString(editDiffPath);

        // 获取所有的修正前java文件
        FileUtil fileUtil = new FileUtil();
        ArrayList<File> beforeFileList = fileUtil.getFiles(editBeforePath);
        // 打印当前文件夹下的所有文件名
        printUtil.printString(tipString.concat("替换前的所有文件"));
        printUtil.printFlieListName(beforeFileList);

        // 循环遍历每一个文件
        for (File file : beforeFileList) {
//            List<String> beforeFileDoc = new ArrayList<String>();
//            List<String> afterFileDoc = new ArrayList<String>();

            // 读入文件流
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            // 获取处理前文件名
            String beforeFileName = file.getPath();
            // 转换为处理后文件名；//
            String afterFileName = editAfterPath.concat("\\").concat(file.getName());

            printUtil.printString("--".concat("当前处理的替换前文件：").concat(beforeFileName));
            printUtil.printString("--".concat("当前处理的替换后文件：").concat(afterFileName));

            FileWriter fw = new FileWriter(new File(afterFileName));
            // 写入中文字符时会出现乱码
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(afterFileName)), "UTF-8"));

            String beforeFileline = "";
            while ((beforeFileline = br.readLine()) != null) {
//                beforeFileDoc.add(beforeFileline);

                int semicolonIndex = beforeFileline.indexOf(";");
                int commentIndex = beforeFileline.indexOf("//");
//                
//                printUtil.printString("semicolonIndex：".concat(String.valueOf(semicolonIndex)));
//                printUtil.printString("commentIndex：".concat(String.valueOf(commentIndex)));

                List<String> afterFilelines = new ArrayList<String>();
                // 如果行注释和分号在同一行，并且行注释在分号之后，就要进行处理
                if (semicolonIndex != -1 && commentIndex != -1 && commentIndex > semicolonIndex) {
                    printUtil.printString("-".concat("当前处理前内容：").concat(beforeFileline));

                    // 处理分号的内容到修正后文件
                    afterFilelines.add(beforeFileline.substring(0, semicolonIndex + 1));
                    String cmtIdxAfterStr = beforeFileline.substring(commentIndex + 1, beforeFileline.length() - 1).trim();
                    
                    if(!"".equals(cmtIdxAfterStr)) {
                        afterFilelines
                        .add("// ".concat(beforeFileline.substring(commentIndex + 2, beforeFileline.length())));
                    }
                    
                    printUtil.printListStringPrefix("-当前处理后内容：", afterFilelines);
                } else {
                    afterFilelines.add(beforeFileline);
                }

                for (String afterFileline : afterFilelines) {
//                    afterFileDoc.add(afterFileline);
                    bw.write(afterFileline+"\n");
                }

            }
            br.close();
            isr.close();
            fis.close();
            
            bw.close();
            fw.close();

//            printUtil.printListString(afterFileDoc);
        }

    }

}
