package pojlib.util.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class StreamDL extends InputStream {
    private final InputStream in;
    private int count;
    private final Collection<StreamListener> listeners = new ArrayList<>();
    private final DownloadManager downloadManager;
    private final String fileName;
    private final int totalBytes;

    public StreamDL(InputStream in, DownloadManager downloadManager, String fileName, int totalBytes) {
        this.in = in;
        this.downloadManager = downloadManager;
        this.fileName = fileName;
        this.totalBytes = totalBytes;
    }

    @Override
    public int read() throws IOException {
        int b = in.read();
        byteReceived(b);
        return b;
    }

    public void addListener(StreamListener listener) {
        listeners.add(listener);
    }

    private void byteReceived(int b) {
        if (b != -1) {
            count++;
            double progress = ((double) count / totalBytes) * 100;
            downloadManager.updateProgress(fileName, progress);
        } else {
            downloadManager.fileDownloadComplete(fileName);
        }

        for (StreamListener l : listeners) {
            l.byteReceived(b, count);
        }
    }
}

