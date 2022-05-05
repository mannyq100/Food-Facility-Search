package com.foodtruck.foodtrucklocator.util;

import com.foodtruck.foodtrucklocator.model.FoodTruck;
import com.foodtruck.foodtrucklocator.model.Location;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@UtilityClass
public class DataStore {

    private static final List<FoodTruck> FOOD_TRUCK_LIST = new ArrayList<>();

    public static void addFoodTruck(FoodTruck foodTruck) {
        FOOD_TRUCK_LIST.add(foodTruck);
    }

    public static List<FoodTruck> getFOOD_TRUCK_LIST() {
        return FOOD_TRUCK_LIST;
    }

    public void loadData() throws IOException {
        final String foodFacilityFilePath = ResourceUtils.getFile("classpath:data/MobileFoodFacilityPermit.csv").getPath();

        log.info("Loading Csv data into memory from {} On Application Start", foodFacilityFilePath);
        Files.lines(Path.of(foodFacilityFilePath))
                .skip(1L)
                .map(line -> line.split(","))
                .filter(DataStore::hasRequiredFields)
                .map(fields -> {


                    Location location = Location.builder()
                            .locationId(Long.parseLong(fields[0]))
                            .streetAddress(fields[5])
                            .latitude(Double.parseDouble(fields[14]))
                            .longitude(Double.parseDouble(fields[15]))
                            .build();

                    List<String> foodItems = Arrays.asList(fields[11].split(":"));

                    return FoodTruck.builder()
                            .foodTruckName(fields[1])
                            .facilityType(fields[2])
                            .menu(foodItems)
                            .location(location)
                            .operationDaysHours(fields[17])
                            .build();
                }).forEach(DataStore::addFoodTruck);
        log.info("Finished loading dataset");
    }

    private boolean hasRequiredFields(String[] fields) {
        return !(ObjectUtils.isEmpty(fields[1]) || ObjectUtils.isEmpty(fields[2]) || ObjectUtils.isEmpty(fields[14])
                || ObjectUtils.isEmpty(fields[15]));
    }

}
