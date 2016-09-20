/**
 * Created by hun on 2016-09-05.
 */
/**
 * Created by hun on 2016-09-02.
 */

//var toolbarsub01 = Ext.create('Plus.view.westToolBarSub01Class');
//var toolbarsub02 = Ext.create('Plus.view.westToolBarSub02Class');
Ext.require('Plus.view.westtoolbar.westToolBarSub01Class');
Ext.require('Plus.view.westtoolbar.westToolBarSub02Class');

Ext.define('Plus.view.westtoolbar.westToolBarClass', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.westtoolbar',

    dockedItems: [{
        xtype: 'westtoolbarsub01',
        dock: 'left',
        border: false
    },{
        xtype: 'westtoolbarsub02',
        dock: 'left',
        border: false
    }]
});