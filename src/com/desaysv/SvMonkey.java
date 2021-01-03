package com.desaysv;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.desay.utils.PropUtil;
import com.desay.utils.RecordUtils;
import com.desaysv.bean.AnrCrash;
import com.desaysv.bean.Device;
import com.desaysv.bean.DeviceInfo;
import com.desaysv.bean.DeviceRecord;
import com.desaysv.bean.MonkeyTaskBean;
import com.desaysv.bean.PackageErrorInfo;
import com.desaysv.bean.Record;
import com.desay.utils.AnrCrashUtils;
import com.desay.utils.MyUtils;
import com.desay.utils.PoiUtils;
import org.eclipse.wb.swt.SWTResourceManager;

/**   
* @Title: SvMonkey.java 
* @Package com.desaysv 
* @Description: 
* @author Tonsen    E-mail: 470029260@qq.com
* @date 2018年8月15日 下午4:18:45 
* @version V1.0  
* @user Tonsen
* @update 
* 2018年8月15日 下午4:18:45 
* Monkey测试工具客户端
* 
* 更新：
* 2020年9月16日
*     1、修复permissonPercent不保存的问题。
* 2021年1月03日
* 	  1、设置Touch和Motion事件默认各为50,
*     2、增加rotaion参数配置。
*/
public class SvMonkey {

	protected Shell shlSvMonkey;
	private Display display;
	
	private Tree treePackageList;
	private Text textThrottleMillisec;
	private Text textLogLevel;
	private Text textSeedValue;
	private Text textTrackballPercent;
	private Text textAppswitchPercent;
	private Text textFlipPercent;
	private Text textTouchPercent;
	private Text textSyskeyPercent;
	private Table tableMonkeyRun;
	private Table tableLogList;
	private Text textAnyeventPercent;
	private Text textPinchzoonPercent;
	private Text textNavPercent;
	private Text textMajornavPercent;
	private Text textMotionPercent;
	private Button btnDetectDevice;
	private Label lblSelectedDevice;
	private Label currentDevicelbl;
	private Button btnMonitorNativeCrash;
	private Button btnIgnoreNativeCrashes;
	private Button btnIgnoreSecurityExceptions;
	private Button btnIgnoreTimeouts;
	private Button btnIgnoreCrashes;
	private Button btnPermissionTargetSystem;
	private Label lblCostTimeShow;
	private Text textTrace;
	private Label lblInfoName;
	private Label lblInternalName;
	private Label lblAndroidVersion;
	private Label lblBuildDate;
	private Label lblInfoNameValue;
	private Label lblInternalNameValue;
	private Label lblAndroidVersionValue;
	private Label lblBuildDateValue;
	private TableColumn tableColumnNum;
	private TableColumn tableColumnID;
	private TableColumn tableColumnTestStatus;
	private TableColumn tableColumnCrashCounter;
	private TableColumn tableColumnAnrCounter;
	private TableColumn tableColumnLogPath;
	private TableColumn tableColumnMonkeyCmd;
	private TableColumn tableColumnRunningTime;
	
	private static final int NUM_WIDTH = 40;
	private static final int ID_WIDTH = 127;
	private static final int STATUS_WIDTH = 61;
	private static final int CRASH_WIDTH = 81;
	private static final int ANR_WIDTH = 65;
	private static final int TIME_WIDTH = 88;
	private static final int USED_WIDTH = NUM_WIDTH + ID_WIDTH + STATUS_WIDTH + CRASH_WIDTH + ANR_WIDTH + TIME_WIDTH;
	
	List<String> devicesIdList;
	List<Device> devicesList;		//设备列表
	private Text textEventCount;
	public String currentMonkeyCmd = "adb -s 2e6408f032520002 shell  monkey ";
	public List<Record> currentRecordList;
	public String currentDeviceId = "";
	private String propFilePath;
	private String saveShellWidth;
	private String saveShellHeight;
	public static boolean programRunning = true;
	
	public static final String ALL_PACKAGES = "所有应用";
	public static final String SYSTEM_PACKAGES = "系统应用";
	public static final String THIRD_PACKAGES = "第三方应用";
	public static final String WHITE_LIST = "白名单";
	public static final String BLACK_LIST = "黑名单";
	public static final String SEARCH_PACKAGES = "搜索包";
	//多个待测应用，测试时用对应设备的参数依次执行该列表下所有应用的测试
	public static final String ORDER_LIST = "顺序测试名单";
	
	public static final int INDEX_ALL_PACKAGES = 0;
	public static final int INDEX_SYSTEM_PACKAGES = 1;
	public static final int INDEX_THIRD_PACKAGES = 2;
	public static final int INDEX_WHITE_PACKAGES = 3;
	public static final int INDEX_BLACK_PACKAGES = 4;
	public static final int INDEX_ORDER_PACKAGES = 5;
	
	/**
	 * 工作空间目录
	 */
	public static String settingsPropPath = System.getProperty("user.dir") + "\\settings.properties";//设置配置文件
	public static String USERDIR = System.getProperty("user.dir");//工作路径
	public static String currentLogPath = USERDIR + "\\2e6408f032520002";
	public static String recordFilePath = USERDIR + "\\record.txt";
	public static final String DEVICE_INFO_FILE_NAME = "deviceInfo.txt";
	
	//当前的monkey线程
	private static ConcurrentMap<String, MonkeyTaskBean> monkeyThreadMap = new ConcurrentHashMap<>();
	public static boolean stopMonkeyByHand = false;
	
	
	//adb命令是否忙碌，如果忙碌则不能进行root、重启adb服务等操作，用来防止多次按下root等快捷键操作adb
	private static boolean isAdbBusy = false;
	private Label lblPermissionPercent;
	private Text textPermissonPercent;
	
	
	//设置
	private static String endGetLogs = "";
	private static String sendEmail = "";
	private static String endTakePic = "";
	private static String getLogCmdFilePath = System.getProperty("user.dir") + "\\getLogCmds.txt";
	private Button btnbugreport;
	private Label lblRotaionPercent;
	private Text textRotaionPercent;
	

	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SvMonkey window = new SvMonkey();
			window.open();
			RecordUtils.interruptSaveRecord();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		display.addFilter(SWT.KeyDown, new Listener()//全局快捷键
		{
			public void handleEvent(Event e)
			{
				if ((e.stateMask == SWT.CTRL) && (e.keyCode == KeyCodePc.KEY_F))
				{
					if (isAdbBusy == false) {//空闲
						getDevices();
					}
				} else if ((e.stateMask == SWT.CTRL) && (e.keyCode == KeyCodePc.KEY_R)) {
					String deviceId = lblSelectedDevice.getText().trim();
					if (isAdbBusy == false) {//adb空闲
						if (!deviceId.equals("")) {
							textTrace.setText("");
							isAdbBusy = true;//开始忙碌
							new Thread(new Runnable() {
								@Override
								public void run() {
							    	display.asyncExec(new Runnable(){
							    		public void run() {
							    			ProgressMonitorDialog dialog = new ProgressMonitorDialog(shlSvMonkey.getShell());
											try {
												dialog.run(true, false, new IRunnableWithProgress() {
												    @Override
												    public void run(IProgressMonitor monitor) throws InvocationTargetException,
												                                                            InterruptedException {
												    	
												    	monitor.subTask("正在root设备...");
												    	try {
															if (!rootDevice(deviceId)) {
																monitor.done();
																MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																//设置对话框的标题
																box.setText("警告");
																//设置对话框显示的消息
																box.setMessage("Root失败，请检查设备是否已开启root！");
																//打开对话框
																box.open();
															} else {
																monitor.subTask("root成功！");
																monitor.done();
															}
															isAdbBusy = false;//空闲
														} catch (IOException e) {
															monitor.done();
															isAdbBusy = false;//空闲
															showError("root失败，请检查", e);
														}
												    }
												});
											} catch (InvocationTargetException e) {
												isAdbBusy = false;//空闲
												showError("root失败，请检查", e);
											} catch (InterruptedException e) {
												isAdbBusy = false;//空闲
												showError("root失败，请检查", e);
											}
							    		}
							    	});
								}
							}).start();
						} else {
							MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
							//设置对话框的标题
							box.setText("警告");
							//设置对话框显示的消息
							box.setMessage("请先识别并选择设备！");
							//打开对话框
							box.open();
						}
					}
				} else if ((e.stateMask == SWT.CTRL) && (e.keyCode == KeyCodePc.KEY_D)) {
					textTrace.setText("");
					if (isAdbBusy == false) {//adb不忙碌的时候可以执行
						isAdbBusy = true;    //开始忙碌
						new Thread(new Runnable() {
							@Override
							public void run() {
								display.asyncExec(new Runnable(){
									public void run() {
										ProgressMonitorDialog dialog = new ProgressMonitorDialog(shlSvMonkey.getShell());
										try {
											dialog.run(true, false, new IRunnableWithProgress() {
												@Override
												public void run(IProgressMonitor monitor) throws InvocationTargetException,
												InterruptedException {
													
													monitor.subTask("正在重启ADB服务...");
													killADB();
													monitor.subTask("已重启adb服务");
													monitor.done();
													isAdbBusy = false;//空闲
												}
											});
										} catch (InvocationTargetException e) {
											isAdbBusy = false;//空闲
											showError("重启adb服务失败，请检查", e);
										} catch (InterruptedException e) {
											isAdbBusy = false;//空闲
											showError("重启adb服务失败，请检查", e);
										}
									}
								});
								
							}
						}).start();
					}
				}
			}
		});
		createContents();
		WinUtil.center(display, shlSvMonkey);
		shlSvMonkey.setLayout(new FormLayout());
		shlSvMonkey.addShellListener(new ShellListener() {  
            public void shellActivated(ShellEvent e) {
            	
            }  
  
            public void shellClosed(ShellEvent e) { 
            	programRunning = false;
            }  
  
            public void shellDeactivated(ShellEvent e) { 
            	
            }  
  
            public void shellDeiconified(ShellEvent e) {  
            }  
  
            public void shellIconified(ShellEvent e) {  
            	
            }  
        });
		
		Group grpControl = new Group(shlSvMonkey, SWT.NONE);
		grpControl.setText("设备信息");
		FormData fd_grpControl = new FormData();
		fd_grpControl.top = new FormAttachment(0, 10);
		fd_grpControl.left = new FormAttachment(0, 10);
		grpControl.setLayoutData(fd_grpControl);
		
		Label label = new Label(shlSvMonkey, SWT.SEPARATOR | SWT.VERTICAL);
		fd_grpControl.right = new FormAttachment(label);
		
		btnDetectDevice = new Button(grpControl, SWT.NONE);
		btnDetectDevice.setBounds(10, 23, 70, 27);
		btnDetectDevice.setText("识别设备");
		btnDetectDevice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDevices();
			}
		});
		
		lblSelectedDevice = new Label(grpControl, SWT.NONE);
		lblSelectedDevice.setText(" ");
		lblSelectedDevice.setBounds(119, 28, 161, 17);
		lblSelectedDevice.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {//老鼠按下
            	if (e.button == 1) {			 //鼠标左键
					textTrace.setText("");
					textTrace.append(lblSelectedDevice.getText());
				}
            }
		});
		
		
		currentDevicelbl = new Label(grpControl, SWT.NONE);
		currentDevicelbl.setText("当前:");
		currentDevicelbl.setBounds(81, 28, 32, 17);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(0, 300);
		fd_label.top = new FormAttachment(0, 10);
		fd_label.bottom = new FormAttachment(100, -10);
		label.setLayoutData(fd_label);
		
		Group grpMonkeySettings = new Group(shlSvMonkey, SWT.NONE);
		fd_label.right = new FormAttachment(grpMonkeySettings, -6);
		grpMonkeySettings.setText("Monkey参数设置");
		FormData fd_grpMonkeySettings = new FormData();
		fd_grpMonkeySettings.top = new FormAttachment(0, 10);
		fd_grpMonkeySettings.left = new FormAttachment(0, 324);
		grpMonkeySettings.setLayoutData(fd_grpMonkeySettings);
//		grpMonkeySettings.setToolTipText("所有百分比加起来必须等于100%或不填，否则monkey不能进行测试");
		grpMonkeySettings.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  //老鼠按下
            	if (e.button == SWT.MouseDown) {   //鼠标右键
            		Menu menu = new Menu(grpMonkeySettings);
            		grpMonkeySettings.setMenu(menu);
    				MenuItem userDefaultMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
    				userDefaultMi.setText("加载默认参数");
    				userDefaultMi.addListener(SWT.Selection, new Listener() {
    					public void handleEvent(Event e) {  	//向指定用户发送消息!
    						if (currentDeviceId != null && !currentDeviceId.equals("")) {
    							PropUtil.setMonkeyParameter(currentDeviceId, new Device());
    							updateConfigUi(currentDeviceId);
							} else {
								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
								//设置对话框的标题
								box.setText("警告");
								//设置对话框显示的消息
								box.setMessage("请先识别并选择要应用默认参数的设备！");
								//打开对话框
								box.open();
							}
    					}
    				});
            	}
            }
		});
		
		btnIgnoreCrashes = new Button(grpMonkeySettings, SWT.CHECK);
		btnIgnoreCrashes.setBounds(10, 24, 118, 17);
		btnIgnoreCrashes.setText("--ignore-crashes");
		btnIgnoreCrashes.setToolTipText("忽略CRASH继续测试");
		btnIgnoreCrashes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				if (btnIgnoreCrashes.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_CRASHES, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_CRASHES, "false");
				}
			}
		});
		
		btnIgnoreTimeouts = new Button(grpMonkeySettings, SWT.CHECK);
		btnIgnoreTimeouts.setText("--ignore-timeouts");
		btnIgnoreTimeouts.setToolTipText("忽略ANR继续测试");
		btnIgnoreTimeouts.setBounds(10, 47, 129, 17);
		btnIgnoreTimeouts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				if (btnIgnoreTimeouts.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_TIMEOUTS, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_TIMEOUTS, "false");
				}
			}
		});
		
		btnIgnoreSecurityExceptions = new Button(grpMonkeySettings, SWT.CHECK);
		btnIgnoreSecurityExceptions.setText("--ignore-security-exceptions");
		btnIgnoreSecurityExceptions.setToolTipText("忽略应用程序许可错误（如证书许可，网络许可等）继续测试");
		btnIgnoreSecurityExceptions.setBounds(10, 70, 185, 17);
		btnIgnoreSecurityExceptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				if (btnIgnoreSecurityExceptions.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, "false");
				}
			}
		});
		
		btnIgnoreNativeCrashes = new Button(grpMonkeySettings, SWT.CHECK);
		btnIgnoreNativeCrashes.setText("--ignore-native-crashes");
		btnIgnoreNativeCrashes.setToolTipText("忽略本地CRASH继续测试");
		btnIgnoreNativeCrashes.setBounds(10, 116, 185, 17);
		btnIgnoreNativeCrashes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				if (btnIgnoreNativeCrashes.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_NATEVE_CRASHES, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_IGNORE_NATEVE_CRASHES, "false");
				}
			}
		});
		
		btnMonitorNativeCrash = new Button(grpMonkeySettings, SWT.CHECK);
		btnMonitorNativeCrash.setText("--monitor-native-crash");
		btnMonitorNativeCrash.setToolTipText("监视本地CRASH");
		btnMonitorNativeCrash.setBounds(10, 93, 185, 17);
		btnMonitorNativeCrash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				if (btnMonitorNativeCrash.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_MONITOR_NATIVE_CRASH, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_MONITOR_NATIVE_CRASH, "false");
				}
			}
		});
		
		Label label_1 = new Label(grpMonkeySettings, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setBounds(239, 24, 2, 136);
		
		Label lblThrottleMillisec = new Label(grpMonkeySettings, SWT.NONE);
		lblThrottleMillisec.setBounds(247, 24, 97, 17);
		lblThrottleMillisec.setText("Throttle Millisec");
		lblThrottleMillisec.setToolTipText("指定用户操作（即事件）间的时延，单位是毫秒");
		
		Label lblLogLevel = new Label(grpMonkeySettings, SWT.NONE);
		lblLogLevel.setText("Log Level");
		lblLogLevel.setBounds(247, 47, 97, 17);
		lblLogLevel.setToolTipText("Monkey日志信息详细等级");
		
		Label lblSeedValue = new Label(grpMonkeySettings, SWT.NONE);
		lblSeedValue.setText("Seed");
		lblSeedValue.setBounds(247, 70, 97, 17);
		lblSeedValue.setToolTipText("随机种子值");
		
		textThrottleMillisec = new Text(grpMonkeySettings, SWT.BORDER);
		textThrottleMillisec.setBounds(350, 23, 73, 18);
		textThrottleMillisec.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {   
		    	// 处理失去焦点的事件 
		    	String throttleStr = textThrottleMillisec.getText();
				String eventCountStr = textEventCount.getText();
				if (!throttleStr.trim().equals("") && !throttleStr.trim().equals("0") && !eventCountStr.trim().equals("") && !eventCountStr.trim().equals("0")) {
					try {
						lblCostTimeShow.setText(MyUtils.millisToTimeMinuteCn(Long.parseLong(throttleStr.trim()) * Long.parseLong(eventCountStr.trim())));
					} catch (NumberFormatException e2) {
					}
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.THROTTLE_MILLISEC, throttleStr);
				}
		    }    
		});  
		
		textLogLevel = new Text(grpMonkeySettings, SWT.BORDER);
		textLogLevel.setBounds(350, 46, 73, 18);
		textLogLevel.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String logLevelStr = textLogLevel.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.LOG_LEVEL, logLevelStr);
		    }    
		});
		
		textSeedValue = new Text(grpMonkeySettings, SWT.BORDER);
		textSeedValue.setBounds(350, 69, 73, 18);
		textSeedValue.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String seedStr = textSeedValue.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.SEED, seedStr);
		    }    
		});   
		
		Label label_2 = new Label(grpMonkeySettings, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(429, 20, 11, 136);
		
		Label lblTrackballPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblTrackballPercent.setText("Trackball Percent");
		lblTrackballPercent.setBounds(619, 116, 108, 17);
		lblTrackballPercent.setToolTipText("轨迹事件的百分比(轨迹事件由一个或几个随机的移动组成，有时还伴随有点击)");
		
		Label lblAppswitchPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblAppswitchPercent.setText("AppSwitch Percent");
		lblAppswitchPercent.setBounds(444, 93, 108, 17);
		lblAppswitchPercent.setToolTipText("Activity启动事件百分比");
		
		Label lblFlipPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblFlipPercent.setText("Flip Percent");
		lblFlipPercent.setBounds(444, 70, 108, 17);
		lblFlipPercent.setToolTipText("键盘翻转事件百分比");
		
		Label lblTouchPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblTouchPercent.setText("Touch Percent");
		lblTouchPercent.setBounds(446, 24, 108, 17);
		lblTouchPercent.setToolTipText("触摸事件的百分比(触摸事件是一个down-up事件)");
		
		Label lblSyskeyPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblSyskeyPercent.setText("SysKeys Percent");
		lblSyskeyPercent.setBounds(444, 116, 108, 17);
		lblSyskeyPercent.setToolTipText("系统按键事件的百分比(这些按键通常被保留，由系统使用，如Home、Back、Start Call、End Call及音量控制键)");
		
		textTrackballPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textTrackballPercent.setBounds(734, 116, 38, 18);
		textTrackballPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String trackBallStr = textTrackballPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.TRACK_BALL_PERCENT, trackBallStr);
		    }    
		});
		
		textAppswitchPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textAppswitchPercent.setBounds(558, 90, 38, 18);
		textAppswitchPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String appSwitchStr = textAppswitchPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.APP_SWITCH_PERCENT, appSwitchStr);
		    }    
		});
		
		textFlipPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textFlipPercent.setBounds(558, 69, 38, 18);
		textFlipPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String flipStr = textFlipPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.FLIP_PERCENT, flipStr);
		    }    
		});
		
		textTouchPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textTouchPercent.setBounds(558, 23, 38, 18);
		textTouchPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String touchStr = textTouchPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.TOUCH_PERCENT, touchStr);
		    }    
		});
		
		textSyskeyPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textSyskeyPercent.setBounds(558, 115, 38, 18);
		textSyskeyPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String sysKeyStr = textSyskeyPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.SYS_KEY_PERCENT, sysKeyStr);
		    }    
		});
		
		Label label_3 = new Label(grpMonkeySettings, SWT.SEPARATOR);
		label_3.setBounds(602, 20, 11, 136);
		
		Label lblAnyeventPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblAnyeventPercent.setText("AnyEvent Percent");
		lblAnyeventPercent.setBounds(619, 24, 108, 17);
		lblAnyeventPercent.setToolTipText("其它类型事件的百分比。它包罗了所有其它类型的事件，如：按键、其它不常用的设备按钮、等等（这个按键可能会按到power，因此建议设置为0）");
		
		Label lblPinchzoonPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblPinchzoonPercent.setText("PinchZoon Percent");
		lblPinchzoonPercent.setBounds(619, 47, 108, 17);
		lblPinchzoonPercent.setToolTipText("缩放事件百分比");
		
		Label lblNavPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblNavPercent.setText("Nav Percent");
		lblNavPercent.setBounds(619, 70, 108, 17);
		lblNavPercent.setToolTipText("基本导航事件的百分比(导航事件由来自方向输入设备的up/down/left/right组成)");
		
		Label lblMajornavPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblMajornavPercent.setText("MajorNav Percent");
		lblMajornavPercent.setBounds(619, 93, 108, 17);
		lblMajornavPercent.setToolTipText("主要导航事件的百分比(这些导航事件通常引发图形界面中的动作，如：5-way键盘的中间按键、回退按键、菜单按键)");
		
		Label lblMotionPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblMotionPercent.setText("Motion Percent");
		lblMotionPercent.setBounds(446, 47, 108, 17);
		lblMotionPercent.setToolTipText("滑动事件的百分比(滑动事件由屏幕上某处的一个down事件、一系列的伪随机事件和一个up事件组成)");
		
		
		lblInfoName = new Label(grpControl, SWT.NONE);
		lblInfoName.setBounds(10, 56, 65, 17);
		lblInfoName.setText("设备名称:");
		
		lblInternalName = new Label(grpControl, SWT.NONE);
		lblInternalName.setText("系统版本:");
		lblInternalName.setBounds(10, 79, 65, 17);
		
		lblAndroidVersion = new Label(grpControl, SWT.NONE);
		lblAndroidVersion.setText("安卓版本:");
		lblAndroidVersion.setBounds(10, 102, 65, 17);
		
		lblBuildDate = new Label(grpControl, SWT.NONE);
		lblBuildDate.setText("编译日期:");
		lblBuildDate.setBounds(10, 125, 65, 17);
		
		lblInfoNameValue = new Label(grpControl, SWT.NONE);
		lblInfoNameValue.setBounds(81, 56, 199, 17);
		lblInfoNameValue.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  //老鼠按下
            	if (e.button == 1) {//鼠标左键
					textTrace.setText("");
					textTrace.append(lblInfoNameValue.getText());
				}
            }
		});
		
		lblInternalNameValue = new Label(grpControl, SWT.NONE);
		lblInternalNameValue.setBounds(81, 79, 199, 17);
		lblInternalNameValue.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  //老鼠按下
            	if (e.button == 1) {//鼠标左键
					textTrace.setText("");
					textTrace.append(lblInternalNameValue.getText());
				}
            }
		});
		
		lblAndroidVersionValue = new Label(grpControl, SWT.NONE);
		lblAndroidVersionValue.setBounds(81, 102, 199, 17);
		lblAndroidVersionValue.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  //老鼠按下
            	if (e.button == 1) {//鼠标左键
					textTrace.setText("");
					textTrace.append(lblAndroidVersionValue.getText());
				}
            }
		});
		
		lblBuildDateValue = new Label(grpControl, SWT.NONE);
		lblBuildDateValue.setBounds(81, 125, 199, 17);
		lblBuildDateValue.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  //老鼠按下
            	if (e.button == 1) {//鼠标左键
					textTrace.setText("");
					textTrace.append(lblBuildDateValue.getText());
				}
            }
		});
		
		
		treePackageList = new Tree(shlSvMonkey, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		fd_grpControl.bottom = new FormAttachment(treePackageList, -6);
		FormData fd_treePackageList = new FormData();
		fd_treePackageList.top = new FormAttachment(0, 165);
		fd_treePackageList.bottom = new FormAttachment(100, -10);
		fd_treePackageList.right = new FormAttachment(label);
		fd_treePackageList.left = new FormAttachment(0, 10);
		
		treePackageList.setLayoutData(fd_treePackageList);
		treePackageList.addMouseListener(new MouseAdapter() {  
            public void mouseDown(MouseEvent e) {  					//老鼠按下
            	TreeItem selectedTreeItem = treePackageList.getItem(new Point(e.x, e.y));//取节点控件
            	if (selectedTreeItem != null && e.button==3) {  	//如果取到节点控件，且是鼠标右键
            		Menu menu = new Menu(treePackageList);  		//为节点建POPUP菜单
            		if(selectedTreeItem.getParentItem() != null)  	//如果不是根节点（不是设备ID）
            		{
            			if (selectedTreeItem.getParentItem().getText().equals(ALL_PACKAGES)) {//点击的是所有包下面的列表
            				//添加到白名单
            				MenuItem addToWhiteMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				addToWhiteMi.setText("添加到白名单");
            				addToWhiteMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
            				addToWhiteMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  			//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            						String selected = (String)mi.getData();		//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.WHITE_LIST)) {//白名单中不存在时才添加
            								TreeItem whiteTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_WHITE_PACKAGES);//1为白名单，0为所有包
            								TreeItem treeItemWhite = new TreeItem(whiteTreeItem, 0);
            								treeItemWhite.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.WHITE_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到黑名单
            				MenuItem addToBlackMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToBlackMi.setText("添加到黑名单");
            				addToBlackMi.setData(selectedTreeItem.getText());
            				addToBlackMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.BLACK_LIST)) {
            								TreeItem blackTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_BLACK_PACKAGES);
            								TreeItem treeItemBlack = new TreeItem(blackTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.BLACK_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到顺序测试名单
            				MenuItem addToOrderMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToOrderMi.setText("添加到顺序测试名单");
            				addToOrderMi.setData(selectedTreeItem.getText());
            				addToOrderMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.ORDER_LIST)) {
            								TreeItem orderTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_ORDER_PACKAGES);
            								TreeItem treeItemBlack = new TreeItem(orderTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.ORDER_LIST);
            							}
            						}
            					}
            				});
            				
            				//在所有包下右键弹出，对选择的单个包执行monkey测试
            				MenuItem oneAppMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppMonkeyMi.setText("对此程序包进行测试");
            				oneAppMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							rootTreeItem.setExpanded(false);
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread onePackageThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    											}
    										});
            								onePackageThread.setDaemon(true);
            								onePackageThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getParentItem().getText().equals(SYSTEM_PACKAGES)) {
            				//添加到白名单
            				MenuItem addToWhiteMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				addToWhiteMi.setText("添加到白名单");
            				addToWhiteMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
            				addToWhiteMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  			//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            						String selected = (String)mi.getData();		//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();//获取顶层的设备ID
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.WHITE_LIST)) {//白名单中不存在时才添加
            								TreeItem whiteTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_WHITE_PACKAGES);//1为白名单，0为所有包
            								TreeItem treeItemWhite = new TreeItem(whiteTreeItem, 0);
            								treeItemWhite.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.WHITE_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到黑名单
            				MenuItem addToBlackMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToBlackMi.setText("添加到黑名单");
            				addToBlackMi.setData(selectedTreeItem.getText());
            				addToBlackMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.BLACK_LIST)) {
            								TreeItem blackTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_BLACK_PACKAGES);//2为黑名单
            								TreeItem treeItemBlack = new TreeItem(blackTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.BLACK_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到顺序测试名单
            				MenuItem addToOrderMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToOrderMi.setText("添加到顺序测试名单");
            				addToOrderMi.setData(selectedTreeItem.getText());
            				addToOrderMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.ORDER_LIST)) {
            								TreeItem orderTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_ORDER_PACKAGES);//2为黑名单
            								TreeItem treeItemBlack = new TreeItem(orderTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.ORDER_LIST);
            							}
            						}
            					}
            				});
            				
            				//在所有包下右键弹出，对选择的单个包执行monkey测试
            				MenuItem oneAppMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppMonkeyMi.setText("对此程序包进行测试");
            				oneAppMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							rootTreeItem.setExpanded(false);
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread onePackageInAllThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    											}
    										});
            								onePackageInAllThread.setDaemon(true);
            								onePackageInAllThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getParentItem().getText().equals(THIRD_PACKAGES)) {
            				//添加到白名单
            				MenuItem addToWhiteMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				addToWhiteMi.setText("添加到白名单");
            				addToWhiteMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
            				addToWhiteMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  			//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            						String selected = (String)mi.getData();		//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();//获取顶层的设备ID
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.WHITE_LIST)) {//白名单中不存在时才添加
            								TreeItem whiteTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_WHITE_PACKAGES);//1为白名单，0为所有包
            								TreeItem treeItemWhite = new TreeItem(whiteTreeItem, 0);
            								treeItemWhite.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.WHITE_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到黑名单
            				MenuItem addToBlackMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToBlackMi.setText("添加到黑名单");
            				addToBlackMi.setData(selectedTreeItem.getText());
            				addToBlackMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.BLACK_LIST)) {
            								TreeItem blackTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_BLACK_PACKAGES);//2为黑名单
            								TreeItem treeItemBlack = new TreeItem(blackTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.BLACK_LIST);
										}
            						}
            					}
            				});
            				
            				//添加到顺序测试名单
            				MenuItem addToOrderMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				addToOrderMi.setText("添加到顺序测试名单");
            				addToOrderMi.setData(selectedTreeItem.getText());
            				addToOrderMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							if (! PropUtil.isContainInList(selected, deviceId, PropUtil.ORDER_LIST)) {
            								TreeItem orderTreeItem = selectedTreeItem.getParentItem().getParentItem().getItem(INDEX_ORDER_PACKAGES);//2为黑名单
            								TreeItem treeItemBlack = new TreeItem(orderTreeItem, 0);
            								treeItemBlack.setText(selected);
            								PropUtil.appendToFile(selected, deviceId, PropUtil.ORDER_LIST);
            							}
            						}
            					}
            				});
            				
            				//对选择的单个包执行monkey测试
            				MenuItem oneAppMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppMonkeyMi.setText("对此程序包进行测试");
            				oneAppMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							rootTreeItem.setExpanded(false);
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread selectPackageThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    											}
    										});
            								selectPackageThread.setDaemon(true);
            								selectPackageThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
            				//卸载apk
            				MenuItem unInstallThisApp = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				unInstallThisApp.setText("卸载该应用");
            				unInstallThisApp.setData(selectedTreeItem.getText());
            				unInstallThisApp.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							String packageName = selectedTreeItem.getText();
            							if (deviceId !=null && !deviceId.equals("") && packageName != null && !packageName.equals("")) {
											new Thread(new Runnable() {
												@Override
												public void run() {
													boolean uninstallResult = adbUninstallApk(deviceId, packageName);//开始卸载
													if (uninstallResult) {//卸载成功
														display.asyncExec(new Runnable(){
															public void run() {
																getDevices();
																MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
					    										//设置对话框的标题
					    										box.setText("提示");
					    										//设置对话框显示的消息
					    										box.setMessage("卸载成功！");
					    										//打开对话框
					    										box.open();
															}
														});
													} else {
														display.asyncExec(new Runnable(){
															public void run() {
																MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
					    										//设置对话框的标题
					    										box.setText("提示");
					    										//设置对话框显示的消息
					    										box.setMessage("卸载失败，请检查！");
					    										//打开对话框
					    										box.open();
															}
														});
													}
												}
											}).start();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getParentItem().getText().equals(WHITE_LIST)) {
							//右键点击白名单中的应用
							//从白名单删除
            				MenuItem removeFormWhiteMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				removeFormWhiteMi.setText("从白名单移除");
            				removeFormWhiteMi.setData(selectedTreeItem.getText());    //向响应菜单项事件的代码，传递值。
            				removeFormWhiteMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							PropUtil.removeFromFile(selected, deviceId, PropUtil.WHITE_LIST);

            							//删除后需要更新所有包中该条的背景色
            							selectedTreeItem.dispose();
            						}
            					}
            				});
            				//在白名单下右键弹出，对选择的单个包执行monkey测试
            				MenuItem oneAppInWhiteMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppInWhiteMonkeyMi.setText("对此程序包进行测试");
            				oneAppInWhiteMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppInWhiteMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread onePackageInWhiteThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    											}
    										});
            								onePackageInWhiteThread.setDaemon(true);
            								onePackageInWhiteThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getParentItem().getText().equals(BLACK_LIST)) {
							//右键点击黑名单中的应用
							//从黑名单删除
            				MenuItem removeFormBlackMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				removeFormBlackMi.setText("从黑名单移除");
            				removeFormBlackMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
            				removeFormBlackMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							PropUtil.removeFromFile(selected, deviceId, PropUtil.BLACK_LIST);
            							
            							selectedTreeItem.dispose();
            							//删除后需要更新所有包中该条的背景色
            							
            						}
            					}
            				});
            				
            				//在黑名单下右键弹出，对选择的单个包执行monkey测试
            				MenuItem oneAppInBlackMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppInBlackMonkeyMi.setText("对此程序包进行测试");
            				oneAppInBlackMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppInBlackMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread onePackageInBlackThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
