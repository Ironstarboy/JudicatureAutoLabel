const jsdom = require("jsdom");
const {JSDOM} = jsdom;
const {window} = new JSDOM('<!doctype html><html><body></body></html>').window;
const $ = require('jQuery')(window);
document = window.document;
XMLHttpRequest = window.XMLHttpRequest;

function cipher() {
    var date = new Date();
    var timestamp = date.getTime().toString();
    var salt = $.WebSite.random(24);
    var year = date.getFullYear().toString();
    var month = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date
        .getMonth()).toString();
    var day = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate())
        .toString();
    var iv = year + month + day;
    var enc = DES3.encrypt(timestamp, salt, iv).toString();
    var str = salt + iv + enc;
    var ciphertext = strTobinary(str);
    console.log(ciphertext)
    return ciphertext;
}

function strTobinary(str) {
    var result = [];
    var list = str.split("");
    for (var i = 0; i < list.length; i++) {
        if (i != 0) {
            result.push(" ");
        }
        var item = list[i];
        var binaryStr = item.charCodeAt().toString(2);
        result.push(binaryStr);
    }
    ;
    return result.join("");
}

console.log(cipher())