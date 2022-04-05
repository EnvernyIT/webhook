package com.enverny.nomadiswebhook.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ApiCallService {
	
	public Object postOrPutHttpMethod(Object object) throws JsonProcessingException, IOException;

	public Object addObjectHttpMethod(Object object) throws JsonProcessingException, IOException;
	
	public Object updateObjectHttpMethod(Object object) throws JsonProcessingException, IOException;
	
	public String refreshTokenByUsername(String username, String password) throws JsonProcessingException, IOException;

	public String getObjects() throws JsonProcessingException, IOException;

}
