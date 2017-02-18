/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.main.OracleInternal',{
    extend: 'Ext.Panel',
    alias: 'widget.orain',
    name: 'orain',
    title: 'Oracle Internal',
    requires:[
        'Plus.view.tableinfo.TableInfo',
        'Plus.view.tableinfo.TableDetailTab',
        'Plus.view.tableinfo.ColumnInfo',
        'Plus.view.oracleinternal.CenterSGA',
        'Plus.view.oracleinternal.TablespaceInfo',
        'Plus.view.oracleinternal.Panel1',
        'Plus.view.oracleinternal.Panel2',
        'Plus.view.oracleinternal.Panel3'
    ],
    //layout: 'border',
    //items: [{
    //    region: 'west',
    //    xtype: 'label',
    //    html: '<img src="../resources/images/oracleinternal2.png"  width="1182" height="800"/>'
    //},{
    //    region: 'center',
    //    xtype: 'tablespaceinfo'
    //}]
    layout: {
        type : 'hbox',
        align : 'stretch'
    },
    items: [{
        xtype: 'label',
        html: '<div class="internal1 internal"><img src="../resources/images/oracleinternal.png"/></div>',
        flex: 1
    },{
        xtype: 'label',
        html: '<div class="internal2 internal"><img src="../resources/images/oracleinternal.png"/></div>',
        flex: 0.5
    },{
        xtype: 'centerSGA',
        flex: 2.5
    },
    //    {
    //    xtype: 'label',
    //    html: '<div class="internal4 internal"><img src="../resources/images/oracleinternal.png"/></div>',
    //    flex: 2
    //}
        {
            layout: 'absolute',
            html: '<div class="internal4 internal"><img src="../resources/images/oracleinternal.png"/></div>',
            flex: 2,
            items: [{
                xtype: 'tablespaceinfo',
                x: 19.5,
                y: 109,
                anchor: '88.8% 46.8%'
            },{
                xtype: 'panel1',
                x: 19.5,
                y: 344,
                anchor: '88.8% 63%'
            },{
                xtype: 'panel2',
                x: 19.5,
                y: 426,
                anchor: '88.8% 70%'
            },{
                xtype: 'panel3',
                x: 19.5,
                y: 503,
                anchor: '88.8% 88%'
            }]
        }
    ]
});