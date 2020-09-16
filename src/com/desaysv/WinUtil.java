package com.desaysv;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


public class WinUtil {

	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
	public static boolean deleteDir(File dir, Shell shell) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), shell);
                if (!success) {
                	MessageBox box = new MessageBox(shell ,SWT.YES | SWT.ICON_WARNING);
					//设置对话框的标题
					box.setText("提示");
					//设置对话框显示的消息
					box.setMessage(children[i] + "正在使用中，删除失败！");
					//打开对话框
					box.open();
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    
	/**
     * 设置窗口位于屏幕中间
     * @param display 设备
     * @param shell 要调整位置的窗口对象
     */
    public static void center(Display display, Shell shell)
    {
        Rectangle bounds = display.getPrimaryMonitor().getBounds();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
    }
    
	/**
	 * 选择目录对话框
	 * @param shell 窗体
	 * @param defaultDir 默认目录（到没有选择目录或找不到目录时默认设置）
	 * @return 目录路径字符串
	 * @Date 2017-04-28
	 */
	public static String showDirectoryDialog(Shell shell, String defaultDir) {
		//选择目录
		String dirctory = "c:\\";
		DirectoryDialog dirctorydialog = new DirectoryDialog(shell);
		dirctorydialog.setText("目录选择对话框");
		dirctorydialog.setFilterPath(dirctory);
		dirctorydialog.setMessage("选择要使用的工作空间目录:");
		dirctory = dirctorydialog.open();
		if (dirctory == null) {
			dirctory = defaultDir;
		}
		System.out.println("Selete save path:" + dirctory);
		return dirctory;
	}
	
	/**
	 * 打开目录
	 * @param dirPath 目录路径
	 */
	public static void openDir(String dirPath) {
		try {
			Runtime.getRuntime().exec("explorer " + dirPath);
		} catch (IOException e1) {
			System.out.println("打开目录失败");
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * 获取文件当前目录
	 * @param pathStr
	 * @return
	 * @Date 2017-05-03
	 */
	public static String getAbsolutePath(String pathStr) {
		File f= new File(pathStr);
		String fileAbsolutePath = "";
	    if (f.exists() && f.isFile()){  
	    	fileAbsolutePath = f.getParent();
	    }else{  
	    	System.out.println("file doesn't exist or is not a file");  
	    }
	    return fileAbsolutePath;
	}
	
	/**
	 * 文件选择对话框
	 * @param shell 窗体
	 * @param selectFilePathStr //默认文件路径
	 * @return String 文件路径
	 */
	public static String fileSeleteDialog(Shell shell, String selectFilePathStr){
		//新建文件对话框，并设置为打开的方式
		FileDialog filedlg=new FileDialog(shell,SWT.OPEN);
		//设置文件对话框的标题
		filedlg.setText("文件选择");
		//设置初始路径
		File file = new File(selectFilePathStr);
		if (file.exists()) {
			filedlg.setFilterPath(file.getParent());
		} else {
			filedlg.setFilterPath("SystemRoot");
		}
		//打开文件对话框，返回选中文件的绝对路径
		String selected = filedlg.open();
		System.out.println("Selete file："+selected);
		if (selected == null) {
			selected = "";
		}
		
		return selected;
	}
	
	/**
	 * 配置文件选择对话框
	 * @param shell 窗体
	 * @param selectFilePathStr //默认文件路径
	 * @return String 文件路径
	 */
	public static String configFileSeleteDialog(Shell shell, String selectFilePathStr){
		//新建文件对话框，并设置为打开的方式
		FileDialog filedlg = new FileDialog(shell,SWT.OPEN);
		//设置文件对话框的标题
		filedlg.setText("文件选择");
		filedlg.setFilterNames(new String[] { "配置文件 (*.properties)" });//设置扩展名
		filedlg.setFilterExtensions(new String[] {"*.properties"});
		//设置初始路径
		File file = new File(selectFilePathStr);
		if (file.exists()) {
			filedlg.setFilterPath(file.getAbsolutePath());
		} else {
			filedlg.setFilterPath("SystemRoot");
		}
		//打开文件对话框，返回选中文件的绝对路径
		String selected = filedlg.open();
		if (selected == null) {
			selected = "";
		}
		
		return selected;
	}
	
	
	
	/**
	 * 保存配置文件对话框
	 * @param shell
	 * @param defaultPath
	 * @return
	 */
	public static String savePropFileDialog(Shell shell, String defaultPath) {
		FileDialog filedlg=new FileDialog(shell,SWT.SAVE);
		filedlg.setFilterPath(defaultPath);
		filedlg.setFilterExtensions(new String[] {"properties"});
		filedlg.setFileName(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".properties");
		String selected = filedlg.open();
		if (selected == null ) {
			selected = defaultPath;
		}
		if (!selected.endsWith(".properties")) {
			selected += ".properties";
		}
		return selected;
	}
	
	/**
	 * 保存配置文件对话框
	 * @param shell
	 * @param defaultPath
	 * @return
	 */
	public static String saveExcelDialog(Shell shell, String defaultPath) {
		FileDialog filedlg = new FileDialog(shell,SWT.SAVE);
		filedlg.setFilterPath(defaultPath);
		filedlg.setFilterExtensions(new String[] {"xlsx"});
		filedlg.setFileName("MonkeyReport_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".xlsx");
		String selected = filedlg.open();
		if (selected == null ) {
			selected = "";
		} else {
			if (!selected.endsWith(".xlsx")) {
				selected += ".xlsx";
			}
		}
		return selected;
	}
	
	/**
	 * 可以多选apk文件的对话框
	 * @param shell
	 * @param selectFilePathStr
	 * @return
	 */
	public static List<String> apkFilesSeleteDialog(Shell shell, String selectFilePathStr){
		List<String> fileList = Collections.synchronizedList(new ArrayList<String>());
		
		//新建文件对话框，并设置为打开的方式
		FileDialog filedlg=new FileDialog(shell,SWT.MULTI);
		//设置文件对话框的标题
		filedlg.setText("文件选择");
		filedlg.setFilterNames(new String[] { "apk文件 (*.apk)" });//设置扩展名
		filedlg.setFilterExtensions(new String[] {"*.apk"});
		//设置初始路径
		File file = new File(selectFilePathStr);
		if (file.exists()) {
			filedlg.setFilterPath(file.getParent());
		} else {
			filedlg.setFilterPath("SystemRoot");
		}
		//打开文件对话框，返回选中文件的绝对路径
		String selected = filedlg.open();
		System.out.println("Selete file："+selected);
		if (selected == null) {
			selected = selectFilePathStr;
		} else {
			if (!selected.equals("")) {
				File firstSelectFile = new File(selected);
				String dir = firstSelectFile.getParent();
				
				String[] selFilesStr = filedlg.getFileNames();
				for (String string : selFilesStr) {
					if (dir.endsWith(File.separator)) {
						fileList.add(dir + string);
					} else {
						fileList.add(dir + File.separator + string);
					}
				}
			}
		}
		
		return fileList;
	}
	
}
