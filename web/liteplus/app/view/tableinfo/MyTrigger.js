/**
 * Created by win on 2016-09-18.
 */
Ext.define('Plus.view.tableinfo.MyTrigger', {
    extend: 'Ext.form.field.TextArea',
    alias: 'widget.mytrigger',

    title: 'MyTrigger',
    name: 'mytrigger',

    initComponent: function(){
        //this.columns = [
        //];
        this.viewConfig = {
            forceFit: true
        };

        this.callParent(arguments);
    }
});