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
package ubc.pavlab.aspiredb.client.events;

import com.google.gwt.event.shared.GwtEvent;
import ubc.pavlab.aspiredb.client.handlers.LoginEventHandler;

/**
 * TODO Document Me
 * 
 * @author mly
 * @version $Id: LoginEvent.java,v 1.4 2013/06/11 22:30:49 anton Exp $
 */
public class LoginEvent extends GwtEvent<LoginEventHandler> {

    public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

    public LoginEvent() {
        // System.out.println(msg);
    }

    @Override
    protected void dispatch( LoginEventHandler handler ) {
        handler.onLogin( this );
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<LoginEventHandler> getAssociatedType() {
        return TYPE;
    }

}