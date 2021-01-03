package com.desaysv;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.desay.utils.PropUtil;

public class Settings {

	private List list;

	private Composite composite;

	private StackLayout stacklayout;

	private Composite comp1;

	private Composite comp2;

	private Composite comp3;

	private Composite comp4;

	private Composite comp5;
	
	private String settingsPropFilePath = "";//配置文件路径
	
	private String endGetLogs = "";
	private String sendEmail = "";
	private String endTakePic = "";
	private String getLogCmdFilePath = System.getProperty("user.dir") + "\\getLogCmds.txt";
	
	//PropUtil.setProperties(propFilePath, "saveOutputStutes", "on", true);

	public Settings(String propPath) {
		settingsPropFilePath = propPath;
		if (settingsPropFilePath != null && !settingsPropFilePath.equals("")) {
			//读取之前的设置，如果为空则设置为默认
			endGetLogs = PropUtil.getValueOfProp("endGetLogs", settingsPropFilePath);
			if (endGetLogs != null && !endGetLogs.equals("")) {
			} else {
				PropUtil.setProperties(settingsPropFilePath, "endGetLogs", "false", true);
				endGetLogs = "false";
			}
			sendEmail = PropUtil.getValueOfProp("sendEmail", settingsPropFilePath);
			if (sendEmail != null && !sendEmail.equals("")) {
			} else {
				PropUtil.setProperties(settingsPropFilePath, "sendEmail", "false", true);
				sendEmail = "false";
			}
			endTakePic = PropUtil.getValueOfProp("endTakePic", settingsPropFilePath);
			if (endTakePic != null && !endTakePic.equals("")) {
				
			} else {
				PropUtil.setProperties(settingsPropFilePath, "endTakePic", "false", true);
				endTakePic = "false";
			}
			
			getLogCmdFilePath = PropUtil.getValueOfProp("getLogCmdFilePath", settingsPropFilePath);
			if (getLogCmdFilePath != null && !getLogCmdFilePath.equals("")) {
				
			} else {
				String defaultPathStr = System.getProperty("user.dir") + "\\getLogCmds.txt";
				PropUtil.setProperties(settingsPropFilePath, "getLogCmdFilePath", defaultPathStr, true);
				getLogCmdFilePath = defaultPathStr;
			}
		}
		
		final Display display = Display.getDefault();
		final Shell shell = new Shell(new Shell(display.getActiveShell(), SWT.APPLICATION_MODAL));
		shell.setLocation(600, 200);
		shell.setSize(500, 460);
		shell.setText("设置");
		// 设置容器为7列
		shell.setLayout(new GridLayout(7, false));
		// 在Shell上定义List，并对List进行布局
		{
			list = new List(shell, SWT.BORDER);
			// 设置列表项
			list.setItems(new String[] { "常规"});
//			list.setItems(new String[] { "通知", "音频", "被拒", "常规通知", "链接" });
			// 使List垂直充满
			GridData gridList = new GridData(GridData.FILL_VERTICAL);
			gridList.horizontalSpan = 3;//水平强占3列
			gridList.widthHint = 100;   //使宽度为100个像素
			// 设置List距离shell容器左边框为5个像素
			gridList.horizontalIndent = 5;
			list.setLayoutData(gridList);
		}
		// 在Shell上定义Composite，并对Composite进行布局
		{
			composite = new Composite(shell, SWT.BORDER);
			// 使composite面板双向充满
			GridData gridComposite = new GridData(GridData.FILL_BOTH);
			gridComposite.horizontalSpan = 3;//水平强占3列
			composite.setLayoutData(gridComposite);
			// 定义一个堆栈布局
			stacklayout = new StackLayout();
			// 在composite面板上应用堆栈布局
			composite.setLayout(stacklayout);
			// 调用自定义方法
			comp1Content();
//			comp2Content();
//			comp3Content();
//			comp4Content();
//			comp5Content();

		}
		// 在Shell上定义单个按钮，并对其进行布局
		{
			final Button helpButton = new Button(shell, SWT.NONE);
			helpButton.setText("帮助");
			GridData gridHelp = new GridData();
			// 使help按钮水平抢占3列
			gridHelp.horizontalSpan = 3;
			// 使宽度为90个像素
			gridHelp.widthHint = 90;
			// 设置帮助按钮距离shell容器左边框为5个像素
			gridHelp.horizontalIndent = 5;
			helpButton.setLayoutData(gridHelp);

			final Button okButton = new Button(shell, SWT.NONE);
			okButton.setText("确定");
			GridData gridOk = new GridData();
			gridOk.horizontalIndent = 180;
			gridOk.horizontalSpan = 2;
			gridOk.widthHint = 90;
			okButton.setLayoutData(gridOk);
			okButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (endGetLogs != null && !endGetLogs.equals("")) {
						//失败播放通知
						PropUtil.setProperties(settingsPropFilePath, "endGetLogs", endGetLogs, true);
					}
					if (sendEmail != null && !sendEmail.equals("")) {
						//失败播放通知
						PropUtil.setProperties(settingsPropFilePath, "sendEmail", sendEmail, true);
					}
					if (endTakePic != null && !endTakePic.equals("")) {
						//是否一致播放
						PropUtil.setProperties(settingsPropFilePath, "endTakePic", endTakePic, true);
					}
					if (getLogCmdFilePath != null && !getLogCmdFilePath.equals("")) {
						//是否一致播放
						PropUtil.setProperties(settingsPropFilePath, "getLogCmdFilePath", getLogCmdFilePath, true);
					}
					shell.dispose();
				}
			});

			final Button cancelButton = new Button(shell, SWT.NONE);
			cancelButton.setText("取消");
			GridData gridCancel = new GridData();
			gridCancel.horizontalSpan = 2;
			gridCancel.widthHint = 90;
			cancelButton.setLayoutData(gridCancel);
			cancelButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
				}
			});
		 
		}
		// 将comp1面板设置为最设置为堆栈的最顶端显示
		stacklayout.topControl = comp1;

		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = list.getSelectionIndex();
				if (selectionIndex == 0) {
					stacklayout.topControl = comp1;
				} else if (selectionIndex == 1) {
					stacklayout.topControl = comp2;
				} else if (selectionIndex == 2) {
					stacklayout.topControl = comp3;
				} else if (selectionIndex == 3)
					stacklayout.topControl = comp4;
				else if (selectionIndex == 4)
					stacklayout.topControl = comp5;
				composite.layout();
			}
		});
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	// 通知面板的布局设置
	private void comp1Content() {
		// 在composite面板上定义comp1面板
		comp1 = new Composite(composite, SWT.BORDER);
		// 在comp1上采用GridLayout布局，将comp1容器设置5列
		comp1.setLayout(new GridLayout(5, false));
		// 定义常规标签，并对其进行布局
		{
			final Label labRoutine = new Label(comp1, SWT.NONE);
			labRoutine.setText("常规");
			// 用GridData对labRoutine进行布局
			GridData gridRoutine = new GridData();
			// 水平抢占2列
			gridRoutine.horizontalSpan = 2;
			// 距离comp1顶端10个像素
			gridRoutine.verticalIndent = 10;
			labRoutine.setLayoutData(gridRoutine);
		}
		// 定义labSeparator分隔符标签，并对其进行布局
		{
			final Label labSeparator = new Label(comp1, SWT.SEPARATOR
					| SWT.HORIZONTAL);
			GridData gridSeparator = new GridData(GridData.FILL_HORIZONTAL);
			gridSeparator.horizontalSpan = 5;
			// 垂直抢占5列
			gridSeparator.verticalSpan = 5;
			labSeparator.setLayoutData(gridSeparator);
		}
		// 定义button1、button2复选框按钮，并对其进行布局
		{
			/**
			 * 因为button1、button2在comp1上的布局相同 
			 * 所以采用下面的同一布局方式设置相同的属性参数进行布局
			 */
			GridData gridButton = new GridData(GridData.FILL_HORIZONTAL);
			// 水平抢占5列
			gridButton.horizontalSpan = 5;
			// 垂直抢占5列
			gridButton.verticalSpan = 5;
			// 距离comp1左边框20个像素
			gridButton.horizontalIndent = 20;
			final Button button1 = new Button(comp1, SWT.CHECK);
			button1.setText("测试结束截图(&S)");
			// 对button1进行布局
			button1.setLayoutData(gridButton);
			if (endTakePic.equals("true")) {
				button1.setSelection(true);
			} else {
				button1.setSelection(false);
			}
			button1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (button1.getSelection()) {
						endTakePic = "true";
					} else {
						endTakePic = "false";
					}
				}
			});
			// 定义button2复选框按钮，并对其进行布局
			final Button button2 = new Button(comp1, SWT.CHECK);
			button2.setText("测试结束抓log(&U)");
			if (endGetLogs.equals("true")) {
				button2.setSelection(true);
			} else {
				button2.setSelection(false);
			}
			button2.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (button2.getSelection()) {
						endGetLogs = "true";
					} else {
						endGetLogs = "false";
					}
				}
			});
			/**
			 * 由于button1和button2布局完全相同所以可以通过
			 * button2.setLayoutData(gridDataC)来设置button2 的布局
			 */
			button2.setLayoutData(gridButton);
		}
		// 定义Group分组框，并对其进行布局
		{
			final Group group = new Group(comp1, SWT.NONE);
			group.setText("抓log命令文件路径：");
			GridData gridGroup = new GridData(GridData.FILL_HORIZONTAL);
	
			gridGroup.horizontalSpan = 3;
			gridGroup.verticalSpan = 5;
			gridGroup.horizontalIndent = 20;
			group.setLayoutData(gridGroup);
			// 设置group分组框上组件,并对组件进行布局
			{
				group.setLayout(new GridLayout());
				/**
				 * 四个复选按钮在group分组框上布局完全相同，
				 * 故此可用下面同一种布局方式设置相同的属性参数进行布局
				 */
				GridData gridDataButton = new GridData(GridData.FILL_HORIZONTAL);
				gridDataButton.verticalSpan = 6;
				gridDataButton.horizontalIndent = 6;
				final org.eclipse.swt.widgets.Text cmdsPath = new org.eclipse.swt.widgets.Text(group, SWT.NONE);
				cmdsPath.setLayoutData(gridDataButton);
				cmdsPath.setText(getLogCmdFilePath);
				final Button buttonOne = new Button(group, SWT.NONE);
				buttonOne.setText("选择文件");
//				buttonOne.setLayoutData(gridDataButton);
				buttonOne.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						String filePath = WinUtil.fileSeleteDialog(Display.getDefault().getActiveShell(), System.getProperty("user.dir"));
						System.out.println(filePath);
						if (!filePath.equals("")) {
							cmdsPath.setText(filePath);
							getLogCmdFilePath = filePath;
						}
						//TODO:
					}
				});
				final Button buttonTwo = new Button(group, SWT.NONE);
				buttonTwo.setText("跳转到该目录");
				buttonTwo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						File logCmdFile = new File(getLogCmdFilePath);
						WinUtil.openDir(logCmdFile.getParent());
					}
				});
			}
		}

		{
			GridData gridButtonEmail = new GridData(GridData.FILL_HORIZONTAL);
			// 水平抢占5列
			gridButtonEmail.horizontalSpan = 5;
			// 垂直抢占5列
			gridButtonEmail.verticalSpan = 5;
			// 距离comp1左边框20个像素
			gridButtonEmail.horizontalIndent = 20;
			// 定义button2复选框按钮，并对其进行布局
			final Button btnSendEmail = new Button(comp1, SWT.CHECK);
			btnSendEmail.setText("测试结束发送邮件");
			if (sendEmail.equals("true")) {
				btnSendEmail.setSelection(true);
			} else {
				btnSendEmail.setSelection(false);
			}
			btnSendEmail.setLayoutData(gridButtonEmail);
			btnSendEmail.setEnabled(false);
			btnSendEmail.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (btnSendEmail.getSelection()) {
						sendEmail = "true";
					} else {
						sendEmail = "false";
					}
				}
			});
			final Group groupEmailSettings = new Group(comp1, SWT.NONE);
			groupEmailSettings.setText("邮件设置");
			GridData gridGroupEmail = new GridData(GridData.FILL_HORIZONTAL);
	
			gridGroupEmail.horizontalSpan = 3;
			gridGroupEmail.verticalSpan = 5;
			gridGroupEmail.horizontalIndent = 20;
			groupEmailSettings.setLayoutData(gridGroupEmail);
		}
		
	}

	// 音频面板的布局设置
	private void comp2Content() {
		comp2 = new Composite(composite, SWT.BORDER);
		comp2.setLayout(new GridLayout());
		// 定义音频标签，并对其进行布局
		{
			final Label labelAudio = new Label(comp2, SWT.NONE);
			labelAudio.setText("音频");
			GridData gridAudio = new GridData();
			gridAudio.verticalIndent = 10;
			labelAudio.setLayoutData(gridAudio);
		}
		// 设置comp2面板上的分隔符标签
		{
			final Label labelSeparator = new Label(comp2, SWT.SEPARATOR
					| SWT.HORIZONTAL);

			GridData gridSeparator = new GridData(GridData.FILL_HORIZONTAL);
			gridSeparator.verticalSpan = 5;
			labelSeparator.setLayoutData(gridSeparator);
		}
		// 在comp2上定义 groupOne 分组框，并对其进行布局
		{
			Group groupOne = new Group(comp2, SWT.NONE);
			groupOne.setText("输入—麦克风或耳麦");
			GridData gridGroupOne = new GridData(GridData.FILL_HORIZONTAL);
			gridGroupOne.horizontalIndent = 20;
			gridGroupOne.verticalSpan = 20;
			groupOne.setLayoutData(gridGroupOne);
			{ // 设置groupa面板上组件的布局
				groupOne.setLayout(new GridLayout());
				/**
				 * comb1和check1复选框按钮可用相同的布局方式和相同的属性值
				 */
				GridData grid = new GridData();
				grid.verticalIndent = 5;
				grid.horizontalIndent = 10;
				final Combo combo1 = new Combo(groupOne, SWT.NONE);
				// 在下拉框中设置下拉项
				combo1.setItems(new String[] { "默认设备", "Realtek AC97 Audio" });
				combo1.setLayoutData(grid);

				final Button check1 = new Button(groupOne, SWT.CHECK);
				check1.setText("自动调整麦克风灵敏度(&A)");
				check1.setLayoutData(grid);

			}
		}
		// 在comp2上定义groupTwo分组框，并对其进行布局
		{
			Group groupTwo = new Group(comp2, SWT.NONE);
			groupTwo.setText("输入—扬声器或耳麦");

			GridData gridGroupTwo = new GridData(GridData.FILL_HORIZONTAL);
			gridGroupTwo.horizontalIndent = 20;
			gridGroupTwo.verticalSpan = 20;
			groupTwo.setLayoutData(gridGroupTwo);

			{// 设置groupTwo面板上组件，并对组件进行布局
				groupTwo.setLayout(new GridLayout());
				GridData gridData = new GridData();
				gridData.horizontalIndent = 10;
				gridData.widthHint = 138;
				gridData.verticalSpan = 5;
				final Label lab1 = new Label(groupTwo, SWT.NONE);
				lab1.setText("通知—铃声(&N)");
				lab1.setLayoutData(gridData);
				final Combo combo2 = new Combo(groupTwo, SWT.NONE);
				combo2.setItems(new String[] { "所有设备", "默认设备",
						"Realtek AC97 Audio" });
				combo2.setLayoutData(gridData);

				final Label lab2 = new Label(groupTwo, SWT.NONE);
				lab2.setText("呼叫(&C)");
				lab2.setLayoutData(gridData);

				final Combo combo3 = new Combo(groupTwo, SWT.NONE);
				combo3.setItems(new String[] { "默认设备", "Realtek AC97 Audio" });

				combo3.setLayoutData(gridData);

				final Button cancelButton = new Button(groupTwo, SWT.CHECK);
				cancelButton.setText("响铃时取消扬声器静音(&S)");
				GridData gridDataCancel = new GridData();
				gridDataCancel.verticalSpan = 6;
				cancelButton.setLayoutData(gridDataCancel);

			}
		}
		// 在comp2上设置callButton按钮，并对其进行布局
		{
			final Button callButton = new Button(comp2, SWT.CHECK);
			callButton.setText("呼叫时取消扬声器和麦克风静音(&U)");
			GridData gridDataCall = new GridData();
			gridDataCall.horizontalIndent = 20;
			gridDataCall.verticalSpan = 6;
			callButton.setLayoutData(gridDataCall);
		}

	}

	private void comp3Content() {
		comp3 = new Composite(composite, SWT.BORDER);
		comp3.setLayout(new GridLayout());
		final Label labelRefuse = new Label(comp3, SWT.NONE);
		labelRefuse.setText("被拒");

	}

	private void comp4Content() {
		comp4 = new Composite(composite, SWT.BORDER);
		comp4.setLayout(new GridLayout());
		final Label labelNotice = new Label(comp4, SWT.NONE);
		labelNotice.setText("通知");
	}

	private void comp5Content() {
		comp5 = new Composite(composite, SWT.BORDER);
		comp5.setLayout(new GridLayout());
		final Label labelLink = new Label(comp5, SWT.NONE);
		labelLink.setText("链接");

	}

}
