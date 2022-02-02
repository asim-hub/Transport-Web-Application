package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    private Double destination_lat;
    private Double destination_lng;
    private Double distance_free;
    private Double distance_full;
    private Integer id;
    private Integer id_request;
    private Integer id_sender;
    private Integer id_transporter;
    private Integer id_truck;
    private String payTerm;
    private Double source_lat;
    private Double source_lng;
    private Double totalCost;
    private String typeGoods;
    private Integer volume;
    private Integer weight;
}
