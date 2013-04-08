package httpfailover;

import org.apache.http.HttpRequest;

/**
 * A {@link DefaultFailoverRetryHandler} which assumes that all requested
 * HTTP methods which should be idempotent according to RFC-2616 are
 * in fact idempotent and can be retried.
 *
 * According to RFC-2616 section 9.1.2 the idempotent HTTP methods are:
 * GET, HEAD, PUT, DELETE, OPTIONS, and TRACE
 */
public class StandardFailoverRetryHandler extends DefaultFailoverRetryHandler {

    /**
     * @see DefaultFailoverRetryHandler#DefaultFailoverRetryHandler(int, boolean)
     **/
    public StandardFailoverRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
        super(retryCount, requestSentRetryEnabled);
    }

    /**
     * @see DefaultFailoverRetryHandler#DefaultFailoverRetryHandler()
     **/
    public StandardFailoverRetryHandler() {
        super();
    }

    @Override
    protected boolean handleAsIdempotent(final HttpRequest request) {
        String method = request.getRequestLine().getMethod().toUpperCase();
        if (method.equals("GET")) return true;
        if (method.equals("PUT")) return true;
        if (method.equals("HEAD")) return true;
        if (method.equals("DELETE")) return true;
        if (method.equals("OPTIONS")) return true;
        if (method.equals("TRACE")) return true;
        return false;
    }
}
