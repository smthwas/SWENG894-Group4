package com.apptime.auth.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apptime.auth.model.ClientUser;
import com.apptime.auth.model.Roles;
import com.apptime.auth.model.Users;
import com.apptime.auth.repository.UserRepository;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	@PostMapping("/signup")
	public ResponseEntity<ClientUser> signup(@RequestBody Users user) {
		
		String pwd = user.getPassword();
		if(user.getRoles()== null) {
			Roles r = new Roles();
			r.setRole("USER");
			Set<Roles> rSet = new HashSet<Roles>();
			rSet.add(r);
			user.setRoles(rSet);
		}
		String encryptPwd = passwordEncoder.encode(pwd);
		user.setPassword(encryptPwd);
		userRepository.save(user);
		return new ResponseEntity<ClientUser>(new ClientUser(user.getUsername(),user.getEmail()), HttpStatus.OK);
		
	}
	//https://stackoverflow.com/questions/3102819/disable-same-origin-policy-in-chrome
	//@PreAuthorize("hasAnyRole('ADMIN')")
	//@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/dashboard")
	public  ResponseEntity<ClientUser> login(Principal p) {	
		Users user = userRepository.findByUsername(p.getName());
		
		//return new ResponseEntity<ClientUser>(new ClientUser(user.getUsername(),user.getEmail()), HttpStatus.OK);
		
		return new ResponseEntity<ClientUser>(new ClientUser(user.getUsername(),user.getEmail()), HttpStatus.OK);

	}
	

}
