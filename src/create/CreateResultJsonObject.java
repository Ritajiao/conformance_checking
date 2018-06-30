package create;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import object.Alignment;
import object.Result;

import java.util.ArrayList;
import java.util.List;

public class CreateResultJsonObject {
    //创建结果的json格式
    public static JSONObject CreateResultJsonObject(Result result){
        JSONObject json=new JSONObject();
        JSONArray alignmentsArray=new JSONArray();
        ArrayList<Alignment> alignments=result.getAlignment();
        for(Alignment alignment:alignments){
            JSONObject alignment1=new JSONObject();
            alignment1.put("eventName",alignment.getA()[0].getName());
            String eventData=alignment.getA()[0].getKey();
            if(eventData!=""&&eventData!=null)
                alignment1.put("eventData",":"+eventData);
            else
                alignment1.put("eventData","");
            alignment1.put("transitionName",alignment.getA()[1].getName());
            String transitionData=alignment.getA()[1].getKey();
            if(transitionData!=null&&transitionData!="")
                alignment1.put("transitionData",":"+transitionData);
            else
                alignment1.put("transitionData","");

            alignment1.put("isHidden",alignment.isHidden());
            alignment1.put("isInserted",alignment.isInserted());
            alignment1.put("isSkipped",alignment.isSkipped());
            alignment1.put("isDatafail",alignment.isDatafail());
            if(alignment.isDatafail())
                alignment1.put("failDataName",alignment.getFailDataName().toString());
            alignmentsArray.add(alignment1);
        }

//        for(int k=0;k<result.getAlignment().size();k++){
//            JSONObject alignment1=new JSONObject();
//            alignment1.put("eventName",result.getAlignment().get(k).getA()[0].getName());
//            String eventData=result.getAlignment().get(k).getA()[0].getKey();
//            if(eventData!=""&&eventData!=null)
//                alignment1.put("eventData",":"+eventData);
//            else
//                alignment1.put("eventData","");
//            alignment1.put("transitionName",result.getAlignment().get(k).getA()[1].getName());
//            String transitionData=result.getAlignment().get(k).getA()[1].getKey();
//            if(transitionData!=null&&transitionData!="")
//                alignment1.put("transitionData",":"+transitionData);
//            else
//                alignment1.put("transitionData","");
////            alignment1.put("transitionData",alignment.getA()[1].getKey());
//            alignment1.put("isInserted",result.getAlignment().get(k).isInserted());
//            alignment1.put("isSkipped",result.getAlignment().get(k).isSkipped());
//            alignment1.put("isDatafail",result.getAlignment().get(k).isDatafail());
//            if(result.getAlignment().get(k).isDatafail())
//                alignment1.put("failDataName",result.getAlignment().get(k).getFailDataName().toString());
////            if(alignment.getFailDataName().size()!=0){
////                alignment1.put("failDataName",alignment.getFailDataName().toString());
////            }
////            alignment1.put("failDataName",alignment.getFailDataName().toString());
//            alignmentsArray.add(alignment1);
////            System.out.println(result.getAlignment().get(k).getA()[0].getKey()+"!!!");
//        }
//        ArrayList<Alignment> alignments=result.getAlignment();
//        for(Alignment alignment:alignments){
//            JSONObject alignment1=new JSONObject();
//            alignment1.put("eventName",alignment.getA()[0].getName());
//            if(alignment.getA()[0].getKey()!=null)
//                alignment1.put("eventData",alignment.getA()[0].getKey());
//            else
//                alignment1.put("eventData","");
//            alignment1.put("transitionName",alignment.getA()[1].getName());
//            if(alignment.getA()[1].getKey()!=null)
//                alignment1.put("transitionData",alignment.getA()[1].getKey());
//            else
//                alignment1.put("transitionData","");
////            alignment1.put("transitionData",alignment.getA()[1].getKey());
//            alignment1.put("isInserted",alignment.isInserted());
//            alignment1.put("isSkipped",alignment.isSkipped());
//            alignment1.put("isDatafail",alignment.isDatafail());
//            if(alignment.isDatafail())
//                alignment1.put("failDataName",alignment.getFailDataName().toString());
////            if(alignment.getFailDataName().size()!=0){
////                alignment1.put("failDataName",alignment.getFailDataName().toString());
////            }
////            alignment1.put("failDataName",alignment.getFailDataName().toString());
//            alignmentsArray.add(alignment1);
//        }

        json.put("traceId",result.getTraceId());
        json.put("alignments",alignmentsArray);
        json.put("fAct",result.getfAct());
        json.put("fAtt",result.getfAtt());
        json.put("fEStr",result.getfEStr());
        json.put("traceAttributes",result.getTraceAttributes());

        return json;
    }
}
