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

package ubc.pavlab.aspiredb.server.project;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ubc.pavlab.aspiredb.server.BaseSpringContextTest;
import ubc.pavlab.aspiredb.server.dao.ProjectDao;
import ubc.pavlab.aspiredb.server.dao.Variant2SpecialVariantInfoDao;
import ubc.pavlab.aspiredb.server.dao.VariantDao;
import ubc.pavlab.aspiredb.server.model.Project;
import ubc.pavlab.aspiredb.server.model.Variant;
import ubc.pavlab.aspiredb.server.model.Variant2SpecialVariantInfo;
import ubc.pavlab.aspiredb.server.service.QueryService;
import ubc.pavlab.aspiredb.shared.BoundedList;
import ubc.pavlab.aspiredb.shared.CNVValueObject;
import ubc.pavlab.aspiredb.shared.CharacteristicValueObject;
import ubc.pavlab.aspiredb.shared.GenomicRange;
import ubc.pavlab.aspiredb.shared.VariantValueObject;
import ubc.pavlab.aspiredb.shared.query.AspireDbFilterConfig;
import ubc.pavlab.aspiredb.shared.query.ProjectFilterConfig;
import ubc.pavlab.aspiredb.shared.query.ProjectOverlapFilterConfig;

public class Project2SpecialProjectOverlapTest extends BaseSpringContextTest {

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private QueryService queryService;

    @Autowired
    private Variant2SpecialVariantInfoDao variant2SpecialVariantInfoDao;

    @Autowired
    ProjectDao projectDao;

    @Autowired
    VariantDao variantDao;

    final String patientId = RandomStringUtils.randomAlphabetic( 5 );
    final String projectId = RandomStringUtils.randomAlphabetic( 5 );

    final String userVariantId = RandomStringUtils.randomAlphabetic( 5 );
    
    final String userVariantId2 = RandomStringUtils.randomAlphabetic( 5 );

    final String overlapVariantId1 = RandomStringUtils.randomAlphabetic( 5 );
    final String overlapVariantId2 = RandomStringUtils.randomAlphabetic( 5 );
    final String overlapVariantId3 = RandomStringUtils.randomAlphabetic( 5 );
    final String overlapVariantId4 = RandomStringUtils.randomAlphabetic( 5 );

    final String patientIdWithOverlap = RandomStringUtils.randomAlphabetic( 5 );
    final String projectIdWithOverlap = RandomStringUtils.randomAlphabetic( 5 );

    final String patientIdWithNoOverlap = RandomStringUtils.randomAlphabetic( 5 );
    final String projectIdWithNoOverlap = RandomStringUtils.randomAlphabetic( 5 );

