/**
 * Created by win on 2016-09-17.
 */
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wshandler")
public class WSHandler extends websocket {
    String replymessage;

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
            default:
                replymessage = "There is NO proper message type included for server to process client message!";
                break;
        }
//        String replymessage = aInfo.toString();
        System.out.println("send to client : "+replymessage);
        Log.debug("send to client : "+replymessage);
        return replymessage;
    }
}
