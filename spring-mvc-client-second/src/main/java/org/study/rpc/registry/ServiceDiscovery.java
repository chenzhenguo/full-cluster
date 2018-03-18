package org.study.rpc.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * @Title: ServiceDiscovery.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午1:59:02
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class ServiceDiscovery {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);
	int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/rpcregistry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
	private CountDownLatch latch = new CountDownLatch(1);

	private volatile List<String> dataList = new ArrayList<>();

	private String registryAddress;

	public ServiceDiscovery(String registryAddress) {
		this.registryAddress = registryAddress;

		ZooKeeper zk = connectServer();
		if (zk != null) {
			watchNode(zk);
		}
	}

	public String discover() {
		String data = null;
		int size = dataList.size();
		if (size > 0) {
			if (size == 1) {
				data = dataList.get(0);
				LOGGER.debug("using only data: {}", data);
			} else {
				data = dataList.get(ThreadLocalRandom.current().nextInt(size));
				LOGGER.debug("using random data: {}", data);
			}
		}
		return data;
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
			LOGGER.error("", e);
		}
		return zk;
	}

	private void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren(ZK_REGISTRY_PATH, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						watchNode(zk);
					}
				}
			});
			List<String> dataList = new ArrayList<>();
			for (String node : nodeList) {
				byte[] bytes = zk.getData(ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(bytes));
			}
			LOGGER.debug("node data: {}", dataList);
			this.dataList = dataList;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

}
