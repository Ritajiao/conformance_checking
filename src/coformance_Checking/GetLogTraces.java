package coformance_Checking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import object.LogEvent;
import object.LogTrace;
import object.Map;

public class GetLogTraces {
	//读取xml文件，获得日志文件的路径
	@SuppressWarnings("unchecked")
	public static List<LogTrace> getLogTraces(String LogPath){
		File LogFile=new File(LogPath);
		SAXReader saxReader = new SAXReader();
		String numSimilarInstances;
		List<LogTrace> traces=new ArrayList<LogTrace>();
		try {
			Document document=saxReader.read(LogFile);
			Element root=document.getRootElement();		//获取日志文件的根元素-workflowlog
			Element process=root.element("Process");
			List<Element> processInstances=process.elements();
			for(Element processInstance:processInstances) {		//遍历日志中的所有路径
				LogTrace trace=new LogTrace();
				trace.setId(Integer.valueOf(processInstance.attributeValue("id")));
				Element data=processInstance.element("Data");

				ArrayList<Map> attributes=new ArrayList<>();
				List<Element> attributeElements=data.elements("Attribute");
				for(Element attr:attributeElements){
					Map attribute=new Map();
                    attribute.setName(attr.attributeValue("name"));
                    //获得元素的值
                    attribute.setKey(attr.getTextTrim());
                    if(attribute.getName().equals("numSimilarInstances")){
                        numSimilarInstances=attribute.getKey();
                        trace.setNumber(Integer.valueOf(numSimilarInstances));		//该路径的发生次数
                    }
                    attributes.add(attribute);
				}
				trace.setAttributes(attributes);
//				numSimilarInstances=data.elementText("Attribute").trim();
//				if((numSimilarInstances!=null)&&!(numSimilarInstances.equals(""))) {
//					trace.setNumber(Integer.valueOf(numSimilarInstances));		//该路径的发生次数
//				}
				ArrayList<LogEvent> events=new ArrayList<LogEvent>();
				List<Element> auditTrailEntrys=processInstance.elements("AuditTrailEntry");
				for(Element auditTrailEntry:auditTrailEntrys) {
					LogEvent event=new LogEvent();
					String name=auditTrailEntry.elementText("WorkflowModelElement");
					event.setName(name);	//获得活动的名字
					//获得对应的数据
					Element dataElement=auditTrailEntry.element("Data");
					if(dataElement!=null) {
						List<Element> Dataattr=dataElement.elements();
						List<Map> eventData=new ArrayList<Map>();
						for(Element attr:Dataattr) {
							Map m=new Map();
							m.setName(attr.attributeValue("name"));
							m.setKey(attr.getTextTrim());
							eventData.add(m);
						}
						event.setData(eventData);
					}
					events.add(event);
//						System.out.print(event.getName());
				}
//					System.out.println();
				trace.setEvents(events);		//路径序列
				traces.add(trace);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return traces;
	}


}
