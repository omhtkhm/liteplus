/**
 * Created by hun on 2016-09-02.
 */
//Ext.require('Ext.EventObject.*');

Ext.define('Plus.view.textarea.centerTextAreaClass', {
    extend : 'Ext.form.field.TextArea',
    //extend : 'Ext.field.TextArea',  //6.0스타일
    alias: 'widget.centertextarea',

    name: 'sqltextarea',
    value: 'SELECT * from scott.emp, dept where emp.deptno=dept.deptno;',
    enableKeyEvents: true,
});