package com.greenorbs.tagassist.datacenter.replier;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.UserInfo;
import com.greenorbs.tagassist.datacenter.util.HibernateHelper;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseUserInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryUserInfo;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;

public class UserReplier extends BusMessageHandler {

	@Override
	protected void initSubscriber() {
		/* Queries */
		this.subscribe(QueryUserInfo.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		MessageBase reply = null;
		
		if (message instanceof QueryUserInfo) {
			
			String username = ((QueryUserInfo) message).getUsername();
			UserInfo userInfo = this.getUserInfoByUsername(username);
			
			// if user exists
			if (userInfo != null) {
				String password = ((QueryUserInfo) message).getPassword();
				if (userInfo.getPassword().equals(password)) {
					// login succeeded, return with password removed
					userInfo.setPassword(null);
				} else {
					// login failed, return null
					userInfo = null;
				}
			}
			
			reply = new ResponseUserInfo(userInfo);
			
		}
		
		return reply;
	}
	
	private UserInfo getUserInfoByUsername(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		
		String queryString = "from UserInfo where username='" + username + "'";
		UserInfo userInfo = (UserInfo) HibernateHelper.uniqueQuery(queryString);
		
		return userInfo;
	}

}
