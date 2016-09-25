package sqlformat;

/**
 * Created by win on 2016-09-25.
 */
public class SqlToken extends AbstractSqlToken {

    public SqlToken(final int argType, final String argString,
                          final int argPos) {
        setType(argType);
        setString(argString);
        setPos(argPos);
    }


    public SqlToken(final int argType, final String argString) {
        this(argType, argString, -1);
    }
}