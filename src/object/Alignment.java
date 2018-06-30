package object;

import java.util.List;

//记录一个活动的对齐
public class Alignment {
	//Map数组类型，分别对应日志和模型
	private Map[] a;
	private boolean isHidden;
	private boolean isInserted;
	private boolean isSkipped;
	private boolean isDatafail;
	private List<String> failDataName;

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean hidden) {
		isHidden = hidden;
	}

	public Map[] getA() {
		return a;
	}
	public void setA(Map[] a) {
		this.a = a;
	}
	public boolean isInserted() {
		return isInserted;
	}
	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}
	public boolean isSkipped() {
		return isSkipped;
	}
	public void setSkipped(boolean isSkipped) {
		this.isSkipped = isSkipped;
	}
	public boolean isDatafail() {
		return isDatafail;
	}
	public void setDatafail(boolean isDatafail) {
		this.isDatafail = isDatafail;
	}
	public List<String> getFailDataName() {
		return failDataName;
	}
	public void setFailDataName(List<String> failDataName) {
		this.failDataName = failDataName;
	}
}
