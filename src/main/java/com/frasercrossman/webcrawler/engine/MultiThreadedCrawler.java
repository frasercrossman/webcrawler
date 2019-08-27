package com.frasercrossman.webcrawler.engine;

import com.frasercrossman.webcrawler.web.WebPageScraper;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class MultiThreadedCrawler extends Crawler {

  private static final int DEFAULT_THREAD_COUNT = 4;

  private ForkJoinPool forkJoinPool;

  public MultiThreadedCrawler() {
    this(DEFAULT_THREAD_COUNT);
  }

  public MultiThreadedCrawler(int threadCount) {
    this(threadCount, new WebClient());
  }

  public MultiThreadedCrawler(WebClient webClient) {
    this(DEFAULT_THREAD_COUNT, webClient);
  }

  public MultiThreadedCrawler(int threadCount, WebClient webClient) {
    super(new ConcurrentHashMap<>(), new WebPageScraper(webClient));

    forkJoinPool = new ForkJoinPool(threadCount);
  }

  public Map<URL, Set<URL>> crawlSite(URL rootUrl) {
    forkJoinPool.invoke(new ForkJoinCrawl(this, rootUrl));

    return this.getSitemap();
  }
}
