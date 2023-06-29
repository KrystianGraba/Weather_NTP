package com.example.pogoda;
// This is a class that is used to store weather history entries

public class ObjectWeatherHistory
{
    Double temperture;
    Double windSpeed;
    Double pressure;
    Double humidity;
    Integer hour;

    public Double getTemperture() {
        return temperture;
    }

    public void setTemperture(Double temperture) {
        this.temperture = temperture;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public ObjectWeatherHistory(Double temperture, Double windSpeed, Double pressure, Double humidity, Integer hour) {
        this.temperture = temperture;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
        this.hour = hour;
    }
    public ObjectWeatherHistory(Double temperture, Integer hour) {
        this.temperture = temperture;
        this.hour = hour;

    }

}
