package recipes.dao;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private RoleDao role;

  @Column
  @NotNull
  @OneToMany(
      mappedBy = "author",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<RecipeDao> recipes;
}
