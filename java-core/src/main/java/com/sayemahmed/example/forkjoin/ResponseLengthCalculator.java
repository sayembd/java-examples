package com.sayemahmed.example.forkjoin;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class ResponseLengthCalculator extends RecursiveTask<Map<String, Integer>> {
  private final List<String> links;

  ResponseLengthCalculator(List<String> links) {
    this.links = links;
  }

  @Override
  protected Map<String, Integer> compute() {
    if (links.isEmpty()) {
      log.info("No more links to fetch");
      return Collections.emptyMap();
    }

    int middle = links.size() / 2;
    log.info("Middle index: {}", links, middle);
    ResponseLengthCalculator leftPartition = new ResponseLengthCalculator(links.subList(0, middle));
    ResponseLengthCalculator rightPartition = new ResponseLengthCalculator(links.subList(middle + 1, links.size()));

    log.info("Forking left partition");
    leftPartition.fork();
    log.info("Left partition forked, now forking right partition");
    rightPartition.fork();
    log.info("Right partition forked");

    String middleLink = links.get(middle);
    HttpRequester httpRequester = new HttpRequester(middleLink);
    String response;
    try {
      log.info("Calling managedBlock for {}", middleLink);
      ForkJoinPool.managedBlock(httpRequester);
      response = httpRequester.response;
    } catch (InterruptedException ex) {
      log.error("Error occurred while trying to implement blocking link fetcher", ex);
      response = "";
    }

    Map<String, Integer> responseMap = new HashMap<>(links.size());

    Map<String, Integer> leftLinks = leftPartition.join();
    responseMap.putAll(leftLinks);
    responseMap.put(middleLink, response.length());
    Map<String, Integer> rightLinks = rightPartition.join();
    responseMap.putAll(rightLinks);

    log.info("Left map {}, middle length {}, right map {}", leftLinks, response.length(), rightLinks);

    return responseMap;
  }

  private static class HttpRequester implements ForkJoinPool.ManagedBlocker {
    private final String link;
    private String response;

    private HttpRequester(String link) {
      this.link = link;
    }

    @Override
    public boolean block() {
      HttpGet headRequest = new HttpGet(link);
      CloseableHttpClient client = HttpClientBuilder
          .create()
          .disableRedirectHandling()
          .build();
      try {
        log.info("Executing blocking request for {}", link);
        CloseableHttpResponse response = client.execute(headRequest);
        log.info("HTTP request for link {} has been executed", link);
        this.response = EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        log.error("Error while trying to fetch response from link {}: {}", link, e.getMessage());
        this.response = "";
      }
      return true;
    }

    @Override
    public boolean isReleasable() {
      return false;
    }
  }
}

