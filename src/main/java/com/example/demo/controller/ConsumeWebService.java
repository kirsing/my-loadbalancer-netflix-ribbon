package com.example.demo.controller;


import com.example.demo.Pizza;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
public class ConsumeWebService {


   private DiscoveryClient discoveryClient;


   private LoadBalancerClient loadBalancer;


   @RequestMapping(value = "/template/products")
   @HystrixCommand(fallbackMethod = "fallBackgetPizzaList", commandProperties = {
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
   })
   public List<Pizza> getPizzaList() throws InterruptedException {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      RestTemplate restTemplate = new RestTemplate();

      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      System.out.println(serviceInstance.getUri());
      ResponseEntity<List<Pizza>> responseEntity = restTemplate.exchange("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Pizza>>() {
      });
      Thread.sleep(800);
      return responseEntity.getBody();
   }

   public List<Pizza> fallBackgetPizzaList() {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      RestTemplate restTemplate = new RestTemplate();

      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      System.out.println(serviceInstance.getUri());
      ResponseEntity<List<Pizza>> responseEntity = restTemplate.exchange("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Pizza>>() {
      });

      return responseEntity.getBody();
   }

   @RequestMapping(value = "/template/pizza", method = RequestMethod.POST)
   @HystrixCommand(fallbackMethod = "fallBackCreatePizza", commandProperties = {
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
   })
   public Pizza createPizza(@RequestBody Pizza product) throws InterruptedException {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(product, headers);
      RestTemplate restTemplate = new RestTemplate();
      Thread.sleep(800);
      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza", HttpMethod.POST, entity, Pizza.class).getBody();
   }

   public Pizza fallBackCreatePizza(@RequestBody Pizza product) {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(product, headers);
      RestTemplate restTemplate = new RestTemplate();

      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza", HttpMethod.POST, entity, Pizza.class).getBody();
   }


   @HystrixCommand(fallbackMethod = "fallBackUpdateProduct", commandProperties = {
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
   })
   @RequestMapping(value = "/template/pizza/{id}", method = RequestMethod.PUT)
   public Pizza updateProduct(@PathVariable("id") String id, @RequestBody Pizza pizza) throws InterruptedException {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(pizza, headers);
      RestTemplate restTemplate = new RestTemplate();
      Thread.sleep(800);
      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza/" + id, HttpMethod.PUT, entity, Pizza.class).getBody();
   }

   public Pizza fallBackUpdateProduct(@PathVariable("id") String id, @RequestBody Pizza pizza) {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(pizza, headers);
      RestTemplate restTemplate = new RestTemplate();

      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza/" + id, HttpMethod.PUT, entity, Pizza.class).getBody();
   }

   @HystrixCommand(fallbackMethod = "fallBackDeletePizza", commandProperties = {
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
   })
   @RequestMapping(value = "/template/pizza/{id}", method = RequestMethod.DELETE)
   public Void deletePizza(@PathVariable("id") String id) throws InterruptedException {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(headers);
      RestTemplate restTemplate = new RestTemplate();
      Thread.sleep(800);
      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza/" + id, HttpMethod.DELETE, entity, Void.class).getBody();
   }

   public Void fallBackDeletePizza(@PathVariable("id") String id) {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<Pizza> entity = new HttpEntity<Pizza>(headers);
      RestTemplate restTemplate = new RestTemplate();

      String serviceId = "EUREKA-CLIENT-PIZZA".toLowerCase();
      ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);
      return restTemplate.exchange(
              "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/stock/pizza/" + id, HttpMethod.DELETE, entity, Void.class).getBody();
   }
}
