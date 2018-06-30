var results=[],alignments=[];
var fitness="";
var lastModelName="",lastLogName="";
var clickedIndex=-1;        //上次点击的对齐的index
var ifEventClicked=false;       //控制当只点击活动时，右面属性表格的显示内容
var clickedAlignmentIndex=-1;   //点击的对齐的下标
function produceResult(modelName,logName,w1,w2){
    var url="/getResults";
    if((lastModelName=""&&lastLogName=="")||(lastModelName!=modelName&&lastLogName!=logName)){
        $.post(url,{"modelName":modelName,"logName":logName,"w1":w1,"w2":w2},function (data) {
            var json=data;
            // results=[],alignments=[];
            results=json.results;
            fitness=json.fitness;
            //在每次重新生成结果之前都要更新这些全局变量，否则会出错
            clickedIndex=-1,ifEventClicked=false,clickedAlignmentIndex=-1;
            produceAlignment();
            produceTableForm();
            lastModelName=modelName;
            lastLogName=logName;
        });
    }
}
function produceTableForm() {
    $("#form1").html("");
    var html="";
    for(var result of results){
        html+=produceTable(result.alignments,result.fAct,result.fAtt,result.fEStr);
    }
    html+="<table  border='1' align='center' cellpadding='0' cellspacing='0' bordercolorlight='#3399FF' bordercolordark='#FFFFFF'>" +
        "<tbody>" +
        "<tr bgcolor='#99ccff' align='left'>" +
        "<th width='600'>日志-模型拟合度fitness(L,M)</th>" +
        "<th width='600'>"+fitness+"</th>" +
        "</tr></tbody></table>";
    $("#form1").html(html);
}

//生成对齐的图形
function produceAlignment() {
    var html = "";
    resultIndex = -1;
    var maxLength = 0;
    for (var result of results) {
        resultIndex++;
        //对于没一个整个的路径，点击就要边长
        var html = html + "<div class='result1' onclick='turnShorterAndLonger(this," + resultIndex + "); setTraceAttributes(this," + resultIndex + ")'><div class='fitness' title='Trace:" + result.traceId + "\nFitness:" + result.fEStr + "\nFitness(event):" + result.fAct + "\nFitness(attribute)" + result.fAtt + "'>Trace:" + result.traceId + "<br/>Fitness:" + result.fEStr +
            "</div><div class='alignments'>";
        alignments = result.alignments;

        if (maxLength < alignments.length)
            maxLength = alignments.length;
        // maxLength=alignments.length*45+100;

        var alignmentHtml = "";
        alignmentIndex = -1;
        for (var alignment of alignments) {
            alignmentIndex++;
            //对于没一个活动点击都会出现其属性的赋值情况

            //如果是插入的活动，只在模型中移动
            if (alignment.isInserted) {
                var alignmentHtml = alignmentHtml + "<div class='figure' onclick='setEventAttributes(" + resultIndex + "," + alignmentIndex + ")'  title='" + alignment.eventName + "'><div class='orangeTriangle1'></div>" +
                    "<span class='square' style='background-color: orange'>" + alignment.eventName.substring(0, 4) + "</span><div class='orangeTriangle2'></div></div>"
            }
            //如果是跳过的活动则是在模型中移动
            else if (alignment.isSkipped) {
                var alignmentHtml = alignmentHtml + "<div class='figure' onclick='setEventAttributes(" + resultIndex + "," + alignmentIndex + ")' title='" + alignment.transitionName + "'><div class='purpleTriangle1'></div>" +
                    "<span class='square' style='background-color: mediumpurple'>" + alignment.transitionName.substring(0, 4) + "</span><div class='purpleTriangle2'></div></div>"
            }
            //如果数据出错
            else if (alignment.isDatafail) {
                var alignmentHtml = alignmentHtml + "<div class='figure' onclick='setEventAttributes(" + resultIndex + "," + alignmentIndex + ")' title='" + alignment.transitionName + "'><div class='honeydewTriangle1'></div>" +
                    "<span class='square' style='background-color: lightgreen'>" + alignment.transitionName.substring(0, 4) + "</span><div class='honeydewTriangle2'></div></div>"
            }
            //如果是隐藏活动
            else if (alignment.isHidden) {
                var alignmentHtml = alignmentHtml + "<div class='figure' onclick='setEventAttributes(" + resultIndex + "," + alignmentIndex + ")'  title='" + alignment.transitionName + "'><div class='greyTriangle1'></div>" +
                    "<span class='square' style='background-color: grey;color: white;'>" + alignment.transitionName.substring(0, 4) + "</span><div class='greyTriangle2'></div></div>"
            }
            //完全对齐
            else {
                var alignmentHtml = alignmentHtml + "<div class='figure'onclick='setEventAttributes(" + resultIndex + "," + alignmentIndex + ")'  title='" + alignment.transitionName + "'><div class='greenTriangle1'></div>" +
                    "<span class='square' style='background-color: #00bc00'>" + alignment.transitionName.substring(0, 4) + "</span><div class='greenTriangle2'></div></div>"
            }
        }
        html = html + alignmentHtml + "</div></div>";
    }

    if (maxLength * 45 + 100 > 900)
        $(".result1").css("width", maxLength * 45 + 100 + "px");
    $("#alignments").html(html);
    $("#fitness").html(fitness);
    // $("#count").html(alignments.length+1);
    $("#count").html(results.length);
}

