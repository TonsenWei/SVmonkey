package com.desay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.desaysv.bean.DeviceInfo;
import com.desaysv.bean.DeviceRecord;
import com.desaysv.bean.PackageErrorInfo;


public class PoiUtils {
	
//	public static void main(String[] args) {
//		try {
//			DeviceInfo deviceInfo = new DeviceInfo("2e6408f043cc0122", 
//					"jacinto6evm", "G5Android_5.1.47", 
//					"5.1.1", "Fri Oct 27 10:42:54 CST 2017");
//			
//			createXlsxTemplate("poi_test.xlsx", deviceInfo);
//			MyUtils.printWithTimeMill("成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
    /**
     * 生成xlsx模板
     * @param tempDataPath
     * @param savePath
     * @throws IOException 
     */
    public static void createXlsxTemplate(String savePath, DeviceInfo deviceInfo, List<PackageErrorInfo> pacakgeInfoList) throws IOException {
    	outputXlsxToFile(createXlsx(deviceInfo, pacakgeInfoList), savePath);
	}
    
    /**
     * 根据输入的数据生成xlsx
     * @param tempDataPath
     * @param savePath
     * @throws IOException 
     */
    public static void outputXlsxToFile(DeviceInfo deviceInfo, List<PackageErrorInfo> pacakgeInfoList, String savePath) throws IOException {
    	outputXlsxToFile(createXlsx(deviceInfo, pacakgeInfoList), savePath);
    }
    
    /**
     * 根据输入的数据生成xlsx
     * @param tempDataPath
     * @param savePath
     * @throws IOException 
     */
    public static void outputXlsxToFile(List<DeviceRecord> deviceRecords, String savePath) throws IOException {
    	outputXlsxToFile(createDeviceAllRecordXlsx(deviceRecords), savePath);
    }

