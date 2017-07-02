package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
@Entity
public class UserInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = -2775795377497733539L;

	public static final int ROLE_ADMIN = 1;

	public static final int ROLE_STAFF = 2;

	public static final int STATUS_ENABLED = 1;

	public static final int STATUS_DISABLED = 2;

	private int _id;

	private String _username;

	private String _password;

	private int _role;

	private int _status;
	private String _realname;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Column(name = "username")
	public String getUsername() {
		return _username;
	}

	public void setUsername(String username) {
		_username = username;
	}

	@Column(name = "password")
	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}

	@Column(name = "role")
	public int getRole() {
		return _role;
	}

	public void setRole(int role) {
		_role = role;
	}

	@Column(name = "status")
	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	@Column(name = "realname")
	public String getRealname() {
		return _realname;
	}

	public void setRealname(String _realname) {
		this._realname = _realname;
	}

}
