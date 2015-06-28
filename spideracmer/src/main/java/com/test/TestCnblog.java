package com.test;

import com.util.CodeUtil;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCnblog {

	public static void main(String[] args) {
		try {
			String testCodeHtml = IOUtils.toString(new FileInputStream("E:\\Workspaces\\ACMER\\spider-hib\\src\\test.txt"));
			System.out.println(CodeUtil.getCode(testCodeHtml));
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
