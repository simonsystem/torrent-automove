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
import com.omertron.thetvdbapi.TheTVDBApi;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.*;
import java.io.*;

public class Main {
    public static Log l = new Log() {
        protected void log(String logName, int priority, String message) {
            System.err.println(message);
        }
    };

    public static Daemon d = Daemon.Deluge;
    public static DaemonSettings s = new DaemonSettings(
        "Pluto", d, "192.168.0.101", 8112, false /*ssl*/, 
        false /*sslTrustAll*/, null /*sslTrustKey*/, null /*folder*/, false /*useAuthentication*/, 
        null, null, "deluge", OS.Windows, null, null, null, 0, false, false, null, false
    );
    public static RetrieveTaskSuccessResult call(Class<?> task, Daemon daemon, DaemonSettings settings) throws DaemonException {
        IDaemonAdapter adapter = daemon.createAdapter(settings);
        DaemonTask dt = null;
        try {
            dt = (DaemonTask)task.getMethod("create", IDaemonAdapter.class).invoke(null, adapter);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        DaemonTaskResult result = dt.execute(l); //adapter.executeTask(l, dt);
        if (! result.wasSuccessful()) {
            throw ((DaemonTaskFailureResult) result).getException();
        }
        return ((RetrieveTaskSuccessResult) result);
    }
    public static RetrieveTaskSuccessResult retrieve(Daemon daemon, DaemonSettings settings) throws DaemonException {
        return call(RetrieveTask.class, daemon, settings);
    }

    public static abstract class ComparableInnerComparator<A, B extends Comparable<B>> implements Comparator<A> {
        public int compare(A o1, A o2) {
            return getKey(o1).compareTo(getKey(o2));
        }
        public boolean equals(A o1, A o2) {
            return compare(o1, o2) == 0;
        }
        public abstract B getKey(A a);
    }
    public static void main(String[] args) throws DaemonException {
        RetrieveTaskSuccessResult result = retrieve(d, s);
        SortedSet<Map<String, String>> smap = new TreeSet<Map<String, String>>(
            new ComparableInnerComparator<Map<String, String>, String>() {
                public String getKey(Map<String, String> o) {
                    return o.get("name") + (o.containsKey("show") ? o.get("episode") : "");
                }
            }
        );
        Map<String, String> release;
        ListMultimap<String, String> parsed;
        Lexer lexer;
        String key;
        for (Torrent t: result.getTorrents()) {
            lexer = new Lexer(new StringReader(t.getName()));
            try {
                parsed = lexer.yylex();
            } catch (IOException e) {
                parsed = null;
            }
            release = new TreeMap<String, String>();
            for (Map.Entry<String, Collection<String>> e: parsed.asMap().entrySet()) {
                release.put(e.getKey(), StringUtils.join(e.getValue(), " "));
            }
            smap.add(release);
        }
        for (Map<String, String> e: smap) {
            System.out.println(e.get("show") + " " + e.get("episode"));
        }
    }
}
