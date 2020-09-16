package com.desaysv.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author uidq0460
 * 某个设备的测试记录，用于导出表格中某个设备的所有记录到excel
 */
public class DeviceRecord {

	private DeviceInfo deviceInfo;
	private List<PackageErrorInfo> packageInfoList;
	
	public DeviceRecord() {
		this.deviceInfo = new DeviceInfo();
		this.packageInfoList = Collections.synchronizedList(new ArrayList<PackageErrorInfo>());
	}
	
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public List<PackageErrorInfo> getPackageInfoList() {
		return packageInfoList;
	}
	public void setPackageInfoList(List<PackageErrorInfo> packageInfoList) {
		this.packageInfoList = packageInfoList;
	}
	
}
