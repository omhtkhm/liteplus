/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.oracleinternal.Panel1', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.panel1',
    name: 'panel1',
    style: 'border: solid #000 2px',
    store: Ext.create('Ext.data.Store', {
        model : Ext.define('User', {
            extend: 'Ext.data.Model',
            fields : [ 'SID', 'USERNAME', 'UBLK', 'Time' ]
        }),
        data: [
            { SID: '49', 'USERNAME': 'CTS', UBLK: '3', 'Time': '09:04:57' },
            { SID: '49', 'USERNAME': 'CTS', UBLK: '3', 'Time': '09:04:57' },
            { SID: '49', 'USERNAME': 'CTS', UBLK: '3', 'Time': '09:04:57' },
        ]
    }),
    columns: [
        {
            text:'SID',
            flex:1,
            sortable: false,
            hideable: false,
            dataIndex : 'SID',
            cls: 'internalGridHeader'
        }, {
            text: '[USERNAME]',
            flex:1,
            dataIndex : 'USERNAME',
            cls: 'internalGridHeader'
        },{
            text:'UBLK',
            flex:1,
            dataIndex:'UBLK',
            cls: 'internalGridHeader'
        },{
            text:'[Start Time]',
            flex:1,
            dataIndex:'Time',
            cls: 'internalGridHeader'
        }
    ]
});