//														display.asyncExec (new Runnable () {
//							                        		public void run () {
//							                        			textTrace.append(e.getMessage());
//							                        		}
//							                        	});
    											}
    										});
            								onePackageInBlackThread.setDaemon(true);
            								onePackageInBlackThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getParentItem().getText().equals(ORDER_LIST)) {
							//右键点击顺序测试名单中的应用
							//从黑名单删除
            				MenuItem removeFormBlackMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
            				removeFormBlackMi.setText("从顺序测试名单移除");
            				removeFormBlackMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
            				removeFormBlackMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							String deviceId = selectedTreeItem.getParentItem().getParentItem().getText();
            							PropUtil.removeFromFile(selected, deviceId, PropUtil.ORDER_LIST);
            							
            							selectedTreeItem.dispose();
            							//删除后需要更新所有包中该条的背景色
            							
            						}
            					}
            				});
            				
            				//在顺序测试名单下右键弹出，对选择的单个包执行monkey测试
            				MenuItem oneAppInBlackMonkeyMi = new MenuItem(menu,SWT.PUSH);  //为菜单，建菜单项
            				oneAppInBlackMonkeyMi.setText("对此程序包进行测试");
            				oneAppInBlackMonkeyMi.setData(selectedTreeItem.getText());
            				oneAppInBlackMonkeyMi.addListener(SWT.Selection, new Listener() {
            					public void handleEvent(Event e) {  	//向指定用户发送消息!
            						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
            						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
            						if(selected != null)
            						{ //添加，响应代码 
            							//执行单个指定包的monkey测试
            							TreeItem rootTreeItem = selectedTreeItem.getParentItem().getParentItem();
            							String deviceId = rootTreeItem.getText();
            							
            							String monkeyCmdStr = getMonkeyCmd(rootTreeItem.getText(), selected);
            							if (!RecordUtils.isRunning(deviceId)) {
            								Thread onePackageInOrderThread = new Thread(new Runnable() {
    											@Override
    											public void run() {
													monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    											}
    										});
            								onePackageInOrderThread.setDaemon(true);
            								onePackageInOrderThread.start();
            							} else {
            								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    										//设置对话框的标题
    										box.setText("警告");
    										//设置对话框显示的消息
    										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
    										//打开对话框
    										box.open();
										}
            						}
            					}
            				});
						} else if (selectedTreeItem.getText().equals(ALL_PACKAGES)) {//如果右键点击的是所有应用（包）
							//待修改为搜索
							String deviceId = selectedTreeItem.getParentItem().getText();
	            			updateConfigUi(deviceId);//右键也要更新该设备的配置状态
	        				//开始白名单测试
	        				MenuItem searchPackagesMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
	        				searchPackagesMi.setText(SEARCH_PACKAGES);
	        				searchPackagesMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
	        				searchPackagesMi.addListener(SWT.Selection, new Listener() {
	        					public void handleEvent(Event e) {  	//向指定用户发送消息!
	        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
	        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
	        						if(selected != null)
	        						{ //添加，响应代码 
	        							//TODO:
	        							MyDialog finishDialog = new MyDialog(shlSvMonkey, "关键字:");
	        							if (finishDialog.open() == IDialogConstants.OK_ID) {
	        								String resultStr = finishDialog.getTextValue();
	        								if (!resultStr.equals("")) {
	        									MyUtils.printWithTimeMill("搜索的关键字:" + resultStr);
	        									String listPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.PACKAGE_LIST);//获取所有的包存的路径
												List<String> packagesList = PropUtil.readListFile(listPath);//获取所有的包名
												List<String> searchResultList = Collections.synchronizedList(new ArrayList<String>());
												for (String string : packagesList) {
													if (string.contains(resultStr)) {
														searchResultList.add(string);
													}
												}
												MyUtils.printWithTimeMill("搜索到包个数：" + searchResultList.size());
												if (packagesList.size() > 0 && searchResultList.size() > 0) {//白名单中有包才开始测试
													TreeItem[] allItems = selectedTreeItem.getItems();
													selectedTreeItem.removeAll();
													//所有应用
													for (int j = 0; j < searchResultList.size(); j++) {
														TreeItem everyPackage = new TreeItem(selectedTreeItem, 0);
														everyPackage.setText(searchResultList.get(j));
													}
													MyUtils.printWithTimeMill("包个数：" + allItems.length);
												} else {
													display.asyncExec (new Runnable () {
														public void run () {
															MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
															//设置对话框的标题
															box.setText("警告");
															//设置对话框显示的消息
															box.setMessage("没有该应用！");
															//打开对话框
															box.open();
														}
													});
												}
	        								}
	        							}
	        						}
	        					}
	        				});
						} else if (selectedTreeItem.getText().equals(WHITE_LIST)) {//如果右键点击的是白名单
							String deviceId = selectedTreeItem.getParentItem().getText();
	            			updateConfigUi(deviceId);//右键也要更新该设备的配置状态
	        				//开始白名单测试
	        				MenuItem startWhiteTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
	        				startWhiteTestMi.setText("开始白名单测试");
	        				startWhiteTestMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
	        				startWhiteTestMi.addListener(SWT.Selection, new Listener() {
	        					public void handleEvent(Event e) {  	//向指定用户发送消息!
	        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
	        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
	        						if(selected != null)
	        						{ //添加，响应代码 
	        							String monkeyCmdStr = getMonkeyWhiteTestCmd(deviceId);
	        							if (!RecordUtils.isRunning(deviceId)) {
	        								Thread whiltTestThread = new Thread(new Runnable() {
	    										@Override
	    										public void run() {
	    											try {
	    												try {
	    													String whitePath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.WHITE_LIST);
	    													List<String> whiteList = PropUtil.readListFile(whitePath);
	    													if (whiteList.size() > 0) {//白名单中有包才开始测试
	    														Process pPush = Runtime.getRuntime().exec("adb -s " + deviceId + " push " + whitePath + " /data/local/tmp");
	    														BufferedReader inputStream = new BufferedReader(new InputStreamReader(pPush.getInputStream(), "UTF-8"));
	    														BufferedReader errorReader = new BufferedReader(new InputStreamReader(pPush.getErrorStream(), "UTF-8"));
	    														String line;
	    														while((line = inputStream.readLine()) != null) {
	    															MyUtils.printWithTimeMill("push file:" + line);
	    														}
	    														while((line = errorReader.readLine()) != null) {
	    															MyUtils.printWithTimeMill("push file errorReader:" + line);
	    														}
	    														int pushResult = pPush.waitFor();
	    														if (pushResult == 0) {
	    															monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
	    														} else {
	    															display.asyncExec (new Runnable () {
	    																public void run () {
	    																	MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
	    																	//设置对话框的标题
	    																	box.setText("警告");
	    																	//设置对话框显示的消息
	    																	box.setMessage("白名单文件推送失败，请检查白名单文件是否存在！");
	    																	//打开对话框
	    																	box.open();
	    																}
	    															});
	    														}
															} else {
																display.asyncExec (new Runnable () {
																	public void run () {
																		MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																		//设置对话框的标题
																		box.setText("警告");
																		//设置对话框显示的消息
																		box.setMessage("白名单为空，请先添加应用到白名单！");
																		//打开对话框
																		box.open();
																	}
																});
															}
	    												} catch (InterruptedException e1) {
	    													e1.printStackTrace();
	    												}
	    											} catch (IOException e) {
	    												e.printStackTrace();
	    											}
	    										}
	    									});
	        								whiltTestThread.setDaemon(true);
	        								whiltTestThread.start();
	        							} else {
											MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
											//设置对话框的标题
											box.setText("警告");
											//设置对话框显示的消息
											box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
											//打开对话框
											box.open();
										}
	        						}
	        					}
	        				});
						} else if (selectedTreeItem.getText().equals(BLACK_LIST)) {//如果右键点击的是白名单
							String deviceId = selectedTreeItem.getParentItem().getText();
	            			updateConfigUi(deviceId);//右键也要更新该设备的配置状态
	        				//开始黑名单测试
	        				MenuItem startBlackTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
	        				startBlackTestMi.setText("开始黑名单测试");
	        				startBlackTestMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
	        				startBlackTestMi.addListener(SWT.Selection, new Listener() {
	        					public void handleEvent(Event e) {  	//向指定用户发送消息!
	        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
	        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
	        						if(selected != null)
	        						{ //添加，响应代码 
	        							String monkeyCmdStr = getMonkeyBlackTestCmd(deviceId);
	        							if (!RecordUtils.isRunning(deviceId)) {
	        								Thread blackTestThread = new Thread(new Runnable() {
	    										@Override
	    										public void run() {
													try {
														String blackPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.BLACK_LIST);
														List<String> blackList = PropUtil.readListFile(blackPath);
														if (blackList.size() > 0) {//白名单中有包才开始测试
															Process pPush = Runtime.getRuntime().exec("adb -s " + deviceId + " push " + blackPath + " /data/local/tmp");
    														BufferedReader inputStream = new BufferedReader(new InputStreamReader(pPush.getInputStream(), "UTF-8"));
    														BufferedReader errorReader = new BufferedReader(new InputStreamReader(pPush.getErrorStream(), "UTF-8"));
    														String line;
    														while((line = inputStream.readLine()) != null) {
    															MyUtils.printWithTimeMill("PushFile:" + line);
    														}
    														while((line = errorReader.readLine()) != null) {
    															MyUtils.printWithTimeMill("PushFile errorReader:" + line);
    														}
															int pushResult = pPush.waitFor();
															if (pushResult == 0) {
																monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
															} else {
																display.asyncExec (new Runnable () {
																	public void run () {
																		MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																		//设置对话框的标题
																		box.setText("警告");
																		//设置对话框显示的消息
																		box.setMessage("黑名单文件推送失败，请检查黑名单文件是否存在！");
																		//打开对话框
																		box.open();
																	}
																});
															}
														} else {
															display.asyncExec (new Runnable () {
																public void run () {
																	MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																	//设置对话框的标题
																	box.setText("警告");
																	//设置对话框显示的消息
																	box.setMessage("黑名单为空，请先添加应用到黑名单！");
																	//打开对话框
																	box.open();
																}
															});
														}
													} catch (IOException e) {
														e.printStackTrace();
													} catch (InterruptedException e1) {
														e1.printStackTrace();
		    										}
	    										}
	    									});
	        								blackTestThread.setDaemon(true);
	        								blackTestThread.start();
	        							} else {
											MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
											//设置对话框的标题
											box.setText("警告");
											//设置对话框显示的消息
											box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
											//打开对话框
											box.open();
										}
	        						}
	        					}
	        				});
						} else if (selectedTreeItem.getText().equals(THIRD_PACKAGES)) {//如果右键点击的是第三方应用
							String deviceId = selectedTreeItem.getParentItem().getText();
							updateConfigUi(deviceId);//右键也要更新该设备的配置状态
							//开始黑名单测试
	        				MenuItem installApkMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
	        				installApkMi.setText("安装第三方apk");
	        				installApkMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
	        				installApkMi.addListener(SWT.Selection, new Listener() {
	        					public void handleEvent(Event e) {  	//向指定用户发送消息!
	        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
	        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
	        						if(selected != null)
	        						{ //添加，响应代码 
	        							List<String> filesList = WinUtil.apkFilesSeleteDialog(shlSvMonkey,"");//选择应用
	        							List<String> failList = Collections.synchronizedList(new ArrayList<String>());//用来记录安装失败的应用
	        							if (filesList.size() > 0) {
	        								new Thread(new Runnable() {
	        									@Override
	        									public void run() {
	        										for (String string : filesList) {
	        											if (adbInstallApk(deviceId, string) == false) {
	        												failList.add(string);
	        											}
	        										}
	        										display.asyncExec (new Runnable () {
        				                        		public void run () {
        				                        			getDevices();
        				                        		}
        				                        	});
	        										if (failList.size() > 0) {
	        											display.asyncExec (new Runnable () {
	        				                        		public void run () {
	        				                        			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_ERROR);
	        													//设置对话框的标题
	        													box.setText("提示");
	        													//设置对话框显示的消息
	        													StringBuilder failBuilder = new StringBuilder();
	        													for (String string : failList) {
	        														failBuilder.append(string + "\r\n");
	        													}
	        													box.setMessage("安装失败请检查：\r\n " + failBuilder.toString());
	        													//打开对话框
	        													box.open();
	        				                        		}
	        				                        	});
	        										} else {
	        											display.asyncExec (new Runnable () {
	        				                        		public void run () {
	        				                        			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
	        													//设置对话框的标题
	        													box.setText("提示");
	        													//设置对话框显示的消息
	        													box.setMessage("安装成功！");
	        													//打开对话框
	        													box.open();
	        				                        		}
	        				                        	});
													}
	        									}
	        								}).start();
	        							}
	        						}
	        					}
	        				});
						} else if (selectedTreeItem.getText().equals(ORDER_LIST)) {//如果右键点击的是第三方应用
							String deviceId = selectedTreeItem.getParentItem().getText();
							updateConfigUi(deviceId);//右键也要更新该设备的配置状态
	        				//开始顺序名单测试
	        				MenuItem startOrderTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
	        				startOrderTestMi.setText("开始顺序名单测试");
	        				startOrderTestMi.setData(selectedTreeItem.getText());  	 //向响应菜单项事件的代码，传递值。
	        				startOrderTestMi.addListener(SWT.Selection, new Listener() {
	        					public void handleEvent(Event e) {  	//向指定用户发送消息!
	        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
	        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
	        						if(selected != null)
	        						{ //添加，响应代码
	        							if (!RecordUtils.isRunning(deviceId)) {//如果该设备是空闲状态
	        								new Thread(new Runnable() {
												@Override
												public void run() {
													String orderListPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.ORDER_LIST);
													List<String> orderList = PropUtil.readListFile(orderListPath);
													if (orderList.size() > 0) {//列表中有程序包才进行测试
														monkeyOrderRunning(deviceId, orderList, tableMonkeyRun);
													} else {
														display.asyncExec (new Runnable () {
															public void run () {
																MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																//设置对话框的标题
																box.setText("警告");
																//设置对话框显示的消息
																box.setMessage("顺序测试名单为空，请先添加应用到名单！");
																//打开对话框
																box.open();
															}
														});
													}
												}
											}).start();
	        							} else {
											MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
											//设置对话框的标题
											box.setText("警告");
											//设置对话框显示的消息
											box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
											//打开对话框
											box.open();
										}
	        						}
	        					}
	        				});
						}
            		} else { //如果是根节点则当前右键点击的是设备ID
            			String deviceId = selectedTreeItem.getText();
            			updateConfigUi(deviceId);//右键也要更新该设备的配置状态
            			//开始随机测试
        				MenuItem startRandomTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				startRandomTestMi.setText("开始随机测试");
        				startRandomTestMi.setData(selectedTreeItem.getText());    //向响应菜单项事件的代码，传递值。
        				startRandomTestMi.addListener(SWT.Selection, new Listener() {
        					public void handleEvent(Event e) {  	//向指定用户发送消息!
        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码 
        							String monkeyCmdStr = getMonkeyCmd(deviceId, "");
        							
        							if (!RecordUtils.isRunning(deviceId)) {
        								Thread monkeyThreadRunnable = new Thread(new Runnable() {
        									@Override
        									public void run() {
												monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
        									}
        								});
        								monkeyThreadRunnable.setDaemon(true);//设置为后台线程
        								monkeyThreadRunnable.start();
									} else {
										MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
										//设置对话框的标题
										box.setText("警告");
										//设置对话框显示的消息
										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
										//打开对话框
										box.open();
									}
        						}
        					}
        				});
        				
        				//开始白名单测试
        				MenuItem startWhiteTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				startWhiteTestMi.setText("开始白名单测试");
        				startWhiteTestMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				startWhiteTestMi.addListener(SWT.Selection, new Listener() {
        					public void handleEvent(Event e) {  	//向指定用户发送消息!
        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码 
        							String monkeyCmdStr = getMonkeyWhiteTestCmd(deviceId);
        							if (!RecordUtils.isRunning(deviceId)) {
        								Thread whiteTestThread = new Thread(new Runnable() {
    										@Override
    										public void run() {
    											try {
    												try {
    													String whitePath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.WHITE_LIST);
    													List<String> whiteList = PropUtil.readListFile(whitePath);
    													if (whiteList.size() > 0) {//白名单中有包才开始测试
    														Process pPush = Runtime.getRuntime().exec("adb -s " + deviceId + " push " + whitePath + " /data/local/tmp");
    														BufferedReader inputStream = new BufferedReader(new InputStreamReader(pPush.getInputStream(), "UTF-8"));
    														BufferedReader errorReader = new BufferedReader(new InputStreamReader(pPush.getErrorStream(), "UTF-8"));
    														String line;
    														while((line = inputStream.readLine()) != null) {
    															MyUtils.printWithTimeMill("PushFile:" + line);
    														}
    														while((line = errorReader.readLine()) != null) {
    															MyUtils.printWithTimeMill("PushFile errorReader:" + line);
    														}
    														int pushResult = pPush.waitFor();
    														if (pushResult == 0) {
    															monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
    														} else {
    															display.asyncExec (new Runnable () {
    																public void run () {
    																	MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    																	//设置对话框的标题
    																	box.setText("警告");
    																	//设置对话框显示的消息
    																	box.setMessage("白名单文件推送失败，请检查白名单文件是否存在！");
    																	//打开对话框
    																	box.open();
    																}
    															});
    														}
														} else {
															display.asyncExec (new Runnable () {
																public void run () {
																	MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																	//设置对话框的标题
																	box.setText("警告");
																	//设置对话框显示的消息
																	box.setMessage("白名单为空，请先添加应用到白名单！");
																	//打开对话框
																	box.open();
																}
															});
														}
    												} catch (InterruptedException e1) {
    													e1.printStackTrace();
    												}
    											} catch (IOException e) {
    												e.printStackTrace();
    											}
    										}
    									});
        								whiteTestThread.setDaemon(true);
        								whiteTestThread.start();
        							} else {
										MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
										//设置对话框的标题
										box.setText("警告");
										//设置对话框显示的消息
										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
										//打开对话框
										box.open();
									}
        						}
        					}
        				});
        				
        				//开始黑名单测试
        				MenuItem startBlackTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				startBlackTestMi.setText("开始黑名单测试");
        				startBlackTestMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				startBlackTestMi.addListener(SWT.Selection, new Listener() {
        					public void handleEvent(Event e) {  	//向指定用户发送消息!
        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码 
        							String monkeyCmdStr = getMonkeyBlackTestCmd(deviceId);
        							if (!RecordUtils.isRunning(deviceId)) {
        								Thread blackTestThread = new Thread(new Runnable() {
    										@Override
    										public void run() {
												try {
													String blackPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.BLACK_LIST);
													List<String> blackList = PropUtil.readListFile(blackPath);
													if (blackList.size() > 0) {//白名单中有包才开始测试
														Process pPush = Runtime.getRuntime().exec("adb -s " + deviceId + " push " + blackPath + " /data/local/tmp");
														BufferedReader inputStream = new BufferedReader(new InputStreamReader(pPush.getInputStream(), "UTF-8"));
														BufferedReader errorReader = new BufferedReader(new InputStreamReader(pPush.getErrorStream(), "UTF-8"));
														String line;
														while((line = inputStream.readLine()) != null) {
															MyUtils.printWithTimeMill("PushFile:" + line);
														}
														while((line = errorReader.readLine()) != null) {
															MyUtils.printWithTimeMill("PushFile errorReader:" + line);
														}
														int pushResult = pPush.waitFor();
														if (pushResult == 0) {
															monkeyRunning(deviceId, monkeyCmdStr, tableMonkeyRun);
														} else {
															display.asyncExec (new Runnable () {
																public void run () {
																	MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																	//设置对话框的标题
																	box.setText("警告");
																	//设置对话框显示的消息
																	box.setMessage("黑名单文件推送失败，请检查黑名单文件是否存在！");
																	//打开对话框
																	box.open();
																}
															});
														}
													} else {
														display.asyncExec (new Runnable () {
															public void run () {
																MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
																//设置对话框的标题
																box.setText("警告");
																//设置对话框显示的消息
																box.setMessage("黑名单为空，请先添加应用到黑名单！");
																//打开对话框
																box.open();
															}
														});
													}
												} catch (IOException e) {
													e.printStackTrace();
												} catch (InterruptedException e1) {
													e1.printStackTrace();
	    										}
    										}
    									});
        								blackTestThread.setDaemon(true);
        								blackTestThread.start();
        							} else {
										MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
										//设置对话框的标题
										box.setText("警告");
										//设置对话框显示的消息
										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
										//打开对话框
										box.open();
									}
        						}
        					}
        				});
        				
        				//开始顺序名单测试
        				MenuItem startOrderTestMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				startOrderTestMi.setText("开始顺序名单测试");
        				startOrderTestMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				startOrderTestMi.addListener(SWT.Selection, new Listener() {
        					public void handleEvent(Event e) {  	//向指定用户发送消息!
        						MenuItem mi = (MenuItem)e.widget;   //取菜单项Widget
        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码
        							if (!RecordUtils.isRunning(deviceId)) {//如果该设备是空闲状态
        								new Thread(new Runnable() {
											@Override
											public void run() {
												String orderListPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.ORDER_LIST);
												List<String> orderList = PropUtil.readListFile(orderListPath);
												if (orderList.size() > 0) {//列表中有程序包才进行测试
													monkeyOrderRunning(deviceId, orderList, tableMonkeyRun);
												} else {
													display.asyncExec (new Runnable () {
														public void run () {
															MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
															//设置对话框的标题
															box.setText("警告");
															//设置对话框显示的消息
															box.setMessage("顺序测试名单为空，请先添加应用到名单！");
															//打开对话框
															box.open();
														}
													});
												}
											}
										}).start();
        							} else {
										MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
										//设置对话框的标题
										box.setText("警告");
										//设置对话框显示的消息
										box.setMessage("该设备正在进行测试，请等待测试完毕后再进行！");
										//打开对话框
										box.open();
									}
        						}
        					}
        				});
        				
        				//导出Monkey配置信息
        				MenuItem exportMonkeyConfigMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				exportMonkeyConfigMi.setText("导出Monkey参数配置");
        				exportMonkeyConfigMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				exportMonkeyConfigMi.addListener(SWT.Selection, new Listener() {
							@Override
							public void handleEvent(Event event) {
								MenuItem mi = (MenuItem)event.widget;   //取菜单项Widget
        						String selected = (String)mi.getData();//取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码
        							String selectDirStr = WinUtil.savePropFileDialog(shlSvMonkey, USERDIR);
        							PropUtil.saveConfig(deviceId, selectDirStr);
        						}
							}
						});
        				
        				MenuItem importMonkeyConfigMi = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				importMonkeyConfigMi.setText("导入Monkey参数配置");
        				importMonkeyConfigMi.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				importMonkeyConfigMi.addListener(SWT.Selection, new Listener() {
							@Override
							public void handleEvent(Event event) {
								MenuItem mi = (MenuItem)event.widget;   //取菜单项Widget
        						String selected = (String)mi.getData(); //取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码
        							String filePath = WinUtil.configFileSeleteDialog(shlSvMonkey, USERDIR);
        							if (!filePath.equals("")) {
        								PropUtil.importConfig(deviceId, filePath);
        								updateConfigUi(deviceId);
									}
        						}
							}
						});
        				
        				MenuItem killMonkeyThread = new MenuItem(menu,SWT.PUSH); //为菜单，建菜单项
        				killMonkeyThread.setText("停止该设备monkey进程");
        				killMonkeyThread.setData(selectedTreeItem.getText());  	//向响应菜单项事件的代码，传递值。
        				killMonkeyThread.addListener(SWT.Selection, new Listener() {
        					@Override
        					public void handleEvent(Event event) {
        						MenuItem mi = (MenuItem)event.widget;   //取菜单项Widget
        						String selected = (String)mi.getData(); //取，在建立菜单项时，传过来的对象。
        						if(selected != null)
        						{ //添加，响应代码
        							TableItem[] items = tableMonkeyRun.getItems();//当前table总共有多少行
        							String indexStr = "-1";
        							String deviceInfoPath = "";
        							for (TableItem tableItem : items) {
        								if (tableItem.getText(1).equals(deviceId)) {//id
											if (tableItem.getText(2).equals(RecordUtils.TESTING_STATUS)) {//status
												indexStr = tableItem.getText(RecordUtils.NUMBER_TYPE);    //number index
												deviceInfoPath = tableItem.getText(RecordUtils.LOG_PATH_TYPE);
											}
										}
									}
        							final String indexStrFinal = indexStr;
        							DeviceInfo deviceInfo = getDeviceInfoFromFile(deviceInfoPath);
        							String startTestTimeStr = deviceInfo.getStartTestTime();
        							Calendar c = Calendar.getInstance();
        							long startTimeL = -1;
        							long endTimeL = -1;
        							endTimeL = System.currentTimeMillis();
        			                try {
										c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTestTimeStr));
										startTimeL = c.getTimeInMillis();
										MyUtils.printWithTimeMill("时间转化后的毫秒数为：" + startTimeL);
									} catch (ParseException e) {
										MyUtils.printWithTimeMill("停止monkey进程：时间解析错误，" + e.getMessage());
									}
        			                final String costTimeStr = MyUtils.millisToTimeSecondCn(endTimeL - startTimeL);
        			                appendDeviceInfo(deviceInfoPath + "\\" + DEVICE_INFO_FILE_NAME, "TestCostTime=" + (endTimeL - startTimeL));//测试耗时
        							if (!indexStr.equals("-1")) {
        								Thread killMonkeyInDeviceThread = new Thread(new Runnable() {
        									@Override
        									public void run() {
        										killMonkey(deviceId, indexStrFinal, costTimeStr);
        									}
        								});
        								killMonkeyInDeviceThread.setDaemon(true);
        								killMonkeyInDeviceThread.start();
									}
        						}
        					}
        				});
            		}
            		treePackageList.setMenu(menu);  //设置弹出菜单
            	} else if (selectedTreeItem != null && e.button == 1) {  //左键单击
            		if (selectedTreeItem.getParentItem() == null) {//在设备ID上单击鼠标左键
            			String idStr = selectedTreeItem.getText();
            			currentDeviceId = idStr;
            			lblSelectedDevice.setText(currentDeviceId);
            			updateConfigUi(currentDeviceId);
            			new Thread(new Runnable() {//在设备ID上单击鼠标则获取设备信息
							@Override
							public void run() {
		            			DeviceInfo deviceInfo = getDeviceInfo(currentDeviceId);
		            			display.asyncExec (new Runnable () {
	                        		public void run () {
	                        			lblInfoNameValue.setText(deviceInfo.getModel());
	                        			lblInternalNameValue.setText(deviceInfo.getDisplayId());
	                        			lblAndroidVersionValue.setText(deviceInfo.getVersionRelease());
	                        			lblBuildDateValue.setText(deviceInfo.getBuildDate());
	                        		}
	                        	});
							}
						}).start();
					} else if (selectedTreeItem.getParentItem().getParentItem() == null) {//在所有包、白名单、黑名单上鼠标左键单击
						updateConfigUi(selectedTreeItem.getParentItem().getText());
					} else if (selectedTreeItem.getParentItem().getParentItem().getParentItem() == null) {//在包列表中上鼠标左键单击
						updateConfigUi(selectedTreeItem.getParentItem().getParentItem().getText());
					}
            		//响应代码段，略
            	} else if (e.button == 3) {
					
				}
            }  
        }); 
		
		tableMonkeyRun = new Table(shlSvMonkey, SWT.BORDER | SWT.FULL_SELECTION);
		fd_grpMonkeySettings.bottom = new FormAttachment(tableMonkeyRun, -6);
		tableMonkeyRun.setHeaderVisible(true);
		tableMonkeyRun.setLinesVisible(true);
		fd_grpMonkeySettings.right = new FormAttachment(100, -10);
		FormData fd_tableMonkeyRun = new FormData();
		fd_tableMonkeyRun.top = new FormAttachment(0, 179);
		fd_tableMonkeyRun.left = new FormAttachment(label, 6);
		fd_tableMonkeyRun.right = new FormAttachment(100, -10);
		
		tableColumnNum = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnNum.setWidth(NUM_WIDTH);
		tableColumnNum.setText("序号");
		
		tableColumnID = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnID.setWidth(ID_WIDTH);
		tableColumnID.setText("设备序列号");
		
		tableColumnTestStatus = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnTestStatus.setWidth(STATUS_WIDTH);
		tableColumnTestStatus.setText("测试状态");
		tableColumnTestStatus.setToolTipText("结束:未检测到Monkey finished（比如设备断电，或出现anr和crash）\r\n"
				+ "进行中:测试进行中\r\n"
				+ "中断:正在测试时软件被关闭后重新打开\r\n"
				+ "执行出错:执行monkey命令时出现执行错误\r\n"
				+ "通过:检测到Monkey finished且无anr和crash发生");
		
		tableColumnCrashCounter = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnCrashCounter.setWidth(CRASH_WIDTH);
		tableColumnCrashCounter.setText("CRASH数量");
		
		tableColumnAnrCounter = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnAnrCounter.setWidth(ANR_WIDTH);
		tableColumnAnrCounter.setText("ANR数量");
		
		tableColumnRunningTime = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnRunningTime.setWidth(TIME_WIDTH);
		tableColumnRunningTime.setText("已运行");
		
