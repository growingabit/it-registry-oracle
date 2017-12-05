package org.growbit.utils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.google.api.client.util.Key;

@Component
@Scope(value = "singleton")
public class OraclizeUtil {

  private static final Logger logger = LoggerFactory.getLogger(OraclizeUtil.class);
  public static final JsonFactory JSON_FACTORY = new GsonFactory();
  public static final String ARG0_ME = "ARG0";
  public static final String ARG1_BUS = "ARG1";
  public static final String ARG2_MARK = "ARG2";

  private OraclizeUtil.OraclizeResult oraclize_result;
  private Map<String, String> container_envs;

  @PostConstruct
  public void init() {
    this.oraclize_result = new OraclizeResult();
    this.container_envs = this.init_container_args();
    this.append_output(this.container_envs);
  }

  private Map<String, String> init_container_args() {
    Map<String, String> envs = new HashMap<String, String>();
    try {
      envs = EnvironmentUtils.getProcEnvironment();
    } catch (IOException e) {
      logger.error("IOException " + e.getMessage());
    }

    return envs;
  }

  public Map<String, String> get_container_envs() {
    return this.container_envs;
  }

  public String get(String container_env_key) {
    return this.container_envs.get(container_env_key);
  }

  public void append_output(Object output) {
    try {
      this.oraclize_result.append_output(output, JSON_FACTORY);
    } catch (IOException e) {
      logger.error("IOException " + e.getMessage());
    }
  }

  private class OraclizeResult {

    @Key
    private final String started_at;

    @Key
    private List<Object> output = new ArrayList<Object>();

    @Key
    private String updated_at;

    public OraclizeResult() {
      this.started_at = DateTime.now().toString();
    }

    public void append_output(String output, JsonFactory jsonFactory) throws IOException {
      this.output.add(DateTime.now().toString() + " " + output);
      this.sync(jsonFactory);
    }

    public void append_output(Object output, JsonFactory jsonFactory) throws IOException {
      this.output.add(output);
      this.sync(jsonFactory);
    }

    private void sync(JsonFactory jsonFactory) throws IOException {
      this.updated_at = DateTime.now().toString();
      FileUtils.writeStringToFile(
          new File("/opt/spring-boot-app/logs/result.json"),
          jsonFactory.toPrettyString(this),
          "UTF-8",
          false
      );
    }
  }
}