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
package ubc.pavlab.aspiredb.server.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ubc.pavlab.aspiredb.server.exceptions.BioMartServiceException;
import ubc.pavlab.aspiredb.server.exceptions.ExternalDependencyException;
import ubc.pavlab.aspiredb.server.exceptions.NotLoggedInException;
import ubc.pavlab.aspiredb.shared.GeneValueObject;

/**
 * author: anton date: 01/05/13
 */
public interface GeneService {

    public Map<String, GeneValueObject> findGenesAndURIsWithNeurocartaPhenotype( String phenotypeValueUri )
            throws NotLoggedInException, ExternalDependencyException;

    public Collection<GeneValueObject> findGenesWithNeurocartaPhenotype( String phenotypeValueUri )
            throws NotLoggedInException, ExternalDependencyException;

    public Collection<GeneValueObject> getGenesInsideVariants( Collection<Long> variantIds )
            throws NotLoggedInException, BioMartServiceException;

    public boolean isGeneSetName( String name );

    public Long saveUserGeneSet( String geneName, List<GeneValueObject> genes );

    public Map<Long, Collection<GeneValueObject>> getGenesPerVariant( Collection<Long> ids ) throws NotLoggedInException,
            BioMartServiceException;

    public Collection<Map<String, String>> getBurdenAnalysisPerSubject( Collection<Long> subjectIds )
            throws NotLoggedInException, BioMartServiceException;

}
