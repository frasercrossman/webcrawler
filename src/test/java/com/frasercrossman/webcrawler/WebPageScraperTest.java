package com.frasercrossman.webcrawler;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class WebPageScraperTest {

  @Test
  public void testStripAnchorFromURL() {
    String url = "https://frasercrossman.com/index.html";
    String urlWithAnchorTag = url + "#intro";

    WebPageScraper ws = new WebPageScraper();

    assertEquals(url, ws.stripAnchorFromURL(urlWithAnchorTag));
  }
}
