var places=[],transitions=[],pnArcs=[];
var lastModelName='';       //上次查看的模型文件名
var lastLogName='';       //上次查看的日志文件名
var currentModelName='';    //本次查看的模型文件名
var currentLogName='';    //本次查看的日志文件名
var eventNets='';
var netIndex=0;//记录查看日志的路径索引
var canvasID='';
var modelName="";
var logName="";

//状态图的nodes数组和alignments数组
var nodes=[],alignments=[];

//结果数组
var resultAlignments=[];

var W1=0.5,W2=0.5;
/**
 * 绘制模型,param-filename是传过来的文件名，canvasId是画布的id名称
 * 两处用到：通过查看view按钮点击触发;运行start也会触发
 * @param url是要查看的Petri网的类型
 * @param filename是查看的文件名
 * @param canvasId是要绘制在哪一个画布上
 */
function drawPetriNet(url,filename,canvasId,w1,w2) {
    if(w1)
        W1=w1;
    if(w2)
        W2=w2;
    // var fileName=$("strong").eq(i).text();
    var fileName=filename;
    canvasID=canvasId;
    //将fileName通过ajax的POST方法传到后台处理，获得文件的内容
    //data是从后台返回的json数据// var url="/getProcessNet";
    $.post(url,{"fileName":fileName},function (data) {
        // var json = data;
        if(url.includes("getProcessNet")){
            modelName=fileName;
            var json = data;
            places=json.places;
            transitions=json.transitions;
            pnArcs=json.pnArcs;
            //在画之前，将fileName赋值给当前的
            currentModelName=fileName;
            //调用draw函数绘制petri网
            draw(canvasId);
            //画完之后，将fileName赋值，作为上次查看的
            lastModelName=fileName;
        }
        else{
            logName=fileName;
            netIndex=0;
            // 日志中有很多路径会获得很多的网络
            eventNets=data.eventNets;
            $("#pre").css("margin-left","43%");

            // 绘制事件网和对应的对齐网
            drawEventNetandAlignment();
        }

    });
}

//获得上一条日志-事件网
$("#pre").click(function () {
    netIndex-=1;
    // alert("pre:"+netIndex);
    if(netIndex<0){
        netIndex=0;
        alert("没有上一个");
    }

    drawEventNetandAlignment();
});

//获得下一条日志
$("#next").click(function () {
    netIndex+=1;
    if(netIndex>=eventNets.length){
        netIndex=eventNets.length-1;
        alert("没有下一个");
    }
    drawEventNetandAlignment();
});

// 绘制时间网和对应的对齐网
function drawEventNetandAlignment() {
    $("#eventNetName").html("Event Net--"+eval(netIndex+1));
    var json=eventNets[netIndex];
    places=json.places;
    transitions=json.transitions;
    pnArcs=json.pnArcs;
    clearCanvas(canvasID);
    //调用draw函数绘制petri网
    draw(canvasID);

    //绘制状态空间网
    var url1="/getStateGraph";
    $.post(url1,{"modelName":modelName,"logName":logName,"netIndex":netIndex},function (data1) {
        //调用viewStateGraph.js的方法
        // var nodes=data1.nodes;
        // var alignments=data1.alignments;
        var json1=data1;
        nodes=json1.nodes;
        alignments=json1.alignments;
        viewStateGraph(nodes,alignments);
        var url2="/getResult";
        $.post(url2,{"modelName":modelName,"logName":logName,"netIndex":netIndex,"w1":W1,"w2":W2},function (data2) {
            var json2=data2;
            resultAlignments=json2.alignments;
            var fAct=json2.fAct;
            var fAtt=json2.fAtt;
            var fEStr=json2.fEStr;
            //生成结果的表格
            $("#resultTable").html("");
            var html=produceTable(resultAlignments,fAct,fAtt,fEStr);
            $("#resultTable").html(html);
        });
    });
}


//清空画布，canvas每当高度或宽度被重设时，画布内容就会被清空
function clearCanvas(canvasId) {
    var canvas=document.getElementById(canvasId);
    canvas.height=canvas.height;
}

/**
 * 绘制petri网
 * @param canvasId，canvas的ID
 */
