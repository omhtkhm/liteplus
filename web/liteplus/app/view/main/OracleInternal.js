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

        'Plus.view.oracleinternal.TablespaceInfo'
    ],
    layout: 'border',
    items: [{
        region: 'west',
        xtype: 'label',
        html: '<img src="../resources/images/oracleinternal2.png"  width="1182" height="800"/>'
    },{
        region: 'center',
        xtype: 'tablespaceinfo'
    }]
});