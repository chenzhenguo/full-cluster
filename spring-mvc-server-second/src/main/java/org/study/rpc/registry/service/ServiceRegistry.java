package org.study.rpc.registry.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.events.Event;

import io.netty.util.Constant;

/**
 * @Title: ServiceRegistry.java
 * @Description: TODO 实现服务注册
 * @author zhaotf
 * @date 2018年3月18日 下午12:36:22
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class ServiceRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
	private CountDownLatch latch = new CountDownLatch(1);
	private String registryAddress;

	int ZK_SESSION_TIMEOUT = 5000;
	String ZK_REGISTRY_PATH = "/rpcregistry";
	String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

	public ServiceRegistry(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public void register(String data) {
		if (data != null) {
			ZooKeeper zk = connectServer();
			if (zk != null) {
				createNode(zk, data);
			}
		}
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, ZK_SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});
			latch.await();
		} catch (Exception e) {
			LOGGER.error("ServiceRegistry", e);
			throw new RuntimeException(e);
		}
		return zk;
	}

	private void createNode(ZooKeeper zk, String data) {
		try {
			byte[] bytes = data.getBytes();
			String path = zk.create(ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			LOGGER.debug("rpc服务端,注册zookeeper节点 ({} => {})", path, data);
		} catch (Exception e) {
			LOGGER.error("rpc服务端异常", e);
		}
	}

}
