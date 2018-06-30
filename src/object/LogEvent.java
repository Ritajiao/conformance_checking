package object;

import java.util.List;

//记录一个日志的一条路径
public class LogEvent {
	private String name;	//活动名称
	private List<Map> data;	//数据
	private boolean skipped;

	public List<Map> getData() {
		return data;
	}
	public void setData(List<Map> data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSkipped() {
		return skipped;
	}
	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}
}
