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
package ubc.pavlab.aspiredb.server.service;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import ubc.pavlab.aspiredb.server.exceptions.BioMartServiceException;
import ubc.pavlab.aspiredb.server.exceptions.ExternalDependencyException;
import ubc.pavlab.aspiredb.server.exceptions.NeurocartaServiceException;
import ubc.pavlab.aspiredb.server.exceptions.NotLoggedInException;
import ubc.pavlab.aspiredb.shared.*;
import ubc.pavlab.aspiredb.shared.query.AspireDbFilterConfig;
import ubc.pavlab.aspiredb.shared.query.Property;
import ubc.pavlab.aspiredb.shared.query.QueryValueObject;
import ubc.pavlab.aspiredb.shared.suggestions.PhenotypeSuggestion;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author anton
 */
public interface QueryService {

    public PagingLoadResult<SubjectValueObject> querySubjects(AspireDbPagingLoadConfig config)
            throws NotLoggedInException, ExternalDependencyException, NotLoggedInException, ExternalDependencyException;

    public BoundedList<VariantValueObject> queryVariants(Set<AspireDbFilterConfig> filters)
            throws NotLoggedInException, ExternalDependencyException;

    public int getSubjectCount(AspireDbPagingLoadConfig config)
            throws NotLoggedInException, ExternalDependencyException;

    public int getVariantCount(AspireDbPagingLoadConfig config)
            throws NotLoggedInException, ExternalDependencyException;

    public PagingLoadResult<OntologyTermValueObject> getOntologyTermSuggestions(String query);
	
	public List<String> getValuesForOntologyTerm(String ontologyTermUri);

    public PagingLoadResult<PhenotypeSuggestion> getPhenotypeSuggestionLoadResult(String query, Collection<Long> activeProjects);

    public PagingLoadResult<GeneValueObject> getGeneSuggestionLoadResult(String query) throws BioMartServiceException, BioMartServiceException;

	public PagingLoadResult<NeurocartaPhenotypeValueObject> getNeurocartaPhenotypeSuggestionLoadResult(String query) throws NeurocartaServiceException, NeurocartaServiceException;

	public PagingLoadResult<SubjectValueObject> getSubjectSuggestionLoadResult(String query);
	
	public List<GwtSerializable> getVariantLocationValueObjects(Property property, List<String> text) throws BioMartServiceException, NeurocartaServiceException;
	
	public List<PhenotypeSuggestion> getPhenotypeSuggestions(List<String> names);
	
	public List<SubjectValueObject> getSubjects(List<String> subjectIds);

    public QueryValueObject saveQuery(QueryValueObject queryVO);

    public QueryValueObject loadQuery(Long id);

    public Collection<QueryValueObject> getSavedQueries() throws NotLoggedInException;

    public void deleteQuery(QueryValueObject query);

    // TODO: To be removed
    @Deprecated
    public Collection<GeneValueObject> getGeneSuggestions(String query) throws BioMartServiceException;
	
    // TODO: To be removed
    @Deprecated
    public Collection<NeurocartaPhenotypeValueObject> getNeurocartaPhenotypeSuggestions(String query) throws NeurocartaServiceException;
}