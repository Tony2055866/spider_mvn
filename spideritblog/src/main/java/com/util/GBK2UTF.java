package com.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class GBK2UTF {
	public static void main(String[] args) throws IOException {
		String path = "D:\\Android\\code\\code\\XmppClient3\\app";
		FileUtils.readFileToString(new File(path));
	}
	
	private static void convert(File file) {
        if (file.isDirectory()) {
            File[] ch = file.listFiles();
            for (int i = 0; i < ch.length; i++) {
                convert(ch[i]);
            }

        } else {
            if (file.getName().endsWith("java") || file.getName().endsWith("xml")) {
                try {
                    String s = FileUtils.readFileToString(file, "GBK");
                    System.out.println("convert " + file.getPath());
                    FileUtils.writeStringToFile(file, s, "UTF-8");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }
}
