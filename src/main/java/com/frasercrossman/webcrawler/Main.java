package com.frasercrossman.webcrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    WebClient wc = new WebClient();

    try {
      HtmlPage page = wc.getPage("https://www.frasercrossman.com");
      System.out.println(page.getTitleText());

      System.out.println("Anchor tags:");
      page.getBody().getHtmlElementsByTagName("a").forEach(htmlElement -> {
        String href = htmlElement.getAttribute("href");
        System.out.println(href);
      });

    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
