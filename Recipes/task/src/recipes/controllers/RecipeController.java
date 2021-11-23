package recipes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDto;
import recipes.services.RecipeService;

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
