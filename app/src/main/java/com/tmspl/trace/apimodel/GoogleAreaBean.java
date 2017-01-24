package com.tmspl.trace.apimodel;

/**
 * Created by Manan on 20/10/2015.
 */
public class GoogleAreaBean {
    String name,reference,lat_long,state;

    public GoogleAreaBean() {
    }

    public GoogleAreaBean(String name, String reference, String lat_long) {
        this.name = name;
        this.reference = reference;
        this.lat_long = lat_long;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
