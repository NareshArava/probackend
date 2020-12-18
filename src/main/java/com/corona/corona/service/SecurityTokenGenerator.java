package com.corona.corona.service;

import java.util.Map;

import com.corona.corona.model.User;



public interface SecurityTokenGenerator {
	
	Map<String,String> generateToken(User user);
}
