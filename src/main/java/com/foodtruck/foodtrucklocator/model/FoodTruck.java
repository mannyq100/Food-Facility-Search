package com.foodtruck.foodtrucklocator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodTruck {
    private String foodTruckName;
    private String facilityType;
    private List<String> menu;
    private Location location;
    private String operationDaysHours;

}
