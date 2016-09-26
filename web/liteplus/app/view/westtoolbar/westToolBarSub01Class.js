/**
 * Created by hun on 2016-09-05.
 */

Ext.define('Plus.view.westtoolbar.westToolBarSub01Class', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.westtoolbarsub01',
    //controller: 'query',

    items: [{
        icon: '../resources/images/bt0101.png',
        cls: 'x-btn-icon',
        tooltip: 'Execute (F7)',
        itemId: 'query',
        action: 'query'
        //handler: queryExecute
        //listeners: {
        //    click: 'onQueryClick'
        //}
    },{
        icon: '../resources/images/bt0102.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2',
        //handler: switchTabTest
    },{
        icon: '../resources/images/bt0103.png',
        cls: 'x-btn-icon',
        tooltip: 'Format (Ctrl+Shift+F)',
        itemId: 'format',
    },{
        icon: '../resources/images/bt0104.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0105.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0106.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 1'
    },{
        icon: '../resources/images/bt0107.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0108.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },{
        icon: '../resources/images/bt0109.png',
        cls: 'x-btn-icon',
        tooltip: 'Button 2'
    },
        {
            icon: '../resources/images/bt0110.png',
            cls: 'x-btn-icon',
            tooltip: 'Button 2'
        }
    ]

    //afterRender: function() {
    //    var me = this;
    //    me.callParent(arguments);
    //    // in case of a form you can also use the findField() method
    //    // I used down() because it will work with all sort of containers
    //    me.down('button[name=query]').on('click',me.onQueryClick,me);
    //},
    //
    //onQueryClick: function(field) {
    //    // custom handler
    //    console.log("query button clicked");
    //}
});