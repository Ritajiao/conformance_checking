package servlet;

import coformance_Checking.Fitness;
import coformance_Checking.GetLogTraces;
import coformance_Checking.GetProcessNet;
import create.CreateResultsJsonObject;
import net.sf.json.JSONObject;
import object.LogEvent;
import object.LogTrace;
import object.PetriNet;
import object.Result;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "getResult")
public class getResults extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        javax.servlet.RequestDispatcher rd=null;

        ServletContext sc = getServletContext();
        String path=sc.getRealPath("/");

        //获得前台的数据
        String modelName=request.getParameter("modelName");
        String logName=request.getParameter("logName");
//        int netIndex=Integer.valueOf(request.getParameter("netIndex"));

//        String modelPath=path+"WEB-INF/classes/PetriNet/"+modelName;
//        String LogPath=path+"WEB-INF/classes/xml/"+logName;
        String modelPath=path+"importFile/"+modelName;
        String LogPath=path+"importFile/"+logName;
        PetriNet processNet= GetProcessNet.getProcessNet(modelPath);
        List<LogTrace> traces= GetLogTraces.getLogTraces(LogPath);

//        double w1=0.5;
//        double w2=0.5;

        double w1=Double.valueOf(request.getParameter("w1"));
        double w2=Double.valueOf(request.getParameter("w2"));

        double fitness=0;
        int totalNumber=0;

        List<Result> results=new ArrayList<Result>();

//        List<Transition> processTransitions=processNet.getTransitions();
        int i=0;
        for(LogTrace trace:traces) {
            i++;
            int number=trace.getNumber();
            totalNumber+=number;
            List<LogEvent> events=trace.getEvents();
            System.out.print(i+":");
            for(LogEvent event:events)
                System.out.print(event.getName()+",");
            System.out.println();
            Result result= Fitness.computeFitness(processNet, trace, w1, w2);
            fitness=fitness*number+Double.valueOf(result.getfEStr());		//fitness=fitness+Double.valueOf(result.getfEStr());
            results.add(result);
        }
        fitness=fitness/totalNumber;
//        System.out.println("fitness:"+fitness);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        String fitness1=nf.format(fitness);
        System.out.println("日志-模型拟合度fitness(L,M)="+fitness1);

        //创建对应的json格式
        JSONObject json= CreateResultsJsonObject.CreateResultsJsonObject(results,fitness1);
        System.out.println("results:"+json.toString());

        response.setContentType("application/json");          //设置请求以及响应的内容类型以及编码方式
        response.setCharacterEncoding("UTF-8");

        PrintWriter out=response.getWriter();       //向客户端发送字符数据
        out .print(json);
        out .flush();
        out .close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
