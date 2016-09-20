/**
 * Created by hun on 2016-09-01.
 */
public class QueryParser {
    public static String removeSemicolon(String strQuery){
        //StringBuilder sb = new StringBuilder(strQuery);
        String strModifiedQuery = strQuery.replaceAll(";","");
        return strModifiedQuery;
    }
}
