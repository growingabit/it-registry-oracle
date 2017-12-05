package org.growbit.controllers;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.growbit.utils.GoogleAuth;
import org.growbit.utils.OraclizeUtil;
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

  @Autowired
  private OraclizeUtil oraclizeUtil;

  @Autowired
  private GoogleAuth gauth;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    return "see /_ah/health and /_ah/envs for status, /_ah/exit to stop";
  }

  @RequestMapping(path = "/_ah/health", method = RequestMethod.GET)
  public String _health(HttpServletRequest request) {
    oraclizeUtil.append_output(request.getServletPath());
    return "pong";
  }

  @RequestMapping(path = "/_ah/token", method = RequestMethod.GET)
  public String _token(HttpServletRequest request) throws IOException {
    oraclizeUtil.append_output(request.getServletPath());
    String access_token = gauth.access_token();
    oraclizeUtil.append_output(access_token);
    return access_token;
  }

  @RequestMapping(path = "/_ah/exit", method = RequestMethod.GET)
  public void _exit(HttpServletRequest request) {
    oraclizeUtil.append_output(request.getServletPath());
    SpringApplication.exit(this.context);
  }

  @RequestMapping(path = "/_ah/envs", method = RequestMethod.POST)
  public Map<String, String> _ah_envs(HttpServletRequest request) {
    oraclizeUtil.append_output(request.getServletPath());

    Map<String, String> envs = oraclizeUtil.get_container_envs();

    oraclizeUtil.append_output(envs);

    return envs;
  }
}
