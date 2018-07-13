package listAdapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Util.Constants;
import ath.password_minimizer.R;
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
        RelativeLayout backgroundLayout = view.findViewById(R.id.background_color_layout);

        Uri imageUri = Uri.parse(mPicturePasswordItems.get(position).getImageUri());
        passwordImageView.setImageURI(imageUri);

        passwordNameView.setText( mPicturePasswordItems.get(position).getPasswordName());

        switch(mPicturePasswordItems.get(position).getPasswordStrength()) {
            case SIMPLE:
                backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_simple_pw));
                break;
            case MIDDLE:
                backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_middle_pw));
                break;
            case STRONG:
                backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_strong_pw));
                break;
            default:
                backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_black_transparent));
                break;
        }

        return view;
    }
}
