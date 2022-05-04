package com.foodtruck.foodtrucklocator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * locationid,Applicant,FacilityType,cnn,LocationDescription,
 * Address,blocklot,block,lot,permit,Status,FoodItems,X,Y,Latitude,Longitude,
 * Schedule,dayshours,NOISent,Approved,Received,PriorPermit,ExpirationDate,Location,
 * Fire Prevention Districts,Police Districts,Supervisor Districts,Zip Codes,Neighborhoods (old)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodFacility {
    private String foodFacilityName;
    private String facilityType;
    private List<String> menu;
    private Location location;
    private String operationDaysHours;

}
