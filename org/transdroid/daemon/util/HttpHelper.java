package org.transdroid.daemon.util;

import org.transdroid.daemon.*;
import org.transdroid.daemon.DaemonException.ExceptionType;
import org.apache.http.auth.*;
import org.apache.http.params.*;
import org.apache.http.client.params.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class HttpHelper extends OriginalHttpHelper {
    public static DefaultHttpClient createStandardHttpClient(DaemonSettings settings, boolean userBasicAuth)
            throws DaemonException {
        return createStandardHttpClient(userBasicAuth && settings.shouldUseAuthentication(), settings.getUsername(),
                settings.getPassword(), settings.getSslTrustAll(), settings.getSslTrustKey(),
                settings.getTimeoutInMilliseconds(), settings.getAddress(), settings.getPort());
    }
    public static DefaultHttpClient createStandardHttpClient(boolean userBasicAuth, String username, String password,
                                                             boolean sslTrustAll, String sslTrustKey, int timeout,
                                                             String authAddress, int authPort) throws DaemonException {

        // Register http and https sockets
        SchemeRegistry registry = new SchemeRegistry();
        SocketFactory httpsSocketFactory;
        if (sslTrustKey != null && sslTrustKey.length() != 0) {
            httpsSocketFactory = new TlsSniSocketFactory(sslTrustKey);
        } else if (sslTrustAll) {
            httpsSocketFactory = new TlsSniSocketFactory(true);
        } else {
            httpsSocketFactory = new TlsSniSocketFactory();
        }
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        registry.register(new Scheme("https", httpsSocketFactory, 443));

        // Standard parameters
        HttpParams httpparams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpparams, timeout);
        HttpConnectionParams.setSoTimeout(httpparams, timeout);
        if (userAgent != null) {
            HttpProtocolParams.setUserAgent(httpparams, userAgent);
        }

        // extra parameters
        HttpClientParams.setCookiePolicy(httpparams, CookiePolicy.BROWSER_COMPATIBILITY);

        DefaultHttpClient httpclient =
                new DefaultHttpClient(new ThreadSafeClientConnManager(httpparams, registry), httpparams);

        // Authentication credentials
        if (userBasicAuth) {
            if (username == null || password == null) {
                throw new DaemonException(ExceptionType.AuthenticationFailure,
                        "No username or password was provided while we had authentication enabled");
            }
            httpclient.getCredentialsProvider()
                    .setCredentials(new AuthScope(authAddress, authPort, AuthScope.ANY_REALM),
                            new UsernamePasswordCredentials(username, password));
        }

        return httpclient;
    }

}
