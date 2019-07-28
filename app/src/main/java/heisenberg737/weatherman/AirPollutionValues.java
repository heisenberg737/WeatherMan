package heisenberg737.weatherman;

public class AirPollutionValues {
    String precision,pressure,value;
   public AirPollutionValues(String precision,String pressure,String value)
   {
       this.precision=precision;
       this.pressure=pressure;
       this.value=value;
   }

    public String getPrecision() {
        return precision;
    }

    public String getPressure() {
        return pressure;
    }

    public String getValue() {
        return value;
    }
}
