package west2test;

public class CityWea {
    private String id;
    private String name;
    private String lat;
    private String lon;
    private String fxDate;
    private String Max;
    private String Min;
    private String Day;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setFxDate(String fxDate) {
        this.fxDate = fxDate;
    }

    public void setMax(String max) {
        Max = max;
    }

    public void setMin(String min) {
        Min = min;
    }

    public void setDay(String day) {
        Day = day;
    }

    @Override
    public String toString() {
        return  "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", fxDate='" + fxDate + '\'' +
                ", Max='" + Max + '\'' +
                ", Min='" + Min + '\'' +
                ", Day='" + Day + '\'' +
                '}';
    }
}
