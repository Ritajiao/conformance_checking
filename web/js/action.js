var selectedFileNum=0;      //记录是否日志模型都选了
var selectedFileIndex1=-1;       //全局变量，记录当前选中的文件
//记录当前显示的是哪一个对话框，方便选择的文件对号入座
//0-model,1-log
var currentDialog=-1;

var modelName="";
var logName="";

var w1=0.5,w2=0.5;      //w1和w2的权重

//selectFile的点击事件
//0-model,1-log
function showDialog(i) {
    //在每次弹出对话框开始时，选择的文件下标都是-1
    selectedFileIndex1=-1;
    currentDialog=i;

    $("#selectDialog").show();
    $("#main_mask").show();
    if(i==0){
        $("#dialogTitle").html("Select model file");
        // 记录模型的数量，每次点击刚开始是0
        var modelFileNum=0;
        //每次点击都要清空，否则会一直重复增加
        $("#dialogFile").html("");
        for(var i=0;i<$(".file").length;i++){
            var originalHtml=$("#dialogFile").html();
            var newHtml="";
            if($(".file").eq(i).find("strong").html().includes("pnml")){
                var newHtml="<div class='file1' style='background-color: #d3d3d3' onclick='changeBgColor1("+modelFileNum+")'><img src='images/resourcetype_model_30x35.png'>" +
                    "<div class='description1'>"+$(".description").eq(i).html()+"</div></div>";
                $("#dialogFile").html(originalHtml+newHtml);
                modelFileNum++;
                //每次都是最开始是选中的是0
                selectedFileIndex1=0;
            }
            //刚开始时让最上面的为绿色
            $(".file1").eq(selectedFileIndex1).css("backgroundColor","green");
        }
    }
    else{
        $("#dialogTitle").html("Select log file");
        // 记录模型的数量，每次点击刚开始是0
        var logFileNum=0;
        $("#dialogFile").html("");
        for(var i=0;i<$(".file").length;i++){
            var originalHtml=$("#dialogFile").html();
            var newHtml="";
            if($(".file").eq(i).find("strong").html().includes("xml")){
                var newHtml="<div class='file1' style='background-color: #d3d3d3' onclick='changeBgColor1("+logFileNum+")'><img src='images/resourcetype_log_30x35.png'>" +
                    "<div class='description1'>"+$(".description").eq(i).html()+"</div></div>";
                $("#dialogFile").html(originalHtml+newHtml);
                logFileNum++;
                selectedFileIndex1=0;
            }
            //刚开始时让最上面的为绿色
            $(".file1").eq(selectedFileIndex1).css("backgroundColor","green");
        }
    }
}
//通过点击文件，改变文件的背景颜色
function changeBgColor1(i) {
    $(".file1").eq(i).css("backgroundColor","green");
    //当当前点击的不等于选中的时候，才改变之前选中的颜色
    if(i!=selectedFileIndex1){
        $(".file1").eq(selectedFileIndex1).css("backgroundColor","#d3d3d3");
        selectedFileIndex1=i;
    }
}

//cancel按钮的点击事件
$("#cancel").click(function () {
    $("#selectDialog").hide();
    $("#main_mask").hide();
    currentDialog=-1;
});

