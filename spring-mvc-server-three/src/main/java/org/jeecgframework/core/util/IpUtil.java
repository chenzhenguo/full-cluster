package org.jeecgframework.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @Title: IpUtil
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月5日 下午1:41:08
 */
public class IpUtil {

	/**
	 * 获取本地IP
	 * 
	 * @return
	 * @throws Exception
	 *             String
	 */
	public static String getLocalIp() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			// 获取本地IP对象
			return ia.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}

	public static List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
						// System.out.println("获取本机IP："+ip);
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtil.error(e.getMessage(), e);
		}
		return ipList;
	}

}
