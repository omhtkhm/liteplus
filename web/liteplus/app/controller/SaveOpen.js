/**
 * Created by hun on 2016-09-27.
 */
Ext.define('Plus.controller.SaveOpen',{
    extend: 'Ext.app.Controller',
    alias: 'controller.saveopen',
    views: ['westtoolbar.westToolBarSub01Class','westtoolbar.westToolBarSub02Class','popup.Save','popup.Load'],

    forward: true,
    casesensitive: false,
    whole: false,

    init: function(){
        console.log('Initialized LitePlus SaveOpen Controller');
        this.control({
            '#save' : {
                click: this.onSaveClick
            },'#saveas' : {
                click: this.onSaveAsClick
            },'#load' : {
                click: this.onLoadClick
            },'#insert' : {
                click: this.onInsertClick
            },'#popsave' : {
                click: this.localStorageSave
            },'#popload' : {
                click: this.localStorageLoad
            },'#find' : {
                click: this.onFindClick
            },'#popfind' : {
                click: this.findText
            },'centertextarea' : {
                keydown: this.onKeyDown
                //specialkey: this.onKeyDown
            }
        });
    },

    onSaveClick: function(){
        console.log('Save button click');
        //this.filename = '1'; // 단순 저장은 파일명을 '1'로 고정한다.
        //this.saveas = false;
        if(!this.filename) { // 파일명을 저장했으면
            this.savepopup = Ext.create('Plus.view.popup.Save');
            this.savepopup.show();
        } else {  // 세션에서 파일명을 한번이라도 지정했으면
            this.localStorageSave();
        }
    },

    localStorageSave: function(){
        console.log('Local Storage pop save clicked!');
        //sessionStorage.setItem('isFileName', 'true'); //세션스토리지에 파일명이 저장되었음을 flag함
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var savetext = sqltextaray.getValue();
        //if(this.saveas == true) {
        if((this.saveas == true)|| (!this.filename)) {
            var filenamefield = Ext.ComponentQuery.query('textfield[name=savename]')[0];
            this.filename = filenamefield.getValue(); // 팝업창에서 파일명을 설정했으면 지정, 아니면 필드변수를 그대로 둠
        }
        //}
        console.log(this.filename + ':' + savetext);
        $.jStorage.set(this.filename, savetext);
        this.savepopup.close();
        //console.log(savetext);
    },

    onSaveAsClick: function(){
        console.log('SaveAs button click');
        this.savepopup = Ext.create('Plus.view.popup.Save');
        this.saveas = true;
        this.savepopup.show();
    },

    onLoadClick: function(){
        console.log('Load button click');
        this.loadpopup = Ext.create('Plus.view.popup.Load',{
            tempText: this.filename
        });
        this.insert = false; // insert인지 아니면 바꿔치기인지 선택
        console.log(this.filename);
        //this.loadpopup.items.items.value = this.filename;
        console.log(this.loadpopup.items.items);
        this.loadpopup.show();
    },

    localStorageLoad: function(){
        console.log('Local Storage pop load clicked!');
        var filenamefield= Ext.ComponentQuery.query('textfield[name=loadname]')[0];
        var filename = filenamefield.getValue();
        this.loadpopup.close();
        var loadtext = $.jStorage.get(filename);
        var sqltextaray = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];

        if(this.insert == false) {  // 바꿔치기면
            sqltextaray.setValue(loadtext);
        } else{  // 커서위치에 삽입하는 것이면
            $(sqltextaray.inputEl.dom).insertAtCursor(loadtext);
        }
    },

    onInsertClick: function(){
        console.log('Insert button click');
        this.loadpopup = Ext.create('Plus.view.popup.Load');
        this.insert = true; // insert인지 아니면 바꿔치기인지 선택
        this.loadpopup.show();
    },

    onFindClick: function(){
        console.log('Find button click');
        var store = this.createFindComboStore();
        this.findpopup = Ext.create('Plus.view.popup.Find',{
            tempText: this.findname,
            tempStore: store,
            forward: this.forward,
            casesensitive: this.casesensitive,
            whole: this.whole
    });
        this.findpopup.show();
    },

    findText: function(){
        console.log('pop find clicked!');
        var findnamefield= Ext.ComponentQuery.query('textfield[name=findname]')[0];
        this.findname = findnamefield.getValue();
        // 정방향인지 역방향인지
        var finddirection = Ext.ComponentQuery.query('radiofield[name=finddirection]')[0];
        if(finddirection.getValue()) {this.forward = true;}
        else {this.forward = false;}// 정방향 역방향 셋팅
        // 대소분자 구분인지 아닌지
        var casesens = Ext.ComponentQuery.query('checkboxfield[name=findoptioncase]')[0];
        if(casesens.getValue()) {this.casesensitive = true;}  // 대소문자 구별하도록
        else {this.casesensitive = false;}
        //전체 문자인지 아닌지
        var wholeword = Ext.ComponentQuery.query('checkboxfield[name=findoptionwhole]')[0];
        if(wholeword.getValue()) {this.whole = true;}  // 대소문자 구별하도록
        else {this.whole = false;}

        this.findpopup.close();
        var sqltextarea = Ext.ComponentQuery.query('textarea[name=sqltextarea]')[0];
        var textareaText = sqltextarea.getValue();
        //sqltextarea.setValue(this.findname);
        var currentPos = $(sqltextarea.inputEl.dom).getCursorPosition();

        var targetText = textareaText;
        var searchText = this.findname;
        if(!this.casesensitive) { //대소문자 안가리면 대문자 변경
            targetText = textareaText.toUpperCase();
            searchText = this.findname.toUpperCase()
        }
        if(this.whole) { //단어가 같아야 되면
            searchText = ' '+searchText+' ';
        }
        var startIndex;
        if(this.forward) { //정방향
            var searchfrom = currentPos+1;
            console.log(searchfrom);
            var startIndex = targetText.indexOf(searchText, searchfrom);
        } else{ //역방향 검샘
            var searchfrom = currentPos-1;
            console.log(searchfrom);
            var startIndex = targetText.lastIndexOf(searchText, searchfrom);
        }
        var queryResultLabel = Ext.ComponentQuery.query('label[name=queryresultlabelname]')[0];
        if(startIndex != -1) { // 찾는 문자가 있는 경우,
            if(this.whole) startIndex = startIndex +1; //단어가 같아야 되면 공백을 포함하였으므로
            var endIndex = startIndex + this.findname.length;

            $(sqltextarea.inputEl.dom).setSelection(startIndex, endIndex);
            queryResultLabel.setText('Ready');
        } else{ //찾는 문자가 없는 경우
            queryResultLabel.setText('String Not Found');
        }
    },

    createFindComboStore : function () {
        if(!this.comboJsonArray) this.comboJsonArray = new Array();
        var comboJson = new Object();
        comboJson.name = this.findname;
        this.comboJsonArray.push(comboJson);

        var store =  Ext.create('Ext.data.Store', {
            fields: [
                {type: 'string', name: 'name'}
            ],
            data: this.comboJsonArray
        });
        return store;
    },

    onKeyDown: function(textarea, e, eOpts){
        if(e.ctrlKey && e.altKey && (e.getCharCode() == Ext.EventObject.F)){
            console.log('Ctrl+Alt+F key down');
            this.onFindClick();
        }
    },
});