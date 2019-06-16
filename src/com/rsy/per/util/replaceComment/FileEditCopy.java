package com.rsy.per.util.replaceComment;

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

/**
 * @author admin
 *
 */
public class FileEditCopy {

    PrintUtil printUtil = new PrintUtil();

    FileUtil fileUtil = new FileUtil();

    public void doMain() throws Exception {

        String tipString = "-------";

        // 获取当前jar包路径目录
        String currentDirPath = new File(
                FileEditCopy.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

        printUtil.printString(tipString.concat("文件替换行注释处理开始"));
        printUtil.printString(currentDirPath);

        String editBeforePath = currentDirPath.concat("\\00_before");
        String editAfterPath = currentDirPath.concat("\\01_after");
        String editDiffPath = currentDirPath.concat("\\02_diff");

        printUtil.printString(tipString.concat("文件替换前文件夹路径"));//
        printUtil.printString(editBeforePath);// 打印
        printUtil.printString(tipString.concat("文件替换后文件夹路径"));
        printUtil.printString(editAfterPath);
        printUtil.printString(tipString.concat("文件替换后diff文件夹路径"));
        printUtil.printString(editDiffPath);

        fileUtil.deleteDirFile(editAfterPath);
        fileUtil.deleteDirFile(editDiffPath);

        fileUtil.addDir(editAfterPath);
        fileUtil.addDir(editDiffPath);

        // 获取所有的修正前java文件
        ArrayList<File> beforeFileList = fileUtil.getFiles(editBeforePath);
        // 打印当前文件夹下的所有文件名
        printUtil.printString(tipString.concat("替换前的所有文件"));
        printUtil.printFlieListName(beforeFileList);

        // 循环遍历每一个文件
        for (File file : beforeFileList) {

            // 读入文件流
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String beforeFileName = file.getPath();// 获取处理前文件名
            String afterFileName = editAfterPath.concat("\\").concat(file.getName());// 转换为处理后文件名；//

            printUtil.printString("--".concat("当前处理的替换前文件：").concat(beforeFileName));
            printUtil.printString("--".concat("当前处理的替换后文件：").concat(afterFileName));

            FileWriter fw = new FileWriter(new File(afterFileName));
            // 写入中文字符时会出现乱码
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(afterFileName)), "UTF-8"));

            String beforeFileline = "";
            while ((beforeFileline = br.readLine()) != null) {
                // 得到处理后的内容
                List<String> afterFilelines = processLine(beforeFileline);
                for (String afterFileline : afterFilelines) {
                    bw.write(afterFileline + "\n");
                }
            }
            br.close();
            isr.close();
            fis.close();
            bw.close();
            fw.close();
        }
    }

    /*
     * out:得到处理后的内容
     */
    private List<String> processLine(String beforeFileline) {

        List<String> afterFilelines = new ArrayList<String>();

        int semicolonIndex = beforeFileline.indexOf(";");
        int commentIndex = beforeFileline.indexOf("//");

        // 如果行注释和分号在同一行，并且行注释在分号之后，就要进行处理
        if (semicolonIndex != -1 && commentIndex != -1 && commentIndex > semicolonIndex) {
            printUtil.printString("-".concat("当前处理前内容：").concat(beforeFileline));

            String cmtIdxAfterStr = beforeFileline.substring(commentIndex + 1, beforeFileline.length() - 1).trim();

            // 处理行注释的内容到修正后文件
            if (!"".equals(cmtIdxAfterStr)) {
                String spaceStr = beforeFileline.substring(0, beforeFileline.length() - beforeFileline.trim().length());
                afterFilelines.add(spaceStr.concat("// ")
                        .concat(beforeFileline.substring(commentIndex + 2, beforeFileline.length()).trim()));
            }
            // 处理分号的内容到修正后文件
            afterFilelines.add(beforeFileline.substring(0, semicolonIndex + 1));

            printUtil.printListStringPrefix("-当前处理后内容：", afterFilelines);
        } else {
            afterFilelines.add(beforeFileline);
        }

        return afterFilelines;

    }

}
