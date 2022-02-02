package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Requests {
    private String dateStart;
    private String dateStartMax;
    private String dateStop;
    private String dateStopMax;
    private Double destination_lat;
    private Double destination_lng;
    private Integer id;
    private Integer id_sender;
    private Double money;
    private Double source_lat;
    private Double source_lng;
    private String typeGoods;
    private Integer volume;
    private Integer weight;
}
