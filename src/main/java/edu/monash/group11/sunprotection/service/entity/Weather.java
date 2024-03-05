package edu.monash.group11.sunprotection.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Weather {

    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lon")
    private Double lon;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("timezone_offset")
    private Integer timezoneOffset;
    @JsonProperty("current")
    private CurrentDTO current;
    @JsonProperty("hourly")
    private List<HourlyDTO> hourly;

    @NoArgsConstructor
    @Data
    public static class CurrentDTO {
        @JsonProperty("dt")
        private Integer dt;
        @JsonProperty("sunrise")
        private Integer sunrise;
        @JsonProperty("sunset")
        private Integer sunset;
        @JsonProperty("temp")
        private Double temp;
        @JsonProperty("feels_like")
        private Double feelsLike;
        @JsonProperty("pressure")
        private Integer pressure;
        @JsonProperty("humidity")
        private Integer humidity;
        @JsonProperty("dew_point")
        private Double dewPoint;
        @JsonProperty("uvi")
        private Double uvi;
        @JsonProperty("clouds")
        private Integer clouds;
        @JsonProperty("visibility")
        private Integer visibility;
        @JsonProperty("wind_speed")
        private Double windSpeed;
        @JsonProperty("wind_deg")
        private Integer windDeg;
        @JsonProperty("wind_gust")
        private Double windGust;
        @JsonProperty("weather")
        private List<WeatherDTO> weather;

        @NoArgsConstructor
        @Data
        public static class WeatherDTO {
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("main")
            private String main;
            @JsonProperty("description")
            private String description;
            @JsonProperty("icon")
            private String icon;
        }
    }

    @NoArgsConstructor
    @Data
    public static class HourlyDTO {
        @JsonProperty("dt")
        private Integer dt;
        @JsonProperty("temp")
        private Double temp;
        @JsonProperty("feels_like")
        private Double feelsLike;
        @JsonProperty("pressure")
        private Integer pressure;
        @JsonProperty("humidity")
        private Integer humidity;
        @JsonProperty("dew_point")
        private Double dewPoint;
        @JsonProperty("uvi")
        private Double uvi;
        @JsonProperty("clouds")
        private Integer clouds;
        @JsonProperty("visibility")
        private Integer visibility;
        @JsonProperty("wind_speed")
        private Double windSpeed;
        @JsonProperty("wind_deg")
        private Integer windDeg;
        @JsonProperty("wind_gust")
        private Double windGust;
        @JsonProperty("weather")
        private List<WeatherDTO> weather;
        @JsonProperty("pop")
        private Double pop;

        @NoArgsConstructor
        @Data
        public static class WeatherDTO {
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("main")
            private String main;
            @JsonProperty("description")
            private String description;
            @JsonProperty("icon")
            private String icon;
        }
    }
}
