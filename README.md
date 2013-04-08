httpclient-failover
===================

An extension to apache httpclient that adds support for failover on multiple http hosts.

## Example Usage

    // create anf configure FailoverHttpClient just as you would a DefaultHttpClient
    PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
    cm.setDefaultMaxPerRoute(20);
    cm.setMaxTotal(100);
    FailoverHttpClient failoverHttpClient = new FailoverHttpClient(cm);


    // have ready a list of hosts to try the call
    List<HttpHost> hosts = Arrays.asList(
        new HttpHost("localhost", 9090),
        new HttpHost("localhost", 9191)
    );

    // create the request
    HttpGet request = new HttpGet(URI.create("/file.txt"));

    // invoke the request on localhost:9090 first,
    // and localhost:9191 if that fails.

    try {
        HttpResponse response = failoverHttpClient.execute(hosts, request);
        System.out.println("One of the hosts responded with " + EntityUtils.toString(response.getEntity()));
    }
    catch(IOException ex) {
        System.err.println("both hosts failed. The last exception is " + ex);
    }

