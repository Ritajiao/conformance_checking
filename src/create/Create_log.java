package create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
//import org.dom4j.io.OutputFormat;  

public class Create_log {
	//读取txt日志中的每条路径
	public static List<String> readTxt(String fileName) {
		File f = new File("src\\txt\\"+fileName+".txt");		//相对路径，相对于该工程的根目录
		FileInputStream in;
		List<String> tracks=new ArrayList();
		if(f.exists()) {
			try {
				in=new FileInputStream(f);
				BufferedReader br=new BufferedReader(new InputStreamReader(in));
				String line=null;
				try {
					int i=0;
					while((line=br.readLine())!=null) {
						tracks.add(line);
						i++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return tracks;
	}

	public static void writeLog() throws Exception{
//		String filename="Lnew";
//		String datafile="Lnew_Data";
		String filename="Lbook1";
		String datafile="Lbook1_Data";
		List<String> tracks=readTxt(filename);
		List<String> logData=readTxt(datafile);
		//创建文档并建立根节点
		Element root=DocumentHelper.createElement("WorkflowLog");
		Document document=DocumentHelper.createDocument(root);
		Element process=root.addElement("Process");
		process.addAttribute("id","0");
		//循环写入任务实例
		for(int i=0;i<tracks.size();i++) {
			String[] route=tracks.get(i).split("\"");
//			String[] route=tracks.get(i).split(" ");
			Element processInstance=process.addElement("ProcessInstance");
			processInstance.addAttribute("id", route[0].trim());
			Element data=processInstance.addElement("Data");
			Element attribute=data.addElement("Attribute");
			attribute.addAttribute("name", "numSimilarInstances");
			attribute.addText(route[2].trim());
			String[] datas=logData.get(i).split(",");
			int k=0;
			String[] events=route[1].split(",");
//			char[] events=route[1].toCharArray();
			for(int j=0;j<events.length;j++) {
				Element auditTrailEntry=processInstance.addElement("AuditTrailEntry");
				Element workflowModelElement=auditTrailEntry.addElement("WorkflowModelElement");
				workflowModelElement.addText(events[j]);
//				workflowModelElement.addText(String.valueOf(events[j]));
				Element eventType=auditTrailEntry.addElement("EventType");
				eventType.addText("complete");
				//写数据节点
				Element data1=auditTrailEntry.addElement("Data");
				k=j;
				if(k<=datas.length-1) {
					if(datas[k].contains("&")) {
						String[] data2=datas[k].split("&");
						for(int m=0;m<data2.length;m++) {
							Element attr=data1.addElement("Attribute");
							attr.addAttribute("name", data2[m].split("=")[0]);
							attr.addText(data2[m].split("=")[1]);
						}
					}

					else if(!datas[k].equals("")){
						Element attr=data1.addElement("Attribute");
						attr.addAttribute("name", datas[k].split("=")[0]);
						attr.addText(datas[k].split("=")[1]);
					}

				}
			}
		}
		//添加到根节点，并输出到控制台
		XMLWriter xml=new XMLWriter();
		xml.write(document);
		//创建文件 ,并写入
		OutputFormat format = OutputFormat.createPrettyPrint(); //设置XML文档输出格式
		format.setEncoding("GB2312"); //设置XML文档的编码类型

		XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("src\\xml\\"+filename+".xml")),format);
		xmlWriter.write(document);
		xmlWriter.close();
	}
	public static void main(String[] args) throws Exception {
		writeLog();
	}
}