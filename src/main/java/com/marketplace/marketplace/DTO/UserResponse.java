package com.marketplace.marketplace.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String sub;
    private String email;
    private String phoneNumber;
    private Date createdAt;
    private Date updatedAt;
}
