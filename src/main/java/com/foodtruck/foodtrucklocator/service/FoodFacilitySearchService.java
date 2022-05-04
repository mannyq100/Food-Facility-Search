package com.foodtruck.foodtrucklocator.service;

import com.foodtruck.foodtrucklocator.model.FoodFacility;
import com.foodtruck.foodtrucklocator.model.response.FoodFacilityApiResponse;
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
public class FoodFacilitySearchService implements IFoodFacilitySearchSearch {

    PriorityQueue<FoodFacilityApiResponse> foodFacilityApiResponsesSortedByDistance =
            new PriorityQueue<>((first, second) -> Double.compare(first.getDistanceInMiles(), second.getDistanceInMiles()));

    @Override
    public List<FoodFacilityApiResponse> getFoodFacilitiesWithinRadius(double latitude, double longitude, int radius, int limit) {
        log.info("Search for Food Facilities. Latitude: {} Longitude: {} Radius: {} SearchLimit: {}",
                latitude, longitude, radius, limit);
        DataStore.getFoodFacilityList().parallelStream()
                .filter(foodFacility -> foodFacility.getFacilityType().equalsIgnoreCase("truck"))
                .map(fc -> {
                    FoodFacilityApiResponse foodFacilityApiResponse = mapToApiResponse(fc);
                    double distance = DistanceCalculator.getDistance(fc.getLocation().getLatitude(),
                            fc.getLocation().getLongitude(),
                            latitude, longitude, "M");
                    foodFacilityApiResponse.setDistanceInMiles(distance);
                    return foodFacilityApiResponse;

                })
                .filter(foodFacilityApiResponse -> foodFacilityApiResponse.getDistanceInMiles() <= radius)
                .forEach(e -> foodFacilityApiResponsesSortedByDistance.add(e));

        return getUpToLimitResponse(limit);


    }


    private List<FoodFacilityApiResponse> getUpToLimitResponse(int limit) {
        List<FoodFacilityApiResponse> result = new ArrayList<>();
        while (!foodFacilityApiResponsesSortedByDistance.isEmpty() && limit > 0) {
            result.add(foodFacilityApiResponsesSortedByDistance.poll());
            limit--;
        }
        log.info("Returning search result. Size = {}", result.size());
        return result;
    }


    private FoodFacilityApiResponse mapToApiResponse(FoodFacility foodFacility) {

        log.info("DTO conversion. FoodFacility object -> FoodFacilityApiResponse object");
        return FoodFacilityApiResponse.builder()
                .foodFacilityName(foodFacility.getFoodFacilityName())
                .menu(foodFacility.getMenu())
                .streetAddress(foodFacility.getLocation().getStreetAddress())
                .operationDaysHours(!ObjectUtils.isEmpty(foodFacility.getOperationDaysHours()) ?
                        foodFacility.getOperationDaysHours() : "NOT_AVAILABLE")
                .build();
    }

    @Override
    public FoodFacilityApiResponse getFoodFacilitiesByName(String name) {

        if (ObjectUtils.isEmpty(name)) {
            log.error("Invalid search name provided");
            return null;
        }
        log.info("Searching for Food Facility that matches ", name);
        return DataStore.getFoodFacilityList().parallelStream().
                filter(fc -> fc.getFoodFacilityName().equalsIgnoreCase(name))
                .map(this::mapToApiResponse)
                .findFirst().orElseGet(() -> null);
    }
}
