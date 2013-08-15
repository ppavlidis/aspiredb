/*
 * The aspiredb project
 * 
 * Copyright (c) 2012 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ubc.pavlab.aspiredb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * TODO Document Me
 * 
 * @author cmcdonald
 * @version $Id: LoginStatusServiceAsync.java,v 1.2 2013/07/16 23:05:06 ptan Exp $
 */
public interface LoginStatusServiceAsync {

    public void isLoggedIn( AsyncCallback<Boolean> callback );
    
    public void isUserAdministrator( AsyncCallback<Boolean> callback );

    public void getCurrentUsername( AsyncCallback<String> callback );

}