/**
 * 将点击的对齐结果变长
 * @param object 点击的对象作为参数传入
 * @param resultIndex
 */
function turnShorterAndLonger(object,resultIndex) {
    //将上次的变短
    turnShorter(clickedIndex);
    //将点击的变长
    turnLonger(object,resultIndex)
    clickedIndex=resultIndex;
}

function turnShorter(clickedIndex) {
    //把上次点击的变短
    if(clickedIndex!=-1){
        alignments=results[clickedIndex].alignments;
        var i=-1;
        var name="";
        $(".result1").eq(clickedIndex).find(".square").css("width","25px");
        $(".result1").eq(clickedIndex).find(".square").css("line-height","40px");
        for(var alignment of alignments){
            i++;
            if(alignment.isInserted)
                $(".result1").eq(clickedIndex).find(".square").eq(i).html(alignment.eventName.substring(0,4));
            else
                $(".result1").eq(clickedIndex).find(".square").eq(i).html(alignment.transitionName.substring(0,4));
        }
    }
}

function turnLonger(object,resultIndex) {
    //将当前的变长
    alignments=results[resultIndex].alignments;
    var i=-1;
    var name="";
    var maxLength=0;
    for(var alignment of alignments){
        i++;
        if(alignment.isInserted){
            name=alignment.eventName;
            $(object).find(".square").eq(i).html(name);
        }
        else{
            name=alignment.transitionName;
            $(object).find(".square").eq(i).html(name);
        }
        //根据名字长短设置square的宽
        if(name.length>4){
            $(object).find(".square").eq(i).css("width",name.length*6+"px");
            // if(name.length>10)
            //     $(object).find(".square").eq(i).css("line-height","20px");
            maxLength+=name.length*6+20;
        }
        else
            maxLength+=25+20;
    }
    //如果超出范围，再改变其宽
    if(maxLength+20>800){
        $(object).css("width",maxLength+200);
    }
}


function setTraceAttributes(object,resultIndex) {
    var nameHtml="<span>--Standard Attributes--</span><br/><span>Name</span><br/><span>Fitness</span><br/><span>Fitness(event)</span><br/><span>Fitness(attribute)</span><br/>";
    var logValueHtml="<span>------------------</span><br/><span>"+results[resultIndex].traceId+"</span><br/><span>"+results[resultIndex].fEStr+"</span><br/><span>"+results[resultIndex].fAct+"</span><br/><span>"+results[resultIndex].fAct+"</span><br/>";
    var processValueHtml="<span>------------------</span><br/><span>-</span><br/><span>-</span><br/><span>-</span><br/><span>-</span><br/>";
    if(results[resultIndex].traceAttributes){
        nameHtml+="<span>--Trace Attributes--</span><br/>";
        logValueHtml+="<span>------------------</span><br/>";
        processValueHtml+="<span>------------------</span><br/>";
        for(var attribute of results[resultIndex].traceAttributes){
            nameHtml+="<span>"+attribute.name+"</span><br/>";
            logValueHtml+="<span>"+attribute.key+"</span><br/>";
            processValueHtml+="<span>-</span><br/>";
        }
    }
    $("#Name").html(nameHtml);
    $("#LogValue").html(logValueHtml);
    $("#ProcessValue").html(processValueHtml);
    if(ifEventClicked) {
        setEventAttributes(resultIndex,clickedAlignmentIndex);
        ifEventClicked=false;
    }
}

