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
                    //{
                    //    xtype: 'radiogroup',
                    //    columns: 2,
                    //    name: 'finddirection',
                    //    fieldLabel: 'Direction',
                    //    //vertical: true,
                    //    items: [{
                    //        name: 'finddirection',
                    //        boxLabel: 'Forward',
                    //        inputValue: 'forward',
                    //        checked: true
                    //    },{
                    //        name: 'finddirection',
                    //        boxLabel: 'Backward',
                    //        inputValue: 'backward'
                    //    }]
                    //},
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
                ]
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