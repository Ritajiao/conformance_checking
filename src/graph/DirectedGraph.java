package graph;

import java.util.*;

//有向图
public class DirectedGraph<T> implements GraphInterface<T>{
	private Map<T, VertexInterface<T>> vertices;//map 对象用来保存图中的所有顶点.T 是顶点标识,VertexInterface为顶点对象
	private int edgeCount;//记录图中 边的总数
	private int vertexId=-1;		//记录点的index
//	private VertexInterface<T> begin;	//记录一张图的源点
//	private VertexInterface<T> end;

	public DirectedGraph() {
		vertices = new LinkedHashMap<>();//按顶点的插入顺序保存顶点,这很重要,因为这会影响到后面图的遍历算法的正确性
	}


    @Override
    public void addVertex(T vertexLabel,int vertexType) {
        //若顶点相同时,新插入的顶点将覆盖原顶点,这是由LinkedHashMap的put方法决定的
        //每添加一个顶点,会创建一个LinkedList列表,它存储该顶点对应的邻接点,或者说是与该顶点相关联的边,即邻接表
        //==null代表如果还没有添加该节点
        if(vertices.get(vertexLabel)==null){
            vertexId++;
            //虽然put方法，不会创建重复的点，但会调用Vertex方法
            vertices.put(vertexLabel, new Vertex(vertexLabel,vertexId,vertexType));
        }
    }

	@Override
	public boolean addEdge(T begin, T end, int edgeWeight,String edgeName,int edgeType) {
		boolean result=false;
		VertexInterface<T> beginVertex=vertices.get(begin);		//获得边的起始顶点
		VertexInterface<T> endVertex=vertices.get(end);		//获得边的终点

		if(beginVertex!=null&&endVertex!=null)
			result=beginVertex.connect(endVertex, edgeWeight,edgeName,edgeType);
		if(result)
			edgeCount++;
		return result;
	}

	@Override
	public boolean hasEdge(T begin, T end) {
		boolean found=false;
		VertexInterface<T> beginVertex=vertices.get(begin);
		VertexInterface<T> endVertex=vertices.get(end);
		if(beginVertex==null||endVertex==null||beginVertex.hasNeighbor()==false)
			return found;
		Iterator<VertexInterface<T>> neighbours=beginVertex.getNeighborIterator();
		while(!found&&neighbours.hasNext()) {//在起始顶点的邻接表中查找结束顶点endVertex
			VertexInterface<T> neighbour=neighbours.next();
			if(neighbour.equals(endVertex))
				found=true;			//如果在起始顶点的邻接表中找到了终点，则说明两个点之间存在边
		}
		return found;
	}

	@Override
	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	@Override
	public int getNumberOfVertices() {
		return vertices.size();
	}

	@Override
	public int getNumberOfEdges() {
		return edgeCount;
	}

	@Override
	public void clear() {
		vertices.clear();
		edgeCount=0;
	}

    @Override
    //获得顶点集
    public Iterator<VertexInterface<T>> getVertice() {
//	    ArrayList<T> vertice=new ArrayList<>();
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator(); //获得图的顶点的迭代器
        return vertexIterator;
    }

    @Override
    //获得边的集合
    public ArrayList<Vertex.Edge> getEdges(){
	    ArrayList<Vertex.Edge> edges=new ArrayList<>();
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        //遍历图的节点
        while (vertexIterator.hasNext()){
            VertexInterface<T> nextVertex = vertexIterator.next();
//            List<Vertex.Edge> edgeList=nextVertex.getEdgeList();
//            edges.addAll(edgeList);
//
            Iterator<VertexInterface<T>> succVertices=nextVertex.getNeighborIterator();	//获得该节点的所有后继节点
            while(succVertices.hasNext()){
                VertexInterface<T> succVertex = succVertices.next();
                //获得该两点的边
                Vertex.Edge edge=nextVertex.getEdge(succVertex);
                edges.add(edge);
            }
        }
        return edges;
    }


