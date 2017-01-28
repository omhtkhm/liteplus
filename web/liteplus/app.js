/**
 * Created by hun on 2016-09-05.
 */
Ext.Loader.setConfig ({
    enabled: true ,
    paths: {
        'Ext.ux.WebSocket': '../resources/websocket/WebSocket.js' ,
        'Ext.ux.WebSocketManager': '../resources/websocket/WebSocketManager.js',
        'Plus.view.Viewport': './app/view/Viewport.js',
        'Plus.controller.WSocket': './app/controller/WSocket.js'
    }
});

//Ext.require('Ext.QuickTips');
//Ext.require('Ext.container.Viewport');
Ext.require (['Ext.ux.WebSocket', 'Ext.ux.WebSocketManager']);
Ext.require('Plus.controller.WSocket');
Ext.require('Plus.view.Viewport');
var mywebsocket; //웹소켓을 연결할 변수 선언

Ext.application({
    name: 'Plus',
    appFolder: 'app',
    controllers: [
        'WSocket',
        'Query',
        'Plan',
        'Desc',
        'Format',
        'Merge',
        'SaveOpen',
        'PlanTree',
        'History',
        'Batch',
        'OracleInternal'         // 애플파이 용
    ],
    //autoCreateViewport: true,

    launch: function() {
        console.log('called function launch - application');

        Ext.create('Plus.view.Viewport');


        //Ext.QuickTips.init();
        //
        //var txtContextMenu = Ext.create('Plus.view.contextmenu.contextMenuClass');
        //Ext.getDoc().on({
        //    contextmenu: function(eventObj) {
        //        txtContextMenu.showAt(eventObj.getXY());
        //    },
        //    stopEvent: true
        //});
    }
});

//Ext.onReady(function () {
////    var oracleInternalController = Plus.app.getController('OracleInternal');
////    oracleInternalController.onTablespaceInfo(); //시작함수 호출
//    console.log('onReady function called');
//});

Ext.TaskManager.start({
    run: function() {
        var oracleInternalController = Plus.app.getController('OracleInternal');
        oracleInternalController.onTablespaceInfo(); //시작함수 호출
        console.log('onReady function called');
    },
    interval: 100000
});

