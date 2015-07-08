/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.android.AndroidUtilities;
import org.telegram.android.LocaleController;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.TLObject;
import org.telegram.messenger.TLRPC;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.android.MessagesController;
import org.telegram.android.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.LayoutHelper;

public class ChangeNameActivity extends BaseFragment {

    private EditText firstNameField;
    private EditText lastNameField;
    private View headerLabelView;
    private View doneButton;

    private final static int done_button = 1;

    @Override
    public View createView(Context context, LayoutInflater inflater) {
            actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            actionBar.setAllowOverlayTitle(true);
            actionBar.setTitle(LocaleController.getString("EditName", R.string.EditName));
            actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                @Override
                public void onItemClick(int id) {
                    if (id == -1) {
                        finishFragment();
                    } else if (id == done_button) {
                        if (firstNameField.getText().length() != 0) {
                            saveName();
                            finishFragment();
                        }
                    }
                }
            });

            ActionBarMenu menu = actionBar.createMenu();
            //doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

            SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
            Drawable done = getParentActivity().getResources().getDrawable(R.drawable.ic_done);
            done.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.SRC_IN);
            doneButton = menu.addItemWithWidth(done_button, done, AndroidUtilities.dp(56));

            TLRPC.User user = MessagesController.getInstance().getUser(UserConfig.getClientUserId());
            if (user == null) {
                user = UserConfig.getCurrentUser();
            }

        LinearLayout linearLayout = new LinearLayout(context);
        fragmentView = linearLayout;
            fragmentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout) fragmentView).setOrientation(LinearLayout.VERTICAL);
            fragmentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

        firstNameField = new EditText(context);
            firstNameField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            firstNameField.setHintTextColor(0xff979797);
            firstNameField.setTextColor(0xff212121);
            firstNameField.getBackground().setColorFilter(AndroidUtilities.getIntColor("themeColor"), PorterDuff.Mode.SRC_IN);
            firstNameField.setMaxLines(1);
            firstNameField.setLines(1);
            firstNameField.setSingleLine(true);
            firstNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            firstNameField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
            firstNameField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            firstNameField.setHint(LocaleController.getString("FirstName", R.string.FirstName));
            AndroidUtilities.clearCursorDrawable(firstNameField);
        linearLayout.addView(firstNameField, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 36, 24, 24, 24, 0));
            firstNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_NEXT) {
                        lastNameField.requestFocus();
                        lastNameField.setSelection(lastNameField.length());
                        return true;
                    }
                    return false;
                }
            });

        lastNameField = new EditText(context);
            lastNameField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            lastNameField.setHintTextColor(0xff979797);
            lastNameField.setTextColor(0xff212121);
            lastNameField.getBackground().setColorFilter(AndroidUtilities.getIntColor("themeColor"), PorterDuff.Mode.SRC_IN);
            lastNameField.setMaxLines(1);
            lastNameField.setLines(1);
            lastNameField.setSingleLine(true);
            lastNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            lastNameField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
            lastNameField.setImeOptions(EditorInfo.IME_ACTION_DONE);
            lastNameField.setHint(LocaleController.getString("LastName", R.string.LastName));
            AndroidUtilities.clearCursorDrawable(lastNameField);
        linearLayout.addView(lastNameField, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 36, 24, 16, 24, 0));
            lastNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        doneButton.performClick();
                        return true;
                    }
                    return false;
                }
            });

            if (user != null) {
                firstNameField.setText(user.first_name);
                firstNameField.setSelection(firstNameField.length());
                lastNameField.setText(user.last_name);
            }

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        boolean animations = preferences.getBoolean("view_animations", true);
        if (!animations) {
            firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(firstNameField);
        }
        updateTheme();
    }

    private void updateTheme(){
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int def = themePrefs.getInt("themeColor", AndroidUtilities.defColor);
        actionBar.setBackgroundColor(themePrefs.getInt("prefHeaderColor", def));
        actionBar.setTitleColor(themePrefs.getInt("prefHeaderTitleColor", 0xffffffff));

        Drawable back = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_back);
        back.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
        actionBar.setBackButtonDrawable(back);
    }

    private void saveName() {
        TLRPC.User currentUser = UserConfig.getCurrentUser();
        if (currentUser == null || lastNameField.getText() == null || firstNameField.getText() == null) {
            return;
        }
        String newFirst = firstNameField.getText().toString();
        String newLast = lastNameField.getText().toString();
        if (currentUser.first_name != null && currentUser.first_name.equals(newFirst) && currentUser.last_name != null && currentUser.last_name.equals(newLast)) {
            return;
        }
        TLRPC.TL_account_updateProfile req = new TLRPC.TL_account_updateProfile();
        currentUser.first_name = req.first_name = newFirst;
        currentUser.last_name = req.last_name = newLast;
        TLRPC.User user = MessagesController.getInstance().getUser(UserConfig.getClientUserId());
        if (user != null) {
            user.first_name = req.first_name;
            user.last_name = req.last_name;
        }
        UserConfig.saveConfig(true);
        NotificationCenter.getInstance().postNotificationName(NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance().postNotificationName(NotificationCenter.updateInterfaces, MessagesController.UPDATE_MASK_NAME);
        ConnectionsManager.getInstance().performRpc(req, new RPCRequest.RPCRequestDelegate() {
            @Override
            public void run(TLObject response, TLRPC.TL_error error) {

            }
        });
    }

    @Override
    public void onOpenAnimationEnd() {
        firstNameField.requestFocus();
        AndroidUtilities.showKeyboard(firstNameField);
    }
}
