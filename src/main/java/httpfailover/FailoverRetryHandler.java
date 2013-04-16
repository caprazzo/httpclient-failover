/**
 * Copyright 2013 Matteo Caprari
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
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
