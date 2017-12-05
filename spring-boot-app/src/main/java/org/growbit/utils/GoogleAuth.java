package org.growbit.utils;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class GoogleAuth {

  private static final Logger logger = LoggerFactory.getLogger(GoogleAuth.class);
  private GoogleCredentials credentials;
  private GoogleCredentialsProvider google_credentials_provider;

  @PostConstruct
  public void init() {
    List<String> scopes = Arrays.asList(
        "https://www.googleapis.com/auth/cloud-platform",
        "https://www.googleapis.com/auth/pubsub"
    );

    ClassPathResource gcloud_json_key = new ClassPathResource("growbit-1154edf3284d.json");

    try {
      this.credentials = GoogleCredentials.fromStream(gcloud_json_key.getInputStream()).createScoped(scopes);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.google_credentials_provider = new GoogleCredentialsProvider() {
      @Override
      public Credentials getCredentials() throws IOException {
        return GoogleAuth.this.credentials;
      }

      @Override
      public List<String> getScopesToApply() {
        return scopes;
      }
    };
  }

  public String access_token() throws IOException {
    AccessToken at = this.credentials.refreshAccessToken();
    return at.getTokenValue();
  }

  public CredentialsProvider get_google_credentials_provider() {
    return google_credentials_provider;
  }
}