package recipes.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import recipes.dao.RoleDao;
import recipes.dao.UserDao;
import recipes.dto.UserDto;
import recipes.repository.RoleRepository;
import recipes.repository.UserRepository;
import recipes.utils.UserMapper;

import javax.annotation.PostConstruct;
import javax.management.relation.Role;
import javax.validation.Valid;

@Service
@Validated
@Data
@NoArgsConstructor
public class RegistrationService {

  private UserRepository userRepository;
  private PasswordEncoder encoder;
  private RoleRepository roleRepository;

  @Autowired
  public RegistrationService(
      UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.roleRepository = roleRepository;
  }

  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

  public HttpStatus registerUser(UserDto user) {
    UserDao userDao = mapper.mapDtoToDao(user);
    if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
      return HttpStatus.BAD_REQUEST;
    }
    userDao.setPassword(encoder.encode(user.getPassword()));
    userDao.setRole(roleRepository.findByName("ROLE_USER").orElseThrow());

    userRepository.save(userDao);
    return HttpStatus.OK;
  }
}
