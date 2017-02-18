/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.oracleinternal.Panel3', {
    extend: 'Ext.Panel',
    alias: 'widget.panel3',

    name: 'frame3',
    requires:[
    ],
    layout: {
        type : 'vbox',
        align : 'stretch'
    },
    items: [{
        xtype: 'label',
        html: '<div class="internal3u internal"><img src="../resources/images/oracleinternal.png"/></div>',
        flex: 1
    },{
        xtype: 'panel',
        layout: {
            type : 'hbox',
            align : 'stretch'
        },
        flex: 5,
        items: [{
            xtype: 'label',
            html: '<div class="internal3ll internal"><img src="../resources/images/oracleinternal.png"/></div>',
            flex: 1
        },{
            xtype: 'label',
            html: '<div class="internal3lc internal"><img src="../resources/images/oracleinternal.png"/></div>',
            flex: 1
        },{
            xtype: 'label',
            html: '<div class="internal3lr internal"><img src="../resources/images/oracleinternal.png"/></div>',
            flex: 1
        }]
    }]

});