package com.frasercrossman.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    Crawler c = new Crawler();

    try {
      URL url = new URL(args[0]);
      Map<URL, Set<URL>> sitemap = c.crawlSite(url);

      sitemap.keySet().forEach(key -> {
        System.out.printf("%s\n", key);
        sitemap.get(key).forEach(page -> {
          System.out.printf("\t%s\n", page);
        });
      });
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
}
