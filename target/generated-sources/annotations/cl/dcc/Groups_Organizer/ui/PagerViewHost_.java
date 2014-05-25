//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package cl.dcc.Groups_Organizer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import cl.dcc.Groups_Organizer.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class PagerViewHost_
    extends PagerViewHost
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.pager_view);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static PagerViewHost_.IntentBuilder_ intent(Context context) {
        return new PagerViewHost_.IntentBuilder_(context);
    }

    public static PagerViewHost_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new PagerViewHost_.IntentBuilder_(fragment);
    }

    public static PagerViewHost_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new PagerViewHost_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mButtonProfile = ((ImageButton) hasViews.findViewById(cl.dcc.Groups_Organizer.R.id.pagerViewButtonProfile));
        mViewPager = ((ViewPager) hasViews.findViewById(cl.dcc.Groups_Organizer.R.id.pager));
        mCreateEvent = ((View) hasViews.findViewById(cl.dcc.Groups_Organizer.R.id.pagerViewButtonCreateEvent));
        mTextUserName = ((TextView) hasViews.findViewById(cl.dcc.Groups_Organizer.R.id.pagerViewTextName));
        mTabHost = ((TabHost) hasViews.findViewById(android.R.id.tabhost));
        mButtonGroups = ((ImageButton) hasViews.findViewById(cl.dcc.Groups_Organizer.R.id.pagerViewButtonGroups));
        initVars();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, PagerViewHost_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, PagerViewHost_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, PagerViewHost_.class);
        }

        public Intent get() {
            return intent_;
        }

        public PagerViewHost_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent_, requestCode);
                } else {
                    if (context_ instanceof Activity) {
                        ((Activity) context_).startActivityForResult(intent_, requestCode);
                    } else {
                        context_.startActivity(intent_);
                    }
                }
            }
        }

    }

}
