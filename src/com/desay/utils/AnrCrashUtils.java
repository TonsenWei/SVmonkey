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

import com.desaysv.bean.AnrCrash;

public class AnrCrashUtils {
	
	
	public static final int COLUMN_COUNTER = 5;
	
	public static final int ANR_TYPE = 0;
	public static final int CRASH_TYPE = 1;
	
	public static final int DIR_UI = 0;
	public static final int CRASH_NUM_UI = 1;
	public static final int CRASH_NAME_UI = 2;
	public static final int ANR_NUM_UI = 3;
	public static final int ANR_NAME_UI = 4;
	
	public static final String ANR_FILE_NAME = "anrRecord.txt";
	public static final String CRASH_FILE_NAME = "crashRecord.txt";
	
    /**
     * 把list序列化到文件
     * @param list 泛型list
     * @param filePath 文件路径
     * @return 成功返回true
     */
    public static <T> boolean writeObject(List<T> list, String filePath)
    {
        @SuppressWarnings("unchecked")
		T[] array = (T[]) list.toArray();
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(filePath)))) 
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
     * @param objPath 文件路径
     * @return 文件不存在返回size为0的list，存在则返回序列化的list
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
     * 添加一条ANR或CRASH记录
     * @param type anr或carsh
     * @param packageName 包名
     * @param content 详细内容
     * @param pathStr 文件所在目录
     * @return
     */
    public static boolean addAnrCrashRecord(int type, String packageName, String content, String pathStr) {
    	String filePath = "";
    	if (type == ANR_TYPE) {
    		filePath = pathStr + "\\" + ANR_FILE_NAME;
		} else {
			filePath = pathStr + "\\" + CRASH_FILE_NAME;
		}
    	List<AnrCrash> recordList = readObjectForList(filePath);
    	List<AnrCrash> tmpList = Collections.synchronizedList(new ArrayList<AnrCrash>());//临时list
    	if (recordList.size() > 0) {//已有记录,先把记录读取出来添加到临时list
			for (AnrCrash anrCrash : recordList) {
				tmpList.add(anrCrash);
			}
		}
    	AnrCrash anrCrashRecord = new AnrCrash();
    	anrCrashRecord.setPackageName(packageName);
    	anrCrashRecord.setContentDetail(content);
    	anrCrashRecord.setFileDir(pathStr);
    	tmpList.add(anrCrashRecord);
    	return writeObject(tmpList, filePath);
	}
    
    /**
     * 读取记录到列表
     * @param type
     * @param pathStr
     * @return
     */
    public static List<AnrCrash> readAnrCrashRecord(int type, String pathStr) {
    	String filePath = "";
    	if (type == ANR_TYPE) {
    		filePath = pathStr + "\\" + ANR_FILE_NAME;
    	} else {
    		filePath = pathStr + "\\" + CRASH_FILE_NAME;
    	}
    	return readObjectForList(filePath);
    }
    
	/**
	 * 更新anr和crash列表界面
	 * @param table 列表
	 * @param pathStr anr和crash记录文件所在目录
	 */
	public static void updateAnrCrashTableUi(Table table, String pathStr) {
		if (pathStr != null) {
			List<AnrCrash> crashList = readAnrCrashRecord(CRASH_TYPE, pathStr);
			List<AnrCrash> anrList = readAnrCrashRecord(ANR_TYPE, pathStr);
			
			int crashCounter = crashList.size();
			int anrCounter = anrList.size();
			
			int tableLineCounter = crashCounter>anrCounter ? crashCounter:anrCounter;
			TableItem[] items = table.getItems();
			if (items.length > tableLineCounter) {//表格多了,把记录填入表格后多余的表格dispose掉
				for (int i = 0; i < items.length; i++) {
					if (i >= tableLineCounter) {
						items[i].dispose();
					} 
				}
			} else if (items.length <= tableLineCounter) {//表格少了或等于，需要增加表格的行再添加数据
				int counter = tableLineCounter - items.length;
				for (int i = 0; i < counter; i++) {
					new TableItem(table, SWT.CENTER);
				}
			}
			items = table.getItems();//删减表格后重新获取表格每一行
			
			if (crashCounter > 0 || anrCounter > 0) {//有anr或crash的情况
				if (crashCounter >= anrCounter) {	 //crash大于anr大于0
					for (int i = 0; i < crashCounter; i++) {
						if (i < anrCounter) {//同时有crash和anr记录的行
							AnrCrash crashRecord = crashList.get(i);
							AnrCrash anrRecord = anrList.get(i);
							
							items[i].setText(DIR_UI, crashRecord.getFileDir());
							items[i].setText(CRASH_NUM_UI, (i + 1) + "");
							items[i].setText(CRASH_NAME_UI, crashRecord.getPackageName());
							
							items[i].setText(ANR_NUM_UI, (i + 1) + "");
							items[i].setText(ANR_NAME_UI, anrRecord.getPackageName());
						} else {//只有crash的行
							AnrCrash crashRecord = crashList.get(i);
							items[i].setText(DIR_UI, crashRecord.getFileDir());
							items[i].setText(CRASH_NUM_UI, (i + 1) + "");
							items[i].setText(CRASH_NAME_UI, crashRecord.getPackageName());
							items[i].setText(ANR_NUM_UI, "");
							items[i].setText(ANR_NAME_UI, "");
						}
					}
				} else { //anr大于0大于crash
					for (int i = 0; i < anrCounter; i++) {
						if (i < crashCounter) {//同时有crash和anr记录的行
							AnrCrash crashRecord = crashList.get(i);
							AnrCrash anrRecord = anrList.get(i);
							
							items[i].setText(DIR_UI, anrRecord.getFileDir());
							items[i].setText(CRASH_NUM_UI, (i + 1) + "");
							items[i].setText(CRASH_NAME_UI, crashRecord.getPackageName());
							
							items[i].setText(ANR_NUM_UI, (i + 1) + "");
							items[i].setText(ANR_NAME_UI, anrRecord.getPackageName());
						} else {//只有anr的行
							AnrCrash anrRecord = anrList.get(i);
							
							items[i].setText(DIR_UI, anrRecord.getFileDir());
							items[i].setText(ANR_NUM_UI, (i + 1) + "");
							items[i].setText(ANR_NAME_UI, anrRecord.getPackageName());
							
							items[i].setText(CRASH_NUM_UI, "");
							items[i].setText(CRASH_NAME_UI, "");
						}
					}
				}
			}
		} else {
			TableItem[] items = table.getItems();
			for (TableItem tableItem : items) {
				tableItem.dispose();
			}
		}
	}
	
	/**
	 * 获取某个记录的某个字段
	 * @param index 第几条记录
	 * @param type anr或crash
	 * @param pathStr 记录所在的目录
	 * @return 内容信息
	 */
	public static String getAnrCrashDetail(int index, int type, String pathStr) {
		String resultStr = "";
		List<AnrCrash> list = readAnrCrashRecord(type, pathStr);
		if (list.size() > 0) {
			AnrCrash anrCrashRecord = list.get(index - 1);
			resultStr = anrCrashRecord.getContentDetail();
		}
		
		return resultStr;
	}
}
