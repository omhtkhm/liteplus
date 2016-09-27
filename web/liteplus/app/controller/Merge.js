/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.controller.Merge',{
    extend: 'Ext.app.Controller',
    alias: 'controller.merge',
    views: ['westtoolbar.westToolBarSub01Class'],

    viewname: '',

    init: function(){
        console.log('Initialized LitePlus Merge Controller');
        this.control({
            '#merge' : {
                click: this.onMergeClick
            }
        });
    },

    onMergeClick: function(button, e, eOpts){
        console.log('Merge button click');
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var tablename = this.getTableFromLine(sqltextaray); //테이블명을 뽑음
        this.viewname = tablename;
        var sqltext = tablename.toUpperCase(); //테이블명
        this.tablename = sqltext;
        console.log('View name:'+sqltext);

        // 웹소켓으로 SQL문 메시지를 보낸다
        var clientMessage = new Object();
        clientMessage.messageType = "merge";
        clientMessage.sqltext = sqltext;
        var clientMessage = JSON.stringify(clientMessage);
        console.log(clientMessage);
        mywebsocket.send (clientMessage);
    },

    getTableFromLine: function(sqltextaray){
        var textarea = sqltextaray.inputEl.dom;
        var nLineCol = this.getController('Query').getLineNumberAndColumnIndex(textarea); //textarea에서 커서위치 가져오기
        var nRow = nLineCol.line;
        var nCol = nLineCol.col;
        var lines = textarea.value.split('\n');
        var nText = lines[(nRow-1)];
        //console.log(nText);
        var len = nText.length;
        var firstPart=nText.substring(0,nCol);  //앞에 자르고
        var secondPart=nText.substring(nCol,len); //뒤에 잘라서 테이블명 구분자를 찾는다
        console.log(firstPart +'|' + secondPart);
        var regExp = /[;,=\s()|&]+/g;
        var arr = firstPart.match(regExp);
        //console.log(arr[arr.length-1]);
        var startIndex =0;
        if(arr!=null) {
            startIndex = firstPart.lastIndexOf(arr[arr.length - 1]) + arr[arr.length - 1].length;
        }
        arr = secondPart.match(regExp);
        var endIndex =len;
        if(arr!=null) {
            var endIndex = firstPart.length + secondPart.indexOf(arr[0]);
        }
        var tablename = nText.substring(startIndex,endIndex);  // 찾은 테이블 명. 메시지 수신시에 교체할 수 있도록 selection해야 함.
        this.selectTextareaObject(textarea,nRow,startIndex,endIndex); // 행번호, selection범위. select범위는 전체 기준으로 바꾸어야 함!
        //console.log(startIndex+' '+endIndex);
        console.log('Table/View 명칭 : ' +tablename);
        return tablename;
    },

    onResult : function(message) {
        var sqltextarea = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var jsonResult = Ext.JSON.decode(message);
        var success = jsonResult.success;
        var jsonResultSet = jsonResult.resultsetmerge;
        console.log(success);

        //sqltextarea.setValue(jsonResultSet);
        var textareael = sqltextarea.inputEl.dom;

        console.log(jsonResultSet[0].TEXT);
        $(textareael).insertAtCursor('( '+jsonResultSet[0].TEXT+' ) '+'/*'+this.viewname+'*/');
    },

    selectTextareaObject : function (tarea,nRow,startidx,endidx) {
        var lines = tarea.value.split("\n");

        // calculate start/end
        var nLength = 0;
        for(var x = 0; x < nRow-1; x++) {  // 지정된 행까지 길이 더해서 startPos 계산
            nLength += (lines[x].length+1);
        }
        var startPos = nLength + startidx;
        var endPos = nLength + endidx;
        console.log('시작지점:'+startPos + ', 종료지점:'+endPos);

        $(tarea).setSelection(startPos,endPos);
    }
});