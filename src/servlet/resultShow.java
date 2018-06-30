package servlet;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coformance_Checking.Fitness;
import coformance_Checking.GetLogTraces;
import coformance_Checking.GetProcessNet;
import object.LogEvent;
import object.LogTrace;
import object.PetriNet;
import object.Result;

/**
 * Servlet implementation class resultShow
 * 获得结果
 */
//@WebServlet("/resultShow")
public class resultShow extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public resultShow() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		javax.servlet.RequestDispatcher rd=null;

		ServletContext sc = getServletContext();
		String path=sc.getRealPath("/");
//		System.out.println("****"+sc.getRealPath("/"));
		String WF_netPath=path+"WEB-INF/classes/PetriNet/netnew.pnml";
		String LogPath=path+"WEB-INF/classes/xml/Lnew.xml";
		PetriNet processNet=GetProcessNet.getProcessNet(WF_netPath);
		List<LogTrace> traces=GetLogTraces.getLogTraces(LogPath);

		double w1=0.5;
		double w2=0.5;

		double fitness=0;
		int totalNumber=0;

		List<Result> results=new ArrayList<Result>();

//        List<Transition> processTransitions=processNet.getTransitions();
		int i=0;
		for(LogTrace trace:traces) {
			i++;
			Result result=new Result();
			int number=trace.getNumber();
			totalNumber+=number;
			List<LogEvent> events=trace.getEvents();
			System.out.print(i+":");
			for(LogEvent event:events)
				System.out.print(event.getName());
			System.out.println();
			result=Fitness.computeFitness(processNet, trace, w1, w2);
			fitness=fitness+Double.valueOf(result.getfEStr());
			results.add(result);
		}
		fitness=fitness/totalNumber;
//        System.out.println("fitness:"+fitness);
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		String fitness1=nf.format(fitness);
		System.out.println("日志-模型拟合度fitness(L,M)="+fitness1);

		request.setAttribute("results", results);
		request.setAttribute("fitness", fitness1);
		rd=request.getRequestDispatcher("/resultShow.jsp");
		rd.forward(request, response);
//		doPost(request,response);
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
