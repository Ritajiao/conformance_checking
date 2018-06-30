package create;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


import object.Transition;

public class AddModeldata {
	@SuppressWarnings("unchecked")
	public static void addModelData(String dataFilename,String WF_netPath) throws IOException {

		List<String> modelData=Create_log.readTxt(dataFilename);

		File WF_netFile=new File(WF_netPath);
		SAXReader saxReader = new SAXReader();


		try {
			Document document = saxReader.read(WF_netFile);
			Element root=document.getRootElement();		//获取pnml文件的根元素-pnml
			Element net=root.element("net");
			//获取transition
			List<Element> transitionElements=net.elements("transition");		//获得transition元素
			for(Element transitionElement:transitionElements) {
				Transition transition1=new Transition();
				transition1.setId(transitionElement.attributeValue("id"));
				transition1.setName(transitionElement.element("name").elementText("text"));
				if(transition1.getName().contains("(")) {
					transition1.setName(transition1.getName().split("\\(")[1].split("\\)")[0]);
					System.out.println(transition1.getName());
				}
				for(String data1:modelData) {
					if(data1.split(":")[0].equals(transition1.getName())) {
						Element data=transitionElement.addElement("Data");
						if(data1.split(":")[1].contains("&")) {
							String[] data2=data1.split(":")[1].split("&");
							for(int m=0;m<data2.length;m++) {
								Element attr=data.addElement("Attribute");
								attr.addAttribute("name", data2[m].split("=")[0]);
								attr.addText(data2[m].split("=")[1]);
								System.out.println("OK");
								System.out.println(attr.attributeValue("name"));
							}
						}
						else {
							Element attr=data.addElement("Attribute");
							attr.addAttribute("name", data1.split(":")[1].split("=")[0]);
							attr.addText(data1.split(":")[1].split("=")[1]);

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

			XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("src\\PetriNet\\shudian1.pnml")),format);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String WF_netPath="src\\PetriNet\\shudian.pnml";
		String dataFilename="Mbook_Data";
		try {
			addModelData(dataFilename,WF_netPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
