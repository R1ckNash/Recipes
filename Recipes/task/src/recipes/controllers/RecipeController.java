package recipes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RecipeDto;
import recipes.services.RecipeService;
import recipes.services.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

  private final RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

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
  public ResponseEntity<RecipeService.Id> setRecipe(
      @RequestBody RecipeDto recipeDto, Authentication authentication) {
    Optional<RecipeService.Id> optionalId = recipeService.putRecipe(recipeDto, authentication);
    return optionalId.isPresent()
        ? ResponseEntity.of(optionalId)
        : ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteRecipe(@PathVariable Long id, Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return ResponseEntity.status(recipeService.deleteRecipeById(id, userDetails)).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateRecipe(
      @RequestBody RecipeDto recipeDto, @PathVariable Long id, Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return ResponseEntity.status(recipeService.updateRecipeById(id, recipeDto, userDetails))
        .build();
  }
}
