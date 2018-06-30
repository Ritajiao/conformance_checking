package servlet;

import coformance_Checking.GetProcessNet;
import create.CreateNetJsonObject;
import object.PetriNet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//import org.json.JSONObject;
import net.sf.json.*;
@WebServlet(name = "getProcessNet")
//getProcessNet类是获取流程网的元素
public class getProcessNet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        javax.servlet.RequestDispatcher rd=null;

        ServletContext sc = getServletContext();
        String path=sc.getRealPath("/");

        //获得前台的数据
        String fileName=request.getParameter("fileName");
        System.out.println("modelName:"+fileName);

        String WF_netPath=path+"importFile/"+fileName;
//        String WF_netPath=path+"WEB-INF/classes/PetriNet/"+fileName;
        System.out.println("modelFilePath:"+WF_netPath);

        //getProcessNet获得petri网的元素
        PetriNet processNet= GetProcessNet.getProcessNet(WF_netPath);

        response.setContentType("application/json");          //设置请求以及响应的内容类型以及编码方式
        response.setCharacterEncoding("UTF-8");
//        JSONObject json=JSONObject.fromObject (processNet);

        //调用getProcessNet函数，将petri网的元素生成json格式，以便前台使用
        JSONObject json= CreateNetJsonObject.CreateJsonObject(processNet);
        System.out.println("processNet:"+json.toString());
//        JSONArray  json = JSONArray.fromObject(newsList);   //将newsList对象转换为json对象
//        String str = json.toString();                //将json对象转换为字符串
//        out.write(str);                     //将str字符传输到前台
        PrintWriter out=response.getWriter();       //向客户端发送字符数据
        out .print(json);
        out .flush();
        out .close();

//        request.setAttribute("processNet", processNet);
//        rd=request.getRequestDispatcher("/resultShow.jsp");
//        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
