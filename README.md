# XAndroidSocket
Socket封装，支持TCP/UDP客户端和服务端，支持自定义粘包处理、验证处理、解析处理。
##使用
详见目录[TCP/UDP使用demo](https://github.com/Blankeer/XAndroidSocket/tree/master/app/src/main/java/com/blanke/xandroidsocket)
下的例子，使用简单。
其中只有TCP才支持支持粘包处理、验证处理、解析处理。

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.Blankeer:XAndroidSocket:1.0.0'
	}

##粘包处理
提供的粘包处理有
- [不处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/stickpackage/BaseStickPackageHelper.java)(默认)
- [首尾特殊字符处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/stickpackage/SpecifiedStickPackageHelper.java)
- [固定长度处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/stickpackage/StaticLenStickPackageHelper.java)
- [动态长度处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/stickpackage/VariableLenStickPackageHelper.java)

支持自定义粘包处理，只需要实现[AbsStickPackageHelper](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/stickpackage/AbsStickPackageHelper.java)接口，
```java
/**
 * 接受消息，粘包处理的helper，通过inputstream，返回最终的数据，需手动处理粘包，返回的byte[]是我们预期的完整数据
 * note:这个方法会反复调用，直到解析到一条完整的数据。该方法是同步的，尽量不要做耗时操作，否则会阻塞读取数据
 */
public interface AbsStickPackageHelper {
    byte[] execute(InputStream is);
}
```
把接收消息的InputStream给你，你返回一个完整包的byte[]给我。
##验证处理
提供的验证处理是 [不处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/validation/BaseValidationHelper.java)，也是默认的。
自定义验证处理需要实现`AbsValidationHelper`:
```java
public interface AbsValidationHelper {
    boolean execute(byte[] msg);
}
```
把完整的数据包给你，你需要返回是否验证通过，一般的自定义协议里都会有MD5验证，可以在这里验证。
##解析处理
提供的解析处理是 [不处理](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/decode/BaseDecodeHelper.java),也是默认的。
自定义解析处理需要实现[AbsDecodeHelper](https://github.com/Blankeer/XAndroidSocket/blob/master/lib/src/main/java/com/blanke/xsocket/tcp/client/helper/decode/AbsDecodeHelper.java)
```java
/**
 * 解析消息的处理
 */
public interface AbsDecodeHelper {
    /**
     *
     * @param data  完整的数据包
     * @param targetInfo    对方的信息(ip/port)
     * @param tcpConnConfig    tcp连接配置，可自定义
     * @return
     */
    byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig);
}
```
设计思路：一般自定义协议会设计好多个字段组成，比如：`dataLen+data+type+md5`，数据长度+数据+类型+MD5，解析处理就是把这4个字段解析出来，返回byte[4][]，便于后续处理。
## Log
日志开关：`XSocketLog.debug(true)`

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
