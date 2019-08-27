package com.frasercrossman.webcrawler.engine;

import com.frasercrossman.webcrawler.web.WebPageScraper;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SingleThreadedCrawler extends Crawler {

  private Queue<URL> pageQueue;

  public SingleThreadedCrawler() {
    this(new WebClient());
  }

  public SingleThreadedCrawler(WebClient webClient) {
    super(new HashMap<>(), new WebPageScraper(webClient));

    this.pageQueue = new ArrayDeque<>();
  }

  public Map<URL, Set<URL>> crawlSite(URL url) {
    Set<URL> internalLinksDiscovered;

    // Add first page
    pageQueue.add(url);

    while (!pageQueue.isEmpty()) {
      url = pageQueue.remove();

      internalLinksDiscovered = this.getWebPageScraper().getInternalLinks(url);
      this.getSitemap().put(url, internalLinksDiscovered);

      internalLinksDiscovered.forEach(discoveredUrl -> {
        // New page has not been searched and is not in the queue then add it to the queue
        if (!this.getSitemap().containsKey(discoveredUrl) && !pageQueue.contains(discoveredUrl)) {
          pageQueue.add(discoveredUrl);
        }
      });
    }

    return this.getSitemap();
  }
}