	@Override
	//给定一个有向图、一个源节点、目标节点集、评估函数
	public Stack<VertexInterface<T>> findBestAlignment(T begin,T end,int Llength){
		//该栈保存沿最短路径的顶点,起点位于栈顶,终点位于栈底
		Stack<VertexInterface<T>> bestPath=new Stack<>();
//		Stack<String> bestPath1=new Stack<>();
		//---使用java自带的优先队列---
		//F越小，优先级越高，越先出队
		Comparator<VertexInterface<T>> comparator = new Comparator<VertexInterface<T>>() {
			@Override
			public int compare(VertexInterface<T> v1, VertexInterface<T> v2) {
				if((v1.getF()) > (v2.getF())) {
					return 1;
				} else if((v1.getF()) == (v2.getF())) {
					return 0;
				} else {
					return -1;
				}
			}
		};
		PriorityQueue <VertexInterface<T>> pqueue=new PriorityQueue<VertexInterface<T>>(comparator);//优先级队列
		ArrayList<VertexInterface<T>> visitedVertexSet=new ArrayList<>();	//访问节点列表
		VertexInterface<T> beginVertex = vertices.get(begin);//获得起始顶点
		VertexInterface<T> endVertex = vertices.get(end);//获得终点,求起始顶点到终点的最短路径
		//Adds the specified element as the tail (last element) of this list
//		priorityQueue.offer(beginVertex);		//用源点初始化优先级队列
//		beginVertex.visit();
		pqueue.offer(beginVertex);//用源点初始化优先级队列
		visitedVertexSet.add(beginVertex);
		while(!pqueue.isEmpty()){
//			VertexInterface<T> currVertex=getBestVertex(priorityQueue);	//在优先级队列中优先级最高的，即成本最短的
			VertexInterface<T> currVertex=pqueue.poll();
			if(currVertex.equals(endVertex)){
				bestPath.push(endVertex);
				VertexInterface<T> vertex = endVertex;
				while(vertex.hasFatherVertex()){
//					bestPath1.push(vertex.getFatherVertex().getEdge(vertex).getName());
					vertex=vertex.getFatherVertex();
					bestPath.push(vertex);
				}
				break;
			}
			else{
				Iterator<VertexInterface<T>> succVertices=currVertex.getNeighborIterator();	//获得该节点的所有后继节点
				while(succVertices.hasNext()){			//遍历所有后继节点
					VertexInterface<T> succVertex = succVertices.next();
					succVertex.setH(beginVertex,Llength);
					if(visitedVertexSet.contains(succVertex)){
						VertexInterface<T> v=vertices.get(succVertex.getLabel());
						double G=succVertex.calG(currVertex);
						if(G<v.getG()){
							v.setFatherVertex(currVertex);
							pqueue.offer(v);
						}
					}
					else{
						VertexInterface<T> v=vertices.get(succVertex.getLabel());
						visitedVertexSet.add(v);
						v.setFatherVertex(currVertex);
						pqueue.offer(v);
					}
				}
			}
		}
		return bestPath;
	}


	@Override
    //广度优先遍历
	public Queue<VertexInterface<T>> getBreadthFirstTraversal(T origin) {//origin 标识遍历的初始顶点
		resetVertices();//将顶点的必要数据域初始化,复杂度为O(V)
		Queue<VertexInterface<T>> vertexQueue = new LinkedList<>();//保存遍历过程中遇到的顶点,它是辅助遍历的,有出队列操作
		Queue<VertexInterface<T>> traversalOrder = new LinkedList<>();//保存遍历过程中遇到的 顶点标识--整个图的遍历顺序就保存在其中,无出队操作
		VertexInterface<T> originVertex = vertices.get(origin);//根据顶点标识获得初始遍历顶点
		originVertex.visit();//访问该顶点
		traversalOrder.offer(originVertex);
		vertexQueue.offer(originVertex);

		while(!vertexQueue.isEmpty()){
			VertexInterface<T> frontVertex = vertexQueue.poll();//出队列,poll()在队列为空时返回null
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
			while(neighbors.hasNext())//对于 每个顶点都遍历了它的邻接表,即遍历了所有的边,复杂度为O(E)
			{
				VertexInterface<T> nextNeighbor = neighbors.next();
				if(!nextNeighbor.isVisited()){
					nextNeighbor.visit();//广度优先遍历未访问的顶点
					traversalOrder.offer(nextNeighbor);
					vertexQueue.offer(nextNeighbor);//将该顶点的邻接点入队列
				}
			}//end inner while
		}//end outer while
		return traversalOrder;
	}

    //设置顶点的初始状态,时间复杂度为O(V)
    protected void resetVertices(){
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while(vertexIterator.hasNext()){
            VertexInterface<T> nextVertex = vertexIterator.next();
            nextVertex.unVisit();
//            nextVertex.setCost(0);
//            nextVertex.setFatherVertex(null);
        }//end while
    }

	//get node with the minimum total distance+underestimation to the nearest target node,
	// 从pqueue中选择F最小的
//	public VertexInterface<T> getBestVertex(Queue<VertexInterface<T>> queue){
//		VertexInterface<T> bestVertex=null;
//		double bestF=Double.MAX_VALUE;
//		for(VertexInterface<T> vertex:queue){
//			if(vertex.getF()<bestF){
//				bestF=vertex.getF();
//				bestVertex=vertex;
//			}
//		}
//		return bestVertex;
//	}
}
