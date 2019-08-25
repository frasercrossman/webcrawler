package com.frasercrossman.webcrawler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.hamcrest.Matchers;

public class CrawlerTest {

  @Test
  public void testSitemapContainsURL() throws MalformedURLException {
    Crawler c = new Crawler();
    URL url = new URL("https://frasercrossman.com/");
    Map<URL, Set<URL>> sitemap = c.crawlSite(url);

    assertThat(sitemap.keySet(), hasItem(url));
  }

  @Test
  public void testSitemapKeyContainNoForeignURLs() throws MalformedURLException {
    Crawler c = new Crawler();
    URL url = new URL("https://frasercrossman.com/");
    Map<URL, Set<URL>> sitemap = c.crawlSite(url);

    assertThat(sitemap.keySet(),
        everyItem(Matchers.hasProperty("host", equalTo(url.getHost()))));
  }

  @Test
  public void testSitemapValuesContainNoForeignURLs() throws MalformedURLException {
    Crawler c = new Crawler();
    URL url = new URL("https://frasercrossman.com/");
    Map<URL, Set<URL>> sitemap = c.crawlSite(url);

    sitemap.keySet().forEach(key -> {
      assertThat(sitemap.get(key),
          everyItem(Matchers.hasProperty("host", equalTo(url.getHost()))));
    });
  }
}
