package graph;

import java.util.*;

public class Vertex<T> implements VertexInterface<T>, java.io.Serializable{
	private T label;		//顶点 的类型，String/int...
	private List<Edge> edgeList;//到该顶点邻接点的边,实际以java.util.LinkedList存储
	private boolean visited;//标识顶点是否已访问
	private VertexInterface<T> fatherVertex;//该顶点的父节点-前驱顶点

	//评估函数，F=G+H
	private double F;
	private double G;		//已知距离
	private double H;		//预测距离
	private int vertexId;        //index
	private int vertexType;         //节点类型，开始-0，中间-1，结束-2


	@Override
	public double getF() {
		return G+H;
	}

	@Override
	public double getG() {
		return G;
	}

	@Override
	public void setH(VertexInterface<T> origin,int Llength) {
		int Llength1= getLlength1(origin);
		H =Math.abs(Llength-Llength1);		//预估函数是返回还未添加的步骤数
	}

//    @Override
//    public void setVertexId(int vertexId){
//        this.vertexId=vertexId;
//    }

    @Override
    public int getVertexId(){
        return vertexId;
    }

    @Override
    public int getVertexType(){
        return vertexType;
    }


	//获得从源点到当前节点的最短的距离（即在对齐中已经添加的活动数目）
    public int getLlength1(VertexInterface<T> origin){
//		LinkedList<VertexInterface<T>> result = new LinkedList<VertexInterface<T>>();
		VertexInterface<T> vertex = this;
		int Llength1=0;
		while(!vertex.equals(origin)){
			if(vertex.getFatherVertex() == null)
				break;
			VertexInterface<T> vertex1=vertex.getFatherVertex();
			String name=vertex1.getEdge(vertex).getName().split(",")[1];
			if(!name.contains(">>"))
				Llength1++;
			vertex = vertex.getFatherVertex();
		}
		return Llength1;
	}

	@Override
	public double calG(VertexInterface<T> fatherVertex){
		double distance=fatherVertex.getEdge(this).getWeight();
		return fatherVertex.getG()+distance;	//G在父节点的基础上相加
	}
	//构建函数
	public Vertex(T vertexLabel,int vertexId,int vertexType){
		label = vertexLabel;
		edgeList = new LinkedList<Edge>();//是Vertex的属性,说明每个顶点都有一个edgeList用来存储所有与该顶点关系的边
		visited = false;
		fatherVertex = null;
		F=0.0;
		G=0.0;
		H=0.0;
		this.vertexId=vertexId;
        this.vertexType=vertexType;
//		cost = 0;
	}

	/**
	 *Task: 这里用了一个单独的类来表示边,主要是考虑到带权值的边
	 *Edge类封装了一个顶点和一个double类型变量
	 */
	public class Edge implements java.io.Serializable {
		private VertexInterface<T> beginVertex;// 起点
		private VertexInterface<T> endVertex;// 终点
		private double weight;//权值
		private String name;
		private int edgeType;

		//Vertex 类本身就代表顶点对象,因此在这里只需提供 endVertex，就可以表示一条边了
		protected Edge(VertexInterface<T> endVertex1, double edgeWeight,String edgeName,int edgeType){
            endVertex = endVertex1;
			weight = edgeWeight;
			name=edgeName;
            this.edgeType=edgeType;
		}

		protected double getWeight(){
			return weight;
		}
		protected void setBeginVertex(VertexInterface<T> beginVertex1){
		    beginVertex=beginVertex1;
		}
        public VertexInterface<T> getBeginVertex(){
            return beginVertex;
        }
        public VertexInterface<T> getEndVertex(){
            return endVertex;
        }
		public String getName(){
			return name;
		}
		public int getEdgeType(){
			return edgeType;
		}
	}

	@Override
	public Edge getEdge(VertexInterface<T> endVertex){
		Iterator<Edge> edgesIterator=edgeList.iterator();
		Edge edgeToNextNeighbor=null;
		while(edgesIterator.hasNext()){
			edgeToNextNeighbor = edgesIterator.next();//LinkedList中存储的是Edge
			VertexInterface<T> nextNeighbor = edgeToNextNeighbor.getEndVertex();//从Edge对象中取出顶点
			if(nextNeighbor.equals(endVertex))
				break;
		}
		return edgeToNextNeighbor;
	}

//	@Override
//	public List<Edge> getEdgeList(){
//	    return edgeList;
//    }

