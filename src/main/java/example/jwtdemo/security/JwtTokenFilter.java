package example.jwtdemo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if(jwt != null && jwtGenerator.validateJwtToken(jwt)) {
				System.out.println("a");
				String userName = jwtGenerator.getUserNameFromToken(jwt);
				UserDetails user = userDetailsServiceImpl.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		filterChain.doFilter(request, response);
	}
	
	private String parseJwt(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if(Strings.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}
}
