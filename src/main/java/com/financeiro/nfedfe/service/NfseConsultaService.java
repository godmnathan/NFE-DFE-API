package com.financeiro.nfedfe.service;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;

@Slf4j
@Service
public class NfseConsultaService {

    private final WebClient webClient;

    public NfseConsultaService(WebClient.Builder webClientBuilder,
                               ResourceLoader resourceLoader,
                               @Value("${server.ssl.key-store-password}") String certificatePassword,
                               @Value("${server.ssl.key-store}") String certificatePath) {
        log.info("Certificate Password: {}", certificatePassword);
        log.info("Key Store Path: {}", certificatePath);

        this.webClient = buildWebClient(webClientBuilder, resourceLoader, certificatePassword, certificatePath);
    }

    private WebClient buildWebClient(WebClient.Builder webClientBuilder,
                                     ResourceLoader resourceLoader,
                                     String certificatePassword,
                                     String certificatePath) {
        try {
            Resource resource = resourceLoader.getResource(certificatePath);
            InputStream inputStream = resource.getInputStream();

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, certificatePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, certificatePassword.toCharArray());

            SslContext sslContext = SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                    .protocols("TLSv1.2")
                    .build();

            HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                    .responseTimeout(Duration.ofSeconds(10)); // Set your desired timeout

            ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

            return webClientBuilder.clientConnector(connector).baseUrl("https://adn.nfse.gov.br").build();
        } catch (Exception e) {
            throw new RuntimeException("Error building WebClient", e);
        }
    }

    public String consultarNfsePorNsu(String nsu) {
        return this.webClient.get()
                .uri("/DFe/{NSU}", nsu)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
