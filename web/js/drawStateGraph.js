var colors=["orange","purple","green","grey"];

var stateGraphNetCanvas=document.getElementById("stateGraphNetCanvas");
// var rootX=stateGraphCanvas.width/2;
var rootX=600;
var rootY=20;
var paddingX=120;
var paddingY=70;
var rx=40;
var ry=15;
//记录哪一个节点已经绘制
var visited="";
var ctx1="";


function viewStateGraph(nodes,alignments){
    visited="";

    stateGraphNetCanvas.width=1200;
    stateGraphNetCanvas.height=400;
    //alignments.length就是最后画出来的图的层数
    if(alignments.length>2){
        // width=240(n-1)+80
        if(alignments.length>5)
            stateGraphNetCanvas.width=alignments.length*paddingX*2;
        //height=70(n-1)+20+250
        stateGraphNetCanvas.height=alignments.length*paddingY+250;
    }

    rootX=stateGraphNetCanvas.width/2;

    //设置stateGraph区域的滚动条位置在中间
    $("#stateGraph").scrollLeft(rootX-600);

    // 每次绘制之前都需要清空画布
    // clearCanvas("stateGraphNetCanvas");
    if(!stateGraphNetCanvas.getContext)
        return;
    ctx1=stateGraphNetCanvas.getContext("2d");
    var j=0;
    for(var node of nodes){
        if(j==0){
            draw1(node,rootX,rootY);
        }
        else{
            draw1(node,node.X,node.Y);
        }
        j++;
    }

    //连续声明变量的时候每一个都要单独初始化
    var source="",target="";
    for(var alignment of alignments){
        for(var node1 of nodes){
            if(node1.name.toString()==alignment.name.toString()){
                // ctx.lineWidth=5;
                // ParamEllipse(ctx,node1.X,node1.Y,rx,ry,5,"red","green","["+node.name+"]");
                ParamEllipse(ctx1,node1.X,node1.Y,rx,ry,node1.type,3,"red","["+node1.name+"]");
                //显示连线名字
                //第一次
                if(source==""){
                    source=node1;
                }
                else {
                    target=node1;
                    //在源点的孩子中找与target的连线
                    if(source.children){
                        for(var child of source.children){
                            if(child.name.toString()==target.name.toString()){
                                ctx1.beginPath();
                                ctx1.lineWidth=0.5;
                                ctx1.strokeStyle="red";
                                ctx1.strokeText(child.edgeName,(source.X+target.X)/2, (source.Y+target.Y)/2);
                                ctx1.stroke();
                                ctx1.closePath();
                                break;
                            }
                        }
                    }
                    //下一次循环源点是本次的目标节点
                    source=target;
                }
                break;
            }
        }
    }
    var largestY=nodes[nodes.length-1].Y;
    drawLegend(ctx1,rootX,largestY+50);
}

function draw1(root,rootX,rootY) {
    // alert(root.name+"PPP")
    drawNode(root,rootX,rootY);
    drawChildren(root,rootX,rootY);
    //如果有孩子，加判断，否则编译时会出错
    if(root.children){
        for(var child of root.children){
            for(var node of nodes){
                if(node.name.toString()==child.name.toString()){
                    // if(node.name.toString()==child.name){
                    drawChildren(node,node.X,node.Y);
                    break;
                }
            }
        }
    }
}

//画节点，x,y是坐标
function drawNode(node,x,y,i) {
    if(!visited.includes(node.name)){
        // if(!visited.includes(node.name.toString())){
        ParamEllipse(ctx1,x,y,rx,ry,node.type,1,"black","["+node.name+"]");
        //在json中添加坐标属性
        node.X=x;
        node.Y=y;
        // if(x>stateGraphNetCanvas.width)
        //     stateGraphNetCanvas.width=(x+50);
        // if(y>stateGraphNetCanvas.height)
        //     stateGraphNetCanvas.height=(y+20);
        visited+=node.name;
    }

}

//绘制节点的孩子节点，并绘制连线
function drawChildren(node,rootX,rootY){
    // alert(node.name+"JJJ")
    // children=node.children;
    // alert(children.length+"KK")
    var x,y;
    var i=0;
    if(node.children){
        for(var child of node.children){
            //没有访问
            if(!visited.includes(child.name)){
                // if(!visited.includes(child.name.toString())){
                //     alert(child.name)
                var node2;
                for(var node1 of nodes){
                    if(node1.name.toString()==child.name.toString()){
                        // if(node1.name.toString()==child.name.toString()){
                        node2=node1;
                        break;
                    }
                }
                y=rootY+paddingY;
                if(child.edgeType==0){
                    x=rootX-paddingX;
                }
                else if(child.edgeType==1||child.edgeType==3){
                    i++;
                    x=rootX+i*paddingX;
                }
                else{
                    x=rootX;
                }
                drawNode(node2,x,y);
            }
            else{
                for(var node1 of nodes){
                    if(node1.name.toString()==child.name.toString()){
                        // if(node1.name.toString()==child.name.toString()){
                        x=node1.X;
                        y=node1.Y;
                        break;
                    }
                }
            }
            var deltaX = x - rootX,
                deltaY = y - rootY,
                dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),        //两点间距离
                normX = deltaX / dist,          //cosθ
                normY = deltaY / dist,          //sinθ

                paddingx=41*normX,          //x=acosθ ， y=bsinθ。
                paddingy=16*normY,

                sourceX = rootX+paddingx ,
                sourceY = rootY+paddingy,
                targetX = x-paddingx,
                targetY = y -paddingy;
            //调用viewPetriNet的方法画有向弧
            drawDirectedLine(ctx1,sourceX,sourceY,targetX,targetY,30,5,1,colors[child.edgeType]);
            // ctx1.beginPath();
            // ctx1.lineWidth=0.2;
            // ctx1.strokeStyle="grey";
            // ctx1.strokeText(child.edgeName,(sourceX+targetX)/2-15, (sourceY+targetY)/2-5);
            // ctx1.stroke();
            // ctx1.closePath();
        }
    }
}

