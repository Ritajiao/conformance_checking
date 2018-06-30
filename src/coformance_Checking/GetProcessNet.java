package coformance_Checking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import object.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GetProcessNet {
	//读取pnml文件，获得petri网的元素
	@SuppressWarnings("unchecked")
	public static PetriNet getProcessNet(String WF_netPath) {
		File WF_netFile=new File(WF_netPath);
		SAXReader saxReader = new SAXReader();
		ArrayList<Transition> transitions=new ArrayList<Transition>();
		ArrayList<Place> places=new ArrayList<Place>();
		ArrayList<PNArc> pnArcs=new ArrayList<PNArc>();

		try {
			Document document = saxReader.read(WF_netFile);
			Element root=document.getRootElement();		//获取pnml文件的根元素-pnml
			Element net=root.element("net");
			//获取transition
			List<Element> transitionElements=net.elements("transition");		//获得transition元素
			for(Element transitionElement:transitionElements) {
				Transition transition1=new Transition();
				transition1.setId(transitionElement.attributeValue("id"));
				Element nameElement=transitionElement.element("name");
				Element offset=nameElement.element("graphics").element("offset");
				NodeName name=new NodeName();
				name.setText(nameElement.elementText("text"));
				name.setOffsetX(Integer.valueOf(offset.attributeValue("x")));
				name.setOffsetY(Integer.valueOf(offset.attributeValue("y")));
				//设置包含坐标信息的节点
				transition1.setNodeName(name);
				//只设置名字
				transition1.setName(nameElement.elementText("text"));
				//如果名字中含有小括号，name要设置成小括号里面的，方便比较
				if(transition1.getName().contains("("))
					transition1.setName(transition1.getName().split("\\(")[1].split("\\)")[0]);
				//获得变迁的坐标x，y
				Element position=transitionElement.element("graphics").element("position");
				transition1.setX(Integer.valueOf(position.attributeValue("x")));
				transition1.setY(Integer.valueOf(position.attributeValue("y")));
				//获取变迁的数据
				Element transitionData=transitionElement.element("Data");
				if(transitionData!=null) {
					List<Element> Dataattr=transitionData.elements();
					List<Map> data=new ArrayList<Map>();
					for(Element attr:Dataattr) {
						Map m=new Map();
						m.setName(attr.attributeValue("name"));
						m.setKey(attr.getTextTrim());
						data.add(m);
					}
					transition1.setData(data);
				}
				//判断变迁是否是隐藏任务
				if(transition1.getId().contains("op"))
					transition1.setHiden(true);
				else
					transition1.setHiden(false);
				transitions.add(transition1);
			}
			//获取place
			List<Element> placeElements=net.elements("place");
			for(Element placeElement:placeElements) {
				Place place1=new Place();
				place1.setId(placeElement.attributeValue("id"));

				Element nameElement=placeElement.element("name");
				Element offset=nameElement.element("graphics").element("offset");
				NodeName name=new NodeName();
				name.setText(nameElement.elementText("text"));
				name.setOffsetX(Integer.valueOf(offset.attributeValue("x")));
				name.setOffsetY(Integer.valueOf(offset.attributeValue("y")));
				place1.setNodeName(name);
				place1.setName(nameElement.elementText("text"));
//				place1.setName(placeElement.element("name").elementText("text"));
				//获得变迁的坐标x，y
				Element position=placeElement.element("graphics").element("position");
				place1.setX(Integer.valueOf(position.attributeValue("x")));
				place1.setY(Integer.valueOf(position.attributeValue("y")));

				if(placeElement.element("initialMarking")!=null) {
					String initialMarking=placeElement.element("initialMarking").elementText("text");
					if(initialMarking!=null) {		//获取最初的token
						int token=Integer.valueOf(initialMarking);
						place1.setToken(token);
					}
				}
				else
					place1.setToken(0);
				places.add(place1);
			}
			//获取arc
			List<Element> arcElements=net.elements("arc");
			PNArc pnArc=null;
			Transition transition=new Transition();
			Place place=new Place();
			for(Element arcElement:arcElements) {
				String source=arcElement.attributeValue("source");
				String target=arcElement.attributeValue("target");

				//若源点是transition
				if(source.startsWith("t")) {
					for(Transition t:transitions) {
						if(t.getId().equals(source)) {
							transition=t;
							break;
						}
					}
					for(Place p:places) {
						if(p.getId().equals(target)) {
							place=p;
							break;
						}
					}
					pnArc=new PNArc(transition,place);
				}
				else {
					for(Place p:places) {
						if(p.getId().equals(source)) {
							place=p;
							break;
						}
					}
					for(Transition t:transitions) {
						if(t.getId().equals(target)) {
							transition=t;
							break;
						}
					}
					pnArc=new PNArc(place,transition);
				}
                List<Element> positionElements=arcElement.element("graphics").elements("position");
                ArrayList<TurnPoint> turnPoints=new ArrayList<>();
				for(Element position:positionElements){
                    TurnPoint turnPoint=new TurnPoint();
                    int x=Integer.valueOf(position.attributeValue("x"));
                    int y=Integer.valueOf(position.attributeValue("y"));
                    turnPoint.setX(x);
                    turnPoint.setY(y);
                    turnPoints.add(turnPoint);
                }
                pnArc.setTurnPoints(turnPoints);
				pnArcs.add(pnArc);
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PetriNet petriNet=new PetriNet(transitions,places,pnArcs);
		return petriNet;
	}
}
