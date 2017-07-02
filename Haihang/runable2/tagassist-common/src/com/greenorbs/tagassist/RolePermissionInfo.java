/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.greenorbs.tagassist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import javax.persistence.*;

import java.util.Set;

/**
 * Model object that represents a security role.
 */
@Entity
@Table(name = "role_permission")
public class RolePermissionInfo {

	private int _id;
	private int _roleid;
	private String _permission;

	@Id
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	@Column(name = "roleid")
	public int getRoleid() {
		return _roleid;
	}

	public void setRoleid(int _roleid) {
		this._roleid = _roleid;
	}

	@Column(name = "permission")
	public String getPermission() {
		return _permission;
	}

	public void setPermission(String _permission) {
		this._permission = _permission;
	}

}
