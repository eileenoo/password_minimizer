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
import model.NavItem;
import model.PicturePassword;

public class PasswordListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<PicturePassword> mPicturePasswordItems;

    public PasswordListAdapter(ArrayList<PicturePassword> picturePasswordItems, Context context) {
        mPicturePasswordItems = picturePasswordItems;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPicturePasswordItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicturePasswordItems.get(position);
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
            view = inflater.inflate(R.layout.password_list_item, null);
        }
        else {
            view = convertView;
        }

        ImageView passwordImageView = (ImageView) view.findViewById(R.id.password_image);
        TextView passwordNameView = (TextView) view.findViewById(R.id.password_name);

        //TODO: set resource with real image path here
        //passwordImageView.setImageResource( mPicturePasswordItems.get(position).imagePath);
        passwordNameView.setText( mPicturePasswordItems.get(position).getPasswordName());

        return view;
    }
}
