package com.pay_my_buddy.payementsystem.DTO;

import com.pay_my_buddy.payementsystem.model.User;
import lombok.Getter;

import java.util.List;

public class ResponseUserDTO {
    @Getter
    private int id;
    @Getter
    private String username;
    @Getter
    private String email;
    @Getter
    private List<User> connections;
    public ResponseUserDTO(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.connections=null;
    }

}
