package com.frasercrossman.webcrawler;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class WebPageScraperTest {

  private MockWebConnection webConnection;
  private WebClient webClient;

  @Before
  public void setup() {
    webConnection = new MockWebConnection();
    webClient = new WebClient();
    webClient.setWebConnection(webConnection);
  }

  @Test
  public void testGetInternalLinks() throws MalformedURLException {
    final URL foo = new URL("http://foo.com/");
    final URL bar = new URL("http://bar.com/");
    final String htmlContent
        = "<html><head><title>foo</title></head><body>\n"
        + "<a href='" + foo.toString() + "/index.html'>Index</a>\n"
        + "<a href='" + foo.toString() + "/about.html'>About</a>\n"
        + "<a href='" + bar.toString() + "/index.html'>Bar Site</a>\n"
        + "</body></html>";

    webConnection.setResponse(foo, htmlContent);

    WebPageScraper webPageScraper = new WebPageScraper(webClient);
    Set<URL> internalLinks = webPageScraper.getInternalLinks(foo);

    // Assert that the correct number of links are returned and that the hostname matches
    assertEquals(2, internalLinks.size());
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", equalTo(foo.getHost()))));
  }

  @Test
  public void testGetURLWithoutParameters() throws MalformedURLException {
    final URL foo = new URL("http://foo.com/");
    final URL bar = new URL("http://bar.com/");
    final String htmlContent
        = "<html><head><title>foo</title></head><body>\n"
        + "<a href='" + foo.toString() + "index.html'>Index</a>\n"
        + "<a href='" + foo.toString() + "about.html#Introduction'>About Introduction</a>\n"
        + "<a href='" + foo.toString() + "about.html#Education'>About Education</a>\n"
        + "<a href='" + foo.toString() + "about.html#Talks'>About Talks</a>\n"
        + "<a href='" + foo.toString() + "about.html?apiid=are&apikey=8e1df01d\n'>Query</a>\n"
        + "<a href='" + bar.toString() + "index.html'>Bar Site</a>\n"
        + "<a href='" + bar.toString() + "index.html#Videos'>Bar Site</a>\n"
        + "</body></html>";

    webConnection.setResponse(foo, htmlContent);

    WebPageScraper webPageScraper = new WebPageScraper(webClient);
    Set<URL> internalLinks = webPageScraper.getInternalLinks(foo);

    // Assert that no urls contain references or query parameters
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("#")))));
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("?")))));
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("&")))));
  }
}
