package com.foodtruck.foodtrucklocator;

import com.foodtruck.foodtrucklocator.util.DataStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodTruckLocatorApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(FoodTruckLocatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DataStore.loadData();
    }


}
