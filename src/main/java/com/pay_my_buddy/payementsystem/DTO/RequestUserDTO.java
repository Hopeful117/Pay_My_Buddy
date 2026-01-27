package com.pay_my_buddy.payementsystem.DTO;

import lombok.Getter;

public class RequestUserDTO {
    @Getter
    private int id;
    @Getter
    private String username;
    @Getter
    private String email;
    @Getter
    private String password;
    public RequestUserDTO(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
