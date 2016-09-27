/**
 * Created by hun on 2016-09-27.
 */
Ext.define('Plus.view.popup.Save', {
    extend: 'Ext.window.Window',
    alias : 'widget.save',

    title : 'Save As... ',
    layout: 'fit',
    autoShow: true,
    modal: true,

    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                items: [
                    {
                        xtype: 'textfield',
                        name : 'savename',
                        fieldLabel: 'Save Name',
                        allowBlank: false,
                        msgTarget: 'side',
                        margin: '10 10 10 10'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: 'Save',
                action: 'save',
                itemId: 'popsave'
            },
            {
                text: 'Cancel',
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});