//选择文件的点击事件
$("#select").click(function () {
    $("#selectDialog").hide();
    $("#main_mask").hide();
    var fileHtml="";
    //刚开始-1代表没有文件
    if(selectedFileIndex1!=-1){
        if(currentDialog==0){
            modelName=$(".description1").eq(selectedFileIndex1).find("strong").text();
            fileHtml="<div class='fileSelected' style='background-color: green'><img src='images/resourcetype_model_30x35.png'>"
                + "<div class='fileSelectedDescription'>"+$(".description1").eq(selectedFileIndex1).html()+
                "</div><div class='operation'><span class='remove' onclick='reset(0)'><img src='images/remove_30x30_black.png'></span></div>";
        }
        else{
            logName=$(".description1").eq(selectedFileIndex1).find("strong").text();
            fileHtml="<div class='fileSelected' style='background-color: green'><img src='images/resourcetype_log_30x35.png'>"
                + "<div class='fileSelectedDescription'>"+$(".description1").eq(selectedFileIndex1).html()+
                "</div><div class='operation'><span class='remove' onclick='reset(1)'><img src='images/remove_30x30_black.png'></span></div>";
        }

        $(".selectInput").eq(currentDialog).html(fileHtml);
        //当不是初始的html时，说明选择了文件
        if(!$(".selectInput").eq(currentDialog).html().includes("Click to"))
            selectedFileNum++;

        $(".selectInput").eq(currentDialog).css("border","2px solid green");


        //如果日志和模型都选择了，则开始按钮则可以点击
        if(selectedFileNum==2){
            $("#startButton").css("opacity",0.8);
            $("#startButton").mouseover(function () {
                $(this).css("opacity",1);
            }).mouseout(function () {
                $(this).css("opacity",0.8);
            }).click(function () {
                //调用运行函数
                // alert("modelName："+modelName+",logName："+logName);
                // produceResultTable("/getResult",modelName,logName);
                if($("#weight").get(0).selectedIndex!=0){
                    var weight=$("#weight option:selected").text();
                    w1=weight.split("-")[0];
                    w2=weight.split("-")[1];
                }
                $("#process").show();
                //触发viewPetriNet.js中的函数，分别显示流程网和事件网模型
                drawPetriNet("/getProcessNet",modelName,"processNetCanvas",w1,w2);
                drawPetriNet("/getEventNet",logName,"eventNetCanvas",w1,w2);
            });
            $("#resultButton").css("opacity",0.8);
            $("#resultButton").mouseover(function () {
                $(this).css("opacity",1);
            }).mouseout(function () {
                $(this).css("opacity", 0.8);
            }).click(function () {
                produceResult(modelName,logName,w1,w2);
                changePage(6);
            })
        }
    }
});


//移除选中的文件
function reset(i) {
    if(i==0)
        $(".selectInput").eq(i).html("Click to select model file");
    else
        $(".selectInput").eq(i).html("Click to select log file");
    $(".selectInput").eq(i).css("border","2px dashed white");
    //移除选中的文件，selectedFileNum要减一
    selectedFileNum--;
    currentDialog=-1;
    if(selectedFileNum<2){
        $("#startButton").css("opacity",0.5);
        // 移除startButton已绑定的事件
        $("#startButton").unbind();
        $("#resultButton").css("opacity",0.5);
        $("#resultButton").unbind();
    }
}

// $("#modelFile").click(function () {
//     $("#selectDialog").show();
//     $("#main_mask").show();
//     $("#dialogTitle").html("Select model file");
//     for(var i=0;i<$(".file").length;i++){
//         var originalHtml=$("#dialogFile").html();
//         var newHtml="";
//         if($(".file").eq(i).find("strong").html().includes("pnml")){
//             var newHtml="<div class='file1' style='background-color: #d3d3d3' onclick='changeBgColor1("+modelFileNum+")'><img src='images/resourcetype_model_30x35.png'>" +
//                 "<div class='description1'>"+$(".description").eq(i).html()+"</div></div>";
//             $("#dialogFile").html(originalHtml+newHtml);
//             modelFileNum++;
//         }
//         //刚开始时让最上面的为绿色
//         $(".file1").eq(selectedFileIndex).css("backgroundColor","green");
//     }
// });
// $("#logFile").click(function () {
//     $("#selectDialog").show();
//     $("#main_mask").show();
//     $("#dialogTitle").html("Select log file");
//     for(var i=0;i<$(".file").length;i++){
//         var originalHtml=$("#dialogFile").html();
//         var newHtml="";
//         if($(".file").eq(i).find("strong").html().includes("xml")){
//             var newHtml="<div class='file1' style='background-color: #d3d3d3' onclick='changeBgColor1("+logFileNum+")'><img src='images/resourcetype_log_30x35.png'>" +
//                 "<div class='description1'>"+$(".description").eq(i).html()+"</div></div>";
//             $("#dialogFile").html(originalHtml+newHtml);
//             logFileNum++;
//         }
//         //刚开始时让最上面的为绿色
//         $(".file1").eq(selectedFileIndex1).css("backgroundColor","green");
//     }
// });

