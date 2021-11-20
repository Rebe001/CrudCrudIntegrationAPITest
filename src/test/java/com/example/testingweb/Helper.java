package com.example.testingweb;

import com.example.testingweb.model.Charger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;


public class Helper {

    private final Logger logger = LogManager.getLogger(this.getClass());

    public Charger createNewCharger() {
        logger.info("create defaulted charger");
        Charger charger = new Charger();
        charger.setModel("Copper SB");
        charger.setPrice(959);
        return charger;
    }

    public HttpEntity<String> createJsonRequestWithNewCharger() {
        logger.info("create json schema with defaulted charger");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(new Gson().toJson(createNewCharger()), header);
    }

    public HttpEntity<String> createJsonRequestWithNewCharger(Charger charger) {
        logger.info("create json schema with defaulted charger");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(new Gson().toJson(charger), header);
    }

    public String getId (ResponseEntity<String> postResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(postResponse.getBody());
        return root.path("_id").asText();
    }
}
