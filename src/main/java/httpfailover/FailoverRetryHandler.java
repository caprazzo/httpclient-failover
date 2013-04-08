package httpfailover;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * A handler for determining if an HttpRequest should be retried on a different
 * host after a recoverable exception during execution
 */
public interface FailoverRetryHandler {

    /**
     * Get the maximum number of retries per each available target.
     * @return the maximum number of retries per host.
     */
    int getRetryCount();

    /**
     * Determines if a method should be retried on a different host after an IOException.
     *
     * @param exception the exception that occurred
     * @param context the context for the request execution
     *
     * @return <code>true</code> if the method should be retried on a
     * different host, <code>false</code> otherwise.
     */
    boolean tryNextHost(final IOException exception,
                        final HttpContext context);
}
