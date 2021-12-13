function function1(x) {
    var str = $('#person').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#person').val(str);//显示 
}
function function2() {
    document.getElementById("3cases").src = "基本信息展示页.html"
}
function function3() {
    document.getElementById("3cases").src = "分词页.html"
}
function function4() {
    document.getElementById("3cases").src = "法律依据.html"
}
function function5(x) {
    var str = $('#sex').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#sex').val(str);//显示 
}
function function6(x) {
    var str = $('#nation').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#nation').val(str);//显示 
}
function function7(x) {
    var str = $('#hometown').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#hometown').val(str);//显示 
}
function function8(x) {
    var str = $('#cause').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#cause').val(str);//显示 
}
function function9(x) {
    var str = $('#court').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if (str.length != 0) {
        str += "，" + x;
    } else {
        str += x;//拼接当前值val
    }
    $('#court').val(str);//显示 
}
function function10() {
    document.getElementById("detail").src = "基本信息树图.html"
}
function function11() {
    document.getElementById("graphs").src = "echarts/2012-2019结案数量统计图.html"
}
function function12() {
    document.getElementById("graphs").src = "echarts/2019男女犯罪比例图.html"
}
function function13() {
    document.getElementById("graphs").src = "echarts/2017-2019五种罪行罪犯人数变化图.html"
}
function function14() {
    document.getElementById("cases").src = "高频刑事案件.html"
}
function function15() {
    document.getElementById("cases").src = "高频民事案件.html"
}
function function16() {
    document.getElementById("lists").src = "高频宪法.html"
}
function function17() {
    document.getElementById("lists").src = "高频民事.html"
}