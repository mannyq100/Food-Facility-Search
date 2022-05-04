package com.foodtruck.foodtrucklocator.controller;

import com.foodtruck.foodtrucklocator.model.response.FoodFacilityApiResponse;
import com.foodtruck.foodtrucklocator.service.IFoodFacilitySearchService;
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
@RequestMapping("/api/v1/food-facility")
@RequiredArgsConstructor
public class FoodFacilityController {

    private final IFoodFacilitySearchService foodFacilitySearchSearch;
    Logger logger = LoggerFactory.getLogger(FoodFacilityController.class);

    @GetMapping()
    public ResponseEntity<List<FoodFacilityApiResponse>> getFoodFacilitiesNearby(@RequestParam(name = "Latitude") double lat,
                                                                                 @RequestParam(name = "Longitude") double log,
                                                                                 @RequestParam(defaultValue = "5") int radius,
                                                                                 @RequestParam(defaultValue = "5") int searchLimit) {
        logger.info("Request received to search Food Facilities within {} radius", radius);
        List<FoodFacilityApiResponse> foodFacilitiesWithinRadius =
                foodFacilitySearchSearch.getFoodFacilitiesWithinRadius(lat, log, radius, searchLimit);

        return ResponseEntity.ok(foodFacilitiesWithinRadius);
    }

    @GetMapping("/name")
    public ResponseEntity<Object> getFoodFacilityByName(@RequestParam(name = "Food Facility Name") String name) {
        logger.info("Request received to search Food Facility by name: {}", name);
        FoodFacilityApiResponse foodFacilitiesByName = foodFacilitySearchSearch.getFoodFacilitiesByName(name);
        if (foodFacilitiesByName == null) {
            logger.error("No Food Facility found by search term {} ", name);
            return new ResponseEntity<>("No Food Facility match for provided name", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(foodFacilitiesByName);
    }

}
