package graph;

import java.util.Iterator;
import java.util.List;

public interface VertexInterface<T> {

	//获得评估值
	public double getF();

	public double getG();

	public double calG(VertexInterface<T> fatherVertex);

	public void setH(VertexInterface<T> origin,int Llength);

//    public void setVertexId(int vertexId);

	public int getVertexId();

	public int getVertexType();

	/** Task:  取得顶点的标识--顶点标识用来区分各个顶点
	 * @return 标记该顶点的对象
	 */
	public T getLabel();

	public void visit();//标记该顶点已经被访问
	public void unVisit();//标记顶点尚未访问
	public boolean isVisited();//判断顶点是否被访问

	public Vertex.Edge getEdge(VertexInterface<T> endVertex);

//	public List<Vertex<T>.Edge> getEdgeList();

	/**Task: 创建一个遍历器遍历从该顶点开始的所有边
	 * @return 从该顶点开始的边对象的迭代器
	 */
	public Iterator<VertexInterface<T>> getNeighborIterator();

	/**Task:  创建一个迭代器,计算从该顶点到其邻接点的边的权重
	 * @return 该顶点的所有邻接点的权重迭代器
	 */
	public Iterator<Double> getWeightIterator();

	/** Task: 用来条加权边连接该顶点与指定顶点
	 * @param endVertex  图中作为这条边终点的顶点
	 * @param edgeWeight  实数值的边权值,如果有的话
	 * @return 若插入成功,返回true,否则返回false
	 */
	public boolean connect(VertexInterface<T>endVertex, int edgeWeight,String edgeName,int edgeType);

//	/** Task: 用一条无权边连接该顶点与指定顶点
//	 * @param endVertex 图中作为这条边终点的顶点
//	 * @return 若插入成功,返回true
//	 */
//	public boolean connect(VertexInterface<T>endVertex);

	public boolean hasNeighbor();//查看顶点是否最少有一个邻接点

	/**Task: 取得该顶点一个未访问的邻接点,如果有的话
	 * @return 未访问的邻接点顶点,若不存在这样的邻接点则返回 null
	 */
	public VertexInterface<T> getUnvisitedNeighbor();

	/**Task: 记录到该顶点路径上的前一个顶点
	 * @param fatherVertex 该顶点的前一个顶点
	 */
	public void setFatherVertex(VertexInterface<T> fatherVertex);

	/**Task:  取得该顶点路径上的前一个顶点
	 * @return 前一个顶点,若没有返回 null
	 */
	public VertexInterface<T> getFatherVertex();

	/**Task:  检查前一个顶点是否被记录
	 * @return 如果为该顶点记录了前一个顶点,则返回true
	 */
	public boolean hasFatherVertex();

//	public void setCost(double newCost);//设置到该顶点路径的费用

//	public double getCost();//提取到该顶点路径的费用
}
