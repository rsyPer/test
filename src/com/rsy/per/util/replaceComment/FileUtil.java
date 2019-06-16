package com.rsy.per.util.replaceComment;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {

    public ArrayList<File> getFiles(String path) throws Exception {
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                // 如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath());
                } else {
                    // 如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        } else {
            throw new RuntimeException("请检查是否存在或是个目录：" + file.getPath());
        }

        return fileList;
    }

    public boolean deleteDirFile(String path) throws Exception {

        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f.getPath());
            }
        }
        return file.delete();
    }

    public boolean deleteFile(String path) throws Exception {
        File file = new File(path);
        return file.delete();
    }

    public void addDir(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            // 创建文件夹，上级目录不存在时自动创建，使用file.mkdir()方法时上级目录不存在会抛异常
            file.mkdirs();
        }
    }

}
