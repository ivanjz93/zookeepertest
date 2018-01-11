package org.dan;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DistributeServer {
	
	public static final String PARENT_NODE_PATH = "/servers";
	public static final String SERVER_NODE_PATH = "/server";
	
	public static final String CONNECT_SERVERS = "219.141.189.132:2181,219.141.189.137:2181,219.141.189.148:2181";
	public static final int SESSION_TIMEOUT = 2000;
	
	private ZooKeeper zkClient;
	private String serverName;
	
	public DistributeServer() throws IOException {
		zkClient = new ZooKeeper(CONNECT_SERVERS, SESSION_TIMEOUT, (we) -> {
			System.out.println(we);
		});
		Random random = new Random();
		String serverName = "Server" + random.nextInt();
		this.serverName = serverName;
	}
	
	public DistributeServer(String serverName) throws IOException {
		zkClient = new ZooKeeper(CONNECT_SERVERS, SESSION_TIMEOUT, (we) -> {
			System.out.println(we);
		});
		this.serverName = serverName;
	}
	
	public void registServer() throws Exception {
		if(zkClient.exists(PARENT_NODE_PATH, false) == null)
			zkClient.create(PARENT_NODE_PATH, "servers".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zkClient.create(PARENT_NODE_PATH + SERVER_NODE_PATH, serverName.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	//在这里注册业务逻辑代码
	public void serviceLogic() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		DistributeServer server;
		if(args.length > 0)
			server = new DistributeServer(args[0]);
		else
			server = new DistributeServer();
		server.registServer();
		server.serviceLogic();
	}
	
}
