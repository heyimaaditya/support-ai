package com.supportai.auth.dto;

import com.supportai.auth.domain.User;
import com.supportai.auth.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private String tenantId;

    public static UserDTO fromUser(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .tenantId(user.getTenantId())
                .build();
    }
}