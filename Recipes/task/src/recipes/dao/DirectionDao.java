package recipes.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "direction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  @Column private String direction;

  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private RecipeDao recipeDao;
}
