package com.frasercrossman.webcrawler;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.frasercrossman.webcrawler.web.HTMLPageScraper;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class HTMLPageScraperTest {

  private MockWebConnection webConnection;
  private WebClient webClient;
  private URL foo;
  private URL bar;

  public HTMLPageScraperTest() throws MalformedURLException {
    foo = new URL("http://foo.com/");
    bar = new URL("http://bar.com/");
  }

  @Before
  public void setup() {
    webConnection = new MockWebConnection();
    webClient = new WebClient();
    webClient.setWebConnection(webConnection);
  }

  @Test
  public void testGetInternalLinks() {
    final String htmlContent
        = "<html><head><title>foo</title></head><body>\n"
        + "<a href='" + foo.toString() + "/index.html'>Index</a>\n"
        + "<a href='" + foo.toString() + "/about.html'>About</a>\n"
        + "<a href='" + bar.toString() + "/index.html'>Bar Site</a>\n"
        + "</body></html>";

    webConnection.setResponse(foo, htmlContent);

    HTMLPageScraper htmlPageScraper = new HTMLPageScraper(webClient);
    Set<URL> internalLinks = htmlPageScraper.getInternalLinks(foo);

    // Assert that the correct number of links are returned and that the hostname matches
    assertEquals(2, internalLinks.size());
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", equalTo(foo.getHost()))));
  }

  @Test
  public void testGetURLWithoutParameters() {
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

    HTMLPageScraper htmlPageScraper = new HTMLPageScraper(webClient);
    Set<URL> internalLinks = htmlPageScraper.getInternalLinks(foo);

    // Assert that no urls contain references or query parameters
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("#")))));
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("?")))));
    assertThat(internalLinks,
        everyItem(Matchers.hasProperty("host", not(containsString("&")))));
  }
}