/**
 *设置所点击的活动的属性
 * @param resultIndex 点击的一条路径的对齐结果的下标
 * @param alignmentIndex        //对齐中的活动的下标
 */
function setEventAttributes(resultIndex,alignmentIndex) {
    ifEventClicked=true;
    clickedAlignmentIndex=alignmentIndex;
    var nameHtml="<span>--Standard Attributes--</span><br/><span>Name</span><br/>";
    var logValueHtml="";
    var processValueHtml="";

    alignments=results[resultIndex].alignments;
    var alignment=alignments[alignmentIndex];
    if(alignment.isSkipped||alignment.isHidden){
        logValueHtml="<span>------------------</span><br/><span>-</span>";
        processValueHtml="<span>------------------</span><br/><span>"+alignment.transitionName+"</span>";
    }
    else{
        logValueHtml="<span>------------------</span><br/><span>"+alignment.eventName+"</span><br/>";
        processValueHtml="<span>------------------</span><br/><span>";
        if(alignment.isInserted)
            processValueHtml+="-</span><br/>";
        else
            processValueHtml+=alignment.transitionName+"</span><br/>";

        if(alignment.eventData!=""){
            nameHtml+="<span>--other Log Attributes--</span><br/>";
            logValueHtml+="<span>----------------</span><br/>"
            processValueHtml+="<span>----------------</span><br/>"
            var eventData=alignment.eventData.split(",");
            // var transitionData=alignment.transitionData.split(",");
            for(var i=0;i<eventData.length;i++){
                var data=eventData[i];
                var nameData="",logData="",processData="";
                if(i==0){
                    nameData=data.split("{")[1].split("=")[0];
                    logData=data.split("{")[1].split("=")[1];
                    if(i==eventData.length-1)
                        logData=logData.split("}")[0];
                }
                else if(i==eventData.length-1){
                    nameData=data.split("}")[0].split("=")[0];
                    logData=data.split("}")[0].split("=")[1];
                }
                else{
                    nameData=data.split("=")[0];
                    logData=data.split("=")[1];
                }
                // alert(nameData+"i:"+i);
                processData=alignment.transitionData.split(nameData)[1].split("=")[1];

                //如果表示范围
                if(processData.includes("["))
                    processData=processData.split("]")[0]+"]";
                else if(processData.includes("}"))
                    processData=processData.split("}")[0];
                else
                    processData=processData.split(",")[0];

                // alert("processData:"+processData)

                if(alignment.isDatafail&&alignment.failDataName.includes(nameData)){
                    nameHtml+="<span style='color: red'>"+nameData+"</span><br/>";
                    // logValueHtml+="<span style='color: red'>"+logData+"</span><br/>";
                    // processValueHtml+="<span style='color: red'>"+processData+"</span><br/>";
                }

                else{
                    nameHtml+="<span>"+nameData+"</span><br/>";

                }
                logValueHtml+="<span>"+logData+"</span><br/>";
                processValueHtml+="<span>"+processData+"</span><br/>";
            }

        }

    }
    $("#Name").html(nameHtml);
    $("#LogValue").html(logValueHtml);
    $("#ProcessValue").html(processValueHtml);
}

//选择按钮的点击事件
$("#selectAll").click(function () {
    for(var i=0;i<results.length;i++){
        turnLonger($(".result1").eq(i),i);
    }
});

$("#deSelectAll").click(function () {
    for(var i=0;i<results.length;i++){
        turnShorter(i);
    }
});

function formChange() {
    var index=$("#form").get(0).selectedIndex;
    for(var i=0;i<$("#form").length;i++){
        if(i!=index)
            $("#form"+i).hide();
        $("#form"+index).show();
    }
}


