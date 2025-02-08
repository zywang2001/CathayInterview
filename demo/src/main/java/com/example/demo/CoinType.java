package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CoinType")
@Getter
@Setter
public class CoinType {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String code;

    private String symbol;

    private String rate;

    private String description;

    private float rate_float;
}