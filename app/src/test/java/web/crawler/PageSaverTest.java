package web.crawler;


import org.junit.Test;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PageSaverTest {

    private PageSaver unitUnderTest;
    @Test
    public void testSavingRoot() {
        unitUnderTest = new PageSaver();
        unitUnderTest.save(List.of(new Page("/", "content")));

        assertTrue("File not found /index.html", fileExists("/index.html") );
    }

    @Test
    public void testSavingNestedRoot() {
        unitUnderTest = new PageSaver();
        unitUnderTest.save(Arrays.asList(new Page("/", "content"), new Page("/blog", "content"), new Page("/contact/leet", "content")));

        assertTrue("File not found /index.html", fileExists("/index.html") );
        assertTrue("File not found /blog/index.html", fileExists("/blog/index.html") );
        assertTrue("File not found /contact/leet/index.html", fileExists("/contact/leet/index.html") );
    }

    private boolean fileExists(String file) {
        return Paths.get(URI.create(unitUnderTest.getTmpPath() + file)).toFile().exists();
    }

}