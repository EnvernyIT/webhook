package com.enverny.nomadiswebhook.services.impl;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.enverny.nomadiswebhook.add.ReportingRestParameter;
import com.enverny.nomadiswebhook.add.RestApi;
import com.enverny.nomadiswebhook.services.ApiCallService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiCallServiceImpl implements ApiCallService {

	private static final Logger logger = Logger.getLogger(ApiCallServiceImpl.class);

	private MultiValueMap<String, Object> initializeMap() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add(ReportingRestParameter.REPORT_LOGIC_API, RestApi.getReportLogicAPI());
		return map;
	}

	@Override
	public Object postOrPutHttpMethod(Object object) throws JsonProcessingException, IOException {
		String filePath = "C:\\Workspaces\\Bludots\\Nomadis-webhook\\src\\main\\resources\\access_data.properties";
		try (InputStream input = new FileInputStream(filePath)) {
			Properties prop = new Properties();
			prop.load(input);

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = initializeMap();
			
			String accessToken = refreshTokenByUsername(prop.getProperty("username"), prop.getProperty("password"));

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + accessToken); // accessToken can be the secret key you generate.
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<Object> entity = new HttpEntity<>(object, headers);

			ResponseEntity<Object> responseEntity = restTemplate.exchange(prop.getProperty("url-employee"),
					HttpMethod.POST, entity, Object.class);
			
			if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.hasBody()) {
				if (responseEntity.getBody().toString().contains("Profile already exists")) {
					return updateObjectHttpMethod(object);
				}
			}

			return responseEntity.getBody();
			
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException
				| IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Object addObjectHttpMethod(Object object) throws JsonProcessingException, IOException {
		String filePath = "C:\\Workspaces\\Bludots\\Nomadis-webhook\\src\\main\\resources\\access_data.properties";
		try (InputStream input = new FileInputStream(filePath)) {
			Properties prop = new Properties();
			prop.load(input);

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = initializeMap();

			String accessToken = refreshTokenByUsername(prop.getProperty("username"), prop.getProperty("password"));

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + accessToken); // accessToken can be the secret key you generate.
			headers.setContentType(MediaType.APPLICATION_JSON);

			/*
			 * List<Object> dtos = new ArrayList<>(); dtos.add(object);
			 * 
			 * ObjectMapper mapper = new ObjectMapper(); String dtoAsString =
			 * mapper.writeValueAsString(dtos); System.out.println(dtoAsString);
			 */
			HttpEntity<Object> entity = new HttpEntity<>(object, headers);

			ResponseEntity<Object> responseEntity = restTemplate.exchange(prop.getProperty("url-employee"),
					HttpMethod.POST, entity, Object.class);

			return responseEntity.getBody();
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException
				| IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Object updateObjectHttpMethod(Object object) throws JsonProcessingException, IOException {
		String filePath = "C:\\Workspaces\\Bludots\\Nomadis-webhook\\src\\main\\resources\\access_data.properties";
		try (InputStream input = new FileInputStream(filePath)) {
			Properties prop = new Properties();
			prop.load(input);

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = initializeMap();

			Object accessToken = refreshTokenByUsername(prop.getProperty("username"), prop.getProperty("password"));

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + accessToken); // accessToken can be the secret key you generate.

			HttpEntity<Object> entity = new HttpEntity<Object>(object, headers);

			ResponseEntity<Object> responseEntity = restTemplate.exchange(prop.getProperty("url-employee"),
					HttpMethod.PUT, entity, Object.class);
			return responseEntity.getBody();
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException
				| IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public String refreshTokenByUsername(String username, String password) throws JsonProcessingException, IOException {
		String filePath = "C:\\Workspaces\\Bludots\\Nomadis-webhook\\src\\main\\resources\\access_data.properties";
		try (InputStream input = new FileInputStream(filePath)) {
			Properties prop = new Properties();

			prop.load(input);

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
			headers.add("Accept", MediaType.APPLICATION_JSON.toString()); // Optional in case server sends back JSON
																			// data

			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
			requestBody.add("username", prop.getProperty("username"));
			requestBody.add("password", prop.getProperty("password"));
			requestBody.add("grant_type", "password");

			HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(prop.getProperty("url-token"),
					HttpMethod.POST, formEntity, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
			String newToken = jsonNode.get("access_token").textValue();
			System.err.println(newToken);

			return newToken;
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException
				| IllegalArgumentException | IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getObjects() throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
