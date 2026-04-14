package com.lms.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ZtsAuthUtil {

    public static String APXSsid;            //SSID
    public static String ZtsMAddress;        //控制器IP
    public static int    ztsMSPAPort;        //控制器SPA端口
    public static int    ztsMSSLPort;        //控制器SSL接入端口
    public static String ZtsAPXAddress;      //网关IP
    public static int    ztsAPXSPAPort;      //网关SPA端口
    public static int    ztsAPXSSLPort;      //网关SSL接入端口
    public static boolean ztsMUdpRecv;       //是否接收UDP回包

    private static Map<String, String> sessionMap = new HashMap();  //用户名会话ID映射
    private static Map<String, String> vipMap = new HashMap();  //用户名虚拟IP映射
    //扩展函数，不足16字节倍数，尾部补0直到成为16字节倍数
    public static byte[] Expand(byte[] str)
    {
        byte[] ret = new byte[(str.length + 15) & ~0xf];
        System.arraycopy(str, 0, ret, 0, str.length);
        return ret;
    }

    //加密
    public static String encrypt(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"
        byte[] cKey = {'g','j','4','3','6','7','i','0',')','#','l','2','3','d','8','v'};
        IvParameterSpec iv = new IvParameterSpec(cKey);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        return new String(Base64.getEncoder().encode(cipher.doFinal(Expand(sSrc.getBytes()))));//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String decrypt(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        byte[] cKey = {'g','j','4','3','6','7','i','0',')','#','l','2','3','d','8','v'};
        IvParameterSpec iv = new IvParameterSpec(cKey);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc.getBytes());//先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString.replace("\0","");
    }


    //返回接收报文map
    public static Map<String, String> analysisMessageMap(String message,Integer containRandom) {
        message = message.trim();
        Map<String, String> map = new HashMap();
        try {
            if (containRandom == 1) {
                message = message.substring(6);
            }

            while (6 < message.length()) {
                String key = message.substring(0, 3);
                int len;

                // 特殊处理640：直接读取剩余全部内容
                if ("640".equals(key) || "641".equals(key)) {
                    try {
                        // 计算剩余内容的字节长度（GBK编码）
                        len = getStringByteLenths(message.substring(6));
                    } catch (UnsupportedEncodingException e) {
                        len = message.substring(6).getBytes("GBK").length;
                    }
                } else {
                    len = Integer.parseInt(message.substring(3, 6));
                }

                StringBuffer valueBuffer = new StringBuffer();
                message = message.substring(6); // 跳过头部
                String s = substringByte(message, 0, len); // 截取内容

                if (map.containsKey(key)) {
                    valueBuffer.append(map.get(key)).append(s);
                } else {
                    valueBuffer.append(s);
                }
                map.put(key, valueBuffer.toString());
                message = message.substring(s.length()); // 更新剩余消息
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static String substringByte(String orignal, int start, int count) {
        // 如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal)){
            return orignal;
        }

        // 目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();

        try {
            // 截取字节起始字节位置大于目标String的Byte的length则返回空值
            if (start >= getStringByteLenths(orignal)) {
                return null;
            }

            int len = 0;
            char c;

            // 遍历String的每一个Char字符，计算当前总长度
            // 如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {

                c = orignal.charAt(i);

                // 当起始位置为0时候
                if (start == 0) {
                    len += String.valueOf(c).getBytes("GBK").length;
                    if (len <= count) {
                        buff.append(c);
                    }else {
                        break;
                    }
                } else {
                    // 截取字符串从非0位置开始
                    len += String.valueOf(c).getBytes("GBK").length;
                    if (len > start && len <= start + count) {
                        buff.append(c);
                    }
                    if (len > start + count) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回最终截取的字符结果;
        // 创建String对象，传入目标char Buff对象
        return new String(buff);
    }

    public static int getStringByteLenths(String args) throws Exception {
        return args != null && args != "" ? args.getBytes("GBK").length : 0;
    }

    public static void log(String msg) {
       log.info(msg);
    }

    //发送UDP包
    public static Map<String, String> ZTS_UDP_PROC(String SBuf, boolean ztsMUdpRecv, String ip, int port) throws Throwable {
        DatagramSocket socket = new DatagramSocket();
        try{


            //收包一分钟超时
            socket.setSoTimeout(60*1000);

            // 2.创建DatagramPacket，接收数据
            Integer randomId = 100000 + (new Random().nextInt(900000));
            // 测试重放攻击
            // Integer randomId = 100000;

            SBuf = encrypt(randomId + SBuf,"1234567890abcdef");
            DatagramPacket packet = new DatagramPacket(SBuf.getBytes(), 0, SBuf.getBytes().length, InetAddress.getByName(ip), port);
            // 3.发送数据
            socket.send(packet);

            if(ztsMUdpRecv)
            {
                // 接收消息
                byte[] data = new byte[1024];
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                String utf8String = decrypt(msg,"1234567890abcdef");
                Map<String, String> map = analysisMessageMap(utf8String, 1);
                //415属性(错误信息)存在中文，需转码才能正常显示
                socket.close();
                if(null != map.get("415")) map.put("415", new String(map.get("415").getBytes("GBK"), "UTF-8"));
                return map;
            }
            else
            {
                socket.close();
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            log("ZTS_UDP_PROC存在异常");
            socket.close();
            return null;
        }
    }

    public  static String str2Attr(String attr, String str) {
        return attr + String.format("%03d", str.length()) + str;
    }

    public static Map<String, String> SDP_SPA_AUTH(String name, String password, boolean ztsMUdpRecv) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   敲门认证，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("002", password) +                            //*密码
                        //str2Attr("004", "17592471") +                          //用户内网IPv4
                        str2Attr("010", "V3.68.3.20034") +                     //客户端版本号
                        str2Attr("015", "1C:69:7A:5E:31:EF") +                 //MAC地址
                        str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                        str2Attr("401", "222") +                               //交互类型（CS/CG/SG）
                        str2Attr("402", "258328") +                            //客户端认证请求ID
                        str2Attr("603",InetAddress.getLocalHost().getHostAddress() +  name) + //序列号
                        str2Attr("604", "My Loadrunner Virtual PC") +          //手机/计算机名称
                        str2Attr("601", "Windows11") +                         //操作系统信息
                        str2Attr("616", "001") +                               //防火墙状态
                        str2Attr("617", "000") +                               //文件共享状态
                        str2Attr("628", "000") +                               //非法外联接入情况
                        str2Attr("639", "1000400");                            //windows系统版本号累加值

        log("[敲门认证]发送：" + SBuf);
        Map<String, String> map = ZTS_UDP_PROC(SBuf, ztsMUdpRecv, ZtsMAddress, ztsMSPAPort);
        if(ztsMUdpRecv)
            log("[敲门认证]接收：" + map);
        return map;
    }

    public static Map<String, String> SDP_SPA_KNOCK(String name, String password, boolean ztsMUdpRecv) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   敲门认证，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("002", password) +                            //*密码
                        //str2Attr("004", "17592471") +                          //用户内网IPv4
                        str2Attr("010", "V3.68.3.20034") +                     //客户端版本号
                        str2Attr("015", "1C:69:7A:5E:31:EF") +                 //MAC地址
                        str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                        str2Attr("401", "202") +                               //交互类型（CS/CG/SG）
                        str2Attr("402", "258328") +                            //客户端认证请求ID
                        str2Attr("603",InetAddress.getLocalHost().getHostAddress() +  name) + //序列号
                        str2Attr("604", "My Loadrunner Virtual PC") +          //手机/计算机名称
                        str2Attr("601", "Windows11") +                         //操作系统信息
                        str2Attr("616", "001") +                               //防火墙状态
                        str2Attr("617", "000") +                               //文件共享状态
                        str2Attr("628", "000") +                               //非法外联接入情况
                        str2Attr("639", "1000400");                            //windows系统版本号累加值

        log("[仅敲门]发送：" + SBuf);
        Map<String, String> map = ZTS_UDP_PROC(SBuf, ztsMUdpRecv, ZtsMAddress, ztsMSPAPort);
        if(ztsMUdpRecv)
            log("[仅敲门]接收：" + map);
        return map;
    }

    public static void APX_SPA_KNOCK(String name, String token, String SessionID, boolean ztsMUdpRecv) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   网关敲门，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("201", token) +                               //*token
                        str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                        str2Attr("401", "059") +                               //交互类型（CS/CG/SG）
                        str2Attr("404", SessionID) +                           //手机/计算机名称
                        str2Attr("603", InetAddress.getLocalHost().getHostAddress() + name); //序列号

        ZTS_UDP_PROC(SBuf, ztsMUdpRecv, ZtsAPXAddress, ztsAPXSPAPort);
    }

    public static Map<String, String> SDP_UDP_ONLINE(String name, String sessionID, boolean ztsMUdpRecv) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   上线成功，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("401", "025") +                               //交互类型（CS/CG/SG）
                        str2Attr("404", sessionID) +                           //会话ID
                        str2Attr("681", vipMap.get(name)==null?"":vipMap.get(name));                     //客户端虚拟IP

        log("[上线成功]发送：" + SBuf);
        Map<String, String> map = ZTS_UDP_PROC(SBuf, ztsMUdpRecv, ZtsMAddress, ztsMSPAPort);
        if(ztsMUdpRecv)
            log("[上线成功]接收：" + map);
        log("");
        log("");
        log("");
        return map;
    }

    public static Map<String, String> SDP_HTTPS_ASK_UPDATE(String name) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 询问更新，用户名" + name + ":\n");

        try{
            // 拼接报文内容
            String SBuf =
                    str2Attr("001", name) +                                //*账户名
                            //str2Attr("004", "17592471") +                          //用户内网IPv4
                            str2Attr("015", "1C:69:7A:5E:31:EF") +                 //MAC地址
                            //str2Attr("020", "17592471") +                          //与控制器通信IP
                            //str2Attr("031", "") +                                  //所属域
                            str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                            str2Attr("401", "309") +                               //交互类型（CS/CG/SG）
                            str2Attr("601", "windows11") +                         //操作系统信息
                            str2Attr("603", InetAddress.getLocalHost().getHostAddress() + name);//序列号


            log("[询问更新]发送：" + SBuf);
            String url = "https://" + ZtsMAddress + ":" + ztsMSSLPort + "/UMC/zts/ZtsHttpsRequest.action";
            String retStr = sendPostRequest(url, SBuf);

            if(retStr.length() == 0)
                return null;
            Map<String, String> map = analysisMessageMap(retStr, 0);
            if(null != map.get("415")) map.put("415", new String(map.get("415").getBytes("GBK"), "UTF-8"));
            log("[询问更新]接收：" + map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sendGetRequest(String url) {
        return HttpUtil.get(url);
    }

    private static String sendPostRequest(String url, String body) {
        return HttpUtil.post(url, body);
    }


    public static Map<String, String> SDP_HTTPS_SECOND_AUTH(String name) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 多因素认证，用户名" + name + ":\n");
        try{
            // 拼接报文内容
            String SBuf =
                    str2Attr("001", name) +                                //*账户名
                            //str2Attr("004", "17592471") +                          //用户内网IPv4
                            str2Attr("015", "1C:69:7A:5E:31:EF") +                 //MAC地址
                            //str2Attr("020", "17592471") +                          //与控制器通信IP
                            str2Attr("401", "002") +                               //交互类型（CS/CG/SG）'002'：询问多因素认证
                            str2Attr("603",InetAddress.getLocalHost().getHostAddress() +  name);//序列号


            log("[多因素认证]发送：" + SBuf);
            String url = "https://" + ZtsMAddress + ":" + ztsMSSLPort + "/UMC/zts/ZtsHttpsRequest.action";
            String retStr = sendPostRequest(url, SBuf);
            log("[多因素认证]接收retStr：" + retStr);
            Map<String, String> map = analysisMessageMap(retStr, 0);
            log("[多因素认证]接收map：" + map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> SDP_HTTPS_SEND_AUTH_POLICY(String name, String sessionID) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 发送安全策略信息，用户名：" + name + "\n");
        try{
            // 拼接报文内容
            String SBuf =
                    str2Attr("001", name) +                                //*账户名
                            str2Attr("401", "050") +                               //交互类型（CS/CG/SG）
                            str2Attr("622", "System,pdfServer.exe,IntelCpHeciSvc.exe,AggregatorHost.exe,zts_services.exe,aTrustAgent.exe,Memory Compression,aTrustXtunnel.exe,cmd.exe,igfxEM.exe,conhost.exe,fontdrvhost.exe,winlogon.exe,SogouCloud.exe,SogouImeBroker.exe,spoolsv.exe,smss.exe,RtkAudUService64.exe,zts_client_ui.exe,audiodg.exe,convSpeedup.exe,SecurityHealthService.exe,igfxCUIService.exe,dwm.exe,wpscloudsvr.exe,svchost.exe,wps.exe,eaio_agent.exe,services.exe,convServer.exe,WmiPrvSE.exe,SOGOUSmartAssistant.exe,Secure System,eaio_service.exe,notepad++.exe,SearchHost.exe,fsnotifier64.exe,jhi_service.exe,LMS.exe,csrss.exe,SangforPW.exe,rdm.exe,idea64.exe,lsass.exe,rundll32.exe,vmcompute.exe,OfficeClickToRun.exe,SearchProtocolHost.exe,promecefpluginhost.exe,dasHost.exe,OneApp.IGCC.WinService.exe,sihost.exe,SangforPWEx.exe,SangforPromoteService.exe,WUDFHost.exe,PDFEngine.exe,SGTool.exe,chrome.exe") +                               //客户端所有在运行进程
                            str2Attr("623", "System,pdfServer.exe,IntelCpHeciSvc.exe,AggregatorHost.exe,zts_services.exe,aTrustAgent.exe,Memory Compression,aTrustXtunnel.exe,cmd.exe,igfxEM.exe,conhost.exe,fontdrvhost.exe,winlogon.exe,SogouCloud.exe,SogouImeBroker.exe,spoolsv.exe,smss.exe,RtkAudUService64.exe,zts_client_ui.exe,audiodg.exe,convSpeedup.exe,SecurityHealthService.exe,igfxCUIService.exe,dwm.exe,wpscloudsvr.exe,svchost.exe,wps.exe,eaio_agent.exe,services.exe,convServer.exe,WmiPrvSE.exe,SOGOUSmartAssistant.exe,Secure System,eaio_service.exe,notepad++.exe,SearchHost.exe,fsnotifier64.exe,jhi_service.exe,LMS.exe,csrss.exe,SangforPW.exe,rdm.exe,idea64.exe,lsass.exe,rundll32.exe,vmcompute.exe,OfficeClickToRun.exe,SearchProtocolHost.exe,promecefpluginhost.exe,dasHost.exe,OneApp.IGCC.WinService.exe,sihost.exe,SangforPWEx.exe,SangforPromoteService.exe,WUDFHost.exe,PDFEngine.exe,SGTool.exe,chrome.exe") +                               //客户端所有在运行进程
                            str2Attr("624", "System,pdfServer.exe,IntelCpHeciSvc.exe,AggregatorHost.exe,zts_services.exe,aTrustAgent.exe,Memory Compression,aTrustXtunnel.exe,cmd.exe,igfxEM.exe,conhost.exe,fontdrvhost.exe,winlogon.exe,SogouCloud.exe,SogouImeBroker.exe,spoolsv.exe,smss.exe,RtkAudUService64.exe,zts_client_ui.exe,audiodg.exe,convSpeedup.exe,SecurityHealthService.exe,igfxCUIService.exe,dwm.exe,wpscloudsvr.exe,svchost.exe,wps.exe,eaio_agent.exe,services.exe,convServer.exe,WmiPrvSE.exe,SOGOUSmartAssistant.exe,Secure System,eaio_service.exe,notepad++.exe,SearchHost.exe,fsnotifier64.exe,jhi_service.exe,LMS.exe,csrss.exe,SangforPW.exe,rdm.exe,idea64.exe,lsass.exe,rundll32.exe,vmcompute.exe,OfficeClickToRun.exe,SearchProtocolHost.exe,promecefpluginhost.exe,dasHost.exe,OneApp.IGCC.WinService.exe,sihost.exe,SangforPWEx.exe,SangforPromoteService.exe,WUDFHost.exe,PDFEngine.exe,SGTool.exe,chrome.exe") +                               //客户端所有在运行进程
                            str2Attr("625", "System,pdfServer.exe,IntelCpHeciSvc.exe,AggregatorHost.exe,zts_services.exe,aTrustAgent.exe,Memory Compression,aTrustXtunnel.exe,cmd.exe,igfxEM.exe,conhost.exe,fontdrvhost.exe,winlogon.exe,SogouCloud.exe,SogouImeBroker.exe,spoolsv.exe,smss.exe,RtkAudUService64.exe,zts_client_ui.exe,audiodg.exe,convSpeedup.exe,SecurityHealthService.exe,igfxCUIService.exe,dwm.exe,wpscloudsvr.exe,svchost.exe,wps.exe,eaio_agent.exe,services.exe,convServer.exe,WmiPrvSE.exe,SOGOUSmartAssistant.exe,Secure System,eaio_service.exe,notepad++.exe,SearchHost.exe,fsnotifier64.exe,jhi_service.exe,LMS.exe,csrss.exe,SangforPW.exe,rdm.exe,idea64.exe,lsass.exe,rundll32.exe,vmcompute.exe,OfficeClickToRun.exe,SearchProtocolHost.exe,promecefpluginhost.exe,dasHost.exe,OneApp.IGCC.WinService.exe,sihost.exe,SangforPWEx.exe,SangforPromoteService.exe,WUDFHost.exe,PDFEngine.exe,SGTool.exe,chrome.exe") +                               //客户端所有在运行进程
                            str2Attr("404", sessionID);

            log("[策略执行]发送：" + SBuf);
            String url = "https://" + ZtsMAddress + ":" + ztsMSSLPort + "/UMC/zts/ZtsHttpsRequest.action";
            String retStr = sendPostRequest(url, SBuf);
            Map<String, String> map = analysisMessageMap(retStr, 0);
            log("[策略执行]接收：" + map);
            if(0 == map.size())
                return null;
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean APX_HTTPS_SSLVPN_SSL_TYPE() throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 获取密码类型：\n");

        try{
            String url = "https://" + ZtsAPXAddress + ":" + ztsAPXSPAPort + "/virtualRequest/SSLVPN_SSL_TYPE";
            String retStr = sendGetRequest(url);
            retStr = new String(retStr.getBytes("GBK"), "UTF-8");
            log("[获取密码类型]接收：" + retStr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean APX_HTTPS_GET_AREA(String name, String token, String sessionID) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 获取域：" + name + "\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("201", token) +                               //*token
                        str2Attr("404", sessionID);                            //会话ID

        try{
            log("[获取域]发送：" + SBuf);
            String url = "https://" + ZtsAPXAddress + ":" + ztsAPXSPAPort + "/virtualRequest/GET_AREA";

            String retStr = sendPostRequest(url, SBuf);
            retStr = new String(retStr.getBytes("GBK"), "UTF-8");
            log("[获取域]接收：" + retStr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean APX_HTTPS_GET_RESOURCE(String name, String token, String sessionID) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 获取资源：" + name + "\n");

        try{
            // 拼接报文内容
            String SBuf =
                    str2Attr("001", name) +                                //*账户名
                            str2Attr("201", token) +                               //*token
                            str2Attr("404", sessionID);                            //会话ID


            log("[获取资源]发送：" + SBuf);
            String url = "https://" + ZtsAPXAddress + ":" + ztsAPXSPAPort + "/virtualRequest/GET_RESOURCE";
            String retStr = sendPostRequest(url, SBuf);
            retStr = new String(retStr.getBytes("GBK"), "UTF-8");
            log("[获取资源]接收：" + retStr);
            String[] arr = retStr.split("\"");
            if(arr.length > 3)
                APXSsid = arr[3];/* 获取SSID */
            else
                return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean APX_HTTPS_TunnelInfo(String name) throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 获取TunnelInfo：\n");

        try{
            // 拼接报文内容
            String SBuf = "";

            log("[获取TunnelInfo]发送：" + SBuf);
            String url = "https://" + ZtsAPXAddress + ":" + ztsAPXSPAPort + "/sslvpn/action/TunnelInfo";
            String retStr = sendGetRequest(url);
            retStr = new String(retStr.getBytes("GBK"), "UTF-8");
            log("[获取TunnelInfo]接收：" + retStr);

            String[] arr = retStr.split("\"");
            if(arr.length > 5)
                vipMap.put(name, arr[5]);
            else
                return false;

            if(retStr.equals("{\"Result\":-1,\"Reason\":[\"Describe\",\"Describe\"]}"))
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean APX_HTTPS_SAInfo() throws Throwable {
        log(">>>>>>>>>>>>>>>>HTTPS 获取SA：\n");

        try{
            // 拼接报文内容
            String SBuf = "";
            String url = "https://" + ZtsAPXAddress + ":" + ztsAPXSPAPort + "/sslvpn/action/SAInfo";
            String retStr = sendGetRequest(url);
            retStr = new String(retStr.getBytes("GBK"), "UTF-8");
            log("[获取SA]接收：" + retStr);
            if(retStr.equals("{\"Result\":-1,\"Reason\":[\"Describe\",\"Describe\"]}") ||
                    retStr.equals("{\"Result\":-3,\"Reason\":[\"Describe\",\"Describe\"]}"))
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, String> LR_ONLY_KNOCK(String name, boolean ztsMUdpRecv) throws Throwable
    {
        Map<String, String> map;
        map = SDP_SPA_KNOCK(name, ""/* 仅敲门不需要密码 */, ztsMUdpRecv);
        if(null != map && null != map.get("415"))
        {
            //敲门失败了
            log("仅敲门失败,原因" + map.get("415"));
            return null;
        }
        return map;
    }

    public static Map<String, String> LR_ASK_UPDATE(String name) throws Throwable
    {
        Map<String, String> map;
        /*****************  询问更新 【客户端 <-> 控制器】*****************/
        do{
            map = SDP_HTTPS_ASK_UPDATE(name);

            if(null != map && map.get("401").equals("075")) {
                Thread.sleep(50);
                log("[询问更新]控制器处理中，准备重试");
            }
        }while(null != map && map.get("401").equals("075"));
        if(null != map && 0 != map.size() && null != map.get("415"))
        {
            //多因素认证失败了
            log("询问更新！！！！！！,原因：" + map.get("415"));
            return null;
        }
        return map;
    }

    public static Map<String, String> LR_M_SPA(String name, String password, boolean ztsMUdpRecv) throws Throwable
    {
        Map<String, String> map;
        /*****************  SPA敲门认证 【客户端 <-> 控制器】*****************/
        map = SDP_SPA_AUTH(name, password, ztsMUdpRecv);
        if(null != map && null != map.get("415"))
        {
            //敲门失败了
            log("登录失败,原因" + map.get("415"));
            return null;
        }
        return map;
    }

    public static Map<String, String> LR_M_SECOND_AUTH(String name, boolean ztsMUdpRecv, Long thinkTime) throws Throwable
    {
        Map<String, String> map;
        /*****************  多因素认证 【客户端 <-> 控制器】*****************/
        //不等待回包，则需要设置思考时间
        if(!ztsMUdpRecv)
            Thread.sleep(thinkTime);
        do{
            map = SDP_HTTPS_SECOND_AUTH(name);

            if(null == map || (map.get("401") != null && map.get("401").equals("075")))
            {
                Thread.sleep(50);
                log("控制器处理中，准备重试");
            }
        }while(null != map && map.get("401") != null && map.get("401").equals("075"));
        if(null == map || 0 == map.size() || null != map.get("415"))
        {
            //多因素认证失败了
            log("多因素认证失败！！！！！！,原因：" + (null != map ? map.get("415"):"null"));
            return null;
        }
        return map;
    }

    public static Map<String, String> LR_M_AUTH_POLICY(String name, String SessionID) throws Throwable
    {
        Map<String, String> map;
        /*****************  多因素认证 【客户端 <-> 控制器】*****************/
        map = SDP_HTTPS_SEND_AUTH_POLICY(name, SessionID);
        if(null == map || null != map.get("415"))
        {
            //策略执行失败了
            log("策略执行失败！！！！！！,原因：" + (null != map?map.get("415"):""));
            return null;
        }
        return map;
    }

    public static boolean LR_APX_AUTH(String name, String SessionID, String token, boolean tunnel) throws Throwable
    {
        /****** 二、网关敲门 ******/
        APX_SPA_KNOCK(name, token, SessionID, false);
        Thread.sleep(50);

        /******  获取密码类型 ******/
        if(!APX_HTTPS_SSLVPN_SSL_TYPE())
        {
            log(">>>>>>>>>>>>>>>>      上线网关失败，原因：(获取密码类型失败)！！！！！！");
            return false;
        }
        /******  获取互斥资源 ******/
        if(!APX_HTTPS_GET_AREA(name, token, SessionID))
        {
            log(">>>>>>>>>>>>>>>>      上线网关失败，原因：(获取互斥资源失败)！！！！！！");
            return false;
        }
        /******  获取资源 ******/
        if(!APX_HTTPS_GET_RESOURCE(name, token, SessionID))
        {
            log(">>>>>>>>>>>>>>>>      上线网关失败，原因：(获取资源失败)！！！！！！");
            return false;
        }
        //是否建立隧道
        if(tunnel)
        {
            log(">>>>>>>>>>>>>>>>建立隧道");
            /******  获取TunnelInfo ******/
            if(!APX_HTTPS_TunnelInfo(name))
            {
                log(">>>>>>>>>>>>>>>>      上线网关失败，原因：(获取TunnelInfo失败)！！！！！！");
                return false;
            }
            /******  1701探测 ******/
            APX_TUNNEL_PROB(1701);
            /******  获取SA ******/
            if(!APX_HTTPS_SAInfo())
            {
                log(">>>>>>>>>>>>>>>>      上线网关失败，原因：(获取SA失败)！！！！！！");
                return false;
            }
        }
        else
            log(">>>>>>>>>>>>>>>>不建隧道");
        return true;
    }

    public static Map<String, String> LR_M_AUTH_PASS(String name, String SessionID) throws Throwable
    {
        Map<String, String> map;
        /*****************  通知平台登录成功 【客户端 <-> 控制器】*****************/
        map = SDP_UDP_ONLINE(name, SessionID, true);
        if(null != map && null != map.get("415"))
        {
            //通知平台登录成功失败了
            log("[name]通知平台登录成功,原因" + map.get("415"));
            return null;
        }
        return map;
    }

    //发送1701包
    public static boolean APX_TUNNEL_PROB(int port) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP 1701探测");
        DatagramSocket socket = new DatagramSocket();
        byte[] Send = new byte[1024];

        //收包一分钟超时
        socket.setSoTimeout(60*1000);

        // 2.创建DatagramPacket，接收数据
        Send[0] = Send[1] = Send[2] = Send[3] = 0;

        byte[] ssid = APXSsid.getBytes();
        for(int i=0; i<ssid.length; i++) {
            Send[i+4] = ssid[i];
        }
        DatagramPacket packet = new DatagramPacket(Send, 0, 36, InetAddress.getByName(ZtsAPXAddress), port);
        // 3.发送数据
        socket.send(packet);

        // 接收消息
        byte[] data = new byte[1024];
        packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        String msg = new String(packet.getData(), 0, packet.getLength());
        log(">>>>>>>>>>>>>>>>UDP 1701探测，收包："+msg);
        socket.close();
        return true;
    }

    public static void AUTH(String name, String password, long thinkTime) throws Throwable {

        log(">>>>>>>>>>>>>>>>用户" + name + "开始认证");
        try
        {
            Map<String, String> map;

            if(ztsMUdpRecv)//新平台ztsMUdpRecv需设置为true
            {
                //SPA仅敲门 【客户端 <-> 控制器】
                if(null == (map = LR_ONLY_KNOCK(name, ztsMUdpRecv)))
                {
                    return;
                }

                //询问更新 【客户端 <-> 控制器】
                if(null == LR_ASK_UPDATE(name))
                {
                    return;
                }
            }

            //SPA敲门认证
            if(null == (map = LR_M_SPA(name, password, ztsMUdpRecv)))
            {
                if(ztsMUdpRecv)//等回包不能返回null
                {
                    return;
                }
            }

            //多因素认证 【客户端 <-> 控制器】
            if(null == (map = LR_M_SECOND_AUTH(name, ztsMUdpRecv, thinkTime)))
            {
                return;
            }

            //策略执行 【客户端 <-> 控制器】
            String SessionID = null != map.get("404") ? map.get("404") : "null";
            sessionMap.put(name, SessionID);
            String token = null != map.get("201")?map.get("201"):"";
            boolean tunnel = null != map.get("412")?map.get("412").equals("001"):true;
            if(null == (map = LR_M_AUTH_POLICY(name, SessionID)))
            {
                return;
            }

            //网关认证 【客户端 <-> 网关】
            if(!LR_APX_AUTH(name, SessionID, token, tunnel))
            {
                return;
            }

            //通知平台登录成功
            if(null == (map = LR_M_AUTH_PASS(name, SessionID)))
            {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log("认证存在异常");
        }
    }

    public static Map<String, String> LR_KEEPALIVE_UDP(String name, String sessionID) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   心跳，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("022", "My Loadrunner Virtual PC") +          //手机/计算机名称
                        str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                        str2Attr("401", "012") +                               //交互类型（CS/CG/SG）
                        str2Attr("404", sessionID) +                           //会话
                        str2Attr("660", "10%") +                               //
                        str2Attr("661", "20%") +                               //
                        str2Attr("678", "001");                                //心跳标识
        log("[心跳]发送：" + SBuf);
        Map<String, String> map = ZTS_UDP_PROC(SBuf, true, ZtsMAddress, ztsMSPAPort);
        log("[心跳]接收：" + map);
        return map;
    }


    public static Map<String, String> LR_OFFLINE_UDP(String name, String sessionID) throws Throwable {
        log(">>>>>>>>>>>>>>>>UDP   主动下线，用户名" + name + ":\n");

        // 拼接报文内容
        String SBuf =
                str2Attr("001", name) +                                //*账户名
                        str2Attr("203", System.currentTimeMillis() + "") +     //秒时间戳
                        str2Attr("401", "015") +                               //交互类型（CS/CG/SG）
                        str2Attr("404", sessionID);                 //会话ID
        log("[主动下线]发送：" + SBuf);
        Map<String, String> map = ZTS_UDP_PROC(SBuf, true, ZtsMAddress, ztsMSPAPort);
        log("[主动下线]接收：" + map);
        return map;
    }

    public static void OFFLINE(String name, double interval) throws Throwable
    {
        log(">>>>>>>>>>>>>>>>用户" + name + "主动下线");
        try
        {
            Map<String, String> map;

            //下线
            if(null == sessionMap.get(name) || null == (map = LR_OFFLINE_UDP(name, sessionMap.get(name))))
            {
                Thread.sleep((long) interval);
                return;
            }

            Thread.sleep((long) interval);
        } catch (Exception e) {
            e.printStackTrace();
            log("心跳存在异常");
            Thread.sleep((long) interval);
        }

    }

    /*
     *
     * 主入口
     *
     */
    public static void auth(String username, String password) throws Throwable {

        ZtsMAddress = "10.136.21.22";        //控制器IP
        ztsMSPAPort = 61113;                   //控制器SPA端口
        ztsMSSLPort = 61113;                   //控制器SSL接入端口
        ZtsAPXAddress = "10.136.17.24";      //网关IP
        ztsAPXSPAPort = 61113;                 //网关SPA端口
        ztsAPXSSLPort = 61113;                 //网关SSL接入端口
        ztsMUdpRecv = true;                    //是否等待UDP回包，新平台设置true，旧平台设置false
        Long thinkTime = 500L;                //思考时间，单位秒，ztsMUdpRecv为false时生效

        //认证
        AUTH(username, password, thinkTime);
    }

    public static void main(String[] args) throws Throwable{
        /*String password = "1.";
        String username = "uu-1-1";
        try {
                auth(username, password);
                Thread.sleep(100);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        String  s = "401003051404006400048013003D:0641451[{\"domainId\":\"2305000000000001\",\"latestModifyTime\":\"1753344836902\",\"noticeInvalidTime\":\"0\",\"noticePictureUrl\":\"\",\"preview\":\"微信及Google浏览器存在安全隐患，建议涉及终端尽快将Google浏览器升级至138.0.7204.157版本及以上，微信PC端请升级至4\",\"title\":\"【安全提醒】微信及Google浏览器存在安全隐患,建议涉及终端尽快完成升级！\",\"url\":\"/zts/notice/25072416050000387426325783374379457.html\"}]6560010640999[{\"behavior\":\"开放减分UDP端口\",\"behaviorId\":116,\"detail\":\"20,21,22,23,25,53,67,68,69,80,110,119,123,135,137,138,139,143,161,162,389,443,445,465,500,514,515,520,546,547,587,593,636,691,902,989,990,993,995,1025,1026,1027,1028,1029,1030,1194,1433,1434,1701,1720,1723,1812,1813,1863,1900,2000,2049,2082,2083,2086,2087,2095,2096,2222,2302,2483,2484,2638,3074,3260,3268,3269,3306,3389,3689,3690,3724,3725,4000,4500,4662,4664,4672,4899,5000,5001,5004,5005,5050,5060,5190,5222,5223,5269,5298,5500,5631,5632,5666,5800,5900,6000,6001,6112,6129,6257,6346,6347,6500,6566,6588,6665,6666,6667,6668,6669,6679,6697,6999,7000,7001,7070,7236,7777,7778,8000,8008,8009,8010,8074,8080,8081,8085,8086,8087,8088,8090,8097,8118,8123,8181,8200,8222,8291,8333,8400,8443,8484,8500,8649,8765,8777,8888,8899,9000,9001,9009,9010,9043,9060,9080,9090,9091,9100,9101,9102,9119,9290,9443,9999,10000\",\"score\":\"-855\",\"time\":1776043110925},{\"behavior\":\"开放减分TCP端口\",\"behaviorId\":115,\"detail\":\"20,21,22,23,25,53,67,68,69,64099980,110,119,123,135,137,138,139,143,161,162,389,443,445,465,500,514,515,520,546,547,587,593,636,691,902,989,990,993,995,1025,1026,1027,1028,1029,1030,1194,1433,1434,1701,1720,1723,1812,1813,1863,1900,2000,2049,2082,2083,2086,2087,2095,2096,2222,2302,2483,2484,2638,3074,3260,3268,3269,3306,3389,3689,3690,3724,3725,4000,4500,4662,4664,4672,4899,5000,5001,5004,5005,5050,5060,5190,5222,5223,5269,5298,5500,5631,5632,5666,5800,5900,6000,6001,6112,6129,6257,6346,6347,6500,6566,6588,6665,6666,6667,6668,6669,6679,6697,6999,7000,7001,7070,7236,7777,7778,8000,8008,8009,8010,8074,8080,8081,8085,8086,8087,8088,8090,8097,8118,8123,8181,8200,8222,8291,8333,8400,8443,8484,8500,8649,8765,8777,8888,8899,9000,9001,9009,9010,9043,9060,9080,9090,9091,9100,9101,9102,9119,9290,9443,9999,10000\",\"score\":\"-855\",\"time\":1776043110923},{\"behavior\":\"安装减分软件\",\"behaviorId\":114,\"detail\":\"Google Chrome,Microsoft Edge,Microsoft Office,Adobe Photoshop,Adobe Acrobat Reader,WinRAR,7-Zip,VLC Media Player,Windows 640999Media Player,Spotify,QQ,WeChat,Telegram,Discord,Zoom,Microsoft Teams,Skype,Steam,Epic Games,Unity,Visual Studio Code,IntelliJ IDEA,PyCharm,Eclipse,Android Studio,Oracle VM VirtualBox,VMware Workstation,Adobe Premiere Pro,Adobe Illustrator,Adobe After Effects,CorelDRAW,AutoCAD,SketchUp,Notepad++,Sublime Text,Python,Java Runtime Environment,.NET Framework,Node.js,MySQL Workbench,MySQL Server,Microsoft SQL Server,MongoDB,PostgreSQL,Oracle Database,Git,Docker,Postman,Insomnia,Fiddler,Wireshark,FileZilla,WinSCP,PuTTY,TeamViewer,AnyDesk,UltraVNC,Google Drive,OneDrive,Dropbox,Baidu Netdisk,iTunes,CCleaner,Malwarebytes,Windows Defender,Avast Antivirus,Norton Antivirus,Kaspersky,McAfee,360 Safe,QQ Browser,Sogou Pinyin,Google Pinyin,Microsoft Pinyin\",\"score\":\"-370\",\"time\":1776043110922},{\"behavior\":\"安装加分软件\",\"behaviorId\":2,\"detail\":\"Google Chrome,Microsoft Edge,Microsoft Office,Adobe Photoshop,Adobe Acrobat Reader,WinRAR,7-Zip,VLC Media Player,Windows Media Player,Spotify,QQ,WeChat,T640999elegram,Discord,Zoom,Microsoft Teams,Skype,Steam,Epic Games,Unity,Visual Studio Code,IntelliJ IDEA,PyCharm,Eclipse,Android Studio,Oracle VM VirtualBox,VMware Workstation,Adobe Premiere Pro,Adobe Illustrator,Adobe After Effects,CorelDRAW,AutoCAD,SketchUp,Notepad++,Sublime Text,Python,Java Runtime Environment,.NET Framework,Node.js,MySQL Workbench,MySQL Server,Microsoft SQL Server,MongoDB,PostgreSQL,Oracle Database,Git,Docker,Postman,Insomnia,Fiddler,Wireshark,FileZilla,WinSCP,PuTTY,TeamViewer,AnyDesk,UltraVNC,Google Drive,OneDrive,Dropbox,Baidu Netdisk,iTunes,CCleaner,Malwarebytes,Windows Defender,Avast Antivirus,Norton Antivirus,Kaspersky,McAfee,360 Safe,QQ Browser,Sogou Pinyin,Google Pinyin,Microsoft Pinyin\",\"score\":\"+370\",\"time\":1776043110920},{\"behavior\":\"减分进程\",\"behaviorId\":113,\"detail\":\"System.exe,Explorer.exe,svchost.exe,winlogon.exe,csrss.exe,services.exe,lsass.exe,smss.exe,dwm.exe,taskhost.exe,spoolsv.exe,msmpeng.exe,chrome.exe,firefox.exe,teams.exe,zoom.exe,discord.exe640999,spotify.exe,steam.exe,epicgames.exe,onedrive.exe,dropbox.exe,vscode.exe,pycharm.exe,eclipse.exe,outlook.exe,thunderbird.exe,word.exe,excel.exe,powerpnt.exe,acrobat.exe,photoshop.exe,illustrator.exe,premiere.exe,autocad.exe,solidworks.exe,vmware.exe,virtualbox.exe,putty.exe,winscp.exe,filezilla.exe,utorrent.exe,qbittorrent.exe,ccleaner.exe,malwarebytes.exe,avastui.exe,norton.exe,teamviewer.exe,anydesk.exe,git.exe,docker.exe,node.exe,python.exe,java.exe,mysql.exe,sqlservr.exe,oracle.exe,apache.exe,nginx.exe,iisexpress.exe,powershell.exe,cmd.exe,regedit.exe,msconfig.exe,taskmgr.exe,control.exe,calc.exe,notepad.exe,mspaint.exe,snippingtool.exe,winword.exe,excel.exe,powerpnt.exe\",\"score\":\"-365\",\"time\":1776043110910},{\"behavior\":\"加分进程\",\"behaviorId\":1,\"detail\":\"System.exe,Explorer.exe,svchost.exe,winlogon.exe,csrss.exe,services.exe,lsass.exe,smss.exe,dwm.exe,taskhost.exe,spoolsv.exe,msmpeng.exe,chrome.exe,firefox.exe,teams.exe,zoom.exe,discord.exe,spotify.exe,steam.exe,epicgames.exe640686,onedrive.exe,dropbox.exe,vscode.exe,pycharm.exe,eclipse.exe,outlook.exe,thunderbird.exe,word.exe,excel.exe,powerpnt.exe,acrobat.exe,photoshop.exe,illustrator.exe,premiere.exe,autocad.exe,solidworks.exe,vmware.exe,virtualbox.exe,putty.exe,winscp.exe,filezilla.exe,utorrent.exe,qbittorrent.exe,ccleaner.exe,malwarebytes.exe,avastui.exe,norton.exe,teamviewer.exe,anydesk.exe,git.exe,docker.exe,node.exe,python.exe,java.exe,mysql.exe,sqlservr.exe,oracle.exe,apache.exe,nginx.exe,iisexpress.exe,powershell.exe,cmd.exe,regedit.exe,msconfig.exe,taskmgr.exe,control.exe,calc.exe,notepad.exe,mspaint.exe,snippingtool.exe,winword.exe,excel.exe,powerpnt.exe\",\"score\":\"+365\",\"time\":1776043110909}]65200116530011703000704000667175您上次于2026-04-13 09:17:32登录成功，终端上次登录IP地址为10.136.18.160，上次登录终端系统为Windows11，终端本次登录IP地址为10.136.18.160。7190006920013";
        Map<String, String> stringStringMap = analysisMessageMap(s, 0);
        System.out.println(stringStringMap);
    }

    /**
     * 获取20000个用户名
     * uu-(1-25)-(1-800)
     * @return
     */
    private static List<String> getNameList() {
        List<String> nameList = new ArrayList<>();
        for (int i = 1; i < 25; i ++) {
            for (int j = 1; j < 800; j ++) {
                nameList.add("uu" + "-" + i + "-" + j);
            }
        }
        return nameList;
    }

}
