package com.frasercrossman.webcrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Crawler {

  private Map<URL, Set<URL>> sitemap;
  private Queue<URL> pageQueue;

  Crawler() {
    sitemap = new HashMap<>();
    pageQueue = new ArrayDeque<>();
  }

  Map<URL, Set<URL>> crawlSite(URL rootUrl) {
    WebClient wc = new WebClient();
    URL currentPageURL;
    URL newPageURL;

    List<HtmlElement> anchorElements;
    HtmlPage page;
    String href;

    // Add first page
    pageQueue.add(rootUrl);

    while (!pageQueue.isEmpty()) {
      currentPageURL = pageQueue.remove();
      sitemap.put(currentPageURL, new HashSet<>());

      try {
        page = wc.getPage(currentPageURL);

        anchorElements = page.getBody().getHtmlElementsByTagName("a");
        for (int i = 0; i < anchorElements.size(); i++) {
          href = anchorElements.get(i).getAttribute("href");

          // If link is local prepend root reference
          if (href.startsWith("/")) {
            href = rootUrl.getProtocol() + "://" + rootUrl.getHost() + href;
          }

          // Ensure url is well formed
          try {
            newPageURL = new URL(href);

            // Only add site if it hosts match
            if (rootUrl.getHost().equalsIgnoreCase(newPageURL.getHost())) {
              sitemap.get(currentPageURL).add(newPageURL);

              // New page has not been searched and is not in the queue then add it to the queue
              if (!sitemap.containsKey(newPageURL) && !pageQueue.contains(newPageURL)) {
                pageQueue.add(newPageURL);
              }
            }
          } catch (MalformedURLException e) {
          }
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

    return sitemap;
  }
}
