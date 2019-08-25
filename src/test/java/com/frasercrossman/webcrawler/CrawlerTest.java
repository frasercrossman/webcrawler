package com.frasercrossman.webcrawler;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class CrawlerTest {

  @Test
  public void testSitemapContainsURL() throws MalformedURLException {
    Crawler c = new Crawler();
    URL url = new URL("https://frasercrossman.com/");
    Map<URL, Set<URL>> sitemap = c.crawlSite(url);

    assertThat(sitemap.keySet(), hasItem(url));
  }
}
