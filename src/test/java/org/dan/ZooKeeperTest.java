package org.dan;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooDefs.Ids;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ZooKeeperTest {

	private final String CONNECT_SERVERS = "219.141.189.132:2181,219.141.189.137:2181,219.141.189.148:2181";
	private final int SESSION_TIMEOUT = 2000;
	
	private ZooKeeper zkClient;
	@Before
	public void init() throws Exception {
		zkClient = new ZooKeeper(CONNECT_SERVERS, SESSION_TIMEOUT, (we) ->{
			System.out.println(we.getType());
		});
		zkClient.create("/test_zk", "test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zkClient.create("/test_zk1", "test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zkClient.create("/test_zk2", "test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	@Test
	public void getChildren() throws KeeperException, InterruptedException {
		System.out.println(zkClient.getChildren("/", true));
	}
	
	@Test
	public void create() throws KeeperException, InterruptedException {
		zkClient.create("/eclipse", "hellozk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		Assert.assertNotNull(zkClient.exists("/eclipse", true));
	}
	
	@Test
	public void getData() throws KeeperException, InterruptedException {
		byte[] data = zkClient.getData("/test_zk", true, new Stat());
		Assert.assertArrayEquals("test".getBytes(), data);
	}
	
	@Test
	public void setData() throws KeeperException, InterruptedException {
		zkClient.setData("/test_zk1", "test1".getBytes(), -1);
		Assert.assertArrayEquals(zkClient.getData("/test_zk1", false, new Stat()), "test1".getBytes());
	}
	
	@Test
	public void delete() throws InterruptedException, KeeperException {
		zkClient.delete("/test_zk2", -1);
		Assert.assertNull(zkClient.exists("/test_zk2", false));
	}

	@After
	public void destroy() throws InterruptedException, KeeperException {
		if(zkClient.exists("/eclipse", false) != null)
			zkClient.delete("/eclipse", -1);
		if(zkClient.exists("/test_zk", false) != null)
			zkClient.delete("/test_zk", -1);
		if(zkClient.exists("/test_zk1", false) != null)
			zkClient.delete("/test_zk1", -1);
		if(zkClient.exists("/test_zk2", false) != null)
			zkClient.delete("/test_zk2", -1);
	}
}
