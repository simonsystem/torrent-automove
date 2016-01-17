import org.transdroid.daemon.*;
import org.transdroid.core.gui.log.Log;
import org.transdroid.daemon.task.*;
import org.transdroid.daemon.Deluge.DelugeAdapter;
import org.transdroid.daemon.util.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.client.params.HttpClientParams;
import org.transdroid.daemon.util.HttpHelper;


public class Main {
    public static Daemon d = Daemon.Deluge;
    public static DaemonSettings s = new DaemonSettings(
        "Pluto", d, "192.168.0.101", 8112, false /*ssl*/, 
		false /*sslTrustAll*/, null /*sslTrustKey*/, null /*folder*/, false /*useAuthentication*/, 
		null, null, "deluge", OS.Windows, null, null, null, 0, false, false, null, false
    );
    public static void main(String[] args) {
        // HttpParams httpparams = new BasicHttpParams();
        // HttpConnectionParams.setConnectionTimeout(httpparams, HttpHelper.DEFAULT_CONNECTION_TIMEOUT);
        // HttpConnectionParams.setSoTimeout(httpparams, HttpHelper.DEFAULT_CONNECTION_TIMEOUT);
        // if (HttpHelper.userAgent != null) {
        //     HttpProtocolParams.setUserAgent(httpparams, HttpHelper.userAgent);
        // }

        // // extra parameters
        // HttpClientParams.setCookiePolicy(httpparams, "compatibility");
        // DefaultHttpClient.setDefaultHttpParams(httpparams);
        // System.out.println(HttpHelper.DEFAULT_CONNECTION_TIMEOUT);
        IDaemonAdapter a = d.createAdapter(s);
        Log l = new Log() {
            protected void log(String logName, int priority, String message) {
                System.out.println(message);
            }
        };



        DaemonTaskResult r = a.executeTask(l, RetrieveTask.create(a));
        System.out.println(r.toString());
        if(r.wasSuccessful()) {
            RetrieveTaskSuccessResult s = (RetrieveTaskSuccessResult) r;
            for (Torrent t: s.getTorrents()) {
                System.out.println(t.getName());
            }
        }
        System.out.println("Hello World");
    }
}
