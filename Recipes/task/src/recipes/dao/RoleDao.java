package recipes.dao;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class RoleDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column
  @NotNull
  @OneToMany(
      mappedBy = "role",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<UserDao> users;
}
