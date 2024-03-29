package com.frasercrossman.webcrawler;

import com.frasercrossman.webcrawler.engine.Crawler;
import com.frasercrossman.webcrawler.engine.MultiThreadedCrawler;
import com.frasercrossman.webcrawler.engine.SingleThreadedCrawler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    Crawler crawler;

    if (args.length != 2) {
      System.out.println("Expected 2 args: [thread count] [url]");
    } else {
      int threadCount = Integer.valueOf(args[0]);
      String inputUrl = args[1];

      if (threadCount > 1) {
        // Thread count must be between 1 and the number of available processors
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        threadCount = Math.min(availableProcessors, Math.max(1, threadCount));

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
