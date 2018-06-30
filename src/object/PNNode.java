package object;

public class PNNode {
	private String id;
    //包含坐标信息的节点名字
    private NodeName nodeName;

    //只有name，方便根据名字比较
	private String name;

	private int x;		//点的坐标
	private int y;
	public String getId() {
		return id; 
	}
	public void setId(String id) {
		this.id = id;
	}

    public NodeName getNodeName() {
        return nodeName;
    }

    public void setNodeName(NodeName nodeName) {
        this.nodeName = nodeName;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}




	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
