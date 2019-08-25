package com.frasercrossman.webcrawler;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private Queue<URL> pageQueue;

  Crawler() {
    sitemap = new HashMap<>();
    pageQueue = new ArrayDeque<>();
  }

  Map<URL, Set<URL>> crawlSite(URL rootUrl) {
    WebPageScraper ws = new WebPageScraper();
    Set<URL> internalLinksDiscovered;
    URL currentPageURL;

    // Add first page
    pageQueue.add(rootUrl);

    while (!pageQueue.isEmpty()) {
      currentPageURL = pageQueue.remove();

      internalLinksDiscovered = ws.getInternalLinks(currentPageURL);
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
