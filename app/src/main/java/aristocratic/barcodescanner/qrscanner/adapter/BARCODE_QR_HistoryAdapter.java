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
import com.google.zxing.client.result.ParsedResultType;

import java.util.ArrayList;

import aristocratic.barcodescanner.qrscanner.model.BARCODE_QR_History;

public class BARCODE_QR_HistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BARCODE_QR_History> histories = new ArrayList<>();
    private Typeface typeface;

    public BARCODE_QR_HistoryAdapter(Context context, ArrayList<BARCODE_QR_History> histories) {
        this.context = context;
        this.histories = histories;
//        this.typeface = Typeface.createFromAsset(context.getAssets(), "RobotoRegular.ttf");
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.barcode_qr_list_history, null);
            setView(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setData(viewHolder, position);
        return convertView;
    }

    class ViewHolder {
        TextView txt_content, txt_type;
        ImageView img_type;
    }

    private void setView(View convertView, ViewHolder viewHolder) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(layoutParams);
        viewHolder.img_type = convertView.findViewById(R.id.img_type);
        viewHolder.txt_content = convertView.findViewById(R.id.txt_content);
        viewHolder.txt_type = convertView.findViewById(R.id.txt_type);
//        viewHolder.txt_type.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoMedium.ttf"));
        viewHolder.txt_content.setTypeface(typeface);
    }

    private void setData(ViewHolder viewHolder, int position) {
        BARCODE_QR_History history = histories.get(position);
        if (history.getType().equals(ParsedResultType.ADDRESSBOOK.name())) {
            viewHolder.img_type.setImageResource(R.drawable.contact_130);
        } else if (history.getType().equals(ParsedResultType.URI.name())) {
            viewHolder.img_type.setImageResource(R.drawable.weblink_130);
        } else if (history.getType().equals(ParsedResultType.SMS.name())) {
            viewHolder.img_type.setImageResource(R.drawable.sms_130);
        } else {
            viewHolder.img_type.setImageResource(R.drawable.text_130);
        }
        viewHolder.txt_type.setText(history.getType());
        viewHolder.txt_content.setText(history.getContent());
    }
}