    @Before
    public void setup() throws Exception {

        super.runAsAdmin();

        ArrayList<VariantValueObject> cnvList = new ArrayList<VariantValueObject>();
        cnvList.add( getCNV( "X", 3, 234, userVariantId, patientId ) );
        
        cnvList.add( getCNV( "X", 1, 5, userVariantId2, patientId ) );
        cnvList.add( getCNV( "2", 123, 235, "XXXXXXX", patientId ) );
        cnvList.add( getCNV( "3", 12, 236, "XXXXXXX2", patientId ) );

        try {

            projectManager.addSubjectVariantsToProject( projectId, true, cnvList );

        } catch ( Exception e ) {

            fail( "projectManager.addSubjectVariantsToProject threw an exception" );

        }

        ArrayList<VariantValueObject> cnvListWithOverlap = new ArrayList<VariantValueObject>();

        // will overlap, by 231
        cnvListWithOverlap.add( getCNV( "X", 3, 234, overlapVariantId1, patientIdWithOverlap ) );

        // will overlap by 229
        cnvListWithOverlap.add( getCNV( "X", 5, 237, overlapVariantId2, patientIdWithOverlap ) );

        // will overlap by2
        cnvListWithOverlap.add( getCNV( "X", 1, 5, overlapVariantId3, patientIdWithOverlap ) );

        // will overlap by 2
        cnvListWithOverlap.add( getCNV( "X", 5, 7, overlapVariantId4, patientIdWithOverlap ) );

        cnvListWithOverlap.add( getCNV( "X", 400, 500, null, patientIdWithOverlap ) );

        cnvListWithOverlap.add( getCNV( "Y", 3, 234, null, patientIdWithOverlap ) );

        try {

            projectManager.addSubjectVariantsToProject( projectIdWithOverlap, true, cnvListWithOverlap );

        } catch ( Exception e ) {

            fail( "projectManager.addSubjectVariantsToProject threw an exception" );

        }

        ArrayList<VariantValueObject> cnvListWithNoOverlap = new ArrayList<VariantValueObject>();

        cnvListWithNoOverlap.add( getCNV( "4", 3000, 23400, null, patientIdWithNoOverlap ) );

        cnvListWithNoOverlap.add( getCNV( "5", 500, 237000, null, patientIdWithNoOverlap ) );

        cnvListWithNoOverlap.add( getCNV( "6", 100, 500, null, patientIdWithNoOverlap ) );

        cnvListWithNoOverlap.add( getCNV( "7", 500, 7000, null, patientIdWithNoOverlap ) );

        cnvListWithNoOverlap.add( getCNV( "8", 4000, 50000, null, patientIdWithNoOverlap ) );

        cnvListWithNoOverlap.add( getCNV( "9", 30, 2340, null, patientIdWithNoOverlap ) );

        try {

            projectManager.addSubjectVariantsToProject( projectIdWithNoOverlap, true, cnvListWithNoOverlap );

        } catch ( Exception e ) {

            fail( "projectManager.addSubjectVariantsToProject threw an exception:" + e.getMessage() );

        }

        try {

            projectManager.populateSpecialProjectOverlap( projectId, projectIdWithOverlap );
       

        } catch ( Exception e ) {

            fail( "projectManager.populateSpecialProjectOverlap threw an exception:" + e.getMessage() );

        }
        
        try {

            projectManager.populateSpecialProjectOverlap( projectId, projectIdWithNoOverlap );
       

        } catch ( Exception e ) {

            fail( "projectManager.populateSpecialProjectOverlap threw an exception:" + e.getMessage() );

        }
        
        
        

    }

    @Test
    public void testPopulateSpecialProjectOverlap() {

        super.runAsAdmin();

        Project projectToPopulate = projectDao.findByProjectName( projectId );

        Project specialProject = projectDao.findByProjectName( projectIdWithOverlap );

        ProjectFilterConfig projectToPopulateFilterConfig = getProjectFilterConfigById( projectToPopulate );

        HashSet<AspireDbFilterConfig> projSet = new HashSet<AspireDbFilterConfig>();
        projSet.add( projectToPopulateFilterConfig );

        BoundedList<VariantValueObject> projToPopulateVvos = null;

        try {

            projToPopulateVvos = queryService.queryVariants( projSet );
        } catch ( Exception e ) {

            fail( "queryService.queryVariants threw an exception" );

        }

        assertEquals( projToPopulateVvos.getItems().size(), 4 );

        
        List<VariantValueObject> vvos = projToPopulateVvos.getItems();

        VariantValueObject vvo = new VariantValueObject();

        for ( VariantValueObject v : vvos ) {

            if ( v.getUserVariantId() != null && v.getUserVariantId().equals( userVariantId ) ) {
                vvo = v;
                break;
            }

        }

        List<Long> specialProjectList = new ArrayList<Long>();

        specialProjectList.add( specialProject.getId() );

        Collection<Variant2SpecialVariantInfo> infos = variant2SpecialVariantInfoDao.loadByVariantId( vvo.getId(),
                specialProjectList );

        Variant v = variantDao.load( vvo.getId() );

        assertEquals( v.getUserVariantId(), userVariantId );

        assertEquals( v.getLocation().getChromosome(), "X" );

        assertEquals( infos.size(), 4 );

        for ( Variant2SpecialVariantInfo vInfo : infos ) {

            Variant specialVariant = variantDao.load( vInfo.getOverlapSpecialVariantId() );

            assertTrue( doesOverlap( v, specialVariant ) );

            // i just arbitrarily gave the overlapping variants userVariantIds and set the nonoverlapping ones to null,
            // only for the purposes of this test
            if ( specialVariant.getUserVariantId() == null ) {
                fail( "overlap special variant has null userVariantId, test failed " );
            } else if ( specialVariant.getUserVariantId().equals( overlapVariantId1 ) ) {// check the accuracy of the
                                                                                         // overlap length

                assertEquals( vInfo.getOverlap().intValue(), 231 );
                assertEquals( vInfo.getOverlapProjectId(), specialProject.getId() );

            } else if ( specialVariant.getUserVariantId().equals( overlapVariantId2 ) ) {

                assertEquals( vInfo.getOverlap().intValue(), 229 );
                assertEquals( vInfo.getOverlapProjectId(), specialProject.getId() );

            } else if ( specialVariant.getUserVariantId().equals( overlapVariantId3 ) ) {
                assertEquals( vInfo.getOverlap().intValue(), 2 );
                assertEquals( vInfo.getOverlapProjectId(), specialProject.getId() );
            } else if ( specialVariant.getUserVariantId().equals( overlapVariantId4 ) ) {
                assertEquals( vInfo.getOverlap().intValue(), 2 );
                assertEquals( vInfo.getOverlapProjectId(), specialProject.getId() );
            }

        }

    }

