package com.foodtruck.foodtrucklocator.service;

import com.foodtruck.foodtrucklocator.model.FoodTruck;
import com.foodtruck.foodtrucklocator.model.response.FoodTruckApiResponse;
import com.foodtruck.foodtrucklocator.util.DataStore;
import com.foodtruck.foodtrucklocator.util.DistanceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Service
@Slf4j
public class FoodTruckSearchServiceService implements IFoodTruckSearchService {

    PriorityQueue<FoodTruckApiResponse> foodTruckApiResponsesSortedByDistance =
            new PriorityQueue<>((first, second) -> Double.compare(first.getDistanceInMiles(), second.getDistanceInMiles()));

    @Override
    public List<FoodTruckApiResponse> getFoodTruckWithinRadius(double latitude, double longitude, int radius, int limit) {
        log.info("Searching for Food Trucks. Latitude: {} Longitude: {} Radius: {} SearchLimit: {}",
                latitude, longitude, radius, limit);
        DataStore.getFOOD_TRUCK_LIST().parallelStream()
                .filter(foodTruck -> foodTruck.getFacilityType().equalsIgnoreCase("truck"))
                .map(foodTruck -> {
                    FoodTruckApiResponse foodTruckApiResponse = mapToApiResponse(foodTruck);
                    double distance = DistanceCalculator.getDistance(foodTruck.getLocation().getLatitude(),
                            foodTruck.getLocation().getLongitude(),
                            latitude, longitude, "M");
                    foodTruckApiResponse.setDistanceInMiles(distance);
                    return foodTruckApiResponse;

                })
                .filter(foodTruckApiResponseWithDistance -> foodTruckApiResponseWithDistance.getDistanceInMiles() <= radius)
                .forEach(foodTruckApiResponseWithDistance -> foodTruckApiResponsesSortedByDistance.add(foodTruckApiResponseWithDistance));

        return getUpToLimitResponse(limit);


    }


    private List<FoodTruckApiResponse> getUpToLimitResponse(int limit) {
        List<FoodTruckApiResponse> result = new ArrayList<>();
        while (!foodTruckApiResponsesSortedByDistance.isEmpty() && limit > 0) {
            result.add(foodTruckApiResponsesSortedByDistance.poll());
            limit--;
        }
        log.info("Returning search result. Size = {}", result.size());
        return result;
    }


    private FoodTruckApiResponse mapToApiResponse(FoodTruck foodTruck) {

        log.info("DTO conversion. FoodTruck object -> FoodTruckApiResponse object");
        return FoodTruckApiResponse.builder()
                .foodTruckName(foodTruck.getFoodTruckName())
                .menu(foodTruck.getMenu())
                .streetAddress(foodTruck.getLocation().getStreetAddress())
                .operationDaysHours(!ObjectUtils.isEmpty(foodTruck.getOperationDaysHours()) ?
                        foodTruck.getOperationDaysHours() : "NOT_AVAILABLE")
                .build();
    }

    @Override
    public FoodTruckApiResponse getFoodTruckByName(String name) {

        if (ObjectUtils.isEmpty(name)) {
            log.error("Invalid search name provided");
            return null;
        }
        log.info("Searching for Food Truck that matches {}", name);
        return DataStore.getFOOD_TRUCK_LIST().parallelStream().
                filter(foodTruck -> foodTruck.getFoodTruckName().equalsIgnoreCase(name))
                .map(this::mapToApiResponse)
                .findFirst().orElseGet(() -> null);
    }
}
