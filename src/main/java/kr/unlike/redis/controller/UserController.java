package kr.unlike.redis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.unlike.redis.dto.TokenResponse;
import kr.unlike.redis.dto.UserRequest;
import kr.unlike.redis.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest request) {
		TokenResponse tokenResponse = userService.login(request);
		return new ResponseEntity<TokenResponse>(tokenResponse, HttpStatus.OK);
	}
}
