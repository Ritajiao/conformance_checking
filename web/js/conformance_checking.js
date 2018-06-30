//设置页面显示与否
var currentPage=1;
$("#page"+currentPage).show();
$("#menu"+currentPage).css("background","black");
$("#white"+currentPage).css("opacity","1");
$("#black"+currentPage).css("opacity","0");
for(var k=2;k<=6;k++){
    $("#menu"+k).css("background"," -webkit-linear-gradient(#888, #777)");
    $(".page").eq(k-1).hide();
    $("#white"+k).css("opacity","0");
    $("#black"+k).css("opacity","1");
}
//点击按钮，切换页面
function changePage(i) {
    currentPage=i;
    for(j=1;j<=6;j++){
        if(j!=i){
            $("#menu"+j).css("background"," -webkit-linear-gradient(#888, #777)");
            $(".page").eq(j-1).hide();
            $("#white"+j).css("opacity","0");
            $("#black"+j).css("opacity","1");
        }
        else{
            $(".page").eq(i-1).show();
            var k=i;
            if(i>3){
                //让第三个按钮再单独点击时，显示上一次的东西
                $("#menu3").attr("onclick","changePage("+i+")");
                $("#menu3").attr("onmouseover","change1("+i+")");
                $("#menu3").attr("onmouseout","change2("+i+")");
                k=3;        //让其在第三个页面显示
            }

            $("#menu"+k).css({"background":" black","opacity":"1"});
            $("#white"+k).css("opacity","1");
            $("#black"+k).css("opacity","0");
        }
    }
}
//menu的onmouseover
function change1(i) {
    if(i!=currentPage){
        $("#menu"+i).css({"background":" black","opacity":"0.6"});
        $("#white"+i).css("opacity","0.6");
        $("#black"+i).css("opacity","0");
    }
}
//menu的onmouseout
function change2(i) {
    if(i!=currentPage){
        $("#menu"+i).css({"background":"  -webkit-linear-gradient(#888, #777)","opacity":"1"});
        $("#white"+i).css("opacity","0");
        $("#black"+i).css("opacity","1");
    }
}



