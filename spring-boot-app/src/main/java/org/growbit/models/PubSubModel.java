package org.growbit.models;

import com.google.api.client.util.Key;
import java.io.IOException;
import org.growbit.utils.OraclizeUtil;
import org.json.JSONObject;

public class PubSubModel {

  public static final String PING = "ping";

  @Key
  private final String sender_key;

  @Key
  private final String recipient_key;

  @Key
  private String _message_type;

  @Key
  private Object _message;

  public PubSubModel(String recipient_key, String sender_key) {
    this.recipient_key = recipient_key;
    this.sender_key = sender_key;
  }

  public void set_message_type(String _message_type) {
    this._message_type = _message_type;
  }

  public void set_message(Object _message) {
    this._message = _message;
  }

  @Override
  public String toString() {
    try {
      return OraclizeUtil.JSON_FACTORY.toPrettyString(this);
    } catch (IOException e) {
      JSONObject error_object = new JSONObject();
      error_object.put("error", "IOException");
      error_object.put("error_message", e.getMessage());
      return error_object.toString();
    }
  }
}
