/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-9
 */

package com.greenorbs.tagassist.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SocketUtils {
	
	public static int getFreePort() {

		int[] ports = getFreePorts(1);
		if (ports.length >= 1) {
			return ports[0];
		}

		return -1;
	}

	public static int[] getFreePorts(int portNumber) {

		int[] result = new int[portNumber];
		List<ServerSocket> servers = new ArrayList<ServerSocket>(portNumber);
		ServerSocket tempServer = null;

		for (int i = 0; i < portNumber; i++) {
			try {
				tempServer = new ServerSocket(0);
				servers.add(tempServer);
				result[i] = tempServer.getLocalPort();
			} catch (IOException e) {
				
			} finally {
				for (ServerSocket server : servers) {
					try {
						server.close();
					} catch (IOException e) {
						// Continue closing servers.
					}
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {

		System.out.println(getFreePort());
	}
}
