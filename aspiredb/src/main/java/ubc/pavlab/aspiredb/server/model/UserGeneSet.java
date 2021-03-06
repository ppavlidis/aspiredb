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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This table is created to hold the user interested gene set. Even though now the requirement is only to create user
 * interested gene set, this model can hold multiple gene sets for a user
 * 
 * @author: Gaya Charath
 * @since: 11/03/14
 */
@Entity
@Table(name = "USERGENESET")
public class UserGeneSet implements Securable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OBJECT", columnDefinition = "MEDIUMBLOB")
    private Serializable object;

    public UserGeneSet() {
    }

    public UserGeneSet( String name, Serializable object ) {
        this.name = name;
        this.object = object;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public Serializable getObject() {
        return object;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setObject( Serializable object ) {
        this.object = object;
    }

}
