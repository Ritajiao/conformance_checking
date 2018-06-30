package create;

import object.*;
//import org.json.JSONObject;
//import org.json.JSONArray;
import net.sf.json.*;

import java.util.ArrayList;

public class CreateNetJsonObject {
    //创建petri网的json格式
    public static JSONObject CreateJsonObject(PetriNet petriNet){
        JSONObject json=new JSONObject();
        //库所变迁弧都是数组，所以定义JSONArray类型
        JSONArray placeArray=new JSONArray();
        JSONArray transitionArray=new JSONArray();
        JSONArray pnArcArray=new JSONArray();
        ArrayList<Place> places=petriNet.getPlaces();
        for(Place place:places){
            JSONObject place1=new JSONObject();
            place1.put("id",place.getId());
            JSONObject name=new JSONObject();
            name.put("text",place.getNodeName().getText());
            name.put("x",place.getNodeName().getOffsetX());
            name.put("y",place.getNodeName().getOffsetY());
            place1.put("name",name);
//            place1.put("name",place.getName());
            place1.put("token",place.getToken());
            place1.put("x",place.getX());
            place1.put("y",place.getY());
            //对于数组要用add方法
            placeArray.add(place1);
        }
        json.put("places",placeArray);
        ArrayList<Transition> transitions=petriNet.getTransitions();
        for(Transition transition:transitions){
            JSONObject transition1=new JSONObject();
            transition1.put("id",transition.getId());
            //name有多个字段，要新建object
            JSONObject name=new JSONObject();
            name.put("text",transition.getNodeName().getText());
            name.put("x",transition.getNodeName().getOffsetX());
            name.put("y",transition.getNodeName().getOffsetY());
            transition1.put("name",name);
            transition1.put("x",transition.getX());
            transition1.put("y",transition.getY());
            transition1.put("isHidden",transition.isHiden());
            transitionArray.add(transition1);
        }
        json.put("transitions",transitionArray);
        ArrayList<PNArc> pnArcs=petriNet.getPnArcs();
        for(PNArc pnArc:pnArcs){
            JSONObject pnArc1=new JSONObject();
            pnArc1.put("source",pnArc.getSource().getId());
            pnArc1.put("target",pnArc.getTarget().getId());
            JSONArray turnPointsArray=new JSONArray();
            ArrayList<TurnPoint> turnPoints=pnArc.getTurnPoints();
            for(TurnPoint turnPoint:turnPoints){
                JSONObject turnPoints1=new JSONObject();
                turnPoints1.put("x",turnPoint.getX());
                turnPoints1.put("y",turnPoint.getY());
                turnPointsArray.add(turnPoints1);
            }
            pnArc1.put("turnPoints",turnPointsArray);
            pnArcArray.add(pnArc1);
        }
        json.put("pnArcs",pnArcArray);
        return json;
    }
}
