package com.example.album.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Perm {
    private String permId;
    private String permName;
    private String permDesc;
    private String available;
}
