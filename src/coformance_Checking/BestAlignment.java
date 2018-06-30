package coformance_Checking;

import graph.DirectedGraph;
import graph.GraphInterface;
import graph.VertexInterface;
import object.PNArc;
import object.PetriNet;
import object.Place;
import object.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BestAlignment {
	//记录图的起始点和终止点
	private static ArrayList<String> originalVertexLabel;
	private static ArrayList<String> endVertexLabel;

	//根据库所，去找变迁
	public static List<Transition> findTransition(List<PNArc> pnArcs,String name,String direction){
		List<Transition> transitions=new ArrayList<Transition>();
		Transition transition=new Transition();
		if(direction.equals("source")) {
			for(PNArc pnArc:pnArcs) {
				if(pnArc.getTarget().getName().equals(name.trim())) {
					transition=(Transition) pnArc.getSource();
					transitions.add(transition);
				}
			}
		}
		else {
			for(PNArc pnArc:pnArcs) {
				if(pnArc.getSource().getName().equals(name.trim())) {
					transition=(Transition) pnArc.getTarget();
					transitions.add(transition);
				}
			}
		}
		return transitions;
	}

	//通过变迁名找库所名
	public static ArrayList<String> findPlace(List<PNArc> pnArcs,String id,String direction){
		ArrayList<String> places=new ArrayList<String>();
		Place place=new Place();
		if(direction.equals("source")) {
			for(PNArc pnArc:pnArcs) {
				if(pnArc.getTarget().getId().equals(id.trim())) {
					place=(Place) pnArc.getSource();
					places.add(place.getName());
				}
			}
		}
		else {
			for(PNArc pnArc:pnArcs) {
				if(pnArc.getSource().getId().equals(id.trim())) {
					place=(Place) pnArc.getTarget();
					places.add(place.getName());
				}
			}
		}
		return places;
	}

    /**
     * move采用递归的方式，通过三种类型的移动，添加节点和边，生成图
     * @param graph
     * @param eventArcs  事件网的弧
     * @param processArcs 流程网的弧
     * @param vertexLabel   当前的节点
     * @param endVertexLabel 结束节点
     * @param processPlace    当前节点的流程网部分的库所列表，可能不止一个库所
     * @param processEndName    结束节点中的流程网中的名字
     * @param eventEndName
     */
	@SuppressWarnings("unchecked")
	public static void move(GraphInterface<ArrayList<String>> graph,ArrayList<PNArc> eventArcs,ArrayList<PNArc> processArcs,ArrayList<String> vertexLabel,ArrayList<String> endVertexLabel,ArrayList<String> processPlace,String processEndName,String eventEndName) {
//		System.out.println("##"+vertexLabel);
		//这里是用if,而不是用while
		//直到找到最终的节点
		if(!processPlace.get(0).equals(processEndName)||!vertexLabel.get(0).equals(eventEndName)) {
            String tId="";
            String tName="";
            String eventPlace="";
            List<Transition> transitions=findTransition(eventArcs,vertexLabel.get(0),"target");		//在事件网中找到变迁
            //若size=0,则说明在事件网中到最后了，不能再只在日志中移动
            if(transitions.size()!=0) {
                tId=transitions.get(0).getId();		//肯定只能找到一个
                tName=transitions.get(0).getName();
                eventPlace=findPlace(eventArcs, tId, "target").get(0);
                ArrayList<String> vertexLabel1=(ArrayList<String>) vertexLabel.clone();		//arrayList是对象需要采用clone方法，但只是浅复制
//				vertexLabel1=vertexLabel;

                //只在日志中移动
                vertexLabel1.remove(0);
                vertexLabel1.add(0, eventPlace);
                //每次添加都可能是结束节点
                //需要判断是否是结束节点，type不一样
                if(vertexLabel1.equals(endVertexLabel))
                    graph.addVertex(vertexLabel1,2);
                else
                    graph.addVertex(vertexLabel1,1);
                String edgeName1="("+tId+",>>)";
//                System.out.println("creating:"+vertexLabel+edgeName1+vertexLabel1);
//                System.out.println("vertex:"+graph.getNumberOfVertices());
                boolean exist1=graph.addEdge(vertexLabel, vertexLabel1, 1, edgeName1,0);
                //如果此条边已经添加，则没有必要再递归，这个可以提升算法运行的效率
                if(!exist1)
                    return;
                //只在日志中移动的递归
                move(graph,eventArcs,processArcs,vertexLabel1,endVertexLabel,processPlace,processEndName,eventEndName);
            }

			//在模型中移动
			for(int i=0;i<processPlace.size();i++) {
				List<Transition> transitions1=findTransition(processArcs,processPlace.get(i),"target");
				if(transitions1.size()!=0) {
					for(Transition t:transitions1) {
						//先判断是否是and-join结构
						ArrayList<String> processPlace3=findPlace(processArcs, t.getId(), "source");
						if (processPlace3.size()>1){
							t.setAndJoin(true);
						}

						//只在模型中移动
						ArrayList<String> vertexLabel2= (ArrayList<String>) vertexLabel.clone();
						ArrayList<String> processPlace2=findPlace(processArcs, t.getId(), "target");


						//list类型是对象，不能直接复制，否则会改变值，需要拷贝
						ArrayList<String> processPlace1= (ArrayList<String>) processPlace.clone();
						if(t.isAndJoin()) {
							//只有到达该发生and-join结构时，才发生该变迁，否则跳出本次循环
							if(processPlace1.containsAll(processPlace3)) {
								processPlace1.removeAll(processPlace3);
								i += processPlace3.size();
							}
							else
								break;
						}
						else
							processPlace1.remove(i);
						for(String p:processPlace2)
							processPlace1.add(p);
						vertexLabel2.removeAll(processPlace);	//要把模型中的状态都移除，再添加
						vertexLabel2.addAll(processPlace1);
                        if(vertexLabel2.equals(endVertexLabel))
                            graph.addVertex(vertexLabel2,2);
                        else
                            graph.addVertex(vertexLabel2,1);
						String edgeName2="(>>,"+t.getId()+")";
						boolean exist2=true;
						if(t.isHiden())
                            exist2=graph.addEdge(vertexLabel, vertexLabel2, 0, edgeName2,3);		//如果是隐藏任务，成本为0
						else
                            exist2=graph.addEdge(vertexLabel, vertexLabel2, 1, edgeName2,1);

						//同时移动
						if(t.getName().equals(tName)) {			//如果名字相同，则认为会发生同步移动
							ArrayList<String> vertexLabel3= (ArrayList<String>) vertexLabel.clone();
							vertexLabel3.remove(0);
							vertexLabel3.add(0, eventPlace);
							vertexLabel3.removeAll(processPlace);
							vertexLabel3.addAll(processPlace1);
                            if(vertexLabel3.equals(endVertexLabel))
                                graph.addVertex(vertexLabel3,2);
                            else
                                graph.addVertex(vertexLabel3,1);
							String edgeName3="("+tId+","+t.getId()+")";
                            boolean exist3=graph.addEdge(vertexLabel, vertexLabel3, 0, edgeName3,2);		//同时发生成本为0
//							System.out.println("creating:"+vertexLabel+edgeName3+vertexLabel3);
//							System.out.println("vertex:"+graph.getNumberOfVertices());
							if(!exist3)
							    return;
							// 同时移动的递归
                            move(graph,eventArcs,processArcs,vertexLabel3,endVertexLabel,processPlace1,processEndName,eventEndName);
						}

//						System.out.println("creating:"+vertexLabel+edgeName2+vertexLabel2);
//						System.out.println("vertex:"+graph.getNumberOfVertices());
                        if(!exist2)
                            return;
                        //只在模型中移动的递归
						move(graph,eventArcs,processArcs,vertexLabel2,endVertexLabel,processPlace1,processEndName,eventEndName);

					}
				}
			}
		}
	}

	public static GraphInterface<ArrayList<String>> createStateGraph(PetriNet processNet,PetriNet eventNet){
		GraphInterface<ArrayList<String>> graph = new DirectedGraph<>();

        ArrayList<PNArc> processArcs=processNet.getPnArcs();
        ArrayList<PNArc> eventArcs=eventNet.getPnArcs();

        Place processStart=new Place();
        Place processEnd=new Place();
        for(Place p:processNet.getPlaces()) {
            if(findTransition(processArcs,p.getName(),"source").size()==0) {
                processStart=p;
            }
            if(findTransition(processArcs,p.getName(),"target").size()==0) {
                processEnd=p;
            }
        }
        Place eventStart=new Place();
        Place eventEnd=new Place();
        for(Place p:eventNet.getPlaces()) {
            if(findTransition(eventArcs,p.getName(),"source").size()==0) {
                eventStart=p;
            }
            if(findTransition(eventArcs,p.getName(),"target").size()==0) {
                eventEnd=p;
            }
        }

//        ArrayList<String> originalVertexLabel=new ArrayList<String>();
        originalVertexLabel=new ArrayList<String>();
        ArrayList<String> processPlace=new ArrayList<String>();
        processPlace.add(processStart.getName());
        String eventPlace=eventStart.getName();
        originalVertexLabel.add(eventPlace);
        originalVertexLabel.addAll(processPlace);
        graph.addVertex(originalVertexLabel,0);		//源点
//		System.out.println(originalVertexLabel.toString()+"!!!");

//        ArrayList<String> endVertexLabel=new ArrayList<String>();
        endVertexLabel=new ArrayList<String>();
        endVertexLabel.add(eventEnd.getName());
        endVertexLabel.add(processEnd.getName());

        //创建顶点和边
        move(graph,eventArcs,processArcs,originalVertexLabel,endVertexLabel,processPlace,processEnd.getId(),eventEnd.getId());

        return graph;
    }

	/*创建状态图，并找最佳对齐
	 *@param processNet 流程网
	 * @param processNet 事件日志网
	 */
	public static ArrayList<String> createAndFind(PetriNet processNet,PetriNet eventNet) {
        GraphInterface<ArrayList<String>> graph=createStateGraph(processNet,eventNet);
		int Llength=eventNet.getTransitions().size();
        Stack<VertexInterface<ArrayList<String>>> bestPath=graph.findBestAlignment(originalVertexLabel,endVertexLabel,Llength);
//		System.out.println(bestPath.size());
//		int cost=0;
        ArrayList<String> alignment=new ArrayList<>();
        while(!bestPath.isEmpty()){
            VertexInterface<ArrayList<String>> v=bestPath.pop();
            if(v.hasFatherVertex()) {
                String name=v.getFatherVertex().getEdge(v).getName();
                alignment.add(name);
//				System.out.println(name);
//				if(name.contains(">>"))
//				    cost++;
            }
//			System.out.println(v.getLabel());
        }
//		System.out.println("cost:"+cost);
        return alignment;

	}
}
