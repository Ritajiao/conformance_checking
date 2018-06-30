package object;

import java.util.List;

public class Transition extends PNNode{
	//变迁记录的数据
	private List<Map> data;
	private boolean isHiden;
	private boolean isAndJoin;
	private boolean isSkipped;

	public boolean isAndJoin() {
		return isAndJoin;
	}

	public void setAndJoin(boolean andJoin) {
		isAndJoin = andJoin;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public boolean isHiden() {
		return isHiden;
	}

	public void setHiden(boolean isHiden) {
		this.isHiden = isHiden;
	}

	public boolean isSkipped() {
		return isSkipped;
	}

	public void setSkipped(boolean isSkipped) {
		this.isSkipped = isSkipped;
	}
	
}
