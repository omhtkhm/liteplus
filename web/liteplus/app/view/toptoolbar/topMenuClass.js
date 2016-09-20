/**
 * Created by hun on 2016-09-02.
 */

Ext.define('Plus.view.toptoolbar.topMenuClass', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.topMenu',

    initComponent: function() {
        this.items = [{
            xtype: 'button',
            text: 'Query Tool'
//			handler: functionReference
        },
            '->',{
                xtype: 'splitbutton',
                text: 'Windows',
                menu: [{
                    text: 'SCOTT@ORCL2:192'
                }, {
                    text: 'PL/SQL Edit-SCOTT@ORCL2:192'
                }]}
        ],
            this.callParent(arguments);
    }
});