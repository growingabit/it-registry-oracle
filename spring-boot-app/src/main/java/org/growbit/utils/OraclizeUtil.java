package org.growbit.utils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private OraclizeUtil.OraclizeResult oraclize_result;

  @PostConstruct
  public void init() {
    this.oraclize_result = new OraclizeResult();
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