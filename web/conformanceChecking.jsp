<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Conformance Checking</title>
    <link type="text/css" href="css/conformance_checking.css" rel="stylesheet">
    <link type="text/css" href="css/workspace.css" rel="stylesheet">
    <link type="text/css" href="css/action.css" rel="stylesheet">
    <link type="text/css" href="css/view.css" rel="stylesheet">
    <link type="text/css" href="css/result.css" rel="stylesheet">
</head>
<body>
    <div id="selectDialog" hidden="hidden">
        <div id="dialogTitle"></div>
        <div id="dialogFile"></div>
        <div>
            <div id="cancel" class="dialogOption"><div><img src="images/cancel_white_30x30.png"></div><div>cancel</div></div>
            <div id="select" class="dialogOption"><div><img src="images/ok_white_30x30.png"></div><div>select</div></div>
        </div>
    </div>
    <!--遮罩，使对话框出现时不能操作其他-->
    <div id="main_mask" hidden="hidden"></div>
    <header id="navigation">
        <div id="logo">
            <span>Conformance</span>
            <span>Checking</span>
        </div>
        <div class="menu" id="menu1" onclick="changePage(1)" onmouseover="change1(1)" onmouseout="change2(1)">
            <img  src="images/workspace_40x30_white.png" id="white1">
            <img  src="images/workspace_40x30_black.png" id="black1">
        </div>
        <div class="menu" id="menu2" onclick="changePage(2)" onmouseover="change1(2)" onmouseout="change2(2)">
            <img  src="images/action_40x30_white.png" id="white2">
            <img  src="images/action_40x30_black.png" id="black2">
        </div>
        <div class="menu" id="menu3" onclick="changePage(3)" onmouseover="change1(3)" onmouseout="change2(3)">
            <img  src="images/view_40x30_white.png" id="white3">
            <img  src="images/view_40x30_black.png" id="black3">
        </div>
        <div id="designer"></div>
    </header>
    <div class="page" id="workspace">
        <div class="title">
            <span class="left_title">Workspace</span>
            <div class="right_title" id="improt">
                <div><img src="images/import_30x30_white.png"></div>
                <%--input须在import之前--%>
                <input type="file" id="file" name="file" title="import" onchange="importFile()" />
                <div onclick="return selectFile()">import</div>
               <!-- <form method="post" id="importForm" enctype="multipart/form-data">
                <%--<form method="post" id="importForm" action="/importFile" enctype="multipart/form-data">--%>
                    <input type="file" id="file" title="import" style="display:none" onchange="importFile()">
                    <div id="submit" value="import" onclick="selectFile()">import</div>
                    <%--&lt;%&ndash;<input type="button" id="submit" value="import" onclick="selectFile()">&ndash;%&gt;--%>
                </form>-->
            </div>
        </div>
        <article>
            <div class="fileList"></div>
        </article>
    </div>
    <!--运行算法模块-->
    <div class="page" id="action">
        <div class="title">
            <span class="left_title">Action</span>
        </div>
        <article>
            <div class="setModule">
                <div class="selectInput" id="modelFile" onclick="showDialog(0)">Click to select model file</div>
                <div class="selectInput" id="logFile" onclick="showDialog(1)">Click to select log file</div>
                <div class="selectInput" id="selectWeight">
                    Select the weight(event-data)：
                    <select id="weight" size="1" name="weight">
                        <option selected="selected">---</option>
                        <option>0-1</option>
                        <option>0.1-0.9</option>
                        <option>0.2-0.8</option>
                        <option>0.3-0.7</option>
                        <option>0.4-0.6</option>
                        <option>0.5-0.5</option>
                        <option>0.6-0.4</option>
                        <option>0.7-0.3</option>
                        <option>0.8-0.2</option>
                        <option>0.9-0.1</option>
                        <option>1-0</option>
                    </select>
                </div>
                <span id="resultButton" class="actionButton">Result</span>
                <span id="startButton" class="actionButton">Start</span>
            </div>
            <div id="runningProcess">
                <div id="process" hidden="hidden">
                    <div class="canvasName">Process Net</div>
                    <div id="processNet" class="net" style="height: 500px;background-color: white">
                        <canvas id="processNetCanvas" class="canvas" width="1200" height="500"></canvas>
                    </div>
                    <div class="canvasName">
                        <div id="pre"><img src="images/prev_white_30x30.png" title="pre"></div>
                        <div id="eventNetName">Event Net</div>
                        <div id="next"><img src="images/next_white_30x30.png" title="next"></div>
                    </div>
                    <div id="eventNet" class="net" style="height: 200px;background-color: white">
                        <canvas id="eventNetCanvas" class="canvas" width="1200" height="200"></canvas>
                    </div>
                    <div class="canvasName">State Graph</div>
                    <div id="stateGraph" class="net" style="height: 400px">
                        <canvas id="stateGraphNetCanvas" class="canvas" width="1200" height="400"></canvas>
                        <%--<canvas id="alignmentNetCanvas" class="canvas" width="4000" height="1500"></canvas>--%>
                    </div>
                    <div class="canvasName">Result Table</div>
                    <div id="resultTable" style="background-color: white;width: 1200px;margin: auto"></div>
                </div>
            </div>
        </article>
    </div>
    <!--没有文件时的显示-->
    <div class="page" id="view">
        <div class="title">
            <span class="left_title">View</span>
        </div>
        <!--<article></article>-->
        <article style="width:1350px;height:580px;background: url('images/tile_corkboard.jpg')"></article>
    </div>
    <!--查看模型-->
    <div class="page" id="viewModel">
        <div class="title">
            <span class="left_title">viewModel</span>
        </div>
        <article>
            <div id="viewModelNet">
                <%--只有浏览器不支持时才会显示中间的文本--%>
                <canvas id="modelCanvas" class="canvas" width="1348" height="525">您的浏览器不支持canvas绘图</canvas>
            </div>
        </article>
    </div>
    <!--查看日志-->
    <div class="page" id="viewLog">
        <div class="title">
            <span class="left_title">viewLog</span>
        </div>
    </div>
    <!--查看结果模块-->
    <div class="page" id="viewResult">
        <div class="title">
            <span class="left_title">Conformance Checking Result</span>
            <select id="form" size="1" name="form" onchange="formChange()">
                <option selected="selected">Model-log Alignment result</option>
                <option>Table Result</option>
            </select>
        </div>
        <article>
            <div id="form0">
                <div id="leftResult" class="result">
                    <div id="leftResult_top" class="name"><b>ALIGNMENTS</b></div>
                    <div id="selectButton">
                        <div id="selectAll">Select all</div>
                        <div id="deSelectAll">Deselect all</div>
                    </div>
                    <div id="alignments"></div>
                </div>
                <div id="rightResult" class="result">
                    <div id="legend" class="name"><b>LEGEND</b></div>
                    <div id="legends" class="right_section">
                        <span class="colorSquare" style="background-color: #00bc00"></span>&nbsp;- Perfect alignment(Move log and model)<br/>
                        <span class="colorSquare" style="background-color: mediumpurple"></span>&nbsp;- Skipped Event(Move model only)<br/>
                        <span class="colorSquare" style="background-color: grey"></span>&nbsp;- Invisible Transition(Move model only)<br/>
                        <span class="colorSquare" style="background-color: orange"></span>&nbsp;- Inserted Event(Move log only)<br/>
                        <span class="colorSquare" style="background-color: lightgreen"></span>&nbsp;- Wrong Data(Move log and model with different data values)<br/>
                    </div>
                    <br/>
                    <div id="alignment_statistics" class="name"><b>ALIGNMENT STATISTICS</b></div>
                    <div id="statistics" class="right_section">
                        <div id="statisticsTitle">
                            <span>Average Fitness</span><br>
                            <span>Count Traces</span>
                        </div>
                        <div>
                            <span id="fitness"></span><br/>
                            <span id="count"></span>
                        </div>
                    </div>
                    <br/>
                    <br/>
                    <br/>
                    <div id="attributes_variables" class="name"><b>ATTRIBUTES/VARIABLES</b></div>
                    <div id="attributes" class="right_section">
                        <div id="attributesTitle"><span>Name</span><span>Log Value</span><span>Process Value</span></div>
                        <div id="attributesTableContent">
                            <div id="Name">
                                <!--<span>Event Name</span><br/>-->
                                <!--<span>--Attributes--</span>-->
                            </div>
                            <div id="LogValue"></div>
                            <div id="ProcessValue"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="form1" hidden="hidden"></div>
        </article>
    </div>
</body>
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/conformance_checking.js"></script>
<script type="text/javascript" src="js/drawPetriNet.js"></script>
<script type="text/javascript" src="js/drawStateGraph.js"></script>
<script type="text/javascript" src="js/produceTable.js"></script>
<script type="text/javascript" src="js/result.js"></script>
<script type="text/javascript" src="js/workspace.js"></script>
<script type="text/javascript" src="js/action.js"></script>
</html>