//		int maxWidth = tableMonkeyRun.getBounds().width;
//		int leftWidth = maxWidth - USED_WIDTH;
//		int logPathWidth = (int)(leftWidth/3);
//		int cmdWidth = leftWidth - logPathWidth;
		
		tableColumnLogPath = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnLogPath.setWidth(105);
		tableColumnLogPath.setText("日志路径");
		tableColumnLogPath.setToolTipText("双击可进入日志所在目录");
		
		tableColumnMonkeyCmd = new TableColumn(tableMonkeyRun, SWT.CENTER);
		tableColumnMonkeyCmd.setWidth(212);
		tableColumnMonkeyCmd.setText("指令详情");
		tableColumnMonkeyCmd.setToolTipText("双击可复制命令到剪贴板");

		//把记录更新到table
		currentRecordList = RecordUtils.readObjectForList(recordFilePath);
		int recordCounter = currentRecordList.size();
		if (recordCounter > 0) {
			RecordUtils.updateRecordFile(currentRecordList);
			RecordUtils.updateTableUi(tableMonkeyRun);
		}
		
		tableMonkeyRun.addMouseListener(new MouseAdapter() {
			
			public void mouseDoubleClick(MouseEvent e)
			{
				//获取鼠标点击的位置
				Point point = new Point(e.x, e.y);
			
				//得到鼠标点击的行（item）
				TableItem item = tableMonkeyRun.getItem(point);
				if (item == null) {
					return;
				}
				
				for (int j = 0; j < RecordUtils.TYPE_COUNTER; j++) {
					Rectangle rect = item.getBounds(j);
					if (rect.contains(point)) {
						if (j == RecordUtils.LOG_PATH_TYPE) {			//如果双击的是log路径的列
							WinUtil.openDir(item.getText(j));
						} else if (j == RecordUtils.MONKEY_CMD_TYPE) {  //如果双击的是monkey命令列
							String monkeyCmdStr = item.getText(j);
							//以下是把文本复制到剪贴板
							final Clipboard clipboard = new Clipboard(display);
							Object[] data = new Object[]{monkeyCmdStr};  
			                Transfer[] types = new Transfer[]{TextTransfer.getInstance()}; 
							clipboard.setContents(data, types);
						}
					}//
				}
			}
			
			public void mouseDown(MouseEvent e) {  						//老鼠按下
            	TableItem selectedTableItem = tableMonkeyRun.getItem(new Point(e.x, e.y));//取节点控件
            	if (selectedTableItem != null && e.button == SWT.MouseDown) {//鼠标右键
            		Menu menuTable = new Menu(tableMonkeyRun);  		//为节点建POPUP菜单
            		tableMonkeyRun.setMenu(menuTable);
            		MenuItem deleteRecordDataMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
            		deleteRecordDataMenu.setText("删除该条记录和日志文件");
            		deleteRecordDataMenu.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
            		deleteRecordDataMenu.addListener(SWT.Selection, new Listener() {
            			public void handleEvent(Event e) {  			//向指定用户发送消息!
            				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            				TableItem selected = (TableItem)mi.getData();//取，在建立菜单项时，传过来的对象。
            				if(selected != null && !selected.isDisposed())
            				{ //添加，响应代码
            					TableItem[] items = tableMonkeyRun.getItems();//获取所有行
            					boolean hasRunning = false;
            					for (TableItem tableItem : items) {    //遍历记录，是否有正在进行的测试，有则不能进行删除操作
									String statusStr = tableItem.getText(RecordUtils.TEST_STATUS_TYPE);
									if (statusStr.equals(RecordUtils.TESTING_STATUS)) {
										hasRunning = true;
										break;
									}
								}
            					if (!hasRunning) {                    //没有正在进行的测试，可以进行删除操作
            						int deleteRecorNum = Integer.parseInt(selected.getText(RecordUtils.NUMBER_TYPE));
            						String logPath = selected.getText(RecordUtils.LOG_PATH_TYPE);
        							File logFile = new File(logPath);
        							WinUtil.deleteDir(logFile, shlSvMonkey);
        							if (!logFile.exists()) {
        								RecordUtils.removeRecordFormFile(deleteRecorNum);
        								RecordUtils.updateTableUi(tableMonkeyRun);
        								AnrCrashUtils.updateAnrCrashTableUi(tableLogList, null);
        								textTrace.setText("");
        							} else {
        								MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
        								//设置对话框的标题
        								box.setText("提示");
        								//设置对话框显示的消息
        								box.setMessage(logPath + "删除失败！\r\n请重试或手动删除！");
        								//打开对话框
        								box.open();
        							}
								} else {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
        							//设置对话框的标题
        							box.setText("提示");
        							//设置对话框显示的消息
        							box.setMessage("还有测试任务正在进行，不能进行删除操作！");
        							//打开对话框
        							box.open();
								}
            				}
            			}
            		});
            		MenuItem deleteAllRecordDataMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
            		deleteAllRecordDataMenu.setText("删除所有记录和日志文件");
            		deleteAllRecordDataMenu.setData(selectedTableItem.getText());  	//向响应菜单项事件的代码，传递值。
            		deleteAllRecordDataMenu.addListener(SWT.Selection, new Listener() {
    					public void handleEvent(Event e) {  			//向指定用户发送消息!
    						MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
    						String selected = (String)mi.getData();		//取，在建立菜单项时，传过来的对象。
    						if(selected != null)
    						{ //添加，响应代码
    							TableItem[] items = tableMonkeyRun.getItems();//获取所有行
    							boolean hasRunning = false;
            					for (TableItem tableItem : items) {//遍历记录，是否有正在进行的测试，有则不能进行删除操作
									String statusStr = tableItem.getText(RecordUtils.TEST_STATUS_TYPE);
									if (statusStr.equals(RecordUtils.TESTING_STATUS)) {
										hasRunning = true;
										break;
									}
								}
            					if (!hasRunning) {//没有正在进行的测试，可以进行删除操作
            						int logCounter = 0;
            						for (TableItem tableItem : items) {
            							String logPath = tableItem.getText(RecordUtils.LOG_PATH_TYPE);
            							File logFile = new File(logPath);
            							WinUtil.deleteDir(logFile, shlSvMonkey);
            							if (logFile.exists()) {
            								logCounter ++;
            							}
            						}
            						if (logCounter > 0) { //说明有删除失败的目录
            							MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
            							//设置对话框的标题
            							box.setText("提示");
            							//设置对话框显示的消息
            							box.setMessage(logCounter + "个记录删除失败！\r\n请重试或手动删除！");
            							//打开对话框
            							box.open();
            						}
            						RecordUtils.removeAllRecordFormFile();
            						RecordUtils.updateTableUi(tableMonkeyRun);
            						AnrCrashUtils.updateAnrCrashTableUi(tableLogList, null);
            						textTrace.setText("");
								} else {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
        							//设置对话框的标题
        							box.setText("提示");
        							//设置对话框显示的消息
        							box.setMessage("还有测试任务正在进行，不能进行删除操作！");
        							//打开对话框
        							box.open();
								}
    						}
    					}
    				});
               		MenuItem exportOneRecordMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
               		exportOneRecordMenu.setText("导出该条记录到excel");
               		exportOneRecordMenu.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
               		exportOneRecordMenu.addListener(SWT.Selection, new Listener() {
            			public void handleEvent(Event e) {  			//向指定用户发送消息!
            				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            				TableItem selected = (TableItem)mi.getData();		//取，在建立菜单项时，传过来的对象。
            				if(selected != null && !selected.isDisposed())
            				{ //添加，响应代码
            					String deviceId = selected.getText(RecordUtils.ID_TYPE);
            					String logPath = selected.getText(RecordUtils.LOG_PATH_TYPE);
            					String testStatusStr = selected.getText(RecordUtils.TEST_STATUS_TYPE);
            					if (!testStatusStr.equals(RecordUtils.TESTING_STATUS)) {//不是正在测试的才能导出
            						exportMonkeyResult(deviceId, logPath);
            					} else {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
									//设置对话框的标题
									box.setText("提示");
									//设置对话框显示的消息
									box.setMessage("不能导出正在进行测试的记录！");
									//打开对话框
									box.open();
								}
            				}
            			}
            		});
               		
               		MenuItem exportDeviceAllRecordMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
               		exportDeviceAllRecordMenu.setText("导出该设备所有记录到excel");
               		exportDeviceAllRecordMenu.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
               		exportDeviceAllRecordMenu.addListener(SWT.Selection, new Listener() {
               			public void handleEvent(Event e) {  			//向指定用户发送消息!
               				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
               				TableItem selected = (TableItem)mi.getData();		//取，在建立菜单项时，传过来的对象。
               				if(selected != null && !selected.isDisposed())
               				{ //添加，响应代码
               					TableItem[] items = tableMonkeyRun.getItems();//获取所有行
               					
               					List<DeviceRecord> deviceRecords = Collections.synchronizedList(new ArrayList<DeviceRecord>());
               					//当前选择的设备的ID
               					String deviceId = selected.getText(RecordUtils.ID_TYPE);
               					for (TableItem tableItem : items) {//遍历所有行
               						String iDStr = tableItem.getText(RecordUtils.ID_TYPE);//获取遍历到的行的ID
               						if (iDStr.equals(deviceId)) {  //如果是待导出设备的ID
               							String statusStr = tableItem.getText(RecordUtils.TEST_STATUS_TYPE);//是否正在测试
               							if (!statusStr.equals(RecordUtils.TESTING_STATUS)) {//该记录不是正在测试的记录
               								String logPath = tableItem.getText(RecordUtils.LOG_PATH_TYPE);
               								deviceRecords.add(getDeviceRecord(deviceId, logPath));
               							} 
									}
								}
               					if (deviceRecords.size() > 0) {
               						exportAllMonkeyResult(deviceRecords);
								} else {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
									//设置对话框的标题
									box.setText("警告");
									//设置对话框显示的消息
									box.setMessage("该设备没有测试结束的记录！");
									//打开对话框
									box.open();
								}
               				}
               			}
               		});
               		MenuItem exportAllDevicesRecordMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
               		exportAllDevicesRecordMenu.setText("导出所有记录到excel");
               		exportAllDevicesRecordMenu.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
               		exportAllDevicesRecordMenu.addListener(SWT.Selection, new Listener() {
               			public void handleEvent(Event e) {  			//向指定用户发送消息!
               				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
               				TableItem selected = (TableItem)mi.getData();		//取，在建立菜单项时，传过来的对象。
               				if(selected != null && !selected.isDisposed())
               				{ //添加，响应代码
               					TableItem[] items = tableMonkeyRun.getItems();//获取所有行
               					
               					List<DeviceRecord> deviceRecords = Collections.synchronizedList(new ArrayList<DeviceRecord>());
               					//当前选择的设备的ID
               					for (TableItem tableItem : items) {//遍历所有行
               						String iDStr = tableItem.getText(RecordUtils.ID_TYPE);			   //获取遍历到的行的ID
           							String statusStr = tableItem.getText(RecordUtils.TEST_STATUS_TYPE);//是否正在测试
           							if (!statusStr.equals(RecordUtils.TESTING_STATUS)) {			   //该记录不是正在测试的记录
           								String logPath = tableItem.getText(RecordUtils.LOG_PATH_TYPE); //获取log路径
           								deviceRecords.add(getDeviceRecord(iDStr, logPath));            //遍历到的每条记录保存到list
           							} 
               					}
               					if (deviceRecords.size() > 0) {//有记录，则导出
               						exportAllMonkeyResult(deviceRecords);
               					} else {					   //无记录，弹出提示
               						MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_INFORMATION);
               						//设置对话框的标题
               						box.setText("提示");
               						//设置对话框显示的消息
               						box.setMessage("当前没有可以导出的记录！");
               						//打开对话框
               						box.open();
               					}
               				}
               			}
               		});
               		
               		MenuItem analyseMonkeyLogMenu = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
               		analyseMonkeyLogMenu.setText("解析Monkey日志");
               		analyseMonkeyLogMenu.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
               		analyseMonkeyLogMenu.addListener(SWT.Selection, new Listener() {
            			public void handleEvent(Event e) {  			//向指定用户发送消息!
            				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
            				TableItem selected = (TableItem)mi.getData();		//取，在建立菜单项时，传过来的对象。
            				if(selected != null && !selected.isDisposed())
            				{ //添加，响应代码
            					String monkeyTxtPath = WinUtil.fileSeleteDialog(shlSvMonkey, "");
            					if (monkeyTxtPath != null && !monkeyTxtPath.equals("")) {
            						new Thread(new Runnable() {
            							@Override
            							public void run() {
            								try {
            									monkeyLogAnalyze(monkeyTxtPath, tableMonkeyRun);
            								} catch (InterruptedException e) {
            									e.printStackTrace();
            								}
            							}
            						}).start();
								}
            				}
            			}
            		});
               		
               		MenuItem killMonkeyMi = new MenuItem(menuTable,SWT.PUSH); //为菜单，建菜单项
               		killMonkeyMi.setText("停止该设备的monkey进程");
               		killMonkeyMi.setData(selectedTableItem);  	//向响应菜单项事件的代码，传递值。
               		killMonkeyMi.addListener(SWT.Selection, new Listener() {
               			public void handleEvent(Event e) {  			//向指定用户发送消息!
               				MenuItem mi = (MenuItem)e.widget;   		//取菜单项Widget
               				TableItem selected = (TableItem)mi.getData();		//取，在建立菜单项时，传过来的对象。
               				if(selected != null && !selected.isDisposed())
               				{ //添加，响应代码
               					String indexStr = selected.getText(RecordUtils.NUMBER_TYPE);
            					String deviceId = selected.getText(RecordUtils.ID_TYPE);
            					String statusRun = selected.getText(RecordUtils.TEST_STATUS_TYPE);
            					String deviceInfoPath = selected.getText(RecordUtils.LOG_PATH_TYPE);
    							DeviceInfo deviceInfo = getDeviceInfoFromFile(deviceInfoPath);
    							String startTestTimeStr = deviceInfo.getStartTestTime();
    							Calendar c = Calendar.getInstance();
    							long startTimeL = -1;
    							long endTimeL = -1;
    							endTimeL = System.currentTimeMillis();
    			                try {
									c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTestTimeStr));
									startTimeL = c.getTimeInMillis();
								} catch (ParseException e1) {
									MyUtils.printWithTimeMill("停止monkey进程：时间解析错误，" + e1.getMessage());
								}
    			                final String costTimeStr = MyUtils.millisToTimeSecondCn(endTimeL - startTimeL);
    			                appendDeviceInfo(deviceInfoPath + "\\" + DEVICE_INFO_FILE_NAME, "TestCostTime=" + (endTimeL -startTimeL));//测试耗时
               					if (deviceId != null && !deviceId.equals("") && statusRun.equals(RecordUtils.TESTING_STATUS)) {
               						Thread killMonkeyInListThread = new Thread(new Runnable() {
               							@Override
               							public void run() {
           									killMonkey(deviceId, indexStr, costTimeStr);
               							}
               						});
               						killMonkeyInListThread.setDaemon(true);
               						killMonkeyInListThread.start();
               					} else {
       		            			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
       								//设置对话框的标题
       								box.setText("已停止");
       								//设置对话框显示的消息
       								box.setMessage("该测试已结束");
       								//打开对话框
       								box.open();
								}
               				}
               			}
               		});
            	} else if (selectedTableItem != null && e.button == 1) {//鼠标左键点击到某条记录，显示该记录信息到Text
            		if (! selectedTableItem.isDisposed()) {
            			String pathStr = selectedTableItem.getText(RecordUtils.LOG_PATH_TYPE);
            			AnrCrashUtils.updateAnrCrashTableUi(tableLogList, pathStr);//更新crash和anr列表
            			
            			String numStr = selectedTableItem.getText(RecordUtils.NUMBER_TYPE);
            			String idStr = selectedTableItem.getText(RecordUtils.ID_TYPE);
            			String logPath = selectedTableItem.getText(RecordUtils.LOG_PATH_TYPE);
            			String cmdDetail = selectedTableItem.getText(RecordUtils.MONKEY_CMD_TYPE);
            			DeviceInfo deviceInfo = getDeviceInfoFromFile(logPath);
            			String costTimeStr = "未知";
            			try {
            				costTimeStr = MyUtils.millisToTimeSecondCn(Long.parseLong(deviceInfo.getTestCostTime()));
						} catch (NumberFormatException e2) {
							MyUtils.printWithTimeMill("时间格式错误：" + e.toString());
						}
            			
            			String deviceInfoStr = "设备识别: " + idStr + "\r\n设备名称: " + deviceInfo.getModel() + "\r\n系统版本: " + deviceInfo.getDisplayId()
            					+ "\r\n安卓版本: "  + deviceInfo.getVersionRelease() + "\r\n编译日期: " + deviceInfo.getBuildDate() 
            					+ "\r\n实际Seed: " + deviceInfo.getSeed() + "\r\n测试耗时: " + costTimeStr + "\r\n";
            			String resultStr = deviceInfoStr + "日志路径: \r\n    " + logPath + "\r\n命令详情: \r\n    " + cmdDetail + "\r\n";
            			//读取错误信息
            			String messageStr = RecordUtils.readOneRecordInFile(Integer.parseInt(numStr), RecordUtils.ERROR_MESSAGE);
            			if (messageStr != null && !messageStr.equals("")) {
            				resultStr += "错误信息:\r\n" + messageStr;
            			}
            			textTrace.setText(resultStr);
					}
				} else if (selectedTableItem == null) {
					AnrCrashUtils.updateAnrCrashTableUi(tableLogList, null);
					textTrace.setText("");
				}
            }
		});
		
		textAnyeventPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textAnyeventPercent.setBounds(734, 21, 38, 18);
		textAnyeventPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String anyEventStr = textAnyeventPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.ANY_EVENT_PERCENT, anyEventStr);
		    }    
		});
		
		textPinchzoonPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textPinchzoonPercent.setBounds(734, 45, 38, 18);
		textPinchzoonPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String pinchZoonStr = textPinchzoonPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.PINCH_ZOON_PERCENT, pinchZoonStr);
		    }    
		});
		
		textNavPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textNavPercent.setBounds(734, 69, 38, 18);
		textNavPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String navStr = textNavPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.NAV_PERCENT, navStr);
		    }    
		});
		
		textMajornavPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textMajornavPercent.setBounds(734, 92, 38, 18);
		textMajornavPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String majorNavStr = textMajornavPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.MAJOR_NAV_PERCENT, majorNavStr);
		    }    
		});
		
		textMotionPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textMotionPercent.setBounds(558, 46, 38, 18);
		textMotionPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String motionStr = textMotionPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.MOTION_PERCENT, motionStr);
		    }    
		});
		
		Label lblEventCount = new Label(grpMonkeySettings, SWT.NONE);
		lblEventCount.setText("Event Count");
		lblEventCount.setBounds(247, 93, 97, 17);
		lblEventCount.setToolTipText("随机事件数量");
		
		textEventCount = new Text(grpMonkeySettings, SWT.BORDER);
		textEventCount.setBounds(350, 92, 73, 18);
		textEventCount.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String eventCountStr = textEventCount.getText();
		    	PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.EVENT_COUNT, eventCountStr);
		    	
				String millisecStr = textThrottleMillisec.getText();
				if (!eventCountStr.trim().equals("") && !eventCountStr.trim().equals("0")) {
					if (!millisecStr.trim().equals("") && !millisecStr.trim().equals("0")) {
						try {
							lblCostTimeShow.setText(MyUtils.millisToTimeMinuteCn(Long.parseLong(millisecStr.trim()) * Long.parseLong(eventCountStr.trim())));
						} catch (NumberFormatException e2) {
						}
					}
				} else {
					MessageBox box = new MessageBox(shlSvMonkey, SWT.YES | SWT.ICON_WARNING);
					//设置对话框的标题
					box.setText("警告");
					//设置对话框显示的消息
					box.setMessage("Event Count不能为空或0！");
					//打开对话框
					box.open();
				}
		    }    
		});
		
		tableMonkeyRun.setLayoutData(fd_tableMonkeyRun);
		tableMonkeyRun.setHeaderVisible(true);
		tableMonkeyRun.setLinesVisible(true);
		
		tableLogList = new Table(shlSvMonkey, SWT.BORDER | SWT.FULL_SELECTION);
		fd_tableMonkeyRun.bottom = new FormAttachment(100, -314);
		FormData fd_tableLogList = new FormData();
		fd_tableLogList.left = new FormAttachment(label, 6);
		fd_tableLogList.top = new FormAttachment(tableMonkeyRun, 6);
		fd_tableLogList.bottom = new FormAttachment(100, -10);
		tableLogList.setLayoutData(fd_tableLogList);
		tableLogList.setHeaderVisible(true);
		tableLogList.setLinesVisible(true);
		
		TableColumn tableColumnNull = new TableColumn(tableLogList, SWT.CENTER);
		tableColumnNull.setWidth(0);
		tableColumnNull.setText("");
		
		TableColumn tableColumnCrashNum = new TableColumn(tableLogList, SWT.CENTER);
		tableColumnCrashNum.setWidth(36);
		tableColumnCrashNum.setText("序号");
		
		TableColumn tableColumnCrashList = new TableColumn(tableLogList, SWT.CENTER);
		tableColumnCrashList.setWidth(230);
		tableColumnCrashList.setText("CRASH事件");
		
		TableColumn tableColumnAnrNum = new TableColumn(tableLogList, SWT.CENTER);
		tableColumnAnrNum.setWidth(36);
		tableColumnAnrNum.setText("序号");
		
		TableColumn tableColumnAnrList = new TableColumn(tableLogList, SWT.CENTER);
		tableColumnAnrList.setWidth(231);
		tableColumnAnrList.setText("ANR事件");
		
		tableLogList.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {  						  //老鼠按下
				Point point = new Point(e.x, e.y);
            	TableItem selectedTableItem = tableLogList.getItem(point);//取节点控件
            	if (selectedTableItem != null && e.button == 1) {//鼠标左键
            		String filePath = selectedTableItem.getText(AnrCrashUtils.DIR_UI);//AnrCrashUtils.DIR_UI第0列默认插入了log日志路径
            		String contenDetailStr = "";
        			for (int j = 0; j < AnrCrashUtils.COLUMN_COUNTER; j++) {
	    				Rectangle rect = selectedTableItem.getBounds(j);
	    				if (rect.contains(point)) {
	    					if (j == AnrCrashUtils.CRASH_NAME_UI || j == AnrCrashUtils.CRASH_NUM_UI) {//如果点击的是crash相关的列
	    						String crashNumStr = selectedTableItem.getText(AnrCrashUtils.CRASH_NUM_UI);
	    						if (crashNumStr != null && !crashNumStr.equals("")) {
	    							int index = Integer.parseInt(crashNumStr);
	    							contenDetailStr = AnrCrashUtils.getAnrCrashDetail(index, AnrCrashUtils.CRASH_TYPE, filePath);
								}
	    					} else if (j == AnrCrashUtils.ANR_NAME_UI || j == AnrCrashUtils.ANR_NUM_UI) {//如果点击的是anr相关的列
	    						String anrNumStr = selectedTableItem.getText(AnrCrashUtils.ANR_NUM_UI);
	    						if (anrNumStr != null && !anrNumStr.equals("")) {
	    							int index = Integer.parseInt(selectedTableItem.getText(AnrCrashUtils.ANR_NUM_UI));
	    							contenDetailStr = AnrCrashUtils.getAnrCrashDetail(index, AnrCrashUtils.ANR_TYPE, filePath);
								}
							}
	    					textTrace.setText(contenDetailStr);
	    				}
        			}
            	}
			}
		});
		
		textTrace = new Text(shlSvMonkey, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		fd_tableLogList.right = new FormAttachment(textTrace, -6);
		FormData fd_textTrace = new FormData();
		fd_textTrace.top = new FormAttachment(tableMonkeyRun, 6);
		fd_textTrace.bottom = new FormAttachment(100, -10);
		fd_textTrace.right = new FormAttachment(100, -10);
		fd_textTrace.left = new FormAttachment(0, 866);
		
		Label lblCostTime = new Label(grpMonkeySettings, SWT.NONE);
		lblCostTime.setToolTipText("根据Throttle Millisec和Event count计算得出");
		lblCostTime.setText("估计用时:");
		lblCostTime.setBounds(249, 116, 57, 17);
		
		lblCostTimeShow = new Label(grpMonkeySettings, SWT.NONE);
		lblCostTimeShow.setToolTipText("根据Throttle Millisec和Event count计算得出");
		lblCostTimeShow.setText("0天0时0分");
		lblCostTimeShow.setBounds(316, 116, 107, 17);
		
		btnPermissionTargetSystem = new Button(grpMonkeySettings, SWT.CHECK);
		btnPermissionTargetSystem.setToolTipText("运行时权限目标系统");
		btnPermissionTargetSystem.setText("--permission-target-system");
		btnPermissionTargetSystem.setBounds(10, 139, 223, 17);
		btnPermissionTargetSystem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//事件处理
				if (btnPermissionTargetSystem.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_PERMISSION_TARGET_SYSTEM, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_PERMISSION_TARGET_SYSTEM, "false");
				}
			}
		});
		
		lblPermissionPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblPermissionPercent.setToolTipText("Android6.0以上的运行时权限系统,权限申请事件的百分比");
		lblPermissionPercent.setText("Permission Percent");
		lblPermissionPercent.setBounds(444, 139, 112, 17);
		
		textPermissonPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textPermissonPercent.setBounds(558, 139, 38, 18);
		textPermissonPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String sysKeyStr = textPermissonPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.PERMISSION_PERCENT, sysKeyStr);
		    }    
		});
		btnbugreport = new Button(grpMonkeySettings, SWT.CHECK);
		btnbugreport.setToolTipText("运行时权限目标系统");
		btnbugreport.setText("--bugreport");
		btnbugreport.setBounds(246, 139, 108, 17);
		btnbugreport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//事件处理
				if (btnbugreport.getSelection() == true) {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_BUGREPORT, "true");
				} else {
					PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.IS_BUGREPORT, "false");
				}
			}
		});
		
		lblRotaionPercent = new Label(grpMonkeySettings, SWT.NONE);
		lblRotaionPercent.setToolTipText("轨迹事件的百分比(轨迹事件由一个或几个随机的移动组成，有时还伴随有点击)");
		lblRotaionPercent.setText("Rotaion Percent");
		lblRotaionPercent.setBounds(619, 139, 108, 17);
		
		textRotaionPercent = new Text(grpMonkeySettings, SWT.BORDER);
		textRotaionPercent.setBounds(734, 139, 38, 18);
		textRotaionPercent.addListener(SWT.FocusOut, new Listener() {    
		    public void handleEvent(Event e) {    
		    	// 处理失去焦点的事件 
		    	String rotationStr = textRotaionPercent.getText();
				PropUtil.setMonkeyParameter(currentDeviceId, PropUtil.ROTATION_PERCENT, rotationStr);
		    }    
		});

		
		textTrace.setLayoutData(fd_textTrace);
		textTrace.setEditable(false);
		textTrace.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.stateMask == SWT.CTRL && evt.keyCode == KeyCodePc.KEY_A) {//CTRL + A全选
					textTrace.selectAll();
				}
			}
		});
		
		getDevices();
		shlSvMonkey.open();
		while (!shlSvMonkey.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * 初始化配置，包括窗口位置配置
	 */
	private void initProp() {
		String rootDir = System.getProperty("user.dir");
		File propDir = new File(rootDir);// + "\\power.properties"
		if (propDir.exists()) {//目录存在
			File propFile = new File(rootDir + "\\config.properties");
			if (!propFile.exists()) {//文件不存在
				try {
					if (propFile.createNewFile()) {
						propFilePath = propFile.getAbsolutePath();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				propFilePath = propFile.getAbsolutePath();
			}
		}
		if (propFilePath != "") {
			saveShellWidth = PropUtil.getValueOfProp("saveShellWidth", propFilePath);
			if (saveShellWidth == null || saveShellWidth.equals("") ) {
				PropUtil.setProperties(propFilePath, "saveShellWidth", "0", true);
				saveShellWidth = "1132";
			}
			saveShellHeight = PropUtil.getValueOfProp("saveShellHeight", propFilePath);
			if (saveShellHeight == null || saveShellHeight.equals("") ) {
				PropUtil.setProperties(propFilePath, "saveShellHeight", "0", true);
				saveShellHeight = "722";
			}
		}
	}
	
    /**
     * 显示错误对话框，已包含同步线程
     * @param msg
     * @param t
     */
    private void showError(final String msg, final Throwable t) {
        shlSvMonkey.getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                Status s = new Status(IStatus.ERROR, "提示", msg, t);
                ErrorDialog.openError(
                		shlSvMonkey, "错误", "操作失败", s);
            }
        });
    }
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSvMonkey = new Shell();
		shlSvMonkey.setImage(SWTResourceManager.getImage(SvMonkey.class, "/images/monkey.ico"));
		initProp();
		shlSvMonkey.setSize(1132, 722);
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage("testLine");
		//TODO:
