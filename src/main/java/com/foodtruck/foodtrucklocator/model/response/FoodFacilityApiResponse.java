package com.foodtruck.foodtrucklocator.model.response;

import com.foodtruck.foodtrucklocator.model.Location;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class FoodFacilityApiResponse {
    private String foodFacilityName;
    private String streetAddress;
    private List<String> menu;
    private String operationDaysHours;
    private Double distanceInMiles;
}
