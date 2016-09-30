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
                ],
            defaults:{
            enableKeyEvents:true,
                listeners:{
                specialKey: function(field, el)
                {
                    if(el.getKey() == Ext.EventObject.ENTER)
                    {
                        el.stopEvent();
                        Ext.ComponentQuery.query('#popsave')[0].fireEvent('click'); //팝업버튼을 찾아서 click이벤트 발생
                    }
                }
            }
        }
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