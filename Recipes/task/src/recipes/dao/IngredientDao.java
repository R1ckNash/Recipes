package recipes.dao;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column @NotNull private String ingredient;

  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private RecipeDao recipeDao;
}
