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

    static class ParsedTorrent extends TreeMap<String, String> implements Comparable<ParsedTorrent> {
        private final Torrent torrent;
        ParsedTorrent(Torrent t) { torrent = t; }
        public Torrent getTorrent() { return torrent; }
        public int compareTo(ParsedTorrent o) {
            return getTorrent().compareTo(o.getTorrent());
        }
    }
    public static void main(String[] args) throws DaemonException {
        RetrieveTaskSuccessResult result = retrieve(d, s);
        SortedSet<ParsedTorrent> torrents = new TreeSet();
        ParsedTorrent release;
        ListMultimap<String, String> parsed;
        Lexer lexer;
        String key;
        for (Torrent t: result.getTorrents()) {
            lexer = new Lexer(new StringReader(t.getName() + "\n"));
            try {
                parsed = lexer.yylex();
                System.out.println(parsed);
            } catch (IOException e) {
                throw new Error(e);
            }
            release = new ParsedTorrent(t);
            for (Map.Entry<String, Collection<String>> e: parsed.asMap().entrySet()) {
                release.put(e.getKey(), StringUtils.join(e.getValue(), " "));
            }
            torrents.add(release);
        }
        for (ParsedTorrent e: torrents) {
            System.out.println(e.get("name"));
        }
    }
}
