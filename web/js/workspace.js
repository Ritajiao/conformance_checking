var fileNum=0;      //全局变量，计算导入的文件数目
var selectedFileIndex=-1;       //全局变量，记录当前选中的文件


function selectFile() {
    $("#file").trigger("click");      //让提交按钮的事件触发
}

// 导入文件生成文件列表
// $('#file').change(function () {
function importFile() {
    // var name = $('#file').files[0].name;　　　//获得文件名称
    // C:\fakepath\shudian1.pnml
    var name = $('#file').val();　　　//获得文件名称,$('#file').val()含有路径
    name=name.split("fakepath\\")[1];
    var originalHtml=$(".fileList").html();
    var newHtml="";
    //当列表中没有该文件时，才导入
    if(!originalHtml.includes(name)) {
        //判断是什么类型的文件，类型正确才上传，防止用户错误提交不正确的文件,白白浪费服务器硬盘空间.
        if (name.includes(".xml") || name.includes(".pnml")) {
            $.ajaxFileUpload
            (
                {
                    url:'/importFile',//用于文件上传的服务器端请求地址
                    secureuri:false,//一般设置为false
                    fileElementId:'file',//文件上传空间的id属性 <input type="file" id="file" name="file" />
                    dataType: 'text',//返回值类型 一般设置为json
                    success: function (data, status) //服务器成功响应处理函数
                    {
                        //当函数的参数是字符串时，要加引号
                        if(name.includes("xml"))
                            newHtml="<div class='file' style='background-color: green' onclick='changeBgColor("+fileNum+")'><img src='images/resourcetype_log_30x35.png'>" +
                                "<div class='description'><strong>"+name+"</strong><br/><span style='font-size: 12px'>Event Log</span></div>" +
                                "<div class='operation'>"+
                                "<span class='remove' onclick='remove("+fileNum+");resetIndex()'><img src='images/remove_30x30_black.png'></span></div></div>";
                        else
                            newHtml="<div class='file' style='background-color: green' onclick='changeBgColor("+fileNum+")'><img src='images/resourcetype_model_30x35.png'>" +
                                "<div class='description'><strong>"+name+"</strong><br/><span style='font-size:12px'>Process Net</span></div>"+
                                "<div class='operation'><span class='view' onclick='drawPetriNet(\"/getProcessNet\",\""+name+"\",\"modelCanvas\");changePage(4);'><img src='images/view_30x30_black.png'></span>" +
                                "<span class='remove' onclick='remove("+fileNum+");resetIndex()'><img src='images/remove_30x30_black.png'></span></div></div>";
                        //其中view的点击函数，调用viewPetriNet函数绘制完之后还要：调用conformance_checking.js中的函数，切换页面
                        $(".fileList").html(originalHtml+newHtml);
                        //导入之前，把之前选中的变颜色
                        if(selectedFileIndex!=-1){
                            $(".file").eq(selectedFileIndex).css("backgroundColor","#A0A0A0");
                            $(".operation").eq(selectedFileIndex).hide();
                        }
                        selectedFileIndex=fileNum;      //新导入的是选中的
                        fileNum++;
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                        alert("上传失败");//后台out输出的值
                    }
                }
            );
        }
        else{
            alert("文件格式必须是xml或pnml！");
        }

    }
    return false;
}

var changeNum=0;
//通过点击文件，改变文件的背景颜色
function changeBgColor(i) {
    if(ifRemove){
        i=selectedFileIndex;
        changeNum++;
    }
    $(".file").eq(i).css("backgroundColor","green");
    $(".operation").eq(i).show();
    //当当前点击的不等于选中的时候，才改变之前选中的颜色
    if(i!=selectedFileIndex){
        $(".file").eq(selectedFileIndex).css("backgroundColor","#A0A0A0");
        $(".operation").eq(selectedFileIndex).hide();
        selectedFileIndex=i;
    }
    if(ifRemove&&changeNum==2)
        ifRemove=false;
}

var ifRemove=false;     //是否删除过文件
//将该（第i个）文件节点删除,fileNum也要-1
function remove(i) {
    ifRemove=true;
    $(".file").get(i).remove();
    fileNum--;
    changeNum=0;
    //如果是文件列表的最后一个
    if(selectedFileIndex==fileNum)
        selectedFileIndex--;
    changeBgColor(selectedFileIndex);
}

//删除后需要将索引重排，否则后续的删除操作会出错
//改变onclick函数中传的参数
function resetIndex() {
    for(var i=0;i<$(".file").length;i++){
        $(".file").eq(i).attr("onclick","changeBgColor("+i+")");
        $(".remove").eq(i).attr("onclick","remove("+i+")");
    }
}



