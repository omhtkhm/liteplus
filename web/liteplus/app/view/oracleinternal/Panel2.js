/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.oracleinternal.Panel2', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.panel2',
    store: Ext.create('Ext.data.Store', {
        model : Ext.define('User', {
            extend: 'Ext.data.Model',
            fields : [ 'SID', 'USERNAME', 'UBLK', 'Time' ]
        }),
        data: [
            { SID: 'UNDOTBS', 'USERNAME': '8.000', UBLK: '0', 'Time': '0.00' },
            { SID: 'UNDOTBS', 'USERNAME': '8.000', UBLK: '0', 'Time': '0.00' },
            { SID: 'UNDOTBS', 'USERNAME': '8.000', UBLK: '0', 'Time': '0.00' }
        ]
    }),
    columns: [
        {
            text:'TS Name',
            flex:1,
            sortable: false,
            hideable: false,
            dataIndex : 'SID',
            cls: 'internalGridHeader'
        }, {
            text: '[Size, M]',
            flex:1,
            dataIndex : 'USERNAME',
            cls: 'internalGridHeader'
        },{
            text:'[Used, M]',
            flex:1,
            dataIndex:'UBLK',
            cls: 'internalGridHeader'
        },{
            text:'[Used, %]',
            flex:1,
            dataIndex:'Time',
            cls: 'internalGridHeader'
        }
    ]
});