package com.foodtruck.foodtrucklocator.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class FoodTruckApiResponse {
    private String foodTruckName;
    private String streetAddress;
    private List<String> menu;
    private String operationDaysHours;
    private Double distanceInMiles;
}
