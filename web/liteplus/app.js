/**
 * Created by hun on 2016-09-05.
 */
Ext.Loader.setConfig ({
    enabled: true ,
    paths: {
        'Ext.ux.WebSocket': '../resources/websocket/WebSocket.js' ,
        'Ext.ux.WebSocketManager': '../resources/websocket/WebSocketManager.js',
        'Plus.view.Viewport': './app/view/Viewport.js',
        //'Plus.controller.WSocket': './app/controller/WSocket.js',
        //'Plus.controller.OracleInternal': './app/controller/OracleInternal.js'
    }
});

//Ext.Loader.setPath('Plus', './app');

//Ext.require('Ext.QuickTips');
//Ext.require('Ext.container.Viewport');
Ext.require (['Ext.ux.WebSocket', 'Ext.ux.WebSocketManager']);
//Ext.require('Plus.controller.WSocket');
Ext.require('Plus.view.Viewport');
//Ext.require('Plus.controller.OracleInternal');
var mywebsocket; //웹소켓을 연결할 글로벌 전역변수 선언

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
        console.log('called function launch - application--------------------');

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

        var myWS = Ext.create('Plus.controller.WSocket');
        var ip = location.host;
        mywebsocket = Ext.create('Ext.ux.WebSocket', {
            url: 'ws://'+ip+'/wshandler',
            listeners: {
                open: function (ws) {
                    console.log('The websocket is ready to use--------');
                    //ws.send ('This is a simple text');
                    myTaskScheduler();  // 스케쥴러 시작
                    //var oracleInternalController = Plus.app.getController('OracleInternal');
                    //oracleInternalController.onTablespaceInfo(); //시작함수 호출
                },
                close: function (ws) {
                    console.log('The websocket is closed!');
                },
                error: function (ws, error) {
                    Ext.Error.raise(error);
                },
                message: function (ws, message) {
                    //console.log('A new message is arrived: ' + message); 서버측 응답메시지. 필요시만 찍을 것
                    myWS.messageHandler(message);
                }
            }
        });
    },

    //success : function() {
    //    var oracleInternalController = Plus.app.getController('OracleInternal');
    //    oracleInternalController.onTablespaceInfo(); //시작함수 호출
    //}
});

Ext.onReady(function () {
    //var oracleInternalController = Plus.app.getController('OracleInternal');
    //while (!MyApp){;}
    //var myoracleInternalController = Ext.create('Plus.controller.OracleInternal');
    //myoracleInternalController.onTablespaceInfo(); //시작함수 호출
    console.log('onReady function called-------------------');
});

var myTaskScheduler = function() {
    Ext.TaskManager.start({
        run: function() {
            var oracleInternalController = Plus.app.getController('OracleInternal');
            oracleInternalController.onTablespaceInfo(); //시작함수 호출
            console.log('onReady function called');
        },
        interval: 3600000
    });
}