//-----------用参数方程绘制椭圆---------------------
//函数的参数x,y为椭圆中心；a,b分别为椭圆横半轴、
//纵半轴长度，不可同时为0
//该方法的缺点是，当lineWidth较宽，椭圆较扁时
//椭圆内部长轴端较为尖锐，不平滑，效率较低
function ParamEllipse(context, x, y, a, b,type,lineWidth,strokeColor,name) {
    if(!name)
        name="";
    //max是等于1除以长轴值a和b中的较大者
    //i每次循环增加1/max，表示度数的增加
    //这样可以使得每次循环所绘制的路径（弧线）接近1像素
    var step = (a > b) ? 1 / a : 1 / b;
    context.strokeStyle=strokeColor;
    if(type==0)
        context.fillStyle="lightgreen";
    else if(type==1)
        context.fillStyle="white";
    else
        context.fillStyle="pink";
    context.lineWidth=lineWidth;
    context.beginPath();
    context.moveTo(x + a, y); //从椭圆的左端点开始绘制
    for (var i = 0; i < 2 * Math.PI; i += step)
    {
        //参数方程为x = a * cos(i), y = b * sin(i)，
        //参数为i，表示度数（弧度）
        context.lineTo(x + a * Math.cos(i), y + b * Math.sin(i));
    }
    context.closePath();
    context.fill();
    context.stroke();
    context.beginPath();
    context.strokeStyle="black";
    context.lineWidth=1;
    context.strokeText(name,x-18, y+5);
    context.stroke();
    context.closePath();
}

//画图的符号含义标识
function drawLegend(context,x,y) {
    var a=16;
    var b=6;
    context.strokeStyle="black";
    context.fillStyle="black";
    //画虚线框
    dottedLine(context,x-300, y, x+300, y);
    dottedLine(context,x+300, y, x+300, y+200);
    dottedLine(context,x-300, y+200, x+300, y+200);
    dottedLine(context,x-300, y, x-300, y+200);

    ParamEllipse(context,x-250,y+50,a,b,0,1,"black");
    context.strokeText("Initial state",x-220, y+55);
    ParamEllipse(context,x-50,y+50,a,b,1,1,"black");
    context.strokeText("State",x-20, y+55);
    ParamEllipse(context,x+150,y+50,a,b,2,1,"black");
    context.strokeText("Final state",x+180, y+55);

    drawDirectedLine(context,x-250-a,y+100,x-250+a,y+100,30,10,2,colors[2]);
    context.strokeText("Synchronous move",x-220, y+105);
    drawDirectedLine(context,x-50-a,y+100,x-50+a,y+100,30,10,2,colors[0]);
    context.strokeText("Move on log",x-20, y+105);
    drawDirectedLine(context,x+150-a,y+100,x+150+a,y+100,30,10,2,colors[1]);
    context.strokeText("Move on model",x+180, y+105);
    drawDirectedLine(context,x-250-a,y+150,x-250+a,y+150,30,10,2,colors[3]);
    context.strokeText("Move on model(invisible transitions)",x-220, y+155);

    //标题加粗
    context.font = "bold 15px Arial";
    context.fillStyle="black";
    context.fillText("LEGEND",x-30, y+30);
}

//画圆点虚线,从小坐标到大坐标
function dottedLine(context,x1,y1,x2,y2,interval) {
    if (!interval) {
        interval = 5;       //间距
    }
    //是否水平方向
    var isHorizontal = true;
    if (x1 == x2) {
        isHorizontal = false;
    }
    var len = isHorizontal ? x2 - x1 : y2 - y1;
    // context.fillStyle="black";
    context.beginPath();
    context.moveTo(x1,y1);
    var progress = -5;
    while(len > progress){
        progress += interval;
        // if (progress > len) {
        //     progress = len;
        // }
        if (isHorizontal) {
            context.moveTo(x1 + progress, y1);
            context.arc(x1 + progress, y1, 1, 0, Math.PI * 2, true);
            context.fill();
        }
        else {
            context.moveTo(x1, y1 + progress);
            context.arc(x1, y1 + progress, 1, 0, Math.PI * 2, true);
            context.fill();
        }
    }
    context.closePath();
}

