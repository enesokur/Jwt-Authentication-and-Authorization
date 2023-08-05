package example.jwtdemo.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {
	
	@Value("${demo.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${demo.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String generateJwt(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.claim("userName", userPrincipal.getUsername())
				.claim("roles", combineAuthorities(userPrincipal.getAuthorities()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(key(),SignatureAlgorithm.HS256).compact();
	}
	
	public String combineAuthorities(Collection<? extends GrantedAuthority> authorities) {
		List<String> authoritiesToBeCombined = new ArrayList<>();
		for(GrantedAuthority authority : authorities) {
			authoritiesToBeCombined.add(authority.getAuthority());
		}
		return String.join(",", authoritiesToBeCombined);
	}
	
	private Key key() {
	    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
			return true;
		}
		catch (MalformedJwtException e) {
			return false;
		}
		catch(ExpiredJwtException e) {
			return false;
		}
		catch(UnsupportedJwtException e) {
			return false;
		}
		catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
