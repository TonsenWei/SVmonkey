package com.desaysv.bean;

import java.io.Serializable;

/**
 * @author uidq0460
 * monkey测试记录
 */
public class Record implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String number;    			//序号
	private String deviceId;		    //设备ID
    private String testStatus;		    //测试状态
    private String crashCounter;		//crash数量
    private String anrCounter;		    //anr数量
    private String logPath;		        //日志路径
    private String runningTime;		    //已运行时间
    private String monkeyCmd;		    //执行的monkey指令
    private String errorMessage;		//其他错误信息
    
    public Record() {
    	this.number = "1";
    	this.deviceId = "";
    	this.testStatus = "进行中";
    	this.crashCounter = "0";
    	this.anrCounter = "0";
    	this.logPath = "";
    	this.runningTime = "0天0时0分";
    	this.monkeyCmd = "";
    	this.errorMessage = "";
    }
    
    public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}
	public String getCrashCounter() {
		return crashCounter;
	}
	public void setCrashCounter(String crashCounter) {
		this.crashCounter = crashCounter;
	}
	public String getAnrCounter() {
		return anrCounter;
	}
	public void setAnrCounter(String anrCounter) {
		this.anrCounter = anrCounter;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public String getMonkeyCmd() {
		return monkeyCmd;
	}
	public void setMonkeyCmd(String monkeyCmd) {
		this.monkeyCmd = monkeyCmd;
	}
	public String getRunningTime() {
		return runningTime;
	}
	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
