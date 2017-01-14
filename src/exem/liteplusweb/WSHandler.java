package exem.liteplusweb; /**
 * Created by win on 2016-09-17.
 */
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exem.applepie.TablespaceInfo;
//import simpledb.EmbeddedDatabase;

import javax.websocket.OnMessage;
//import javax.exem.liteplusweb.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wshandler")
public class WSHandler extends websocket {
    String replymessage;
//    boolean isDBinitialized = false;

    @OnMessage
    public String handleMessage(String message){
        System.out.println("receive from client : "+message);
        Log.debug("receive from client : "+message);

        JsonElement jelement = new JsonParser().parse(message);
        JsonObject jobject = jelement.getAsJsonObject();
        String strMessageType = jobject.get("messageType").getAsString();
        String strQueryText = jobject.get("sqltext").getAsString();
        Log.debug("strMessageType from client : "+strMessageType);
        Log.debug("SQL Text from client : "+strQueryText);

        int intMessageType=ClientMessageType.strArrayClientMessageTypes.length;
        for(int i=0; i<ClientMessageType.strArrayClientMessageTypes.length ;i++){
            if ( strMessageType.equals(ClientMessageType.strArrayClientMessageTypes[i]) ) {
                intMessageType = i;
                break;
            }
        }
//        intMessageType = 0;
        switch(intMessageType){
            case 0:     // query 메시지 인 경우,
                Log.debug("client message Type : query");
                Query myQuery = new Query();
                replymessage = myQuery.handleMessage(strQueryText);
                break;
            case 1:     // plan 메시지 인 경우,
                Log.debug("client message Type : plan");
                Plan myPlan = new Plan();
                replymessage = myPlan.handleMessage(strQueryText);
                break;
            case 2:     // tableinfo 메시지 인 경우,
                Log.debug("client message Type : tableinfo");
                TableInfo myTableInfo = new TableInfo();
                replymessage = myTableInfo.handleMessage(strQueryText);
                break;
            case 3:     // format 메시지 인 경우,
                Log.debug("client message Type : format");
                Format myFormat = new Format();
                replymessage = myFormat.handleMessage(strQueryText);
                break;
            case 4:     // format 메시지 인 경우,
                Log.debug("client message Type : merge");
                Merge myMerge = new Merge();
                replymessage = myMerge.handleMessage(strQueryText);
                break;
            case 5:     // format 메시지 인 경우,
                Log.debug("client message Type : exem.liteplusweb.History");
                History myHistory = new History();
                String strDirection = jobject.get("direction").getAsString();
                replymessage = myHistory.handleMessage(strQueryText,strDirection);
                break;
            case 6:     // exem.liteplusweb.HistoryGrid 메시지 인 경우,
                Log.debug("client message Type : exem.liteplusweb.History Grid");
                HistoryGrid myHistoryGrid = new HistoryGrid();
                replymessage = myHistoryGrid.handleMessage();
                break;
            case 7:     // exem.liteplusweb.Batch 메시지 인 경우,
                Log.debug("client message Type : batch");
                Batch myBatch = new Batch();
                String sqlIndex = jobject.get("sqlindex").getAsString();
                replymessage = myBatch.handleMessage(strQueryText, sqlIndex);
                break;
            case 8:     // applepie.tablespaceinfo 메시지 인 경우,
                Log.debug("client message Type : tablespaceinfo");
                TablespaceInfo myTSInfo = new TablespaceInfo();
                replymessage = myTSInfo.handleMessage();
                break;
            default:
                replymessage = handleMessage();
                break;
        }
//        String replymessage = aInfo.toString();
        System.out.println("send to client : "+replymessage);
        Log.debug("send to client : "+replymessage);
        return replymessage;
    }

    public String handleMessage(){
        JsonObject aInfo = new JsonObject();
        aInfo.addProperty("success", false); //{"success":true}
        aInfo.addProperty("messageType", "error");
        aInfo.addProperty("errorMessage", "There is NO proper message type included for server to process client message!");

        String strJson = aInfo.toString();
        Log.debug(strJson);
        return strJson;
    }
//    @OnOpen
//    public void handleOpen(){
//        System.out.println("exem.liteplusweb.WSHandler: client is now connected...");
////        if(!isDBinitialized){
////            EmbeddedDatabase.initDB();
////            isDBinitialized = true;
////        }
//    }
}
