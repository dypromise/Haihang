package com.greenorbs.tagassist.user;

import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.CrowdClient;

public class CrowdUserAuthen implements IUserAuthen {

	// String url = "http://192.168.1.107:8095/crowd/";
	
	CrowdClient client;
	
	public CrowdUserAuthen(String url, String appname, String apppass) {
		client = new RestCrowdClientFactory().newInstance(
                url, appname, apppass
        );
	}
	
	@Override
	public int authenticate(String username, String password) {
        try {
            //client.authenticateUser(username, password);
            return IUserAuthen.SUCCESS;
        } catch (Exception e) {
            return IUserAuthen.FAILURE;
        }
	}

}
