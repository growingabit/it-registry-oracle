package it.growbit.controllers;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

  @RequestMapping(value = "/_ah/health", method = RequestMethod.GET)
  public String healthcheck() {
    return "ok";
  }

  @RequestMapping(value = "_ah/health/dump-envs", method = RequestMethod.GET)
  public String dump_envs() {
    try {
      Map<String, String> envs = EnvironmentUtils.getProcEnvironment();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "ok";
  }
}
