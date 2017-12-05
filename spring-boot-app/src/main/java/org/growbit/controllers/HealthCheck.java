package org.growbit.controllers;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
  private static final Logger logger = LoggerFactory.getLogger(HealthCheck.class);

  @Autowired
  private ApplicationContext context;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    return "see /_ah/health and /_ah/envs for status";
  }

  @RequestMapping(path = "/_ah/health", method = RequestMethod.GET)
  public String _health() {
    return "ok";
  }

  @RequestMapping(path = "/_ah/exit", method = RequestMethod.GET)
  public void _exit() {

    SpringApplication.exit(this.context);
  }

  @RequestMapping(path = "/_ah/envs", method = RequestMethod.POST)
  public Map<String, String> _ah_envs() {

    Map<String, String> envs = null;

    try {
      envs = EnvironmentUtils.getProcEnvironment();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return envs;
  }
}
