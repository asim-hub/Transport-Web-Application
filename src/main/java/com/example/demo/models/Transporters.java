package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transporters {
    private Integer id;
    private Integer id_user;
    private Integer nrFreeTrucks;
    private Integer nrTotalTrucks;
}
