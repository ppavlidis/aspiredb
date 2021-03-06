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
package ubc.pavlab.aspiredb.server;

import ubc.pavlab.aspiredb.server.model.CnvType;
import ubc.pavlab.aspiredb.shared.GenomicRange;
import ubc.pavlab.aspiredb.shared.LabelValueObject;
import ubc.pavlab.aspiredb.shared.NumericValue;
import ubc.pavlab.aspiredb.shared.TextValue;
import ubc.pavlab.aspiredb.shared.VariantType;
import ubc.pavlab.aspiredb.shared.query.CNVTypeProperty;
import ubc.pavlab.aspiredb.shared.query.CharacteristicProperty;
import ubc.pavlab.aspiredb.shared.query.CopyNumberProperty;
import ubc.pavlab.aspiredb.shared.query.GenomicLocationProperty;
import ubc.pavlab.aspiredb.shared.query.Operator;
import ubc.pavlab.aspiredb.shared.query.VariantLabelProperty;
import ubc.pavlab.aspiredb.shared.query.VariantTypeProperty;
import ubc.pavlab.aspiredb.shared.query.restriction.Conjunction;
import ubc.pavlab.aspiredb.shared.query.restriction.RestrictionExpression;
import ubc.pavlab.aspiredb.shared.query.restriction.SetRestriction;
import ubc.pavlab.aspiredb.shared.query.restriction.SimpleRestriction;

/**
 * author: anton date: 22/05/13
 */
public class QueryTestUtils {

    public static RestrictionExpression makeTestVariantRestrictionExpression() {
        // RestrictionExpression type = new VariantTypeRestriction( VariantType.CNV );
        RestrictionExpression type = new SimpleRestriction( new VariantTypeProperty(), Operator.TEXT_EQUAL,
                new TextValue( VariantType.CNV.toString() ) );
        RestrictionExpression copyNumber = new SimpleRestriction( new CopyNumberProperty(), Operator.NUMERIC_EQUAL,
                new NumericValue( 2 ) );
        RestrictionExpression characteristic = new SimpleRestriction( new CharacteristicProperty( "BENIGN" ),
                Operator.TEXT_EQUAL, new TextValue( "YES" ) );

        Conjunction restriction = new Conjunction();
        restriction.add( type );
        restriction.add( copyNumber );
        restriction.add( characteristic );

        return restriction;
    }

    public static RestrictionExpression makeTestVariantRestrictionExpression( Long labelId ) {
        RestrictionExpression location = new SimpleRestriction( new GenomicLocationProperty(), Operator.IS_IN_SET,
                new GenomicRange( "X", 56600000, 56800000 ) );
        // FIXME
        // RestrictionExpression type = new VariantTypeRestriction( VariantType.CNV );
        RestrictionExpression type = new SetRestriction( new VariantTypeProperty(), Operator.IS_IN_SET, new TextValue(
                VariantType.CNV.toString() ) );

        RestrictionExpression cnvType = new SimpleRestriction( new CNVTypeProperty(), Operator.TEXT_EQUAL,
                new TextValue( CnvType.LOSS.toString() ) );
        RestrictionExpression labelRestriction = new SimpleRestriction( new VariantLabelProperty(),
                Operator.TEXT_EQUAL, new LabelValueObject( labelId, "CNV_TEST_LABEL" ) );

        RestrictionExpression copyNumber = new SimpleRestriction( new CopyNumberProperty(), Operator.NUMERIC_EQUAL,
                new NumericValue( 2 ) );
        RestrictionExpression characteristic = new SimpleRestriction( new CharacteristicProperty( "BENIGN" ),
                Operator.TEXT_EQUAL, new TextValue( "YES" ) );

        Conjunction restriction = new Conjunction();
        restriction.add( location );
        restriction.add( type );
        restriction.add( cnvType );
        restriction.add( labelRestriction );
        restriction.add( copyNumber );
        restriction.add( characteristic );

        return restriction;
    }

    public static RestrictionExpression makeTestVariantRestrictionExpressionWithSets( Long labelId ) {
        RestrictionExpression location = new SetRestriction( new GenomicLocationProperty(), Operator.IS_IN_SET,
                new GenomicRange( "X", 56600000, 56800000 ) );
        // FIXME
        // RestrictionExpression type = new VariantTypeRestriction( VariantType.CNV );
        RestrictionExpression type = new SetRestriction( new VariantTypeProperty(), Operator.IS_IN_SET, new TextValue(
                VariantType.CNV.toString() ) );

        RestrictionExpression cnvType = new SetRestriction( new CNVTypeProperty(), Operator.IS_IN_SET, new TextValue(
                CnvType.LOSS.toString() ) );
        RestrictionExpression labelRestriction = new SetRestriction( new VariantLabelProperty(), Operator.IS_IN_SET,
                new LabelValueObject( labelId, "CNV_TEST_LABEL" ) );

        RestrictionExpression copyNumber = new SimpleRestriction( new CopyNumberProperty(), Operator.NUMERIC_EQUAL,
                new NumericValue( 2 ) );
        RestrictionExpression characteristic = new SetRestriction( new CharacteristicProperty( "BENIGN" ),
                Operator.IS_IN_SET, new TextValue( "YES" ) );

        Conjunction restriction = new Conjunction();
        restriction.add( location );
        restriction.add( type );
        restriction.add( cnvType );
        restriction.add( labelRestriction );
        restriction.add( copyNumber );
        restriction.add( characteristic );

        return restriction;
    }
}
