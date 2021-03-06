/**
 * Created by win on 2016-09-15.
 */
Ext.define('Plus.view.oracleinternal.TablespaceInfo', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.tablespaceinfo',
    name: 'tablespaceinfo',
    style: 'border: solid #000 2px',
    hideHeaders: false,
    //height: 100,
    //width : 300,
    initComponent: function(){
        this.columns = [
            //{id:'company',header: 'Company', width: 160, sortable: true, dataIndex: 'company'},
            //{header: 'Price', width: 75, sortable: true, renderer: 'usMoney', dataIndex: 'price'},
            //{header: 'Change', width: 75, sortable: true, dataIndex: 'change'},
            //{header: '% Change', width: 75, sortable: true, dataIndex: 'pctChange'},
            //{header: 'Last Updated', width: 85, sortable: true, renderer: Ext.util.Format.dateRenderer('m/d/Y'), dataIndex: 'lastChange'}
        ];
        this.viewConfig = {
            forceFit: true
        };
        this.callParent(arguments);
    }
});