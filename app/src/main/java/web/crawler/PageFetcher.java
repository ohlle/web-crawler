package web.crawler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class PageFetcher {

    private final HttpClient httpClient;
    private final String     base;

    private final ConcurrentMap<String, CompletableFuture<Page>> fetched = new ConcurrentHashMap<>();


    public PageFetcher(HttpClient httpClient, String base) {
        this.httpClient = httpClient;
        this.base = base;
    }

    public CompletableFuture<Page> fetchSingle(String url) {
        final CompletableFuture<Page> previouslyFetched = fetched.get(url);
        if (previouslyFetched != null) {
            return previouslyFetched;
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(base + url)).build();

        final CompletableFuture<Page> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(fetchedContent -> new Page(url, fetchedContent));
        fetched.put(url, future);
        return future;
    }

    public CompletableFuture<List<Page>> fetch(Collection<String> urlList) {

        List<CompletableFuture<Page>> futureList = new LinkedList<>();
        for (String url : urlList) {
            futureList.add(fetchSingle(url));
        }

        return allOf(futureList);

    }

    private <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futuresList) {
        CompletableFuture<Void> allFuturesResult =
                CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
        return allFuturesResult.thenApply(v ->
                futuresList.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.<T>toList())
        );
    }
}
