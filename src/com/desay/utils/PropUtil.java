package com.desay.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.desaysv.bean.Device;

public class PropUtil {
	
	private static final String USERDIR = System.getProperty("user.dir");//工作路径
	
	
	public static final String DEVICE_ID = "deviceId";  				//设备ID
	public static final String NICK_NAME = "nickName";				    //设备别名
    
	public static final String THROTTLE_MILLISEC = "throttleMillisec";  //事件间的时间间隔
	public static final String LOG_LEVEL = "logLevel";		            //日志等级
	
	public static final String SEED = "seed";		           
	public static final String EVENT_COUNT = "eventCount";		           
	public static final String TRACK_BALL_PERCENT = "trackBallPercent";		           
	public static final String APP_SWITCH_PERCENT = "appSwitchPercent";		           
	public static final String FLIP_PERCENT = "flipPercent";		           
	public static final String TOUCH_PERCENT = "touchPercent";		            
	public static final String SYS_KEY_PERCENT = "sysKeyPercent";		           
	public static final String ANY_EVENT_PERCENT = "anyEventPercent";		           
	public static final String PINCH_ZOON_PERCENT = "pinchZoonPercent";		           
	public static final String NAV_PERCENT = "navPercent";		           
	public static final String PERMISSION_PERCENT = "permissionPercent";		           
	
	public static final String MAJOR_NAV_PERCENT = "majorNavPercent";		            
	public static final String MOTION_PERCENT = "motionPercent";			            
	public static final String ROTATION_PERCENT = "rotationPercent";		            
	public static final String IS_IGNORE_CRASHES = "isIgnoreCrashes";		            
	public static final String IS_IGNORE_TIMEOUTS = "isIgnoreTimeouts";		           
	public static final String IS_IGNORE_SECURITY_EXCEPTIONS = "isIgnoreSecurityExceptions";		           
	public static final String IS_MONITOR_NATIVE_CRASH = "isMonitorNativeCrash";		          
	public static final String IS_IGNORE_NATEVE_CRASHES = "isIgnoreNativeCrashes";
	public static final String IS_PERMISSION_TARGET_SYSTEM = "isPermissionTargetSystem";
	public static final String IS_BUGREPORT = "isBugreport";
	
	public static final String SYSTEM_PACKAGE_LIST = "SystemPackageList.txt";		           
	public static final String THIRD_PACKAGE_LIST = "ThirdPackageList.txt";		           
	public static final String PACKAGE_LIST = "packagelist.txt";		           
	public static final String WHITE_LIST = "whitelist.txt";		           
	public static final String BLACK_LIST = "blacklist.txt";		           
	public static final String ORDER_LIST = "orderlist.txt";		           
    
