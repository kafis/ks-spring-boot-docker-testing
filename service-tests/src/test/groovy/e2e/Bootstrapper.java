package e2e;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;

import java.io.IOException;

public class Bootstrapper {

    private Database db;

    public Bootstrapper() throws InterruptedException, DockerException, DockerCertificateException, IOException {
        db = new Database();
    }

    public void init() throws DockerException, InterruptedException {
        db.start();
    }

    public void close() throws DockerException, InterruptedException {
        db.stop();
        db.remove();
    }

    public Database getDb() {
        return db;
    }
}
