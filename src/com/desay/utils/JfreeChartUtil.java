package com.desay.utils;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * @author uidq0460
 * 生成excel柱状图的工具类
 */
public class JfreeChartUtil {

	/**
	 * 生成图表文件（图片格式），需要插入excel中
	 * @param list 数据对象
	 * @param title 图表标题
	 * @param xTitle X轴标题
	 * @param yTitle Y轴标题
	 * @param num Y轴刻度单位
	 * @return 图片文件
	 */
	public static File createChartBar(List<Object[]> list,String title,String xTitle,String yTitle,String num){  
	          
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();   
	        for(int i=0;list!=null && i<list.size();i++){  
	            Object [] obj = list.get(i);  
	            dataset.addValue(Integer.parseInt(obj[0].toString()),"CRASH总量", "第" + (i + 1) + "个");  
	            dataset.addValue(Integer.parseInt(obj[1].toString()),"ANR总量", "第" + (i + 1) + "个");  
	            dataset.addValue(Integer.parseInt(obj[2].toString()),"发生错误\r\n的应用程\r\n序总量", "第" + (i + 1) + "个");  
	        }  
	
	        JFreeChart  chart = ChartFactory.createBarChart(title, //图表的主标题   
	                                            xTitle,//X轴（种类轴）外的标题  
	                                            yTitle,//Y轴（值轴）外的标题   
	                                            dataset,  //数据的集合  
	                                            PlotOrientation.VERTICAL, //图形的显示形式（水平/垂直）  
	                                            true, //是否生成子标题   
	                                            true, //是否生成提示的工具  
	                                            true);//是否在图像上生成URL路径  
	          
	        //处理乱码  
	        //处理主标题乱码  
	        chart.getTitle().setFont(new Font("宋体",Font.BOLD,18));  
	        //处理子标题乱码  
	        chart.getLegend().setItemFont(new Font("宋体",Font.BOLD,12));//右侧显示文字
	        chart.getLegend().setPosition(RectangleEdge.RIGHT);//右侧显示子菜单  
	        //调出图表区域对象  
	        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();  
	        //获取到X轴  
	        CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();  
	        //获取到Y轴  
	        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();  
	        //处理X轴外的乱码  
	        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,15));    //X轴坐标标题  
	        //处理X轴上的乱码  
	        categoryAxis.setTickLabelFont(new Font("宋体",Font.BOLD,10));//X轴坐标文字  
	        //处理Y轴外的乱码  
	        numberAxis.setLabelFont(new Font("宋体",Font.BOLD,15));      //Y轴坐标标题  
	        //处理Y轴上的乱码  
	        numberAxis.setTickLabelFont(new Font("宋体",Font.BOLD,10));  //Y轴坐标文字
	        categoryAxis.setMaximumCategoryLabelWidthRatio(5f);//X轴刻度文字显示宽度 
//	        categoryAxis.setCategoryMargin(0.1);
	        
	        //处理Y轴上的刻度，默认从1开始  
	        numberAxis.setAutoTickUnitSelection(false);  
	        NumberTickUnit unit = new NumberTickUnit(Integer.parseInt(num));  
	        numberAxis.setTickUnit(unit);  
	          
	        //处理图形，先要获取绘图区域对象  
	        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();  
	        barRenderer.setItemMargin(0);//设置每一项目item间隔
	        //设置图形的宽度  
	//      barRenderer.setMaximumBarWidth(0.1);  
	          
	        //在图形上显示对应数值  
	        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
	        barRenderer.setBaseItemLabelsVisible(true);  
	        barRenderer.setBaseItemLabelFont(new Font("宋体",Font.BOLD,15));  //圆柱上上方显示的值
	          
	        String filename = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".png";  //DateFormatUtils
	        File file = new File(filename);  
	        try {  
	             ChartUtilities.saveChartAsPNG(file,chart,600,500);  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return file;  
	    }  
	
	/**
	 * 把生成的图表图片插入到excel某个sheet中
	 * @param wb excel文档
	 * @param sheet 指定sheet
	 * @param file 图片文件
	 */
	@SuppressWarnings({ "rawtypes", "deprecation", "static-access" })
	public static void putImageToSheet(XSSFWorkbook wb,XSSFSheet sheet,File file) {  
        
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();  
        BufferedImage bufferImg;  
        try {  
            bufferImg = ImageIO.read(file);  
            ImageIO.write(bufferImg, "png", byteArrayOut);  
            Drawing dp = sheet.createDrawingPatriarch();  
            XSSFClientAnchor anchor = new XSSFClientAnchor(0,0,512,255,0,0,12,22);//设置图表在excel中位置  
            anchor.setAnchorType(2);  
            dp.createPicture(anchor,wb.addPicture(byteArrayOut.toByteArray(),wb.PICTURE_TYPE_PNG)).resize(2);  
            if (file.exists()) {
    			file.delete();
    		}   
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
    } 
}
