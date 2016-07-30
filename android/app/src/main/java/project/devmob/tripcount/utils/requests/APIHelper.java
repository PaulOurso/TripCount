package project.devmob.tripcount.utils.requests;

import android.content.Context;

import com.android.volley.Request;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony Wisniewski on 29/07/2016.
 */
public class APIHelper {
    public static final String DOMAIN = "http://0.0.0.0:3000/api/";
    public static final String URL_ACCOUNTS = DOMAIN + "accounts";
    public static final String URL_ACCOUNTS_GROUPS_FROM_ID = DOMAIN + "accounts/%s/groups";

    enum OrderBy { ASC, DESC }

    // filter[where][and][0][title]=My%20Post&filter[where][and][1][content]=Hello
    private static String where(int indexFilter, String urlBase, Map<String, String> hashWhere) {
        String url = urlBase;
        if (hashWhere.size() > 0) {
            if (indexFilter == 0)
                url += "?";
            int i = 0;
            for (Map.Entry<String, String> entry : hashWhere.entrySet()) {
                if (i > 0 || indexFilter > 0)
                    url += "&";
                url += "filter[where]";
                if (hashWhere.size() > 1)
                    url += "[and][" + i + "]";
                url += "[" + entry.getKey() + "]=" + entry.getValue();
                i++;
            }
        }
        return url;
    }

    // filter[include]=reviews&filter[include]=orders
    private static String include(int indexFilter, String urlBase, List<String> listIncludes) {
        String url = urlBase;
        if (listIncludes.size() > 0) {
            if (indexFilter == 0)
                url += "?";
            int i = 0;
            for (String include : listIncludes) {
                if (i > 0 || indexFilter > 0)
                    url += "&";
                url += "filter[include]";
                url += "=" + include;
                i++;
            }
        }
        return url;
    }

    // filter[order][and][0]=mail&filter%20DESC&filter[order][and][1]access_token%20ASC
    private static String orderBy(int indexFilter, String urlBase, Map<String, OrderBy> hashOrder) {
        String url = urlBase;
        if (hashOrder.size() > 0) {
            if (indexFilter == 0)
                url += "?";
            int i = 0;
            for (Map.Entry<String, OrderBy> entry : hashOrder.entrySet()) {
                if (i > 0 || indexFilter > 0)
                    url += "&";
                url += "filter[order]";
                if (hashOrder.size() > 1)
                    url += "[and][" + i + "]";
                url += "=" + entry.getKey() + "%20" + entry.getValue().name();
                i++;
            }
        }
        return url;
    }


    public static void getMyAccount(Context c, String access_token, Type typeAnswer, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, typeAnswer, taskComplete);
        String url = URL_ACCOUNTS;
        Map<String, String> wh = new HashMap<>();
        wh.put("access_token", access_token);
        url = where(0, url, wh);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void getMyGroups(Context c, String idAccount, Type typeAnswer, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, typeAnswer, taskComplete);
        String url = String.format(URL_ACCOUNTS_GROUPS_FROM_ID, idAccount);
        Map<String, OrderBy> order = new HashMap<>();
        order.put("create_date", OrderBy.DESC);
        url = orderBy(0, url, order);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }
}
