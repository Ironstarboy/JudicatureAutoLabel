function function1(x) {
    var str = $('#criminals_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#criminal_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#criminals_text').val(str);//显示
}
function function4() {
    document.getElementById("3cases").src = "法律依据.html"
}
function function5(x) {
    var str = $('#gender_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#gender_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#gender_text').val(str);//显示
}
function function6(x) {
    var str = $('#ethnicity_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#ethnicity_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#ethnicity_text').val(str);//显示
}
function function7(x) {
    var str = $('#birthplace_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#birthplace_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#birthplace_text').val(str);//显示
}
function function8(x) {
    var str = $('#accusation_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#accusation_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#accusation_text').val(str);//显示
}
function function9(x) {
    var str = $('#courts_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#courts_" + x).is(':checked')) {//选中时，看是否已有数据
        if (str.indexOf(x) == -1) {//如果不含该串
            if (str.length != 0) {
                str += "，" + x;
            } else {
                str += x;//拼接当前值val
            }
        }
    } else {//取消复选框时，str有且仅有一个x，删除即可
        if (str == x) {//若只有这一个字符串
            str = null
        } else {
            if (str.startsWith(x)) {//如果在开头
                str = str.replace(x + "，", '')
            } else {
                str = str.replace("，" + x, '')
            }
        }
    }
    $('#courts_text').val(str);//显示
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
function init_criminals(keys) {
    var criminals_str = ''
    for (var i in keys) {
        criminals_str += `
        <label for="criminal_${keys[i]}"><input id="criminal_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function1(this.value)"/>${keys[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    criminals_str = `<div role = "tabpanel" class="tab-pane fade in active" id = "criminals" aria - labelledby="criminals-tab">
        <p>` +
        criminals_str +
        `</p></div>`
    console.log(keys)
    document.querySelector('#criminals').innerHTML = criminals_str
}
function init_gender(keys) {
    var gender_str = ''
    for (var i in keys) {
        gender_str +=
            `<label for="gender_${keys[i]}"><input id="gender_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function5(this.value)"/>${keys[i]}</label>`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    gender_str = `<div role = "tabpanel" class="tab-pane fade in" id = "gender" aria - labelledby="gender-tab">
        <p>` +
        gender_str +
        `</p></div>`
    document.querySelector('#gender').innerHTML = gender_str
}
function init_ethnicity(keys) {
    var ethnicity_str = ''
    for (var i in keys) {
        ethnicity_str += `
        <label for="ethnicity_${keys[i]}"><input id="ethnicity_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function6(this.value)"/>${keys[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    ethnicity_str = `<div role = "tabpanel" class="tab-pane fade in" id = "ethnicity" aria - labelledby="ethnicity-tab">
        <p>` +
        ethnicity_str +
        `</p></div>`
    document.querySelector('#ethnicity').innerHTML = ethnicity_str
}
function init_birthplace(keys) {
    var birthplace_str = ''
    for (var i in keys) {
        birthplace_str += `
        <label for="birthplace_${keys[i]}"><input id="birthplace_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function7(this.value)"/>${keys[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    birthplace_str = `<div role = "tabpanel" class="tab-pane fade in" id = "birthplace" aria - labelledby="birthplace-tab">
        <p>` +
        birthplace_str +
        `</p></div>`
    document.querySelector('#birthplace').innerHTML = birthplace_str
}

function init_accusation(keys) {
    var accusation_str = ''
    for (var i in keys) {
        accusation_str += `
        <label for="accusation_${keys[i]}"><input id="accusation_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function8(this.value)"/>${keys[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    accusation_str = `<div role = "tabpanel" class="tab-pane fade in" id = "accusation" aria - labelledby="accusation-tab">
        <p>` +
        accusation_str +
        `</p></div>`
    document.querySelector('#accusation').innerHTML = accusation_str

}
function init_courts(keys) {
    var courts_str = ''
    for (var i in keys) {
        courts_str += `
        <label for="courts_${keys[i]}"><input id="courts_${keys[i]}" type="checkbox" value="${keys[i]}" onclick="function9(this.value)"/>${keys[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    courts_str = `<div role = "tabpanel" class="tab-pane fade in" id = "courts" aria - labelledby="courts-tab">
        <p>` +
        courts_str +
        `</p></div>`
    document.querySelector('#courts').innerHTML = courts_str
}
function info_courts(keys) {
    var courts_str = ''
    for (var i in keys) {
        courts_str += "&nbsp" + `${keys[i]}`
    }
    document.querySelector('#info_courts').innerHTML = courts_str
}
function info_type(keys) {
    var type_str = ''
    for (var i in keys) {
        type_str += "&nbsp" + `${keys[i]}`
    }
    document.querySelector('#info_type').innerHTML = type_str
}
function info_accusation(keys) {
    var accusation_str = ''
    for (var i in keys) {
        accusation_str += "&nbsp" + `${keys[i]}`
    }
    document.querySelector('#info_accusation').innerHTML = accusation_str
}
function info_category(keys) {
    var category_str = ''
    for (var i in keys) {
        category_str += `&nbsp${keys[i]}`
    }
    document.querySelector('#info_category').innerHTML = category_str
}
function info_date(keys) {
    var date_str = ''
    for (var i in keys) {
        date_str += `${keys[i]}`
    }
    document.querySelector('#info_date').innerHTML = date_str
}
function info_caseno(keys) {
    var caseno_str = ''
    for (var i in keys) {
        caseno_str += `${keys[i]}`
    }
    document.querySelector('#info_caseno').innerHTML = caseno_str
}