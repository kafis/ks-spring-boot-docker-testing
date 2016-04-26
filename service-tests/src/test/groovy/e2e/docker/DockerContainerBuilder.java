package e2e.docker;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class DockerContainerBuilder {
    private static final Logger LOG = LogManager.getLogger(DockerContainerBuilder.class);

    private String imageName;
    private String name;
    private List<String> links = new ArrayList<>();
    private Map<String, List<PortBinding>> portBindings = new HashMap<>();
    private List<String> parameters = new ArrayList<>();
    private Set<String> exposedPorts = new HashSet<>();
    private List<String> environment = new ArrayList<>();

    private DockerContainerBuilder() {
    }

    public static DockerContainerBuilder of() {
        return new DockerContainerBuilder();
    }

    public DockerContainerBuilder withImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public DockerContainerBuilder withContainerName(String name) {
        this.name = name;
        return this;
    }

    public DockerContainerBuilder withPortBinding(PortMapping... ports) {
        for (PortMapping portMapping : ports) {
            portBindings.put(portMapping.containerPort, Collections.singletonList(PortBinding.of("0.0.0.0", portMapping.hostPort)));
        }
        return this;
    }

    public DockerContainerBuilder withParameter(String... params) {
        Collections.addAll(parameters, params);
        return this;
    }

    public DockerContainerBuilder withEnv(String... env) {
        Collections.addAll(environment, env);
        return this;
    }

    public DockerContainerBuilder withExposedPorts(String... ports) {
        Collections.addAll(exposedPorts, ports);
        return this;
    }

    public DockerContainerBuilder withLinks(String links) {
        Collections.addAll(this.links, links);
        return this;
    }

    public DockerContainer build() throws DockerException, InterruptedException, DockerCertificateException, IOException {
        DockerClient docker = DockerClientBuilder.of();
        //docker.pull(imageName);


        final HostConfig hostConfig = buildHostConfig();
        final ContainerConfig containerConfig = buildContainerConfig(hostConfig);
        final ContainerCreation creation = docker.createContainer(containerConfig);
        String containerId = creation.id();
        return new DockerContainer(docker, containerId);
    }


    private HostConfig buildHostConfig() throws DockerCertificateException {
        HostConfig.Builder builder = HostConfig.builder();

        if(portBindings.size() > 0)
            builder.portBindings(portBindings);

        builder.links(links);
        return builder.build();
    }

    private ContainerConfig buildContainerConfig(HostConfig hostConfig) throws DockerCertificateException {
        ContainerConfig.Builder builder = ContainerConfig
                .builder()
                .hostConfig(hostConfig)
                .image(imageName);

        if(parameters.size() > 0)
            builder.cmd(parameters);

        if(exposedPorts.size() > 0)
            builder.exposedPorts(exposedPorts);

        if(environment.size() > 0) {
            builder.env(environment);
        }




        return builder.build();
    }

    public static class PortMapping {
        private final String hostPort;
        private final String containerPort;

        public PortMapping(String hostPort, String containerPort) {
            this.hostPort = hostPort;
            this.containerPort = containerPort;
        }

        public static Builder fromHostPort(String hostPort) {
            return new Builder(hostPort);
        }

        public static class Builder {
            private final String hostPort;

            public Builder(String hostPort) {
                this.hostPort = hostPort;
            }

            public PortMapping toContainerPort(String containerPort) {
                return new PortMapping(hostPort, containerPort);
            }
        }
    }
}
