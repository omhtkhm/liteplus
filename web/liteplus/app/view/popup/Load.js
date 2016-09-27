/**
 * Created by hun on 2016-09-27.
 */
Ext.define('Plus.view.popup.Load', {
    extend: 'Ext.window.Window',
    alias : 'widget.load',

    title : 'Open A File',
    layout: 'fit',
    autoShow: true,
    modal: true,
    tempText: '',
    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                items: [
                    {
                        xtype: 'textfield',
                        name : 'loadname',
                        value: this.tempText,
                        fieldLabel: 'Load Name',
                        allowBlank: false,
                        msgTarget: 'side',
                        margin: '10 10 10 10'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: 'Load',
                itemId: 'popload'
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