package com.frasercrossman.webcrawler.engine;

import com.frasercrossman.webcrawler.web.WebPageScraper;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public abstract class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private WebPageScraper webPageScraper;

  Crawler(Map<URL, Set<URL>> sitemap, WebPageScraper webPageScraper) {
    this.sitemap = sitemap;
    this.webPageScraper = webPageScraper;
  }

  Map<URL, Set<URL>> getSitemap() {
    return sitemap;
  }

  WebPageScraper getWebPageScraper() {
    return webPageScraper;
  }

  public abstract Map<URL, Set<URL>> crawlSite(URL url);
}
