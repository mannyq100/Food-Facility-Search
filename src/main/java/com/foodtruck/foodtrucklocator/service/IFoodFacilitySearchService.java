package com.foodtruck.foodtrucklocator.service;

import com.foodtruck.foodtrucklocator.model.response.FoodFacilityApiResponse;

import java.util.List;

public interface IFoodFacilitySearchService {

    List<FoodFacilityApiResponse> getFoodFacilitiesWithinRadius(double latitude, double longitude, int radius, int limit);

    FoodFacilityApiResponse getFoodFacilitiesByName(String name);
}
