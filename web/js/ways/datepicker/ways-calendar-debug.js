define(function(require) {

    var jQuery = require('jquery-debug');
    require('libs/ways/datepicker/ways-calendar-debug.css');

    /*
     * Summary: calendar1.0
     * Author: xiaoyang
     * Date: 2013
     * Emial: luozhaoyang@way-s.cn
     */

    jQuery.fn.waysDatepicker = function(options) {
        //var c = options || {};
        return $(this).each(function() {
            var that = this;
            var n = $(this),c = options || {},templateList,$calendar,$calendarYearTitle,
                $calendarMonthTitle,$calendarHour,$calendarMinute,$calendarSecond,datetime = {},enMonths;
            // data: date   //时间源
            // template : "day" //显示模板
            // language : "zh_CN"  //语言
            // callback: (datetime){}//回调函数
            // hasTime : true
            c = $.extend({
                // controlId: "calendar",
                speed: 200,
                complement: true,
                readonly: true,
                upperLimit: NaN,
                lowerLimit: NaN,
                language:"zh_CN",
                hasTime:false,
                callback: function(data) {}
            },c);

            if (c.readonly) {
                n.attr("readonly", true);
                n.bind("keydown",
                    function(event) {
                        if (event.keyCode == 8) event.keyCode = 0
                    })
            }
            //定义当前时间
            var nowDate = new Date;
            var nowYear = nowDate.getFullYear(),
                nowMonth = nowDate.getMonth(),
                nowDay = nowDate.getDate(),
                nowWeek = nowDate.getDay(),
                k = "";
            //初始化时间选择框
            enMonths = ['Jan','Feb','Mar','Apr','May','June','July','Aug','Sept','Oct','Nov','Dec'];
            if(c.language == "en"){
                nowMonth = enMonths[Number(nowMonth)];
            }
            k += "<div class='calendar'>";
            k += "  <div class='calMain'>";
            k += "    <div class='calHead'>";
            k += "      <div class='calTitle'>";
            k += "      <a class='prevMonth'></a><span class='t_date'>";
            if(c.language == "zh_CN"){
                if(c.template == "year"){
                    k += "<span class='currentYearText'><a class='currentYear onlyYear'>" + nowYear + "</a>\u5e74</span>";
                }
                else{
                    k += "<span class='currentYearText'><a class='currentYear'>" + nowYear + "</a>\u5e74</span><span class='currentMonthText'><a class='currentMonth'>" + (nowMonth + 1) + "</a>\u6708</span>";
                }
            }else if(c.language == "en"){
                if(c.template == "year"){
                    k += "<span class='currentYearText'><a class='currentYear onlyYear'>" + nowYear + "</a></span>";
                }
                else{
                    k += "<span class='currentMonthText'><a class='currentMonth'>" + (nowMonth + 1) + "</a></span><span class='currentYearText'><a class='currentYear'>" + nowYear + "</a></span>";
                }
            }

            k += "       </span><a class='nextMonth'></a>";
            k += "    </div></div>";
            k += "    <div class='calContent'>";
            k += "      <div class='reserve'>";
            k += "      </div>";
            k += "      <div class='enabled'>";
            k += "      </div>";
            k += "    </div>";
            k += "  </div>";
            if(c.hasTime && c.template == "day"){
                k += "<div class='calTime'>";
                k += "<div class='selectTime'>";
                k += "<span class='timeTitle'>时间</span>";
                k += "<input type='text' value='' class='currentHour' /><input type='text' value='' class='currentMinute'/><input type='text' value='' class='currentSecond'/>";
                k += "</div>";
                k += "<div class='calFooter'><a class='submit'>确定</a></div>";
                k += "</div>";
            }
            k += "</div>";


            /* 将文本框与控件包起来，防止在页面有固定定位的时候无法正常显示 */
            n.wrapAll('<div class="calendar-container"></div>');
            n.addClass("waysdatepicker");
            n.after(k);
            $calendar = n.siblings(".calendar");
            $calendarYearTitle = $calendar.find(".currentYear");
            $calendarMonthTitle = $calendar.find(".currentMonth");
            $calendarHour = $calendar.find(".currentHour");
            $calendarMinute = $calendar.find(".currentMinute");
            $calendarSecond = $calendar.find(".currentSecond");
            //加载模板
            function template(){
                var currentDate;
                if(c.template == "month"){
                    if(!n.val()){
                        templateList = mList(nowYear);
                        n.val(nowYear+"-"+(nowDate.getMonth()+1));
                    }else{
                        currentDate = n.val().split("-");
                        templateList = mList(currentDate[0]);
                    }
                }else if(c.template == "year"){
                    if(!n.val()){
                        templateList = yList(nowYear);
                        n.val(nowYear);
                    }else{
                        templateList = yList(n.val());
                    }
                }
                else{
                    if(!n.val()){
                        n.val(nowYear+"-"+(nowDate.getMonth()+1)+"-"+nowDay);
                        templateList = dList(nowYear, nowDate.getMonth());
                    }else{
                        currentDate = n.val().split("-");
                        templateList = dList(currentDate[0], currentDate[1]-1);
                    }
                    if(c.hasTime){
                        var today = new Date,
                            nowHour = today.getHours(),
                            nowMinute = today.getMinutes(),
                            nowSecond = today.getSeconds();
                        $calendarHour.val(nowHour);
                        $calendarMinute.val(nowMinute);
                        $calendarSecond.val(nowSecond);
                    }
                }
                $calendar.find(".enabled").html(templateList);

                if (n.val()) {
                    var datetime = {},
                        val = n.val().split('-')
                    if (val.length > 0) {
                        datetime.year = val[0];
                    }
                    if (val.length > 1) {
                        datetime.month = val[1];
                    }
                    if (val.length > 2) {
                        datetime.day = val[2];
                    }
                    datetime.date = n.val();
                    n.data("datetime",datetime);
                }
            }

            //$("body").append(k);
            //n.after(k);
            template();
            //根据选择时间标题时间
            function default_title(){
                //var currentDate = n.val().replace(/年|月|日/g, "/");
                var currentYear,currentMonth;
                if(c.template == "year"){
                    currentYear = n.val();
                    if(currentYear !=""){
                        $calendarYearTitle.text(currentYear);
                    }else{

                        $calendarYearTitle.text(nowYear);
                    }
                }
                else{
                    var currentDate = n.val().split("-");
                    currentYear = currentDate[0];
                    currentMonth = currentDate[1];
                    if(c.language == "en"){
                        currentMonth = enMonths[currentMonth-1];
                    }
                    if(currentYear !="" && currentMonth != ""){
                        $calendarYearTitle.text(currentYear);
                        $calendarMonthTitle.text(currentMonth);
                    }else{
                        $calendarYearTitle.text(nowYear);
                        $calendarMonthTitle.text(nowMonth);
                    }

                }
            }
            //默认显示选中时间
            function default_item(){
                var currentYear,currentMonth,currentDay;
                $calendar.find(".tabY a").removeClass("select");
                $calendar.find(".tabM a").removeClass("select");
                $calendar.find(".tabD a").removeClass("select");
                if(c.template == "year"){
                    if(n.val()){
                        currentYear = n.val();
                    }else{
                        currentYear = nowYear;
                    }
                    $calendar.find(".tabY a[val="+currentYear+"]").addClass("select");
                }
                else{
                    var titleYear = $calendarYearTitle.text();
                    var titleMonth = $calendarMonthTitle.text();
                    var currentDate = n.val().split("-");
                    if(n.val()){
                        currentYear = currentDate[0];
                        currentMonth = currentDate[1]-1;
                        currentDay = currentDate[2];
                    }else{
                        currentYear = nowYear;
                        currentMonth = nowMonth-1;
                        currentDay = nowDay;
                    }

                    $calendar.find(".tabY a[val="+currentYear+"]").addClass("select");
                    if(c.template =="month"){
                        if(currentYear == titleYear){
                            $calendar.find(".tabM a[val="+currentMonth+"]").addClass("select");
                        }
                    }
                    else{
                        if(currentYear == titleYear){
                            $calendar.find(".tabM a[val="+currentMonth+"]").addClass("select");
                        }
                        if(c.language == "en"){
                            $.each(enMonths,function(key,val){
                                if(key == currentMonth){
                                    currentMonth = val;
                                }
                            });
                            if(currentYear == titleYear && currentMonth == titleMonth){
                                $calendar.find(".tabD a:contains('" + currentDay + "')").each(function() {
                                    currentDay == $(this).text() && !$(this).hasClass("prevD") && !$(this).hasClass("nextD") && $(this).addClass("select")
                                });
                            }
                        }else{
                            if(currentYear == titleYear && currentMonth+1 == titleMonth){
                                $calendar.find(".tabD a:contains('" + currentDay + "')").each(function() {
                                    currentDay == $(this).text() && !$(this).hasClass("prevD") && !$(this).hasClass("nextD") && $(this).addClass("select")
                                });
                            }
                        }
                    }


                }

            }
            //日列表
            function dList(a, b) {
                var newDate = new Date(a, b, 1);
                newDate.setDate(0);
                var d = 1,
                    h = newDate.getDate();
                newDate.setDate(1);
                newDate.setMonth(newDate.getMonth() + 1);
                var m = newDate.getDay();
                if (m == 0) m = 7;
                h = h - m + 1;
                newDate.setMonth(newDate.getMonth() + 1);
                newDate.setDate(0);
                var o = newDate.getDate(),
                    g = "<table class='tabD'>";
                g += "<tr><th>\u65e5</th><th>\u4e00</th><th>\u4e8c</th><th>\u4e09</th><th>\u56db</th><th>\u4e94</th><th>\u516d</th></tr>";
                var i = new Date,
                    l = "",
                    p = "",
                    e = i.getFullYear(),
                    f = i.getMonth(),
                    q = i.getDate(),
                    t = "";
                c.complement || (t = "style='display:none'");
                for (var x = 0; x < 6; x++) {
                    g += "<tr>";
                    for (var y = 0; y < 7; y++) {
                        var j = x * 7 + y + 1 - m;
                        p = l = "";
                        if (c.lowerLimit != NaN && c.lowerLimit > new Date(newDate.getFullYear(), newDate.getMonth(), j) || c.upperLimit != NaN && new Date(newDate.getFullYear(), newDate.getMonth(), j) > c.upperLimit) if (0 < j && j <= o) {
                            if (newDate.getFullYear() == e && newDate.getMonth() == f && j == q) l = "current";
                            g += "<td><span class='" + l + "'>" + j + "</span></td>"
                        } else if (j <= 0) {
                            if (newDate.getFullYear() == e && newDate.getMonth() - 1 == f && h == q) l = "current";
                            g += "<td><span class='" + l + "' " + t + ">" + h + "</span></td>";
                            h++
                        } else {
                            if (j > o) {
                                if (newDate.getFullYear() == e && newDate.getMonth() + 1 == f && d == q) l = "current";
                                g += "<td><span class='" + l + "' " + t + ">" + d + "</span></td>";
                                d++
                            }
                        } else if (0 < j && j <= o) {
                            if (newDate.getFullYear() == e && newDate.getMonth() == f && j == q) l = "current";
                            if (newDate.getFullYear() == i.getFullYear() && newDate.getMonth() == i.getMonth() && j == i.getDate()) p = "select";
                            g += "<td><a class='" + p + " " + l + "'>" + j + "</a></td>"
                        } else if (j <= 0) {
                            if (newDate.getFullYear() == e && newDate.getMonth() - 1 == f && h == q) l = "current";
                            if (newDate.getFullYear() == i.getFullYear() && newDate.getMonth() - 1 == i.getMonth() && h == i.getDate()) p = "select";
                            g += "<td><a class='prevD " + p + " " + l + "' " + t + ">" + h + "</a></td>";
                            h++
                        } else if (j > o) {
                            if (newDate.getFullYear() == e && newDate.getMonth() + 1 == f && d == q) l = "current";
                            if (newDate.getFullYear() == i.getFullYear() && newDate.getMonth() + 1 == i.getMonth() && d == i.getDate()) p = "select";
                            g += "<td><a class='nextD " + p + " " + l + "' " + t + ">" + d + "</a></td>";
                            d++
                        }
                        g = g.replace("class=' '", "")
                    }
                    g += "</tr>"
                }
                g += "</table>";
                $calendar.find('.calMain').height(173);
                return g
            }
            //月份列表
            function mList(a) {
                var texts = ['\u4e00\u6708','\u4e8c\u6708','\u4e09\u6708','\u56db\u6708','\u4e94\u6708','\u516d\u6708','\u4e03\u6708','\u516b\u6708','\u4e5d\u6708','\u5341\u6708','\u5341\u4e00\u6708','\u5341\u4e8c\u6708'];
                if(c.language =="en"){
                    texts = enMonths;
                }else if(c.language == "zh_CN"){
                    texts = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
                }

                var mList = "<table class='tabM'>";
                var currentYear;

                if(n.val()){
                    currentYear = a;
                }
                else{
                    currentYear =nowYear;
                }
                //var L = texts.length;
                for(var i = 0; i < 3; i++){
                    mList += "<tr>";
                    for(var j = i*4; j < (i+1)*4; j++){
                        var monthName = texts[j];
                        if (c.data) {
                            if (c.data[currentYear]) {
                                var flag = true
                                $.each(c.data[currentYear],function(index,val){
                                    if (val-1 == j) {
                                        mList += "<td><a val='"+ j +"'>"+monthName+"</a></td>";
                                        flag = false;
                                        return false;
                                    }
                                });
                                if (flag) {
                                    mList += "<td><span>"+monthName+"</span></td>";
                                }
                            }else{
                                mList += "<td><span>"+monthName+"</span></td>";
                            }
                        } else {
                            if(c.lowerLimit && c.lowerLimit.getMonth() > j && c.lowerLimit.getFullYear() == currentYear){
                                mList += "<td><span>"+monthName+"</span></td>";
                            }else if(c.upperLimit && c.upperLimit.getMonth() < j && c.upperLimit.getFullYear() == currentYear){
                                mList += "<td><span>"+monthName+"</span></td>";
                            }else if(c.upperLimit && c.upperLimit.getFullYear() < currentYear){
                                mList += "<td><span>"+monthName+"</span></td>";
                            }else if(c.lowerLimit && c.lowerLimit.getFullYear() > currentYear){
                                mList += "<td><span>"+monthName+"</span></td>";
                            }
                            else{
                                mList += "<td><a val='"+j+"'>"+monthName+"</a></td>";
                            }
                        }
                    }
                    mList += "</tr>";
                }
                mList += "</table>";
                $calendar.find('.calMain').height(153);
                return mList;
            }
            //年份列表
            function yList(a) {
                var a = Math.floor(a / 10) * 10;
                var yList = "<table class='tabY'>",
                    o = "";
                c.complement || (o = "style='display:none'");
                for (var g = 0; g < 3; g++) {
                    yList += "<tr>";
                    for (var i = 0; i < 4; i++) {
                        if (g + 1 * i + 1 != 1 && (g + 1) * (i + 1) != 12) {
                            if (c.data) {
                                if (c.data[a]) {
                                    yList += "<td><a val='"+ a +"'>" + a + "</a></td>";
                                } else {
                                    yList += "<td><span>" + a + "</span></td>";
                                }
                            } else {
                                if(c.lowerLimit && c.lowerLimit.getFullYear() > a || c.upperLimit && c.upperLimit.getFullYear() < a){
                                    yList += "<td><span>" + a + "</span></td>";
                                }else{
                                    yList += "<td><a val='"+ a +"'>" + a + "</a></td>";
                                }
                            }
                            a++;
                        } else if (g + 1 * i + 1 == 1) {//第一行第一个年份等于a-1
                            if (c.data) {
                                if (c.data[a-1]) {
                                    yList += "<td><a val='"+ (a - 1) +"' " + o + ">" + (a - 1) + "</a></td>";
                                } else {
                                    yList += "<td><span " + o + ">" + (a - 1) + "</span></td>";
                                }
                            } else {
                                if(c.lowerLimit && c.lowerLimit.getFullYear() > a - 1 || c.upperLimit && c.upperLimit.getFullYear() < a - 1){
                                    yList += "<td><span " + o + ">" + (a - 1) + "</span></td>";
                                }else{
                                    yList += "<td><a val='"+ (a-1) +"' " + o + ">" + (a - 1) + "</a></td>";
                                }
                            }
                        } else {
                            if (c.data) {
                                if (c.data[a]) {
                                    yList += "<td><a val='"+ a +"' " + o + ">" + a + "</a></td>";
                                } else {
                                    yList += "<td><span" + o + ">" + a + "</span></td>";
                                }
                            } else {
                                if(c.lowerLimit && c.lowerLimit.getFullYear() > a || c.upperLimit && c.upperLimit.getFullYear() < a){
                                    yList += "<td><span " + o + ">" + a + "</span></td>";
                                }else{
                                    yList += "<td><a val='"+ a +"' " + o + ">" + a + "</a></td>";
                                }
                            }
                        }
                    }
                    yList += "</tr>"
                }
                yList += "</table>";
                $calendar.find('.calMain').height(153);
                return yList;
            }

            function fixNumber(num){
                return num;
                if(num > 9){
                    return num;
                }else{
                    return "0" + String(num)
                }
            }

            //日选择
            function selectDay() {
                $calendar.find(".tabD a").unbind("mouseup").mouseup(function() {
                    var currentYear,currentMonth,currentDay,currentDate;
                    currentYear = $calendarYearTitle.text();
                    currentMonth = $calendarMonthTitle.text();
                    if(c.language == "en"){
                        $.each(enMonths,function(key,val){
                            if(val == currentMonth){
                                currentMonth = key + 1;
                            }
                        });
                    }
                    if ($(this).hasClass("prevD")) {//当前选择上个月日期
                        if (Number(currentMonth) != 1) {
                            currentMonth = Number(currentMonth) - 1;
                        }
                        else {
                            currentMonth = 12;
                            currentYear = Number(currentYear) - 1;
                        }
                        var b = c.speed;
                        c.speed = 0;
                        $calendar.find(".prevMonth").triggerHandler("mouseup");
                        c.speed = b
                    } else if ($(this).hasClass("nextD")) {//当前选择下个月日期
                        if (Number(currentMonth) != 12) {
                            currentMonth = Number(currentMonth) + 1;
                        }
                        else {
                            currentMonth = 1;
                            currentYear = Number(currentYear) + 1;
                        }
                        b = c.speed;
                        c.speed = 0;
                        $calendar.find(".nextMonth").triggerHandler("mouseup");
                        c.speed = b
                    }
                    currentDay = $(this).text();
                    currentDate = currentYear  + "-" + fixNumber(Number(currentMonth))+ "-" + fixNumber(Number(currentDay));
                    n.val(currentDate);
                    if(c.hasTime){
                        $calendar.find(".tabD a").removeClass("select");
                        $(this).addClass("select");
                        $calendar.find(".submit").click(function(){
                            $calendar.hide();
                            var currentHour = $calendarHour.val(),
                                currentMinute = $calendarMinute.val(),
                                currentSecond = $calendarSecond.val();
                            datetime = {
                                year:currentYear,
                                month:currentMonth,
                                day:currentDay,
                                hour:currentHour,
                                minute:currentMinute,
                                second:currentSecond,
                                date:currentYear + "-" +  currentMonth + "-" +  currentDay + " " + currentHour +":" + currentMinute +":" +currentSecond
                            };
                            n.data("datetime",datetime);
                            c.callback.call(that, datetime);
                            n.trigger('change')
                        });
                    }else{
                        $calendar.hide();
                        datetime = {
                            year:currentYear,
                            month:currentMonth,
                            day:currentDay,
                            hour:"",
                            minute:"",
                            second:"",
                            date:currentYear + "-" +  currentMonth + "-" +  currentDay
                        };
                        n.data("datetime",datetime);
                        c.callback.call(that, datetime);
                        n.trigger('change')
                    }

                }).hover(function() {
                        $(this).addClass("hover")
                    },
                    function() {
                        $(this).removeClass("hover")
                    })
            }
            //月份选择
            function selectMonth() {
                $calendar.find(".tabM a").unbind("mouseup").mouseup(function() {
                    if(c.template == "month"){
                        var currentYear = $calendarYearTitle.text();
                        var currentMonth = Number($(this).attr("val"))+1;
                        var currentDate = $calendarYearTitle.text() + "-" + fixNumber(currentMonth) ;
                        n.val(currentDate);
                        $calendar.hide();
                        datetime = {
                            year:currentYear,
                            month:currentMonth,
                            date:currentYear + "-" + currentMonth
                        };
                        n.data("datetime",datetime);
                        c.callback.call(that, datetime);
                        n.trigger('change')
                    }else{
                        var a = dList(Number($calendarYearTitle.text()), Number($(this).attr("val")));
                        D(a);
                        selectDay();
                        if(c.language == "en"){
                            $calendarMonthTitle.text($(this).text());
                        }else{
                            var currentMonth = Number($(this).attr("val"))+1;
                            $calendarMonthTitle.text(Number(currentMonth));
                        }
                        default_item();
                    }

                }).hover(function() {
                        $(this).addClass("hover")
                    },
                    function() {
                        $(this).removeClass("hover")
                    })
            }
            //年份选择

            function selectYear() {
                $calendar.find(".tabY a").unbind("mouseup").mouseup(function() {
                    //$calendarYearTitle.text(Number($(this).text()));
                    if(c.template == "month"){

                        $calendarYearTitle.text(Number($(this).text()));
                        var a = mList($calendarYearTitle.text());
                        E(a);
                        selectMonth();
                        default_item();

                    }else if(c.template == "year"){
                        var currentYear = Number($(this).text());
                        n.val(currentYear);
                        $calendar.hide();
                        datetime = {
                            year:currentYear,
                            date:currentYear
                        };
                        n.data("datetime",datetime);
                        c.callback.call(that, datetime);
                        n.trigger('change')
                    }
                    else{
                        $calendarYearTitle.text(Number($(this).text()));
                        var currentMonth = $calendarMonthTitle.text();
                        if(c.language == "en"){
                            $.each(enMonths,function(key,val){
                                if(val == currentMonth){
                                    currentMonth = key;
                                }
                            });
                        }else{
                            currentMonth = Number(currentMonth) -1;
                        }
                        var a = dList(Number($(this).text()), currentMonth);
                        D(a);
                        selectDay();
                        default_item();
                    }
                }).hover(function() {
                        $(this).addClass("hover")
                    },
                    function() {
                        $(this).removeClass("hover")
                    })
            }
            //控件渐变事件
            function A(a){
                var b = $calendar.find(".reserve"),
                    d = $calendar.find(".enabled");
                b.stop();
                d.stop();
                b.removeClass("reserve").addClass("enabled");
                d.removeClass("enabled").addClass("reserve");
                b.css({
                    "margin-left": "0px",
                    "margin-top": "0px"
                });
                d.css({
                    "margin-left": "-" + d.width() + "px"
                });
                b.empty().html(a);
                d.empty();
            }
            function B(a) {
                var b = $calendar.find(".reserve"),
                    d = $calendar.find(".enabled");
                b.stop();
                d.stop();
                b.removeClass("reserve").addClass("enabled");
                d.removeClass("enabled").addClass("reserve");
                b.css({
                    "margin-left": d.width() + "px",
                    "margin-top": "0px"
                });
                b.empty().append(a);
                b.animate({
                        "margin-left": "0px"
                    },
                    c.speed);
                d.animate({
                        "margin-left": "-" + d.width() + "px"
                    },
                    c.speed,
                    function() {
                        d.empty()
                    })
            }
            function C(a) {
                var b = $calendar.find(".reserve"),
                    d = $calendar.find(".enabled");
                b.stop();
                d.stop();
                b.removeClass("reserve").addClass("enabled");
                d.removeClass("enabled").addClass("reserve");
                b.css({
                    "margin-left": "-" + d.width() + "px",
                    "margin-top": "0px"
                });
                b.empty().append(a);
                b.animate({
                        "margin-left": "0px"
                    },
                    c.speed);
                d.animate({
                        "margin-left": d.width() + "px"
                    },
                    c.speed,
                    function() {
                        d.empty()
                    })
            }
            function D(a) {
                var b = $calendar.find(".reserve"),
                    d = $calendar.find(".enabled");
                b.stop();
                d.stop();
                b.removeClass("reserve").addClass("enabled");
                d.removeClass("enabled").addClass("reserve");
                $calendar.css({
                    "z-index": 1
                });
                b.css({
                    "z-index": -1
                });
                d.css({
                    "z-index": -1
                });
                b.css({
                    "margin-left": "0px",
                    "margin-top": d.height() + "px"
                });
                b.empty().append(a);
                b.animate({
                        "margin-top": "0px"
                    },
                    c.speed);
                d.animate({
                        "margin-top": "-" + d.width() + "px"
                    },
                    c.speed,
                    function() {
                        d.empty();
                        $calendar.css({
                            "z-index": 1
                        });
                        b.css({
                            "z-index": 0
                        });
                        d.css({
                            "z-index": 0
                        })
                    })
            }
            function E(a) {
                var b = $calendar.find(".reserve"),
                    d = $calendar.find(".enabled");
                b.stop();
                d.stop();
                b.removeClass("reserve").addClass("enabled");
                d.removeClass("enabled").addClass("reserve");
                $calendar.css({
                    "z-index": 1
                });
                b.css({
                    "z-index": -1
                });
                d.css({
                    "z-index": -1
                });
                b.css({
                    "margin-left": "0px",
                    "margin-top": "-" + d.height() + "px"
                });
                b.empty().append(a);
                b.animate({
                        "margin-top": "0px"
                    },
                    c.speed);
                d.animate({
                        "margin-top": d.height() + "px"
                    },
                    c.speed,
                    function() {
                        d.empty();
                        $calendar.css({
                            "z-index": 1
                        });
                        b.css({
                            "z-index": 0
                        });
                        d.css({
                            "z-index": 0
                        })
                    })
            }
            function F(a) {
                var b = [];
                b.x = a.offsetLeft;
                for (b.y = a.offsetTop; a = a.offsetParent;) {
                    b.x += a.offsetLeft;
                    b.y += a.offsetTop
                }
                return b
            }
            //翻页事件
            $calendar.find(".prevMonth").unbind("mouseup").mouseup(function() {
                var currentYear = $calendarYearTitle.text(),
                    currentMonth = $calendarMonthTitle.text();

                if ($calendar.find(".enabled > .tabD").length > 0) {
                    var d = dList(Number(currentYear), Number(currentMonth) -2);
                    C(d);
                    if (Number(currentMonth) != 1) $calendarMonthTitle.text(Number(currentMonth) - 1);
                    else {
                        $calendarYearTitle.text(Number(currentYear) - 1);
                        $calendarMonthTitle.text("12")
                    }
                    selectDay();
                    default_item();
                } else if ($calendar.find(".enabled > .tabM").length > 0) {
                    $calendarYearTitle.text(Number(currentYear) - 1);
                    var d = mList($calendarYearTitle.text());
                    C(d);
                    selectMonth();
                    default_item();
                    //limitMonthProNext();
                } else if ($calendar.find(".enabled > .tabY").length > 0) {
                    var d = yList(Number(currentYear) - 10);
                    C(d);
                    selectYear();
                    $calendarYearTitle.text(Number(currentYear) - 10);
                    default_item();
                }
            });
            $calendar.find(".nextMonth").unbind("mouseup").mouseup(function() {
                var currentYear = $calendarYearTitle.text(),
                    currentMonth = $calendarMonthTitle.text();
                if ($calendar.find(".enabled > .tabD").length > 0) {
                    var d = dList(Number(currentYear), Number(currentMonth));
                    B(d);
                    if (Number(currentMonth) != 12) $calendarMonthTitle.text(Number(currentMonth) + 1);
                    else {
                        $calendarYearTitle.text(Number(currentYear) + 1);
                        $calendarMonthTitle.text("1");
                    }
                    selectDay();
                    default_item();
                } else if ($calendar.find(".enabled > .tabM").length > 0) {
                    $calendarYearTitle.text(Number(currentYear) + 1)
                    var d = mList($calendarYearTitle.text());
                    B(d);
                    selectMonth();
                    default_item();
                    //limitMonthProNext();
                } else if ($calendar.find(".enabled > .tabY").length > 0) {
                    var d = yList(Number(currentYear) + 10);
                    B(d);
                    selectYear();
                    $calendarYearTitle.text(Number(currentYear) + 10);
                    default_item();
                }
            });
            //点击标题月份事件
            $calendar.find(".currentMonthText").mouseup(function() {
                if (! ($calendar.find(".enabled > .tabM").length > 0)) {
                    var a = mList($calendarYearTitle.text());
                    E(a);
                    selectMonth();
                    default_item();
                }
            });
            //点击标题年份事件
            $calendar.find(".currentYearText").unbind("mouseup").mouseup(function() {
                if (! ($calendar.find(".enabled > .tabY").length > 0)) {
                    var a = yList(Number($calendarYearTitle.text()));
                    E(a);
                    selectYear();
                    default_item();
                }
            });
            n.bind("click",function() {
                $(".calendar").hide();
                $calendar.css({
                    top: $(this).outerHeight() + 2 + "px"
                }).show();
                template();
                selectDay();
                selectMonth();
                selectYear();
                default_title();
                default_item();
            });

            n.mouseleave(function(){
                $(document).bind("click.dataselect",function() {
                    $calendar.hide();
                    $(document).unbind("click.dataselect");
                });
            }).mouseenter(function(){
                    $(document).unbind("click.dataselect");
                });
            $calendar.mouseleave(function(){
                $(document).bind("click.dataselect",function() {
                    $calendar.hide();
                    $(document).unbind("click.dataselect");
                });
            }).mouseenter(function(){
                    $(document).unbind("click.dataselect");
                });
            $calendar.bind("click",function(){
                $(document).unbind("click.dataselect");
            });

        });
    }

}); // end define