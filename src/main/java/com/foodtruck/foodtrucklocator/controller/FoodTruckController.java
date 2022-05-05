package com.foodtruck.foodtrucklocator.controller;

import com.foodtruck.foodtrucklocator.model.response.FoodTruckApiResponse;
import com.foodtruck.foodtrucklocator.service.IFoodTruckSearchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food-truck")
@RequiredArgsConstructor
public class FoodTruckController {

    private final IFoodTruckSearchService foodTruckSearchService;
    Logger logger = LoggerFactory.getLogger(FoodTruckController.class);

    @GetMapping()
    public ResponseEntity<List<FoodTruckApiResponse>> getFoodTrucksNearby(@RequestParam(name = "Latitude") double lat,
                                                                          @RequestParam(name = "Longitude") double log,
                                                                          @RequestParam(defaultValue = "5") int radius,
                                                                          @RequestParam(defaultValue = "5") int searchLimit) {
        logger.info("Request received to search Food Trucks within {} radius", radius);
        List<FoodTruckApiResponse> foodTrucksWithinRadius =
                foodTruckSearchService.getFoodTruckWithinRadius(lat, log, radius, searchLimit);

        return ResponseEntity.ok(foodTrucksWithinRadius);
    }

    @GetMapping("/name")
    public ResponseEntity<Object> getFoodTruckByName(@RequestParam(name = "FoodTruckName") String name) {
        logger.info("Request received to search Food Truck by name: {}", name);
        FoodTruckApiResponse foodTruckByName = foodTruckSearchService.getFoodTruckByName(name);
        if (foodTruckByName == null) {
            logger.error("No Food Truck found for search term {} ", name);
            return new ResponseEntity<>("No Food Truck match for provided name", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(foodTruckByName);
    }

}