function draw(canvasId) {
    var canvas=document.getElementById(canvasId);

    //当不是第一次查看，并且上次和本次文件不一样时，才会清空
    //不清空的话，会在上次的基础上继续画
    if((lastModelName!=""&&lastModelName!=currentModelName)||(lastLogName!=""&&lastLogName!=currentLogName))
        clearCanvas(canvasId);

    //最大的x/y坐标,动态设置canvas的宽和高
    var maxX=places[places.length-1].x+20;
    var maxY=places[places.length-1].y+50;
    if(maxX>canvas.width)
        canvas.width=maxX+100;
    if(maxY>canvas.height)
        canvas.height=maxY+100;

    if(!canvas.getContext)
        return;
    var ctx=canvas.getContext("2d");
    ctx.strokeStyle="#000000";
    ctx.lineWidth=1;

    //遍历库所，绘制圆
    for(var place of places){
        ctx.beginPath();
        //context.arc(x,y,r,sAngle,eAngle,counterclockwise);
        // 圆心的x坐标，y坐标，半径，起始角，结束角（弧度），False = 顺时针||true = 逆时针
        ctx.arc(eval(place.x+20),eval(place.y+20),20,0,Math.PI*2,true);
        ctx.closePath();
        ctx.stroke();
        //如果有令牌，绘制填充圆
        if(place.token){
            ctx.fillStyle="#000000";
            ctx.beginPath();
            ctx.arc(eval(place.x+20),eval(place.y+20),5,0,Math.PI*2,true);
            ctx.closePath();
            ctx.fill();
        }

        //写节点的名字，采用其原有的坐标
        ctx.strokeText(place.id+"--"+place.name.text,place.name.x, eval(place.name.y+10));
        // ctx.strokeText(place.name,eval(place.x), eval(place.y+50));

        ctx.stroke();
    }
    //遍历变迁，绘制矩形
    for(var transition of transitions){
        //如果是隐藏任务，则用黑框表示
        if(transition.isHidden){
            ctx.fillStyle="#000000";
            ctx.fillRect(transition.x,transition.y,40,40);
            ctx.strokeText(transition.id+"--"+transition.name.text,transition.name.x, eval(transition.name.y+10));
        }
        else{
            ctx.strokeRect(transition.x,transition.y,40,40);
            ctx.strokeText(transition.id+"--"+transition.name.text,transition.name.x, eval(transition.name.y+10));
            // ctx.strokeText(transition.name, eval(transition.x), eval(transition.y+60));
        }
    }
    //遍历弧，绘制连线
    var i=0;
    for(var pnArc of pnArcs){
        i++;
        var id1=pnArc.source;
        var id2=pnArc.target;
        // alert(i+":"+id1+id2);
        var turnPoints=pnArc.turnPoints;
        var source,target;
        //如果id1中包含t，则是变迁指向库所
        if(id1.includes("t")){
            for(var transition of transitions){
                if(id1==transition.id){
                    source=transition;
                    break;
                }
            }
            for(var place of places){
                if(id2==place.id){
                    target=place;
                    break;
                }
            }
        }
        else{
            for(var place of places){
                if(id1==place.id){
                    source=place;
                    break;
                }
            }
            for(var transition of transitions){
                if(id2==transition.id){
                    target=transition;
                    break;
                }
            }
        }
        //如果没有转折点
        if(turnPoints.length==0){
            //如果是正常的从左到右的连线
            if(source.x<target.x){
                drawDirectedLine(ctx,eval(source.x+40),eval(source.y+20),eval(target.x),eval(target.y+20),30,10,1,"#000000");
                // alert("无转折，左右："+source.id+target.id);
            }
            //否则是从右到左
            else{
                drawDirectedLine(ctx,eval(source.x),eval(source.y+20),eval(target.x+40),eval(target.y+20),30,10,1,"#000000");
                // alert("无转折，右左"+source.id+target.id);
            }
        }

        //如果有转折点
        else {
            //*注意开始绘制线时，要beginPath
            ctx.beginPath();
            ctx.moveTo(eval(source.x+40),eval(source.y+20));
            //转折点处不用绘制箭头
            for(var turnPoint of turnPoints)
                ctx.lineTo(turnPoint.x,turnPoint.y)
            ctx.stroke();
            //如果大于说明方向没有转变
            if(target.x>turnPoints[turnPoints.length-1].x){
                // alert("有转折，左右"+source.id+target.id);
                drawDirectedLine(ctx,turnPoints[turnPoints.length-1].x,turnPoints[turnPoints.length-1].y,eval(target.x),eval(target.y+20),30,10,1,"#000000");
            }
            else{
                drawDirectedLine(ctx,turnPoints[turnPoints.length-1].x,turnPoints[turnPoints.length-1].y,eval(target.x+40),eval(target.y+20),30,10,1,"#000000");
                // alert("有转折，右左"+source.id+target.id);
            }
        }
    }
}

/**
 * 绘制有箭头的连线
 * @param ctx,Canvas绘图环境
 * @param fromX,起点X坐标
 * @param fromY,起点Y坐标
 * @param toX,终点坐标
 * @param toY
 * @param theta,三角斜边一直线夹角
 * @param headlen,三角斜边长度
 * @param width,箭头线宽度
 * @param color,箭头线的颜色
 */
function drawDirectedLine(ctx, fromX, fromY, toX, toY,theta,headlen,width,color) {
    //利用三目运算符，设定默认值
    theta = typeof(theta) != 'undefined' ? theta : 30;
    headlen = typeof(theta) != 'undefined' ? headlen : 10;
    width = typeof(width) != 'undefined' ? width : 1;
    color = typeof(color) != 'color' ? color : '#000';
    // 计算各角度（直线的与箭头两边的）和对应的箭头两个端点坐标
    var angle = Math.atan2(fromY - toY, fromX - toX) * 180 / Math.PI,
        angle1 = (angle + theta) * Math.PI / 180,
        angle2 = (angle - theta) * Math.PI / 180,
        topX = headlen * Math.cos(angle1),
        topY = headlen * Math.sin(angle1),
        botX = headlen * Math.cos(angle2),
        botY = headlen * Math.sin(angle2);
    //save()保存目前canvas状态
    ctx.save();
    ctx.beginPath();
    var arrowX ,arrowY;
    //绘制边
    ctx.moveTo(fromX, fromY);
    ctx.lineTo(toX, toY);
    //绘制箭头
    arrowX = toX + topX;
    arrowY = toY + topY;
    ctx.moveTo(arrowX, arrowY);
    ctx.lineTo(toX, toY);
    arrowX = toX + botX;
    arrowY = toY + botY;
    ctx.lineTo(arrowX, arrowY);
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.stroke();
    // restore()从绘图堆栈中的顶端弹出最近保存的状态，并且根据这些存储的值来设置当前绘图状态
    ctx.restore();
}


