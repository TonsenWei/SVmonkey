package com.desay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.desaysv.SvMonkey;
import com.desaysv.bean.Record;

public class RecordUtils {
	
	public static final String RECORD_FILE_PATH = SvMonkey.recordFilePath;
	public static final String FINISHED_STATUS = "结束";//结束:未检测到Monkey finished（比如设备断电，或出现anr和crash）
	public static final String TESTING_STATUS = "进行中";//进行中:测试进行中
	public static final String INTERRUPT_STATUS = "中断";//中断:正在测试时软件被关闭后重新打开
	public static final String ERROR_STATUS = "执行出错";//执行出错:执行monkey命令时出现执行错误
	public static final String PASS_STATUS = "通过";	 //通过:检测到Monkey finished且无anr和crash发生
	

	public static final int TYPE_COUNTER = 9;
	
	public static final int NUMBER_TYPE = 0;
	public static final int ID_TYPE = 1;
	public static final int TEST_STATUS_TYPE = 2;
	public static final int CRASH_TYPE = 3;
	public static final int ANR_TYPE = 4;
	public static final int RUNNING_TIME_TYPE = 5;
	public static final int LOG_PATH_TYPE = 6;
	public static final int MONKEY_CMD_TYPE = 7;
	public static final int ERROR_MESSAGE = 8;
	
	
	/**
	 * 根据设备ID判断设备是否正在测试
	 * @param deviceId 设备ID
	 * @return 正在测试返回true
	 */
	public static boolean isRunning(String deviceId) {
		boolean isRun = false;
		
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);
		if (recordList.size() >0) {
			for (Record record : recordList) {
				String idStr = record.getDeviceId();
				if (idStr.equals(deviceId)) {
					String statusStr = record.getTestStatus();
					if (statusStr.equals(TESTING_STATUS)) {
						isRun = true;
					}
				}
			}
		}
		return isRun;
	}
	
	/**
	 * 如果正在测试，程序关闭则把记录改为中断
	 */
	public static void interruptSaveRecord() {
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);
		List<Record> tmpList = Collections.synchronizedList(new ArrayList<Record>());
		if (recordList.size() >0) {
			for (Record record : recordList) {
				String statusStr = record.getTestStatus();
				if (statusStr.equals(TESTING_STATUS)) {
					record.setTestStatus(INTERRUPT_STATUS);
				}
				tmpList.add(record);
			}
			updateRecordFile(tmpList);
		}
	}
	/**
	 * 更新某一条记录中的数据
	 * @param index 第几条记录（从1开始）
	 * @param type 更新哪一列
	 * @param valueStr 更新后的值
	 */
	public static void updateOneRecordInFile(int index, int type, String valueStr) {
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);//先把数据读取出来
		if (recordList.size() > 0) {//有数据才能进行更新
			List<Record> tmpList = Collections.synchronizedList(new ArrayList<Record>());
			for (int i = 0; i < recordList.size(); i++) {//遍历旧的记录
				Record record = recordList.get(i);		
				if (i == (index-1)) {//找到该条记录
					switch (type) {  //按类型修改
					case TEST_STATUS_TYPE:
						record.setTestStatus(valueStr);
						break;
					case CRASH_TYPE:
						record.setCrashCounter(valueStr);
						break;
					case ANR_TYPE:
						record.setAnrCounter(valueStr);
						break;
					case RUNNING_TIME_TYPE:
						record.setRunningTime(valueStr);
						break;
					case ERROR_MESSAGE:
						record.setErrorMessage(valueStr);
						break;
					default:
						break;
					}
					tmpList.add(record);
				} else {
					tmpList.add(record);
				}
			}
			updateRecordFile(tmpList);//更新新数据
		}
	}
	
	/**
	 * duqu mou
	 * @param index 第几条记录（从1开始）
	 * @param type 更新哪一列
	 * @param valueStr 更新后的值
	 */
	public static String readOneRecordInFile(int index, int type) {
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);//先把数据读取出来
		String recordItemStr = "";
		if (recordList.size() > 0) {//有数据才能进行更新
			for (int i = 0; i < recordList.size(); i++) {//遍历旧的记录
				Record record = recordList.get(i);		
				if (i == (index-1)) {//找到该条记录
					switch (type) {  //按类型修改
					case TEST_STATUS_TYPE:
						recordItemStr = record.getTestStatus();
						break;
					case CRASH_TYPE:
						recordItemStr = record.getCrashCounter();
						break;
					case ANR_TYPE:
						recordItemStr = record.getAnrCounter();
						break;
					case RUNNING_TIME_TYPE:
						recordItemStr = record.getRunningTime();
						break;
					case ERROR_MESSAGE:
						recordItemStr = record.getErrorMessage();
						break;
					default:
						break;
					}
				}
			}
		}
		return recordItemStr;
	}
	
	/**
	 * 更新测试记录列表,序列化到本地(不更新界面，界面需要刷新)
	 * @param table 列表
	 * @param recordList 记录
	 */
	public static boolean updateRecordFile(List<Record> recordList) {
		return writeObject(recordList);
	}
	
    /**
     * 把list序列号到文件
     * @param list 泛型list
     * @param file 文件
     * @return 成功返回true
     */
    public static <T> boolean writeObject(List<T> list)
    {
        @SuppressWarnings("unchecked")
		T[] array = (T[]) list.toArray();
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(RECORD_FILE_PATH)))) 
        {
            out.writeObject(array);
            out.writeObject(null);//需要写个空对象，否则会报错
            out.flush();
            out.close();
            return true;
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从文件反序列化对象到泛型的list
     * @param file 文件
     * @return 返回list
     */
    @SuppressWarnings("unchecked")
	public static <E> List<E> readObjectForList(String objPath)
    {
    	File file = new File(objPath);
    	if (file.exists()) {
    		E[] object;
    		try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))) 
    		{
    			object = (E[]) out.readObject();
    			out.close();
    			return Collections.synchronizedList(new ArrayList<E>(Arrays.asList(object)));
    		}
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		} 
    		catch (ClassNotFoundException e) 
    		{
    			e.printStackTrace();
    		}
		}
    	return Collections.synchronizedList(new ArrayList<E>());
    }

	/**
	 * 添加一条记录（没有文件则创建新文件）
	 * @param deviceId 设备ID
	 * @param monkeyWorkDir 日志路径
	 * @param monkeyCmdStr  monkey命令
	 * @return 添加成功返回true
	 */
	public static List<Record> addRecord(String deviceId, String monkeyWorkDir, String monkeyCmdStr) {
		
		File recordFile = new File(RECORD_FILE_PATH);
		List<Record> recordList = Collections.synchronizedList(new ArrayList<Record>());
		if (recordFile.exists()) {//文件已存在，读取原有文件的记录并在最后追加一条记录
			recordList = readObjectForList(RECORD_FILE_PATH);
			
			Record addRecord = new Record();
			addRecord.setNumber((recordList.size() +1) + "");
			addRecord.setDeviceId(deviceId);
			addRecord.setLogPath(monkeyWorkDir);
			addRecord.setMonkeyCmd(monkeyCmdStr);
			
			recordList.add(addRecord);
			writeObject(recordList);
		} else {//文件不存在，则创建文件并添加为第一条记录
			try {
				if (recordFile.createNewFile()) {
					Record addRecord = new Record();
					addRecord.setNumber("1");
					addRecord.setDeviceId(deviceId);
					addRecord.setLogPath(monkeyWorkDir);
					addRecord.setMonkeyCmd(monkeyCmdStr);
					
					recordList.add(addRecord);
					writeObject(recordList);
				} else {
					MyUtils.printWithTimeMill("创建record文件失败");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return recordList;
	}
	
	/**
	 * 删除所有测试记录，进行中的记录除外.
	 * @param table 记录所在表格
	 */
	public static void removeAllRecordFormFile() {
		
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);
		if (recordList.size() > 0) {
			List<Record> tmpList = Collections.synchronizedList(new ArrayList<Record>());
			for (Record record : recordList) {
				if (record.getTestStatus().equals(TESTING_STATUS)) {//进行中的测试记录不能删除
					tmpList.add(record);
				}
			}
			//TODO
			updateRecordFile(tmpList);//直接把新的list写到文件，会覆盖旧的内容
		}
	}
	
	/**
	 * 删除测试记录
	 * @param table 记录所在表格
	 * @param index 要删除的记录的序号(从1开始)
	 */
	public static void removeRecordFormFile(int index) {
		File recordFile = new File(RECORD_FILE_PATH);
		
		if (index > 0) {
			List<Record> recordList = readObjectForList(RECORD_FILE_PATH);
			if (recordList.size() > 1) {
				recordList.remove(index -1);
				for (int i = 0; i < recordList.size(); i++) {
					Record record = recordList.get(i);
					record.setNumber((i+1) + "");
				}
				updateRecordFile(recordList);
			} else {
				if (recordFile.exists()) {
					recordFile.delete();
				}
			}
		}
	}
	
	/**
	 * 更新table显示
	 * @param table
	 */
	public static void updateTableUi(Table table) {
		List<Record> recordList = readObjectForList(RECORD_FILE_PATH);
		if (recordList.size() > 0) {
			TableItem[] items = table.getItems();//当前table总共有多少行
			if (items.length > recordList.size()) {//表格多了,把记录填入表格后多余的表格dispose掉
				for (int i = 0; i < items.length; i++) {
					if (i<recordList.size()) {//填入表格
						items[i].setText(0, recordList.get(i).getNumber());
						items[i].setText(1, recordList.get(i).getDeviceId());
						items[i].setText(2, recordList.get(i).getTestStatus());
						items[i].setText(3, recordList.get(i).getCrashCounter());
						items[i].setText(4, recordList.get(i).getAnrCounter());
						items[i].setText(5, recordList.get(i).getRunningTime());
						items[i].setText(6, recordList.get(i).getLogPath());
						items[i].setText(7, recordList.get(i).getMonkeyCmd());
					} else {//多的表格，dispose掉
						items[i].dispose();
					}
				}
			} else if (items.length <= recordList.size()) {//表格少了或等于，需要增加表格的行再添加数据
				int counter = recordList.size() - items.length;
				for (int i = 0; i < counter; i++) {
					new TableItem(table, SWT.None);
				}
				TableItem[] allItems = table.getItems();//增加table行后，重新获取table所有行
				for (int i = 0; i < recordList.size(); i++) {
					allItems[i].setText(0, recordList.get(i).getNumber());
					allItems[i].setText(1, recordList.get(i).getDeviceId());
					allItems[i].setText(2, recordList.get(i).getTestStatus());
					allItems[i].setText(3, recordList.get(i).getCrashCounter());
					allItems[i].setText(4, recordList.get(i).getAnrCounter());
					allItems[i].setText(5, recordList.get(i).getRunningTime());
					allItems[i].setText(6, recordList.get(i).getLogPath());
					allItems[i].setText(7, recordList.get(i).getMonkeyCmd());
				}
			}
		} else {
			TableItem[] items = table.getItems();
			if (items.length > 0) {
				for (TableItem tableItem : items) {
					tableItem.dispose();
				}
			}
		}
	}
	
	
}
