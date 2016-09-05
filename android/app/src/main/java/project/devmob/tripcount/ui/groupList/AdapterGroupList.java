package project.devmob.tripcount.ui.grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;

/**
 * Created by Jean-Noel on 28/07/2016.
 */
public class AdapterGroupList extends ArrayAdapter<Group> {

    int resId;
    LayoutInflater layoutInflater;

    public AdapterGroupList(Context context, int resource, List<Group> objects) {
        super(context, resource, objects);
        resId = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        TextView hGroupName;
        //TextView hGroupTotal;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(resId, null);
            vHolder = new ViewHolder();
            vHolder.hGroupName = (TextView) convertView.findViewById(R.id.item_groupe_name);
            //vHolder.hGroupTotal = (TextView) convertView.findViewById(R.id.item_groupe_total);
            convertView.setTag(vHolder);
        }
        else
            vHolder = (ViewHolder) convertView.getTag();
        Group group = getItem(position);
        vHolder.hGroupName.setText(group.name);
        //vHolder.hGroupTotal.setText(String.format(getContext().getString(R.string.currency), group.getTotalSpendings()));

        return convertView;
    }
}
