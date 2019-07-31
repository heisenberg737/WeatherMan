package heisenberg737.weatherman;

public class FiveDayForecastClass {
    String temperature,pressure,humidity,description,windspeed,datetime;
    public FiveDayForecastClass(String temperature, String pressure, String humidity, String description, String windspeed, String datetime)
    {
        this.description=description;
        this.temperature=temperature;
        this.pressure=pressure;
        this.humidity=humidity;
        this.windspeed=windspeed;
        this.datetime=datetime;

    }

    public String getTemperature() {
        return temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public String getDatetime() {
        return datetime;
    }

}