    @Test
    public void testProjectOverlapFilterWithOverlap() {

        

        Project project = projectDao.findByProjectName( projectId );

        Project projectWithOverlap = projectDao.findByProjectName( projectIdWithOverlap );
        
        List<Long> projectList = new ArrayList<Long>();
        projectList.add( project.getId() );

        List<Long> projectListWithOverlap = new ArrayList<Long>();
        projectListWithOverlap.add( projectWithOverlap.getId() );

        ProjectOverlapFilterConfig overlapFilter = new ProjectOverlapFilterConfig();
        
        overlapFilter.setProjectIds( projectList );
        overlapFilter.setOverlapProjectIds( projectListWithOverlap );
        

        BoundedList<VariantValueObject> result = null;

        Set<AspireDbFilterConfig> set = new HashSet<AspireDbFilterConfig>();

        set.add( overlapFilter );

        try {

            result = queryService.queryVariants( set );

        } catch ( Exception e ) {
            fail();
        }

        assertEquals( result.getItems().size(), 2 );

        assertEquals( result.getItems().iterator().next().getUserVariantId(), userVariantId );
        
        
    }
    
    @Test
    public void testProjectOverlapFilterWithNoOverlap() {

        Project project = projectDao.findByProjectName( projectId );
        
        Project projectWithNoOverlap = projectDao.findByProjectName( projectIdWithNoOverlap );

        List<Long> projectList = new ArrayList<Long>();
        projectList.add( project.getId() );

                
        List<Long> projectListWithNoOverlap = new ArrayList<Long>();
        projectListWithNoOverlap.add( projectWithNoOverlap.getId() );

        ProjectOverlapFilterConfig overlapFilter = new ProjectOverlapFilterConfig();
        
        overlapFilter.setProjectIds( projectList );
        overlapFilter.setOverlapProjectIds( projectListWithNoOverlap );

        BoundedList<VariantValueObject> result = null;

        Set<AspireDbFilterConfig> set = new HashSet<AspireDbFilterConfig>();

        set.add( overlapFilter );

        try {

            result = queryService.queryVariants( set );

        } catch ( Exception e ) {
            fail();
        }

        assertEquals( result.getItems().size(), 0 );

       
    }
    
    
    @Test
    public void testProjectOverlapFilterWithSpecificOverlapGreaterThan() {

        Project project = projectDao.findByProjectName( projectId );

        Project projectWithOverlap = projectDao.findByProjectName( projectIdWithOverlap );
        
        List<Long> projectList = new ArrayList<Long>();
        projectList.add( project.getId() );

        List<Long> projectListWithOverlap = new ArrayList<Long>();
        projectListWithOverlap.add( projectWithOverlap.getId() );

        ProjectOverlapFilterConfig overlapFilter = new ProjectOverlapFilterConfig();
        
        overlapFilter.setProjectIds( projectList );
        overlapFilter.setOverlapProjectIds( projectListWithOverlap );
        
      //overlap greater than 230, should return 1
        overlapFilter.setOperator( 1 );
        overlapFilter.setOverlap( 230 );

        BoundedList<VariantValueObject> result = null;

        Set<AspireDbFilterConfig> set = new HashSet<AspireDbFilterConfig>();

        set.add( overlapFilter );

        try {

            result = queryService.queryVariants( set );

        } catch ( Exception e ) {
            fail();
        }

        assertEquals( result.getItems().size(), 1 );

        assertEquals( result.getItems().iterator().next().getUserVariantId(), userVariantId );
        
        
    }
 
