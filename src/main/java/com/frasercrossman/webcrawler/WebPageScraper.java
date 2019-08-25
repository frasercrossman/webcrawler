package com.frasercrossman.webcrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebPageScraper {

  private WebClient wc;

  WebPageScraper() {
    wc = new WebClient();
  }

  public Set<URL> getInternalLinks(URL url) {
    Set<URL> internalLinks = new HashSet<>();

    try {
      HtmlPage page = wc.getPage(url);
      List<HtmlElement> anchorElements = page.getBody().getHtmlElementsByTagName("a");
      String href;
      URL newPageURL;

      for (int i = 0; i < anchorElements.size(); i++) {
        href = anchorElements.get(i).getAttribute("href");

        // If link is local prepend root url protocol and hostname
        if (href.startsWith("/")) {
          href = url.getProtocol() + "://" + url.getHost() + href;
        }

        try {
          newPageURL = new URL(href);

          // Only add site if hosts match
          if (url.getHost().equalsIgnoreCase(newPageURL.getHost())) {
            internalLinks.add(newPageURL);
          }
        } catch (MalformedURLException e) {
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return internalLinks;
  }
}
