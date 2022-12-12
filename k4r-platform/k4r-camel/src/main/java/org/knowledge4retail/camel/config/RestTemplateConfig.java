package org.knowledge4retail.camel.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.getDefaultHostnameVerifier;

@Configuration
@ComponentScan
@Slf4j
public class RestTemplateConfig {

    @Value("${auth.ssl.keyPassword}")
    private String keyStorePassword ;
    @Value("${auth.ssl.keyType}")
    private String keyStoreType;
    @Value("${auth.ssl.keyLocation}")
    private String keyLocation;

    @Bean
    @ConditionalOnProperty(name="auth.useSslAuthenticatedCalls", havingValue="true")
    public RestTemplate restTemplateAuthenticated() {
        log.debug("using authenticated Rest Template");
        RestTemplate template = new RestTemplate();

        try {
            template = new RestTemplate(clientHttpRequestFactory());
        } catch (Exception e) {
            log.error(e.toString());
        }

        return template;
    }

    @Bean
    @ConditionalOnProperty(name="auth.useSslAuthenticatedCalls", havingValue="false")
    public  RestTemplate restTemplateDefault() {
        log.debug("using default Rest Template");
        return new RestTemplate();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private HttpClient httpClient() throws Exception {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore trustStore = KeyStore.getInstance(keyStoreType);

        Resource resource = new FileSystemResource(keyLocation);

        if (resource.exists()) {

            try (InputStream inputStream = resource.getInputStream()) {
                if (inputStream != null) {
                    trustStore.load(inputStream, keyStorePassword.toCharArray());
                    keyManagerFactory.init(trustStore, keyStorePassword.toCharArray());
                }
            }
        } else {
            throw new RuntimeException("Cannot find resource: " + resource.getFilename());
        }

        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        sslcontext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, getDefaultHostnameVerifier());

        return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    }
}
