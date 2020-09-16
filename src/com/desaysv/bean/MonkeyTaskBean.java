package com.desaysv.bean;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class MonkeyTaskBean {

	private long threadPid;
	private boolean keepRunning;
	private Thread thread;
	private long logcatPid;
	private long timerThreadPid;
	private BufferedWriter bufferdWriter;
	private FileWriter fileWriter;
	
	public MonkeyTaskBean() {
		this.threadPid = -1;
		this.keepRunning = true;
	}
	
	/**
	 * 构造函数，传入是否继续的值
	 * @param keepRun
	 */
	public MonkeyTaskBean(long pid, boolean keepRun) {
		this.threadPid = pid;
		this.keepRunning = keepRun;
	}

	public long getThreadPid() {
		return threadPid;
	}

	public void setThreadPid(long threadPid) {
		this.threadPid = threadPid;
	}

	public boolean isKeepRunning() {
		return keepRunning;
	}

	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public long getLogcatPid() {
		return logcatPid;
	}

	public void setLogcatPid(long logcatPid) {
		this.logcatPid = logcatPid;
	}

	public long getTimerThreadPid() {
		return timerThreadPid;
	}

	public void setTimerThreadPid(long timerThreadPid) {
		this.timerThreadPid = timerThreadPid;
	}

	public BufferedWriter getBufferdWriter() {
		return bufferdWriter;
	}

	public void setBufferdWriter(BufferedWriter bufferdWriter) {
		this.bufferdWriter = bufferdWriter;
	}

	public FileWriter getFileWriter() {
		return fileWriter;
	}

	public void setFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}
	
}
