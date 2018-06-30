function produceTable(resultAlignments,fAct,fAtt,fEStr) {
    // $("#resultTable").html("");
    var html="<table  border='1' align='center' cellpadding='0' cellspacing='0' bordercolorlight='#3399FF' bordercolordark='#FFFFFF'>" +
        "<tbody>" +
        "<tr bgcolor='#99ccff' align='left'>" +
        "<th width='420'>Log</th>" +
        "<th width='600'>Model</th>" +
        "<th width='180'>Deviation</th>" +
        "</tr>";
    for(var alignment of resultAlignments){
        var eventData="",transitionData="";
        if(alignment.eventData)
            eventData=alignment.eventData.split(":")[1];
        if(alignment.transitionData)
            transitionData=alignment.transitionData.split(":")[1];
        html=html+"<tr align='left'><td height='10'>"+alignment.eventName+"<br/>"+eventData+"</td>" +
            "<td height='10'>"+alignment.transitionName+"<br/>"+transitionData+"</td>";
        if(alignment.isInserted==true)
            html=html+"<td style='color:orange'>Inserted</td></tr>";
        else if(alignment.isSkipped==true)
            html=html+"<td style='color:purple'>Skipped</td></tr>";
        else if(alignment.isDatafail==true)
            html=html+"<td style='color:green'>"+alignment.failDataName+"Data Fail</td></tr>";
        else if(alignment.isHidden==true)
            html=html+"<td style='color:#aaaaaa'>Invisible Transition</td></tr>";
        else
            html=html+"<td></td></tr>";
    }
    html=html+"</tbody></table><table  border='1' align='center' cellpadding='0' cellspacing='0' bordercolorlight='#3399FF' bordercolordark='#FFFFFF'>"+
        "<tr align='left'><td width='600'>活动拟合度f(LAct,MAct)</td><td width='600'>"+fAct+"</td></tr>"+
        "<tr align='left'><td width='600'>属性拟合度f(LAtt,MSch)</td><td width='600'>"+fAtt+"</td></tr>"+
        "<tr align='left'><td width='600'>活动-属性关联拟合度f(LEStr,MEStr)</td><td width='600'>"+fEStr+"</td></tr>";

    return html;
    // $("#resultTable").html(html);
}