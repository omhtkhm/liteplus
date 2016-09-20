/**
 * Created by hun on 2016-09-01.
 */
import java.io.*;
import java.util.Date;

public class Log {
    static String logFile = "D:/Develop/webtest1/debug.log";
    static FileWriter fw;
    static final String ENTER = System.getProperty("line.separator");

//    private static Log2 instance = new Log2();
//    private static Log instance;

//    private Log() {
//        try {
//            fw = new FileWriter(logFile, true);
//        } catch (IOException e){}
//    }

//    public static Log getInstance () {
//        if(instance  ==null){//있는지 체크 없으면
//            instance  = new Log2 (); //생성한뒤
//        }
//        return instance;
//    }

    public static void close() {
        try {
            fw.close();
        } catch (IOException e){}
    }

    public static void debug(String msg) {
        try {
            fw = new FileWriter(logFile, true); // 매번 로그파일 열고 닫기
            fw.write(new Date()+ " : ");
            fw.write(msg + ENTER);
            fw.flush();
            fw.close(); // 매번 로그파일 열고 닫기
        } catch (IOException e) {
            System.err.println("IOException!");
        }
    }

//    public void finalize() {
//        try {
//            fw.close();
//        } catch (IOException e){}
//    }
}