    @Test
    public void testProjectOverlapFilterWithSpecificOverlapLessThan() {

        Project project = projectDao.findByProjectName( projectId );

        Project projectWithOverlap = projectDao.findByProjectName( projectIdWithOverlap );
        
        List<Long> projectList = new ArrayList<Long>();
        projectList.add( project.getId() );

        List<Long> projectListWithOverlap = new ArrayList<Long>();
        projectListWithOverlap.add( projectWithOverlap.getId() );

        ProjectOverlapFilterConfig overlapFilter = new ProjectOverlapFilterConfig();
        
        overlapFilter.setProjectIds( projectList );
        overlapFilter.setOverlapProjectIds( projectListWithOverlap );
        
      //overlap less than 10, should return 1
        overlapFilter.setOperator( -1 );
        overlapFilter.setOverlap( 10 );

        BoundedList<VariantValueObject> result = null;

        Set<AspireDbFilterConfig> set = new HashSet<AspireDbFilterConfig>();

        set.add( overlapFilter );

        try {

            result = queryService.queryVariants( set );

        } catch ( Exception e ) {
            fail();
        }

        assertEquals( result.getItems().size(), 2 );

       
        
        
    }
    

    private boolean doesOverlap( Variant variant, Variant specialVariant ) {

        if ( !variant.getLocation().getChromosome().equals( specialVariant.getLocation().getChromosome() ) ) {
            return false;
        }

        int start = Math.max( variant.getLocation().getStart(), specialVariant.getLocation().getStart() );
        int end = Math.min( variant.getLocation().getEnd(), specialVariant.getLocation().getEnd() );

        // genius
        if ( start < end ) {
            return true;
        } else {
            return false;
        }

    }

    private ProjectFilterConfig getProjectFilterConfigById( Project p ) {

        ProjectFilterConfig projectFilterConfig = new ProjectFilterConfig();

        ArrayList<Long> projectIds = new ArrayList<Long>();

        projectIds.add( p.getId() );

        projectFilterConfig.setProjectIds( projectIds );

        return projectFilterConfig;

    }

    private CNVValueObject getCNV( String chrom, int baseStart, int baseEnd, String userVariantId, String patientId ) {

        CharacteristicValueObject cvo = new CharacteristicValueObject();

        cvo.setKey( "testChar" );
        cvo.setValue( "testcharvalue" );

        Map<String, CharacteristicValueObject> charMap = new HashMap<String, CharacteristicValueObject>();
        charMap.put( cvo.getKey(), cvo );

        CNVValueObject cnv = new CNVValueObject();

        cnv.setCharacteristics( charMap );
        cnv.setType( "GAIN" );

        cnv.setUserVariantId( userVariantId );

        GenomicRange gr = new GenomicRange();
        gr.setChromosome( chrom );
        gr.setBaseStart( baseStart );
        gr.setBaseEnd( baseEnd );
        cnv.setGenomicRange( gr );

        cnv.setPatientId( patientId );

        return cnv;

    }

}