package create;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import object.Alignment;
import object.Map;
import object.Result;

import java.util.ArrayList;
import java.util.List;

public class CreateResultsJsonObject {
    //创建结果的json格式
    public static JSONObject CreateResultsJsonObject(List<Result> results,String fitness){
        JSONObject json=new JSONObject();
        JSONArray resultsArray=new JSONArray();
        for(Result result:results){
            JSONObject result1=CreateResultJsonObject.CreateResultJsonObject(result);

//            JSONArray alignmentsArray=new JSONArray();
//            ArrayList<Alignment> alignments=result.getAlignment();
//            for(Alignment alignment:alignments){
//                JSONObject alignment1=new JSONObject();
//                alignment1.put("eventName",alignment.getA()[0].getName());
//                String eventData=alignment.getA()[0].getKey();
//                if(eventData!=""&&eventData!=null)
//                    alignment1.put("eventData",":"+eventData);
//                else
//                    alignment1.put("eventData","");
//                alignment1.put("transitionName",alignment.getA()[1].getName());
//                String transitionData=alignment.getA()[1].getKey();
//                if(transitionData!=null&&transitionData!="")
//                    alignment1.put("transitionData",":"+transitionData);
//                else
//                    alignment1.put("transitionData","");
//
//                alignment1.put("isHidden",alignment.isHidden());
//                alignment1.put("isInserted",alignment.isInserted());
//                alignment1.put("isSkipped",alignment.isSkipped());
//                alignment1.put("isDatafail",alignment.isDatafail());
//                if(alignment.isDatafail())
//                    alignment1.put("failDataName",alignment.getFailDataName().toString());
//                alignmentsArray.add(alignment1);
//            }
//            result1.put("traceId",result.getTraceId());
//            result1.put("alignments",alignmentsArray);
//            result1.put("fAct",result.getfAct());
//            result1.put("fAtt",result.getfAtt());
//            result1.put("fEStr",result.getfEStr());
//            result1.put("traceAttributes",result.getTraceAttributes());
            JSONArray traceAttributes=new JSONArray();
            for(Map attribute:result.getTraceAttributes()){
                JSONObject traceAttribute=new JSONObject();
                traceAttribute.put("name",attribute.getName());
                traceAttribute.put("key",attribute.getKey());
                traceAttributes.add(traceAttribute);
            }
            resultsArray.add(result1);
        }
        json.put("results",resultsArray);
        json.put("fitness",fitness);
        return json;
    }
}
