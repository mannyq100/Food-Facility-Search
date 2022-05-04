package com.foodtruck.foodtrucklocator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    private Long locationId;
    private String streetAddress;
    private Double latitude;
    private Double longitude;
}
