package servlet;

import coformance_Checking.BestAlignment;
import coformance_Checking.GetEventNet;
import coformance_Checking.GetLogTraces;
import coformance_Checking.GetProcessNet;
import create.CreateGraphJsonObject;
import graph.GraphInterface;
import net.sf.json.JSONObject;
import object.LogTrace;
import object.PetriNet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//获得状态空间变化系统图
@WebServlet(name = "getStateGraph")
public class getStateGraph extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        javax.servlet.RequestDispatcher rd=null;

        ServletContext sc = getServletContext();
        String path=sc.getRealPath("/");

        //获得前台的数据
        String modelName=request.getParameter("modelName");
//        System.out.println("modelName:"+modelName);
        String logName=request.getParameter("logName");
//        System.out.println("logName:"+logName);
        int netIndex=Integer.valueOf(request.getParameter("netIndex"));
        System.out.println("netIndex:"+netIndex);

//        String modelPath=path+"WEB-INF/classes/PetriNet/"+modelName;
//        String LogPath=path+"WEB-INF/classes/xml/"+logName;
        String modelPath=path+"importFile/"+modelName;
        String LogPath=path+"importFile/"+logName;
//        System.out.println("modelFilePath:"+modelPath);
//        System.out.println("logFilePath:"+LogPath);

        //getProcessNet获得petri网的元素
        PetriNet processNet= GetProcessNet.getProcessNet(modelPath);
        //获得日志中的路径
        List<LogTrace> traces= GetLogTraces.getLogTraces(LogPath);
        //获得路径对应的事件网
        PetriNet eventNet = GetEventNet.getEventNet(traces.get(netIndex));
        int Llength=eventNet.getTransitions().size();
        //创建状态空间图
        GraphInterface<ArrayList<String>> graph= BestAlignment.createStateGraph(processNet,eventNet);
        JSONObject json= CreateGraphJsonObject.CreateGraphJsonObject(graph,Llength);
        System.out.println("stateGraph:"+json.toString());

        response.setContentType("application/json");          //设置请求以及响应的内容类型以及编码方式
        response.setCharacterEncoding("UTF-8");

        PrintWriter out=response.getWriter();       //向客户端发送字符数据
        out .print(json);
        out .flush();
        out .close();
//        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
