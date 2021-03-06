package peterstuck.coursewebsitebackend.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import peterstuck.coursewebsitebackend.resources.auth.UserCredentials;
import peterstuck.coursewebsitebackend.services.user.UserService;
import peterstuck.coursewebsitebackend.utils.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String authenticateUser(UserCredentials credentials) throws UsernameNotFoundException, BadCredentialsException {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword())
        );
        final UserDetails user = userService.loadUserByUsername(credentials.getEmail());

        return jwtUtil.generateToken(user);
    }

}
