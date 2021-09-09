package web.crawler;

import org.apache.commons.text.StringEscapeUtils;

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

    public List<Page> save(List<Page> pages)  {
        for (Page page : pages) {
            final String pageUrl = StringEscapeUtils.escapeHtml4(page.getUrl());
            final Path basePath = Paths.get(URI.create(tmpPath + pageUrl));

            try {
                Files.createDirectories(basePath);

                Path path = Paths.get(URI.create(tmpPath + pageUrl + "/index.html"));

                if (path.toFile().exists()) { //File already exists
                    continue;
                }

                byte[] strToBytes = page.getContent().getBytes();

                Files.write(path, strToBytes);
            } catch (Exception e) {
                System.err.println(pageUrl);
                e.printStackTrace();
            }
        }
        return pages;
    }

    protected String getTmpPath() {
        return tmpPath;
    }
}
