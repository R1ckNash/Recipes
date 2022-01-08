package recipes.utils;

import org.mapstruct.Mapper;
import recipes.dao.UserDao;
import recipes.dto.UserDto;

@Mapper
public interface UserMapper {

    UserDao mapDtoToDao(UserDto userDto);

    UserDto mapDaoToDto(UserDao userDao);

}
