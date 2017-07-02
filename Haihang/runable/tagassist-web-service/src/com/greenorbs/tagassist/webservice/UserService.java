package com.greenorbs.tagassist.webservice;

import com.greenorbs.tagassist.user.CrowdUserAuthen;

public class UserService {

//	public int login(String username, String password) {
//		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
//			return 0;
//		}
//		
//		username = StringUtils.trim(username);
//		password = StringUtils.trim(password);
//		
//		UserQuerier querier = new UserQuerier(BusUtils.getPublisher());
//		UserInfo userInfo = querier.getUserInfo(username, password);
//		
//		if (userInfo != null) {
//			return userInfo.getRole();
//		} else {
//			return 0;
//		}
//	}
	
	public int login(String username, String password) {
		CrowdUserAuthen authen = new CrowdUserAuthen(
				"http://192.168.1.107:8095/crowd/", "cargoscanner", "tagsys");
		
		return authen.authenticate(username, password);
	}
	
}
