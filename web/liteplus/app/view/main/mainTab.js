/**
 * Created by win on 2016-09-15.
 */

//Ext.require('Plus.view.plan.planResultClass');
//Ext.require('Plus.view.query.gridResultClass');
//Ext.require('Plus.view.bottombar.bottomResultBarClass');

Ext.define('Plus.view.main.mainTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.mainTab',
    name: 'mainTab',
    cls: 'maintab',
    requires: [
        'Plus.view.main.Query',
        'Plus.view.main.OracleInternal'
    ],
    activeTab: 0,
    items: [
        {xtype: 'orain', closable: false},
        {xtype: 'query', closable: false}
    ]
});