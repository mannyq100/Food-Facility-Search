package com.foodtruck.foodtrucklocator.controller;

import com.foodtruck.foodtrucklocator.model.response.FoodTruckApiResponse;
import com.foodtruck.foodtrucklocator.service.IFoodTruckSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FoodTruckController.class)
class FoodTruckControllerTest {
    @Autowired
    MockMvc mockMvc;


    @MockBean
    IFoodTruckSearchService foodFacilitySearchSearch;

    @Test
    void Should_Get_FoodTruck_By_Name() throws Exception {

        var testFoodApiResponse = FoodTruckApiResponse.builder()
                .foodTruckName("Casita Vegana")
                .menu(Arrays.asList("testMenuItem1", "testMenuItem2"))
                .streetAddress("testStreetAddress").build();
        var testFoodFacilityName = "Casita Vegana";

        Mockito.when(foodFacilitySearchSearch.getFoodTruckByName(testFoodFacilityName))
                .thenReturn(testFoodApiResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/food-truck/name")
                        .param("FoodTruckName", testFoodFacilityName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodTruckName").value(testFoodFacilityName));

    }
}