package example.jwtdemo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public content";
	}
	
	@GetMapping("/user")
	public String userAccess() {
		return "user content";
	}
	
	@GetMapping("/mod")
	public String modAccess() {
		return "mod content";
	}
	
	@GetMapping("/admin")
	public String adminAccess() {
		return "admin content";
	}
	
	
}
