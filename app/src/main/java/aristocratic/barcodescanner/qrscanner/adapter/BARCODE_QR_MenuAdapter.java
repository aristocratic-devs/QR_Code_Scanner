package aristocratic.barcodescanner.qrscanner.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import aristocratic.barcodescanner.qrscanner.R;

public class BARCODE_QR_MenuAdapter extends BaseAdapter {

    private Context context;
    //    private int[] resources = {R.drawable.scan, R.drawable.history, R.drawable.settings, R.drawable.share, R.drawable.rate, R.drawable.pp};
    private int[] resources = {R.drawable.ic_round_qr_code_scanner_24, R.drawable.ic_round_history_24, R.drawable.ic_round_settings_24, R.drawable.ic_round_share_24, R.drawable.ic_round_star_rate_24, R.drawable.ic_round_policy_24};
    private String[] name;

    public BARCODE_QR_MenuAdapter(Context context) {
        this.context = context;
        name = context.getResources().getStringArray(R.array.menu);
    }

    @Override
    public int getCount() {
        return resources.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.barcode_qr_list_menu, null);
            setView(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setData(viewHolder, position);
        return convertView;
    }

    class ViewHolder {
        TextView txt_name;
        ImageView img_icon;
    }

    private void setView(View convertView, ViewHolder viewHolder) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(layoutParams);
        viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
        viewHolder.img_icon = convertView.findViewById(R.id.img_icon);
//        viewHolder.txt_name.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoRegular.ttf"));
    }

    private void setData(ViewHolder viewHolder, int position) {
        viewHolder.txt_name.setText(name[position]);
        viewHolder.img_icon.setImageResource(resources[position]);
    }
}
