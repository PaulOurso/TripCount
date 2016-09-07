package project.devmob.tripcount.ui.group.spending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.ui.group.fragment.SpendingFragment;

/**
 * Created by Jean-Noel on 03/09/2016.
 */
public class AdapterSpendingList extends ArrayAdapter<Spending> {

    int resId;
    LayoutInflater layoutInflater;

    public AdapterSpendingList(Context context, int resource, List<Spending> objects) {
        super(context, resource, objects);
        resId = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        TextView hSpendingName;
        TextView hSpendingPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(resId, null);
            vHolder = new ViewHolder();
            vHolder.hSpendingName = (TextView) convertView.findViewById(R.id.item_spending_name);
            vHolder.hSpendingPrice = (TextView) convertView.findViewById(R.id.item_spending_price);
            convertView.setTag(vHolder);
        }
        else
            vHolder = (ViewHolder) convertView.getTag();
        Spending spending = getItem(position);
        vHolder.hSpendingName.setText(spending.name);
        vHolder.hSpendingPrice.setText(String.format(getContext().getString(R.string.currency), spending.price));

        return convertView;
    }
}
