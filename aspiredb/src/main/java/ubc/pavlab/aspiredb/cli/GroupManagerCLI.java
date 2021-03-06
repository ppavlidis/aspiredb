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
package ubc.pavlab.aspiredb.cli;

import gemma.gsec.authentication.UserDetailsImpl;
import gemma.gsec.authentication.UserManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * cli for creating users
 * 
 * @author cmcdonald
 * @version $Id: GroupManagerCLI.java,v 1.9 2013/07/19 17:03:49 ptan Exp $
 */
public class GroupManagerCLI extends AbstractCLI {

    private static UserManager userManager;

    private static PasswordEncoder passwordEncoder;

    private boolean createUser = false;

    private boolean deleteUser = false;

    private boolean changePassword = false;

    private boolean createGroup = false;

    private boolean deleteGroup = false;

    private boolean assignUserToGroup = false;

    private String groupName = null;

    private String aspireUserName = null;

    private String aspireUserEmail = null;

    private String aspireUserPassword = null;

    private static BeanFactory applicationContext;

    /**
     * @param args
     */
    public static void main( String[] args ) {
        ConsoleAppender console = new ConsoleAppender(); // create appender
        // configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout( new PatternLayout( PATTERN ) );
        console.setThreshold( Level.DEBUG );
        console.activateOptions();
        Logger.getRootLogger().addAppender( console );

        applicationContext = SpringContextUtil.getApplicationContext( false );

        userManager = ( UserManager ) applicationContext.getBean( "userManager" );

        passwordEncoder = ( PasswordEncoder ) applicationContext.getBean( "passwordEncoder" );

        GroupManagerCLI p = new GroupManagerCLI();
        try {
            Exception ex = p.doWork( args );
            if ( ex != null ) {
                ex.printStackTrace();
            }
            System.exit( 0 );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public String getShortDesc() {
        return "Add users and groups";
    }

    @Override
    protected void buildOptions() {

        OptionBuilder.hasArg();
        OptionBuilder.withArgName( "group name" );
        OptionBuilder.withDescription( "The group to give permissions to, if it doesn't exist it will be created" );
        Option groupname = OptionBuilder.create( "groupname" );

        OptionBuilder.hasArg();
        OptionBuilder.withArgName( "User" );
        OptionBuilder.withDescription( "The aspiredb user to assign to a group" );
        Option aspireuser = OptionBuilder.create( "aspireuser" );

        OptionBuilder.hasArg();
        OptionBuilder.withArgName( "User password" );
        OptionBuilder.withDescription( "The user password" );
        Option aspireuserpassword = OptionBuilder.create( "aspireuserpassword" );

        OptionBuilder.hasArg();
        OptionBuilder.withArgName( "User e-mail" );
        OptionBuilder.withDescription( "The user email" );
        Option aspireuserEmail = OptionBuilder.create( "aspireuseremail" );

        addOption( groupname );
        addOption( aspireuser );
        addOption( aspireuserpassword );
        addOption( aspireuserEmail );

        addOption(
                "createuser",
                false,
                "Use this option to create the user, options -aspireuser and -aspireuserpassword and -aspireuseremail required when using this option." );
        addOption( "deleteuser", false,
                "Use this option to delete an existing user, option -aspireuser required when using this option." );
        addOption( "creategroup", false,
                "Use this option to create a group, option -groupname required when using this option" );
        addOption( "deletegroup", false,
                "Use this option to delete an existing group, option -groupname required when using this option" );
        addOption( "assign", false,
                "Using this option will assign a user to a group, options -groupname and -aspireuser required" );

        addOption(
                "changepassword",
                false,
                "Using this option will change an existing user's password, options -aspireuser and -aspireuserpassword and -aspireuseremail required when using this option." );
    }

    @Override
    protected Exception doWork( String[] args ) {
        Exception err = processCommandLine( "Project ", args );
        authenticate( applicationContext );
        if ( err != null ) {
            return err;
        }

        if ( createUser ) {

            if ( aspireUserPassword == null || aspireUserName == null || aspireUserEmail == null ) {
                log.error( "missing -aspireuser or -aspireuserpassword or -aspireuseremail options" );
                bail( AbstractCLI.ErrorCode.MISSING_OPTION );
            }

            try {

                userManager.loadUserByUsername( aspireUserName );
                log.error( "User name already exists" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            } catch ( UsernameNotFoundException e ) {

                // String encodedPassword = passwordEncoder.encodePassword( aspireUserPassword, aspireUserName );
                String encodedPassword = passwordEncoder.encode( aspireUserPassword );
                UserDetailsImpl u = new UserDetailsImpl( encodedPassword, aspireUserName, true, null, null, null,
                        new Date() );

                userManager.createUser( u );

            }

        } else if ( deleteUser ) {

            if ( aspireUserName == null ) {
                log.error( "missing -aspireuser options" );
                bail( AbstractCLI.ErrorCode.MISSING_OPTION );
            }

            try {

                userManager.loadUserByUsername( aspireUserName );
                userManager.deleteUser( aspireUserName );

            } catch ( UsernameNotFoundException e ) {

                log.error( "User name doesn't exist" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );

            }

        } else if ( changePassword ) {

            if ( aspireUserName == null || aspireUserPassword == null || aspireUserEmail == null ) {
                log.error( "missing -aspireuser or -aspireuserpassword" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            }

            try {
                // String encodedPassword = passwordEncoder.encodePassword( aspireUserPassword, aspireUserName );
                String encodedPassword = passwordEncoder.encode( aspireUserPassword );
                userManager.changePasswordForUser( aspireUserEmail, aspireUserName, encodedPassword );

            } catch ( UsernameNotFoundException e ) {
                log.error( "user does not exist" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            }

        } else if ( createGroup ) {

            if ( groupName == null ) {
                log.error( "missing  -groupname option" );
                bail( AbstractCLI.ErrorCode.MISSING_OPTION );
            }

            if ( userManager.groupExists( groupName ) ) {
                log.error( "Group already exists" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            }

            List<GrantedAuthority> authos = new ArrayList<GrantedAuthority>();
            authos.add( new GrantedAuthorityImpl( groupName ) );

            userManager.createGroup( groupName, authos );

        } else if ( deleteGroup ) {

            if ( groupName == null ) {
                log.error( "missing  -groupname option" );
                bail( AbstractCLI.ErrorCode.MISSING_OPTION );
            }

            if ( !userManager.groupExists( groupName ) ) {
                log.error( "Group does not exist" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            }

            userManager.deleteGroup( groupName );

        } else if ( assignUserToGroup ) {

            if ( aspireUserName == null || groupName == null ) {
                log.error( "missing -aspireuser or -groupname options" );
                bail( AbstractCLI.ErrorCode.MISSING_OPTION );
            }

            if ( !userManager.groupExists( groupName ) ) {
                log.error( "Group does not exist, create group first with -creategroup option" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );
            }

            try {
                userManager.loadUserByUsername( aspireUserName );
            } catch ( UsernameNotFoundException e ) {

                log.error( "Username does not exist, create user first with -createuser option" );
                bail( AbstractCLI.ErrorCode.INVALID_OPTION );

            }

            userManager.addUserToGroup( aspireUserName, groupName );

        }

        return null;
    }

    @Override
    protected void processOptions() {

        if ( this.hasOption( "groupname" ) ) {
            groupName = this.getOptionValue( "groupname" );
        }
        if ( this.hasOption( "aspireuser" ) ) {
            aspireUserName = this.getOptionValue( "aspireuser" );
        }
        if ( this.hasOption( "aspireuserpassword" ) ) {
            aspireUserPassword = this.getOptionValue( "aspireuserpassword" );
        }
        if ( this.hasOption( "aspireuseremail" ) ) {
            aspireUserEmail = this.getOptionValue( "aspireuseremail" );
        }
        if ( this.hasOption( "createuser" ) ) {
            createUser = true;
        }
        if ( this.hasOption( "deleteuser" ) ) {
            deleteUser = true;
        }
        if ( this.hasOption( "changepassword" ) ) {
            changePassword = true;
        }
        if ( this.hasOption( "creategroup" ) ) {
            createGroup = true;
        }
        if ( this.hasOption( "deletegroup" ) ) {
            deleteGroup = true;
        }
        if ( this.hasOption( "assign" ) ) {
            assignUserToGroup = true;
        }
    }

}
