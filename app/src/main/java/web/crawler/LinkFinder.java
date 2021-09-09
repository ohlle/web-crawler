package web.crawler;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFinder {


    private final static Pattern HREF_PATTERN = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");
    private final String html;
    private boolean relative = false;

    public LinkFinder(String html) {
        this.html = html;
    }

    public LinkFinder relative() {
        relative = true;
        return this;
    }

    public Set<String> find() {
        final Matcher matcher = HREF_PATTERN.matcher(html);
        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            final String link = matcher.group(2);
            if (relative) {
                if (link.startsWith("/") && !link.contains("http")) {
                    links.add(cleanLink(link));
                }
            } else {
                links.add(cleanLink(link));
            }
        }
        return links;
    }

    private String cleanLink(String link) {
        return link.replaceAll("#.*","");
    }
}
