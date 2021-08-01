package kr.unlike.redis.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.unlike.redis.aop.isAuthenticated;
import kr.unlike.redis.domain.User;
import kr.unlike.redis.dto.GeoRequest;
import kr.unlike.redis.dto.Result;
import kr.unlike.redis.service.TrackerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class TrackerController {

	private final TrackerService trackerService;

	@isAuthenticated
	@PostMapping
	public ResponseEntity<?> save(User user, @RequestBody GeoRequest request) {
		trackerService.save(request, user);
		return new ResponseEntity<>(new Result().ok(), HttpStatus.OK);
	}

	@isAuthenticated
	@DeleteMapping
	public ResponseEntity<?> delete(User user) {
		trackerService.delete(user);
		return new ResponseEntity<>(new Result().ok(), HttpStatus.OK);
	}

	@isAuthenticated
	@GetMapping
	public ResponseEntity<?> getList(GeoRequest request, User user) {
		List<User> users = trackerService.getGeoUserList(request, user);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
}
