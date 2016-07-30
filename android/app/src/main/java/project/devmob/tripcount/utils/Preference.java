package project.devmob.tripcount.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Tony Wisniewski on 29/29/2016.
 */
public class Preference {

    public static final String KEY_PHONE = "KEY_PHONE";
    public static final String KEY_INFO_COMMERCANT = "KEY_INFO_COMMERCANT";

    private static SharedPreferences get(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    private static String getPref(Context c, String key) {
        return get(c).getString(key, null);
    }

    private static void setPref(Context c, String key, String value) {
        get(c).edit().putString(key, value).apply();
    }

    /*public static Commercant getCommercant(Context c) {
        String commer = getPref(c, Preference.KEY_INFO_COMMERCANT);
        Commercant commercant = null;
        if (commer != null) {
            Gson gson = new Gson();
            commercant = gson.fromJson(commer, Commercant.class);
        }
        return commercant;
    }

    public static void setCommercant(Context c, Commercant commercant) {
        if (commercant != null)
            setPref(c, KEY_INFO_COMMERCANT, new Gson().toJson(commercant));
        else
            setPref(c, KEY_INFO_COMMERCANT, null);
    }*/
}
