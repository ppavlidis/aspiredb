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
package ubc.pavlab.aspiredb.server.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ubc.pavlab.aspiredb.server.model.Inversion;

/**
 * TODO Document Me
 * 
 * @author ??
 * @version $Id: InversionDaoImpl.java,v 1.2 2013/06/11 22:30:44 anton Exp $
 */
@Repository("inversionDao")
public class InversionDaoImpl extends VariantDaoBaseImpl<Inversion> implements InversionDao {

    @Autowired
    public InversionDaoImpl( SessionFactory sessionFactory ) {
        super( Inversion.class );
        super.setSessionFactory( sessionFactory );
    }

}
