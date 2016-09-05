package project.devmob.tripcount.models;

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
    public Barcode.GeoPoint position;

    public static Type typeObjectOf() {
        return new TypeToken<Spending>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Spending>>() {
        }.getType();
    }
}
