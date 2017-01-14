/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.controller.OracleInternal',{
    extend: 'Ext.app.Controller',
    alias: 'controller.oracleinternal',

    views: ['main.OracleInternal'],

    init: function(){
        console.log('Initialized ApplePie OracleInternal Controller');
        this.control({
        });

        //this.onTablespaceInfo();
    },

    onTablespaceInfo: function(){
        console.log('테이블스페이스 정보 요청');
        var sqltext = "null"; //테이블스페이스 정보

        // 웹소켓으로 SQL문 메시지를 보낸다
        var msg = new Object();
        msg.messageType = "tablespaceinfo";
        msg.sqltext = sqltext;
        var clientMessage = JSON.stringify(msg);
        console.log(clientMessage);

        mywebsocket.send (clientMessage);
    },

    onResult : function(jsonResult) {
        var success = jsonResult.success;
        console.log(success);
        if(success == true) {
            // 컬럼정보 가져와서 뿌리기
            var tablespaceinfos = Ext.ComponentQuery.query('tablespaceinfo[name=tablespaceinfo]');
            var tablespaceinfoinfo = tablespaceinfos[tablespaceinfos.length-1];
            var jsonResultSetTablespaceInfo = jsonResult.resultset;
            tablespaceinfoinfo.reconfigure(this.createStore(jsonResultSetTablespaceInfo), this.createColumns(jsonResultSetTablespaceInfo));
        } else {   // success가 false인경우
            var errorMsg = jsonResult.errormessage;
            Ext.Msg.alert('ApplePie', errorMsg, function (btn) {
            });
        }
    },

    getKeysFromJson : function (obj) {
        var keys = [];
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                keys.push(key);
            }
        }
        return keys;
    },

    createStore : function (json) {  // json에 json객체의 배열이 들어감
        var keys = this.getKeysFromJson(json[0]); // 0번째 행의 Json객체
        //console.log(keys);    // 컬럼명(필드명)을 뽑아낸다. 스토어의 fields property를 셋팅하기 위함임
        return Ext.create('Ext.data.Store', {
            fields: keys,
            data: json     // 데이터는 json객체 배열을 그대로 넣었음
        });
    },

    createColumns : function (json) {
        var keys = this.getKeysFromJson(json[0]);
        //var keys2 = keys.pop(); // 이상하게 id값이 컬럼에 추가가 되는 버그가 있는데, 임시로 그냥 단순히 마지막값을 삭제 처리함
        //console.log(keys);
        return keys.map(function (field) {
            return {
                text: Ext.String.capitalize(field),
                //width: 150,
                dataIndex: field
            };
        });
    }
});