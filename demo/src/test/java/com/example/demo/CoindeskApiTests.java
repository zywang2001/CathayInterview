package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoindeskApiTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CoindeskApiTests.class);

    @Test
    String testInsert() throws JsonMappingException, JsonProcessingException {
        String json = "{\"code\": \"testCode\",\"symbol\": \"testSymbol\",\"rate\": \"12.2\",\"description\": \"testDesc\",\"rate_float\": 0}";
        ObjectMapper objectMapper = new ObjectMapper();
        CoinType coinType = objectMapper.readValue(json, CoinType.class);

        HttpEntity<CoinType> request = new HttpEntity<>(coinType);
        ResponseEntity<String> responseEntity =
                testRestTemplate.postForEntity("/insert", request, String.class);
        return responseEntity.getBody();
    }
      
    @Test
    void testSelectById() throws JsonMappingException, JsonProcessingException {
        String id = testInsert();
        String url = "/selectById/" + String.valueOf(id);
        CoinType response =
                testRestTemplate.getForObject(url, CoinType.class);

        assertNotNull(response);
        assertEquals(response.getCode(), "testCode");
        assertEquals(response.getSymbol(), "testSymbol");
        assertEquals(response.getRate(), "12.2");        
        assertEquals(response.getDescription(), "testDesc");
        assertEquals(response.getRate_float(), 0);
    }

    @Test
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        String id = testInsert();

        String json = "{\"code\": \"updateCode\",\"symbol\": \"updateSymbol\",\"rate\": \"999.9\",\"description\": \"updateDesc\",\"rate_float\": 0}";
        ObjectMapper objectMapper = new ObjectMapper();
        CoinType updateCoinType = objectMapper.readValue(json, CoinType.class);

        HttpEntity<CoinType> request = new HttpEntity<>(updateCoinType);
        String updateUrl = "/update/" + String.valueOf(id);
        testRestTemplate.put(updateUrl, request);

        String selectUrl = "/selectById/" + String.valueOf(id);
        CoinType response = testRestTemplate.getForObject(selectUrl, CoinType.class);

        assertNotNull(response);
        assertEquals(response.getCode(), "updateCode");
        assertEquals(response.getSymbol(), "updateSymbol");
        assertEquals(response.getRate(), "999.9");        
        assertEquals(response.getDescription(), "updateDesc");
        assertEquals(response.getRate_float(), 0);
    }

    @Test
    void testDelete() throws JsonMappingException, JsonProcessingException {
        String id = testInsert();
        String deleteUrl = "/delete/" + String.valueOf(id);
        testRestTemplate.delete(deleteUrl);

        @SuppressWarnings("rawtypes")
        List response = testRestTemplate.getForObject("/selectAll", List.class);
        assertTrue(response.size() == 0);
    }


    @Test
    String testCallCoindesk() {
        String response = 
                testRestTemplate.getForObject("/callCoindesk", String.class);   
        logger.info("Response from callCoindesk: {}", response);
        return response;
    }

    @Test
    void testConvertCoindesk() {
        ResponseEntity<String> response = 
                testRestTemplate.getForEntity("/convertCoindesk", String.class);   
        logger.info("Response from convertCoindesk: {}", response);
    }

}