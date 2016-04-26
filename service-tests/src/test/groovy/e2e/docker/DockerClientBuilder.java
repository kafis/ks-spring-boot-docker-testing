package e2e.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.AuthConfig;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class DockerClientBuilder {

    public static DockerClient of() throws DockerCertificateException, IOException {
      return buildClient();
    }

    private static DockerClient buildClient() throws DockerCertificateException, IOException {
        return DefaultDockerClient
                .fromEnv()
                .authConfig(authConfig())
                .build();
    }

    private static AuthConfig authConfig() throws IOException {
        String username = System.getProperty("docker.user.name");
        String pwd = System.getProperty("docker.user.password");
        String email = System.getProperty("docker.user.email");

        if(StringUtils.isBlank(username) || StringUtils.isBlank(pwd) || StringUtils.isBlank(email)) {
            return AuthConfig.fromDockerConfig().build();
        }
        return AuthConfig.builder().username(username).password(pwd).email(email).build();
    }
}
