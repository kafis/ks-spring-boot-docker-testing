package e2e.docker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.LogStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author nimag
 */
public class DockerContainer {
    private static final Logger LOG = LogManager.getLogger(DockerContainer.class);

    final private DockerClient docker;
    final private String containerId;

    protected DockerContainer() {
        this(null,null);
    }

    public DockerContainer(DockerClient docker, String containerId) {
        this.docker = docker;
        this.containerId = containerId;
    }

    public void start() throws DockerException, InterruptedException {
        docker.startContainer(containerId);
    }

    public void pause() throws DockerException, InterruptedException {
        docker.pauseContainer(containerId);
    }

    public void resume() throws DockerException, InterruptedException {
        docker.restartContainer(containerId);
    }

    public void stop() throws DockerException, InterruptedException {
        docker.killContainer(containerId);
    }

    public void remove() throws DockerException, InterruptedException {
        docker.removeContainer(containerId);
    }

    public LogStream log() throws DockerException, InterruptedException {
        return docker.attachContainer(containerId);
    }

    public String containerName() throws DockerException, InterruptedException {
        return docker.inspectContainer(containerId).name();
    }

    public void execute(String... cmd) throws DockerException, InterruptedException {
        final String execId = docker.execCreate(containerId, cmd, DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
        String output;
        try (final LogStream logStream = docker.execStart(execId)) {
            output = logStream.readFully();
        }
        LOG.info("Executing "+String.join(" ", cmd)+":\n"+output);
    }

    public String host() {
        return docker.getHost();
    }
}
