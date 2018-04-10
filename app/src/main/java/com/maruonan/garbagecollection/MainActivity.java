package com.maruonan.garbagecollection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maruonan.garbagecollection.bean.UserBean;
import com.maruonan.garbagecollection.ui.AppointmentFragment;
import com.maruonan.garbagecollection.ui.InfoFragment;
import com.maruonan.garbagecollection.ui.PointFragment;
import com.maruonan.garbagecollection.ui.SettingsFragment;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private Fragment currentFragment;     //记录当前正在使用的fragment
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.getDatabase();
        UserBean user = DataSupport.find(UserBean.class, 1);
        if (user == null){
            user = new UserBean();
            user.setId(1);
            user.setUsername("马若男");
            user.setTelNum("1335412375");
            user.setAddress("阳光小区5栋4单元33号");
            user.save();
        }

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);




        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        final TextView tvHeaderName = headerView.findViewById(R.id.tv_header_name);
        final TextView tvHeaderAddr = headerView.findViewById(R.id.tv_header_addr);
        tvHeaderName.setText(user.getUsername());
        tvHeaderAddr.setText(user.getAddress());
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                UserBean DBUser = DataSupport.find(UserBean.class, 1);
                tvHeaderName.setText(DBUser.getUsername());
                tvHeaderAddr.setText(DBUser.getAddress());
            }
        };
        toggle.syncState();
        mDrawer.addDrawerListener(toggle);
        initFragment(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            MainActivity.this.finish();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            toolbar.setTitle("设置");
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.replace(R.id.container, new SettingsFragment()).commitAllowingStateLoss();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        switch (item.getItemId()) {

            case R.id.nav_appointment:
                toolbar.setTitle("我的预约");
                currentFragment = new AppointmentFragment();
                break;
            case R.id.nav_point:
                toolbar.setTitle("我的积分");
                currentFragment = new PointFragment();

                break;
            case R.id.nav_info:
                toolbar.setTitle("个人信息");
                currentFragment = new InfoFragment();
                break;
        }
        fragmentTransaction.replace(R.id.container, currentFragment).commitAllowingStateLoss();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * 为页面加载初始状态的fragment
     */
    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState==null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            if(currentFragment==null) {
                toolbar.setTitle("我的预约");
                currentFragment = new AppointmentFragment();
            }
            fragmentTransaction.replace(R.id.container, currentFragment).commitAllowingStateLoss();
        }
    }
}
