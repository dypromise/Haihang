package com.greenorbs.tagassist.messagebus.util.querier;

import com.greenorbs.tagassist.UserInfo;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.ResponseUserInfo;
import com.greenorbs.tagassist.messagebus.message.GeneralQueries.QueryUserInfo;

public class UserQuerier extends BusQuerier {

	public UserQuerier(Publisher publisher) {
		super(publisher);
	}
	
	public UserInfo getUserInfo(String username, String password) {
		QueryUserInfo query = new QueryUserInfo(username, password);
		ResponseUserInfo response = (ResponseUserInfo) this.queryOne(query);
		
		if (response != null && response.getUserInfo() != null) {
			return response.getUserInfo();
		} else {
			return null;
		}
	}

}
