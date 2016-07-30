package project.devmob.tripcount.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import project.devmob.tripcount.R;

/**
 * Created by Tony Wisniewski on 29/07/2016.
 */
public class NetworkVolley<TypeResult> extends Volley {

    private Context context;
    private final HashMap<String, String> params;
    private boolean displayProgressDialog;
    private Dialog dialog;
    private Type resultClass;
    private TaskComplete<TypeResult> taskComplete;
    private int method;

    public NetworkVolley(Context c, Type resClass, TaskComplete<TypeResult> taskCpl) {
        context = c;
        params = new HashMap<>();
        displayProgressDialog = true;
        dialog = null;
        resultClass = resClass;
        taskComplete = taskCpl;
        method = -99999;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    @Override
    public HashMap<String, String> getParams() {
        return params;
    }

    public void setShowDialog(boolean dialog) {
        displayProgressDialog = dialog;
    }

    public void setMethod(int meth) {
        method = meth;
    }

    public void execute(String url) {
        if (method == -99999) {
            Log.e("TripCount NetworkVolley", "Method not specified !");
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        showDialog();
        StringRequest stringRequest = new StringRequest(
                method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        TypeResult result = gson.fromJson(response, resultClass);
                        if (result != null && taskComplete != null) {
                            taskComplete.result = result;
                            taskComplete.run();
                        }
                        dismissDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                        FastDialog.showDialog(context, FastDialog.SIMPLE_DIALOG, R.string.error_http);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        queue.add(stringRequest);
        queue.start();
    }



    private void showDialog() {
        if (displayProgressDialog) {
            dialog = FastDialog.showProgressDialog(context, R.string.loading);
            dialog.show();
        }
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
