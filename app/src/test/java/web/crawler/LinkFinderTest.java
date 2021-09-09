package web.crawler;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class LinkFinderTest {

    String testData = "<html><a href=\"/relative-path\">relative-link</a><a href=\"https://example.net/absolute\">Absolute path</a></html>";
    String testData2 = "<html><a href=\"/relative-path#withhash\">relative-link</a><a href=\"https://example.net/absolute\">Absolute path</a></html>";

    @Test
    public void testGettingAllLinks() {
        Set<String> expected = new HashSet<>(Arrays.asList("/relative-path", "https://example.net/absolute"));

        LinkFinder unitUnderTest = new LinkFinder(new Page("/", testData));

        final Set<String> actual = unitUnderTest.find();

        assertEquals(expected,actual);

    }

    @Test
    public void testGettingAllRelativeLinks() {
        Set<String> expected = Set.of("/relative-path");

        LinkFinder unitUnderTest = new LinkFinder(new Page("/", testData));

        final Set<String> actual = unitUnderTest.relative().find();

        assertEquals(expected,actual);

    }

    @Test
    public void testGettingAllRelativeLinksCleaned() {
        Set<String> expected = Set.of("/relative-path");

        LinkFinder unitUnderTest = new LinkFinder(new Page("/", testData2));

        final Set<String> actual = unitUnderTest.relative().find();

        assertEquals(expected,actual);

    }

}