package example;

import httpfailover.FailoverHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class HttpFailoverExample {

    public static void main(String[] main) throws IOException {

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
    }

}
