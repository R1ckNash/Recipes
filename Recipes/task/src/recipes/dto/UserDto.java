package recipes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  @JsonProperty("email")
  @Pattern(regexp = ".+@.+\\..+")
  private String username;

  @NotBlank
  @Min(8)
  private String password;
}
