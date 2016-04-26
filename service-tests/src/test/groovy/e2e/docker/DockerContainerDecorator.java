package e2e.docker;

import com.spotify.docker.client.DockerException;

public class DockerContainerDecorator extends DockerContainer {

    private final DockerContainer delegate;

    public DockerContainerDecorator(DockerContainer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void start() throws DockerException, InterruptedException {
        delegate.start();
    }

    @Override
    public void remove() throws DockerException, InterruptedException {
        delegate.remove();
    }

    @Override
    public void stop() throws DockerException, InterruptedException {
        delegate.stop();
    }

    @Override
    public void execute(String... cmd) throws DockerException, InterruptedException {
        delegate.execute(cmd);
    }

    @Override
    public String containerName() throws DockerException, InterruptedException {
        return delegate.containerName();
    }

    @Override
    public String host() {
        return delegate.host();
    }


    @Override
    public void pause() throws DockerException, InterruptedException {
        delegate.pause();
    }

    @Override
    public void resume() throws DockerException, InterruptedException {
        delegate.resume();
    }
}
