package edipo.com.cadeirantes;

/**
 * Created by thiagocury on 10/01/2018.
 */

public class Localizacao {

    private String key;
    private double latitude;
    private double longitude;

    public Localizacao() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Localizacao{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