//		shlSvMonkey.setSize(Integer.parseInt(saveShellWidth), Integer.parseInt(saveShellHeight));
		shlSvMonkey.setText("SVmonkey(By Tonsen)");
		shlSvMonkey.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
//				MyUtils.printWithTimeMill("窗口位置移动了");
			}
			public void controlResized(ControlEvent e) {
				Rectangle shlRect = shlSvMonkey.getBounds();
				PropUtil.setProperties(propFilePath, "saveShellWidth", shlRect.width + "", true);
				PropUtil.setProperties(propFilePath, "saveShellHeight", shlRect.height + "", true);
				if (tableMonkeyRun != null && !tableMonkeyRun.isDisposed()) {
					int maxWidth = tableMonkeyRun.getBounds().width;
					int leftWidth = maxWidth - USED_WIDTH;
					int logPathWidth = (int)(leftWidth/2);
					int cmdWidth = leftWidth - logPathWidth;
					if (tableColumnLogPath != null && !tableColumnLogPath.isDisposed()) {
						tableColumnLogPath.setWidth(logPathWidth);
					}
					if (tableColumnMonkeyCmd != null && !tableColumnMonkeyCmd.isDisposed()) {
						tableColumnMonkeyCmd.setWidth(cmdWidth);
					}
					tableMonkeyRun.getParent().layout();
				}
			}
		});
		
		//菜单栏
		Menu menuBar = new Menu(shlSvMonkey, SWT.BAR);
		shlSvMonkey.setMenuBar(menuBar);
		
		//工具菜单
		MenuItem toolsItem = new MenuItem(menuBar, SWT.CASCADE);
		toolsItem.setText("文件");
		
		Menu filemenu = new Menu(shlSvMonkey, SWT.DROP_DOWN);
		//style必须是SWT.DROP_DOWN
		toolsItem.setMenu(filemenu);
		
		MenuItem actionItem = new MenuItem(filemenu, SWT.PUSH);
		actionItem.setText("首选项");
		actionItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MyUtils.createPropFile(settingsPropPath);
				new Settings(settingsPropPath);
			}

		});
		
		//设置菜单
		MenuItem settingItem = new MenuItem(menuBar, SWT.CASCADE);
		settingItem.setText("设置");
		Menu settingMenu=new Menu(shlSvMonkey,SWT.DROP_DOWN); 
		settingItem.setMenu(settingMenu);
		//"新建"菜单,如果有这一项会在Monkey参数设置后面有展开箭头
