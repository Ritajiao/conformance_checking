package coformance_Checking;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import object.*;

public class Fitness {

	//通过Id寻找变迁
	public static Transition findTransitionById(List<Transition> transitions,String id){
		Transition transition=new Transition();
		for(Transition t:transitions){
			if(t.getId().equals(id)){
				transition=t;
				break;
			}
		}
		return transition;
	}


	//通过找最佳对齐，并计算拟合度
	public static Result computeFitness(PetriNet processNet,LogTrace trace,double w1,double w2) {
		PetriNet eventNet = GetEventNet.getEventNet(trace);
		List<Transition> eventTransitions = eventNet.getTransitions();
		List<Transition> processTransitions=processNet.getTransitions();
		ArrayList<String> alignments=BestAlignment.createAndFind(processNet, eventNet);
//		ArrayList<String> alignments=LogReplay.Replay(processNet,eventNet,w1,w2);
//		//如果属于不完全对齐，再利用算法去找最佳对齐
//		if(alignments==null)
//			alignments = BestAlignment.createAndFind(processNet, eventNet);

//		System.out.println(alignments.toString());
		//找到对齐之后，计算拟合度
		int maxData=0;
		int dataDeviate=0;
		int hiden=0;
		int skipped=0;
		int inserted=0;
		Result result=new Result();
		ArrayList<Alignment> newAlignments=new ArrayList<Alignment>();
		for(String alignment:alignments){
			Alignment newAlignment=new Alignment();	//一条路径的对齐
			Map[] newAlignment1=new Map[2];		//记录一个活动的对齐
			//**数组要初始化，否则会空指针异常
			for(int j=0;j<2;j++)
				newAlignment1[j]=new Map();
			boolean ifSkipped=false;
			boolean ifInserted=false;
			boolean dataFail=false;
			List<String> failDataName=new ArrayList<>();
			String eventTransitionId=alignment.split(",")[0].split("\\(")[1];
			String processTransitionId=alignment.split(",")[1].split("\\)")[0];
			String MData="";
			String LData="";
			Transition eventTransition=new Transition();
			Transition processTransition=new Transition();
			List<Map> eventData=new ArrayList<>();
			List<Map> processData=new ArrayList<>();
			List<String> eventDataName=new ArrayList<>();
			List<String> processDataName=new ArrayList<>();
			if(!eventTransitionId.equals(">>")) {
				eventTransition = findTransitionById(eventTransitions, eventTransitionId);
				eventData=eventTransition.getData();
			}
			if(!processTransitionId.equals(">>")){
				processTransition = findTransitionById(processTransitions, processTransitionId);
				processData=processTransition.getData();
			}
			if(processData!=null&&processData.size()>0){
//				maxData += processData.size();
				for (int n = 0; n < processData.size(); n++) {
					processDataName.add(processData.get(n).getName());
					MData += processData.get(n).getName() + "=" + processData.get(n).getKey();
					if (n != processData.size() - 1)
						MData += ",";
				}
				//若活动没有偏差
				if(!eventTransitionId.equals(">>"))
					maxData += processData.size();
			}
			if(eventData!=null&&eventData.size()>0){
				for(int m=0;m<eventData.size();m++) {
					eventDataName.add(eventData.get(m).getName());
					LData=LData+eventData.get(m).getName()+"="+eventData.get(m).getKey();
					if(m!=eventData.size()-1)
						LData+=",";
				}
			}
			//如果两者数据都有
			if(MData!=""&&LData!=""){
				for(int n=0;n<processData.size();n++) {
					if(!eventDataName.contains(processData.get(n).getName())) {
						dataDeviate += 1;
						dataFail=true;
						failDataName.add(processData.get(n).getName());
						break;
					}
					for (int m = 0; m < eventData.size(); m++) {
						if(!processDataName.contains(eventData.get(m).getName())){
							dataDeviate += 1;
							dataFail=true;
							failDataName.add(eventData.get(m).getName());
							break;
						}
						if (processData.get(n).getName().equals(eventData.get(m).getName())) {
							String modelType = processData.get(n).getKey().trim();
							String logKey = eventData.get(m).getKey().trim();
							//分四种情况
							if (logKey.equalsIgnoreCase("null")) {
								dataDeviate += 1;
								dataFail=true;
								failDataName.add(eventData.get(m).getName());
							} else if (modelType.contains("||")) {        //如果是几种选择
								String[] keys = modelType.split("\\|\\|");        //注意：||每一个|都要转译
								boolean ifDatasame = false;
								for (int t = 0; t < keys.length; t++) {
									if (logKey.equalsIgnoreCase(keys[t].trim()))        //相等说明符合
										ifDatasame = true;
								}
								if (!ifDatasame) {
									dataDeviate += 1;
									dataFail=true;
									failDataName.add(eventData.get(m).getName());
								}
							} else if (modelType.contains("[")) {        //如果是一个取值区间
								int min = Integer.valueOf(modelType.split("\\[")[1].split("\\]")[0].split(",")[0]);
								int max = Integer.valueOf(modelType.split("\\[")[1].split("\\]")[0].split(",")[1]);
								int key = Integer.valueOf(logKey);
								if (key > max || key < min) {
									dataDeviate += 1;
									dataFail=true;
									failDataName.add(eventData.get(m).getName());
								}
							} else {            //如果是定义的类型
								String type = modelType;
								Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
								if (pattern.matcher(logKey).matches()) {        //判断是否为数字
									float a = Float.valueOf(logKey);
									int b = (int) a;
									int c = b;
									if (c != a) {        //如果不等说明不是整型
										if (!type.equalsIgnoreCase("float") || !type.equalsIgnoreCase("double")) {
											dataDeviate += 1;
											dataFail=true;
											failDataName.add(eventData.get(m).getName());
										}
									} else {
										if (!type.equalsIgnoreCase("int") && !type.equalsIgnoreCase("Integer")) {
											dataDeviate += 1;
											dataFail=true;
											failDataName.add(eventData.get(m).getName());
										}
									}
								} else if (type.length() == 1) {
									if (!type.equalsIgnoreCase("char")) {
										dataDeviate += 1;
										dataFail=true;
										failDataName.add(eventData.get(m).getName());
									}
								} else {
									if (!type.equalsIgnoreCase("String")) {
										dataDeviate += 1;
										dataFail=true;
										failDataName.add(eventData.get(m).getName());
									}
								}
							}
						}
					}
				}
			}

			if (!eventTransitionId.equals(">>")) {
				//活动名是name
				newAlignment1[0].setName(eventTransition.getName());
				System.out.print(eventTransition.getName());
				if (LData != "") {
					//数据是key
					newAlignment1[0].setKey("{" + LData + "}");
					System.out.print("{" + LData + "}");
				}
				else
					newAlignment1[0].setKey("");
			} else {
			    //如果不是隐藏任务
			    if(!processTransition.isHiden()){
                    skipped+=1;
                    ifSkipped=true;
                }
				newAlignment1[0].setName(eventTransitionId);
				System.out.print(eventTransitionId);
			}
			System.out.print("--");
			if (!processTransitionId.equals(">>")) {
				newAlignment1[1].setName(processTransition.getNodeName().getText());
//					newAlignment1[1].setName(processTransition.getName());
				System.out.print(processTransition.getName());
				if (MData != "") {
					newAlignment1[1].setKey("{" + MData + "}");
					System.out.print("{" + MData + "}");
				}
				else
					newAlignment1[1].setKey("");
			} else {
				inserted+=1;
				ifInserted=true;
				newAlignment1[1].setName(processTransitionId);
				System.out.print(processTransitionId);
			}
			newAlignment.setA(newAlignment1);
			if(ifInserted) {
				newAlignment.setInserted(true);
				System.out.print(",Inserted");
			}
			if(ifSkipped) {
				newAlignment.setSkipped(true);
				System.out.print(",Skipped");
			}
			if(dataFail) {
				newAlignment.setDatafail(true);
				newAlignment.setFailDataName(failDataName);
				System.out.print(","+failDataName+"Data Fail");
			}
			if(processTransition.isHiden()){
				hiden+=1;
				newAlignment.setHidden(true);
                System.out.print(",Hidden");
			}
			System.out.println();
			newAlignments.add(newAlignment);
		}

		//活动拟合度f(LAct,MAct)=equalEvent/maxEvent
		int maxEvent=alignments.size()-hiden;	//隐藏活动不考虑
		int equalEvent=maxEvent-skipped-inserted;
		double fEvent=(double)equalEvent/maxEvent;		//当分子小于分母时，会输出0，所以注意类型转换
//        System.out.println("maxEvent:"+maxEvent+",equalEvent:"+equalEvent+",fEvent:"+fEvent);
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		String fEvent1=nf.format(fEvent);
		System.out.println("活动拟合度f(LAct,MAct)="+fEvent1);

		//数据拟合度f(LAtt,MSch)=adpatData/maxData
		int adpatData=maxData-dataDeviate;
		double fData;
		if(maxData==0)
			fData=1;
		else
			fData=(double)adpatData/maxData;

		//关联拟合度f(LEStr,MEStr)=w1*fEvent+w2*fData
		double fRelavant=w1*fEvent+w2*fData;
		nf.setMaximumFractionDigits(3);
		String fRelavant1=nf.format(fRelavant);
//        System.out.println("maxData:"+maxData+",adpatData:"+adpatData+",fData:"+fData);
		nf.setMaximumFractionDigits(3);
		String fData1=nf.format(fData);
		System.out.println("属性拟合度f(LAtt,MSch)="+fData1);
		System.out.println("活动-属性关联拟合度f(LEStr,MEStr)="+fRelavant1);
		result.setTraceId(trace.getId());
		result.setAlignment(newAlignments);
		result.setfAct(fEvent1);
		result.setfAtt(fData1);
		result.setfEStr(fRelavant1);
		//设置路径的属性
		result.setTraceAttributes(trace.getAttributes());
		return result;
	}

//	public static void main(String[] args) {
//		String WF_netPath="PetriNet\\net.pnml";
//		String LogPath="xml\\Lnew.xml";
//		PetriNet processNet=GetProcessNet.getProcessNet(WF_netPath);
//		List<LogTrace> traces=GetLogTraces.getLogTraces(LogPath);
//		int i=0;
//
//		double w1=0.5;
//		double w2=0.5;
//
//		double fitness=0;
//		int totalNumber=0;
//
////        List<Transition> processTransitions=processNet.getTransitions();
//        for(LogTrace trace:traces) {
//        	int number=trace.getNumber();
//        	totalNumber+=number;
//            i++;
//            System.out.print(i + ":");
//            List<LogEvent> events=trace.getEvents();
//            for(LogEvent event:events)
//                System.out.print(event.getName());
//            System.out.println();
//            double nfRelavant=number*computeFitness(processNet, trace, w1, w2);
//            fitness+=nfRelavant;
//        }
//        fitness=fitness/totalNumber;
////        System.out.println("fitness:"+fitness);
//	    NumberFormat nf = NumberFormat.getNumberInstance();
//	    nf.setMaximumFractionDigits(3);
//	    String fitness1=nf.format(fitness);
//	    System.out.println("日志-模型拟合度fitness(L,M)="+fitness1);
//	}
}
