/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.view.Viewport',{
    extend: 'Ext.container.Viewport',
    alias: 'widget.mainviewport',
    requires:[
        'Plus.view.toptoolbar.topMenuClass',
        'Plus.view.main.mainTab'
    ],
    layout: 'border',
    items: [{
        region: 'north',
        xtype: 'topMenu',
        //xtype: 'textarea',
        height: 40,
        margin: '0 0 0 0'
    }, {
        region: 'center',
        margin: '0 0 0 0',
        xtype: 'mainTab'
    }]
});