//		Menu newFileMenu=new Menu(shlMonkeytest,SWT.DROP_DOWN);
//		newFileItem.setMenu(newFileMenu); 
		//"邮箱设置"项
		MenuItem emailSettingItem=new MenuItem(settingMenu,SWT.CASCADE);
		emailSettingItem.setText("邮箱配置");
		//"Log保存路径"项
		MenuItem logPathItem=new MenuItem(settingMenu,SWT.CASCADE);
		logPathItem.setText("Log保存路径");
		logPathItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
//				ShowDirectoryDialog(shell);
				MyDialog finishDialog = new MyDialog(shlSvMonkey);
//				finishDialog.setTextValue(System.getProperty("user.dir"));
				finishDialog.setTextValue("暂不支持");
				if (finishDialog.open() == IDialogConstants.OK_ID) {
					String resultFolder = finishDialog.getTextValue();
					if (!resultFolder.equals("")) {
						USERDIR = resultFolder;
						MyUtils.printWithTimeMill("设置log路径为：" + USERDIR);
					}
				}
			}

		});
		
		
		//快捷键菜单
		MenuItem hotKeyItem = new MenuItem(menuBar, SWT.CASCADE);
		hotKeyItem.setText("快捷键");
		hotKeyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//事件处理
				MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES);
				//设置对话框的标题
				box.setText("快捷键");
				//设置对话框显示的消息
				String messageStr = "识别设备  ：Ctrl + F \r\nROOT设备 : Ctrl + R \r\n重启ADB：Ctrl + D";
				box.setMessage(messageStr);
				//打开对话框
				box.open();
			}
		});
		
		//关于菜单
		MenuItem aboutItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutItem.setText("关于");
		aboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES);
			    //设置对话框的标题
			    box.setText("关于");
			    //设置对话框显示的消息
			    box.setMessage("版本：V3.50 \r\n日期：2020-01-03\r\n有问题或建议请联系 : Tonsen \r\n微信:TonsenWei \r\n QQ:470029260");
			    //打开对话框
			    box.open();
			}

		});
		
		
	}
	
	/**
	 * 杀死设备的monkey线程，通过adb shell ps|findstr monkey获取线程ID
	 * @param deviceId 设备ID
	 */
	public void killMonkey(String deviceId, String indexStr, String costTimeStr) {
		stopMonkeyByHand = true;
		//get SDK version to confirm what kill monkey cmd to use
		String getAndroidSdkCmdStr = "adb -s " + deviceId + " shell getprop ro.build.version.sdk";
		String killCmdStr = "adb -s " + deviceId + " shell ps -e | grep monkey";//-e参数为列出所有进程
		String pidMonkeyStr = "";
		String sdkVersionStr = "";
		try {
			//get device SDK Version
			Process p = Runtime.getRuntime().exec(getAndroidSdkCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
			String line;
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					sdkVersionStr = line;
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get SDK Version) errorReader:" + line);
			}
			int result = p.waitFor();
			MyUtils.printWithTimeMill(deviceId + " getAndroidSdk result = " + result);
			
			try {
				int sdkVersionInt = Integer.parseInt(sdkVersionStr);
				if (sdkVersionInt >= 26) {
					killCmdStr = "adb -s " + deviceId + " shell ps -e | grep monkey";//-e参数为列出所有进程
				} else {
					killCmdStr = "adb -s " + deviceId + " shell ps | grep monkey";//-e参数为列出所有进程
				}
			} catch (NumberFormatException e) {
				MyUtils.printWithTimeMill("killMonkey(get SDK Version):" + e.getMessage());
			}
			
			p = Runtime.getRuntime().exec(killCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			//这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果
			errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {   //一个或多个空格
					String[] strs = line.split("\\s+");
					if (strs.length > 2) {
						pidMonkeyStr = strs[1];
					}
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("errorReader:" + line);
			}
			result = p.waitFor();
			MyUtils.printWithTimeMill(deviceId + " getMonkeyThreadPidInDevice result = " + result);
			errorReader.close();
			inputStream.close();
			MyUtils.printWithTimeMill("killMonkeyResult=" + result + ", pidMonkeyStr=" + pidMonkeyStr);
			if (result != 0 || pidMonkeyStr.equals("")) {
				int index = -1;
				try {
					index = Integer.parseInt(indexStr);
					if (index > 0) {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
						RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, costTimeStr);
						RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, "\r\nMonkey被手动停止!");
					}
				} catch (NumberFormatException e) {
					MyUtils.printWithTimeMill("kill monkey exception:" + e.getMessage());
				}
				display.asyncExec (new Runnable () {
            		public void run () {
            			RecordUtils.updateTableUi(tableMonkeyRun);
            			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
						//设置对话框的标题
						box.setText("警告");
						//设置对话框显示的消息
						box.setMessage("找不到这只猴子，请检查");
						//打开对话框
						box.open();
            		}
            	});
			} else {
				String killPidCmd = "adb -s " + deviceId + " shell kill " + pidMonkeyStr;
				p = Runtime.getRuntime().exec(killPidCmd);
				inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
				errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
				while((line = inputStream.readLine()) != null) {
					if (!line.matches("\\s*")) {   //一个或多个空格
						MyUtils.printWithTimeMill("inputStream:" + line);
					}
				}
				while((line = errorReader.readLine()) != null) {
					MyUtils.printWithTimeMill("errorReader:" + line);
				}
				result = p.waitFor();
				MyUtils.printWithTimeMill(deviceId + " kill device monkey thread result = " + result);
				errorReader.close();
				inputStream.close();
				if (result != 0) {
					int index = -1;
					try {
						index = Integer.parseInt(indexStr);
						if (index > 0) {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, costTimeStr);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, "\r\nMonkey被手动停止!");
							
						}
					} catch (NumberFormatException e) {
						MyUtils.printWithTimeMill("kill monkey exception:" + e.getMessage());
					}
					display.asyncExec (new Runnable () {
						public void run () {
							RecordUtils.updateTableUi(tableMonkeyRun);
							MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
							//设置对话框的标题
							box.setText("警告");
							//设置对话框显示的消息
							box.setMessage("杀死monkey进程失败，请手动杀掉");
							//打开对话框
							box.open();
						}
					});
				} else {
					int index = -1;
					try {
						index = Integer.parseInt(indexStr);
						if (index > 0) {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, costTimeStr);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, "\r\nMonkey被手动停止!");
						}
					} catch (NumberFormatException e) {
						MyUtils.printWithTimeMill("kill monkey exception:" + e.getMessage());
					}
					display.asyncExec (new Runnable () {
						public void run () {
							RecordUtils.updateTableUi(tableMonkeyRun);
							MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
							//设置对话框的标题
							box.setText("提示");
							//设置对话框显示的消息
							box.setMessage("已杀掉此猴子，请检查！");
							//打开对话框
							box.open();
						}
					});
				}
			}
			int waitTimeCounter = 10;
			while ((waitTimeCounter--) > 0) {
				//检查是否为空，为空说明monkey已自动结束，退出循环结束killmonkey动作
				if (monkeyThreadMap.get(deviceId) == null) {
					waitTimeCounter = 0;
				} else {//不为空说明monkey可能还没结束，等待1秒后再检查，超过10（10秒）次仍未结束说明出现异常，则强制结束
					Thread.sleep(1000);//等待5秒后判断monkey是否自动结束
				}
			}
			/*
			 * 杀掉猴子后monkey会自动结束测试，
			 * 如果没有结束，把该设备的monkey测试标志位设为false，monkey会结束测试
			 * */
			if (monkeyThreadMap.get(deviceId) != null) {
				MonkeyTaskBean taskBean = monkeyThreadMap.get(deviceId);
				long pid = taskBean.getThreadPid();
				long timerPid = taskBean.getTimerThreadPid();
				long logcatPid = taskBean.getTimerThreadPid();
				MyUtils.printWithTimeMill("killMonkey pid = " + pid);
				//把map里面该pid是否继续测试的标志为设为false：停止测试
				monkeyThreadMap.put(deviceId, new MonkeyTaskBean(pid, false));
				Thread currentMonkeyThread = findThread(pid);
				Thread getTimeThread = findThread(timerPid);
				Thread getLogcatThread = findThread(logcatPid);
				if (getLogcatThread != null) {
					getLogcatThread.interrupt();
					MyUtils.printWithTimeMill("interrupt logcatThread in kill monkey");
				}
				if (getTimeThread != null) {
					getTimeThread.interrupt();
					MyUtils.printWithTimeMill("interrupt timeThread in kill monkey");
				}
				if (taskBean.getBufferdWriter() != null) {
					taskBean.getBufferdWriter().close();
				}
				if (taskBean.getFileWriter() != null) {
					taskBean.getFileWriter().close();
				}
				if (currentMonkeyThread != null) {
					MyUtils.printWithTimeMill(deviceId + " interrupt monkey thread ...");
					currentMonkeyThread.interrupt();//中断线程
				} else {
					MyUtils.printWithTimeMill(deviceId + " (kill monkey Thread) currentMonkeyThread = null!");
				}
			}
		} catch (IOException e) {
			MyUtils.printWithTimeMill("killMonkey() IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			MyUtils.printWithTimeMill("killMonkey() InterruptedException: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 重启adb服务
	 */
	public void killADB() {
		programRunning = false;//需要先停止monkey线程
		
		String killCmdStr = "adb kill-server";
		try {
			Process p = Runtime.getRuntime().exec(killCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			//这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果
			BufferedReader  errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
			String line;
			while((line = inputStream.readLine()) != null) {
				MyUtils.printWithTimeMill("errorReader:" + line);
				if (!line.matches("\\s*")) {//一个或多个空格
					StringBuilder builder = new StringBuilder();
					builder.append(line);
					display.asyncExec(new Runnable() {
						public void run() {
							textTrace.append(builder.toString() + "\r\n");
						}
					});
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("errorReader:" + line);
				if (!line.matches("\\s*")) {//一个或多个空格
					StringBuilder builder = new StringBuilder();
					builder.append(line);
					display.asyncExec(new Runnable(){
						public void run() {
							textTrace.append(builder.toString() + "\r\n");
						}
					});
				}
			}
			int result = p.waitFor();
			errorReader.close();
			inputStream.close();
			if (result != 0) {
				display.asyncExec (new Runnable () {
					public void run () {
						MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
						//设置对话框的标题
						box.setText("警告");
						//设置对话框显示的消息
						box.setMessage("停止adb服务失败，请检查");
						//打开对话框
						box.open();
					}
				});
			} else {
				String startAdbCmd = "adb start-server";
				p = Runtime.getRuntime().exec(startAdbCmd);
				inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
				errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
				while((line = inputStream.readLine()) != null) {
					if (!line.matches("\\s*")) {   //一个或多个空格
						MyUtils.printWithTimeMill("errorReader:" + line);
						if (!line.matches("\\s*")) {//一个或多个空格
							StringBuilder builder = new StringBuilder();
							builder.append(line);
							display.asyncExec(new Runnable(){
								public void run() {
									textTrace.append(builder.toString() + "\r\n");
								}
							});
						}
					}
				}
				while((line = errorReader.readLine()) != null) {
					MyUtils.printWithTimeMill("errorReader:" + line);
					if (!line.matches("\\s*")) {//一个或多个空格
						StringBuilder builder = new StringBuilder();
						builder.append(line);
						display.asyncExec(new Runnable(){
							public void run() {
								textTrace.append(builder.toString() + "\r\n");
							}
						});
					}
				}
				result = p.waitFor();
				errorReader.close();
				inputStream.close();
				if (result != 0) {
					display.asyncExec (new Runnable () {
						public void run () {
							MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
							//设置对话框的标题
							box.setText("警告");
							//设置对话框显示的消息
							box.setMessage("启动ADB服务失败，请手动启动");
							//打开对话框
							box.open();
						}
					});
				}
			}
			programRunning = true;
		} catch (IOException e) {
			programRunning = true;
			e.printStackTrace();
		} catch (InterruptedException e) {
			programRunning = true;
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取设备（adb devices）
	 */
	public void getDevices() {
		isAdbBusy = true;
		btnDetectDevice.setEnabled(false);
       new Thread(new Runnable() {
            public void run() {
                try {
                	devicesList = Collections.synchronizedList(new ArrayList<Device>());//设备列表
                	String adbCmdStr = "adb devices";
                	Process p = Runtime.getRuntime().exec(adbCmdStr);
                	BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
        			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
                	String line;
                	devicesIdList = Collections.synchronizedList(new ArrayList<String>());
                    while((line = inputStream.readLine()) != null) {
                    	if (line!= null && !line.equals("")) {
                    		if (line.trim().equals("List of devices attached")) {
                    			while((line = inputStream.readLine()) != null) {
                    				if (line!= null && !line.equals("")) {
                    					String[] tt = line.split("\\s+");//多个空格分隔
                    					devicesIdList.add(tt[0]);
                    					Device device = new Device();
                    					device.setDeviceId(tt[0]);
                    					devicesList.add(device);
                    				}
                    			}
                    		}
						}
                   }
    				while((line = errorReader.readLine()) != null) {
    					MyUtils.printWithTimeMill("AdbDevices errorReader:" + line);
    				}
                    if ((line = inputStream.readLine()) == null) {
                    	if (devicesIdList.size() > 0) {
                    		display.asyncExec (new Runnable () {
                        		public void run () {
                        			for(int i=0; i < devicesIdList.size(); i++){
                        				String deviceId = devicesIdList.get(i);
										PropUtil.createDeviceProp(deviceId);//用id创建目录并创建配置文件
                        			}
                        			String currentDeviceIdStr = devicesIdList.get(0);
                        			if (currentDeviceIdStr != null && !currentDeviceIdStr.equals("")) {
                        				lblSelectedDevice.setText(currentDeviceIdStr);//设置默认选择第一项
                        				currentDeviceId = lblSelectedDevice.getText();
                        				updateConfigUi(currentDeviceId);
                        				DeviceInfo deviceInfo = getDeviceInfo(currentDeviceId);
                        				lblInfoNameValue.setText(deviceInfo.getModel());
                        				lblInternalNameValue.setText(deviceInfo.getDisplayId());
                        				lblAndroidVersionValue.setText(deviceInfo.getVersionRelease());
                        				lblBuildDateValue.setText(deviceInfo.getBuildDate());
									}
                        		}
                        	});
						} else {
							display.asyncExec (new Runnable () {
                        		public void run () {
                        			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
    								//设置对话框的标题
    								box.setText("警告");
    								//设置对话框显示的消息
    								box.setMessage("找不到任何android设备,请先连接设备后打开adb功能,然后点击获取设备");
    								//打开对话框
    								box.open();
									lblSelectedDevice.setText("");
									lblInfoNameValue.setText("");
                    				lblInternalNameValue.setText("");
                    				lblAndroidVersionValue.setText("");
                    				lblBuildDateValue.setText("");
                        		}
                        	});
						}
                    }
                    
                    try {
                    	int waitResult = p.waitFor();
                    	MyUtils.printWithTimeMill("getDevices[adb devices] waitResult = " + waitResult);
                    } catch (InterruptedException e) {
                    	MyUtils.printWithTimeMill("adb devices");
                    	isAdbBusy = false;
                    	showError("获取设备失败", e);
                    }
                    
                    for (int devicesInt = 0; devicesInt < devicesList.size(); devicesInt++) {//获取各个设备包
                    	final Device getDevice = devicesList.get(devicesInt);
                    	String deviceIdStr = getDevice.getDeviceId();
						//获取系统包
						p = Runtime.getRuntime().exec("adb -s " + deviceIdStr + " shell pm list packages -s");
						inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
						errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
                    	List<String> systemPackagesList = Collections.synchronizedList(new ArrayList<String>());
						while((line = inputStream.readLine()) != null) {
							if (! line.equals("")) {
								String[] tt=line.split(":");
								systemPackagesList.add(tt[1]);
							}
						}
						while((line = errorReader.readLine()) != null) {
							MyUtils.printWithTimeMill("ListSystemPackages errorReader:" + line);
						}
						try {
							int waitResult = p.waitFor();
							MyUtils.printWithTimeMill("getDevices[Get System Packages] waitResult = " + waitResult);
							getDevice.setSystemPackageList(systemPackagesList);
							PropUtil.createPackageList(deviceIdStr, systemPackagesList);
						} catch (InterruptedException e) {
							MyUtils.printWithTimeMill("adb devices");
							showError("获取系统应用列表失败", e);
						}
						//获取第三方包
						p = Runtime.getRuntime().exec("adb -s " + deviceIdStr + " shell pm list packages -3");
						inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
						errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
						List<String> thirdPackagesList = Collections.synchronizedList(new ArrayList<String>());
						while((line = inputStream.readLine()) != null) {
							if (! line.equals("")) {
								String[] tt=line.split(":");
								thirdPackagesList.add(tt[1]);
							}
						}
						while((line = errorReader.readLine()) != null) {
							MyUtils.printWithTimeMill("ListThirdPackages errorReader:" + line);
						}
						try {
							int waitResult = p.waitFor();
							MyUtils.printWithTimeMill("getDevices[Get Third Packages]waitResult = " + waitResult);
							getDevice.setThirdPackageList(thirdPackagesList);
							PropUtil.createPackageList(deviceIdStr, thirdPackagesList);
						} catch (InterruptedException e) {
							showError("获取第三方应用列表失败", e);
						}
						
						//所有包
						List<String> packagesList = systemPackagesList;
						packagesList.addAll(thirdPackagesList);//第三方和系统包合并后就是所有包
						
						getDevice.setPackageList(packagesList);
						PropUtil.createPackageList(deviceIdStr, packagesList);
						PropUtil.createWhiteList(deviceIdStr);
						PropUtil.createBlackList(deviceIdStr);
					}
                    
                	display.asyncExec (new Runnable () {
                		public void run () {
                			treePackageList.removeAll();
                			for (int i = 0; i < devicesList.size(); i++) {
								Device deviceTree = devicesList.get(i);
								TreeItem treeItemDeviceId = new TreeItem(treePackageList, 0);
								treeItemDeviceId.setText(deviceTree.getDeviceId());
								//所有应用
								TreeItem treeItemAllPackages = new TreeItem(treeItemDeviceId, 0);
								treeItemAllPackages.setText(ALL_PACKAGES);
								//系统应用
								TreeItem treeItemSystemPackages = new TreeItem(treeItemDeviceId, 0);
								treeItemSystemPackages.setText(SYSTEM_PACKAGES);
								//第三方应用
								TreeItem treeItemThirdPackages = new TreeItem(treeItemDeviceId, 0);
								treeItemThirdPackages.setText(THIRD_PACKAGES);
								//白名单
								TreeItem whitePackagesTreeItem = new TreeItem(treeItemDeviceId, 0);
								whitePackagesTreeItem.setText(WHITE_LIST);
								//黑名单
								TreeItem blackPackagesTreeItem = new TreeItem(treeItemDeviceId, 0);
								blackPackagesTreeItem.setText(BLACK_LIST);
								//顺序测试名单
								TreeItem orderPackagesTreeItem = new TreeItem(treeItemDeviceId, 0);
								orderPackagesTreeItem.setText(ORDER_LIST);
								//所有应用
								for (int j = 0; j < deviceTree.getPackageList().size(); j++) {
									TreeItem everyPackage = new TreeItem(treeItemAllPackages, 0);
									everyPackage.setText(deviceTree.getPackageList().get(j));
								}
								//系统应用
								for (int j = 0; j < deviceTree.getSystemPackageList().size(); j++) {
									TreeItem everyPackage = new TreeItem(treeItemSystemPackages, 0);
									everyPackage.setText(deviceTree.getSystemPackageList().get(j));
								}
								//第三方应用
								for (int j = 0; j < deviceTree.getThirdPackageList().size(); j++) {
									TreeItem everyPackage = new TreeItem(treeItemThirdPackages, 0);
									everyPackage.setText(deviceTree.getThirdPackageList().get(j));
								}
								//白名单
								String pathStr = PropUtil.getDevicePackageListPath(devicesList.get(i).getDeviceId(), PropUtil.WHITE_LIST);
								List<String> whiteList = PropUtil.readListFile(pathStr);
								for (int j = 0; j < whiteList.size(); j++) {
									TreeItem everyWhitePackage = new TreeItem(whitePackagesTreeItem, 0);
									everyWhitePackage.setText(whiteList.get(j));
								}
								//黑名单
								pathStr = PropUtil.getDevicePackageListPath(devicesList.get(i).getDeviceId(), PropUtil.BLACK_LIST);
								List<String> blackList = PropUtil.readListFile(pathStr);
								for (int j = 0; j < blackList.size(); j++) {
									TreeItem everyBlackPackage = new TreeItem(blackPackagesTreeItem, 0);
									everyBlackPackage.setText(blackList.get(j));
								}
								//顺序测试名单
								pathStr = PropUtil.getDevicePackageListPath(devicesList.get(i).getDeviceId(), PropUtil.ORDER_LIST);
								List<String> orderList = PropUtil.readListFile(pathStr);
								for (int j = 0; j < orderList.size(); j++) {
									TreeItem everyOrderPackage = new TreeItem(orderPackagesTreeItem, 0);
									everyOrderPackage.setText(orderList.get(j));
								}
							}
                			btnDetectDevice.setEnabled(true);
                	    }
                   });
                    inputStream.close();
                	isAdbBusy = false;
                } catch (IOException e) {
                	isAdbBusy = false;
                	display.asyncExec (new Runnable () {
                		public void run () {
                			btnDetectDevice.setEnabled(true);
                		}
                	});
                	MyUtils.printWithTimeMill("adb devices执行失败");
                	showError("adb执行失败！", e);
                }
            }
       }).start();
	}
	
	
	
	/**
	 * 根据设备ID从配置信息加载配置
	 * @param deviceId
	 */
	public void updateConfigUi(String deviceId) {
		long millTimeL = 0;
		long eventCountL = 0;
		currentDeviceId = deviceId;
		String propFilePath = PropUtil.getDevicePropFilePath(deviceId);
		String isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_CRASHES, propFilePath);
		Boolean isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnIgnoreCrashes.setSelection(isIgnore);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_TIMEOUTS, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnIgnoreTimeouts.setSelection(isIgnore);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_SECURITY_EXCEPTIONS, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnIgnoreSecurityExceptions.setSelection(isIgnore);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_MONITOR_NATIVE_CRASH, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnMonitorNativeCrash.setSelection(isIgnore);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_IGNORE_NATEVE_CRASHES, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnIgnoreNativeCrashes.setSelection(isIgnore);
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_PERMISSION_TARGET_SYSTEM, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnPermissionTargetSystem.setSelection(isIgnore);
		
		
		isIgnoreStr = PropUtil.getValueOfProp(PropUtil.IS_BUGREPORT, propFilePath);
		isIgnore = Boolean.parseBoolean(isIgnoreStr);
		btnbugreport.setSelection(isIgnore);
		
		String valuePropStr = PropUtil.getValueOfProp(PropUtil.THROTTLE_MILLISEC, propFilePath);
		try {
			millTimeL = Long.parseLong(valuePropStr);
		} catch (NumberFormatException e) {
		}
		textThrottleMillisec.setText(valuePropStr);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.LOG_LEVEL, propFilePath);
		textLogLevel.setText(valuePropStr);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.SEED, propFilePath);
		textSeedValue.setText(valuePropStr);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.EVENT_COUNT, propFilePath);
		try {
			eventCountL = Long.parseLong(valuePropStr);
		} catch (NumberFormatException e) {
		}
		textEventCount.setText(valuePropStr);
		
		valuePropStr = PropUtil.getValueOfProp(PropUtil.PERMISSION_PERCENT, propFilePath);
		textPermissonPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.TRACK_BALL_PERCENT, propFilePath);
		textTrackballPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.APP_SWITCH_PERCENT, propFilePath);
		textAppswitchPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.FLIP_PERCENT, propFilePath);
		textFlipPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.TOUCH_PERCENT, propFilePath);
		if (valuePropStr == null || valuePropStr.equals("")) {
			valuePropStr = "50";
			PropUtil.setProperties(propFilePath, PropUtil.TOUCH_PERCENT, "50", true);
		}
		textTouchPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.SYS_KEY_PERCENT, propFilePath);
		textSyskeyPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.ANY_EVENT_PERCENT, propFilePath);
		textAnyeventPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.PINCH_ZOON_PERCENT, propFilePath);
		textPinchzoonPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.NAV_PERCENT, propFilePath);
		textNavPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MAJOR_NAV_PERCENT, propFilePath);
		textMajornavPercent.setText(valuePropStr);
		valuePropStr = PropUtil.getValueOfProp(PropUtil.MOTION_PERCENT, propFilePath);
		if (valuePropStr == null || valuePropStr.equals("")) {
			valuePropStr = "50";
			PropUtil.setProperties(propFilePath, PropUtil.MOTION_PERCENT, "50", true);
		}
		textMotionPercent.setText(valuePropStr);
		
		lblCostTimeShow.setText(MyUtils.millisToTimeMinuteCn(eventCountL * millTimeL));
		
	}
	

	/**
	 * 从已存在的文件中读取设备信息
	 * @param monkeyWorkDir
	 * @return
	 */
	public DeviceInfo getDeviceInfoFromFile(String monkeyWorkDir) {
		DeviceInfo deviceInfo = new DeviceInfo();
		String filePath = monkeyWorkDir + "\\deviceInfo.txt";
		File infoFile = new File(filePath);
		if (infoFile.exists()) {
			try {
//				FileReader read = new FileReader(filePath);
//				BufferedReader br = new BufferedReader(read);  
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
				String line;
				String info;
				while ((line = br.readLine()) != null) {  
					info = line.trim();
					if (info.contains("ro.product.model=")) {
						String[] strS = info.split("=");
						deviceInfo.setModel(strS[1]);
					} else if (info.contains("ro.build.display.id=")) {
						String[] strS = info.split("=");
						deviceInfo.setDisplayId(strS[1]);
					} else if (info.contains("ro.build.version.release=")) {
						String[] strS = info.split("=");
						deviceInfo.setVersionRelease(strS[1]);
					} else if (info.contains("ro.build.date=")) {
						String[] strS = info.split("=");
						deviceInfo.setBuildDate(strS[1]);
					} else if (info.contains("ro.build.version.sdk=")) {
						String[] strS = info.split("=");
						deviceInfo.setSdkVersion(strS[1]);
					} else if (info.contains("testStartTime=")) {
						String[] strS = info.split("=");
						deviceInfo.setStartTestTime(strS[1]);
					} else if (info.contains("MonkeySeed=")) {
						String[] strS = info.split("=");
						deviceInfo.setSeed(strS[1]);
					} else if (info.contains("monkeyCmdStr=")) {
						String[] strS = info.split("=");
						deviceInfo.setTestCmd(strS[1]);
					} else if (info.contains("logcatPath=")) {
						String[] strS = info.split("=");
						deviceInfo.setLogPath(strS[1]);//logcatPath=
					} else if (info.contains("TestCostTime=")) {
						String[] strS = info.split("=");
						deviceInfo.setTestCostTime(strS[1]);//测试耗时
					}
				} 
				br.close();
			} catch (FileNotFoundException e) {
				showError("读取设备信息失败！", e);
			} catch (IOException e) {
				showError("读取设备信息失败！", e);
			}  
		}
		return deviceInfo;
	}
	
	/**
	 * 获取设备信息并写入文件
	 * @param deviceId 设备ID
	 * @param monkeyWorkDir monkeylog路径
	 * @param timeStr 测试开始时间
	 */
	public DeviceInfo getDeviceInfo(String deviceId, String monkeyWorkDir, String timeStr) {
		
		String getModelCmdStr = "adb -s " + deviceId + " shell getprop ro.product.model";
		String getDisplayIdCmdStr = "adb -s " + deviceId + " shell getprop ro.build.display.id ";
		String getVersionReleaseCmdStr = "adb -s " + deviceId + " shell getprop ro.build.version.release";
		String getBuildDateCmdStr = "adb -s " + deviceId + " shell getprop ro.build.date";
		String getAndroidSdkCmdStr = "adb -s " + deviceId + " shell getprop ro.build.version.sdk";

		
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDeviceId(deviceId);
		try {
			Process pGetInfo = Runtime.getRuntime().exec(getModelCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			String line;
			String infoPathStr = monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME;
			File infoFile = new File(infoPathStr);
			if (infoFile.exists()) {
				infoFile.delete();
				infoFile.createNewFile();
			} else {
				infoFile.createNewFile();
			}
			FileWriter fw = new FileWriter(infoPathStr, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append("testStartTime=" + timeStr + "\r\n");//加入测试开始时间记录
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					bw.append("ro.product.model=" + line + System.lineSeparator());
					deviceInfo.setModel(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfoWF(get model) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device displayId
			pGetInfo = Runtime.getRuntime().exec(getDisplayIdCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					bw.append("ro.build.display.id=" + line + System.lineSeparator());
					deviceInfo.setDisplayId(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfoWF(get displayId) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device Android Version
			pGetInfo = Runtime.getRuntime().exec(getVersionReleaseCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					bw.append("ro.build.version.release=" + line + System.lineSeparator());
					deviceInfo.setVersionRelease(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfoWF(get Android Version) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device Build Date
			pGetInfo = Runtime.getRuntime().exec(getBuildDateCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					bw.append("ro.build.date=" + line + System.lineSeparator());
					deviceInfo.setBuildDate(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfoWF(get Build Date) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device SDK Version
			pGetInfo = Runtime.getRuntime().exec(getAndroidSdkCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					bw.append("ro.build.version.sdk=" + line + System.lineSeparator());
					deviceInfo.setSdkVersion(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfoWF(get SDK Version) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			inputStream.close();
			errorReader.close();
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			showError("记录设备信息失败！", e);
		} catch (InterruptedException e) {
			showError("记录设备信息失败！", e);
		}
		return deviceInfo;
	}
	
	/**
	 * 根据ID获取设备信息
	 * @param deviceId 设备ID
	 */
	public DeviceInfo getDeviceInfo(String deviceId) {
		
		String getModelCmdStr = "adb -s " + deviceId + " shell getprop ro.product.model";
		String getDisplayIdCmdStr = "adb -s " + deviceId + " shell getprop ro.build.display.id ";
		String getVersionReleaseCmdStr = "adb -s " + deviceId + " shell getprop ro.build.version.release";//android版本platformVersion
		String getBuildDateCmdStr = "adb -s " + deviceId + " shell getprop ro.build.date";
		String getAndroidSdkCmdStr = "adb -s " + deviceId + " shell getprop ro.build.version.sdk";
		
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDeviceId(deviceId);
		try {
			//get device model
			Process pGetInfo = Runtime.getRuntime().exec(getModelCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			String line;
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					deviceInfo.setModel(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get model) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device displayId
			pGetInfo = Runtime.getRuntime().exec(getDisplayIdCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					deviceInfo.setDisplayId(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get displayId) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device Android Version
			pGetInfo = Runtime.getRuntime().exec(getVersionReleaseCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					deviceInfo.setVersionRelease(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get Android Version) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device Build Date
			pGetInfo = Runtime.getRuntime().exec(getBuildDateCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					deviceInfo.setBuildDate(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get Build Date) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			//get device SDK Version
			pGetInfo = Runtime.getRuntime().exec(getAndroidSdkCmdStr);
			inputStream = new BufferedReader(new InputStreamReader(pGetInfo.getInputStream(), "UTF-8"));
			errorReader = new BufferedReader(new InputStreamReader(pGetInfo.getErrorStream(), "UTF-8"));
			while((line = inputStream.readLine()) != null) {
				if (!line.matches("\\s*")) {//一个或多个空格
					deviceInfo.setSdkVersion(line);
				}
			}
			while((line = errorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("getDeviceInfo(get SDK Version) errorReader:" + line);
			}
			pGetInfo.waitFor();
			
			inputStream.close();
			errorReader.close();
		} catch (IOException e) {
			showError("根据ID获取设备信息失败！", e);
		} catch (InterruptedException e) {
			showError("根据ID获取设备信息失败！", e);
		}
		return deviceInfo;
	}
	
	/**
	 * root设备
	 * @param deviceId 设备ID
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public boolean rootDevice(String deviceId) throws IOException, InterruptedException {
		//adb -s 2e6408f0608200e2 shell cat /system/build.prop
		String getInfoCmdStr = "adb -s " + deviceId + " root";
		boolean rootResult = false;
		Process process = Runtime.getRuntime().exec(getInfoCmdStr);
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
		//这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果
		BufferedReader  errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
		
		String line;
		while((line = inputStream.readLine()) != null) {
			if (!line.matches("\\s*")) {//一个或多个空格
				StringBuilder builder = new StringBuilder();
				builder.append(line);
				display.asyncExec(new Runnable(){
					public void run() {
						textTrace.append(builder.toString() + "\r\n");
					}
				});
			}
		}
		while((line = errorReader.readLine()) != null) {
			if (!line.matches("\\s*")) {//一个或多个空格
				StringBuilder builder = new StringBuilder();
				builder.append(line);
				display.asyncExec(new Runnable(){
					public void run() {
						textTrace.append(builder.toString() + "\r\n");
					}
				});
			}
		}
		
		int result = process.waitFor();
		inputStream.close();
		errorReader.close();
		if (result == 0) {
			rootResult = true;
		}
		return rootResult;
	}
	
	/**
	 * 追加设备信息
	 * @param infoPath 设备信息文件路径
	 * @param appendStr 追加的字符串
	 */
	public void appendDeviceInfo(String infoPath, String appendStr) {
		File infoFile = new File(infoPath);
		try {
			if (!infoFile.exists()) {
				infoFile.createNewFile();
			}
			FileWriter fw = new FileWriter(infoPath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(appendStr + "\r\n");
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			showError("追加设备信息失败！", e);
		}
	}
	
	/**
	 * 获取包的错误信息
	 */
	public List<PackageErrorInfo> getPackageErrorInfo(String logDir) {
		List<AnrCrash> crashList = AnrCrashUtils.readAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, logDir);
		List<PackageErrorInfo> packageInfoList = Collections.synchronizedList(new ArrayList<PackageErrorInfo>());
		List<String> packageList = Collections.synchronizedList(new ArrayList<String>());
		
		//遍历crash
		for (AnrCrash crash : crashList) {
			PackageErrorInfo packageErrorInfo = new PackageErrorInfo();
			String PackageNameStr = crash.getPackageName();
			if (packageList.contains(PackageNameStr)) {//如果list中已有此包说明此包crash加1次
				for (PackageErrorInfo info : packageInfoList) {
					if (info.getPackageName().equals(PackageNameStr)) {//取到该信息
						info.setCrashCount(info.getCrashCount() + 1);
					}
				}
			} else {//如果list中没有此包说明第一次
				packageList.add(PackageNameStr);
				packageErrorInfo.setPackageName(PackageNameStr);//加入info中
				packageErrorInfo.setCrashCount(packageErrorInfo.getCrashCount() + 1);
//				packageErrorInfo.setLogPath(logDir);
				packageInfoList.add(packageErrorInfo);
			}
		}
		//遍历anr
		List<AnrCrash> anrList = AnrCrashUtils.readAnrCrashRecord(AnrCrashUtils.ANR_TYPE, logDir);
		for (AnrCrash anr : anrList) {
			PackageErrorInfo packageErrorInfo = new PackageErrorInfo();
			String PackageNameStr = anr.getPackageName();
			if (packageList.contains(PackageNameStr)) {//如果list中已有此包说明此包crash加1次
				for (PackageErrorInfo info : packageInfoList) {
					if (info.getPackageName().equals(PackageNameStr)) {//取到该信息
						info.setAnrCount(info.getAnrCount() + 1);
					}
				}
			} else {//如果list中没有此包说明第一次
				packageList.add(PackageNameStr);
				packageErrorInfo.setPackageName(PackageNameStr);//加入info中
				packageErrorInfo.setAnrCount(packageErrorInfo.getAnrCount() + 1);
//				packageErrorInfo.setLogPath(logDir);
				packageInfoList.add(packageErrorInfo);
			}
		}
		
		return packageInfoList;
	}
	
	/**
	 * 导出monkey测试信息到excel
	 * @param monkeyWorkDir
	 */
	public void exportMonkeyResult(String deviceId, String monkeyWorkDir) {
		DeviceInfo deviceInfo = getDeviceInfoFromFile(monkeyWorkDir);
		deviceInfo.setDeviceId(deviceId);
		
		List<PackageErrorInfo> packageInfoList = getPackageErrorInfo(monkeyWorkDir);
		
		String filePath = WinUtil.saveExcelDialog(shlSvMonkey, monkeyWorkDir);
		if (filePath != null && !filePath.equals("")) {
			try {
				PoiUtils.outputXlsxToFile(deviceInfo, packageInfoList, filePath);
			} catch (IOException e) {
				//事件处理
				MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_ERROR);
			    //设置对话框的标题
			    box.setText("错误");
			    //设置对话框显示的消息
			    box.setMessage(e.getMessage());
			    //打开对话框
			    box.open();
			}
		}
	}
	
	/**
	 * 导出monkey测试信息到excel
	 * @param monkeyWorkDir
	 */
	public void exportAllMonkeyResult(List<DeviceRecord> deviceRecords) {
		String filePath = WinUtil.saveExcelDialog(shlSvMonkey, System.getProperty("user.dir"));
		if (filePath != null && !filePath.equals("")) {
			try {
				PoiUtils.outputXlsxToFile(deviceRecords, filePath);
			} catch (IOException e) {
				//事件处理
				MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_ERROR);
				//设置对话框的标题
				box.setText("错误");
				//设置对话框显示的消息
				box.setMessage(e.getMessage());
				//打开对话框
				box.open();
			}
		}
	}
	
	/**
	 * 根据设备ID和monkey日志路径获取对应的设备信息和crashAnr信息返回记录
	 * @param monkeyWorkDir
	 */
	public DeviceRecord getDeviceRecord(String deviceId, String monkeyWorkDir) {
		DeviceRecord deviceRecord = new DeviceRecord();
		
		DeviceInfo deviceInfo = getDeviceInfoFromFile(monkeyWorkDir);
		deviceInfo.setDeviceId(deviceId);
		List<PackageErrorInfo> packageInfoList = getPackageErrorInfo(monkeyWorkDir);
		deviceRecord.setDeviceInfo(deviceInfo);
		deviceRecord.setPackageInfoList(packageInfoList);
		
		return deviceRecord;
	}
	
	/**
	 * 根据设备ID获取对应日志所在目录
	 * @param deviceStr
	 * @return
	 */
	public static String getDeviceLogsDir(String deviceStr) {
		String deviceStrTran = "";
		if(deviceStr.contains(":")){//网络adb的方式,则目录不能包含点和冒号，所以要转换为下划线和横杠
			deviceStrTran = deviceStr.replace(".", "-");
			deviceStrTran = deviceStrTran.replace(":", "_");
		} else {
			deviceStrTran = deviceStr;
		}
		return USERDIR + "\\" + deviceStrTran;
	}
	
	
	/**
	 * monkey测试
	 * @param deviceId 设备id
	 * @param monkeyCmdStr monkey指令
	 * @param monkeyWorkDir monkey工作目录
	 * @param table 记录表格
	 */
	public void monkeyRunning(String deviceId, String monkeyCmdStr, Table table) {
		getSettings();
		stopMonkeyByHand = false;//是否手动停止monkey
		MonkeyTaskBean monkeyTask = new MonkeyTaskBean();
		if (!monkeyThreadMap.containsKey(deviceId)) {//monkey线程
			monkeyTask.setThreadPid(Thread.currentThread().getId());
			monkeyTask.setThread(Thread.currentThread());
			monkeyThreadMap.put(deviceId, monkeyTask);
			MyUtils.printWithTimeMill("put pid=" + monkeyTask.getThreadPid());
		}
		
		Date startDate = new Date();
		String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(startDate);
		String nowTimeStrSpace = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
		//以设备名称和执行时间创建文件夹保存monkey日志
		String monkeyWorkDir = getDeviceLogsDir(deviceId) + "\\" + nowTimeStr;
		File filePathSaveLogs = new File(monkeyWorkDir);
		if (!filePathSaveLogs.exists()) {//创建以设备名称命名的路径
			filePathSaveLogs.mkdirs();
		}
		
		//拷贝黑名单或白名单到log
		if (monkeyCmdStr.contains("--pkg-blacklist-file")) {
			MyUtils.printWithTimeMill("保存黑名单文件到log。。。");
			String blackPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.BLACK_LIST);
			try {
				MyUtils.copyFileByPath(blackPath, monkeyWorkDir + "\\blacklist.txt");
			} catch (IOException e) {
				MyUtils.printWithTimeMill("保存黑名单文件到log失败!");
			}
		} else if (monkeyCmdStr.contains("--pkg-whitelist-file")) {
			MyUtils.printWithTimeMill("保存白名单文件到log。。。");
			String whiltListPath = PropUtil.getDevicePackageListPath(deviceId, PropUtil.WHITE_LIST);
			try {
				MyUtils.copyFileByPath(whiltListPath, monkeyWorkDir + "\\whitelist.txt");
			} catch (IOException e) {
				MyUtils.printWithTimeMill("保存白名单文件到log失败!");
			}
		}
		
		DeviceInfo runningDeviceInfo = getDeviceInfo(deviceId, monkeyWorkDir, nowTimeStrSpace);
		String sdkVersionStr = runningDeviceInfo.getSdkVersion();
		String getLogcatCmdStr = "adb -s " + deviceId + " shell ps -e | grep logcat";	//-e参数为列出所有进程
		try {
			int sdkVersionInt = Integer.parseInt(sdkVersionStr);
			if (sdkVersionInt >= 26 ) {
				getLogcatCmdStr = "adb -s " + deviceId + " shell ps -e | grep logcat";	//-e参数为列出所有进程
			} else {
				getLogcatCmdStr = "adb -s " + deviceId + " shell ps | grep logcat";		//-e参数为列出所有进程
			}
		} catch (NumberFormatException e) {
			MyUtils.printWithTimeMill("monkeyRunning() get android SDK NumberFormatException:" + e.getMessage());
		}
		
		appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "monkeyCmdStr=" + monkeyCmdStr);//增加指令
		appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "logcatPath=" + monkeyWorkDir + "\\logcat.txt");//增加log路径
		
		currentRecordList = RecordUtils.addRecord(deviceId, monkeyWorkDir, monkeyCmdStr);
		int index = currentRecordList.size();
		display.asyncExec(new Runnable(){
			public void run() {
				RecordUtils.updateRecordFile(currentRecordList);
				RecordUtils.updateTableUi(tableMonkeyRun);
			}
		});
		
		long startTestTime = System.currentTimeMillis();//这就是距离1970年1月1日0点0分0秒的毫秒数
		
		String pidMonkeyStr = "";
		//list 保存在跑monkey之前所有logcat线程IDs
		List<String> logcatInDeviceBeforeList = Collections.synchronizedList(new ArrayList<String>());
		try {
			Process pGetLogcat = Runtime.getRuntime().exec(getLogcatCmdStr);//执行命令获取已运行的所有logcat进程
			BufferedReader getLogcatBr = new BufferedReader(new InputStreamReader(pGetLogcat.getInputStream(), "UTF-8"));
			//这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果
			BufferedReader  getLogcatErrorReader = new BufferedReader(new InputStreamReader(pGetLogcat.getErrorStream(), "UTF-8"));
			String lineGetLogcatPids;
			while((lineGetLogcatPids = getLogcatBr.readLine()) != null) {
				if (!lineGetLogcatPids.matches("\\s*")) {   //一个或多个空格
					String[] strs = lineGetLogcatPids.split("\\s+");
					if (strs.length > 2) {
						pidMonkeyStr = strs[1];
						logcatInDeviceBeforeList.add(pidMonkeyStr);
					}
				}
			}
			while((lineGetLogcatPids = getLogcatErrorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("errorReader(lineGetLogcatPids):" + lineGetLogcatPids);
			}
			MyUtils.printWithTimeMill("logcatInDeviceBeforeList size = " + logcatInDeviceBeforeList.size());//目前有多少个logcat进程在运行
			int resultGetLogcat = pGetLogcat.waitFor();
			MyUtils.printWithTimeMill("resultGetLogcat = " + resultGetLogcat);
			getLogcatErrorReader.close();
			getLogcatBr.close();
		} catch (IOException e) {
			MyUtils.printWithTimeMill("monkeyRunning()_pGetLogcat_" + e.toString());
		} catch (InterruptedException e) {
			MyUtils.printWithTimeMill("monkeyRunning()_pGetLogcat_" + e.toString());
		}
		//logcat -v threadtime -b all命令启动一个logcat进程实时保存logcat
		LogcatThread logcatThread = new LogcatThread(deviceId, monkeyWorkDir);
		logcatThread.start();
		monkeyTask.setLogcatPid(logcatThread.getId());
		
		//list 保存跑monkey时新建的所有logcat线程IDs
		List<String> logcatInDeviceDiffList = Collections.synchronizedList(new ArrayList<String>());
		try {
			Process pGetLogcatAfter = Runtime.getRuntime().exec(getLogcatCmdStr);
			BufferedReader getLogcaAfterBr = new BufferedReader(new InputStreamReader(pGetLogcatAfter.getInputStream(), "UTF-8"));
			//这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果
			BufferedReader  getLocatErrorReader = new BufferedReader(new InputStreamReader(pGetLogcatAfter.getErrorStream(), "UTF-8"));
			String lineGetLogcatPidsAfter;
			while((lineGetLogcatPidsAfter = getLogcaAfterBr.readLine()) != null) {
				if (!lineGetLogcatPidsAfter.matches("\\s*")) {   //一个或多个空格
					String[] strs = lineGetLogcatPidsAfter.split("\\s+");
					if (strs.length > 2) {
						pidMonkeyStr = strs[1];
						//如果该logcat ID不是在跑monkey之前就存在，说明该logcat 线程为该monkey对应的logcat
						if (!logcatInDeviceBeforeList.contains(pidMonkeyStr)) {
							logcatInDeviceDiffList.add(pidMonkeyStr);
						}
					}
				}
			}
			MyUtils.printWithTimeMill("logcatInDeviceDiffList size = " + logcatInDeviceDiffList.size());
			while((lineGetLogcatPidsAfter = getLocatErrorReader.readLine()) != null) {
				MyUtils.printWithTimeMill("errorReader(lineGetLogcatPids):" + lineGetLogcatPidsAfter);
			}
			int resultGetLogcatAfter = pGetLogcatAfter.waitFor();
			MyUtils.printWithTimeMill("resultGetLogcatAfter = " + resultGetLogcatAfter);
			
			getLocatErrorReader.close();
			getLogcaAfterBr.close();
		} catch (IOException e) {
			MyUtils.printWithTimeMill("monkeyRunning()_pGetLogcatAfter_" + e.toString());
		} catch (InterruptedException e) {
			MyUtils.printWithTimeMill("monkeyRunning()_pGetLogcatAfter_" + e.toString());
		}
		
		//计时线程
		TimerThread timerThread = new TimerThread(display, tableMonkeyRun, index, startTestTime);
		timerThread.start();
		monkeyTask.setLogcatPid(timerThread.getId());
		monkeyThreadMap.put(deviceId, monkeyTask);
		
//		BufferedReader inputStream = null;//标准输入流
//		BufferedReader errorReader = null;//错误流
		//标准输入流文件
//		FileWriter fw = null;
//		BufferedWriter bw = null;
		//错误流文件
//		FileWriter errFileWriter = null;
//		BufferedWriter errBuffWriter = null;
		
		try {
			final Process p = Runtime.getRuntime().exec(monkeyCmdStr);
			final String monkeyEventLogStr = monkeyWorkDir + "\\monkey.txt";
//			String monkeyErrorLogPathStr = monkeyWorkDir + "\\monkey_err.txt";
			FileWriter fwMonkeyLog = new FileWriter(monkeyEventLogStr, true);
			BufferedWriter bwMonkeyLog = new BufferedWriter(fwMonkeyLog);
			monkeyTask.setFileWriter(fwMonkeyLog);
			monkeyTask.setBufferdWriter(bwMonkeyLog);
			monkeyThreadMap.put(deviceId, monkeyTask);
			//保存错误流的文件
//			errFileWriter = new FileWriter(monkeyErrorLogPathStr, true);
//			errBuffWriter = new BufferedWriter(errFileWriter);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String line;
					int crashCounter = 0;
					int anrCounter = 0;
					String errorMessageStr = "";
					boolean isMonkeyPass = false;
					long startTestTimeS = System.currentTimeMillis();//这就是距离1970年1月1日0点0分0秒的毫秒数
					BufferedWriter bw;
					try {
						BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
						bw = new BufferedWriter(new FileWriter(monkeyEventLogStr, true));
						try {
							while((line = inputStream.readLine()) != null && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
								if (!line.matches("\\s*")) {   //一个或多个空格
									bw.append(line + "\r\n");
									bw.flush();
								}
								if (line.contains("CRASH:")) { //检测crash
									crashCounter ++;
									int Counter = crashCounter;
									String titleStr = line;
									
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append(line + "\r\n");
									
									boolean keepTest = true;
									String newLine;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
										newLine = inputStream.readLine();
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();
											if (newLine.startsWith("//")) {//crash信息都以"//"开头
												stringBuilder.append(newLine + "\r\n");
											} else {
												keepTest = false;
											}
										}
									}
									
									display.asyncExec(new Runnable(){//更新crash记录
										public void run() {
											AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
											RecordUtils.updateOneRecordInFile(index, RecordUtils.CRASH_TYPE, Counter + "");
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (line.contains("NOT RESPONDING:")) {//检测ANR
									anrCounter ++;
									final int counter = anrCounter;
									final String titleStr = line;
									
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append(line + "\r\n");
									boolean keepTest = true;
									String newLine;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
										newLine = inputStream.readLine();
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();
											if (! newLine.startsWith("//")) {//直到出现“//”说明anr信息打印完毕
												stringBuilder.append(newLine + "\r\n");
											} else {
												keepTest = false;
											}
										}
									}
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.ANR_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ANR_TYPE, counter + "");
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (line.contains("Monkey finished")) {//检测Monkey finished
									isMonkeyPass = true;
								} else if (line.trim().startsWith("**")) {
									//出现异常退出测试，如：** Error: SecurityException while injecting event.
									errorMessageStr += (line + "\r\n");
									final String erroeMessageView = errorMessageStr;
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (line.contains(":Monkey: seed=")) {
									String[] strings = line.trim().split(" ");
									String[] seedStrs = strings[1].split("=");
									appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "MonkeySeed=" + seedStrs[1]);
								} else if (line.trim().startsWith("java.lang.")) {
									//第一层循环已append到文件不用再append
									errorMessageStr += (line + "\r\n");
									boolean keepTest = true;
									String newLine;
									int lineCounter = 0;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) != null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
										newLine = inputStream.readLine();
										lineCounter ++;
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();//第二层循环要再append
											if (newLine.trim().startsWith("at") && lineCounter<50) {
												errorMessageStr += (line + "\r\n");
												final String erroeMessageView = errorMessageStr;
												display.asyncExec(new Runnable() {//更新anr记录
													public void run() {
														RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
														RecordUtils.updateTableUi(tableMonkeyRun);
													}
												});
											} else {
												keepTest = false;
											}
										}
									}
								} else if (line.contains("WATCHDOG") || line.contains("watchdog")) {
									MyUtils.printWithTimeMill("MonkeyRunningInputStream: WATCHDOG");
									//出现软件看门狗动作,待增加判断是否遇到狗急时退出测试
									errorMessageStr += (line + "\r\n");
									final String erroeMessageView = errorMessageStr;
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								}
							}
							if (isMonkeyPass) {
								display.asyncExec(new Runnable() {//更新anr记录
									public void run() {
										RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.PASS_STATUS);
										RecordUtils.updateTableUi(tableMonkeyRun);
									}
								});
							}
							long endTestTimeS = System.currentTimeMillis();//测试结束时间点距离1970年1月1日0点0分0秒的毫秒数
							String runTimeStr = MyUtils.millisToTimeMinuteCn(endTestTimeS - startTestTimeS);
							display.asyncExec(new Runnable() {
								public void run() {
									RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, runTimeStr);
									RecordUtils.updateTableUi(tableMonkeyRun);
								}
							});
						} catch (IOException e) {
							MyUtils.printWithTimeMill(e.getMessage());
							e.printStackTrace();
						} finally {
							try {
								inputStream.close();
								bw.close();
							} catch (Exception e2) {
								MyUtils.printWithTimeMill(e2.getMessage());
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					//错误流
					String errLine;
					int crashCounter = 0;
					int anrCounter = 0;
					String errorMessageStr = "";
					boolean isMonkeyPass = false;
					//这里一定要注意错误流的读取，不然很容易阻塞，得不到想要的结果
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					BufferedWriter bw;
					try {
						bw = new BufferedWriter(new FileWriter(monkeyEventLogStr, true));
						try {
							while((errLine = errorReader.readLine()) != null && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
								bw.append(errLine + "\r\n");
								bw.flush();
								if (errLine.contains("CRASH:")) { //检测crash
									crashCounter ++;
									final int Counter = crashCounter;
									final String titleStr = errLine;
									
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append(errLine + "\r\n");
									
									boolean keepTest = true;
									String newLine;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
										newLine = errorReader.readLine();
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();
											if (newLine.startsWith("//")) {//crash信息都以"//"开头
												stringBuilder.append(newLine + "\r\n");
											} else {
												keepTest = false;
											}
										}
									}
									
									display.asyncExec(new Runnable(){//更新crash记录
										public void run() {
											AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
											RecordUtils.updateOneRecordInFile(index, RecordUtils.CRASH_TYPE, Counter + "");
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (errLine.contains("NOT RESPONDING:")) {//检测ANR
									anrCounter ++;
									final int counter = anrCounter;
									final String titleStr = errLine;
									
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append(errLine + "\r\n");
									boolean keepTest = true;
									String newLine;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
										newLine = errorReader.readLine();
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();
											if (! newLine.startsWith("//")) {//直到出现“//”说明anr信息打印完毕
												stringBuilder.append(newLine + "\r\n");
											} else {
												keepTest = false;
											}
										}
									}
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.ANR_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ANR_TYPE, counter + "");
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (errLine.contains("Monkey finished")) {//检测Monkey finished
									isMonkeyPass = true;
								} else if (errLine.trim().startsWith("**")) {
									//出现异常退出测试，如：** Error: SecurityException while injecting event.
									errorMessageStr += (errLine + "\r\n");
									final String erroeMessageView = errorMessageStr;
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								} else if (errLine.contains(":Monkey: seed=")) {
									String[] strings = errLine.trim().split(" ");
									String[] seedStrs = strings[1].split("=");
									appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "MonkeySeed=" + seedStrs[1]);
								} else if (errLine.trim().startsWith("java.lang.")) {
									//第一层循环已append到文件不用再append
									errorMessageStr += (errLine + "\r\n");
									boolean keepTest = true;
									String newLine;
									int lineCounter = 0;
									while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) != null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
										newLine = errorReader.readLine();
										lineCounter ++;
										if (newLine != null && !newLine.matches("\\s*")) {
											bw.append(newLine + "\r\n");
											bw.flush();//第二层循环要再append
											if (newLine.trim().startsWith("at") && lineCounter<50) {
												errorMessageStr += (errLine + "\r\n");
												final String erroeMessageView = errorMessageStr;
												display.asyncExec(new Runnable() {//更新anr记录
													public void run() {
														RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
														RecordUtils.updateTableUi(tableMonkeyRun);
													}
												});
											} else {
												keepTest = false;
											}
										}
									}
								} else if (errLine.contains("WATCHDOG") || errLine.contains("watchdog")) {
									//出现软件看门狗动作,待增加判断是否遇到狗急时退出测试
									errorMessageStr += (errLine + "\r\n");
									final String erroeMessageView = errorMessageStr;
									display.asyncExec(new Runnable() {//更新anr记录
										public void run() {
											RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, erroeMessageView);
											RecordUtils.updateTableUi(tableMonkeyRun);
										}
									});
								}
							}
							if (isMonkeyPass) {
								display.asyncExec(new Runnable() {//更新anr记录
									public void run() {
										RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.PASS_STATUS);
										RecordUtils.updateTableUi(tableMonkeyRun);
									}
								});
							}
						} catch (IOException e) {
							MyUtils.printWithTimeMill(e.toString());
						} finally {
							try {
								errorReader.close();
								bw.close();
							} catch (Exception e2) {
								MyUtils.printWithTimeMill(e2.getMessage());
							}
						}
					} catch (IOException e1) {
						MyUtils.printWithTimeMill(e1.toString());
					}
				}
			}).start();
			
			int monkeyResult = p.waitFor();
			MyUtils.printWithTimeMill("monkeyResult(p.waitFor)=" + monkeyResult);
			//kill掉对应的logcat进程
//			if (logcatInDeviceDiffList.size() > 0) {
//				for (String string : logcatInDeviceDiffList) {
//					Process pKillLogcat = Runtime.getRuntime().exec("adb -s " + deviceId + " shell kill " + string);
//					int killLogcatResult = pKillLogcat.waitFor();
//					if (killLogcatResult != 0) {
//				    	display.asyncExec(new Runnable(){
//				    		public void run() {
//				    			MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
//								//设置对话框的标题
//								box.setText("警告");
//								//设置对话框显示的消息
//								box.setMessage("kill logcat 失败,请手动kill!！");
//								//打开对话框
//								box.open();
//				    		}
//				    	});
//					} else {
//						MyUtils.printWithTimeMill(deviceId + " kill logcat thread " + string + " OK!");
//					}
//				}
//			}
			
			logcatThread.stop = true;
			logcatThread.interrupt();
			timerThread.stop = true;
			timerThread.interrupt();
			monkeyThreadMap.remove(deviceId);
			MyUtils.printWithTimeMill("mapSize=" + monkeyThreadMap.size());
			//
			appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "TestCostTime=" + (System.currentTimeMillis() - startTestTime));//测试耗时
			
			//是否截图
			if (endTakePic.equals("true")) {
				excuteWinCmd("adb -s " + deviceId + " shell rm /data/local/tmp/screenshort.png");//删除旧的截图文件
				//"/system/bin/screencap -p /data/local/tmp/" + fileNameNoSuffix+ ".png";
				excuteWinCmd("adb -s " + deviceId + " shell /system/bin/screencap -p /data/local/tmp/screenshort.png");
				excuteWinCmd("adb -s " + deviceId + " pull /data/local/tmp/screenshort.png " + monkeyWorkDir);
				MyUtils.printWithTimeMill("已截图到:" + monkeyWorkDir + "/screenshort.png");
			}
			
			//是否执行指定文件里面的命令
			if (endGetLogs.equals("true")) {
				excuteCmdFromFile(getLogCmdFilePath, monkeyWorkDir);
			}
			
			if (monkeyResult == 0) {//monkey正常结束
				display.asyncExec(new Runnable() {
					public void run() {
						String statusStr = RecordUtils.readOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE);
						String crashs = RecordUtils.readOneRecordInFile(index, RecordUtils.CRASH_TYPE);
						String anrs = RecordUtils.readOneRecordInFile(index, RecordUtils.ANR_TYPE);
						String message = RecordUtils.readOneRecordInFile(index, RecordUtils.ERROR_MESSAGE);
						if (statusStr.equals(RecordUtils.PASS_STATUS) && crashs.equals("0") && anrs.equals("0")) {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.PASS_STATUS);
						} else {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.FINISHED_STATUS);
						}
						RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message);
						RecordUtils.updateTableUi(tableMonkeyRun);
					}
				});
			} else {
				MyUtils.printWithTimeMill("stopMonkeyByHand = " + stopMonkeyByHand);
				if (stopMonkeyByHand) {
					stopMonkeyByHand = false;
					display.asyncExec(new Runnable(){
						public void run() {
							String message = RecordUtils.readOneRecordInFile(index, RecordUtils.ERROR_MESSAGE);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
							RecordUtils.updateTableUi(tableMonkeyRun);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message + "\r\nMonkey被手动停止!");
						}
					});
				} else {
					display.asyncExec(new Runnable(){
						public void run() {
							String message = RecordUtils.readOneRecordInFile(index, RecordUtils.ERROR_MESSAGE);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.ERROR_STATUS);
							RecordUtils.updateTableUi(tableMonkeyRun);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message + "\r\nMonkey命令运行出现错误，请检查!");
						}
					});
				}
			}
		} catch (IOException e) {
			MyUtils.printWithTimeMill(deviceId + " monkeyRun IOException:" + e.getMessage());
			showError("monkey执行失败！", e);
			try {//kill掉对应的logcat进程
				if (logcatInDeviceDiffList.size() > 0) {
					for (String string : logcatInDeviceDiffList) {
						Process p = Runtime.getRuntime().exec("adb -s " + deviceId + " shell kill " + string);
						int killLogcatResult = p.waitFor();
						if (killLogcatResult != 0) {
							display.asyncExec(new Runnable(){
								public void run() {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
									//设置对话框的标题
									box.setText("警告");
									//设置对话框显示的消息
									box.setMessage(deviceId + " kill logcat 失败,请手动kill!！");
									//打开对话框
									box.open();
								}
							});
						} else {
							MyUtils.printWithTimeMill(deviceId + " kill logcat thread " + string + " OK!");
						}
					}
				}
			} catch (IOException e2) {
				// TODO: handle exception
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
			}
			//把停止标志位都置为true退出
			if (logcatThread != null && logcatThread.isAlive()) {
				MyUtils.printWithTimeMill(deviceId + " stop logcat InterruptedException");
				logcatThread.stop = true;
				logcatThread.interrupt();
			}
			if (timerThread != null && timerThread.isAlive()) {
				timerThread.stop = true;
				timerThread.interrupt();
			}
			if (monkeyThreadMap.get(deviceId) != null) {
				monkeyThreadMap.remove(deviceId);
			}
			display.asyncExec(new Runnable(){
				public void run() {
					MyUtils.printWithTimeMill("stopMonkeyByHand=" + stopMonkeyByHand);
					if (stopMonkeyByHand) {
						stopMonkeyByHand = false;
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
					} else {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.ERROR_STATUS);
					}
					RecordUtils.updateTableUi(tableMonkeyRun);
				}
			});
		} catch (InterruptedException e) {
			appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "TestCostTime=" + (System.currentTimeMillis() - startTestTime));//测试耗时
			MyUtils.printWithTimeMill(deviceId + " InterruptedException in monkey run");
			try {//kill掉对应的logcat进程
				if (logcatInDeviceDiffList.size() > 0) {
					for (String string : logcatInDeviceDiffList) {
						Process p = Runtime.getRuntime().exec("adb -s " + deviceId + " shell kill " + string);
						int killLogcatResult = p.waitFor();
						if (killLogcatResult != 0) {
							display.asyncExec(new Runnable(){
								public void run() {
									MessageBox box = new MessageBox(shlSvMonkey ,SWT.YES | SWT.ICON_WARNING);
									//设置对话框的标题
									box.setText("警告");
									//设置对话框显示的消息
									box.setMessage(deviceId + " kill logcat 失败,请手动kill!！");
									//打开对话框
									box.open();
								}
							});
						} else {
							MyUtils.printWithTimeMill(deviceId + " kill logcat thread " + string + " OK!");
						}
					}
				}
			} catch (IOException e2) {
				MyUtils.printWithTimeMill(deviceId + " kill logcat thread IOException fail : " + e2.getMessage());
			} catch (InterruptedException e1) {
				MyUtils.printWithTimeMill(deviceId + " kill logcat thread InterruptedException fail : " + e1.getMessage());
			}
			//把停止标志位都置为true退出
			if (logcatThread != null && logcatThread.isAlive()) {
				MyUtils.printWithTimeMill(deviceId + " stop logcat InterruptedException");
				logcatThread.stop = true;
				logcatThread.interrupt();
			}
			if (timerThread != null && timerThread.isAlive()) {
				timerThread.stop = true;
				timerThread.interrupt();
			}
			if (monkeyThreadMap.get(deviceId) != null) {
				monkeyThreadMap.remove(deviceId);
			}
			display.asyncExec(new Runnable(){
				public void run() {
					MyUtils.printWithTimeMill("stopMonkeyByHand=" + stopMonkeyByHand);
					if (stopMonkeyByHand) {
						stopMonkeyByHand = false;
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.INTERRUPT_STATUS);
					} else {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.ERROR_STATUS);
					}
					RecordUtils.updateTableUi(tableMonkeyRun);
				}
			});
			MyUtils.printWithTimeMill(deviceId + " monkeyRun InterruptedException:" + e.getMessage());
		}
	}
	
	/**
	 * 顺序名单测试
	 * @param deviceId 设备ID
	 * @param orderList 顺序测试的应用程序包列表
	 * @param table 更新记录的表格
	 */
	public void monkeyOrderRunning(String deviceId, List<String> orderList, Table table) {
		//TODO:
		if (!monkeyThreadMap.containsKey(deviceId)) {
			MonkeyTaskBean monkeyTask = new MonkeyTaskBean();
			monkeyTask.setThreadPid(Thread.currentThread().getId());
			monkeyTask.setThread(Thread.currentThread());
			monkeyThreadMap.put(deviceId, monkeyTask);
			MyUtils.printWithTimeMill("put pid=" + monkeyTask.getThreadPid());
		}
		
		Date startDate = new Date();
		String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(startDate);
		String nowTimeStrSpace = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
		//以设备名称和执行时间创建文件夹保存monkey日志
		String monkeyWorkDir = USERDIR + "\\" + deviceId + "\\" + nowTimeStr;
		File filePathSaveLogs = new File(monkeyWorkDir);
		if (!filePathSaveLogs.exists()) {//创建以设备名称命名的路径
			filePathSaveLogs.mkdirs();
		}
		getDeviceInfo(deviceId, monkeyWorkDir, nowTimeStrSpace);
		StringBuilder monkeyCmdStr = new StringBuilder();
		for (String string : orderList) {
			monkeyCmdStr.append(getMonkeyCmd(deviceId, string) + ";");
		}
		appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "monkeyCmdStr=" + monkeyCmdStr.toString());//增加指令
		appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "logcatPath=" + monkeyWorkDir + "\\logcat.txt");//增加log路径
		
		currentRecordList = RecordUtils.addRecord(deviceId, monkeyWorkDir, monkeyCmdStr.toString());
		int index = currentRecordList.size();
		display.asyncExec(new Runnable(){
			public void run() {
				RecordUtils.updateRecordFile(currentRecordList);
				RecordUtils.updateTableUi(tableMonkeyRun);
			}
		});
		
		int crashCounter = 0;
		int anrCounter = 0;
		
		long startTestTime = 0;//开始测试时刻
		long endTestTime = 0;  //测试结束时刻
		startTestTime = System.currentTimeMillis();//这就是距离1970年1月1日0点0分0秒的毫秒数
		
		//logcat -b all -v time
		LogcatThread logcatThread = new LogcatThread(deviceId, monkeyWorkDir);
		logcatThread.start();
		//计时线程
		TimerThread timerThread = new TimerThread(display, tableMonkeyRun, index, startTestTime);
		timerThread.start();
		
		String errorMessageStr = "";
		BufferedReader inputStream = null;
		BufferedReader errorReader = null;
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			String monkeyEventLogStr = monkeyWorkDir + "\\monkey.txt";
			fw = new FileWriter(monkeyEventLogStr, true);
			bw = new BufferedWriter(fw);
			
			boolean isMonkeyPass = false;
			int monkeyResult = 0;
			String everyOrderCmdStr = "";//monkey命令
			String seedStr = "";
			
			//程序在运行，且该测试进程不为空且继续进行时
			for (int i = 0; i < orderList.size() && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) && monkeyResult==0; i++) {
				if (i > 0) {
					everyOrderCmdStr = getMonkeyCmd(deviceId, orderList.get(i), seedStr);//第二个应用以后的seed值都设置为跟第一个一样
					MyUtils.printWithTimeMill("everyOrderCmdStr(" + i + "):" + everyOrderCmdStr);
				} else {
					everyOrderCmdStr = getMonkeyCmd(deviceId, orderList.get(i));		 //获取到monkey命令
					MyUtils.printWithTimeMill("everyOrderCmdStr(" + i + "):" + everyOrderCmdStr);
				}
				
				Process p = Runtime.getRuntime().exec(everyOrderCmdStr);				 //开始一条一条指令执行
				inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
				//这里一定要注意错误流的读取，不然很容易阻塞，得不到想要的结果
				errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
				
				String line;

				
				while((line = inputStream.readLine()) != null && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
					if (!line.matches("\\s*")) {   //一个或多个空格
						bw.append(line + "\r\n");
						bw.flush();
					}
					if (line.contains("CRASH:")) { //检测crash
						bw.append(line + "\r\n");
						bw.flush();
						
						crashCounter ++;
						final int Counter = crashCounter;
						final String titleStr = line;
						
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(line + "\r\n");
						
						boolean keepTest = true;
						String newLine;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
							newLine = inputStream.readLine();
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (newLine.startsWith("//")) {
									stringBuilder.append(newLine + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
						
						display.asyncExec(new Runnable(){//更新crash记录
							public void run() {
								AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
								RecordUtils.updateOneRecordInFile(index, RecordUtils.CRASH_TYPE, Counter + "");
								RecordUtils.updateTableUi(tableMonkeyRun);
							}
						});
					} else if (line.contains("NOT RESPONDING:")) {//检测ANR
						bw.append(line + "\r\n");
						bw.flush();
						
						anrCounter ++;
						final int counter = anrCounter;
						final String titleStr = line;
						
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(line + "\r\n");
						boolean keepTest = true;
						String newLine;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
							newLine = inputStream.readLine();
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (! newLine.startsWith("//")) {
									stringBuilder.append(newLine + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
						display.asyncExec(new Runnable() {//更新anr记录
							public void run() {
								AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.ANR_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
								RecordUtils.updateOneRecordInFile(index, RecordUtils.ANR_TYPE, counter + "");
								RecordUtils.updateTableUi(tableMonkeyRun);
							}
						});
					} else if (line.contains("Monkey finished")) {//检测Monkey finished
						isMonkeyPass = true;
					} else if (line.trim().startsWith("**")) {
						//出现异常退出测试，如：** Error: SecurityException while injecting event.
						errorMessageStr += (line + "\r\n");
					} else if (line.contains(":Monkey: seed=")) {
						String[] strings = line.trim().split(" ");
						String[] seedStrs = strings[1].split("=");
						seedStr = seedStrs[1];
						MyUtils.printWithTimeMill("seedStr(" + orderList.get(i)+ ")=" + seedStr);
						appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "MonkeySeed=" + seedStr);
					} else if (line.trim().startsWith("java.lang.")) {
						bw.append(line + "\r\n");
						bw.flush();
						
						errorMessageStr += (line + "\r\n");
						boolean keepTest = true;
						String newLine;
						int lineCounter = 0;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) != null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
							newLine = inputStream.readLine();
							lineCounter ++;
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (newLine.trim().startsWith("at") && lineCounter<50) {
									errorMessageStr += (line + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
					} else if (line.contains("WATCHDOG") || line.contains("watchdog")) {
						//出现软件看门狗动作,待增加判断是否遇到狗急时退出测试
						bw.append(line + "\r\n");
						bw.flush();
						errorMessageStr += (line + "\r\n");
					}
				}
				while((line = errorReader.readLine()) != null && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
					if (!line.matches("\\s*")) {   //一个或多个空格
						bw.append(line + "\r\n");
						bw.flush();
					}
					if (line.contains("CRASH:")) { //检测crash
						bw.append(line + "\r\n");
						bw.flush();
						
						crashCounter ++;
						final int Counter = crashCounter;
						final String titleStr = line;
						
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(line + "\r\n");
						
						boolean keepTest = true;
						String newLine;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
							newLine = errorReader.readLine();
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (newLine.startsWith("//")) {
									stringBuilder.append(newLine + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
						
						display.asyncExec(new Runnable(){//更新crash记录
							public void run() {
								AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
								RecordUtils.updateOneRecordInFile(index, RecordUtils.CRASH_TYPE, Counter + "");
								RecordUtils.updateTableUi(tableMonkeyRun);
							}
						});
					} else if (line.contains("NOT RESPONDING:")) {//检测ANR
						bw.append(line + "\r\n");
						bw.flush();
						
						anrCounter ++;
						final int counter = anrCounter;
						final String titleStr = line;
						
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(line + "\r\n");
						boolean keepTest = true;
						String newLine;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) !=null) && monkeyThreadMap.get(deviceId).isKeepRunning()) ) {
							newLine = errorReader.readLine();
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (! newLine.startsWith("//")) {
									stringBuilder.append(newLine + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
						display.asyncExec(new Runnable() {//更新anr记录
							public void run() {
								AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.ANR_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
								RecordUtils.updateOneRecordInFile(index, RecordUtils.ANR_TYPE, counter + "");
								RecordUtils.updateTableUi(tableMonkeyRun);
							}
						});
					} else if (line.contains("Monkey finished")) {//检测Monkey finished
						isMonkeyPass = true;
					} else if (line.trim().startsWith("**")) {
						//出现异常退出测试，如：** Error: SecurityException while injecting event.
						errorMessageStr += (line + "\r\n");
					} else if (line.contains(":Monkey: seed=")) {
						String[] strings = line.trim().split(" ");
						String[] seedStrs = strings[1].split("=");
						seedStr = seedStrs[1];
						MyUtils.printWithTimeMill("seedStr(" + orderList.get(i)+ ")=" + seedStr);
						appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "MonkeySeed=" + seedStr);
					} else if (line.trim().startsWith("java.lang.")) {
						bw.append(line + "\r\n");
						bw.flush();
						
						errorMessageStr += (line + "\r\n");
						boolean keepTest = true;
						String newLine;
						int lineCounter = 0;
						while(keepTest && programRunning==true && ((monkeyThreadMap.get(deviceId) != null) && monkeyThreadMap.get(deviceId).isKeepRunning())) {
							newLine = errorReader.readLine();
							lineCounter ++;
							if (newLine != null && !newLine.matches("\\s*")) {
								bw.append(newLine + "\r\n");
								bw.flush();
								if (newLine.trim().startsWith("at") && lineCounter<50) {
									errorMessageStr += (line + "\r\n");
								} else {
									keepTest = false;
								}
							}
						}
					} else if (line.contains("WATCHDOG") || line.contains("watchdog")) {
						//出现软件看门狗动作,待增加判断是否遇到狗急时退出测试
						bw.append(line + "\r\n");
						bw.flush();
						errorMessageStr += (line + "\r\n");
					}
				}
				endTestTime = System.currentTimeMillis();//测试结束时间点距离1970年1月1日0点0分0秒的毫秒数
				String runTimeStr = MyUtils.millisToTimeMinuteCn(endTestTime - startTestTime);
				display.asyncExec(new Runnable() {
					public void run() {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, runTimeStr);
						RecordUtils.updateTableUi(tableMonkeyRun);
					}
				});
				
				monkeyResult = p.waitFor();
			}
			bw.close();
			fw.close();
			inputStream.close();
			errorReader.close();
			
			logcatThread.stop = true;
			logcatThread.interrupt();
			timerThread.stop = true;
			timerThread.interrupt();
			monkeyThreadMap.remove(deviceId);
			MyUtils.printWithTimeMill("mapSize=" + monkeyThreadMap.size());
			//
			appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "TestCostTime=" + (System.currentTimeMillis() - startTestTime));//测试耗时
			
			final String message = errorMessageStr;
			if (monkeyResult == 0) {
				final boolean testPass = isMonkeyPass;
				final int crashs = crashCounter;
				final int anrs = anrCounter;
				display.asyncExec(new Runnable() {
					public void run() {
						if (testPass && crashs == 0 && anrs == 0) {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.PASS_STATUS);
						} else {
							RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.FINISHED_STATUS);
						}
						RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message);
						RecordUtils.updateTableUi(tableMonkeyRun);
					}
				});
			} else {
				display.asyncExec(new Runnable(){
					public void run() {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.ERROR_STATUS);
						RecordUtils.updateTableUi(tableMonkeyRun);
						RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message + "\r\nMonkey命令运行出现错误，请检查!");
					}
				});
			}
			display.asyncExec(new Runnable(){
				public void run() {
					final String messageError = RecordUtils.readOneRecordInFile(index, RecordUtils.ERROR_MESSAGE);
					if (messageError!=null && !message.equals("")) {
						textTrace.setText(messageError);
					} else {
						textTrace.setText("");
					}
				}
			});
		} catch (IOException e) {
			MyUtils.printWithTimeMill("monkeyRun IOException:" + e.getMessage());
			showError("monkey执行失败！", e);
		} catch (InterruptedException e) {
			//把停止标志位都置为true退出
			if (logcatThread != null && logcatThread.isAlive()) {
				logcatThread.stop = true;
				logcatThread.interrupt();
			}
			if (timerThread != null && timerThread.isAlive()) {
				timerThread.stop = true;
				timerThread.interrupt();
			}
			if (monkeyThreadMap.get(deviceId) != null) {
				monkeyThreadMap.remove(deviceId);
			}
			display.asyncExec(new Runnable(){
				public void run() {
					RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.ERROR_STATUS);
					RecordUtils.updateTableUi(tableMonkeyRun);
				}
			});
			MyUtils.printWithTimeMill("monkeyRun InterruptedException:" + e.getMessage());
		}
	}
	
	/**
	 * monkey测试
	 * @param deviceId 设备id
	 * @param monkeyCmdStr monkey指令
	 * @param monkeyWorkDir monkey工作目录
	 * @param table 记录表格
	 * @throws InterruptedException 
	 */
	public void monkeyLogAnalyze(String filePath, Table table) throws InterruptedException {
		String deviceId = "未知设备";
		Date startDate = new Date();
		String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(startDate);
		//以设备名称和执行时间创建文件夹保存monkey日志
		String monkeyWorkDir = USERDIR + "\\" + deviceId + "\\" + nowTimeStr;
		File filePathSaveLogs = new File(monkeyWorkDir);
		if (!filePathSaveLogs.exists()) {//创建以设备名称命名的路径
			filePathSaveLogs.mkdirs();
		}
		
		currentRecordList = RecordUtils.addRecord(deviceId, monkeyWorkDir, "");
		int index = currentRecordList.size();
		display.asyncExec(new Runnable(){
			public void run() {
				RecordUtils.updateRecordFile(currentRecordList);
				RecordUtils.updateTableUi(tableMonkeyRun);
			}
		});
		
		int crashCounter = 0;
		int anrCounter = 0;
		
		long startTestTime = 0;//开始测试时刻
		long endTestTime = 0;  //测试结束时刻
		startTestTime = System.currentTimeMillis();//这就是距离1970年1月1日0点0分0秒的毫秒数
		
		//计时线程
		TimerThread timerThread = new TimerThread(display, tableMonkeyRun, index, startTestTime);
		timerThread.start();
		
		String errorMessageStr = "";
		try {
//			FileReader fileReader = new FileReader(filePath);
//			BufferedReader inputStream = new BufferedReader(fileReader); 

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			String line;
			boolean isMonkeyPass = false;
			while((line = inputStream.readLine()) != null && programRunning==true) {
				if (line.contains("CRASH:")) { //检测crash
					crashCounter ++;
					final int counter = crashCounter;
					final String titleStr = line;
					
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(line + "\r\n");
					
					boolean keepTest = true;
					String newLine;
					while(keepTest && programRunning==true) {
						newLine = inputStream.readLine();
						if (newLine != null && !newLine.matches("\\s*")) {
							if (newLine.startsWith("//")) {
								stringBuilder.append(newLine + "\r\n");
							} else {
								keepTest = false;
							}
						}
					}
					display.asyncExec(new Runnable(){//更新crash记录
						public void run() {
							AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.CRASH_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.CRASH_TYPE, counter + "");
							RecordUtils.updateTableUi(tableMonkeyRun);
						}
					});
				} else if (line.contains("NOT RESPONDING:")) {//检测ANR
					anrCounter ++;
					final int counter = anrCounter;
					final String titleStr = line;
					
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(line + "\r\n");
					boolean keepTest = true;
					String newLine;
					while(keepTest && programRunning==true) {
						newLine = inputStream.readLine();
						if (newLine != null && !newLine.matches("\\s*")) {
							if (! newLine.startsWith("//")) {
								stringBuilder.append(newLine + "\r\n");
							} else {
								keepTest = false;
							}
						}
					}
					display.asyncExec(new Runnable() {//更新anr记录
						public void run() {
							AnrCrashUtils.addAnrCrashRecord(AnrCrashUtils.ANR_TYPE, titleStr, stringBuilder.toString(), monkeyWorkDir);
							RecordUtils.updateOneRecordInFile(index, RecordUtils.ANR_TYPE, counter + "");
							RecordUtils.updateTableUi(tableMonkeyRun);
						}
					});
				} else if (line.contains("Monkey finished")) {//检测Monkey finished
					isMonkeyPass = true;
				} else if (line.trim().startsWith("**")) {
					//出现异常退出测试，如：** Error: SecurityException while injecting event.
					if (!line.contains("Permissions error starting activity")) {
						errorMessageStr += (line + "\r\n");
					}
				} else if (line.contains(":Monkey: seed=")) {
					String[] strings = line.trim().split(" ");
					String[] seedStrs = strings[1].split("=");
					appendDeviceInfo(monkeyWorkDir + "\\" + DEVICE_INFO_FILE_NAME, "MonkeySeed=" + seedStrs[1]);
				} else if (line.trim().startsWith("java.lang.")) {
					errorMessageStr += (line + "\r\n");
					boolean keepTest = true;
					String newLine;
					int lineCounter = 0;
					while(keepTest && programRunning==true) {
						newLine = inputStream.readLine();
						lineCounter ++;
						if (newLine != null && !newLine.matches("\\s*")) {
							if (newLine.trim().startsWith("at") && lineCounter<50) {
								errorMessageStr += (line + "\r\n");
							} else {
								keepTest = false;
							}
						}
					}
				}
			}
			endTestTime = System.currentTimeMillis();//测试结束时间点距离1970年1月1日0点0分0秒的毫秒数
			String runTimeStr = MyUtils.millisToTimeMinuteCn(endTestTime - startTestTime);
			display.asyncExec(new Runnable() {
				public void run() {
					RecordUtils.updateOneRecordInFile(index, RecordUtils.RUNNING_TIME_TYPE, runTimeStr);
					RecordUtils.updateTableUi(tableMonkeyRun);
				}
			});
			timerThread.stop = true;
			timerThread.interrupt();
			
			final String message = errorMessageStr;
			final boolean testPass = isMonkeyPass;
			final int crashs = crashCounter;
			final int anrs = anrCounter;
			display.asyncExec(new Runnable(){
				public void run() {
					if (testPass && crashs == 0 && anrs == 0) {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.PASS_STATUS);
					} else {
						RecordUtils.updateOneRecordInFile(index, RecordUtils.TEST_STATUS_TYPE, RecordUtils.FINISHED_STATUS);
					}
					RecordUtils.updateOneRecordInFile(index, RecordUtils.ERROR_MESSAGE, message);
					RecordUtils.updateTableUi(tableMonkeyRun);
					
					final String messageError = RecordUtils.readOneRecordInFile(index, RecordUtils.ERROR_MESSAGE);
					if (messageError!=null && !message.equals("")) {
						textTrace.setText(messageError);
					} else {
						textTrace.setText("");
					}
				}
			});
			inputStream.close();
		} catch (IOException e) {
			showError("分析现有monkey日志失败！", e);
		}
	}
	
	/**
	 * 拼凑monkey命令
	 * @param deviceIdStr
	 * @return
	 */
	public String getMonkeyCmd(String deviceIdStr, String packageName) {
		String monkeyCmdStr = "adb -s " + deviceIdStr + " shell monkey ";
		//包名
		if (packageName != null && !packageName.equals("")) {
			monkeyCmdStr += ("-p " + packageName + " ");
		}
		String configStr = PropUtil.getMonkeyConfigString(deviceIdStr, null);
		monkeyCmdStr += configStr;
		
		return monkeyCmdStr;
	}
	
	/**
	 * 拼凑monkey命令
	 * @param deviceIdStr 设备ID
	 * @param packageName 应用包名
	 * @param seedStr 随机种子值
	 * @return
	 */
	public String getMonkeyCmd(String deviceIdStr, String packageName, String seedStr) {
		String monkeyCmdStr = "adb -s " + deviceIdStr + " shell monkey ";
		//包名
		if (packageName != null && !packageName.equals("")) {
			monkeyCmdStr += ("-p " + packageName + " ");
		}
		String configStr = PropUtil.getMonkeyConfigString(deviceIdStr, seedStr);
		monkeyCmdStr += configStr;
		
		return monkeyCmdStr;
	}
	
	
	/**
	 * 拼凑monkey白名单测试命令
	 * @param deviceIdStr
	 * @return
	 */
	public String getMonkeyWhiteTestCmd(String deviceIdStr) {
		String monkeyCmdStr = "adb -s " + deviceIdStr + " shell monkey --pkg-whitelist-file /data/local/tmp/whitelist.txt ";
		String configStr = PropUtil.getMonkeyConfigString(deviceIdStr, null);
		monkeyCmdStr += configStr;
		return monkeyCmdStr;
	}
	
	/**
	 * 拼凑monkey黑名单测试命令
	 * @param deviceIdStr
	 * @return
	 */
	public String getMonkeyBlackTestCmd(String deviceIdStr) {
		String monkeyCmdStr = "adb -s " + deviceIdStr + " shell monkey --pkg-blacklist-file /data/local/tmp/blacklist.txt ";
		String configStr = PropUtil.getMonkeyConfigString(deviceIdStr, null);
		monkeyCmdStr += configStr;
		return monkeyCmdStr;
	}
	
	/**
	 * 安装apk
	 * @param deviceId 设备ID
	 * @param pcPath 待安装文件在PC上的路径
	 * @return
	 */
	public boolean adbInstallApk(String deviceId, String pcPath) {
		boolean isSuccess = true;
		String adbCmdStr = "adb -s " + deviceId + " install \"" + pcPath + "\"";
		try {
			Process p = Runtime.getRuntime().exec(adbCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
			try {
				String line;
				while((line = inputStream.readLine()) != null) {
					if (line.contains("Failure")) {
						isSuccess = false;
					}
					MyUtils.printWithTimeMill("adbInstallApk:" + line);
				}
				while((line = errorReader.readLine()) != null) {
					MyUtils.printWithTimeMill("adbInstallApk errorReader:" + line);
				}
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					showError("安装apk失败！", e);
				}
				inputStream.close();
			} catch (IOException e) {
				showError("安装apk失败！", e);
			}
		} catch (Exception e1) {
			showError("执行安装apk失败！", e1);
		}
		return isSuccess;
	}
	
	/**
	 * 卸载apk
	 * @param deviceId 设备ID
	 * @param packageName 待卸载的应用包名 
	 * @return
	 */
	public boolean adbUninstallApk(String deviceId, String packageName) {
		boolean isSuccess = false;
		
		String adbCmdStr = "adb -s " + deviceId + " uninstall " + packageName;
		try {
			Process p = Runtime.getRuntime().exec(adbCmdStr);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
			try {
				String line;
				while((line = inputStream.readLine()) != null) {
					if (line.contains("Success")) {
						isSuccess = true;
					}
					MyUtils.printWithTimeMill("adbUninstallApk:" + line);
				}
				while((line = errorReader.readLine()) != null) {
					MyUtils.printWithTimeMill("adbUninstallApk errorReader:" + line);
				}
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					showError("卸载apk失败！", e);
				}
				inputStream.close();
			} catch (IOException e) {
				showError("卸载apk失败！", e);
			}
		} catch (Exception e1) {
			showError("执行卸载apk失败！", e1);
		}
		return isSuccess;
	}
	
    /**
     * 通过线程组获得线程
     *
     * @param threadId
     * @return
     */
    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }
    
    public static void getSettings() {
    	File propFile = new File(settingsPropPath);
    	if (propFile.exists()) {
    		endGetLogs = PropUtil.getValueOfProp("endGetLogs", settingsPropPath);
    		if (endGetLogs != null && !endGetLogs.equals("")) {
    			
    		} else {
    			PropUtil.setProperties(settingsPropPath, "endGetLogs", "false", true);
    			endGetLogs = "false";
    		}
    		sendEmail = PropUtil.getValueOfProp("sendEmail", settingsPropPath);
    		if (sendEmail != null && !sendEmail.equals("")) {
    			
    		} else {
    			PropUtil.setProperties(settingsPropPath, "sendEmail", "false", true);
    			sendEmail = "false";
    		}
    		endTakePic = PropUtil.getValueOfProp("endTakePic", settingsPropPath);
    		if (endTakePic != null && !endTakePic.equals("")) {
    			//是否一致播放
    		} else {
    			PropUtil.setProperties(settingsPropPath, "endTakePic", "false", true);
    			endTakePic = "false";
    		}
    		getLogCmdFilePath = PropUtil.getValueOfProp("getLogCmdFilePath", settingsPropPath);
    		if (getLogCmdFilePath != null && !getLogCmdFilePath.equals("")) {
    			
    		} else {
    			PropUtil.setProperties(settingsPropPath, "getLogCmdFilePath", System.getProperty("user.dir") + "\\getLogCmds.txt", true);
    			getLogCmdFilePath = System.getProperty("user.dir") + "\\getLogCmds.txt";
    		}
		}
    }
    
    /**
     * 从文件执行命令
     * @param filePath 文件路径
     * @param toPath 替换路径
     */
    public static void excuteCmdFromFile(String filePath, String toPath) {
    	MyUtils.printWithTimeMill(filePath);
    	File fromFile = new File(filePath);
    	try {
			fromFile.createNewFile();
		} catch (IOException e2) {
			MyUtils.printWithTimeMill("excuteCmdFromFile:" + e2.toString());
		}
    	if (fromFile.exists()) {
    		BufferedReader bufferedReader;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fromFile), "UTF-8"));
				try {
					String line = "";
					while((line = bufferedReader.readLine()) != null){
						if (!line.trim().equals("")) {
							if (line.contains("%auto%")) {
								excuteWinCmd(line.replace("%auto%", toPath));
							} else {
								excuteWinCmd(line);
							}
						}
					}
				} catch (IOException e) {
					MyUtils.printWithTimeMill("excuteCmdFromFile:" + e.toString());
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						MyUtils.printWithTimeMill(e.toString());
					}
				}
			} catch (UnsupportedEncodingException | FileNotFoundException e1) {
				MyUtils.printWithTimeMill("excuteCmdFromFile:" + e1.toString());
			}
			MyUtils.printWithTimeMill("end excuteCmdFromFile");
		}
    }
    
	/**
	 * 执行cmd命令
	 * @param cmdStr 命令
	 * @return 返回值
	 */
	public static int excuteWinCmd(String cmdStr) {
		int result = -1;
		try {
			MyUtils.printWithTimeMill("excuteWinCmd:" + cmdStr);
			final Process process = Runtime.getRuntime().exec(cmdStr);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String line = "";
					BufferedReader adbCmdInputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
					try {
						while((line = adbCmdInputStream.readLine()) != null) {
							MyUtils.printWithTimeMill(line);
						}
					} catch (IOException e) {
						MyUtils.printWithTimeMill(e.toString());
					} finally {
						try {
							adbCmdInputStream.close();
						} catch (IOException e) {
							MyUtils.printWithTimeMill(e.toString());
						}
					}
					
				}
			}).start();
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String line = "";
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					try {
						while((line = errorReader.readLine()) != null) {
							MyUtils.printWithTimeMill(line);
						}
					} catch (IOException e) {
						MyUtils.printWithTimeMill(e.toString());
					} finally {
						try {
							errorReader.close();
						} catch (IOException e) {
							MyUtils.printWithTimeMill(e.toString());
						}
					}
					
				}
			}).start();
			result = process.waitFor();
		} catch (IOException | InterruptedException e) {
			MyUtils.printWithTimeMill(e.toString());
		}
		MyUtils.printWithTimeMill("excuteWinCmd result = " + result);
		return result;
	}
	
}
