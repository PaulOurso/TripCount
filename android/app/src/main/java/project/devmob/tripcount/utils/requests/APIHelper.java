package project.devmob.tripcount.utils.requests;

import android.content.Context;

import com.android.volley.Request;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.devmob.tripcount.models.Account;
import project.devmob.tripcount.models.Group;

/**
 * Created by Tony Wisniewski on 29/07/2016.
 */
public class APIHelper {
    public static final String DOMAIN = "http://192.168.1.14:3000/api";
    public static final String URL_ACCOUNTS = DOMAIN + "/accounts";
    public static final String URL_ACCOUNTS_GROUPS_FROM_ID = DOMAIN + "/accounts/%s/groups";
    public static final String URL_GROUP_FIND_ONE = DOMAIN + "/groups";
    public static final String URL_JOIN_GROUP = DOMAIN + "/accounts/%1$s/groups/rel/%2$s";

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

    public static void getMyAccounts(Context c, String access_token, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, Account.typeListOf(), taskComplete);
        String url = URL_ACCOUNTS;
        Map<String, String> wh = new HashMap<>();
        wh.put("access_token", access_token);
        url = where(0, url, wh);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void createAccount(Context c, Account account, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, Account.typeObjectOf(), taskComplete);
        String url = URL_ACCOUNTS;
        apiRequest.addParam("mail", account.mail);
        apiRequest.addParam("access_token", account.access_token);
        apiRequest.setMethod(Request.Method.POST);
        apiRequest.execute(url);
    }

    public static void getMyGroups(Context c, Account account, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, Group.typeListOf(), taskComplete);
        String url = String.format(URL_ACCOUNTS_GROUPS_FROM_ID, account.id);
        Map<String, OrderBy> order = new HashMap<>();
        order.put("create_date", OrderBy.DESC);
        url = orderBy(0, url, order);
        List<String> includes = new ArrayList<>();
        includes.add("spendings");
        url = include(1, url, includes);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void createGroup(Context c, Account account, Group group, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, Group.typeObjectOf(), taskComplete);
        String url = String.format(URL_ACCOUNTS_GROUPS_FROM_ID, account.id);
        apiRequest.setMethod(Request.Method.POST);
        apiRequest.addParam("name", group.name);
        apiRequest.addParam("token", group.token);
        apiRequest.addParam("create_date", group.create_date);
        apiRequest.execute(url);
    }

    public static void findGroupWithToken(Context c, String token, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, Group.typeListOf(), taskComplete);
        Map<String, String> where = new HashMap<>();
        where.put("token", token);
        String url = where(0, URL_GROUP_FIND_ONE, where);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void joinGroup(Context c, Account account, Group group, TaskComplete<Type> taskComplete) {
        APIRequest<Type> apiRequest = new APIRequest<>(c, null, taskComplete);
        String url = String.format(URL_JOIN_GROUP, account.id, group.id);
        apiRequest.setMethod(Request.Method.PUT);
        apiRequest.execute(url);
    }
}
