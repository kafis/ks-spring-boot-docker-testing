package e2e

import de.rewe.poll.PollApplicationConfiguration
import groovyx.net.http.RESTClient
import groovyx.net.http.Status
import org.codehaus.groovy.runtime.MethodClosure
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [E2EConfiguration])
@WebIntegrationTest
class PollServiceSpecification extends Specification {

    private static Bootstrapper bootstrapper;

    static {
        bootstrapper = new Bootstrapper()
        bootstrapper.init();
    }



    @Import(PollApplicationConfiguration)
    static class E2EConfiguration {

        @Bean
        public RESTClient client() {
            def client =  new RESTClient("http://localhost:8080");
            client.contentType = "application/json"
            client.handler.put(Status.FAILURE, {req, resp-> resp})
            return client
        }

        @Bean
        public Database database() {
            return bootstrapper.db;
        }

        @PreDestroy
        private void close() {
            bootstrapper.close();
        }
    }
}
