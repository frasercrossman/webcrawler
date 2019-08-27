package com.frasercrossman.webcrawler;

import java.net.URL;
import java.util.Map;
import java.util.Set;

public abstract class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private WebPageScraper webPageScraper;

  public Crawler(Map<URL, Set<URL>> sitemap, WebPageScraper webPageScraper) {
    this.sitemap = sitemap;
    this.webPageScraper = webPageScraper;
  }

  public Map<URL, Set<URL>> getSitemap() {
    return sitemap;
  }

  public WebPageScraper getWebPageScraper() {
    return webPageScraper;
  }

  public abstract Map<URL, Set<URL>> crawlSite(URL url);
}
