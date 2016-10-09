/**
 * Created by win on 2016-10-09.
 */
Ext.define('Plus.view.bottom.BatchOutput', {
    extend: 'Ext.form.field.TextArea',
    alias: 'widget.batchoutput',
    //disabled: true,
    readOnly: true,
    title: 'Batch Output',
    name: 'batchoutput',

    initComponent: function(){
        this.value = 'test';
        this.callParent(arguments);
    }
});