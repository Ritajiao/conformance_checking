package coformance_Checking;

import java.util.ArrayList;
import java.util.List;

import object.*;

public class GetEventNet {
	//根据日志中的路径生成一个eventNet
	public static PetriNet getEventNet(LogTrace trace) {
		List<LogEvent> events=trace.getEvents();

		ArrayList<Transition> transitions=new ArrayList<Transition>();
		ArrayList<Place> places=new ArrayList<Place>();
		ArrayList<PNArc> pnArcs=new ArrayList<PNArc>();

		int X=30;
		int Y=50;
		for(int i=0;i<events.size();i++) {		//发生
			Transition t=new Transition();
			t.setId("t"+(i+1)+"'");
			t.setName(events.get(i).getName());
			//设置带有坐标信息的名字
			NodeName name=new NodeName();
			name.setText(events.get(i).getName());
            name.setOffsetX(X+70);
            name.setOffsetY(Y+40);
			t.setNodeName(name);
			//设置其坐标
			t.setX(X+70);
			t.setY(Y);
//			System.out.println("eventName:"+t.getName());
			t.setData(events.get(i).getData());
			transitions.add(t);

			Place p=new Place();
			p.setId("p"+(i+1)+"'");
			p.setName("p"+(i+1)+"'");
            //设置带有坐标信息的名字
            NodeName name1=new NodeName();
            name1.setText(p.getName());
            name1.setOffsetX(X);
            name1.setOffsetY(Y+40);
            p.setNodeName(name1);
			p.setX(X);
			p.setY(Y);
			if(i==0)
				p.setToken(1);
			places.add(p);
			PNArc arc1=new PNArc(p, t);		//p->t
            //一般没有转折点
            arc1.setTurnPoints(new ArrayList<TurnPoint>());
//            ArrayList<TurnPoint> turnPoints=new ArrayList<>();
//            arc1.setTurnPoints(turnPoints);
			pnArcs.add(arc1);
			if(i!=0&&i!=events.size()) {
				PNArc arc2=new PNArc(transitions.get(i-1), p);		//t->p
                arc2.setTurnPoints(new ArrayList<TurnPoint>());
				pnArcs.add(arc2);
			}
			X+=140;		//X坐标间隔为70，Y不变
		}
		//因为每次循环都是建立一个库所，一个变迁
		//最后还剩下一条边，一个结束库所
		Place p=new Place();		//建立结束库所
		p.setId("p"+(events.size()+1)+"'");
		p.setName("p"+(events.size()+1)+"'");
        //设置带有坐标信息的名字
        NodeName name1=new NodeName();
        name1.setText(p.getName());
        name1.setOffsetX(X);
        name1.setOffsetY(Y+40);
        p.setNodeName(name1);
		p.setX(X);
		p.setY(Y);
		places.add(p);
		PNArc arc2=new PNArc(transitions.get(events.size()-1), p);		//t->p
//        ArrayList<TurnPoint> turnPoints=new ArrayList<>();
//        arc2.setTurnPoints(turnPoints);
        arc2.setTurnPoints(new ArrayList<TurnPoint>());
		pnArcs.add(arc2);
//		for(Place p1:places){
//			System.out.print(p1.getName());
//		}
//		System.out.println();
//		for(Transition t1:transitions){
//			System.out.print(t1.getName());
//		}
//		System.out.println();


		PetriNet petriNet=new PetriNet(transitions,places,pnArcs);
		return petriNet;
	}
}
