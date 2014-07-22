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

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ubc.pavlab.aspiredb.server.dao.VariantDao;
import ubc.pavlab.aspiredb.server.model.Variant;
import ubc.pavlab.aspiredb.server.util.ConfigUtils;
import ubc.pavlab.aspiredb.server.ws.CnvToBed;
import ubc.pavlab.aspiredb.shared.GenomicRange;

/**
 *
 *
 */
@Service("ucscConnector")
@RemoteProxy(name = "UCSCConnector")
public class UCSCConnectorImpl implements UCSCConnector {

    @Autowired
    VariantDao variantDao;

    @Override
    @RemoteMethod
    @Transactional(readOnly = true)
    public String constructCustomTracksFile( GenomicRange range, Collection<Long> activeProjectIds ) {

        Collection<Variant> variants = variantDao.findByGenomicLocation( new GenomicRange( range.getChromosome() ),
                activeProjectIds );

        return CnvToBed.create( variants, range.getChromosome(), range.getBaseStart(), range.getBaseEnd(),
                ConfigUtils.getBaseUrl() );
    }
}
