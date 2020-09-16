package com.desaysv;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import com.desay.utils.MyUtils;
import com.desay.utils.RecordUtils;

public class TimerThread extends Thread {
	/**
	 * 停止线程
	 */
	volatile boolean stop = false;
	
	private Display display;
	private Table table;
	private int index;
	private long startTime;
	
	
	/**
	 * 创建已运行时间的线程
	 * @param display
	 * @param table 表格
	 * @param index 时间写在表格的第几行
	 * @param startTime 开始计时的毫秒数
	 */
	public TimerThread(Display display, Table table, int index, long startTime) {
		this.display = display;
		this.table = table;
		this.index = index;
		this.startTime = startTime;
	}
	 
	public void run() {
		long endTestTime = 0;
		try {
			while(stop == false) {
				endTestTime = System.currentTimeMillis();//测试结束时间点距离1970年1月1日0点0分0秒的毫秒数
				if ((endTestTime - startTime) > 60*1000) {//一分钟刷新一次
					String runTimeStr = MyUtils.millisToTimeMinuteCn(endTestTime - startTime);
					display.asyncExec(new Runnable() {
						public void run() {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, runTimeStr);
							RecordUtils.updateTableUi(table);
						}
					});
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			MyUtils.printWithTimeMill("timer Interrupted");
			stop = true;
		}
	}
}
