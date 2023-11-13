package pe.grpc.support.config.webclient

import io.micrometer.core.instrument.MeterRegistry
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.actuate.metrics.AutoTimer
import org.springframework.boot.actuate.metrics.web.reactive.client.DefaultWebClientExchangeTagsProvider
import org.springframework.boot.actuate.metrics.web.reactive.client.MetricsWebClientFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.function.Function

@Configuration
class WebClientConfig(val registry: MeterRegistry) {

    @Bean
    fun reactorResourceFactory() = ReactorResourceFactory().apply {
        connectionProvider = ConnectionProvider.builder("provider")
            .maxConnections(200)
            .maxLifeTime(Duration.ofSeconds(60))
            .maxIdleTime(Duration.ofSeconds(10))
            .evictInBackground(Duration.ofSeconds(30))
            //.pendingAcquireMaxCount(2)
            .lifo()
            .build()
    }

    @Bean
    fun webClient(): WebClient.Builder {

        val mapper: (HttpClient) -> HttpClient = {
            it.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .responseTimeout(Duration.ofSeconds(7))
                .doOnConnected { conn ->
                    conn.addHandlerLast(ReadTimeoutHandler(7))
                        .addHandlerLast(WriteTimeoutHandler(7))
                }
                .resolver { spec ->
                    spec.queryTimeout(Duration.ofSeconds(2))
                }
                .metrics(true, Function.identity())
        }

        val metricsWebClientFilterFunction = MetricsWebClientFilterFunction(
            registry, DefaultWebClientExchangeTagsProvider(), "metricsWebClient", AutoTimer.ENABLED,
        )

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(reactorResourceFactory(), mapper))
            .filter(metricsWebClientFilterFunction)
    }
}