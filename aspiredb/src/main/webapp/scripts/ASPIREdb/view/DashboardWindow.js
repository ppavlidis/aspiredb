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
Ext.require( [ 'Ext.Window', 'ASPIREdb.view.ProjectManagerWindow', 'ASPIREdb.view.UserManagerWindow' ] );
var projSel = 0;
/**
 * Display list of projects to load.
 */
Ext.define( 'ASPIREdb.view.DashboardWindow', {
   extend : 'Ext.Window',
   alias : 'widget.dashboardWindow',
   id : 'dashboardWindow',
   singleton : true,
   title : 'Dashboard',
   closable : true,
   closeAction : 'hide',
   width : 400,
   height : 250,
   modal : true,   
   layout : {
      type : 'vbox',
      align : 'center'
   },
   listeners:{
      'hide':function(win){     
         if(projSel==0)
            Ext.MessageBox.alert( 'Tip', 'Click Dashboard at the top of the page to select or create a new project.', function(){});            
       },
   },
   header: {
      items: [{
          xtype: 'image',       
          style:'right: auto; left: 0px; top: 6px;',          
          src: 'scripts/ASPIREdb/resources/images/qmark.png',     
          height: '14px',
          width: '15px',
          listeners: {
             afterrender: function(c) {
                 var toolTip = Ext.create('Ext.tip.ToolTip', {
                     target: c.getEl(),
                     html: 'Choose a project from the dropdown or select Manage Projects to create a new one.',
                     dismissDelay: 0,
                     showDelay: 0,                     
                 });                 
             }
         }
      }],
      layout: 'fit'
  },   
   bodyStyle : 'padding: 5px;',
   border : false,

   config : {
      // active project ID values holder
      activeProjectIds : [],

   },
   dockedItems : [ {
      xtype : 'toolbar',
      itemId : 'dashboardToolbar',
      dock : 'top'
   } ],

   initComponent : function() {

      this.callParent();
      this.enableToolbar();
      var ref = this;
      var projectStore = Ext.create( 'Ext.data.Store', {
         id : 'projectStore',
         proxy : {
            type : 'dwr',
            dwrFunction : ProjectService.getProjects,
            model : 'ASPIREdb.model.Project',
            reader : {
               type : 'json',
               root : 'name'
            }
         }
      } );

      var projectComboBox = Ext.create( 'Ext.form.ComboBox', {
         id : 'projectField',
         name : 'unit',
         fieldLabel : 'Project',
         store : projectStore,
         editable : false,
         displayField : 'name',
         allowBlank : false,
         valueField : 'id',
         forceSelection : true,
         emptyText : "Choose project...",
         msgTarget : 'qtip'
      } );

      this.add( projectComboBox );

      projectComboBox.on( 'select', function() {

         ProjectService.numSubjects( [ projectComboBox.getValue() ], {
            callback : function(numSubjects) {

               ref.getComponent( 'numSubjects' ).setText( 'Number of Subjects: ' + numSubjects );

            }

         } );

         ProjectService.numVariants( [ projectComboBox.getValue() ], {
            callback : function(numVariants) {

               ref.getComponent( 'numVariants' ).setText( 'Number of Variants:  ' + numVariants );

            }

         } );

      } );

      var okButton = Ext.create( 'Ext.Button', {
         text : 'ok',
         handler : function() {
            // var selectedProjectId=0;

            if ( !projectComboBox.getValue() ) {

               projectComboBox.setActiveError( 'Please select project' );
               return;
            } else {
               projSel = 1;
               var selectedProjectId = projectComboBox.getValue();
               // TODO : Now only one project is loaded at a time, but in future this might change
               if ( selectedProjectId != ref.activeProjectIds[0] ) {

                  ASPIREdb.ActiveProjectSettings.setActiveProject( [ {
                     id : projectComboBox.getValue(),
                     name : projectComboBox.getRawValue(),
                     description : ''
                  } ] );

                  var filterConfigs = [];

                  ref.activeProjectIds = ASPIREdb.ActiveProjectSettings.getActiveProjectIds();
                  var projectFilter = new ProjectFilterConfig;
                  projectFilter.projectIds = ref.activeProjectIds;
                  filterConfigs.push( projectFilter );
                  console.log( "filter_submit event from DashBoard window" );
                  ASPIREdb.EVENT_BUS.fireEvent( 'filter_submit', filterConfigs );

                  console.log( "query_update event from DashboardWindow" );
                  ASPIREdb.EVENT_BUS.fireEvent( 'query_update' );
                  ASPIREdb.EVENT_BUS.fireEvent( 'project_select' );

               }
               ref.close();

            }
         }
      } );

      this.add( {
         xtype : 'label',
         itemId : 'numSubjects',
         text : 'Number of Subjects:',
         margin : '20 20 5 20'
      }, {
         xtype : 'label',
         itemId : 'numVariants',
         text : 'Number of Variants:',
         margin : '5 20 20 20'
      } );

      this.add( okButton );

      ASPIREdb.EVENT_BUS.on( 'project_list_updated', ref.refreshDashboardHandler, ref );

   },
   /**
    * Enable the tool bar in dash board
    * 
    */
   enableToolbar : function() {
      var me = this;
      this.getDockedComponent( 'dashboardToolbar' ).removeAll();

      me.getDockedComponent( 'dashboardToolbar' ).add( {
         xtype : 'button',
         id : 'manageProject',
         text : 'Manage Projects',
         tooltip : 'Upload variants or upload phenotypes',         
         icon : 'scripts/ASPIREdb/resources/images/icons/wrench.png',
         handler : function() {
            ASPIREdb.view.ProjectManagerWindow.initGridAndShow();

         }
      } );

      LoginStatusService.isUserAdministrator( {
         callback : function(admin) {
            if ( admin ) {

               me.getDockedComponent( 'dashboardToolbar' ).add( '-' );

               // add project manager button
               me.getDockedComponent( 'dashboardToolbar' ).add( {
                  xtype : 'button',
                  id : 'manageManager',
                  text : 'Manage User Groups',
                  tooltip : 'Create groups and invite users to group',
                  icon : 'scripts/ASPIREdb/resources/images/icons/wrench.png',
                  handler : function() {
                     ASPIREdb.view.UserManagerWindow.initGridAndShow();

                  }
               } );

            }

         }
      } );

   },
   /**
    * Refresh the dash board
    */
   refreshDashboardHandler : function() {
      Ext.getCmp( 'projectField' ).getStore().load();
   }

} );