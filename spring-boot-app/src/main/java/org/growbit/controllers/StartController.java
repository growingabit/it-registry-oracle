package org.growbit.controllers;


import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.growbit.models.PubSubModel;
import org.growbit.utils.OraclizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController {

  private static final Logger logger = LoggerFactory.getLogger(StartController.class);

  private final Publisher publisher;
  String topicId = "oraclize";
  String projectId = "growbit-0";

  @Autowired
  private OraclizeUtil oraclizeUtil;

  public StartController() throws IOException {
    List<String> scopes = Arrays.asList(
        "https://www.googleapis.com/auth/cloud-platform",
        "https://www.googleapis.com/auth/pubsub"
    );

    ClassPathResource gcloud_json_key = new ClassPathResource("growbit-1154edf3284d.json");

    GoogleCredentials credentials = GoogleCredentials.fromStream(gcloud_json_key.getInputStream()).createScoped(scopes);

    GoogleCredentialsProvider google_credentials_provider = new GoogleCredentialsProvider() {
      @Override
      public Credentials getCredentials() throws IOException {
        return credentials;
      }

      @Override
      public List<String> getScopesToApply() {
        return scopes;
      }
    };

    TopicName topicName = TopicName.of(projectId, topicId);

    Publisher p = Publisher.newBuilder(topicName).setCredentialsProvider(google_credentials_provider).build();

    this.publisher = p;
  }

  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public void start(HttpServletRequest request) {
    oraclizeUtil.append_output(request.getServletPath());

    String recipient_key = oraclizeUtil.get(oraclizeUtil.ARG0_ME);
    String sender_key = "a sender key";
    PubSubModel model = new PubSubModel(recipient_key, sender_key);
    model.set_message_type(PubSubModel.PING);
    model.set_message("encrypt(sign(mark,sender_key), me)");

    this.publish(model);
  }

  private void publish(PubSubModel payload) {
    PubsubMessage pubsubMessage =
        PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(payload.toString())).build();

    this.publisher.publish(pubsubMessage);

    ApiFuture<String> future = this.publisher.publish(pubsubMessage);

    ApiFutures.addCallback(future, new ApiFutureCallback<String>() {

      @Override
      public void onFailure(Throwable throwable) {
        String error_message = "Pub onFailure " + throwable.getMessage();
        if (throwable instanceof ApiException) {
          ApiException apiException = ((ApiException) throwable);
          // details on the API exception
          error_message += " " + apiException.getStatusCode().getCode();
          error_message += " " + apiException.isRetryable();
        }
        logger.error(error_message);
        oraclizeUtil.append_output(error_message);
      }

      @Override
      public void onSuccess(String messageId) {
        // Once published, returns server-assigned message ids (unique within the topic)
        String success_message = "Pub onSuccess " + messageId;
        logger.info(success_message);
        oraclizeUtil.append_output(success_message);
      }
    });

    try {
      this.publisher.shutdown();
    } catch (Exception e) {
      String error_message = "Exception on Publisher shutdown " + e.getMessage();
      logger.error(error_message);
      oraclizeUtil.append_output(error_message);
    }
  }
}
