package e2e.pollresource

import e2e.Database
import e2e.PollServiceSpecification
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired

class RecoverySpec extends PollServiceSpecification {
    @Autowired
    RESTClient client
    @Autowired
    Database db

    def "can reconnect to db"(){
        given: "a database that is down"
        db.stop()

        when: "querying the resource"
        def response = client.get(path: "/polls")

        then:
        response.status == 500

        when: "the db is up again"
        db.start()
        db.restoreSchema()

        and: "querying the resouce again"
        response = client.get(path: "/polls")

        then: "its all good"
        response.status == 200
        response.data == []
    }
}
