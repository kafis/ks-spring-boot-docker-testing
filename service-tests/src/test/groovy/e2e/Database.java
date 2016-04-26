package e2e;

import ch.qos.logback.classic.Logger;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import e2e.docker.DockerContainer;
import e2e.docker.DockerContainerBuilder;
import e2e.docker.DockerContainerDecorator;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static e2e.docker.DockerContainerBuilder.PortMapping.fromHostPort;

public class Database extends DockerContainerDecorator {

    public Database() throws InterruptedException, DockerException, DockerCertificateException, IOException {
        super(DockerContainerBuilder.of()
                .withImageName("postgres")
                .withPortBinding(
                        fromHostPort("5432").toContainerPort("5432/tcp"))
                .withEnv(
                         "POSTGRES_PASSWORD=s3cr3t",
                         "POSTGRES_USER=pollservice")
                .build());
    }

    @Override
    public void start() throws DockerException, InterruptedException {
        System.out.println("Starting DB...");
        super.start();
        while(!available()) {
            Thread.sleep(500);
        }
    }

    @Override
    public void stop() throws DockerException, InterruptedException {
        System.out.println("Stopping DB...");
        super.stop();
    }

    public void restoreSchema() throws LiquibaseException, SQLException {
        System.out.println("Restoring Schema...");
        PostgresDatabase database = new PostgresDatabase();
        database.setConnection(new JdbcConnection(getConnection()));

        Liquibase liquibase = new Liquibase("db/changelog/master.xml", new ClassLoaderResourceAccessor(), database);

        //liquibase.update(new Contexts(), new LabelExpression());
    }

    private static Connection getConnection() throws SQLException {

        Properties connectionProps = new Properties();
        connectionProps.put("user", "pollservice");
        connectionProps.put("password", "s3cr3t");

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://dockerhost:5432/pollservice",
                connectionProps
        );
        return connection;
    }

    private static boolean available() {

        try {
            getConnection();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
