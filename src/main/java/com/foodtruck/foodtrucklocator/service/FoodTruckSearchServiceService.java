package com.foodtruck.foodtrucklocator.service;

import com.foodtruck.foodtrucklocator.model.FoodTruck;
import com.foodtruck.foodtrucklocator.model.response.FoodTruckApiResponse;
import com.foodtruck.foodtrucklocator.util.DataStore;
import com.foodtruck.foodtrucklocator.util.DistanceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodTruckSearchServiceService implements IFoodTruckSearchService {

    PriorityQueue<FoodTruckApiResponse> foodTruckApiResponsesSortedByDistance =
            new PriorityQueue<>((first, second) -> Double.compare(second.getDistanceInMiles(), first.getDistanceInMiles()));

    private int searchLimit;

    @Override
    public List<FoodTruckApiResponse> getFoodTruckWithinRadius(double latitude, double longitude, int radius, int limit) {
        log.info("Searching for Food Trucks. Latitude: {} Longitude: {} Radius: {} SearchLimit: {}",
                latitude, longitude, radius, limit);
        this.searchLimit = limit;
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
                .forEach(this::addToFoodTruckApiResponsesSortedByDistance);

        return getUpToLimitResponse(limit);


    }

    private void addToFoodTruckApiResponsesSortedByDistance(FoodTruckApiResponse foodTruckApiResponse) {
        if (searchLimit > 0) {
            foodTruckApiResponsesSortedByDistance.add(foodTruckApiResponse);
            searchLimit--;
        } else {
            if (foodTruckApiResponse.getDistanceInMiles() <= foodTruckApiResponsesSortedByDistance.peek().getDistanceInMiles()) {
                foodTruckApiResponsesSortedByDistance.poll();
                foodTruckApiResponsesSortedByDistance.offer(foodTruckApiResponse);
            }
        }
    }


    private List<FoodTruckApiResponse> getUpToLimitResponse(int limit) {
        LinkedList<FoodTruckApiResponse> result = new LinkedList<>();
        while (foodTruckApiResponsesSortedByDistance.size() > limit) {
            foodTruckApiResponsesSortedByDistance.poll();
        }
        while (!foodTruckApiResponsesSortedByDistance.isEmpty() && limit > 0) {
            result.addFirst(foodTruckApiResponsesSortedByDistance.poll());
            limit--;
        }
        log.info("Returning search result. Size = {}", result.size());
        return result;
    }


    private FoodTruckApiResponse mapToApiResponse(FoodTruck foodTruck) {

        log.debug("DTO conversion. FoodTruck object -> FoodTruckApiResponse object");
        return FoodTruckApiResponse.builder()
                .foodTruckName(foodTruck.getFoodTruckName())
                .menu(foodTruck.getMenu())
                .streetAddress(foodTruck.getLocation().getStreetAddress())
                .operationDaysHours(!ObjectUtils.isEmpty(foodTruck.getOperationDaysHours()) ?
                        foodTruck.getOperationDaysHours() : "NOT_AVAILABLE")
                .build();
    }

    @Override
    public List<FoodTruckApiResponse> getFoodTruckByName(String name) {

        if (ObjectUtils.isEmpty(name)) {
            log.error("Invalid search name provided");
            return null;
        }
        log.info("Searching for Food Truck that matches {}", name);
        return DataStore.getFOOD_TRUCK_LIST().parallelStream().
                filter(foodTruck -> foodTruck.getFoodTruckName().toLowerCase().contains(name.toLowerCase()))
                .map(this::mapToApiResponse)
                .limit(3)
                .collect(Collectors.toList());
    }
}
