Ext.require([ 'Ext.layout.container.*', 'ASPIREdb.view.filter.OrFilterContainer' ]);

Ext.define('ASPIREdb.view.filter.DecipherProjectOverlapFilterContainer', {
	extend : 'Ext.Container',
	alias : 'widget.filter_decipherprojectoverlap',
	layout : {
		type : 'vbox'
	},
	config : {
		propertyStore : {
			
			proxy : {
				type : 'dwr',
				dwrFunction : VariantService.suggestPropertiesForProjectOverlap,				
				model : 'ASPIREdb.model.Property',
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'count'
				}
			}
		},
		propertyStore2 : {
			
			proxy : {
				type : 'dwr',
				dwrFunction : VariantService.suggestPropertiesForNumberOfVariantsInProjectOverlap,				
				model : 'ASPIREdb.model.Property',
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'count'
				}
			}
		},
		propertyStore3 : {
			
			proxy : {
				type : 'dwr',
				dwrFunction : VariantService.suggestPropertiesForSupportOfVariantsInProjectOverlap,				
				model : 'ASPIREdb.model.Property',
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'count'
				}
			}
		},
		suggestValuesRemoteFunction : null,
		filterItemType : 'ASPIREdb.view.filter.PropertyFilter'
	},
	items : [ {
		xtype : 'container',
		itemId : 'filterContainer',
		layout : {
			type : 'vbox',
			defaultMargins : {
				top : 5,
				right : 5,
				left : 5,
				bottom : 5
			}
		}
	} ],

	getRestrictionExpression : function() {
		var filterContainer = this.getComponent('filterContainer');
		
		var projectOverlapConfig = new ProjectOverlapFilterConfig();
        
		projectOverlapConfig.projectIds = ASPIREdb.ActiveProjectSettings.getActiveProjectIds();
		
		
		
		var overlapProjectIds = [];
		overlapProjectIds.push(this.specialProjectValueObject.id); 
		
		projectOverlapConfig.overlapProjectIds = overlapProjectIds;
		
		var overlapRestriction = filterContainer.getComponent('overlapItem').getRestrictionExpression();
		
		projectOverlapConfig.restriction1 = this.validateOverlapRestriction(overlapRestriction);
		
		var numVariantsOverlapRestriction = filterContainer.getComponent('numVariantsOverlapItem').getRestrictionExpression();
		
		projectOverlapConfig.restriction2 = this.validateOverlapRestriction(numVariantsOverlapRestriction);
		
		//"supportOfVariantsOverlapItem" should be hidden and empty, however we need to set the dwr object to have it. 
		var supportOfVariantsOverlapRestriction = filterContainer.getComponent('supportOfVariantsOverlapItem').getRestrictionExpression();
		
		projectOverlapConfig.restriction3 = this.validateOverlapRestriction(supportOfVariantsOverlapRestriction);
				
		projectOverlapConfig.phenotypeRestriction = filterContainer.getComponent('phenRestriction').getRestrictionExpression();
		
		projectOverlapConfig.invert = filterContainer.getComponent('invertCheckbox').getValue();
		
		return projectOverlapConfig;
	},

	setRestrictionExpression : function(restriction) {
		var filterContainer = this.getComponent('filterContainer');

		var getNewOverlapItem = this.getNewOverlapItemFunction();

		//TODO this will get called when a saved query is reconstructed, implement later after requirements are more clear
	},
	
	validateOverlapRestriction : function(restriction){
		
		
		//the widget defaults to a SetRestriction if nothing is set, so return SimpleRestriction(what dwr expects) if it is not set
		if (restriction.operator == null || restriction.property == null){
			
			var simpleRestriction = new SimpleRestriction();
			simpleRestriction.property = null;
			simpleRestriction.operator = null;
			var value = new NumericValue();
			value.value = null;
			simpleRestriction.value = value;
			return simpleRestriction;
			
		}
		
			
		return restriction;
	},
	
	
	getNewOverlapItemFunction : function(remoteFunction, storeRef, id, hidden) {
		
		if (hidden==undefined || hidden==null){
			
			hidden=false;
		}

		var filterTypeItem = this.getFilterItemType();
				
		var getNewOverlapItem = function() {

			return Ext.create(filterTypeItem, {
				propertyStore : storeRef,
				suggestValuesRemoteFunction : remoteFunction,
				itemId : id,
				hidden: hidden
			});

		};

		return getNewOverlapItem;
	},

	initComponent : function() {
		this.callParent();
		
		var filterContainer = this.getComponent("filterContainer");

		var getNewOverlapItem = this.getNewOverlapItemFunction(VariantService.suggestPropertiesForProjectOverlap, this.getPropertyStore(),'overlapItem');

		var overlapItem = getNewOverlapItem();
		
		var getNewNumVariantsOverlapItem = this.getNewOverlapItemFunction(VariantService.suggestPropertiesForNumberOfVariantsInProjectOverlap, this.getPropertyStore2(), 'numVariantsOverlapItem');

		var numVariantsOverlapItem = getNewNumVariantsOverlapItem();
		
		var getNewVariantSupportOverlapItem = this.getNewOverlapItemFunction(VariantService.suggestPropertiesForSupportOfVariantsInProjectOverlap, this.getPropertyStore3(), 'supportOfVariantsOverlapItem', true);

		var supportOfVariantsOverlapItem = getNewVariantSupportOverlapItem();
		
		filterContainer.insert(0,{xtype:"checkbox", itemId:"invertCheckbox" });
		filterContainer.insert(0, {
			xtype : 'label',
			text : 'Invert Filter: '
		});
		
		filterContainer.insert(0, {xtype: 'filter_phenotype_property', itemId : 'phenRestriction' });
		filterContainer.insert(0, {
			xtype : 'label',
			text : 'Phenotype Association of target project variants restriction: '
		});
		
		filterContainer.insert(0, supportOfVariantsOverlapItem);
		filterContainer.insert(0, {
			xtype : 'label',
			text : 'Number of Different Support Evidence restriction: ',
			hidden: true
		});
		
		filterContainer.insert(0, numVariantsOverlapItem);
		filterContainer.insert(0, {
			xtype : 'label',
			text : 'Number of Variants Overlapped restriction: '
		});
		
		filterContainer.insert(0, overlapItem);
		filterContainer.insert(0, {
			xtype : 'label',
			text : 'Length or %Length of Overlap restriction: '
		});
		

		this.updateSpecialProjectValue();
		
		
		
		
	},
	

	updateSpecialProjectValue : function() {

		var ref = this;
		
		ProjectService.getDecipherProject({
			
			callback : function(pvo) {

				ref.specialProjectValueObject = pvo;

			}
		});

	}

});
