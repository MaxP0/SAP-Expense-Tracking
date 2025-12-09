package org.maks.expensosap.mapper;

import org.maks.expensosap.dto.UserDTO;
import org.maks.expensosap.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
