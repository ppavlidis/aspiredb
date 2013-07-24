/*
 * The aspiredb project
 * 
 * Copyright (c) 2013 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ubc.pavlab.aspiredb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ubc.pavlab.aspiredb.client.exceptions.BioMartServiceException;
import ubc.pavlab.aspiredb.client.exceptions.ExternalDependencyException;
import ubc.pavlab.aspiredb.client.exceptions.NotLoggedInException;
import ubc.pavlab.aspiredb.shared.GeneValueObject;

import java.util.Collection;
import java.util.List;

/**
 * author: anton
 * date: 01/05/13
 */

@RemoteServiceRelativePath("springGwtServices/geneService")
public interface GeneService extends RemoteService {
    
    public List<GeneValueObject> getGenesInsideVariants( Collection<Long> variantIds )
            throws NotLoggedInException, BioMartServiceException;

    public Collection<GeneValueObject> findGenesWithNeurocartaPhenotype( String phenotypeValueUri )
            throws NotLoggedInException, ExternalDependencyException;


}
