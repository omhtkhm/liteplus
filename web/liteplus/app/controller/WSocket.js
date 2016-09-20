/**
 * Created by win on 2016-09-17.
 */
Ext.define('Plus.controller.WSocket', {
    extend: 'Ext.app.Controller',
    alias: 'controller.wsocket',

    init: function () {

        this.addEvents('newmessage');
        var me = this;
        var ip = location.host;

        mywebsocket = Ext.create('Ext.ux.WebSocket', {
            //url: 'ws://10.10.202.66/websocket' ,
            //url: 'ws://127.0.0.1/websocket',
            url: 'ws://'+ip+'/wshandler',
            listeners: {
                open: function (ws) {
                    console.log('The websocket is ready to use');
                    //ws.send ('This is a simple text');
                },
                close: function (ws) {
                    console.log('The websocket is closed!');
                },
                error: function (ws, error) {
                    Ext.Error.raise(error);
                },
                message: function (ws, message) {
                    console.log('A new message is arrived: ' + message);
                    //Plus.app.getController('Query').onQueryClick();
                    //showGrid(message);
                    //me.fire(message);
                    me.messageHandler(message);
                }
            }
        });
    },

    //fire : function(message){
    //    this.fireEvent('newmessage', message);
    //}
    messageHandler : function(message){
        var jsonResult = Ext.JSON.decode(message);
        var messageType = jsonResult.messageType;
        //var messageType = 'query';
        switch(messageType){
            case 'query' : {
                Plus.app.getController('Query').onResult(message);
                break;
            }
            case 'plan' : {
            Plus.app.getController('Plan').onResult(message);
            break;
            }
            case 'tableinfo' : {
                Plus.app.getController('Desc').onResult(message);
                break;
            }
            default :{
                console.log('Not Expected MessageType received');
            }
        }
    }

});