package project.devmob.tripcount.models;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by Jean-Noel on 26/07/2016.
 */
public class Spending {
    public String id;
    public String name;
    public long price;
    public Date create_date;
    public Debt indebted;
    public LatLng localistation;

    public static Type typeObjectOf() {
        return new TypeToken<Spending>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Spending>>() {
        }.getType();
    }
}
