package org.growbit.controllers;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    return "see /_ah/health and /_ah/envs for status";
  }

  @RequestMapping(path = "/_ah/health", method = RequestMethod.GET)
  public String _ah() {
    return "ok";
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
