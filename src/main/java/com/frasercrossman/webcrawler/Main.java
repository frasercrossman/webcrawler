package com.frasercrossman.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    Crawler crawler;

    if(args.length != 2) {
      System.out.println("Expected 2 args: [thread count] [url]");
    } else {
      int threadCount = Integer.valueOf(args[0]);
      String inputUrl = args[1];

      if(threadCount > 1) {
        // Limit thread count to available processors
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if(threadCount > availableProcessors) threadCount = availableProcessors;

        System.out.printf("MultiThreadedCrawler [%d threads]:\n", threadCount);
        crawler = new MultiThreadedCrawler(threadCount);
      } else {
        System.out.println("SingleThreadedCrawler:");
        crawler = new SingleThreadedCrawler();
      }

      try {
        URL url = new URL(inputUrl);
        Map<URL, Set<URL>> sitemap = crawler.crawlSite(url);

        sitemap.keySet().forEach(key -> {
          System.out.printf("%s\n", key);
          sitemap.get(key).forEach(page -> {
            System.out.printf("\t\t%s\n", page);
          });
        });
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
  }
}
