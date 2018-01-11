package org.dan;

import static org.dan.DistributeServer.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class DistributeClient {
	
	private ZooKeeper zkClient;
	
	private volatile List<String> servers;
	
	public DistributeClient() throws Exception {
		zkClient = new ZooKeeper(CONNECT_SERVERS, SESSION_TIMEOUT, (we) -> {
			try {
				getServers();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(servers);
			
		});
	}
	
	public void serviceLogic() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getServers() throws Exception{
		List<String> nodes = zkClient.getChildren(PARENT_NODE_PATH, true);
		List<String> servers = new ArrayList<>();
		for(String node : nodes) {
			byte[] data = zkClient.getData(PARENT_NODE_PATH + "/"  + node, false, new Stat());
			servers.add(new String(data));
		}
		this.servers = servers;
	}
	
	public static void main(String[] args) throws Exception {
		DistributeClient client = new DistributeClient();
		client.serviceLogic();
	}

}
