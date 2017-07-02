/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenorbs.tagassist.util.log4jplus;

import java.sql.Connection;

/**
 * This interface has to be implemented for your own
 * database-connection-handling and its used in class JDBCLogger.
 *
 * @author 
 * <a href="mailto:t.fenner@klopotek.de">Thomas Fenner</a>
 * @since 1.0
 * @version see jdbcappender.jar/META-INF/MANIFEST.MF for version information
 */
public interface JDBCPoolConnectionHandler extends JDBCConnectionHandler {
	/**
	 *  The connection is free again, and can be used elsewhere
	 *
	 *@param  con            Description of Parameter
	 *@exception  Exception  Description of Exception
	 */
	void freeConnection(Connection con) throws Exception;

}

