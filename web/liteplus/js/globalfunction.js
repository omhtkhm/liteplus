//function getLineNumber(tArea) {
//
//    return tArea.value.substr(0, tArea.selectionStart).split("\n").length;
//}
//
//function getCursorPos() {
//    var me = $("textarea[name='documenttext']")[0];
//    var el = $(me).get(0);
//    var pos = 0;
//    if ('selectionStart' in el) {
//        pos = el.selectionStart;
//    } else if ('selection' in document) {
//        el.focus();
//        var Sel = document.selection.createRange();
//        var SelLength = document.selection.createRange().text.length;
//        Sel.moveStart('character', -el.value.length);
//        pos = Sel.text.length - SelLength;
//    }
//    var ret = pos - prevLine(me);
//    alert(ret);
//
//    return ret;
//}
//
//function prevLine(me) {
//    var lineArr = me.value.substr(0, me.selectionStart).split("\n");
//
//    var numChars = 0;
//
//    for (var i = 0; i < lineArr.length-1; i++) {
//        numChars += lineArr[i].length+1;
//    }
//
//    return numChars;
//}

jQuery.fn.extend({
    setCursorPosition: function(position){
        if(this.length == 0) return this;
        return $(this).setSelection(position, position);
    },

    setSelection: function(selectionStart, selectionEnd) {
        if(this.length == 0) return this;
        input = this[0];

        if (input.createTextRange) {
            var range = input.createTextRange();
            range.collapse(true);
            range.moveEnd('character', selectionEnd);
            range.moveStart('character', selectionStart);
            range.select();
        } else if (input.setSelectionRange) {
            input.focus();
            input.setSelectionRange(selectionStart, selectionEnd);
        }

        return this;
    },

    focusEnd: function(){
        this.setCursorPosition(this.val().length);
        return this;
    },

    getCursorPosition: function() {
        var el = $(this).get(0);
        var pos = 0;
        if('selectionStart' in el) {
            pos = el.selectionStart;
        } else if('selection' in document) {
            el.focus();
            var Sel = document.selection.createRange();
            var SelLength = document.selection.createRange().text.length;
            Sel.moveStart('character', -el.value.length);
            pos = Sel.text.length - SelLength;
        }
        return pos;
    },

    insertAtCursor: function(myValue) {
        return this.each(function(i) {
            if (document.selection) {
                //For browsers like Internet Explorer
                this.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                this.focus();
            }
            else if (this.selectionStart || this.selectionStart == '0') {
                //For browsers like Firefox and Webkit based
                var startPos = this.selectionStart;
                var endPos = this.selectionEnd;
                var scrollTop = this.scrollTop;
                this.value = this.value.substring(0, startPos) + myValue +
                    this.value.substring(endPos,this.value.length);
                this.focus();
                this.selectionStart = startPos + myValue.length;
                this.selectionEnd = startPos + myValue.length;
                this.scrollTop = scrollTop;
            } else {
                this.value += myValue;
                this.focus();
            }
        })
    },
    //getXYFromIndex: function(textareaEl, index){
    //    var textLines = textareaEl.value.substr(0, textareaEl.selectionStart).split("\n");
    //    var currentLineNumber = textLines.length;
    //    var currentColumnIndex = textLines[textLines.length-1].length+1;
    //    var nLineCol = new Object();
    //    nLineCol.line = currentLineNumber;
    //    nLineCol.col = currentColumnIndex;
    //    return nLineCol;
    //},
    //getLineNumber : function(){
    //    var el = $(this).get(0);
    //    return el.value.substr(0, el.selectionStart).split("\n").length;
    //}
});