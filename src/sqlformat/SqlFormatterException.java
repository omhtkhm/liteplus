package sqlformat;

/**
 * Created by win on 2016-09-25.
 */
import java.io.IOException;

@SuppressWarnings("serial")
public class SqlFormatterException extends IOException {

    public SqlFormatterException() {
        super();
    }

    public SqlFormatterException(final String argMessage) {
        super(argMessage);
    }
}
