package example.jwtdemo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.jwtdemo.entities.Role;
import example.jwtdemo.entities.User;
import example.jwtdemo.repositories.UserRepository;
import example.jwtdemo.requests.LoginRequest;
import example.jwtdemo.requests.SignUpRequest;
import example.jwtdemo.responses.JwtResponse;
import example.jwtdemo.responses.MessageResponse;
import example.jwtdemo.security.JwtGenerator;
import example.jwtdemo.security.UserDetailsImpl;
import lombok.AllArgsConstructor;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtGenerator jwtGenerator;
	
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
		if(userRepository.existsByUserName(signUpRequest.getUserName())) {
			return new ResponseEntity<>(new MessageResponse("Username is already in use."),HttpStatus.BAD_REQUEST);
			//return "a";
		}
		else if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new MessageResponse("Email is already in use."),HttpStatus.BAD_REQUEST);
			//return "b";
		}
		User user = new User(signUpRequest.getUserName(),signUpRequest.getEmail(),passwordEncoder.encode(signUpRequest.getPassword()));
		List<String> userRolesFromRequest = signUpRequest.getRoles();
		List<Role> roles = new ArrayList<>();
		
		if(userRolesFromRequest == null) {
			roles.add(new Role("ROLE_USER",user));
		}
		else {
			userRolesFromRequest.stream().map(role -> 
				roles.add(new Role(role,user))
			).collect(Collectors.toList());
		}
		user.setRoles(roles);
		userRepository.save(user);
		return new ResponseEntity<>(new MessageResponse("User registered successfully."),HttpStatus.CREATED);
		//return "c";
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
		Authentication authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = "Bearer " + jwtGenerator.generateJwt(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
		        .map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		return new ResponseEntity<>(new JwtResponse(jwt,userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),roles),HttpStatus.OK);
	}
}
