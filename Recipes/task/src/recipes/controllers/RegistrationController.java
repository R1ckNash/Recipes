package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.dao.RoleDao;
import recipes.dto.UserDto;
import recipes.repository.RoleRepository;
import recipes.services.RegistrationService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class RegistrationController {

  private RegistrationService registrationService;
  private RoleRepository roleRepository;

  @Autowired
  public RegistrationController(RegistrationService registrationService, RoleRepository roleRepository) {
    this.registrationService = registrationService;
    this.roleRepository = roleRepository;
  }

  @PostConstruct
  public void createRole() {
    if (roleRepository.findByName("ROLE_USER").isEmpty()) {
      RoleDao roleDao = new RoleDao();
      roleDao.setName("ROLE_USER");
      roleDao.setId(1L);
      roleRepository.save(roleDao);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserDto user) {
    return ResponseEntity.status(registrationService.registerUser(user)).build();
  }
}
