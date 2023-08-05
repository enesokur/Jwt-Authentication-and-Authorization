package example.jwtdemo.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
	
	private JwtEntryPoint jwtEntryPoint;
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); 
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtTokenFilter authenticationJwtTokenFilter() {
		return new JwtTokenFilter();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth ->
			auth.requestMatchers("/auth/**","/error","/test/all").permitAll()
			.requestMatchers("/test/user").hasAnyRole("USER","MODERATOR","ADMIN")
			.requestMatchers("/test/mod").hasRole("MODERATOR")
			.requestMatchers("/test/admin").hasRole("ADMIN")
			.anyRequest().authenticated()
		);
		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
