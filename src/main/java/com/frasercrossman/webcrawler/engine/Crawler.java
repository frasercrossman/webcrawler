package com.frasercrossman.webcrawler.engine;

import com.frasercrossman.webcrawler.web.HTMLPageScraper;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public abstract class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private HTMLPageScraper htmlPageScraper;

  Crawler(Map<URL, Set<URL>> sitemap, HTMLPageScraper htmlPageScraper) {
    this.sitemap = sitemap;
    this.htmlPageScraper = htmlPageScraper;
  }

  Map<URL, Set<URL>> getSitemap() {
    return sitemap;
  }

  HTMLPageScraper getHtmlPageScraper() {
    return htmlPageScraper;
  }

  public abstract Map<URL, Set<URL>> crawlSite(URL url);
}
