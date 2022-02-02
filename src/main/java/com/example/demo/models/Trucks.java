package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Trucks {
    private Integer id;
    private String availability;
    private String brand;
    private Double emptyprice;
    private Double fullprice;
    private Double height;
    private Integer idTransporters;
    private Double lat;
    private Double length;
    private Double lng;
    private Integer volume;
    private Integer weight;
    private Double width;
}
