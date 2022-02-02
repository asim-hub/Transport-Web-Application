package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Jobs {
    // Request
    static int counter = 1;
    private Integer id;
    private String dateStart;
    private String dateStartMax;
    private String dateStop;
    private String dateStopMax;
    private Double destination_lat;
    private Double destination_lng;
    private Integer request_id;
    private Integer id_sender;
    private Double money;
    private Double source_lat;
    private Double source_lng;
    private String typeGoods;
    private Integer volume;
    private Integer weight;
    //
    private Double distance;
    private Double length;
    private Double profit;
    //
    private Integer truck_id;
    private String truck_availability;
    private String truck_brand;
    private Double truck_emptyprice;
    private Double truck_fullprice;
    private Double truck_height;
    private Integer truck_idTransporters;
    private Double truck_lat;
    private Double truck_length;
    private Double truck_lng;
    private Integer truck_volume;
    private Integer truck_weight;
    private Double truck_width;

    public Jobs(Requests request, Trucks truck) {
        this.id = counter++;
        this.dateStart = request.getDateStart();
        this.dateStartMax = request.getDateStartMax();
        this.dateStop = request.getDateStop();
        this.dateStopMax = request.getDateStopMax();
        this.destination_lat = request.getDestination_lat();
        this.destination_lng = request.getDestination_lng();
        this.request_id = request.getId();
        this.id_sender = request.getId_sender();
        this.money = request.getMoney();
        this.source_lat = request.getSource_lat();
        this.source_lng = request.getSource_lng();
        this.typeGoods = request.getTypeGoods();
        this.volume = request.getVolume();
        this.weight = request.getWeight();
        //
        this.distance = calculateDistance(truck.getLat(), truck.getLng(), this.source_lat, this.source_lng);
        this.length = calculateDistance(this.destination_lat, this.destination_lng, this.source_lat, this.source_lng);
        this.profit = calculateProfit(request, truck);
        //
        this.truck_id = truck.getId();
        this.truck_availability = truck.getAvailability();
        this.truck_brand = truck.getBrand();
        this.truck_emptyprice = truck.getEmptyprice();
        this.truck_fullprice = truck.getFullprice();
        this.truck_height = truck.getHeight();
        this.truck_idTransporters = truck.getIdTransporters();
        this.truck_lat = truck.getLat();
        this.truck_lng = truck.getLng();
        this.truck_length = truck.getLength();
        this.truck_volume = truck.getVolume();
        this.truck_weight = truck.getWeight();
        this.truck_width = truck.getWidth();
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371.01; // kilometers
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        double distance = earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
        return Math.round(distance * 100.00) / 100.00;
    }

    public static double calculateProfit(Requests request, Trucks truck) {
        double profit = request.getMoney() - (calculateDistance(truck.getLat(), truck.getLng(), request.getSource_lat(), request.getSource_lng()) * truck.getEmptyprice()
                + calculateDistance(request.getSource_lat(), request.getSource_lng(), request.getDestination_lat(), request.getDestination_lng()) * truck.getFullprice());
        return Math.round(profit * 100.00) / 100.00;
    }
}
