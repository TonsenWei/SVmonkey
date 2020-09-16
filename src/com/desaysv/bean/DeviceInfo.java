package com.desaysv.bean;


public class DeviceInfo {

	private String deviceId;
	private String model;        	//ro.product.model=jacinto6evm 设备名称
	private String displayId;    	//ro.build.display.id=G5Android_5.1.47 内部名称//she bei mingcheng
	private String versionRelease;  //ro.build.version.release=5.1.1      安卓版本
	private String buildDate;		//ro.build.date=Fri Oct 27 10:42:54 CST 2017发布日期
	private String sdkVersion;		//ro.build.date=Fri Oct 27 10:42:54 CST 2017发布日期
	private String startTestTime;	//开始测试时间
	private String testCostTime;	//测试用时
	private String testCmd;			//monkey测试指令
	private String seed;			//实际seed值
	private String logPath;			//日志路径
	
	public DeviceInfo() {
		this.deviceId = "未知";
		this.model = "未知";
		this.displayId = "未知";
		this.versionRelease = "未知";
		this.buildDate = "未知";
		this.sdkVersion = "未知";
		this.startTestTime =  "未知";
		this.testCmd = "未知";
		this.seed = "未知";
		this.logPath = "未知";
		this.testCostTime = "0";
	}
	
	public DeviceInfo(String deviceId, String model, String displayId, String versionRelease, String BuildDate) {
		this.deviceId = deviceId;
		this.model = model;
		this.displayId = displayId;
		this.versionRelease = versionRelease;
		this.buildDate = BuildDate;
		this.sdkVersion = "未知";
		this.startTestTime =  "未知";
		this.testCmd = "未知";
		this.seed = "未知";
		this.logPath = "未知";
		this.testCostTime = "0";
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getVersionRelease() {
		return versionRelease;
	}

	public void setVersionRelease(String versionRelease) {
		this.versionRelease = versionRelease;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getStartTestTime() {
		return startTestTime;
	}

	public void setStartTestTime(String startTestTime) {
		this.startTestTime = startTestTime;
	}

	public String getTestCmd() {
		return testCmd;
	}

	public void setTestCmd(String testCmd) {
		this.testCmd = testCmd;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getTestCostTime() {
		return testCostTime;
	}

	public void setTestCostTime(String testCostTime) {
		this.testCostTime = testCostTime;
	}

}
