/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package web.crawler;

import java.net.http.HttpClient;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class App {


    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String base = "https://tretton37.com";
        PageFetcher pageFetcher = new PageFetcher(httpClient, base);
        PageSaver pageSaver = new PageSaver();
        final CompletableFuture<Page> pageFuture = pageFetcher.fetchSingle("/");

        pageFuture.thenAccept(page -> {
            LinkFinder linkFinder = new LinkFinder(page.getContent());
            final Set<String> links = linkFinder.relative().find();
            System.out.println("links = " + links);
            pageFetcher.fetch(links).thenAccept(pageSaver::save).join();
        }).join();

    }
}
