/**
 * Created by hun on 2016-09-05.
 */

Ext.define('Plus.view.westtoolbar.westToolBarSub02Class', {
    extend : 'Ext.toolbar.Toolbar',
    alias: 'widget.westtoolbarsub02',

    items: [{
        icon: '../resources/images/bt0201.png',
        cls: 'x-btn-icon',
        tooltip: 'Execution Plan (Ctrl+Alt+P)',
        itemId: 'plan',
        //handler: planExecute
    },{
        icon: '../resources/images/bt0202.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0203.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0204.png',
        cls: 'x-btn-icon',
        itemId: 'tableinfo',
        tooltip: 'Describe Table/View (ALT+F5)'
    },{
        icon: '../resources/images/bt0205.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0206.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 1'
    },{
        icon: '../resources/images/bt0207.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0208.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0209.png',
        cls: 'x-btn-icon',
        tooltip: 'Change Connection...',
        itemId: 'connection'
    }
    ]
});