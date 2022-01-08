package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.repository.UserRepository;

@Service("userDetailsServiceImpl")
class UserDetailsServiceIml implements UserDetailsService {

  UserRepository userRepository;

  @Autowired
  public UserDetailsServiceIml(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findUserByUsername(username)
        .map(recipes.services.userDetails.UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
  }
}
