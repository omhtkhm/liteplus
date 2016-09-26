/**
 * Created by hun on 2016-09-12.
 */
/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.controller.Plan',{
    extend: 'Ext.app.Controller',
    alias: 'controller.plan',

    views: ['bottom.PlanResult'],

    init: function(){
        console.log('Initialized LitePlus Controller');
        this.control({
            'westtoolbarsub02 #plan' : {
                click: this.onPlanClick
            },
            'centertextarea' : {
                //specialkey: this.onKeyDown,
                keydown: this.onKeyDown
            }
        });
    },

    onPlanClick: function(button, e, eOpts){
        console.log('실행 button click');
        var me = this;
        //var sqltext = Ext.getCmp('textareaId').getValue();
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];

        var sqltext = sqltextaray.getValue();
        console.log(sqltext);

        var tabs = Ext.ComponentQuery.query('sqltabpanel[name=sqltabpanel]')[0];
        var items = tabs.items.items;
        tabs.setActiveTab(items[1].id);

        // 웹소켓으로 SQL문 메시지를 보낸다
        var clientMessage = new Object();

        clientMessage.messageType = "plan";
        clientMessage.sqltext = sqltext;
        var clientMessage = JSON.stringify(clientMessage);
        console.log(clientMessage);
        mywebsocket.send (clientMessage);
    },

    onResult : function(message) {
        // do something
        //var gridResult = Ext.getCmp('gridQueryResultId');
        var treegridPlan = Ext.ComponentQuery.query('planresult[name=treegridplanresult]')[0];

        //console.log(resp.responseText);

        var jsonResult = Ext.JSON.decode(message);

        var success = jsonResult.success;
        var jsonResultSet = jsonResult.root;
        console.log(success);

        treegridPlan.reconfigure(this.createTreeStore(jsonResultSet));

        if(success) {
            //queryResultLabel.setText("First Rows Retrieved In " + firstRowTime + " seconds");
            //queryResultLabel.setText(firstRowTime);
            //console.log(queryResultLabel.text);
        } else {
            //queryResultLabel.setText(firstRowTime);
            //Ext.window.MessageBox.alert('Execute Plan : Error!');
        }
    },

    createTreeStore : function (json) {  // json에 tree json객체의 배열이 들어감
        return Ext.create('Ext.data.TreeStore', {
            root: json
        });
    },

    onKeyDown: function(textarea, e, eOpts){
        if(e.ctrlKey && e.altKey && (e.getCharCode() == Ext.EventObject.P)){
            console.log('Ctrl+ALT+P key down');
            this.onPlanClick();
        }
    }
});