package com.kafka.springkafka.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserregistrationDto {

    private String name;

    private String lastName;

    private String email;

    private String password;

    private String country;
}
