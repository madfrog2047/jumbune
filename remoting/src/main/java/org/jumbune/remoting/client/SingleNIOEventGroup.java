package org.jumbune.remoting.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Singleton class for initializing the NIO event group. JumbuneAgentCommunicator reuses it for creating channel for each remoter command.
 *
 */
public class SingleNIOEventGroup {

	private static SingleNIOEventGroup instance;
	
	static {
		synchronized (SingleNIOEventGroup.class) {
			if (instance == null) {
				instance = new SingleNIOEventGroup();
			}
		}
	}
	
	private EventLoopGroup group;
	
	private SingleNIOEventGroup(){
		group = new NioEventLoopGroup();
	}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	
	public static EventLoopGroup eventLoopGroup(){
		return instance.group;
	}
}
