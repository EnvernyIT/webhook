package com.enverny.nomadiswebhook.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enverny.nomadiswebhook.services.ApiCallService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	private ApiCallService apiCallService;
	
	@PutMapping("/add-upd/employee/")
	public void updateEmployee(@RequestBody Object object) throws JsonProcessingException, IOException{
		apiCallService.postOrPutHttpMethod(object);
	}
	
}
