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
package ubc.pavlab.aspiredb.shared;

import java.io.Serializable;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * 
 */
@DataTransferObject
public class ProjectValueObject implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2176326059505424010L;

    private Long id;
    
    private String name;
    
    private String description;
    
    private Boolean special;

    public ProjectValueObject() {
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }    

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial( Boolean special ) {
        this.special = special;
    }

    
}