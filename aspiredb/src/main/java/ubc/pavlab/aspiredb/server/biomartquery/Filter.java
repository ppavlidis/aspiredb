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
package ubc.pavlab.aspiredb.server.biomartquery;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An XML element called "Filter"
 * 
 * @author ??
 * @version $Id: Filter.java,v 1.4 2013/01/25 02:59:19 anton Exp $
 */
@XmlRootElement(name = "Filter")
class Filter {

    @XmlAttribute
    public String name;

    @XmlAttribute
    public String value;

    public Filter() {
    }

    public Filter( String name, String value ) {
        this.name = name;
        this.value = value;
    }

}
