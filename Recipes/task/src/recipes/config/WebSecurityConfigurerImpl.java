package recipes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

  @Autowired
  UserDetailsService userDetailsService;

  public WebSecurityConfigurerImpl(
      @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeRequests()
            .antMatchers("/api/register").not().fullyAuthenticated()
            .antMatchers("/api/role").not().fullyAuthenticated()
            .antMatchers("/actuator/shutdown").not().fullyAuthenticated()
            .antMatchers("/").permitAll()
        .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
            .passwordEncoder(getEncoder());
  }

  @Bean
  public PasswordEncoder getEncoder() {
    return new BCryptPasswordEncoder();
  }
}
