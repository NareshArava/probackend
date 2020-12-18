package com.corona.corona.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corona.corona.model.User;
import com.corona.corona.service.RegistrationService;
import com.corona.corona.service.SecurityTokenGenerator;

@RestController
public class RegistrationController {
	
	@Autowired
	private RegistrationService service;
	
	@Autowired
	private SecurityTokenGenerator tokenGenerator;
	
	@GetMapping("/")
	public String welcome() {
		return "accessed";
		
	}
	
	
	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4002")
	public User registerUser(@RequestBody User user) throws Exception {
		String tempEmailId = user.getEmailId();
		if(tempEmailId != null && !"".equals(tempEmailId)) {
			User userobj = service.fetchUserByEmailId(tempEmailId);
			if (userobj != null){
				throw new Exception("user with "+tempEmailId+" is already exist");
			}
		}
		
		User userObj = null;
		userObj = service.saveUser(user);
		return userObj;
	}
	
	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:4002")
	public ResponseEntity<?>loginUser(@RequestBody User user) throws Exception {
		String tempEmailId  = user.getEmailId();
		String tempPass = user.getPassword();
 		User userObj = null;
		if (tempEmailId != null && tempPass != null) {
			
			userObj = service .fetchUserByEmailIdAndPassword(tempEmailId, tempPass);
			
			
		}
		
		if(userObj == null) {
			throw new Exception("bad Credentials");
		}else {
		//System.out.println(user);
		Map<String,String> map=tokenGenerator.generateToken(user);
		map.put("email",user.getEmailId());
		map.put("id",String.valueOf(user.getId()));
		return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);
		//return userObj;
	}
		
	}
	

}
