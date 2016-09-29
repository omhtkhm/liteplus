/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.view.Viewport',{
    extend: 'Ext.container.Viewport',
    alias: 'widget.mainviewport',
    //cls: 'custom-liteplusmain',
    requires:[
        'Plus.view.toptoolbar.topMenuClass',
        'Plus.view.main.mainTab'
    ],
    layout: 'border',
    items: [{
        region: 'north',
        layout: 'hbox',
        height: 80,
        //margin: '0 0 0 0',
        items: [{
            //xtype: 'image',
            //src: '../resources/images/LitePlus_logo.png'
            html: '<img src="../resources/images/LitePlus_logo.png" width="141" height="86" />',
            border: 0
        },{
            xtype: 'topMenu',
            border: 0
        }]
    }, {
        region: 'center',
        margin: '0 0 0 0',
        xtype: 'mainTab'
    }]
});
