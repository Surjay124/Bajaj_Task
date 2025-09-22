package com.example.bajajtask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitialRequestDTO {
    private String name;
    private String regNo;
    private String email;
}
