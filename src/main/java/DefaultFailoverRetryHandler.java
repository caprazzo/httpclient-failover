import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Default implementation of {@link FailoverRetryHandler}.
 *
 * When a request fails with IOException, the request can be retried if
 *      the request was not aborted
 *  OR  the request is idempotent (not implements HttpEntityEnclosingRequest)
 *  OR  the request has not been send
 *  OR  the request has been sent and <code>requestSentRetryEnabled</code> is <code>true</code>
 *
 *  (Also consider {link @StandardFailoverRetryHandler})
 */
public class DefaultFailoverRetryHandler implements FailoverRetryHandler {

    /** the number of times a method will be retried */
    private int retryCount;

    /** Whether or not methods that have successfully sent their request will be retried */
    private final boolean requestSentRetryEnabled;

    /**
     * Create a new DefaultFailoverRetryHandler
     *
     * @param retryCount the number of times each host can be retried
     * @param requestSentRetryEnabled whether or not methods that have successfully sent their request will be retried
     */
    public DefaultFailoverRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
        this.retryCount = retryCount;
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    /**
     * Create a new DefaultFailoverRetryHandler with
     * retryCount = 3 and requestSentRetryEnabled = false
     */
    public DefaultFailoverRetryHandler() {
        this(3, false);
    }

    public int getRetryCount() {
        return retryCount;
    }

    public boolean tryNextHost(
            final IOException exception,
            final HttpContext context) {

        if (exception == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        }

        HttpRequest request = (HttpRequest)
                context.getAttribute(ExecutionContext.HTTP_REQUEST);

        if(requestIsAborted(request)){
            return false;
        }

        if (handleAsIdempotent(request)) {
            // Retry if the request is considered idempotent
            return true;
        }

        Boolean b = (Boolean)
                context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
        boolean sent = (b != null && b.booleanValue());

        if (!sent || this.requestSentRetryEnabled) {
            // Retry if the request has not been sent fully or
            // if it's OK to retry methods that have been sent
            return true;
        }
        // otherwise do not retry
        return false;
    }

    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    protected boolean handleAsIdempotent(final HttpRequest request) {
        return !(request instanceof HttpEntityEnclosingRequest);
    }

    protected boolean requestIsAborted(final HttpRequest request) {
        HttpRequest req = request;
        if (request instanceof RequestWrapper) { // does not forward request to original
            req = ((RequestWrapper) request).getOriginal();
        }
        return (req instanceof HttpUriRequest && ((HttpUriRequest)req).isAborted());
    }

}
