package graph;

import java.util.*;

public interface GraphInterface<T> {
	/*
	 *@Task:将给定的顶点插入图
	 *@param vertexLabel 标记新顶点的对象
	 *@param type 标记顶点是否是开始或结束，0代表开始节点，1是中间，2是结束节点
	 */
	public void addVertex(T vertexLabel,int vertexType);

	/*
	 * @Task 在图的指定顶点间插入一条加权边,这两个顶点之间在插入边之前不能有边
	 * @param begin 标识边的起点
	 * @param end 标识边的终点
	 * @param edgeWeight 边的权重，即成本
	 * @param edgeName 边的名称
	 * @param type 边的类型，0-log,1-model,2-both,3-hidden
	 * @return 若插入成功,返回true,否则返回false
	 */
	public boolean addEdge(T begin, T end, int edgeWeight,String edgeName,int edgeType);

	/*
	 * @Task 检查两个指定的顶点之间是否存在边
	 * @param begin 标识边的起点的对象
	 * @param end 标识边的终点的对象
	 * @return 若有边则返回true
	 */
	public boolean hasEdge(T begin, T end);

	public boolean isEmpty();//检查图是否为空

	public int getNumberOfVertices();//获得图中顶点的个数

	public int getNumberOfEdges();//获得图中的边的条数

	public void clear();//删除图中所有的顶点与边

	public Iterator<VertexInterface<T>> getVertice();

	public ArrayList<Vertex.Edge> getEdges();

	/**Task：寻找两个指定顶点之间的最短路径
	 * @param begin 标识路径起点
	 * @param end 标识路径终点
	 * @return 返回最短路径的长度
	 */
//	public int getShortestPath(T begin, T end, Stack<T>path);
	public Stack<VertexInterface<T>> findBestAlignment(T begin,T end,int Llength);

	public Queue<VertexInterface<T>> getBreadthFirstTraversal(T origin);
}