	/**
	 * 保存系统包列表（如果已存在则先删除后创建）
	 * @param deviceStr
	 */
	public static void createSystemPackageList(String deviceStr, List<String> packageList) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + SYSTEM_PACKAGE_LIST;
			File packageListFile = new File(packageFileStr);
			try {
				boolean isCreateOk = false;
				if (packageListFile.exists()) {
					packageListFile.delete();
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					}
				} else {
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					} 
				}
				if (isCreateOk) {
					BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
					for (String packageStr : packageList) {
						out.write(packageStr + "\r\n"); // \r\n即为换行  
					}
		            out.flush(); // 把缓存区内容压入文件  
		            out.close(); // 最后记得关闭文件
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 保存系统包列表（如果已存在则先删除后创建）
	 * @param deviceStr
	 */
	public static void createThirdPackageList(String deviceStr, List<String> packageList) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + THIRD_PACKAGE_LIST;
			File packageListFile = new File(packageFileStr);
			try {
				boolean isCreateOk = false;
				if (packageListFile.exists()) {
					packageListFile.delete();
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					}
				} else {
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					} 
				}
				if (isCreateOk) {
					BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
					for (String packageStr : packageList) {
						out.write(packageStr + "\r\n"); // \r\n即为换行  
					}
					out.flush(); // 把缓存区内容压入文件  
					out.close(); // 最后记得关闭文件
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存所有包列表（如果已存在则先删除后创建）
	 * @param deviceStr
	 */
	public static void createPackageList(String deviceStr, List<String> packageList) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);// + "\\power.properties"
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + PACKAGE_LIST;
			File packageListFile = new File(packageFileStr);
			try {
				boolean isCreateOk = false;
				if (packageListFile.exists()) {
					packageListFile.delete();
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					}
				} else {
					if (packageListFile.createNewFile()) {
						isCreateOk = true;
					} 
				}
				if (isCreateOk) {
					BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
					for (String packageStr : packageList) {
						out.write(packageStr + "\r\n"); // \r\n即为换行  
					}
					out.flush(); // 把缓存区内容压入文件  
					out.close(); // 最后记得关闭文件
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存白名单列表（如果已存在则检查名单中每个包是否包含在所有包中）
	 * @param deviceStr
	 */
	public static void createWhiteList(String deviceStr) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);// + "\\power.properties"
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + WHITE_LIST;
			String packagesStr = packageDirStr + "\\" + PACKAGE_LIST;
			
			File packageListFile = new File(packageFileStr);
			try {
				if (packageListFile.exists()) {//文件已存在
					List<String> whiteList = Collections.synchronizedList(new ArrayList<String>());
					//判断是否包含在全部包中，不是的则删除
					whiteList = removeUnexistsFromList(readListFile(packagesStr), readListFile(packageFileStr));
					if (whiteList.size() > 0) {
						packageListFile.delete();
						if (packageListFile.createNewFile()) {
							BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
							for (String packageStr : whiteList) {
								out.write(packageStr + "\r\n"); // \r\n即为换行  
							}
				            out.flush(); // 把缓存区内容压入文件  
				            out.close(); // 最后记得关闭文件
						} 
					}
				} else {//文件不存在，则创建，但内容为空,等待用户添加
					if (packageListFile.createNewFile()) {
						MyUtils.printWithTimeMill("whitelist.txt创建成功");
					} 
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存黑名单列表（如果已存在则检查名单中每个包是否包含在所有包中）
	 * @param deviceStr
	 */
	public static void createBlackList(String deviceStr) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);// + "\\power.properties"
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + BLACK_LIST;
			String packagesStr = packageDirStr + "\\" + PACKAGE_LIST;
			
			File packageListFile = new File(packageFileStr);
			try {
				if (packageListFile.exists()) {//文件已存在
					List<String> blackList = Collections.synchronizedList(new ArrayList<String>());
					//判断是否包含在全部包中，不是的则删除
					blackList = removeUnexistsFromList(readListFile(packagesStr), readListFile(packageFileStr));
					if (blackList.size() > 0) {
						packageListFile.delete();
						if (packageListFile.createNewFile()) {
							BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
							for (String packageStr : blackList) {
								out.write(packageStr + "\r\n"); // \r\n即为换行  
							}
							out.flush(); // 把缓存区内容压入文件  
							out.close(); // 最后记得关闭文件
						} 
					}
				} else {//文件不存在，则创建，但内容为空,等待用户添加
					if (packageListFile.createNewFile()) {
						MyUtils.printWithTimeMill("blacklist.txt创建成功");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 保存黑名单列表（如果已存在则检查名单中每个包是否包含在所有包中）
	 * @param deviceStr
	 */
	public static void createOrderList(String deviceStr) {
		String packageDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(packageDirStr);// + "\\power.properties"
		if (!deviceProp.exists()) {//创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录存在，创建设备对应的配置文件
			String packageFileStr = packageDirStr + "\\" + ORDER_LIST;
			String packagesStr = packageDirStr + "\\" + PACKAGE_LIST;
			
			File packageListFile = new File(packageFileStr);
			try {
				if (packageListFile.exists()) {//文件已存在
					List<String> blackList = Collections.synchronizedList(new ArrayList<String>());
					//判断是否包含在全部包中，不是的则删除
					blackList = removeUnexistsFromList(readListFile(packagesStr), readListFile(packageFileStr));
					if (blackList.size() > 0) {
						packageListFile.delete();
						if (packageListFile.createNewFile()) {
							BufferedWriter out = new BufferedWriter(new FileWriter(packageListFile));
							for (String packageStr : blackList) {
								out.write(packageStr + "\r\n"); // \r\n即为换行  
							}
							out.flush(); //把缓存区内容压入文件  
							out.close(); //最后记得关闭文件
						} 
					}
				} else {//文件不存在，则创建，但内容为空,等待用户添加
					if (packageListFile.createNewFile()) {
						MyUtils.printWithTimeMill("orderlist.txt创建成功");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 判断字符串是否在某个设备的某个名单中
	 * @param str 字符串
	 * @param filePath 文件路径
	 * @param typePackage 黑名单还是白名单
	 * @return 存在返回true否则返回false
	 */
	public static boolean isContainInList(String str, String devicesId, String typePackage) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		list = readListFile(getDevicePackageListPath(devicesId, typePackage));
		if (list.contains(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 把字符串追加到文件
	 * @param str
	 * @param filePath
	 */
	public static void appendToFile(String str, String devicesId, String typePackage) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		String listFilePathStr = getDevicePackageListPath(devicesId, typePackage);
		list = readListFile(listFilePathStr);
		list.add(str);
		try {
			File file = new File(listFilePathStr);
			file.delete();
			file.createNewFile();
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (String packageStr : list) {
				out.write(packageStr + "\r\n"); // \r\n即为换行  
			}
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除某个包名
	 * @param str 包名
	 * @param devicesId 设备ID
	 * @param typePackage 黑名单还是白名单
	 */
	public static void removeFromFile(String str, String devicesId, String typePackage) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		String listFilePathStr = getDevicePackageListPath(devicesId, typePackage);
		list = readListFile(listFilePathStr);
		list.remove(str);
		try {
			File file = new File(listFilePathStr);
			file.delete();
			file.createNewFile();
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (String packageStr : list) {
				out.write(packageStr + "\r\n"); // \r\n即为换行  
			}
			out.flush(); // 把缓存区内容压入文件  
			out.close(); // 最后记得关闭文件
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDevicePackageListPath(String deviceId, String typePackage) {
		String deviceStrTran = "";
		if(deviceId.contains(":")){//网络adb的方式,则目录不能包含点和冒号，所以要转换为下划线和横杠
			deviceStrTran = deviceId.replace(".", "-");
			deviceStrTran = deviceStrTran.replace(":", "_");
		} else {
			deviceStrTran = deviceId;
		}
		
		return USERDIR + "\\" + deviceStrTran + "\\" + typePackage;
	}
	
	/**
	 * 读取名单到list
	 * @param filePath 白名单文件路径
	 * @return
	 */
	public static List<String> readListFile(String filePath) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		
		try {
			File filename = new File(filePath);
			if (filename.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(reader); 
				
				String line = "";  
				while ((line = br.readLine()) != null) {
					String listStr = line.trim();
					if (listStr != null && !listStr.equals("")) {
						list.add(listStr);
					}
				}
			} else {
				MyUtils.printWithTimeMill("该设备对应名单不存在:" + filePath);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return list;
	}
	
	
	/**
	 * 检查第一个list中是否包含第二list的所有元素，不包含则删除掉第二个list中的这个元素
	 * @param firstList 所有包
	 * @param secondList 黑白名单
	 * @return 返回第二个list中包含在第一个list中元素的list
	 */
	public static List<String> removeUnexistsFromList(List<String> firstList, List<String> secondList) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		
		for (String stringStr : secondList) {
			if (firstList.contains(stringStr)) {
				list.add(stringStr);
			}
		}
        
		return list;
	}
	
	/**
	 * 判断是否存在该设备
	 * @param deviceStr 设备ID
	 * @return
	 */
	public static boolean isDeviceExists(String deviceStr) {
		String devicePropDirStr = USERDIR + "\\" + deviceStr;
		File deviceProp = new File(devicePropDirStr);// + "\\power.properties"
		if (deviceProp.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 创建设备配置文件，不存在时才重新创建
	 * @param deviceStr
	 */
	public static void createDeviceProp(String deviceStr) {
		String deviceStrTran = "";
		if(deviceStr.contains(":")){//网络adb的方式,则目录不能包含点和冒号，所以要转换为下划线和横杠
			deviceStrTran = deviceStr.replace(".", "-");
			deviceStrTran = deviceStrTran.replace(":", "_");
		} else {
			deviceStrTran = deviceStr;
		}
		
		String devicePropDirStr = USERDIR + "\\" + deviceStrTran;
		File deviceProp = new File(devicePropDirStr);//目录
		if (!deviceProp.exists()) {//目录不存在则创建目录
			deviceProp.mkdirs();
		}
		
		if (deviceProp.exists()) {//目录创建成功，创建设备对应的配置文件
			String devciePropFileStr = devicePropDirStr + "\\" + deviceStrTran + ".properties";
			File propFile = new File(devciePropFileStr);
			try {
				if (! propFile.exists()) {
					if (propFile.createNewFile()) {
						MyUtils.printWithTimeMill("创建成功");
					}
					setMonkeyParameter(deviceStr, new Device());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取设备配置文件路径
	 * @param deviceStr
	 * @return
	 */
	public static String getDevicePropFilePath(String deviceStr) {
		String deviceStrTran = "";
		if(deviceStr.contains(":")){//网络adb的方式,因目录不能包含点和冒号，所以要转换为下划线和横杠
			deviceStrTran = deviceStr.replace(".", "-");
			deviceStrTran = deviceStrTran.replace(":", "_");
		} else {
			deviceStrTran = deviceStr;
		}
		return USERDIR + "\\" + deviceStrTran + "\\" + deviceStrTran + ".properties";
	}
	
	/**
	 * 获取键值
	 * @param keyStr 键
	 * @param pathStr 配置文件路径
	 * @return
	 * @Date 2017-05-31
	 */
	public static String getValueOfProp(String keyStr, String pathStr) {
		String valueStr = "";
		Map<String, String> mapTest = getProperties(pathStr);
		for (Map.Entry<String, String> entry : mapTest.entrySet()) {
			if (entry.getKey().equals(keyStr)) {//获取键值
				valueStr = entry.getValue();
			}
		}
		return valueStr;
	}
	
	/*
	 * 获取配置信息
	 * 返回map
	 * */
	//"a.properties"
	@SuppressWarnings("unchecked")
	public static Map<String, String> getProperties(String pathStr) {
		Properties prop = new Properties();
		try {
          //读取属性文件a.properties
         InputStream in = new BufferedInputStream (new FileInputStream(pathStr));
         prop.load(in);     //加载属性列表
         @SuppressWarnings("rawtypes")
         Map<String, String> mapProperties = new HashMap<String, String>((Map) prop);
         in.close();
         return mapProperties;
		} catch (Exception e) {
			MyUtils.printWithTimeMill("GetProperties Exception:" + e.toString());
			return null;
		}
	}
	
	/**
	 * 设置配置文件
	 * @param pathStr 配置文件路径
	 * @param keyStr 键
	 * @param valueStr 值
	 * @param isAdd 是否以追加的方式
	 * @Date 2017-05-31
	 */
	public static void setProperties(String pathStr, String keyStr, String valueStr, boolean isAdd) {
		Properties prop = new Properties();
		try {
          ///保存属性到b.properties文件
          FileOutputStream oFile = new FileOutputStream(pathStr, isAdd);//true表示追加打开
          prop.setProperty(keyStr, valueStr);
          prop.store(oFile, "The New properties file");
          oFile.close();
		} catch (Exception e) {
			MyUtils.printWithTimeMill(e.toString());
		}
	}

	/**
	 * 设置monkey默认参数
	 * @param deviceId 设备ID
	 * @param device 设备
	 */
	public static void setMonkeyParameter(String deviceId, Device device) {
		String propFilePath = getDevicePropFilePath(deviceId);
		device.setDeviceId(deviceId);
		
		PropUtil.setProperties(propFilePath, "deviceId", device.getDeviceId(), true);
		
		PropUtil.setProperties(propFilePath, "throttleMillisec", device.getThrottleMillisec(), true);
		PropUtil.setProperties(propFilePath, "logLevel", device.getLogLevel(), true);
		PropUtil.setProperties(propFilePath, "seed", device.getSeed(), true);
		PropUtil.setProperties(propFilePath, "eventCount", device.getEventCount(), true);
		
		PropUtil.setProperties(propFilePath, "trackBallPercent", device.getTrackBallPercent(), true);
		PropUtil.setProperties(propFilePath, "appSwitchPercent", device.getAppswitchPercent(), true);
		PropUtil.setProperties(propFilePath, "flipPercent", device.getFlipPercent(), true);
		PropUtil.setProperties(propFilePath, "touchPercent", device.getTouchPercent(), true);
		PropUtil.setProperties(propFilePath, "sysKeyPercent", device.getSyskeyPercent(), true);
		
		PropUtil.setProperties(propFilePath, "anyEventPercent", device.getAnyeventPercent(), true);
		PropUtil.setProperties(propFilePath, "pinchZoonPercent", device.getPinchzoonPercent(), true);
		PropUtil.setProperties(propFilePath, "navPercent", device.getNavPercent(), true);
		PropUtil.setProperties(propFilePath, "majorNavPercent", device.getMajorNavPercent(), true);
		PropUtil.setProperties(propFilePath, "motionPercent", device.getMotionPercent(), true);
		PropUtil.setProperties(propFilePath, "permissionPercent", device.getPermissionPercent(), true);
		
		Boolean isIgnore = device.isIgnoreCrashes();
		PropUtil.setProperties(propFilePath, "isIgnoreCrashes", isIgnore.toString(), true);
		isIgnore = device.isIgnoreTimeouts();
		PropUtil.setProperties(propFilePath, "isIgnoreTimeouts", isIgnore.toString(), true);
		isIgnore = device.isIgnoreSecurityExceptions();
		PropUtil.setProperties(propFilePath, "isIgnoreSecurityExceptions", isIgnore.toString(), true);
		isIgnore = device.isMonitorNativeCrash();
		PropUtil.setProperties(propFilePath, "isMonitorNativeCrash", isIgnore.toString(), true);
		isIgnore = device.isIgnoreNativeCrashes();
		PropUtil.setProperties(propFilePath, "isIgnoreNativeCrashes", isIgnore.toString(), true);
		isIgnore = device.isPermissionTargetSystem();
		PropUtil.setProperties(propFilePath, "isPermissionTargetSystem", isIgnore.toString(), true);
		isIgnore = device.isBugreport();
		PropUtil.setProperties(propFilePath, "isBugreport", isIgnore.toString(), true);
	}
	
	
	
	/**
	 * 设置monkey参数
	 * @param deviceId 设备ID
	 * @param key 设置项
	 * @param value 设置值
	 */
	public static void setMonkeyParameter(String deviceId, String key, String value) {
		String propFilePath = getDevicePropFilePath(deviceId);
		PropUtil.setProperties(propFilePath, key, value, true);
	}
	
	
    /**
     * 从配置文件获取记录的参数
     * @param deviceId
     * @param seedStr
     * @return
     */
    public static String getMonkeyConfigString(String deviceId, String seedStr) {
		String propFilePath = PropUtil.getDevicePropFilePath(deviceId);
		
		String isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_CRASHES, propFilePath);
		Boolean isIgnore = Boolean.parseBoolean(isIgnoreStr);
    	
    	String monkeyCmdStr = "";
		//忽略crash
		if (isIgnore) {
			monkeyCmdStr += ("--ignore-crashes ");
		}
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_TIMEOUTS, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		//忽略nar
		if (isIgnore) {
			monkeyCmdStr += ("--ignore-timeouts ");
		}
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		//忽略安全异常
		if (isIgnore) {
			monkeyCmdStr += ("--ignore-security-exceptions ");
		}
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_MONITOR_NATIVE_CRASH, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		//监视本地crash
		if (isIgnore) {
			monkeyCmdStr += ("--monitor-native-crashes ");
		}
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_NATEVE_CRASHES, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		//忽略本地crash
		if (isIgnore) {
			monkeyCmdStr += ("--ignore-native-crashes ");
		}
		
		String valuePropStr = PropUtil.getValueOfProp(PropUtil.TRACK_BALL_PERCENT, propFilePath);
		//其他参数事件参数
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-trackball " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.APP_SWITCH_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-appswitch " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.FLIP_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-flip " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.TOUCH_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-touch " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.SYS_KEY_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-syskeys " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.ANY_EVENT_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-anyevent " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.PINCH_ZOON_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-pinchzoom " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.NAV_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-nav " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MAJOR_NAV_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-majornav " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MOTION_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-motion " + valuePropStr + " ");
		}
		//ROTATION_PERCENT
		valuePropStr = PropUtil.getValueOfProp(PropUtil.ROTATION_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-rotation " + valuePropStr + " ");
		}
		valuePropStr = PropUtil.getValueOfProp(PropUtil.PERMISSION_PERCENT, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += ("--pct-permission " + valuePropStr + " ");
		}
		
		
		//seed值
		if (seedStr != null && !seedStr.trim().equals("")) {//指定seed值的时候
			monkeyCmdStr += ("-s " + seedStr.trim() + " ");
		} else {//没有指定seed值则从配置文件获取
			valuePropStr = PropUtil.getValueOfProp(PropUtil.SEED, propFilePath);
			if (valuePropStr != null && !valuePropStr.equals("")) {
				monkeyCmdStr += ("-s " + valuePropStr + " ");
			}
		}
		//日志等级
		valuePropStr = PropUtil.getValueOfProp(PropUtil.LOG_LEVEL, propFilePath);
		if (valuePropStr != null && !valuePropStr.equals("")) {
			monkeyCmdStr += (valuePropStr + " ");
		}
		//事件间隔时间
		valuePropStr = PropUtil.getValueOfProp(PropUtil.THROTTLE_MILLISEC, propFilePath);
		if (valuePropStr != null && !valuePropStr.trim().equals("")) {
			monkeyCmdStr += ("--throttle " + valuePropStr.trim() + " ");
		}
		
		//--permission-target-system是否是运行时权限系统（android6.0+特性）
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_PERMISSION_TARGET_SYSTEM, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		if (isIgnore) {
			monkeyCmdStr += ("--permission-target-system ");
		}
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_BUGREPORT, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		if (isIgnore) {
			monkeyCmdStr += ("--bugreport ");
		}
		//log:profile,bugreport等
				
		//事件数量
		valuePropStr = PropUtil.getValueOfProp(PropUtil.EVENT_COUNT, propFilePath);
		if (valuePropStr != null && !valuePropStr.trim().equals("")) {
			monkeyCmdStr += (valuePropStr.trim() + " ");
		}
    	
    	return monkeyCmdStr;
    }
    
	/**
	 * 保存配置
	 * @param deviceId 从哪个设备保存配置
	 * @param savePath 保存到哪个路径
	 */
	public static void saveConfig(String deviceId, String savePath){
		String propFilePath = PropUtil.getDevicePropFilePath(deviceId);
		String isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_CRASHES, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_IGNORE_CRASHES, isIgnoreStr, true);
		
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_TIMEOUTS, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_IGNORE_TIMEOUTS, isIgnoreStr, true);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, isIgnoreStr, true);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_MONITOR_NATIVE_CRASH, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_MONITOR_NATIVE_CRASH, isIgnoreStr, true);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_NATEVE_CRASHES, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_IGNORE_NATEVE_CRASHES, isIgnoreStr, true);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_PERMISSION_TARGET_SYSTEM, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_PERMISSION_TARGET_SYSTEM, isIgnoreStr, true);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_BUGREPORT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.IS_BUGREPORT, isIgnoreStr, true);
		
		String valuePropStr = PropUtil.getValueOfProp(PropUtil.THROTTLE_MILLISEC, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.THROTTLE_MILLISEC, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.LOG_LEVEL, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.LOG_LEVEL, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.SEED, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.SEED, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.EVENT_COUNT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.EVENT_COUNT, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.TRACK_BALL_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.TRACK_BALL_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.APP_SWITCH_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.APP_SWITCH_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.FLIP_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.FLIP_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.TOUCH_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.TOUCH_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.SYS_KEY_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.SYS_KEY_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.ANY_EVENT_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.ANY_EVENT_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.PINCH_ZOON_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.PINCH_ZOON_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.NAV_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.NAV_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MAJOR_NAV_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.MAJOR_NAV_PERCENT, valuePropStr, true);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MOTION_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.MOTION_PERCENT, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.ROTATION_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.ROTATION_PERCENT, valuePropStr, true);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MOTION_PERCENT, propFilePath);
		PropUtil.setProperties(savePath, PropUtil.MOTION_PERCENT, valuePropStr, true);
		
	}
	
	
	/**
	 * 导入配置信息
	 * @param deviceId 要导入到哪个设备
	 * @param importPath 从哪个文件导入
	 */
	public static void importConfig(String deviceId, String importPath){
		String propFilePath = getDevicePropFilePath(deviceId);
		
		String isIgnoreStr = getValueOfProp(PropUtil.IS_IGNORE_CRASHES, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_IGNORE_CRASHES, isIgnoreStr, true);
		
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_IGNORE_TIMEOUTS, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_IGNORE_TIMEOUTS, isIgnoreStr, true);
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, isIgnoreStr, true);
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_MONITOR_NATIVE_CRASH, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_MONITOR_NATIVE_CRASH, isIgnoreStr, true);
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_IGNORE_NATEVE_CRASHES, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_IGNORE_NATEVE_CRASHES, isIgnoreStr, true);
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_PERMISSION_TARGET_SYSTEM, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_PERMISSION_TARGET_SYSTEM, isIgnoreStr, true);
		
		isIgnoreStr = getValueOfProp(PropUtil.IS_BUGREPORT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.IS_BUGREPORT, isIgnoreStr, true);
		
		String valuePropStr = getValueOfProp(PropUtil.THROTTLE_MILLISEC, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.THROTTLE_MILLISEC, valuePropStr, true);
		
		valuePropStr = getValueOfProp(PropUtil.LOG_LEVEL, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.LOG_LEVEL, valuePropStr, true);
		
		valuePropStr = getValueOfProp(PropUtil.SEED, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.SEED, valuePropStr, true);
		
		valuePropStr = getValueOfProp(PropUtil.EVENT_COUNT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.EVENT_COUNT, valuePropStr, true);
		
		valuePropStr = getValueOfProp(PropUtil.TRACK_BALL_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.TRACK_BALL_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.APP_SWITCH_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.APP_SWITCH_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.FLIP_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.FLIP_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.TOUCH_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.TOUCH_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.SYS_KEY_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.SYS_KEY_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.ANY_EVENT_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.ANY_EVENT_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.PINCH_ZOON_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.PINCH_ZOON_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.NAV_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.NAV_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.MAJOR_NAV_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.MAJOR_NAV_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.MOTION_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.MOTION_PERCENT, valuePropStr, true);
		valuePropStr = getValueOfProp(PropUtil.ROTATION_PERCENT, importPath);
		PropUtil.setProperties(propFilePath, PropUtil.ROTATION_PERCENT, valuePropStr, true);
	}
	
}
