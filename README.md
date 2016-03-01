[![Build Status](https://travis-ci.org/sinofool/alipay-java-sdk.png)](https://travis-ci.org/sinofool/alipay-java-sdk)
[![Coverage Status](https://coveralls.io/repos/sinofool/alipay-java-sdk/badge.svg?branch=master&service=github)](https://coveralls.io/github/sinofool/alipay-java-sdk?branch=master)

支付宝Java SDK
===============
官方的Demo代码效率不够高，所以自己写了一个。欢迎Fork。
使用这个库的例子源代码在demo/目录下，测试在这里：https://oneskill.com/alipay-java-sdk-demo/

V1.0
----
包含基本的即时到账交易，PC和WAP平台。


Demo的使用方法
===============
代码里面的Demo部分是一个示例，并没有完整的事物检测，仅用来展示本SDK与支付宝接口交互。
使用前提
--------
一个外网域名
一个可以配置的支付宝企业账号

账号配置
--------
需要到支付宝企业账号内配置公钥，保留好公钥留作下一步使用。

代码配置
--------
修改Demo里面两个Config类，把上面账号的各种信息填全，每个字段都需要修改。
修改PCHandler里面关于callback URL的域名，改成自己的外网域名。

测试
----
Demo包含一个Servlet类，用标准容器都可以启动，例如：
```
java -cp jetty-runner.jar org.eclipse.jetty.runner.Runner alipay-java-sdk-demo-1.0-SNAPSHOT.war
```
