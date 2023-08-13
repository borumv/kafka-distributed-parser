package com.example.kafkadistributor.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Entity {

    private String name;

    private int count;
}
