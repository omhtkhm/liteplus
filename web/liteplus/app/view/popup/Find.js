/**
 * Created by hun on 2016-09-27.
 */
Ext.define('Plus.view.popup.Find', {
    extend: 'Ext.window.Window',
    alias : 'widget.find',

    title : 'Find',
    layout: 'fit',
    autoShow: true,
    modal: true,
    tempText: '',
    //tempRecord: [],
    tempStore: '',
    forward: true,
    casesensitive: false,
    whole: false,
    initComponent: function() {
        this.items =
            Ext.create('Ext.form.Panel', {
                //xtype: 'form',
                fieldDefaults:{
                    margin: '10 10 10 10'
                    //labelAlign: 'left',
                    //labelWidth: 90,
                    //anchor: '100%'
                },
                items: [
                    {
                        xtype: 'combobox',
                        fieldLabel: 'Search For',
                        displayField: 'name',
                        name : 'findname',
                        value: this.tempText,
                        allowBlank: false,
                        msgTarget: 'side',
                        store: this.tempStore
                    },
                    {
                        xtype: 'radiofield',
                        name: 'finddirection',
                        value: this.forward,
                        fieldLabel: 'Direction',
                        inputValue: 'forward',
                        boxLabel: 'Forward'
                    },
                    {
                        xtype: 'radiofield',
                        name: 'finddirection',
                        value: !(this.forward),
                        fieldLabel: '',
                        inputValue: 'backward',
                        labelSeparator: '',
                        hideEmptyLabel: false,
                        boxLabel: 'Backward'
                    },
                    {
                        xtype: 'checkboxfield',
                        name: 'findoptioncase',
                        value: this.casesensitive,
                        fieldLabel: 'Options',
                        boxLabel: 'Match Case'
                    },
                    {
                        xtype: 'checkboxfield',
                        name: 'findoptionwhole',
                        value: this.whole,
                        fieldLabel: '',
                        labelSeparator: '',
                        hideEmptyLabel: false,
                        boxLabel: 'Match Whole Word'
                    }
                ],
                defaults:{
                    enableKeyEvents:true,
                    listeners:{
                        specialKey: function(field, el)
                        {
                            if(el.getKey() == Ext.EventObject.ENTER)
                            {
                                //el.stopPropagation();
                                el.stopEvent();
                                Ext.ComponentQuery.query('#popfind')[0].fireEvent('click'); //OK버튼을 찾아서 click이벤트 발생. enter이벤트를 여기서 멈추어야 함
                            }
                        }
                    }
                }
            });

        this.buttons = [
            {
                text: 'OK',
                itemId: 'popfind'
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