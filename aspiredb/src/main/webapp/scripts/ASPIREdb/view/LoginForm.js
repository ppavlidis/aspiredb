Ext.require([
    'Ext.layout.container.*'
]);

/*
    <g:HorizontalPanel spacing="10" styleName="{style.box}">
        <c:ContentPanel width="300" bodyStyle="padding: 5px;" headerVisible="false">

                <container:child>
                    <g:HTML ui:field="message" text="Login failed. Username or password incorrect"
                    visible="false" styleName="{style.message}" />
                </container:child>

*/

Ext.define('ASPIREdb.view.LoginForm', {
    extend: 'Ext.window.Window',
    title: 'Welcome, please login.',
    width: 290,
    closable: false,
    resizable: false,
    layout: {
        type: 'vbox',
        padding: '5'
    },
    items: [
        {
            xtype: 'textfield',
            itemId: 'username',
            fieldLabel: 'Username',
            allowBlank: false
        },
        {
            xtype: 'textfield',
            itemId: 'password',
            fieldLabel: 'Password',
            inputType: 'password',
            allowBlank: false
        }
    ],

    buttons: [
        {
            xtype: 'button',
            itemId: 'helpButton',
            text: 'Help',
            style: 'float: left;',
            handler : function() {
                window.open( "http://aspiredb.sites.olt.ubc.ca/" , "_blank", "" );
            }
        },
        {
            xtype: 'button',
            itemId: 'clearButton',
            text: 'Clear'
        },
        {
            xtype: 'button',
            itemId: 'loginButton',
            text: 'Login',
            handler: function() {
                var me = this.ownerCt.ownerCt;
                Ext.Ajax.request({
                    url: '/j_spring_security_check',
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    params: Ext.Object.toQueryString(
                        {
                            'j_username' : this.ownerCt.ownerCt.getComponent('username').getValue(),
                            'j_password' : this.ownerCt.ownerCt.getComponent('password').getValue(),
                            'ajaxLoginTrue' : true
                        }
                    ),
                    success: function(response, opts) {
                        me.fireEvent('login');
/*
                        username.clear();
                        password.clear();

                        if ( result ) {
                            aspiredb.EVENT_BUS.fireEvent( new LoginEvent() );
                            message.setVisible( false );
                        } else {
                            message.setVisible( true );
                        }
*/
                    },
                    failure: function(response, opts) {

                    }
                });
            }
        }
    ],

    initComponent: function () {
        this.callParent();
    }

});
