/*
 * This is the source code of Telegram for Android v. 2.x
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2015.
 */

package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.telegram.android.AndroidUtilities;
import org.telegram.android.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SimpleTextView;

public class AddMemberCell extends FrameLayout {

    private SimpleTextView textView;
    private ImageView imageView;

    public AddMemberCell(Context context) {
        super(context);

        //ImageView imageView = new ImageView(context);
        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.addmember);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        addView(imageView, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, LocaleController.isRTL ? 0 : 68, 8, LocaleController.isRTL ? 68 : 0, 0));

        textView = new SimpleTextView(context);
        textView.setTextColor(0xff212121);
        textView.setTextSize(17);
        textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP);
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 20, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, LocaleController.isRTL ? 28 : 129, 22.5f, LocaleController.isRTL ? 129 : 28, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64), MeasureSpec.EXACTLY));
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setDrawableColor(int color) {
        Drawable d = getResources().getDrawable(R.drawable.addmember);
        d.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        imageView.setImageDrawable(d);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
