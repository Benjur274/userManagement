package com.testcompany.usermanagement.mapper;

import com.testcompany.usermanagement.model.dto.CreateUserDto;
import com.testcompany.usermanagement.model.dto.UpdateUserDto;
import com.testcompany.usermanagement.model.entity.User;
import com.testcompany.usermanagement.model.dto.UserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserDto createUserDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDto updateUserDTO, @MappingTarget User user);

    UserDto toUserResponseDTO(User user);
}
