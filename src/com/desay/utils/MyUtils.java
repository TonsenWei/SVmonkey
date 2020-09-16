/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-09-12
 */
package com.desay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* @author 作者 E-mail: 470029260@qq.com
* @version 创建时间：2017年9月12日 下午2:58:38 
* 类说明 :
*/
/**
 * @author uidq0460
 *
 */
public class MyUtils {
	
	public static void printWithTimeMill(String message) {
		System.out.println(getNowTimeMills() + "> " + message.replaceAll("\r\n", "\r\n" + getNowTimeMills() + "> "));
	}
	
	/**
	 * 获取设备当前时间
	 * */
	public static String getNowTimeMills(){
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS").format(new Date());
	}
	
	/**
	 * 获取设备当前时间
	 * */
	public static String getNowTime(){
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
	}
	
	/**
	 * 获取设备当前时间
	 * */
	public static String getNowTimeSpace(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 把毫秒转成时间  XX天XX时XX分
	 * @param time 毫秒数
	 * @return 时分秒字符串
	 */
	public static String millisToTimeMinuteCn(long time) {  
		String timeStr = null;
		long dateL = 0;
		long hour = 0;  
		long minute = 0;  
		long second = 0;  
		if (time < 1000*60)  
			return "0天0时0分";  
		else {  
			second = time /1000;  
			minute = second / 60; 
			hour = minute /60;
			if (second < 60) {  
				timeStr = "0天0时0分";  
			}else if (minute < 60) {  
				timeStr = "0天0时" + unitFormat(minute) + "分";  
			} else if (hour < 24) {
				minute = minute - hour*60;  
				timeStr = "0天" + unitFormat(hour) + "时" + unitFormat(minute) + "分"; 
			} else {//数字>=3600 000的时候 (超过一天)
				dateL = hour / 24; //多少天
				hour = minute /60 - dateL*24; //多少小时 
				minute = minute - dateL*24*60 - hour*60;  
				timeStr = unitFormat(dateL) + "天" +  unitFormat(hour) + "时" + unitFormat(minute) + "分";  
			}  
		}  
		return timeStr;  
	}  
	
	/**
	 * 把毫秒转成时间  XX天XX时XX分XX秒
	 * @param time 毫秒数
	 * @return 天 时 分 秒字符串
	 */
	public static String millisToTimeSecondCn(long time) {  
		String timeStr = null;
		long dateL = 0;
		long hour = 0;  
		long minute = 0;  
		long second = 0;  
		if (time < 1000)  
			return time + "毫秒";
		else {  
			second = time/1000;  
			minute = second / 60; 
			hour = minute /60;
			if (second < 60) {  
				timeStr = second + "秒";  
			}else if (minute < 60) { 
				second = second - minute * 60;
				timeStr = minute + "分" + unitFormat(second) + "秒";  
			} else if (hour < 24) {
				second = second - minute * 60;
				minute = minute - hour*60;  
				timeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒"; 
			} else {//数字>=3600 000的时候 (超过一天)
				dateL = hour / 24; //多少天
				hour = minute/60 - dateL*24; //多少小时 
				minute = minute - dateL*24*60 - hour*60; 
				second = second - dateL*24*60*60 - hour*60*60 - minute*60;
				timeStr = dateL + "天" +  unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";  
			}  
		}  
		return timeStr;  
	}  
	
	/**
	 * 把毫秒转成时间hh:mi:ss
	 * @param time 毫秒数
	 * @return 时分秒字符串
	 */
	public static String millisToTimeMinute(long time) {  
		String timeStr = null;  
		long hour = 0;  
		long minute = 0;  
		long second = 0;  
		if (time < 1000*60)  
			return "00:00";  
		else {  
			second = time /1000;  
			minute = second / 60;  
			if (second < 60) {  
				timeStr = "00:00";  
			}else if (minute < 60) {  
				timeStr = "00:" + unitFormat(minute);  
			}else{//数字>=3600 000的时候  
				hour = minute /60;  
				minute = minute % 60;  
				timeStr = unitFormat(hour) + ":" + unitFormat(minute);  
			}  
		}  
		return timeStr;  
	}  
	
	/**
	 * 把毫秒转成时间hh:mi:ss
	 * @param time 毫秒数
	 * @return 时分秒字符串
	 */
	public static String millisToTimeSecond(long time) {  
		String timeStr = null;  
		long hour = 0;  
		long minute = 0;  
		long second = 0;  
		if (time < 1000)  
			return "00:00:00";  
		else {  
			second = time /1000;  
			minute = second / 60;  
			if (second < 60) {  
				timeStr = "00:00:" + unitFormat(second);  
			}else if (minute < 60) {  
				second = second % 60;  
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);  
			}else{//数字>=3600 000的时候  
				hour = minute /60;  
				minute = minute % 60;  
				second = second - hour * 3600 - minute * 60;  
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
			}  
		}  
		return timeStr;  
	}  
	
	/*把毫秒转成时间hh:mi:ss.xxx*/  
    public static String millisToTime(long time) {  
        String timeStr = null;  
        long hour = 0;  
        long minute = 0;  
        long second = 0;  
        long millisecond = 0;  
        if (time <= 0)  
            return "00:00:00.000";  
        else {  
            second = time /1000;  
            minute = second / 60;  
            millisecond = time % 1000;  
            if (second < 60) {  
                  
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);  
            }else if (minute < 60) {  
                second = second % 60;  
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);  
            }else{//数字>=3600 000的时候  
                hour = minute /60;  
                minute = minute % 60;  
                second = second - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);  
            }  
        }  
        return timeStr;  
    }  
  
    public static String unitFormat(long i) {//时分秒的格式转换  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Long.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }  
  
    public static String unitFormat2(long i) {//毫秒的格式转换
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "00" + Long.toString(i);  
        else if (i >=10 && i < 100) {  
             retStr = "0" + Long.toString(i);  
        }  
        else  
            retStr = "" + i;  
        return retStr;  
    }
    

    /**
     * 文件拷贝
     * @param fromFile 原始文件
     * @param toFile 拷贝到的文件
     * @throws IOException
     */
    public static void copyFile(File fromFile,File toFile) throws IOException{
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }
        ins.close();
        out.close();
    }
    
    
    /**
     * 文件拷贝
     * @param fromFile 原始文件
     * @param toFile 拷贝到的文件
     * @throws IOException
     */
    public static void copyFileByPath(String fromPath, String toPath) throws IOException {
    	File fromFile = new File(fromPath);
    	File toFile = new File(toPath);
    	if (fromFile.exists()) {
			if (toFile.exists()) {
				toFile.delete();
				toFile.createNewFile();
			}
			FileInputStream ins = new FileInputStream(fromFile);
			FileOutputStream out = new FileOutputStream(toFile);
			byte[] b = new byte[1024];
			int n=0;
			while((n=ins.read(b))!=-1){
				out.write(b, 0, n);
			}
			ins.close();
			out.close();
		}
    }
    

	//创建配置文件
	public static void createPropFile(String propFilePath) {
		File propFile = new File(propFilePath);
		if (propFile.exists()) {//文件存在
			propFilePath = propFile.getAbsolutePath();
			System.out.println("propFile = exists");
		} else {
			System.out.println("propFile = not exists");
			try {
				if (propFile.createNewFile()) {
					propFilePath = propFile.getAbsolutePath();
					System.out.println("创建成功");
				} else {
					System.out.println("创建失败");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
