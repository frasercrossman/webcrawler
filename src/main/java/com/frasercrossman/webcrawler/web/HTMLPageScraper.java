package com.frasercrossman.webcrawler.web;

import static com.gargoylesoftware.htmlunit.util.UrlUtils.resolveUrl;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HTMLPageScraper {

  private WebClient webClient;

  public HTMLPageScraper() {
    this(new WebClient());
  }

  public HTMLPageScraper(WebClient webClient) {
    this.webClient = webClient;
    webClient.setCssEnabled(false);
    webClient.setAppletEnabled(false);
    webClient.setJavaScriptEnabled(false);
  }

  public Set<URL> getInternalLinks(URL url) {
    Set<URL> internalLinks = new HashSet<>();

    try {
      Page page = webClient.getPage(url);

      // Only proceed if the page is an HTML page
      if (page instanceof HtmlPage) {
        HtmlPage htmlPage = (HtmlPage) page;

        List<HtmlElement> anchorElements = htmlPage.getBody().getHtmlElementsByTagName("a");
        String href;
        URL newPageURL;

        for (HtmlElement anchorElement : anchorElements) {
          href = anchorElement.getAttribute("href");

          // If URL is relative resolve url, otherwise leave unchanged
          href = resolveUrl(url, href);

          try {
            newPageURL = getURLWithoutParameters(new URL(href));

            // Only add page url if hosts match
            if (url.getHost().equalsIgnoreCase(newPageURL.getHost())) {
              internalLinks.add(newPageURL);
            }
          } catch (MalformedURLException e) {
            // The URL specified in the href attribute was malformed
            // It can be safely ignored as the link cannot be followed
          }
        }
      }
    } catch (Exception e) {
      // If something goes wrong while requesting or parsing the page an exception will be thrown
      // It can be safely ignored, an empty set of internal links is returned
    }

    return internalLinks;
  }

  private URL getURLWithoutParameters(URL url) throws MalformedURLException {
    return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath());
  }
}
