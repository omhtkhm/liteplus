/**
 * Created by hun on 2016-09-27.
 */
Ext.define('Plus.controller.SaveOpen',{
    extend: 'Ext.app.Controller',
    alias: 'controller.saveopen',
    views: ['westtoolbar.westToolBarSub01Class','westtoolbar.westToolBarSub02Class','popup.Save','popup.Load'],

    //savepopup: '',
    //loadpopup: '',
    //insert: false,
    //filename: '1';

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
            tempText: this.filename,
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
    }
});