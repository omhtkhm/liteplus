/**
 * Created by hun on 2016-09-12.
 */
/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.controller.Desc',{
    extend: 'Ext.app.Controller',
    alias: 'controller.desc',

    views: ['main.Desc'],

    init: function(){
        console.log('Initialized LitePlus Controller');
        this.control({
            'westtoolbarsub02 #tableinfo' : {
                click: this.onTableInfoClick
            },
            'centertextarea' : {
                keydown: this.onKeyDown
            }
        });
    },

    onTableInfoClick: function(button, e, eOpts){
        console.log('TableInfo button click');
        var me = this;
        //var sqltext = Ext.getCmp('textareaId').getValue(); //id로 가져올 경우 사용
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var sqltext = sqltextaray.getValue();
        var sqltext = 'EMP';

        console.log(sqltext);

        var tabs = Ext.ComponentQuery.query('mainTab[name=mainTab]')[0];
        //var items = tabs.items.items;  // 탭패널의 아이템들 중 items라는 항목을 찾는다
        //tabs.setActiveTab(items[1].id);  // items의 첫번째 항목 중 id 값을 가지고 탭을 활성화 시킨다

        // 패널을 하나 만들어서 mainTab에다가 붙여넣기
        var descPanel = Ext.create('Plus.view.main.Desc');
        var tab = tabs.add(descPanel);
        tabs.setActiveTab(tab);

        // 웹소켓으로 SQL문 메시지를 보낸다
        var clientMessage = new Object();

        clientMessage.messageType = "tableinfo";
        clientMessage.sqltext = sqltext;
        var clientMessage = JSON.stringify(clientMessage);
        console.log(clientMessage);

        mywebsocket.send (clientMessage);
    },

    onResult : function(message) {
        ////////////////////////////////////////
        // 컬럼정보 가져와서 뿌리기
        var columninfos = Ext.ComponentQuery.query('columninfo[name=columninfo]');
        //var columninfo = Ext.ComponentQuery.query('columninfo[name=columninfo]')[0];
        var columninfo = columninfos[columninfos.length-1];
        //console.log(resp.responseText);

        var jsonResult = Ext.JSON.decode(message);

        var success = jsonResult.success;
        var jsonResultSetColumnInfo = jsonResult.resultsetcolumninfo;
        console.log(success);

        columninfo.reconfigure(this.createStore(jsonResultSetColumnInfo), this.createColumns(jsonResultSetColumnInfo));

        ////////////////////////////////////////
        // Index정보 가져와서 뿌리기
        var indexinfos = Ext.ComponentQuery.query('index[name=index]');
        var indexinfo = indexinfos[indexinfos.length-1];
        var jsonResultSetIndexInfo = jsonResult.resultsetindexinfo;
        indexinfo.reconfigure(this.createStore(jsonResultSetIndexInfo), this.createColumns(jsonResultSetIndexInfo));

        ////////////////////////////////////////
        // Table기본정보 가져와서 뿌리기
        var tableinfos = Ext.ComponentQuery.query('tableinfo[name=tableinfo]');
        var tableinfo = tableinfos[tableinfos.length-1];
        var jsonResultSetTableInfo = jsonResult.resultsettableinfo;
        tableinfo.reconfigure(this.createTransposeStore(jsonResultSetTableInfo), this.createTransposeColumns());
        ////////////////////////////////////////
        // Constrait정보 가져와서 뿌리기
        var constraintinfos = Ext.ComponentQuery.query('constraint[name=constraint]');
        var constraintinfo = constraintinfos[constraintinfos.length-1];
        var jsonResultSetConstraintInfo = jsonResult.resultsetconstraintinfo;
        constraintinfo.reconfigure(this.createStore(jsonResultSetConstraintInfo), this.createColumns(jsonResultSetConstraintInfo));
        ////////////////////////////////////////
        // Grants정보 가져와서 뿌리기
        var grantsinfos = Ext.ComponentQuery.query('grants[name=grants]');
        var grantsinfo = grantsinfos[grantsinfos.length-1];
        var jsonResultSetGrantsInfo = jsonResult.resultsetgrantsinfo;
        grantsinfo.reconfigure(this.createStore(jsonResultSetGrantsInfo), this.createColumns(jsonResultSetGrantsInfo));
        ////////////////////////////////////////
        // Trigger정보 가져와서 뿌리기
        var triggerinfos = Ext.ComponentQuery.query('trigger[name=trigger]');
        var triggerinfo = triggerinfos[triggerinfos.length-1]; // 방금 생성한 마지막 탭의 컴포넌트를 가져온다.
        var jsonResultSetTriggerInfo = jsonResult.resultsettriggerinfo;
        //triggerinfo.reconfigure(this.createStore(jsonResultSetTriggerInfo), this.createColumns(jsonResultSetTriggerInfo));
        triggerinfo.setValue(jsonResultSetTriggerInfo[0].TRGSTMT1 + '\n' + jsonResultSetTriggerInfo[0].TRGSTMT2 + '\n\n' + jsonResultSetTriggerInfo[0].TRIGGER_BODY);
        //console.log(jsonResultSetTriggerInfo[0].TRGSTMT1);
        ////////////////////////////////////////
        // DDL정보 가져와서 뿌리기
        var ddlinfos = Ext.ComponentQuery.query('ddl[name=ddl]');
        var ddlinfo = ddlinfos[ddlinfos.length-1]; // 방금 생성한 마지막 탭의 컴포넌트를 가져온다.
        var jsonResultSetDDLInfo = jsonResult.resultsetddlinfo;
        ddlinfo.setValue(jsonResultSetDDLInfo[0].DDL);
        //console.log(jsonResultSetDDLInfo[0].DDL);
        //////////////////////////////////////////////
        if(success) {
            //console.log(queryResultLabel.text);
        } else {
            //Ext.window.MessageBox.alert('Execute Plan : Error!');
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
    },

    createTransposeStore : function (json) {  // json에 json객체의 배열이 들어감
        var keys = this.getKeysFromJson(json[0]); // 0번째 행의 Json객체
        //console.log(keys);    // 컬럼명(필드명)을 뽑아낸다. 스토어의 fields property를 셋팅하기 위함임
        //console.log(json);
        //console.log(keys);
        return Ext.create('Ext.data.Store', {
            fields: [{name: 'field1', type: 'string'},
                     {name: 'field2',  type: 'string'}],
            //data: [{field1:'key1',field2:'value1'},{field1:'key2',field2:'value2'}]     // 데이터는 [{field1:'key1',field2:'value1'},{field1:'key2',field2:'value2'}]
            data: this.swapJsonKeyValues(json)
        });
    },

    createTransposeColumns : function () {
        return ([{
                text: 'A',
                dataIndex: 'field1'
        },{
            text: 'B',
            dataIndex: 'field2'
        }]);
    },

    swapJsonKeyValues : function (input) {
    //var one, output = {};
    var tableInfoArray = new Array();
    for (one in input[0]) {
        //console.log(one);
        //console.log(input[0][one]);
        //if (input[0].hasOwnProperty(one)) {
            //output[input[one]] = one;
            //output['field1'] = one;
            //output['field2'] = input[0][one];
        //}
        var tableRow = new Object();
        tableRow.field1 = one;
        tableRow.field2 = input[0][one];
        tableInfoArray.push(tableRow);
    }
        //console.log(output);
        //console.log(tableInfoArray);
    //return output;
        return tableInfoArray;
    },

    onKeyDown: function(textarea, e, eOpts){
        //console.log('key down');
        //if(e.getCharCode()== (Ext.event.Event.CTRL && Ext.event.Event.ALT  && Ext.event.Event.P)){
        if(e.getCharCode()== (Ext.EventObject.ALT && Ext.EventObject.F5)){
            console.log('F5 key down');
            this.onTableInfoClick();
        }
    }
});