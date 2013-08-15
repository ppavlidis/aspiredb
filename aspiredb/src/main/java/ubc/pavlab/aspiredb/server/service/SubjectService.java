/*
 * The aspiredb project
 * 
<<<<<<< HEAD
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ubc.pavlab.aspiredb.server.dao.CNVDao;
import ubc.pavlab.aspiredb.server.dao.LabelDao;
import ubc.pavlab.aspiredb.server.dao.SubjectDao;
import ubc.pavlab.aspiredb.server.model.Subject;
import ubc.pavlab.aspiredb.server.valueobjects.SubjectValueObject;

/**
 * TODO Document Me
 * 
 * @author ptan
 * @version $Id$
 */
@Service
@RemoteProxy
public class SubjectService {

    private static Logger log = LoggerFactory.getLogger( SubjectService.class );

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private CNVDao cnvDao;

    @Autowired
    private PhenotypeBrowserService phenotypeBrowserService;

    @Autowired
    private LabelDao labelDao;

    @RemoteMethod
    public List<SubjectValueObject> getSubjects() {

        Collection<Subject> subjects = subjectDao.loadAll();
        List<SubjectValueObject> vos = new ArrayList<SubjectValueObject>();

        for ( Subject s : subjects ) {
            SubjectValueObject vo = Subject.convertToValueObject( s );
            vos.add( vo );
        }

        return vos;
    }

    @RemoteMethod
    @Transactional(readOnly = true)
    public SubjectValueObject getSubject(Long projectId, Long subjectId ) {
        Subject subject = subjectDao.load( subjectId );
        if ( subject == null ) return null;

        SubjectValueObject vo = Subject.convertToValueObject( subject );
        
        // TODO add variants
        //Integer numVariants = cnvDao.findBySubjectId( subject.getPatientId() ).size();
        //vo.setVariants( numVariants != null ? numVariants : 0 );

        return vo;
    }

}
