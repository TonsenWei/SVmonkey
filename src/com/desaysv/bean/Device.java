package com.desaysv.bean;

import java.util.List;

/**
 * @author uidq0460
 * 设备monkey参数
 */
public class Device {
	
	private String deviceId;  			//设备ID
    private List<String> packageList;	//设备程序列表
    private List<String> systemPackageList;	//设备系统程序列表
    private List<String> thirdPackageList;	//设备第三方程序列表
    
	private String throttleMillisec;    //事件间的时间间隔
    private String logLevel;		    //日志等级
    private String seed;			    //随机种子值
    private String eventCount;		    //事件数量
    
    private String trackBallPercent;    //轨迹事件的百分比(轨迹事件由一个或几个随机的移动组成，有时还伴随有点击)
    private String appSwitchPercent;	//Activity启动事件百分比
    private String flipPercent;		    //键盘翻转事件百分比
    private String touchPercent;		//触摸事件的百分比(触摸事件是一个down-up事件)
    private String sysKeyPercent;		//系统按键事件的百分比(这些按键通常被保留，由系统使用，如Home、Back、Start Call、End Call及音量控制键)

    //其它类型事件的百分比。它包罗了所有其它类型的事件，如：按键、其它不常用的设备按钮、等等（这个按键可能会按到power，因此工具中强制设置为0）
    private String anyEventPercent;
    private String pinchZoonPercent;	//缩放事件百分比
    private String navPercent;		    //基本导航事件的百分比(导航事件由来自方向输入设备的up/down/left/right组成)
    private String majorNavPercent;		//主要导航事件的百分比(这些导航事件通常引发图形界面中的动作，如：5-way键盘的中间按键、回退按键、菜单按键)
    private String motionPercent;		//滑动事件的百分比(滑动事件由屏幕上某处的一个down事件、一系列的伪随机事件和一个up事件组成)
    private String permissionPercent;	//滑动事件的百分比(滑动事件由屏幕上某处的一个down事件、一系列的伪随机事件和一个up事件组成)
    
    private Boolean isIgnoreCrashes;    //是否在出现crash时停止测试
    private Boolean isIgnoreTimeouts;   //是否在出现anr时停止测试
    private Boolean isIgnoreSecurityExceptions;//是否在出现指定当应用程序发生许可错误时（如证书许可，网络许可等）停止测试
    private Boolean isMonitorNativeCrash;//监视系统中本地代码发生的崩溃
    private Boolean isIgnoreNativeCrashes;//忽略本地代码导致的崩溃。设置忽略后，Monkey将执行完所有的事件，不会因此停止
    private Boolean isPermissionTargetSystem;//忽略本地代码导致的崩溃。设置忽略后，Monkey将执行完所有的事件，不会因此停止
    private Boolean isbugreport;//忽略本地代码导致的崩溃。设置忽略后，Monkey将执行完所有的事件，不会因此停止
    
    
    public Device() {
    	
    	this.throttleMillisec = "300";
    	this.logLevel = "-v -v -v";
    	this.seed = "0";
    	this.eventCount = "100000";
    	
    	this.trackBallPercent = "";
    	this.appSwitchPercent = "";
    	this.flipPercent = "";
    	this.touchPercent = "";
    	this.sysKeyPercent = "";
    	this.anyEventPercent = "";
    	this.pinchZoonPercent = "";
    	this.navPercent = "";
    	this.majorNavPercent = "";
    	this.motionPercent = "";
    	this.permissionPercent = "";
    	
    	this.isIgnoreCrashes = false;
    	this.isIgnoreTimeouts = false;
    	this.isIgnoreSecurityExceptions = false;
    	this.isMonitorNativeCrash = false;
    	this.isIgnoreNativeCrashes = false;
    	this.isPermissionTargetSystem = false;
    	this.isbugreport = true;
    	
	}
    
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public List<String> getPackageList() {
		return packageList;
	}
	public void setPackageList(List<String> packagesList) {
		this.packageList = packagesList;
	}
	public List<String> getThirdPackageList() {
		return thirdPackageList;
	}

	public void setThirdPackageList(List<String> thirdPackageList) {
		this.thirdPackageList = thirdPackageList;
	}

	public List<String> getSystemPackageList() {
		return systemPackageList;
	}

