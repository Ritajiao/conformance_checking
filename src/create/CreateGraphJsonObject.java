package create;

import graph.GraphInterface;
import graph.Vertex;
import graph.VertexInterface;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class CreateGraphJsonObject {
//    创建图的json格式
    public static JSONObject CreateGraphJsonObject(GraphInterface<ArrayList<String>> graph,int Llength){
        JSONObject json=new JSONObject();
        JSONArray nodeArray=new JSONArray();
        Iterator<VertexInterface<ArrayList<String>>> vertice=graph.getVertice();
        VertexInterface<ArrayList<String>> origin=null;
        VertexInterface<ArrayList<String>> end=null;
        if(vertice.hasNext()){
            origin = vertice.next();
        }
        Queue<VertexInterface<ArrayList<String>>> queue=graph.getBreadthFirstTraversal(origin.getLabel());
        while(!queue.isEmpty()){
            VertexInterface<ArrayList<String>> vertex = queue.poll();
            JSONObject node=new JSONObject();
            node.put("name",vertex.getLabel());
            node.put("type",vertex.getVertexType());
            if(vertex.getVertexType()==2){
                end=vertex;
            }
//            node.put("id",vertex.getVertexId());
            JSONObject json1=findChildren(vertex);
            node.putAll(json1);
            nodeArray.add(node);
        }
        json.put("nodes",nodeArray);

        JSONArray alignmentArray=new JSONArray();
        Stack<VertexInterface<ArrayList<String>>> bestPath=graph.findBestAlignment(origin.getLabel(),end.getLabel(),Llength);
        while(!bestPath.isEmpty()) {
            VertexInterface<ArrayList<String>> v = bestPath.pop();
            JSONObject alignment=new JSONObject();
            alignment.put("name",v.getLabel());
            alignmentArray.add(alignment);
        }
        json.put("alignments",alignmentArray);
        return json;
    }
    public static JSONObject findChildren(VertexInterface<ArrayList<String>> vertex){
        JSONObject json=new JSONObject();
        Iterator<VertexInterface<ArrayList<String>>> succVertices=vertex.getNeighborIterator();	//获得该节点的所有后继节点
        JSONArray childrenArray=new JSONArray();
        while(succVertices.hasNext()){
            VertexInterface<ArrayList<String>> succVertex = succVertices.next();
            Vertex.Edge edge=vertex.getEdge(succVertex);
            JSONObject child=new JSONObject();
            child.put("name",succVertex.getLabel());
//            child.put("type",succVertex.getVertexType());
//            child.put("id",succVertex.getVertexId());
            child.put("edgeName",edge.getName());
            child.put("edgeType",edge.getEdgeType());
            childrenArray.add(child);
        }
        json.put("children",childrenArray);
        return json;
    }
}
