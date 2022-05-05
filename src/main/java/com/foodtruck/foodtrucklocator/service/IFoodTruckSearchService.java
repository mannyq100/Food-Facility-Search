package com.foodtruck.foodtrucklocator.service;

import com.foodtruck.foodtrucklocator.model.response.FoodTruckApiResponse;

import java.util.List;

public interface IFoodTruckSearchService {

    List<FoodTruckApiResponse> getFoodTruckWithinRadius(double latitude, double longitude, int radius, int limit);

    List<FoodTruckApiResponse> getFoodTruckByName(String name);
}
