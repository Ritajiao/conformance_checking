package object;

import java.util.ArrayList;

//记录一个日志文件的合集
public class LogTrace {
    //trace的id
	private int id;
	private ArrayList<LogEvent> events;
	private int number;
	private ArrayList<Map> attributes;

	public ArrayList<Map> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Map> attributes) {
		this.attributes = attributes;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public ArrayList<LogEvent> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<LogEvent> events) {
		this.events = events;
	}
	
}
