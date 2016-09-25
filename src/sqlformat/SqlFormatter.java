/**
 * Created by win on 2016-09-25.
 */
        package sqlformat;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileReader;
        import java.util.List;
        import java.util.Stack;

        import SqlToken;

public class SqlFormatter {
    private final SqlParser fParser = new SqlParser();

    private SqlRule fRule = null;


    private Stack<Boolean> functionBracket = new Stack<Boolean>();


    public SqlFormatter(final SqlRule argRule) {
        fRule = argRule;
    }

    public String format(final String argSql)
            throws SqlFormatterException {
        functionBracket.clear();
        try {
            boolean isSqlEndsWithNewLine = false;
            if (argSql.endsWith("\n")) {
                isSqlEndsWithNewLine = true;
            }

            List<SqlToken> list = fParser.parse(argSql);

            list = format(list);

            //
            String after = "";
            for (int index = 0; index < list.size(); index++) {
                SqlToken token = list.get(index);
                after += token.getString();
            }

            if (isSqlEndsWithNewLine) {
                after += "\n";
            }

            return after;
        } catch (Exception ex) {
            final SqlFormatterException sqlException = new SqlFormatterException(
                    ex.toString());
            sqlException.initCause(ex);
            throw sqlException;
        }
    }


    private List<SqlToken> format(final List<SqlToken> argList) {


        SqlToken token = argList.get(0);
        if (token.getType() == SqlTokenConstants.SPACE) {
            argList.remove(0);
        }

        token = argList.get(argList.size() - 1);
        if (token.getType() == SqlTokenConstants.SPACE) {
            argList.remove(argList.size() - 1);
        }


        for (int index = 0; index < argList.size(); index++) {
            token = argList.get(index);
            if (token.getType() == SqlTokenConstants.KEYWORD) {
                switch (fRule.keyword) {
                    case SqlRule.KEYWORD_NONE:
                        break;
                    case SqlRule.KEYWORD_UPPER_CASE:
                        token.setString(token.getString().toUpperCase());
                        break;
                    case SqlRule.KEYWORD_LOWER_CASE:
                        token.setString(token.getString().toLowerCase());
                        break;
                }
            }
        }


        for (int index = argList.size() - 1; index >= 1; index--) {
            token = argList.get(index);
            SqlToken prevToken = argList.get(index - 1);
            if (token.getType() == SqlTokenConstants.SPACE
                    && (prevToken.getType() == SqlTokenConstants.SYMBOL || prevToken
                    .getType() == SqlTokenConstants.COMMENT)) {
                argList.remove(index);
            } else if ((token.getType() == SqlTokenConstants.SYMBOL || token
                    .getType() == SqlTokenConstants.COMMENT)
                    && prevToken.getType() == SqlTokenConstants.SPACE) {
                argList.remove(index - 1);
            } else if (token.getType() == SqlTokenConstants.SPACE) {
                token.setString(" ");
            }
        }


        for (int index = 0; index < argList.size() - 2; index++) {
            SqlToken t0 = argList.get(index);
            SqlToken t1 = argList.get(index + 1);
            SqlToken t2 = argList.get(index + 2);

            if (t0.getType() == SqlTokenConstants.KEYWORD
                    && t1.getType() == SqlTokenConstants.SPACE
                    && t2.getType() == SqlTokenConstants.KEYWORD) {
                if (((t0.getString().equalsIgnoreCase("ORDER") || t0
                        .getString().equalsIgnoreCase("GROUP")) && t2
                        .getString().equalsIgnoreCase("BY"))) {
                    t0.setString(t0.getString() + " " + t2.getString());
                    argList.remove(index + 1);
                    argList.remove(index + 1);
                }
            }

            // Oracle begin 2007/10/24 A.Watanabe
            if (t0.getString().equals("(") && t1.getString().equals("+")
                    && t2.getString().equals(")")) {
                t0.setString("(+)");
                argList.remove(index + 1);
                argList.remove(index + 1);
            }
            // Oracle
        }

        int indent = 0;
        final Stack<Integer> bracketIndent = new Stack<Integer>();
        SqlToken prev = new SqlToken(SqlTokenConstants.SPACE,
                " ");
        boolean encounterBetween = false;
        for (int index = 0; index < argList.size(); index++) {
            token = argList.get(index);
            if (token.getType() == SqlTokenConstants.SYMBOL) {

                if (token.getString().equals("(")) {
                    functionBracket
                            .push(fRule.isFunction(prev.getString()) ? Boolean.TRUE
                                    : Boolean.FALSE);
                    bracketIndent.push(new Integer(indent));
                    indent++;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }

                else if (token.getString().equals(")")) {
                    indent = bracketIndent.pop().intValue();
                    index += insertReturnAndIndent(argList, index, indent);
                    functionBracket.pop();
                }

                else if (token.getString().equals(",")) {
                    index += insertReturnAndIndent(argList, index, indent);
                } else if (token.getString().equals(";")) {
                    // indent = 0; //akardapolov comm
                    index += insertReturnAndIndent(argList, index+1, indent);
                }
            } else if (token.getType() == SqlTokenConstants.KEYWORD) {

                if (token.getString().equalsIgnoreCase("DELETE")
                        || token.getString().equalsIgnoreCase("SELECT")
                        || token.getString().equalsIgnoreCase("UPDATE")
                        // akardapolov
                        || token.getString().equalsIgnoreCase("DECLARE")
                        || token.getString().equalsIgnoreCase("BEGIN")) {
                    indent += 2;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }

                if (token.getString().equalsIgnoreCase("INSERT")
                        || token.getString().equalsIgnoreCase("INTO")
                        || token.getString().equalsIgnoreCase("CREATE")
                        || token.getString().equalsIgnoreCase("DROP")
                        || token.getString().equalsIgnoreCase("TRUNCATE")
                        || token.getString().equalsIgnoreCase("TABLE")
                        || token.getString().equalsIgnoreCase("CASE")) {
                    indent++;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                if (token.getString().equalsIgnoreCase("FROM")
                        || token.getString().equalsIgnoreCase("WHERE")
                        || token.getString().equalsIgnoreCase("SET")
                        || token.getString().equalsIgnoreCase("ORDER BY")
                        || token.getString().equalsIgnoreCase("GROUP BY")
                        || token.getString().equalsIgnoreCase("HAVING")
                        // akardapolov
                        || token.getString().equalsIgnoreCase("BEGIN")
                        || token.getString().equalsIgnoreCase("END")) {
                    index += insertReturnAndIndent(argList, index, indent - 1);
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                if (token.getString().equalsIgnoreCase("VALUES")) {
                    indent--;
                    index += insertReturnAndIndent(argList, index, indent);
                }
                if (token.getString().equalsIgnoreCase("END")) {
                    indent--;
                    index += insertReturnAndIndent(argList, index, indent);
                }
                if (token.getString().equalsIgnoreCase("OR")
                        || token.getString().equalsIgnoreCase("THEN")
                        || token.getString().equalsIgnoreCase("ELSE")) {
                    index += insertReturnAndIndent(argList, index, indent);
                }
                if (token.getString().equalsIgnoreCase("ON")
                        || token.getString().equalsIgnoreCase("USING")) {
                    index += insertReturnAndIndent(argList, index, indent + 1);
                }
                if (token.getString().equalsIgnoreCase("UNION")
                        || token.getString().equalsIgnoreCase("INTERSECT")
                        || token.getString().equalsIgnoreCase("EXCEPT")) {
                    indent -= 2;
                    index += insertReturnAndIndent(argList, index, indent);
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                if (token.getString().equalsIgnoreCase("BETWEEN")) {
                    encounterBetween = true;
                }
                if (token.getString().equalsIgnoreCase("AND")) {
                    if (!encounterBetween) {
                        index += insertReturnAndIndent(argList, index, indent);
                    }
                    encounterBetween = false;
                }
            } else if (token.getType() == SqlTokenConstants.COMMENT) {
                if (token.getString().startsWith("/*")) {
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
            }
            prev = token;
        }

        for (int index = argList.size() - 1; index >= 4; index--) {
            if (index >= argList.size()) {
                continue;
            }

            SqlToken t0 = argList.get(index);
            SqlToken t1 = argList.get(index - 1);
            SqlToken t2 = argList.get(index - 2);
            SqlToken t3 = argList.get(index - 3);
            SqlToken t4 = argList.get(index - 4);

            if (t4.getString().equalsIgnoreCase("(")
                    && t3.getString().trim().equalsIgnoreCase("")
                    && t1.getString().trim().equalsIgnoreCase("")
                    && t0.getString().equalsIgnoreCase(")")) {
                t4.setString(t4.getString() + t2.getString() + t0.getString());
                argList.remove(index);
                argList.remove(index - 1);
                argList.remove(index - 2);
                argList.remove(index - 3);
            }
        }

        for (int index = 1; index < argList.size(); index++) {
            prev = argList.get(index - 1);
            token = argList.get(index);

            if (prev.getType() != SqlTokenConstants.SPACE
                    && token.getType() != SqlTokenConstants.SPACE) {
                if (prev.getString().equals(",")) {
                    continue;
                }
                if (fRule.isFunction(prev.getString())
                        && token.getString().equals("(")) {
                    continue;
                }
                argList.add(index, new SqlToken(
                        SqlTokenConstants.SPACE, " "));
            }
        }

        return argList;
    }


    private int insertReturnAndIndent(final List<SqlToken> argList,
                                      final int argIndex, final int argIndent) {
        if (functionBracket.contains(Boolean.TRUE))
            return 0;
        try {
            String s = "\n";
            final SqlToken prevToken = argList.get(argIndex - 1);
            if (prevToken.getType() == SqlTokenConstants.COMMENT
                    && prevToken.getString().startsWith("--")) {
                s = "";
            }
            for (int index = 0; index < argIndent; index++) {
                s += fRule.indentString;
            }

            SqlToken token = argList.get(argIndex);
            if (token.getType() == SqlTokenConstants.SPACE) {
                token.setString(s);
                return 0;
            }

            token = argList.get(argIndex - 1);
            if (token.getType() == SqlTokenConstants.SPACE) {
                token.setString(s);
                return 0;
            }
            argList.add(argIndex, new SqlToken(
                    SqlTokenConstants.SPACE, s));
            return 1;
        } catch (IndexOutOfBoundsException e) {
            // e.printStackTrace();
            return 0;
        }
    }

    public static void main(final String[] args) throws Exception {
        final SqlRule rule = new SqlRule();
        rule.keyword = SqlRule.KEYWORD_UPPER_CASE;
        rule.indentString = "    ";
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

        final File[] files = new File("Test").listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("-- " + files[i]);
            final BufferedReader reader = new BufferedReader(new FileReader(
                    files[i]));
            String before = "";
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null)
                    break;
                before += line + "\n";
            }
            reader.close();

            System.out.println("[before]\n" + before);
            String after = formatter.format(before);
            System.out.println("[after]\n" + after);
        }
    }
}