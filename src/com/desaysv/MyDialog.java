package com.desaysv;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class MyDialog extends Dialog {
	
	private String textValue;// 用来保存Text值的变量
	private Text text;		 // 将文本写为类实例变量，否则其他方法无法访问它
	private String lblStr;		 // 将文本写为类实例变量，否则其他方法无法访问它

	protected MyDialog(Shell parentShell) {
		super(parentShell);
		this.lblStr = "保存log路径：";
	}
	
	protected MyDialog(Shell parentShell, String lableStr) {
		super(parentShell);
		this.lblStr = lableStr;
	}
	
	public String getTextValue() {
		return this.textValue;
	}
	
	public void setTextValue(String valueStr) {
		this.textValue = valueStr;
	}
	
	// 在这个方法里构建Dialog中的界面内容
	protected Control createDialogArea(Composite parent) {
		Composite topComp = new Composite(parent, SWT.None);
		RowLayout layout = new RowLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		
		topComp.setLayout(layout);
		new Label(topComp, SWT.NONE).setText(lblStr);
		text = new Text(topComp, SWT.BORDER);
		// 把textValue设给Text作为初值，这时要注意对textValue作空值判断，给文本框设置空值是会出错的
		text.setText(textValue == null ? "" : textValue);
		text.setLayoutData(new RowData(300, -1));
		
		return topComp;
	}
	
	// 单击对话框底部按钮会执行此方法，参数buttonId是被单击按钮的ID值。
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID)// 如果单击确定按钮，则将值保存到变量
		{
			textValue = text.getText();
		}
		super.buttonPressed(buttonId);
	}

}
