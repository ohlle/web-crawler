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
            tmpPath = "file://" + Files.createTempDirectory("page-fetcher-").toAbsolutePath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Page> pages)  {
        for (Page page : pages) {
            System.out.println("page.getUrl() in save = " + page.getUrl());
            final Path path = Paths.get(URI.create(tmpPath + page.getUrl()));

            if (path.toFile().exists()) { //Path already exists
                continue;
            }

            byte[] strToBytes = page.getContent().getBytes();

            try {
                Files.write(path, strToBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
