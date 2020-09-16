package com.desaysv.bean;

import java.io.Serializable;

/**
 * @author uidq0460
 * Crash和ANR信息
 */
public class AnrCrash implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fileDir;			//文件路径
	private String packageName;		//包名
	private String contentDetail;   //详细内容
	
	public AnrCrash() {
		this.fileDir = "";
		this.packageName = "";
		this.contentDetail = "";
	}
	public String getFileDir() {
		return fileDir;
	}
	public void setFileDir(String filePath) {
		this.fileDir = filePath;
	}
	public String getPackageName() {
		String name = "";
		String[] strings = packageName.split(" ");
		for (int i = 0; i < strings.length; i++) {
			String getName = strings[i];
			if (getName.equals("CRASH:") || getName.equals("RESPONDING:")) {
				name = strings[i + 1];
				if (name.contains(":")) {
					String[] names = name.split(":");
					name = names[0];
				}
			}
		}
		return name;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getContentDetail() {
		return contentDetail;
	}
	public void setContentDetail(String contentDetail) {
		this.contentDetail = contentDetail;
	}
 
}
