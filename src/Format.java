import com.google.gson.JsonObject;
import sqlformat.*;

import java.sql.SQLSyntaxErrorException;

/**
 * Created by hun on 2016-09-26.
 */
public class Format {
    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);
        ////////////////////////////////////////////////
        String before = message;
        final SqlRule rule = new SqlRule();
        rule.setKeywordCase(SqlRule.KEYWORD_UPPER_CASE);
//        rule.indentString = "    ";
        final String[] mySqlFuncs = {
                // getNumericFunctions
                "ABS", "ACOS", "ASIN", "ATAN", "ATAN2", "BIT_COUNT", "CEILING",
                "COS", "COT", "DEGREES", "EXP",
                "FLOOR",
                "LOG",
                "LOG10",
                "MAX",
                "MIN",
                "MOD",
                "PI",
                "POW",
                "POWER",
                "RADIANS",
                "RAND",
                "ROUND",
                "SIN",
                "SQRT",
                "TAN",
                "TRUNCATE",
                // getStringFunctions
                "ASCII", "BIN", "BIT_LENGTH", "CHAR", "CHARACTER_LENGTH",
                "CHAR_LENGTH", "CONCAT", "CONCAT_WS", "CONV", "ELT",
                "EXPORT_SET", "FIELD", "FIND_IN_SET", "HEX,INSERT", "INSTR",
                "LCASE", "LEFT", "LENGTH", "LOAD_FILE", "LOCATE", "LOCATE",
                "LOWER", "LPAD", "LTRIM", "MAKE_SET", "MATCH", "MID", "OCT",
                "OCTET_LENGTH", "ORD", "POSITION", "QUOTE", "REPEAT",
                "REPLACE", "REVERSE", "RIGHT", "RPAD", "RTRIM", "SOUNDEX",
                "SPACE", "STRCMP", "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING_INDEX",
                "TRIM",
                "UCASE",
                "UPPER",
                // getSystemFunctions
                "DATABASE", "USER",
                "SYSTEM_USER",
                "SESSION_USER",
                "PASSWORD",
                "ENCRYPT",
                "LAST_INSERT_ID",
                "VERSION",
                // getTimeDateFunctions
                "DAYOFWEEK", "WEEKDAY", "DAYOFMONTH", "DAYOFYEAR", "MONTH",
                "DAYNAME", "MONTHNAME", "QUARTER", "WEEK", "YEAR", "HOUR",
                "MINUTE", "SECOND", "PERIOD_ADD", "PERIOD_DIFF", "TO_DAYS",
                "FROM_DAYS", "DATE_FORMAT", "TIME_FORMAT", "CURDATE",
                "CURRENT_DATE", "CURTIME", "CURRENT_TIME", "NOW", "SYSDATE",
                "CURRENT_TIMESTAMP", "UNIX_TIMESTAMP", "FROM_UNIXTIME",
                "SEC_TO_TIME", "TIME_TO_SEC" };
        rule.setFunctionNames(mySqlFuncs);
        final SqlFormatter formatter = new SqlFormatter(rule);

        System.out.println("[before]\n" + before);
        String after = "";
        try {
            after = formatter.format(before);
        } catch (SqlFormatterException e){
            e.printStackTrace();
        }
        System.out.println("[after]\n" + after);
        ////////////////////////////////////////////////////
        JsonObject aInfo = new JsonObject();
        aInfo.addProperty("success", true); //{"success":true}
        aInfo.addProperty("messageType", "format");

        String strJson = aInfo.toString();
        Log.debug(strJson);
        return strJson;
    }
}
