package com.example.album.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String roleId;
    private String roleName;
    private String roleDesc;
    private String available;
}











