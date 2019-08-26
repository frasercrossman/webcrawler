package com.frasercrossman.webcrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private Queue<URL> pageQueue;
  private WebPageScraper webPageScraper;

  Crawler() {
    this(new WebPageScraper());
  }

  Crawler(WebClient webClient) {
    this(new WebPageScraper(webClient));
  }


  private Crawler(WebPageScraper webPageScraper) {
    sitemap = new HashMap<>();
    pageQueue = new ArrayDeque<>();
    this.webPageScraper = webPageScraper;
  }

  Map<URL, Set<URL>> crawlSite(URL rootUrl) {
    Set<URL> internalLinksDiscovered;
    URL currentPageURL;

    // Add first page
    pageQueue.add(rootUrl);

    while (!pageQueue.isEmpty()) {
      currentPageURL = pageQueue.remove();

      internalLinksDiscovered = webPageScraper.getInternalLinks(currentPageURL);
      sitemap.put(currentPageURL, internalLinksDiscovered);

      internalLinksDiscovered.forEach(newPageURL -> {
        // New page has not been searched and is not in the queue then add it to the queue
        if (!sitemap.containsKey(newPageURL) && !pageQueue.contains(newPageURL)) {
          pageQueue.add(newPageURL);
        }
      });
    }

    return sitemap;
  }
}
