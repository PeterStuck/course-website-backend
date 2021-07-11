package peterstuck.coursewebsitebackend.services.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import peterstuck.coursewebsitebackend.models.user.User;

public interface UserService extends UserDetailsService {

    void register(User user);

    String update(String token, User user) throws UsernameNotFoundException;

    User getUserInfo(String token) throws UsernameNotFoundException;

}
