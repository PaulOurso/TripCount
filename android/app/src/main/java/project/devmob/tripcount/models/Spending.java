package project.devmob.tripcount.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 26/07/2016.
 */
public class Spending implements Serializable {
    public String id;
    public String name;
    public double price;
    public String create_date;
    public double latitude;
    public double longitude;
    public Person purchaser;
    public List<Person> indebted;

    public static Type typeObjectOf() {
        return new TypeToken<Spending>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Spending>>() {
        }.getType();
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
