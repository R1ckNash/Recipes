package recipes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDto;
import recipes.services.RecipeService;

import java.util.HashMap;
import java.util.Map;
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
    return Optional.ofNullable(optionalId)
        .map(ResponseEntity::of)
        .orElse(ResponseEntity.badRequest().build());
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<Map<String, String>> handler(RuntimeException exception) {
    Map<String, String> exceptionMap = new HashMap<>();
    exceptionMap.put("error", exception.getMessage());
    ResponseEntity.BodyBuilder bodyBuilder;
    if (exception instanceof IllegalArgumentException) {
      bodyBuilder = ResponseEntity.status(HttpStatus.NOT_FOUND);
    } else {
      bodyBuilder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
    }
    return bodyBuilder.body(exceptionMap);
  }
}
