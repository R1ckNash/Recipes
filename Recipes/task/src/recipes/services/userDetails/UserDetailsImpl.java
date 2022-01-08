package recipes.services.userDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.dao.UserDao;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

  private final String username;
  private final String password;
  private final List<GrantedAuthority> rolesAndAuthorities;

  public UserDetailsImpl(UserDao user) {
    username = user.getUsername();
    password = user.getPassword();
    rolesAndAuthorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return rolesAndAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
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