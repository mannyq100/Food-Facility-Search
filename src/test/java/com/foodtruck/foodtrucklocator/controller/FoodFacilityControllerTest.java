package com.foodtruck.foodtrucklocator.controller;

import com.foodtruck.foodtrucklocator.model.response.FoodFacilityApiResponse;
import com.foodtruck.foodtrucklocator.service.IFoodFacilitySearchService;
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


@WebMvcTest(controllers = FoodFacilityController.class)
class FoodFacilityControllerTest {
    @Autowired
    MockMvc mockMvc;


    @MockBean
    IFoodFacilitySearchService foodFacilitySearchSearch;

    @Test
    void Should_Get_FoodFacility_By_Name() throws Exception {

        var testFoodApiResponse = FoodFacilityApiResponse.builder()
                .foodFacilityName("Casita Vegana")
                .menu(Arrays.asList("testMenuItem1", "testMenuItem2"))
                .streetAddress("testStreetAddress").build();
        var testFoodFacilityName = "Casita Vegana";

        Mockito.when(foodFacilitySearchSearch.getFoodFacilitiesByName(testFoodFacilityName))
                .thenReturn(testFoodApiResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/food-facility/name")
                        .param("Food Facility Name", testFoodFacilityName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodFacilityName").value(testFoodFacilityName));

    }
}