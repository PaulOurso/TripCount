package project.devmob.tripcount.ui.groupe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Noel on 28/07/2016.
 */
public class AdapterGroup extends ArrayAdapter<Group> {

    int resId;
    LayoutInflater layoutInflater;

    public AdapterGroup(Context context, int resource, List<Group> objects) {
        super(context, resource, objects);

        resId = resource;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }
}
