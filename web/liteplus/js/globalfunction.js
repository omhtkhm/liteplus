function getLineNumber(tArea) {

    return tArea.value.substr(0, tArea.selectionStart).split("\n").length;
}

function getCursorPos() {
    var me = $("textarea[name='documenttext']")[0];
    var el = $(me).get(0);
    var pos = 0;
    if ('selectionStart' in el) {
        pos = el.selectionStart;
    } else if ('selection' in document) {
        el.focus();
        var Sel = document.selection.createRange();
        var SelLength = document.selection.createRange().text.length;
        Sel.moveStart('character', -el.value.length);
        pos = Sel.text.length - SelLength;
    }
    var ret = pos - prevLine(me);
    alert(ret);

    return ret; 
}

function prevLine(me) {
    var lineArr = me.value.substr(0, me.selectionStart).split("\n");

    var numChars = 0;

    for (var i = 0; i < lineArr.length-1; i++) {
        numChars += lineArr[i].length+1;
    }

    return numChars;
}