package example.jwtdemo.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import example.jwtdemo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails{
	
	private Long id;
	private String username;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	 
	
	public static UserDetailsImpl build(User user) {
		 List<GrantedAuthority> authorities = user.getRoles().stream()
			        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
			        .collect(Collectors.toList());
		return new UserDetailsImpl(user.getId(),user.getUserName(),user.getEmail(),user.getPassword(),authorities);
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
