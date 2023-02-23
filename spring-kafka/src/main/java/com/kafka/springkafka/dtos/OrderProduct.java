package com.kafka.springkafka.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OrderProduct {
    
    public String name;

    public String category;

    public String code;

    public int quantity;

    public double price;
}
