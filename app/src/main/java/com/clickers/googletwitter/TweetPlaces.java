package com.clickers.googletwitter;

/**
 * Created by Pinal Patel on 29/09/2021.
 * LuxshTech
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TweetPlaces {

    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("includes")
    @Expose
    public Includes includes;
    @SerializedName("matching_rules")
    @Expose
    public List<MatchingRule> matchingRules = null;

    static class MatchingRule {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("tag")
        @Expose
        public String tag;

    }

    public static class Place {

        @SerializedName("full_name")
        @Expose
        public String fullName;
        @SerializedName("geo")
        @Expose
        public Geo__1 geo;
        @SerializedName("id")
        @Expose
        public String id;

    }

    public static class Includes {

        @SerializedName("places")
        @Expose
        public List<Place> places = null;


    }

    public static class Geo__1 {

        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("bbox")
        @Expose
        public List<Double> bbox = null;


    }

    static class Geo {

        @SerializedName("place_id")
        @Expose
        public String placeId;


    }

    public static class Data {

        @SerializedName("geo")
        @Expose
        public Geo geo;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("text")
        @Expose
        public String text;

    }
}



