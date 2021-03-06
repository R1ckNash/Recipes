package recipes.dao;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "category")
  private String category;

  @Column(name = "date")
  private LocalDateTime date;

  @Column(name = "description")
  private String description;

  @Column
  @NotNull
  @OneToMany(
      mappedBy = "recipeDao",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<IngredientDao> ingredients;

  @Column
  @NotNull
  @OneToMany(
      mappedBy = "recipeDao",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<DirectionDao> directions;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private UserDao author;
}
