package example.jwtdemo.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
	private String userName;
	private String email;
	private String password;
	private List<String> roles;
	
}
