package com.example.bajajtask;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InitialResponseDTO {
    private String webhook;
    private String accessToken;    
}
