package e2e.pollresource

import e2e.PollServiceSpecification
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired

class PollSpec extends PollServiceSpecification {

    @Autowired
    private RESTClient client;

    def "can CRUD polls"() {
        when: "querying the empty resource "
        def response = client.get(path: "/polls")

        then:
        response.status == 200

        and:
        response.data == []

        when: "adding a entry"
        def entry = client.post(
                path: "/polls",
                body: [
                        "topic": "Test Title"
                ]).data

        then:
        entry.id != null
        entry.topic == "Test Title"
        entry.entries == []

        when: "querying the resource again"
        response = client.get(path: "/polls")

        then:
        response.data == [
                entry
        ]

        when: "deleting the poll"
        client.delete(path: "/polls/"+entry.id)

        and: "query the polls again"
        response = client.get(path: "/polls")

        then:
        response.data == []


    }
}
