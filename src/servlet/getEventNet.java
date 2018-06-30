package servlet;

import coformance_Checking.GetEventNet;
import coformance_Checking.GetLogTraces;
import create.CreateNetJsonObject;
import net.sf.json.JSONArray;
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
import java.util.List;

@WebServlet(name = "getEventNet")
//getProcessNet类是获取自己生成的事件网的元素
public class getEventNet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        javax.servlet.RequestDispatcher rd=null;

        ServletContext sc = getServletContext();
        String path=sc.getRealPath("/");

        //获得前台的数据
        String fileName=request.getParameter("fileName");
        System.out.println("logName:"+fileName);

//        String LogPath=path+"WEB-INF/classes/xml/"+fileName;
        String LogPath=path+"importFile/"+fileName;
        System.out.println("logFilePath:"+LogPath);

        //获得日志中的路径
        List<LogTrace> traces= GetLogTraces.getLogTraces(LogPath);
//        PetriNet eventNet = GetEventNet.getEventNet(traces.get(0));
//        JSONObject json= CreateNetJsonObject.CreateNetJsonObject(eventNet);
        JSONObject json=new JSONObject();
        JSONArray eventNetArray=new JSONArray();
        for(LogTrace trace:traces) {
            PetriNet eventNet = GetEventNet.getEventNet(trace);
            JSONObject eventNetJson= CreateNetJsonObject.CreateJsonObject(eventNet);
            eventNetArray.add(eventNetJson);
        }
        json.put("eventNets",eventNetArray);
        System.out.println("eventNet:"+json.toString());

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