	public void setSystemPackageList(List<String> systemPackageList) {
		this.systemPackageList = systemPackageList;
	}

	public String getThrottleMillisec() {
		return throttleMillisec;
	}
	public void setThrottleMillisec(String throttleMillisec) {
		this.throttleMillisec = throttleMillisec;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getEventCount() {
		return eventCount;
	}
	public void setEventCount(String eventCount) {
		this.eventCount = eventCount;
	}
	public String getTrackBallPercent() {
		return trackBallPercent;
	}
	public void setTrackBallPercent(String trackBallPercent) {
		this.trackBallPercent = trackBallPercent;
	}
	public String getAppswitchPercent() {
		return appSwitchPercent;
	}
	public void setAppswitchPercent(String appswitchPercent) {
		this.appSwitchPercent = appswitchPercent;
	}
	public String getFlipPercent() {
		return flipPercent;
	}
	public void setFlipPercent(String flipPercent) {
		this.flipPercent = flipPercent;
	}
	public String getTouchPercent() {
		return touchPercent;
	}
	public void setTouchPercent(String touchPercent) {
		this.touchPercent = touchPercent;
	}
	public String getSyskeyPercent() {
		return sysKeyPercent;
	}
	public void setSyskeyPercent(String syskeyPercent) {
		this.sysKeyPercent = syskeyPercent;
	}
	public String getAnyeventPercent() {
		return anyEventPercent;
	}
	public void setAnyeventPercent(String anyeventPercent) {
		this.anyEventPercent = anyeventPercent;
	}
	public String getPinchzoonPercent() {
		return pinchZoonPercent;
	}
	public void setPinchzoonPercent(String pinchzoonPercent) {
		this.pinchZoonPercent = pinchzoonPercent;
	}
	public String getNavPercent() {
		return navPercent;
	}
	public void setNavPercent(String navPercent) {
		this.navPercent = navPercent;
	}
	public String getMajorNavPercent() {
		return majorNavPercent;
	}
	public void setMajorNavPercent(String majorNavPercent) {
		this.majorNavPercent = majorNavPercent;
	}
	public String getMotionPercent() {
		return motionPercent;
	}
	public void setMotionPercent(String motionPercent) {
		this.motionPercent = motionPercent;
	}
	public String getPermissionPercent() {
		return permissionPercent;
	}

	public void setPermissionPercent(String permissionPercent) {
		this.permissionPercent = permissionPercent;
	}

	public boolean isIgnoreCrashes() {
		return isIgnoreCrashes;
	}
	public void setIgnoreCrashes(Boolean isIgnoreCrashes) {
		this.isIgnoreCrashes = isIgnoreCrashes;
	}
	public boolean isIgnoreTimeouts() {
		return isIgnoreTimeouts;
	}
	public void setIgnoreTimeouts(Boolean isIgnoreTimeouts) {
		this.isIgnoreTimeouts = isIgnoreTimeouts;
	}
	public boolean isIgnoreSecurityExceptions() {
		return isIgnoreSecurityExceptions;
	}
	public void setIgnoreSecurityExceptions(Boolean isIgnoreSecurityExceptions) {
		this.isIgnoreSecurityExceptions = isIgnoreSecurityExceptions;
	}
	public boolean isMonitorNativeCrash() {
		return isMonitorNativeCrash;
	}
	public void setMonitorNativeCrash(Boolean isMonitorNativeCrash) {
		this.isMonitorNativeCrash = isMonitorNativeCrash;
	}
	public boolean isIgnoreNativeCrashes() {
		return isIgnoreNativeCrashes;
	}
	public void setIgnoreNativeCrashes(Boolean isIgnoreNativeCrashes) {
		this.isIgnoreNativeCrashes = isIgnoreNativeCrashes;
	}

	public Boolean isPermissionTargetSystem() {
		return isPermissionTargetSystem;
	}

	public void setIsPermissionTargetSystem(Boolean isPermissionTargetSystem) {
		this.isPermissionTargetSystem = isPermissionTargetSystem;
	}

	public Boolean isBugreport() {
		return isbugreport;
	}

	public void setIsBugreport(Boolean isbugreport) {
		this.isbugreport = isbugreport;
	}
	
}
