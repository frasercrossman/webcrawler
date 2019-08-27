package com.frasercrossman.webcrawler;

import static com.gargoylesoftware.htmlunit.util.UrlUtils.getUrlWithNewPath;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import com.frasercrossman.webcrawler.engine.Crawler;
import com.frasercrossman.webcrawler.engine.MultiThreadedCrawler;
import com.frasercrossman.webcrawler.engine.SingleThreadedCrawler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CrawlerTest {

  private static MockWebConnection webConnection;

  private Crawler crawler;
  private URL foo;

  @Parameters
  public static Collection<Crawler> crawlers() {
    webConnection = new MockWebConnection();
    WebClient webClient = new WebClient();
    webClient.setWebConnection(webConnection);

    return Arrays.asList(new SingleThreadedCrawler(webClient), new MultiThreadedCrawler(webClient));
  }

  public CrawlerTest(Crawler crawler) throws MalformedURLException {
    this.crawler = crawler;

    foo = new URL("http://foo.com/");
    URL bar = new URL("http://bar.com/");

    final String fooIndexHtmlContent
        = "<html><head><title>foo</title></head><body>\n"
        + "<a href='" + foo.toString() + "index.html'>Index</a>\n"
        + "<a href='" + foo.toString() + "about.html'>About</a>\n"
        + "<a href='" + bar.toString() + "index.html'>Bar Site</a>\n"
        + "</body></html>";

    final String fooAboutHtmlContent
        = "<html><head><title>foo</title></head><body>\n"
        + "<a href='" + foo.toString() + "index.html'>Index</a>\n"
        + "<a href='" + foo.toString() + "about.html'>About</a>\n"
        + "<a href='" + foo.toString() + "404.html'>404</a>\n"
        + "<a href='" + bar.toString() + "index.html'>Bar Site Index</a>\n"
        + "<a href='" + bar.toString() + "about.html'>Bar Site About</a>\n"
        + "</body></html>";

    webConnection.setDefaultResponse("404 page not found");
    webConnection.setResponse(foo, fooIndexHtmlContent);
    webConnection.setResponse(getUrlWithNewPath(foo, "/index.html"), fooIndexHtmlContent);
    webConnection.setResponse(getUrlWithNewPath(foo, "/about.html"), fooAboutHtmlContent);
  }

  @Test
  public void testSitemapContainsURL() {
    Map<URL, Set<URL>> sitemap = crawler.crawlSite(foo);

    assertThat(sitemap.keySet(), hasItem(foo));
  }

  @Test
  public void testSitemapKeyContainNoForeignURLs() {
    Map<URL, Set<URL>> sitemap = crawler.crawlSite(foo);

    assertThat(sitemap.keySet(),
        everyItem(Matchers.hasProperty("host", equalTo(foo.getHost()))));
  }

  @Test
  public void testSitemapValuesContainNoForeignURLs() throws MalformedURLException {
    Map<URL, Set<URL>> sitemap = crawler.crawlSite(foo);

    sitemap.keySet().forEach(key -> {
      assertThat(sitemap.get(key),
          everyItem(Matchers.hasProperty("host", equalTo(foo.getHost()))));
    });
  }

  @Test
  public void testAllDiscoveredPagesAreExplored() {
    Map<URL, Set<URL>> sitemap = crawler.crawlSite(foo);

    Set<URL> discoveredPages = new HashSet<>();
    discoveredPages.add(foo);
    sitemap.keySet().forEach(key -> discoveredPages.addAll(sitemap.get(key)));

    assertEquals(discoveredPages, sitemap.keySet());
  }
}
