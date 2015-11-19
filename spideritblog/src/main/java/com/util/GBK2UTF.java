package com.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBK2UTF {
	private static Logger logger = LoggerFactory.getLogger(GBK2UTF.class);
    
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
                    logger.info("convert " + file.getPath());
                    FileUtils.writeStringToFile(file, s, "UTF-8");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }
}
