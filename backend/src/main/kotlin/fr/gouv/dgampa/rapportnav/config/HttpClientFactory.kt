package fr.gouv.dgampa.rapportnav.config

import org.springframework.stereotype.Component
import java.net.http.HttpClient

@Component
class HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient.newBuilder().build();
    }
}
