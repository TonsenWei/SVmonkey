package com.desaysv.bean;

/**
 * @author uidq0460
 * 包错误的信息内容，包括这个包发生anr的数量，crash的数量，log路径
 */
public class PackageErrorInfo {

	private String packageName;
	private int crashCount;      
	private int anrCount;    	
//	private String logPath;
	
	public PackageErrorInfo() {
		this.packageName = "";
		this.crashCount = 0;
		this.anrCount = 0;
//		this.logPath = "";
	}
	
	public PackageErrorInfo(String packageName, int crashCount, int anrCount, String logPath) {
		this.packageName = packageName;
		this.crashCount = crashCount;
		this.anrCount = anrCount;
//		this.logPath = logPath;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getCrashCount() {
		return crashCount;
	}
	public void setCrashCount(int crashCount) {
		this.crashCount = crashCount;
	}
	public int getAnrCount() {
		return anrCount;
	}
	public void setAnrCount(int anrCount) {
		this.anrCount = anrCount;
	}
//	public String getLogPath() {
//		return logPath;
//	}
//	public void setLogPath(String logPath) {
//		this.logPath = logPath;
//	}  		
	
}
