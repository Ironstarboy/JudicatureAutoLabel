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
    var str1 = `<b>`+"高频刑事相关法律依据"+`</b>`;
    document.querySelector('#fre1').innerHTML = str1
    var str2 = `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=39c1b78830b970eabdfb&keyword=%e5%88%91%e6%b3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国刑法(2020修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=5a06769be1274052bdfb&keyword=%e5%88%91%e4%ba%8b%e8%af%89%e8%ae%bc%e6%b3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国刑事诉讼法(2018修正)"+`</a><br>`;
    document.querySelector('#fre2').innerHTML = str2
}
function function15() {
    var str1 = `<b>`+"高频经济相关法律依据"+`</b>`;
    document.querySelector('#fre1').innerHTML = str1
    var str2 = `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=a347c82e6a7d13aabdfb&keyword=%e3%80%8a%e4%b8%ad%e5%8d%8e%e4%ba%ba%e6%b0%91%e5%85%b1%e5%92%8c%e5%9b%bd%e6%b6%88%e8%b4%b9%e8%80%85%e6%9d%83%e7%9b%8a%e4%bf%9d%e6%8a%a4%e6%b3%95%e3%80%8b&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国消费者权益保护法(2013修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=6393f2e43412bddbbdfb&keyword=%e5%8a%b3%e5%8a%a8%e6%b3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国劳动法(2018修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=4e5a046905fd9240bdfb&keyword=%e5%9c%9f%e5%9c%b0%e7%ae%a1%e7%90%86%e6%b3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国土地管理法(2019修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=503218397088141cbdfb&keyword=%e5%8f%8d%e5%9e%84%e6%96%ad&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国反垄断法"+`</a><br>`;
    ;
    document.querySelector('#fre2').innerHTML = str2
}
function function16() {
    var str1 = `<b>`+"宪法"+`</b>`;
    document.querySelector('#fre1').innerHTML = str1
    var str2 = `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=7c7e81f43957c58bbdfb&keyword=%E5%AE%AA%E6%B3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国宪法(2018修正)"+`</a><br>`
    document.querySelector('#fre2').innerHTML = str2
}
function function17() {
    var str1 = `<b>`+"高频民事相关法律依据"+`</b>`;
    document.querySelector('#fre1').innerHTML = str1
    var str2 = `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=3ce82cb92ee006b6bdfb&keyword=%e3%80%8a%e4%b8%ad%e5%8d%8e%e4%ba%ba%e6%b0%91%e5%85%b1%e5%92%8c%e5%9b%bd%e6%b0%91%e4%ba%8b%e8%af%89%e8%ae%bc%e6%b3%95%e3%80%8b&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"《中华人民共和国民事诉讼法》（2021修正）"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=12e67f26d4b80606bdfb&keyword=%e6%9c%80%e9%ab%98%e4%ba%ba%e6%b0%91%e6%b3%95%e9%99%a2%e5%85%b3%e4%ba%8e%e9%80%82%e7%94%a8%e3%80%8a%e4%b8%ad%e5%8d%8e%e4%ba%ba%e6%b0%91%e5%85%b1%e5%92%8c%e5%9b%bd%e6%b0%91%e4%ba%8b&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"最高人民法院关于适用《中华人民共和国民事诉讼法》的解释（2020修正）"+`</a><br>`;
    document.querySelector('#fre2').innerHTML = str2
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
function init_else(keys1,keys2) {
    keys1 = keys1.split(",")
    keys2 = keys2.split(",")
    var else_str = "动词："
    for (var i in keys1) {
        else_str += `
        <label for="else_${keys1[i]}"><input id="else_${keys1[i]}" type="checkbox" value="${keys1[i]}" onclick="function24(this.value)"/>${keys1[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    else_str += `<br>` + "形容词："
    for (var i in keys2) {
        else_str += `
        <label for="else_${keys2[i]}"><input id="else_${keys2[i]}" type="checkbox" value="${keys2[i]}" onclick="function24(this.value)"/>${keys2[i]}</label>&nbsp;&nbsp;&nbsp;&nbsp;`
    }
    //拼接完字符串数组后用innerHTML把它渲染到父元素中
    else_str = `<div role = "tabpanel" class="tab-pane fade in" id = "else" aria - labelledby="else-tab">
        <p>` +
        else_str +
        `</p></div>`
    document.querySelector('#else').innerHTML = else_str
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
function function18(){
    document.getElementById("graphs").src = "echarts/2017-2021文书数量排行前十名.html"
}
function function19(){
    document.getElementById("two2").classList.remove("active");
    document.getElementById("three2").classList.remove("active");
    document.getElementById("four2").classList.remove("active");
    document.getElementById("one2").classList.add("active");
    var str = `<h2>` + "第五十四条" + `</h2><p>` + "人民法院公开审理行政案件，但涉及国家秘密、个人隐私和法律另有规定的除外。涉及商业秘密的案件，当事人申请不公开审理的，可以不公开审理。"+`</p>`;
    document.querySelector('#list2').innerHTML = str;
}
function function20(){
    document.getElementById("one2").classList.remove("active");
    document.getElementById("three2").classList.remove("active");
    document.getElementById("four2").classList.remove("active");
    document.getElementById("two2").classList.add("active");
    var str = `<h2>` + "第六十一条" + `</h2><p>` + "在涉及行政许可、登记、征收、征用和行政机关对民事争议所作的裁决的行政诉讼中，当事人申请一并解决相关民事争议的，人民法院可以一并审理。在行政诉讼中，人民法院认为行政案件的审理需以民事诉讼的裁判为依据的，可以裁定中止行政诉讼。" + `</p>`;
    document.querySelector('#list2').innerHTML = str;
}
function function21(){
    document.getElementById("one2").classList.remove("active");
    document.getElementById("two2").classList.remove("active");
    document.getElementById("four2").classList.remove("active");
    document.getElementById("three2").classList.add("active");
    var str = `<h2>` + "第八十九条" + `</h2><p>` + "人民法院审理上诉案件，按照下列情形，分别处理："+`<br>` +
        "（一）原判决、裁定认定事实清楚，适用法律、法规正确的，判决或者裁定驳回上诉，维持原判决、裁定；"+`<br>` +
        "（二）原判决、裁定认定事实错误或者适用法律、法规错误的，依法改判、撤销或者变更；"+`<br>` +
        "（三）原判决认定基本事实不清、证据不足的，发回原审人民法院重审，或者查清事实后改判；"+`<br>` +
        "（四）原判决遗漏当事人或者违法缺席判决等严重违反法定程序的，裁定撤销原判决，发回原审人民法院重审。"+`<br>` +
        "原审人民法院对发回重审的案件作出判决后，当事人提起上诉的，第二审人民法院不得再次发回重审。"+`<br>` +
        "人民法院审理上诉案件，需要改变原审判决的，应当同时对被诉行政行为作出判决。" + `</p>`;
    document.querySelector('#list2').innerHTML = str;
}function function22(){
    document.getElementById("one2").classList.remove("active");
    document.getElementById("three2").classList.remove("active");
    document.getElementById("two2").classList.remove("active");
    document.getElementById("four2").classList.add("active");
    var str = `<h2>` + "第十条" + `</h2><p>` +"【禁止作为商标使用的文字、图形】下列标志不得作为商标使用："+`<br>`+
        "（一）同中华人民共和国的国家名称、国旗、国徽、军旗、勋章相同或者近似的，以及同中央国家机关所在地特定地点的名称或者标志性建筑物的名称、图形相同的；"+`<br>`+
        "（二）同外国的国家名称、国旗、国徽、军旗相同或者近似的，但该国政府同意的除外；"+`<br>`+
        "（三）同政府间国际组织的名称、旗帜、徽记相同或者近似的，但经该组织同意或者不易误导公众的除外；"+`<br>`+
        "（四）与表明实施控制、予以保证的官方标志、检验印记相同或者近似的，但经授权的除外；"+`<br>`+
        "（五）同“红十字”、“红新月”的名称、标志相同或者近似的；"+`<br>` +
        "（六）带有民族歧视性的；"+`<br>` +
        "（七）夸大宣传并带有欺骗性的；"+`<br>`+
        "（八）有害于社会主义道德风尚或者有其他不良影响的。"+`<br>`+
　　     "县级以上行政区划的地名或者公众知晓的外国地名，不得作为商标。但是，地名具有其他含义或者作为集体商标、证明商标组成部分的除外；已经注册的使用地名的商标继续有效。" + `</p>`;
    document.querySelector('#list2').innerHTML = str;
}
function function23() {
    var str1 = `<b>`+"高频行政相关法律依据"+`</b>`;
    document.querySelector('#fre1').innerHTML = str1
    var str2 = `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=76c54b08f88ee7efbdfb&keyword=%e8%a1%8c%e6%94%bf%e8%af%89%e8%ae%bc&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国行政诉讼法(2017修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=6add260f579ab541bdfb&keyword=%e8%a1%8c%e6%94%bf%e5%a4%84%e7%bd%9a&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国行政处罚法(2021修订)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=497d709c9225b1e3bdfb&keyword=%e8%a1%8c%e6%94%bf%e5%a4%8d%e8%ae%ae&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国行政复议法(2017修正)"+`</a><br>`+
        `<a href="http://www.pkulaw.cn/fulltext_form.aspx?Db=chl&Gid=c8054098b4b25952bdfb&keyword=%e5%85%ac%e5%8a%a1%e5%91%98%e6%b3%95&EncodingName=&Search_Mode=accurate&Search_IsTitle=0">`+"中华人民共和国公务员法(2018修订)"+`</a><br>`;
    document.querySelector('#fre2').innerHTML = str2
}
function function24(x) {
    var str = $('#else_text').val();//先获取用显示div的内容，然后把当前复选框内容拼接，再把拼接内容在show中
    if ($("#else_" + x).is(':checked')) {//选中时，看是否已有数据
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
    $('#else_text').val(str);//显示
}