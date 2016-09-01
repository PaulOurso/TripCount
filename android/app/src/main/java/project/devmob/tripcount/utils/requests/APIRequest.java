package project.devmob.tripcount.utils.requests;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import project.devmob.tripcount.utils.FastDialog;

/**
 * Created by Tony Wisniewski on 29/07/2016.
 */
public class APIRequest<TypeResult> {

    private static final String TAG = "APIRequest";
    private Context context;
    private HashMap<String, String> params;
    private boolean displayProgressDialog;
    private Dialog dialog;
    private Type resultClass;
    private TaskComplete<TypeResult> taskComplete;
    private int method;
    private final static String[] METHOD_LOG = new String[] {"GET", "POST", "PUT", "DELETE"};

    public APIRequest(Context c, Type resClass, TaskComplete<TypeResult> taskCpl) {
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

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setShowDialog(boolean dialog) {
        displayProgressDialog = dialog;
    }

    public void setMethod(int meth) {
        method = meth;
    }

    public void execute(final String url) {
        if (method == -99999) {
            Log.e("TripCount APIRequest", "Method request missing ! (url: "+url+")");
            return;
        }
        if (!Network.isOnline(context)) {
            Toast.makeText(context, R.string.not_connect, Toast.LENGTH_SHORT).show();
            return;
        }
        showLog(url);
        RequestQueue queue = Volley.newRequestQueue(context);
        showDialog();
        StringRequest stringRequest = new StringRequest(
                method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Log.d(TAG+" RESPONSE ("+METHOD_LOG[method]+")", "(url: "+url+") -> "+response);
                        TypeResult result = null;
                        if (resultClass != null) {
                            result = gson.fromJson(response, resultClass);
                        }
                        if (taskComplete != null) {
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
                        FastDialog.showDialog(context, FastDialog.SIMPLE_DIALOG, context.getString(R.string.error_http)+": "+error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return APIRequest.this.getParams();
            }
        };
        queue.add(stringRequest);
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
            dialog = null;
        }
    }

    private void showLog(String url) {
        String log = url + " params: {";
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (i>0)
                log +=", ";
            log += entry.getKey()+": "+entry.getValue();
            i++;
        }
        log += "}";
        Log.d(TAG+" REQUEST ("+METHOD_LOG[method]+")", log);
    }
}
