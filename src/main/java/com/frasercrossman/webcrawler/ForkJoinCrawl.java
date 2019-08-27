package com.frasercrossman.webcrawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

class ForkJoinCrawl extends RecursiveAction {

  private MultiThreadedCrawler crawler;
  private URL url;

  ForkJoinCrawl(MultiThreadedCrawler crawler, URL url) {
    this.crawler = crawler;
    this.url = url;
  }

  @Override
  protected void compute() {
    Set<URL> internalLinksDiscovered = crawler.getWebPageScraper().getInternalLinks(url);
    List<RecursiveAction> actions = new ArrayList<>();

    crawler.getSitemap().put(url, internalLinksDiscovered);

    internalLinksDiscovered.forEach(discoveredUrl -> {
      // Synchronized putIfAbsent ensures url is added once and only once
      // If url is absent then it is added as a new action
      if (crawler.getSitemap().putIfAbsent(discoveredUrl, Collections.emptySet()) == null) {
        actions.add(new ForkJoinCrawl(crawler, discoveredUrl));
      }
    });

    invokeAll(actions);
  }
}