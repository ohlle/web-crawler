package web.crawler;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PageSaver {

    private String tmpPath;
    public PageSaver() {
        try {
            tmpPath = "file://" + Files.createTempDirectory("page-fetcher-").toAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Page> pages)  {
        for (Page page : pages) {
            final Path basePath = Paths.get(URI.create(tmpPath + page.getUrl()));

            try {
                Files.createDirectories(basePath);

                Path path = Paths.get(URI.create(tmpPath + page.getUrl() + "/index.html"));

                if (path.toFile().exists()) { //File already exists
                    continue;
                }

                byte[] strToBytes = page.getContent().getBytes();

                Files.write(path, strToBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String getTmpPath() {
        return tmpPath;
    }
}
