package com.example.testingweb;

import com.example.testingweb.model.Charger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SmokeTest extends Helper {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private String getRootUrl() {
        String endpointIdentifier = "5abbfac228004cb1a9d879995e24ff9e";
        return "https://crudcrud.com/api/" + endpointIdentifier + "/chargers/";
    }
    String id;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAndGetAllCharters()  {
        logger.info("assert create and get all chargers should pass");
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(getRootUrl(), createJsonRequestWithNewCharger(), String.class);
        assertThat(postResponse.getBody()).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<Charger[]> getResponse = this.restTemplate.getForEntity(getRootUrl(), Charger[].class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCreateAndGetACharter() throws JsonProcessingException {
        logger.info("assert create and get charger by id should pass");
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(getRootUrl(), createJsonRequestWithNewCharger(), String.class);
        assertThat(postResponse.getBody()).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        this.id = getId(postResponse);
        ResponseEntity<Charger> getResponse = this.restTemplate.getForEntity(getRootUrl() + id, Charger.class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getModel()).isEqualTo(createNewCharger().getModel());
        assertThat(getResponse.getBody().getPower()).isEqualTo(createNewCharger().getPower());
        assertThat(getResponse.getBody().getPrice()).isEqualTo(createNewCharger().getPrice());
    }
    @Test
    public void testDeleteAndGetACharter() throws JsonProcessingException {
        logger.info("assert delete and get a charger by id should pass");
        testCreateAndGetACharter();
        Charger charger = this.restTemplate.getForObject(getRootUrl() + this.id, Charger.class);
        assertThat(charger).isNotNull();
        this.restTemplate.delete(getRootUrl() + this.id);
        ResponseEntity<Charger> getResponse = this.restTemplate.getForEntity(getRootUrl() + this.id, Charger.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateAndGetACharter() throws JsonProcessingException  {
        logger.info("assert update and get a charger by id should pass");
        testCreateAndGetACharter();
        Charger charger = this.restTemplate.getForObject(getRootUrl() + this.id, Charger.class);
        charger.setModel("Commander 2");
        charger.setPower("22kW");
        this.restTemplate.put(getRootUrl() + this.id, createJsonRequestWithNewCharger(charger), String.class);
        ResponseEntity<Charger> getResponse = this.restTemplate.getForEntity(getRootUrl() + this.id, Charger.class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getModel()).isEqualTo(charger.getModel());
        assertThat(getResponse.getBody().getPower()).isEqualTo(charger.getPower());
        assertThat(getResponse.getBody().getPrice()).isEqualTo(charger.getPrice());
    }
}
