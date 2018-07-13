package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class EndlessRelativeLayout extends RelativeLayout
{
    public EndlessRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(0, 0);
    }
}
