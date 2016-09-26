/**
 * Created by win on 2016-09-26.
 */
/**
 * Created by hun on 2016-09-12.
 */
/**
 * Created by hun on 2016-09-12.
 */
Ext.define('Plus.controller.Format',{
    extend: 'Ext.app.Controller',
    alias: 'controller.format',

    views: ['westtoolbar.westToolBarSub01Class'],

    init: function(){
        console.log('Initialized LitePlus Format Controller');
        this.control({
            '#format' : {
                click: this.onFormatClick
            },
            'centertextarea' : {
                keydown: this.onKeyDown
                //specialkey: this.onKeyDown
            }
        });
    },

    onFormatClick: function(button, e, eOpts){
        console.log('Format button click');
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        //var sqltext = sqltextaray.getValue();
        //console.log(sqltext);
        var selectedText = Plus.app.getController('Query').getSelectedText(sqltextaray); //선택된값을 가져온다.

        var sqltext ;
        if(selectedText!='') {   // 선택된 셀렉션값이 있으면, SQL문을 선택된값으로 수정한다.
            sqltext = selectedText;
        } else {   // 셀렉션이 없으면 커서위치로 위아래의 빈줄로 나뉘어진 문장을 동적 Selection해야함. 빈줄이 없으면 전체문장으로 동적 Selection
            sqltext = sqltextaray.getValue();
            $(sqltextaray.inputEl.dom).setSelection(0,200);
        }
        console.log('sqltext: '+sqltext);

        // 웹소켓으로 SQL문 메시지를 보낸다
        var clientMessage = new Object();
        clientMessage.messageType = "format";
        clientMessage.sqltext = sqltext;
        var clientMessage = JSON.stringify(clientMessage);
        console.log(clientMessage);
        mywebsocket.send (clientMessage);
    },

    onResult : function(message) {
        var sqltextarea = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var jsonResult = Ext.JSON.decode(message);
        var success = jsonResult.success;
        var jsonResultSet = jsonResult.afterformat;
        console.log(success);

        //sqltextarea.setValue(jsonResultSet);
        var textareael = sqltextarea.inputEl.dom;
        $(textareael).insertAtCursor(jsonResultSet);
    },

    onKeyDown: function(textarea, e, eOpts){
        if(e.ctrlKey && e.shiftKey && (e.getCharCode() == Ext.EventObject.F)){
            console.log('Ctrl+Shift+F key down');
            this.onFormatClick();
        }
    }
});