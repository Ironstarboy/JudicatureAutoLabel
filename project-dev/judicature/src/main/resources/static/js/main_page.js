function function1(x) {
    var str = $('#person').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#person').val(str);//显示 
}
function function2() {
    document.getElementById("3cases").src = "details.html"
}
function function3() {
    document.getElementById("3cases").src = "fenci.html"
}
function function4() {
    document.getElementById("3cases").src = ""
}
function function5(x) {
    var str = $('#sex').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#sex').val(str);//显示 
}
function function6(x) {
    var str = $('#nation').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#nation').val(str);//显示 
}
function function7(x) {
    var str = $('#hometown').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#hometown').val(str);//显示 
}
function function8(x) {
    var str = $('#cause').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#cause').val(str);//显示 
}
function function9(x) {
    var str = $('#court').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "," + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#court').val(str);//显示 
}