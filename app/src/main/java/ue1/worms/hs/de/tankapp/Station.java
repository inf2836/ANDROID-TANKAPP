package ue1.worms.hs.de.tankapp;

import android.support.annotation.NonNull;

import java.util.Comparator;

import static java.lang.Double.*;

public class Station {
    String brand ;
    String street;
    String place;
    String dist;
    String dPreis;
    String e5Preis;
    String e10Preis;
    String houseNumber;
    String lat ;
    String lng;
    String isOpen;

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }


    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getE5Preis() {
        return e5Preis;
    }

    public String getE10Preis() {
        return e10Preis;
    }

    public void setE5Preis(String e5Preis) {
        this.e5Preis = e5Preis;
    }

    public void setE10Preis(String e10Preis) {
        this.e10Preis = e10Preis;
    }

    public Station(String brand , String street , String place , String dist, String dPreis, String e5Preis, String e10Preis, String houseNumber){
        this.brand=brand;
        this.street=street;
        this.place= place;
        this.dist=dist;
        this.dPreis=dPreis;
        this.e5Preis=e5Preis;
        this.e10Preis=e10Preis;
        this.houseNumber = houseNumber;
    }

    public Station(){}

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getdPreis() {
        return dPreis;
    }

    public void setdPreis(String dPreis) {
        this.dPreis = dPreis;
    }

    public String getHouseNumber(){
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber){
        this.houseNumber = houseNumber;
    }


    public static Comparator<Station> stationComparator = new Comparator<Station>() {
        @Override
        public int compare(Station o1, Station o2) {
            double preis1 = parseDouble( o1.getdPreis() );
            double preis2 = parseDouble( o2.getdPreis() );
            if (preis1 < preis2){
                return -1;
            }
            if (preis1 > preis2){
                return 1;
            }
            return -1;
        }
    };
}
