package org.knowledge4retail.camel.process;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.knowledge4retail.camel.xml.PriceLabel;
import org.knowledge4retail.camel.xml.Product;
import org.knowledge4retail.camel.xml.ShelfScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetData implements Processor {


    @Override
    @Builder
    public void process(Exchange exchange) throws Exception {

        ShelfScan shelfScan = exchange.getIn().getBody(ShelfScan.class);

        List<PriceLabel> priceLabels = shelfScan.getPriceLabels();
        for (PriceLabel priceLabel : priceLabels) {

            Integer barcode = priceLabel.getBarcode();
            Product product = new Product();

            try {

                product = sendRestRequest(barcode);
            } catch (HttpClientErrorException | NullPointerException e) {

                log.debug(String.format("barcode %d not found in the database", barcode));
                product = null;
            }

            priceLabel.setProduct(product);

            try {

                priceLabel.setDescription(getDescriptionByRestRequest(barcode));
            } catch (HttpClientErrorException | NullPointerException e) {

                log.debug(String.format("barcode %d not found in the database", barcode));
                priceLabel.setDescription("product not found by dan");
            }

            try {

                URLConnection con = new URL(String.format("https://products.dm.de/images/de/products/dans/%s/images/medium", barcode)).openConnection();
                InputStream is = con.getInputStream();
                priceLabel.setPictureUrl(con.getURL().toString());
            } catch (HttpClientErrorException | IOException e) {

                log.debug(String.format("picture URL to barcode %d not found in the database", barcode));
                priceLabel.setPictureUrl("product not found by dan");
            }
        }

        exchange.getMessage().setHeader("shelfId", shelfScan.getShelf().getUbicaExternalReferenceId());
    }

    private Product sendRestRequest(Integer barcode) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "URL";

        ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders("--", "--")), HashMap.class);
        HashMap infoMap = response.getBody();

        Product product = new Product();
        product.setHeight(Double.parseDouble(infoMap.get("hoehe").toString()));
        product.setWidth(Double.parseDouble(infoMap.get("breite").toString()));
        product.setDepth(Double.parseDouble(infoMap.get("laenge").toString()));

        return product;
    }

    private String getDescriptionByRestRequest(Integer barcode) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "URL";

        ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders("--", "--")), HashMap.class);
        HashMap infoMap = response.getBody();

        return infoMap.get("bezeichnung").toString();
    }

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode ( auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}