package sqlformat;
import java.util.ArrayList;
import java.util.List;

public class SqlParser {

    private String fBefore;


    private char fChar;


    private int fPos;


    private static final String[] twoCharacterSymbol = { "<>", "<=", ">=", "||" };

    public SqlParser() {
    }

    public static boolean isSpace(final char argChar) {
        // 2005.07.26 Tosiki Iga 
        // 2005.08.12 Tosiki Iga 
        return argChar == ' ' || argChar == '\t' || argChar == '\n'
                || argChar == '\r' || argChar == 65535;
    }


    public static boolean isLetter(final char argChar) {

        if (isSpace(argChar)) {
            return false;
        }
        if (isDigit(argChar)) {
            return false;
        }
        if (isSymbol(argChar)) {
            return false;
        }
        return true;
    }


    public static boolean isDigit(final char argChar) {
        return '0' <= argChar && argChar <= '9';
    }


    public static boolean isSymbol(final char argChar) {
        switch (argChar) {
            case '"': // double quote
            case '?': // question mark
            case '%': // percent
            case '&': // ampersand
            case '\'': // quote
            case '(': // left paren
            case ')': // right paren
            case '|': // vertical bar
            case '*': // asterisk
            case '+': // plus sign
            case ',': // comma
            case '-': // minus sign
            case '.': // period
            case '/': // solidus
            case ':': // colon
            case ';': // semicolon
            case '<': // less than operator
            case '=': // equals operator
            case '>': // greater than operator


                // case '!':
                // case '$':
                // case '[':
                // case '\\':
                // case ']':
                // case '^':
                // case '{':
                // case '}':
                // case '~':
                return true;
            default:
                return false;
        }
    }

    SqlToken nextToken() {
        int start_pos = fPos;
        if (fPos >= fBefore.length()) {
            fPos++;
            return new SqlToken(SqlTokenConstants.END, "",
                    start_pos);
        }

        fChar = fBefore.charAt(fPos);

        if (isSpace(fChar)) {
            String workString = "";
            for (;;) {
                workString += fChar;
                fChar = fBefore.charAt(fPos);
                if (!isSpace(fChar)) {
                    return new SqlToken(SqlTokenConstants.SPACE,
                            workString, start_pos);
                }
                fPos++;
                if (fPos >= fBefore.length()) {
                    return new SqlToken(SqlTokenConstants.SPACE,
                            workString, start_pos);
                }
            }
        } else if (fChar == ';') {
            fPos++;
            return new SqlToken(SqlTokenConstants.SYMBOL, ";",
                    start_pos);
        } else if (isDigit(fChar)) {
            String s = "";
            while (isDigit(fChar) || fChar == '.') {
                // if (ch == '.') type = Token.REAL;
                s += fChar;
                fPos++;

                if (fPos >= fBefore.length()) {

                    break;
                }

                fChar = fBefore.charAt(fPos);
            }
            return new SqlToken(SqlTokenConstants.VALUE, s,
                    start_pos);
        } else if (isLetter(fChar)) {
            String s = "";

            while (isLetter(fChar) || isDigit(fChar) || fChar == '.') {
                s += fChar;
                fPos++;
                if (fPos >= fBefore.length()) {
                    break;
                }

                fChar = fBefore.charAt(fPos);
            }
            for (int i = 0; i < SqlConstants.SQL_RESERVED_WORDS.length; i++) {
                if (s
                        .compareToIgnoreCase(SqlConstants.SQL_RESERVED_WORDS[i]) == 0) {
                    return new SqlToken(SqlTokenConstants.KEYWORD,
                            s, start_pos);
                }
            }
            return new SqlToken(SqlTokenConstants.NAME, s,
                    start_pos);
        }
        // single line comment
        else if (fChar == '-') {
            fPos++;
            char ch2 = fBefore.charAt(fPos);

            if (ch2 != '-') {
                return new SqlToken(SqlTokenConstants.SYMBOL, "-",
                        start_pos);
            }
            fPos++;
            String s = "--";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\n' || fPos >= fBefore.length()) {
                    return new SqlToken(SqlTokenConstants.COMMENT,
                            s, start_pos);
                }
            }
        }

        else if (fChar == '/') {
            fPos++;
            char ch2 = fBefore.charAt(fPos);

            if (ch2 != '*') {
                return new SqlToken(SqlTokenConstants.SYMBOL, "/",
                        start_pos);
            }

            String s = "/*";
            fPos++;
            int ch0 = -1;
            for (;;) {
                ch0 = fChar;
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (ch0 == '*' && fChar == '/') {
                    return new SqlToken(SqlTokenConstants.COMMENT,
                            s, start_pos);
                }
            }
        } else if (fChar == '\'') {
            fPos++;
            String s = "'";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\'') {
                    return new SqlToken(SqlTokenConstants.VALUE, s,
                            start_pos);
                }
            }
        } else if (fChar == '\"') {
            fPos++;
            String s = "\"";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\"') {
                    return new SqlToken(SqlTokenConstants.NAME, s,
                            start_pos);
                }
            }
        }

        else if (isSymbol(fChar)) {

            String s = "" + fChar;
            fPos++;
            if (fPos >= fBefore.length()) {
                return new SqlToken(SqlTokenConstants.SYMBOL, s,
                        start_pos);
            }

            char ch2 = fBefore.charAt(fPos);
            for (int i = 0; i < twoCharacterSymbol.length; i++) {
                if (twoCharacterSymbol[i].charAt(0) == fChar
                        && twoCharacterSymbol[i].charAt(1) == ch2) {
                    fPos++;
                    s += ch2;
                    break;
                }
            }
            return new SqlToken(SqlTokenConstants.SYMBOL, s,
                    start_pos);
        } else {
            fPos++;
            return new SqlToken(SqlTokenConstants.UNKNOWN, ""
                    + fChar, start_pos);
        }
    }


    public List<SqlToken> parse(final String argSql) {
        fPos = 0;
        fBefore = argSql;

        final List<SqlToken> list = new ArrayList<SqlToken>();
        for (;;) {
            final SqlToken token = nextToken();
            if (token.getType() == SqlTokenConstants.END) {
                break;
            }

            list.add(token);
        }
        return list;
    }
}