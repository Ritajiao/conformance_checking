package object;

import java.util.ArrayList;

//最终的对齐+计算结果
public class Result {
	private int traceId;
	private ArrayList<Alignment> alignment;
	private String fAct;	//活动拟合度f(LAct,MAct)
	private String fAtt;	//属性拟合度f(LAtt,MSch)
	private String fEStr;	//活动-属性关联拟合度f(LEStr,MEStr)
	private ArrayList<Map> traceAttributes;

    public ArrayList<Map> getTraceAttributes() {
        return traceAttributes;
    }

    public void setTraceAttributes(ArrayList<Map> traceAttributes) {
        this.traceAttributes = traceAttributes;
    }

    public int getTraceId() {
		return traceId;
	}

	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}

	public ArrayList<Alignment> getAlignment() {
		return alignment;
	}
	public void setAlignment(ArrayList<Alignment> alignment) {
		this.alignment = alignment;
	}
	public String getfAct() {
		return fAct;
	}
	public void setfAct(String fAct) {
		this.fAct = fAct;
	}
	public String getfAtt() {
		return fAtt;
	}
	public void setfAtt(String fAtt) {
		this.fAtt = fAtt;
	}
	public String getfEStr() {
		return fEStr;
	}
	public void setfEStr(String fEStr) {
		this.fEStr = fEStr;
	}

}