	/**Task: 遍历该顶点邻接点的迭代器--为 getNeighborInterator()方法 提供迭代器
	 * 由于顶点的邻接点以边的形式存储在java.util.List中,因此借助List的迭代器来实现
	 */
	private class NeighborIterator implements Iterator<VertexInterface<T>>{

		Iterator<Edge> edgesIterator;
		private NeighborIterator() {
			edgesIterator = edgeList.iterator();//获得 LinkedList 的迭代器
		}
		@Override
		public boolean hasNext() {
			return edgesIterator.hasNext();
		}

		@Override
		public VertexInterface<T> next() {
			VertexInterface<T> nextNeighbor = null;
			if(edgesIterator.hasNext()){
				Edge edgeToNextNeighbor = edgesIterator.next();//LinkedList中存储的是Edge
				nextNeighbor = edgeToNextNeighbor.getEndVertex();//从Edge对象中取出顶点
			}
			else
				throw new NoSuchElementException();
			return nextNeighbor;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**Task: 生成一个遍历该顶点所有邻接边的权值的迭代器
	 * 权值是Edge类的属性,因此先获得一个遍历Edge对象的迭代器,取得Edge对象,再获得权值
	 */
	private class WeightIterator implements Iterator{

		private Iterator<Edge> edgesIterator;
		private WeightIterator(){
			edgesIterator = edgeList.iterator();
		}
		@Override
		public boolean hasNext() {
			return edgesIterator.hasNext();
		}
		@Override
		public Object next() {
			Double result;
			if(edgesIterator.hasNext()){
				Edge edge = edgesIterator.next();
				result = edge.getWeight();
			}
			else throw new NoSuchElementException();
			return (Object)result;//从迭代器中取得结果时,需要强制转换成Double
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean connect(VertexInterface<T> endVertex, int edgeWeight,String edgeName,int edgeType) {
		// 将"边"(边的实质是顶点)插入顶点的邻接表
		boolean result = false;
		if(!this.equals(endVertex)){//顶点互不相同
			Iterator<VertexInterface<T>> neighbors = this.getNeighborIterator();
			boolean duplicateEdge = false;
			while(!duplicateEdge && neighbors.hasNext()){//保证不添加重复的边
				VertexInterface<T> nextNeighbor = neighbors.next();
				if(endVertex.equals(nextNeighbor)){
					duplicateEdge = true;
					break;
				}
			}
			if(!duplicateEdge){
			    Edge edge=new Edge(endVertex, edgeWeight,edgeName,edgeType);
			    edge.setBeginVertex(this);
				edgeList.add(edge);//添加一条新边
				result = true;
			}
		}
		return result;
	}

	@Override
	public VertexInterface<T> getUnvisitedNeighbor() {
		VertexInterface<T> result = null;
		Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
		while(neighbors.hasNext() && result == null){//获得该顶点的第一个未被访问的邻接点
			VertexInterface<T> nextNeighbor = neighbors.next();
			if(!nextNeighbor.isVisited())
				result = nextNeighbor;
		}
		return result;
	}

	//判断两个顶点是否相同
	public boolean equals(Object other){
		boolean result;
		if((other == null) || (getClass() != other.getClass()))
			result = false;
		else{
			Vertex<T> otherVertex = (Vertex<T>)other;
			result = label.equals(otherVertex.label);//节点是否相同最终还是由标识 节点类型的类的equals() 决定
		}
		return result;
	}

	@Override
	public T getLabel() {
		return label;
	}

	@Override
	public void visit() {
		this.visited=true;
	}

	@Override
	public void unVisit() {
		this.visited=false;
	}

	@Override
	public boolean isVisited() {
		return visited;
	}

	@Override
	public Iterator<VertexInterface<T>> getNeighborIterator() {
		return new NeighborIterator();
	}

	@Override
	public Iterator<Double> getWeightIterator() {
		return new WeightIterator();
	}

//	@Override
//	public boolean connect(VertexInterface<T> endVertex) {
//		return connect(endVertex,0);
//	}

	@Override
	public boolean hasNeighbor() {
		return !(edgeList.isEmpty());		//邻接点存储在list中
	}

	@Override
	public void setFatherVertex(VertexInterface<T> fatherVertex) {
		this.fatherVertex=fatherVertex;
		//更新F、G、H
		this.G=fatherVertex.getG()+fatherVertex.getEdge(this).getWeight();
		this.F=this.G+this.H;
	}

	@Override
	public VertexInterface<T> getFatherVertex() {
		return this.fatherVertex;
	}

	@Override
	public boolean hasFatherVertex() {
		return this.fatherVertex!=null;
	}
}
