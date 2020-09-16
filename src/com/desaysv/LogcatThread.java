package com.desaysv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.desay.utils.MyUtils;

public class LogcatThread extends Thread {
	/**
	 * 停止线程
	 */
	volatile boolean stop = false;
	private String monkeyWorkDir;
	private String idStr;
	
	/**
	 * 创建获取logcat日志的线程
	 * @param idStr 要获取logcat日志的设备ID
	 * @param dir 保存目录
	 */
	public LogcatThread(String idStr, String dir) {
		this.monkeyWorkDir = dir;
		this.idStr = idStr;
		this.setDaemon(true);
	}
	 
	public void run() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		BufferedReader inputStream = null;
		BufferedReader errorReader = null;
		try {
			try {
//				String logcatCmd = "adb -s " + idStr + " shell logcat -v threadTime -b all";//同时输出缓冲区日志
				String logcatCmd = "adb -s " + idStr + " shell logcat -v threadTime";//不输出缓冲区日志
				MyUtils.printWithTimeMill(logcatCmd);
				Process pLogcat = Runtime.getRuntime().exec(logcatCmd);
				inputStream = new BufferedReader(new InputStreamReader(pLogcat.getInputStream()));
				errorReader = new BufferedReader(new InputStreamReader(pLogcat.getErrorStream()));
				String line;
				String logcatPathStr = monkeyWorkDir + "\\logcat.txt";
				fw = new FileWriter(logcatPathStr, true);
				bw = new BufferedWriter(fw);
				while((line = inputStream.readLine()) != null && stop==false) {
					if (!line.matches("\\s*")) {//一个或多个空格
						bw.append(MyUtils.getNowTimeMills() + "> " + line + "\r\n");
						bw.flush();
					}
				}
				while((line = errorReader.readLine()) != null && stop==false) {
					MyUtils.printWithTimeMill(logcatCmd);
					if (!line.matches("\\s*")) {//一个或多个空格
						bw.append(MyUtils.getNowTimeMills() + "> " + line + "\r\n");
						bw.flush();
					}
				}
//				pLogcat.waitFor();
				bw.close();
				fw.close();
				inputStream.close();
				errorReader.close();
				Thread.sleep(1);
				MyUtils.printWithTimeMill(idStr + " LogcatThread finished in normal");
			} catch (IOException e) {
				MyUtils.printWithTimeMill(idStr + " LogcatThread IOException" + e.getMessage());
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e1) {
					}
				}
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e1) {
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e1) {
					}
				}
				if (errorReader != null) {
					try {
						errorReader.close();
					} catch (IOException e1) {
					}
				}
				stop = true; // 在异常处理代码中修改共享变量的状态
			}
		} catch (InterruptedException ie) {
			MyUtils.printWithTimeMill(idStr + " close logcat input stream in LogcatThread InterruptedException");
			stop = true; // 在异常处理代码中修改共享变量的状态
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (errorReader != null) {
				try {
					errorReader.close();
				} catch (IOException e) {
				}
			}
		} 
	}
}
