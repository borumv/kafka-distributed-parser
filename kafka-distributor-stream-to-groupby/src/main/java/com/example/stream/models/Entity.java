package com.example.stream.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Entity {

    private String name;

    @Builder.Default
    private int count = 0;
}
