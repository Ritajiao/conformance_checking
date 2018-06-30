package object;

//petri网的库所，继承PNNode
public class Place extends PNNode{
	private int token;
	private boolean xor_split;		//判断是否属于or-split结构

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public boolean isXor_split() {
		return xor_split;
	}

	public void setXor_split(boolean xor_split) {
		this.xor_split = xor_split;
	}

}
