package pull_up.global.Oauth.v2;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pull_up.global.Oauth.CustomUserDetails;

@Slf4j
public class CustomUserDetailService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("try to load users by username! : {}", username);
    return null;
  }
}
