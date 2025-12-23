package org.example;

import org.springframework.stereotype.Component;

@Component
public class AddressServiceImpl implements AddressService {

    private String city;
    private String street;

    public AddressServiceImpl() {
    }

    public AddressServiceImpl(String city, String street) {
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getAddress() {
        return city + ", " + street;
    }

    @Override
    public String toString() {
        return "Address [city=" + city + ", street=" + street + "]";
    }
}
