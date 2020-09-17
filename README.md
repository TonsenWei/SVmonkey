# SVmonkey
A Tool use for running android monkey test on PC.

# 一、简介
Android系统自带的monkey测试，一般用于测试Android系统上各个app的稳定性，对各个界面发送随机事件，检测是否会产生CRASH或ANR等异常。
详情可访问谷歌官网了解: [UI/Application Exerciser Monkey](https://developer.android.com/studio/test/monkey?hl=zh-cn)

目前测试人员进行monkey测试一般在终端输入一长串指令，或者把指令写成一个脚本，指令有改变需要打开脚本修改脚本参数再执行，有些麻烦和低效。
该工具简化Monkey执行步骤，简化参数配置。

![](project-files/image/MonkeyHome.png)

测试过程监控ANR和CRASH以及其他EXCEPTION。

![](project-files/image/MonkeyAreas.png)

实时保存logcat信息。

测试结束可以导出Excel格式的测试报告，统计发生ANR和CRASH数量以及发生的包名。

# 二、使用

1、java语言编写，依赖java环境，运行环境为windows

2、依赖adb，应已把adb配置为环境变量

3、添加依赖的jar包

![](project-files/image/MonkeyJars.png)

4、打包成jar后使用java命令运行

    java -jar SVmonkey.jar

5、也可以把jar打包成exe后双击运行
