package com.watook.model;

import java.io.Serializable;

/**
 * Created by Avinash.Kumar on 27-Oct-17.
 */

public class Preferences implements Serializable {


    /**
     * distanceUnitKm : true
     * distanceRange : 3
     * distanceIn : 3
     * ageMin : 20
     * ageMax : 25
     * femaleInterest : 202
     * maleInterest : 201
     */

    private boolean distanceUnitKm;
    private Integer distanceRange;
    private Integer distanceIn;
    private Integer ageMin;
    private Integer ageMax;
    private boolean femaleInterest;
    private boolean maleInterest;
    private boolean discoverable;

    public boolean isDistanceUnitKm() {
        return distanceUnitKm;
    }

    public void setDistanceUnitKm(boolean distanceUnitKm) {
        this.distanceUnitKm = distanceUnitKm;
    }

    public Integer getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(Integer distanceRange) {
        this.distanceRange = distanceRange;
    }

    public Integer getDistanceIn() {
        return distanceIn;
    }

    public void setDistanceIn(Integer distanceIn) {
        this.distanceIn = distanceIn;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public boolean isFemaleInterest() {
        return femaleInterest;
    }

    public void setFemaleInterest(boolean femaleInterest) {
        this.femaleInterest = femaleInterest;
    }

    public boolean isMaleInterest() {
        return maleInterest;
    }

    public void setMaleInterest(boolean maleInterest) {
        this.maleInterest = maleInterest;
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public void setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
    }
}
