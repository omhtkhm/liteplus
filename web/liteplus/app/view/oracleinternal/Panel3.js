/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.oracleinternal.Panel3', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.panel3',
    style: 'border: solid #000 2px',
    store: Ext.create('Ext.data.Store', {
        model : Ext.define('User', {
            extend: 'Ext.data.Model',
            fields : [ 'SID', 'USERNAME', 'UBLK', 'Time' ]
        }),
        data: [
            { SID: 'TEMP', 'USERNAME': '4.000', UBLK: '0', 'Time': '0.00' },
            { SID: 'TEMP', 'USERNAME': '4.000', UBLK: '0', 'Time': '0.00' },
            { SID: 'TEMP', 'USERNAME': '4.000', UBLK: '0', 'Time': '0.00' }
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
            text: '[MaxSize, M]',
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