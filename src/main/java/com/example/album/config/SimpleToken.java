package com.example.album.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleToken implements AuthenticationToken {

    private String userName;
    private String phone;
    private String email;
    private String password;
    private TokenType type;

    @Override
    public String getPrincipal() {
        switch (type){
            case NAME:
                return userName;
            case PHONE:
                return phone;
            case EMAIL:
                return email;
                default:
                    return null;
        }
    }

    @Override
    public char[] getCredentials() {
        return password.toCharArray();
    }
}
