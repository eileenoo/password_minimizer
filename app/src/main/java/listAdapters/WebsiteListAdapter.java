package listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ath.password_minimizer.R;
import model.PicturePassword;
import model.WebsiteCredentials;

public class WebsiteListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<WebsiteCredentials> mWebsiteCredentialsItems;

    public WebsiteListAdapter(ArrayList<WebsiteCredentials> websiteCredentialsItems, Context context) {
        mWebsiteCredentialsItems = websiteCredentialsItems;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mWebsiteCredentialsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mWebsiteCredentialsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.website_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView websiteNameView = (TextView) view.findViewById(R.id.website_name);
        websiteNameView.setText( mWebsiteCredentialsItems.get(position).mName );

        return view;
    }
}