    /**
     * 更新（修改）某单元格数据
     * @param exlFile xlsx文件（需要先new File(path)）
     * @param sheetIndex 第几个sheet
     * @param col        列
     * @param row        行
     * @param value      修改的值
     * @throws Exception
     */
    @SuppressWarnings({ "deprecation", "resource" })
	public static void updateExcel(File exlFile,int sheetIndex,int col,int row,String value) throws Exception{
        FileInputStream fis=new FileInputStream(exlFile);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
//      workbook.
        Sheet sheet = workbook.getSheetAt(sheetIndex);
 
        Row r= sheet.getRow(row);//获取行
        if (r == null) {
			r = sheet.createRow(row);
		}
        Cell cell=r.getCell(col);		   //获取列
//      String str1=cell.getStringCellValue();
        //这里假设对应单元格原来的类型也是String类型
        if (cell == null) {
			cell = r.createCell(col);
		}
        
        if (value.contains("%")) {//百分数
        	String[] getNums = value.split("%");
        	cell.setCellValue(Double.parseDouble(getNums[0])/100);  
        	
        	XSSFDataFormat xssfFormat = workbook.createDataFormat();
        	String formatStr = "0%";
        	
	        XSSFCellStyle cellStyle = workbook.createCellStyle();  
	        cellStyle.setDataFormat(xssfFormat.getFormat(formatStr));
	        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	        cell.setCellStyle(cellStyle); 
			
		} else {//字符数字
			cell.setCellValue(Double.parseDouble(value));
			setCellCenter(cell);//居中
		}

 
        fis.close();//关闭文件输入流
 
        FileOutputStream fos=new FileOutputStream(exlFile);
        workbook.write(fos);
        fos.close();//关闭文件输出流
    }
    
    
    /**
     * 设置某个单元格居中
     * @param workbook 哪个xlsx表
     * @param cell 哪个单元格
     */
    @SuppressWarnings("deprecation")
	public static void setCellCenter(Cell cell) {
        XSSFCellStyle cellStyleCenter = (XSSFCellStyle) cell.getCellStyle();
        cellStyleCenter.setAlignment(XSSFCellStyle.ALIGN_CENTER);           //左右居中
        cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        cell.setCellStyle(cellStyleCenter);
	}
    
    
    /**
     * 设置默认单元格样式（上下左右居中，带框线）
     * @param cell
     */
    @SuppressWarnings("deprecation")
	public static XSSFCellStyle setCellDefaultStyle(Cell cell, XSSFCellStyle cellStyle) {
    	cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);           //居中
    	cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
    	cellStyle.setBorderTop(CellStyle.BORDER_THIN);
    	cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    	return cellStyle;
    }
    
    /**
     * 设置单元格背景色
     * @param cell
     */
    @SuppressWarnings("deprecation")
    public static XSSFCellStyle setCellColorStyle(Cell cell, short color,  XSSFCellStyle cellStyle) {
    	
    	cellStyle.setFillForegroundColor(color);	//设置背景色
    	cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	
    	return cellStyle;
    }
    
    /**
     * 设置单元格文字字体
     * @param cell
     */
    public static XSSFCellStyle setCellFontStyle(Cell cell, XSSFFont font, XSSFCellStyle cellStyle) {
    	cellStyle.setFont(font);
    	
    	return cellStyle;
    }
    
    
    
	/**
	 * 根据数据(设备信息和包错误信息)在内存中生成excel
	 * @param deviceInfo 设备信息（名称版本发布日期等）
	 * @param packageInfoList 包的错误信息列表
	 * @return 表格
	 */
	public static XSSFWorkbook createXlsx(DeviceInfo deviceInfo, List<PackageErrorInfo> packageInfoList) {
//    	XSSFWorkbook wb = new XSSFWorkbook();
        //创建excel工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
        //创建一个工作表sheet
		XSSFSheet sheetResult = workbook.createSheet("SVMonkeyResult");
		/* 
		 * 设定合并单元格区域范围 
		 *  firstRow  0-based 
		 *  lastRow   0-based 
		 *  firstCol  0-based 
		 *  lastCol   0-based 
		 */  
		CellRangeAddress craDeviceId = new CellRangeAddress(0, 0, 0, 3);
		CellRangeAddress craDeviceName = new CellRangeAddress(1, 1, 1, 3);
		CellRangeAddress craNickName = new CellRangeAddress(2, 2, 1, 3);
		CellRangeAddress craVersion = new CellRangeAddress(3, 3, 1, 3);
		CellRangeAddress craBuildDate = new CellRangeAddress(4, 4, 1, 3);
		CellRangeAddress craTestDate = new CellRangeAddress(5, 5, 1, 3);
		for (int i = 0; i < 4; i++) {
			sheetResult.setColumnWidth(i, (int)(40*256));
		}
		//在sheet里增加合并单元格
		sheetResult.addMergedRegion(craDeviceId);
		sheetResult.addMergedRegion(craDeviceName);
		sheetResult.addMergedRegion(craNickName);
		sheetResult.addMergedRegion(craVersion);
		sheetResult.addMergedRegion(craBuildDate);
		sheetResult.addMergedRegion(craTestDate);
		
        //创建第一行
        Row rowDeviceId = sheetResult.createRow(0);
        rowDeviceId.setHeight((short)(49.5*20));  
        Cell cellId = rowDeviceId.createCell(0);
        cellId.setCellValue("设备ID:" + deviceInfo.getDeviceId());
    	/**
    	  * font设置---设置字体样式
    	  */
    	 XSSFFont font1 = workbook.createFont();
    	 //设置行高使用HSSFRow对象的setHeight和setHeightInPoints方法，这两个方法的区别在于setHeightInPoints的单位是点，而setHeight的单位是1/20个点，所以setHeight的值永远是setHeightInPoints的20倍。
    	 //设置字号大小
    	 //font1.setFontHeight(20);
    	//设置字号大小
    	 font1.setFontHeightInPoints((short) 20);
    	 //设置字体
//    	 font1.setFontName("");
    	 //设置加粗
    	 font1.setBold(true);
    	 
    	 XSSFCellStyle style = setCellDefaultStyle(cellId, workbook.createCellStyle());
    	 setCellColorStyle(cellId, IndexedColors.SKY_BLUE.getIndex(), style);
    	 setCellFontStyle(cellId, font1, style);
    	 cellId.setCellStyle(style);
    	 for (int i = 0; i < 3; i++) {
    		 Cell cell = rowDeviceId.createCell(i + 1);
    		 cell.setCellStyle(style);
		}
    	 
    	XSSFCellStyle styleDeviceInfo = setCellDefaultStyle(cellId, workbook.createCellStyle());
    	setCellColorStyle(cellId, IndexedColors.SKY_BLUE.getIndex(), styleDeviceInfo);
    	//创建第二行
    	Row rowDeviceName = sheetResult.createRow(1);
    	Cell cellName = rowDeviceName.createCell(0);
    	cellName.setCellValue("设备名称");
    	cellName.setCellStyle(styleDeviceInfo);
    	Cell cellNameValue = rowDeviceName.createCell(1);
    	cellNameValue.setCellValue(deviceInfo.getModel());
    	cellNameValue.setCellStyle(styleDeviceInfo);
		for (int i = 0; i < 2; i++) {
			Cell cell = rowDeviceName.createCell(i + 2);
			cell.setCellStyle(styleDeviceInfo);
		}
    	//创建第三行
    	Row rowNickName = sheetResult.createRow(2);
    	Cell cellNickName = rowNickName.createCell(0);
    	cellNickName.setCellValue("系统版本");
    	cellNickName.setCellStyle(styleDeviceInfo);
    	Cell neckNameValue = rowNickName.createCell(1);
    	neckNameValue.setCellValue(deviceInfo.getDisplayId());
    	neckNameValue.setCellStyle(styleDeviceInfo);
    	for (int i = 0; i < 2; i++) {
			Cell cell = rowNickName.createCell(i + 2);
			cell.setCellStyle(styleDeviceInfo);
		}
    	//创建第四行
    	Row rowAndroidVersion = sheetResult.createRow(3);
    	Cell cellVersion = rowAndroidVersion.createCell(0);
    	cellVersion.setCellValue("安卓版本");
    	cellVersion.setCellStyle(styleDeviceInfo);
    	Cell versionValue = rowAndroidVersion.createCell(1);
    	versionValue.setCellValue(deviceInfo.getVersionRelease());
    	versionValue.setCellStyle(styleDeviceInfo);
    	for (int i = 0; i < 2; i++) {
			Cell cell = rowAndroidVersion.createCell(i + 2);
			cell.setCellStyle(styleDeviceInfo);
		}
    	//创建第五行
    	Row rowBuildDate = sheetResult.createRow(4);
    	Cell cellBuildDate = rowBuildDate.createCell(0);
    	cellBuildDate.setCellValue("编译日期");
    	cellBuildDate.setCellStyle(styleDeviceInfo);
    	Cell cellBuildDateValue = rowBuildDate.createCell(1);
    	cellBuildDateValue.setCellValue(deviceInfo.getBuildDate());
    	cellBuildDateValue.setCellStyle(styleDeviceInfo);
    	for (int i = 0; i < 2; i++) {
			Cell cell = rowBuildDate.createCell(i + 2);
			cell.setCellStyle(styleDeviceInfo);
		}
    	
    	//增加一行测试命令 第六行
    	Row rowTestCmd = sheetResult.createRow(5);
    	Cell cellCmd = rowTestCmd.createCell(0);
    	cellCmd.setCellValue("测试指令");
    	cellCmd.setCellStyle(styleDeviceInfo);
    	Cell cellCmdValue = rowTestCmd.createCell(1);
    	cellCmdValue.setCellValue(deviceInfo.getTestCmd());
    	cellCmdValue.setCellStyle(styleDeviceInfo);
    	for (int i = 0; i < 2; i++) {
			Cell cell = rowTestCmd.createCell(i + 2);
			cell.setCellStyle(styleDeviceInfo);
		}
    	//增加一行测试日期和seed值 第七行
    	Row rowDateSeed = sheetResult.createRow(6);
    	Cell cellTestDate = rowDateSeed.createCell(0);
    	cellTestDate.setCellValue("测试日期");
    	cellTestDate.setCellStyle(styleDeviceInfo);
    	
    	Cell cellTestDateValue = rowDateSeed.createCell(1);
    	cellTestDateValue.setCellValue(deviceInfo.getStartTestTime());
    	cellTestDateValue.setCellStyle(styleDeviceInfo);
    	
    	Cell cellSeed = rowDateSeed.createCell(2);
    	cellSeed.setCellValue("Seed实际值");
    	cellSeed.setCellStyle(styleDeviceInfo);
    	
    	Cell cellSeedValue = rowDateSeed.createCell(3);
    	cellSeedValue.setCellValue(deviceInfo.getSeed());
    	cellSeedValue.setCellStyle(styleDeviceInfo);
    	
    	//创建第八行
    	long anrCounter = 0;
    	long crashCounter = 0;
    	for (PackageErrorInfo info : packageInfoList) {
    		anrCounter += info.getAnrCount();
    		crashCounter += info.getCrashCount();
		}
    	XSSFCellStyle styleDefault = setCellDefaultStyle(cellId, workbook.createCellStyle());
    	Row rowAnrCrashCounter = sheetResult.createRow(7);
    	Cell cellCrahName = rowAnrCrashCounter.createCell(0);
    	cellCrahName.setCellValue("CRASH总数");
    	cellCrahName.setCellStyle(styleDefault);
    	
    	Cell cellCrahValue = rowAnrCrashCounter.createCell(1);
    	cellCrahValue.setCellValue(crashCounter + "");
    	cellCrahValue.setCellStyle(styleDefault);
    	
    	Cell cellANRName = rowAnrCrashCounter.createCell(2);
    	cellANRName.setCellValue("ANR总数");
    	cellANRName.setCellStyle(styleDefault);
    	
    	Cell cellANRValue = rowAnrCrashCounter.createCell(3);
    	cellANRValue.setCellValue(anrCounter + "");
    	cellANRValue.setCellStyle(styleDefault);
    	
    	//创建第九行
    	CellRangeAddress craPackages = new CellRangeAddress(8, 8, 1, 3);
    	sheetResult.addMergedRegion(craPackages);
    	
    	Row rowPackages = sheetResult.createRow(8);
    	Cell cellPackages = rowPackages.createCell(0);
    	cellPackages.setCellValue("出现问题的应用程序数量");
    	cellPackages.setCellStyle(styleDefault);
    	
    	Cell cellPackageValue = rowPackages.createCell(1);
    	cellPackageValue.setCellValue(packageInfoList.size() + "");
    	cellPackageValue.setCellStyle(styleDefault);
    	for (int i = 0; i < 2; i++) {
			Cell cell = rowPackages.createCell(i + 2);
			cell.setCellStyle(styleDefault);
		}
    	
    	//创建第十行
    	CellRangeAddress craLogPath = new CellRangeAddress(9, 9, 1, 3);
    	sheetResult.addMergedRegion(craLogPath);
    	
    	Row logPathRow = sheetResult.createRow(9);
    	Cell cellLogPath = logPathRow.createCell(0);
    	cellLogPath.setCellValue("日志路径");
    	cellLogPath.setCellStyle(styleDefault);
    	
    	Cell cellLogPathValue = logPathRow.createCell(1);
    	cellLogPathValue.setCellValue(deviceInfo.getLogPath());
    	cellLogPathValue.setCellStyle(styleDefault);
    	for (int i = 0; i < 2; i++) {
    		Cell cell = logPathRow.createCell(i + 2);
    		cell.setCellStyle(styleDefault);
    	}
    	
    	/**
    	 * 详细包列表
    	 * */
    	for (int i = 0; i < packageInfoList.size(); i++) {
    		PackageErrorInfo errorInfo = packageInfoList.get(i);
			
    		//创建第十一行
    		CellRangeAddress craPackageDetail = new CellRangeAddress(i*2+10, i*2+10, 0, 3);
    		sheetResult.addMergedRegion(craPackageDetail);
    		Row rowPackageFirst = sheetResult.createRow(i*2+10);
    		rowPackageFirst.setHeight((short)(26.25*20));
    		Cell cellPackageFirst = rowPackageFirst.createCell(0);
    		cellPackageFirst.setCellValue(errorInfo.getPackageName());
        	/**
        	 * font设置---设置字体样式
        	 */
			XSSFFont fontPackageName = workbook.createFont();
			fontPackageName.setFontHeightInPoints((short) 20);
	    	XSSFCellStyle stylePackage = setCellDefaultStyle(cellId, workbook.createCellStyle());
	    	setCellColorStyle(cellId, IndexedColors.SEA_GREEN.getIndex(), stylePackage);
	    	setCellFontStyle(cellId, fontPackageName, stylePackage);
	    	cellPackageFirst.setCellStyle(stylePackage);
	    	for (int j = 0; j < 3; j++) {
	    		Cell cell = rowPackageFirst.createCell(j + 1);
				cell.setCellStyle(stylePackage);
			}
       	 
    		//创建第十二行
    		Row rowPackageCrashAnr = sheetResult.createRow(i*2+11);
    		Cell cellPackageCrash = rowPackageCrashAnr.createCell(0);
    		cellPackageCrash.setCellValue("CRASH数量");
    		cellPackageCrash.setCellStyle(styleDefault);
    		
    		Cell cellPackageCrashValue = rowPackageCrashAnr.createCell(1);
    		cellPackageCrashValue.setCellValue(errorInfo.getCrashCount() + "");
    		cellPackageCrashValue.setCellStyle(styleDefault);
    		
    		Cell cellPackageAnr = rowPackageCrashAnr.createCell(2);
    		cellPackageAnr.setCellValue("ANR数量");
    		cellPackageAnr.setCellStyle(styleDefault);
    		
    		Cell cellPackageAnrValue = rowPackageCrashAnr.createCell(3);
    		cellPackageAnrValue.setCellValue(errorInfo.getAnrCount() + "");
    		cellPackageAnrValue.setCellStyle(styleDefault);
		}
    	
        return workbook;
    }
	
	
	/**
	 * 根据数据(设备信息和包错误信息)在内存中生成excel,需要先判断list是否为0
	 * @param deviceInfo 设备信息（名称版本发布日期等）
	 * @param packageInfoList 包的错误信息列表
	 * @return 表格
	 */
	public static XSSFWorkbook createDeviceAllRecordXlsx(List<DeviceRecord> deviceRecords) {
//    	XSSFWorkbook wb = new XSSFWorkbook();
		//创建excel工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		//图表
		List<Object[]> objs = Collections.synchronizedList(new ArrayList<Object[]>());
		
		long maxCounter = 0;
		
		for (int index = 0; index < deviceRecords.size(); index++) {
			DeviceRecord deviceRecord = deviceRecords.get(index);
			
			//创建一个工作表sheet
			XSSFSheet sheetResult = workbook.createSheet((index + 1) + "");
			DeviceInfo deviceInfo = deviceRecord.getDeviceInfo();
			List<PackageErrorInfo> packageInfoList = deviceRecord.getPackageInfoList();
			
			//crash, anr, 错误包数量， 版本
			String[] resultCounters = new String[]{"0", "0", "0", "未知"};
			/* 
			 * 设定合并单元格区域范围 
			 *  firstRow  0-based 
			 *  lastRow   0-based 
			 *  firstCol  0-based 
			 *  lastCol   0-based 
			 */  
			CellRangeAddress craDeviceId = new CellRangeAddress(0, 0, 0, 3);
			CellRangeAddress craDeviceName = new CellRangeAddress(1, 1, 1, 3);
			CellRangeAddress craNickName = new CellRangeAddress(2, 2, 1, 3);
			CellRangeAddress craVersion = new CellRangeAddress(3, 3, 1, 3);
			CellRangeAddress craBuildDate = new CellRangeAddress(4, 4, 1, 3);
			CellRangeAddress craTestDate = new CellRangeAddress(5, 5, 1, 3);
			for (int i = 0; i < 4; i++) {
				sheetResult.setColumnWidth(i, (int)(40*256));
			}
			//在sheet里增加合并单元格
			sheetResult.addMergedRegion(craDeviceId);
			sheetResult.addMergedRegion(craDeviceName);
			sheetResult.addMergedRegion(craNickName);
			sheetResult.addMergedRegion(craVersion);
			sheetResult.addMergedRegion(craBuildDate);
			sheetResult.addMergedRegion(craTestDate);
			
			//创建第一行
			Row rowDeviceId = sheetResult.createRow(0);
			rowDeviceId.setHeight((short)(49.5*20));  
			Cell cellId = rowDeviceId.createCell(0);
			cellId.setCellValue("设备ID:" + deviceInfo.getDeviceId());
			/**
			 * font设置---设置字体样式
			 */
			XSSFFont font1 = workbook.createFont();
			//设置行高使用HSSFRow对象的setHeight和setHeightInPoints方法，这两个方法的区别在于setHeightInPoints的单位是点，而setHeight的单位是1/20个点，所以setHeight的值永远是setHeightInPoints的20倍。
			//设置字号大小
			//font1.setFontHeight(20);
			//设置字号大小
			font1.setFontHeightInPoints((short) 20);
			//设置字体
//	    	 font1.setFontName("");
			//设置加粗
			font1.setBold(true);
			
			XSSFCellStyle style = setCellDefaultStyle(cellId, workbook.createCellStyle());
			setCellColorStyle(cellId, IndexedColors.SKY_BLUE.getIndex(), style);
			setCellFontStyle(cellId, font1, style);
			cellId.setCellStyle(style);
			for (int i = 0; i < 3; i++) {
				Cell cell = rowDeviceId.createCell(i + 1);
				cell.setCellStyle(style);
			}
			
			XSSFCellStyle styleDeviceInfo = setCellDefaultStyle(cellId, workbook.createCellStyle());
			setCellColorStyle(cellId, IndexedColors.SKY_BLUE.getIndex(), styleDeviceInfo);
			//创建第二行
			Row rowDeviceName = sheetResult.createRow(1);
			Cell cellName = rowDeviceName.createCell(0);
			cellName.setCellValue("设备名称");
			cellName.setCellStyle(styleDeviceInfo);
			Cell cellNameValue = rowDeviceName.createCell(1);
			cellNameValue.setCellValue(deviceInfo.getModel());
			cellNameValue.setCellStyle(styleDeviceInfo);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowDeviceName.createCell(i + 2);
				cell.setCellStyle(styleDeviceInfo);
			}
			//创建第三行
			Row rowNickName = sheetResult.createRow(2);
			Cell cellNickName = rowNickName.createCell(0);
			cellNickName.setCellValue("内部名称");
			cellNickName.setCellStyle(styleDeviceInfo);
			Cell neckNameValue = rowNickName.createCell(1);
			neckNameValue.setCellValue(deviceInfo.getDisplayId());
			resultCounters[3] = deviceInfo.getDisplayId();
			neckNameValue.setCellStyle(styleDeviceInfo);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowNickName.createCell(i + 2);
				cell.setCellStyle(styleDeviceInfo);
			}
			//创建第四行
			Row rowAndroidVersion = sheetResult.createRow(3);
			Cell cellVersion = rowAndroidVersion.createCell(0);
			cellVersion.setCellValue("安卓版本");
			cellVersion.setCellStyle(styleDeviceInfo);
			Cell versionValue = rowAndroidVersion.createCell(1);
			versionValue.setCellValue(deviceInfo.getVersionRelease());
			versionValue.setCellStyle(styleDeviceInfo);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowAndroidVersion.createCell(i + 2);
				cell.setCellStyle(styleDeviceInfo);
			}
			//创建第五行
			Row rowBuildDate = sheetResult.createRow(4);
			Cell cellBuildDate = rowBuildDate.createCell(0);
			cellBuildDate.setCellValue("发布日期");
			cellBuildDate.setCellStyle(styleDeviceInfo);
			Cell cellBuildDateValue = rowBuildDate.createCell(1);
			cellBuildDateValue.setCellValue(deviceInfo.getBuildDate());
			cellBuildDateValue.setCellStyle(styleDeviceInfo);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowBuildDate.createCell(i + 2);
				cell.setCellStyle(styleDeviceInfo);
			}
			
			//增加一行测试命令 第六行
			Row rowTestCmd = sheetResult.createRow(5);
			Cell cellCmd = rowTestCmd.createCell(0);
			cellCmd.setCellValue("测试指令");
			cellCmd.setCellStyle(styleDeviceInfo);
			Cell cellCmdValue = rowTestCmd.createCell(1);
			cellCmdValue.setCellValue(deviceInfo.getTestCmd());
			cellCmdValue.setCellStyle(styleDeviceInfo);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowTestCmd.createCell(i + 2);
				cell.setCellStyle(styleDeviceInfo);
			}
			//增加一行测试日期和seed值 第七行
			Row rowDateSeed = sheetResult.createRow(6);
			Cell cellTestDate = rowDateSeed.createCell(0);
			cellTestDate.setCellValue("测试日期");
			cellTestDate.setCellStyle(styleDeviceInfo);
			
			Cell cellTestDateValue = rowDateSeed.createCell(1);
			cellTestDateValue.setCellValue(deviceInfo.getStartTestTime());
			cellTestDateValue.setCellStyle(styleDeviceInfo);
			
			Cell cellSeed = rowDateSeed.createCell(2);
			cellSeed.setCellValue("Seed实际值");
			cellSeed.setCellStyle(styleDeviceInfo);
			
			Cell cellSeedValue = rowDateSeed.createCell(3);
			cellSeedValue.setCellValue(deviceInfo.getSeed());
			cellSeedValue.setCellStyle(styleDeviceInfo);
			
			//创建第八行
			long anrCounter = 0;
			long crashCounter = 0;
			for (PackageErrorInfo info : packageInfoList) {
				anrCounter += info.getAnrCount();
				crashCounter += info.getCrashCount();
			}
			XSSFCellStyle styleDefault = setCellDefaultStyle(cellId, workbook.createCellStyle());
			Row rowAnrCrashCounter = sheetResult.createRow(7);
			Cell cellCrahName = rowAnrCrashCounter.createCell(0);
			cellCrahName.setCellValue("CRASH总数");
			cellCrahName.setCellStyle(styleDefault);
			
			Cell cellCrahValue = rowAnrCrashCounter.createCell(1);
			cellCrahValue.setCellValue(crashCounter + "");
			resultCounters[0] = crashCounter + "";
			cellCrahValue.setCellStyle(styleDefault);
			if (maxCounter < crashCounter) {
				maxCounter = crashCounter;
			}
			
			Cell cellANRName = rowAnrCrashCounter.createCell(2);
			cellANRName.setCellValue("ANR总数");
			cellANRName.setCellStyle(styleDefault);
			
			Cell cellANRValue = rowAnrCrashCounter.createCell(3);
			cellANRValue.setCellValue(anrCounter + "");
			resultCounters[1] = anrCounter + "";
			cellANRValue.setCellStyle(styleDefault);
			if (maxCounter < anrCounter) {
				maxCounter = anrCounter;
			}
			
			//创建第九行
			CellRangeAddress craPackages = new CellRangeAddress(8, 8, 1, 3);
			sheetResult.addMergedRegion(craPackages);
			
			Row rowPackages = sheetResult.createRow(8);
			Cell cellPackages = rowPackages.createCell(0);
			cellPackages.setCellValue("出现问题的应用程序数量");
			cellPackages.setCellStyle(styleDefault);
			
			Cell cellPackageValue = rowPackages.createCell(1);
			cellPackageValue.setCellValue(packageInfoList.size() + "");
			resultCounters[2] = packageInfoList.size() + "";
			objs.add(resultCounters);
			cellPackageValue.setCellStyle(styleDefault);
			for (int i = 0; i < 2; i++) {
				Cell cell = rowPackages.createCell(i + 2);
				cell.setCellStyle(styleDefault);
			}
			if (maxCounter < packageInfoList.size()) {
				maxCounter = packageInfoList.size();
			}
			
			//创建第十行
			CellRangeAddress craLogPath = new CellRangeAddress(9, 9, 1, 3);
			sheetResult.addMergedRegion(craLogPath);
			
			Row logPathRow = sheetResult.createRow(9);
			Cell cellLogPath = logPathRow.createCell(0);
			cellLogPath.setCellValue("日志路径");
			cellLogPath.setCellStyle(styleDefault);
			
			Cell cellLogPathValue = logPathRow.createCell(1);
			cellLogPathValue.setCellValue(deviceInfo.getLogPath());
			cellLogPathValue.setCellStyle(styleDefault);
			for (int i = 0; i < 2; i++) {
				Cell cell = logPathRow.createCell(i + 2);
				cell.setCellStyle(styleDefault);
			}
			
			/**
			 * 详细包列表
			 * */
			for (int i = 0; i < packageInfoList.size(); i++) {
				PackageErrorInfo errorInfo = packageInfoList.get(i);
				
				//创建第十一行
				CellRangeAddress craPackageDetail = new CellRangeAddress(i*2+10, i*2+10, 0, 3);
				sheetResult.addMergedRegion(craPackageDetail);
				Row rowPackageFirst = sheetResult.createRow(i*2+10);
				rowPackageFirst.setHeight((short)(26.25*20));
				Cell cellPackageFirst = rowPackageFirst.createCell(0);
				cellPackageFirst.setCellValue(errorInfo.getPackageName());
				/**
				 * font设置---设置字体样式
				 */
				XSSFFont fontPackageName = workbook.createFont();
				fontPackageName.setFontHeightInPoints((short) 20);
				XSSFCellStyle stylePackage = setCellDefaultStyle(cellId, workbook.createCellStyle());
				setCellColorStyle(cellId, IndexedColors.SEA_GREEN.getIndex(), stylePackage);
				setCellFontStyle(cellId, fontPackageName, stylePackage);
				cellPackageFirst.setCellStyle(stylePackage);
				for (int j = 0; j < 3; j++) {
					Cell cell = rowPackageFirst.createCell(j + 1);
					cell.setCellStyle(stylePackage);
				}
				
				//创建第十二行
				Row rowPackageCrashAnr = sheetResult.createRow(i*2+11);
				Cell cellPackageCrash = rowPackageCrashAnr.createCell(0);
				cellPackageCrash.setCellValue("CRASH数量");
				cellPackageCrash.setCellStyle(styleDefault);
				
				Cell cellPackageCrashValue = rowPackageCrashAnr.createCell(1);
				cellPackageCrashValue.setCellValue(errorInfo.getCrashCount() + "");
				cellPackageCrashValue.setCellStyle(styleDefault);
				
				Cell cellPackageAnr = rowPackageCrashAnr.createCell(2);
				cellPackageAnr.setCellValue("ANR数量");
				cellPackageAnr.setCellStyle(styleDefault);
				
				Cell cellPackageAnrValue = rowPackageCrashAnr.createCell(3);
				cellPackageAnrValue.setCellValue(errorInfo.getAnrCount() + "");
				cellPackageAnrValue.setCellStyle(styleDefault);
			}
		}
		
		int testRecordCounter = objs.size();//测试记录条数
		if (testRecordCounter > 0) {
			XSSFSheet sheetResult = workbook.createSheet("测试结果趋势图");
//	        Row rowResult;
//	        Cell cellResult;
//
//	        //创建行
//	        rowResult = sheetResult.createRow(0);
//	        Row rowCrash = sheetResult.createRow(1);
//	        Row rowAnr = sheetResult.createRow(2);
//	        Row rowPackages = sheetResult.createRow(3);
//	        
//	        rowResult.createCell(0);//列
//	        rowCrash.createCell(0).setCellValue("CRASH");
//	        rowAnr.createCell(0).setCellValue("ANR");
//	        rowPackages.createCell(0).setCellValue("packages");
//	        
//	        for(int i=0; objs!=null && i<objs.size(); i++){  
//	            Object [] obj = objs.get(i);
//	            
//	            rowResult.createCell(i + 1).setCellValue(obj[3].toString());
//		        rowCrash.createCell(i + 1).setCellValue(Integer.parseInt(obj[0].toString()));
//		        rowAnr.createCell(i + 1).setCellValue(Integer.parseInt(obj[1].toString()));
//		        rowPackages.createCell(i + 1).setCellValue(Integer.parseInt(obj[2].toString()));
//	        } 
//	        
//	        Drawing drawing = sheetResult.createDrawingPatriarch();
//	        
//	        /**
//	         * 图表位置
//	         * 第0列第5行，到第8列20行
//	         * */
//	        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 8, 20);
//
//	        Chart chart = drawing.createChart(anchor);//在指定位置画出图表
//
//	        CTChart ctChart = ((XSSFChart)chart).getCTChart();
//	        CTPlotArea ctPlotArea = ctChart.getPlotArea();
//	        CTBarChart ctBarChart = ctPlotArea.addNewBarChart();
//	        CTBoolean ctBoolean = ctBarChart.addNewVaryColors();
//	        ctBoolean.setVal(true);
//	        ctBarChart.addNewBarDir().setVal(STBarDir.COL);
//
//	        /**
//	         * 设置数据
//	         * 柱子从第二格开始显示，每个Header共有6个位置，header有四个位置，则两边各空一个位置
//	         * */
//	        for (int r = 2; r < 6; r++) {
//	           CTBarSer ctBarSer = ctBarChart.addNewSer();
//	           CTSerTx ctSerTx = ctBarSer.addNewTx();
//	           CTStrRef ctStrRef = ctSerTx.addNewStrRef();
//	           ctStrRef.setF("Sheet1!$A$" + r);		//A列第r行
//	           ctBarSer.addNewIdx().setVal(r-2);  
//	           CTAxDataSource cttAxDataSource = ctBarSer.addNewCat();
//	           ctStrRef = cttAxDataSource.addNewStrRef();
//	           ctStrRef.setF("Sheet1!$" +numToABC(r)+ "$1:$"+numToABC(r)+"$1");  //B1到D1
//	           CTNumDataSource ctNumDataSource = ctBarSer.addNewVal();
//	           CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
//	           ctNumRef.setF("Sheet1!$" +numToABC(r)+ "$" + r + ":$" +numToABC(r)+ "$" + r);//Br到Dr
//
//	           //at least the border lines in Libreoffice Calc ;-)
//	           ctBarSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[] {0,0,0});   
//
//	        } 
//
//	        //telling the BarChart that it has axes and giving them Ids
//	        ctBarChart.addNewAxId().setVal(123456);
//	        ctBarChart.addNewAxId().setVal(123457);
//
//	        //cat axis
//	        CTCatAx ctCatAx = ctPlotArea.addNewCatAx(); 
//	        ctCatAx.addNewAxId().setVal(123456); //id of the cat axis
//	        CTScaling ctScaling = ctCatAx.addNewScaling();
//	        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
//	        ctCatAx.addNewDelete().setVal(false);
//	        ctCatAx.addNewAxPos().setVal(STAxPos.B);
//	        ctCatAx.addNewCrossAx().setVal(123457); //id of the val axis
//	        ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
//
//	        //val axis
//	        CTValAx ctValAx = ctPlotArea.addNewValAx(); 
//	        ctValAx.addNewAxId().setVal(123457); //id of the val axis
//	        ctScaling = ctValAx.addNewScaling();
//	        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
//	        ctValAx.addNewDelete().setVal(false);
//	        ctValAx.addNewAxPos().setVal(STAxPos.L);
//	        ctValAx.addNewCrossAx().setVal(123456); //id of the cat axis
//	        ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
//
//	        //legend
//	        CTLegend ctLegend = ctChart.addNewLegend();
//	        ctLegend.addNewLegendPos().setVal(STLegendPos.B);
//	        ctLegend.addNewOverlay().setVal(false);
	        
			JfreeChartUtil.putImageToSheet(workbook, sheetResult, JfreeChartUtil.createChartBar(objs, "Monkey测试结果趋势图", "软件版本", "单位/个", maxCounter/10 + ""));
			workbook.setActiveSheet(testRecordCounter - 1);
		}
		
		
		return workbook;
	}
	
	public static String numToABC(int num) {
		String resultABC = "A";
		switch (num) {
		case 1:
			resultABC = "A";
			break;
		case 2:
			resultABC = "B";
			break;
		case 3:
			resultABC = "C";
			break;
		case 4:
			resultABC = "D";
			break;
		case 5:
			resultABC = "E";
			break;
		case 6:
			resultABC = "F";
			break;
		case 7:
			resultABC = "G";
			break;
		case 8:
			resultABC = "H";
			break;
		case 9:
			resultABC = "I";
			break;
		case 10:
			resultABC = "J";
			break;
		case 11:
			resultABC = "K";
			break;
		case 12:
			resultABC = "L";
			break;
		case 13:
			resultABC = "M";
			break;
		case 14:
			resultABC = "N";
			break;
		case 15:
			resultABC = "O";
			break;
		default:
			break;
		}
		return resultABC;
	}
    
    /**
     * 把内存中的表格数据写入到文件（.xlsx），若文件已存在则删除旧文件
     * @param workbook 内存中的表格数据（表格）
     * @param xlsxFilePath 输出文件路径
     * @throws IOException 
     */
    public static void outputXlsxToFile(XSSFWorkbook workbook, String xlsxFilePath) throws IOException {
    	//创建一个文件
        File file = new File(xlsxFilePath);
        if (file.exists()) {
			file.delete();
		}
        file.createNewFile();
        //创建输出流 
        FileOutputStream stream = FileUtils.openOutputStream(file);
        //将拼好的Excel写入到文件流
        workbook.write(stream);
        //关闭输出流
        stream.close();
	}
}
