package com.example.album.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private String perId;
    private String perName;
    private String perDesc;
    private String available;
}
