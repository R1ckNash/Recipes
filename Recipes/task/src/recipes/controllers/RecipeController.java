package recipes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RecipeDto;
import recipes.services.RecipeService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/recipe")
public class RecipeController {

  private final RecipeService recipeService;

  @GetMapping("/{id}")
  public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id) {
    return ResponseEntity.of(recipeService.findRecipeById(id));
  }

  @GetMapping("/search")
  public List<RecipeDto> searchRecipe(
      @RequestParam(defaultValue = "") String category,
      @RequestParam(defaultValue = "") String name) {

    if ((name.equals("")) == (category.equals("")))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    if (!name.equals("")) return recipeService.searchByName(name);
    return recipeService.searchByCategory(category);
  }

  @PostMapping("/new")
  public ResponseEntity<RecipeService.Id> setRecipe(@RequestBody RecipeDto recipeDto) {
    Optional<RecipeService.Id> optionalId = recipeService.putRecipe(recipeDto);
    return optionalId.isPresent()
        ? ResponseEntity.of(optionalId)
        : ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
    boolean existed = recipeService.deleteRecipeById(id);
    return existed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateRecipe(@RequestBody RecipeDto recipeDto, @PathVariable Long id) {
    return recipeService.updateRecipeById(id, recipeDto);
  }
}
