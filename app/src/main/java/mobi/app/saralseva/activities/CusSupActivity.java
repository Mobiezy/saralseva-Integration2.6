package mobi.app.saralseva.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.fragments.NewCompFragment;
import mobi.app.saralseva.fragments.RegCompFragment;
import mobi.app.saralseva.utils_view.Pager;

public class CusSupActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ImageView imageBackAction;
    private TextView titleBar;
    LinearLayout toolbarLL;

    //This is our viewPager
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_sup);
        toolbarLL= (LinearLayout) findViewById(R.id.linerLaytoolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Light.ttf");
        titleBar= (TextView) findViewById(R.id.activityName);
        titleBar.setText(getResources().getString(R.string.customer_support));
        titleBar.setTypeface(robotoLight);
        imageBackAction= (ImageView) findViewById(R.id.actionButton);
        imageBackAction.setOnClickListener(this);

        //Adding the tabs using addTab() method
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.new_complaint));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.registered_complaint));

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        //   viewPager.addOnPageChangeListener(this);

        //Creating our pager adapter
//        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
       // viewPager.setAdapter(adapter);

        //Addding onTabSelectedListener to swipe views
//        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewCompFragment(), getString(R.string.complaints_title_new));
        adapter.addFragment(new RegCompFragment(), getString(R.string.complaints_title_lodged));
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        viewPager.setCurrentItem(position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.actionButton:
                super.onBackPressed();
                break;
        }
    }
}
