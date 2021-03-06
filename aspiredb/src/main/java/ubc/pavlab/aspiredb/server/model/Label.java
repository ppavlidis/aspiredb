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
package ubc.pavlab.aspiredb.server.model;

import gemma.gsec.model.Securable;
import gemma.gsec.model.SecuredChild;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ubc.pavlab.aspiredb.server.ValueObjectConvertible;
import ubc.pavlab.aspiredb.shared.LabelValueObject;

@Entity
@Table(name = "LABEL")
public class Label implements SecuredChild, ValueObjectConvertible<LabelValueObject> {

    public static Collection<LabelValueObject> toValueObjects( Collection<Label> labels ) {
        Collection<LabelValueObject> valueObjects = new ArrayList<LabelValueObject>();
        for ( Label label : labels ) {
            valueObjects.add( label.toValueObject() );
        }
        return valueObjects;
    }

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COLOUR")
    private String colour;

    @Column(name = "IS_SHOWN")
    private Boolean isShown;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "QUERY_FK")
    private Query query;

    @Column(name = "DESCRIPTION", length = 4096)
    private String description;

    public Label() {
    }

    public Label( String name, String colour ) {
        this.name = name;
        this.colour = colour;
        this.isShown = true;
    }

    public Label( String name, String colour, Boolean isShown, String description ) {
        this.name = name;
        this.colour = colour;
        this.isShown = isShown;
        this.description = description;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Label ) ) {
            return false;
        }

        Label label = ( Label ) o;

        if ( id != null ? !id.equals( label.id ) : label.id != null ) {
            return false;
        }
        if ( !name.equals( label.name ) ) {
            return false;
        }

        return true;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    public Boolean isShown() {
        return isShown;
    }

    public void setColour( String colour ) {
        this.colour = colour;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setIsShown( Boolean isShown ) {
        this.isShown = isShown;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setQuery( Query query ) {
        this.query = query;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    @Override
    public LabelValueObject toValueObject() {
        return new LabelValueObject( id, name, colour, isShown, description );
    }

    @Override
    public Securable getSecurityOwner() {
        // TODO Auto-generated method stub
        return null;
    }
}
