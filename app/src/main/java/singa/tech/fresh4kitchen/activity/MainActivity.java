package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import singa.tech.fresh4kitchen.BuildConfig;
import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.adapter.NavigationAdapter;
import singa.tech.fresh4kitchen.fragment.AboutUsFragment;
import singa.tech.fresh4kitchen.fragment.CategoryProductsFragment;
import singa.tech.fresh4kitchen.fragment.HomeFragment;
import singa.tech.fresh4kitchen.fragment.MyOrderFragment;
import singa.tech.fresh4kitchen.fragment.NotificationFragment;
import singa.tech.fresh4kitchen.fragment.SearchFragment;
import singa.tech.fresh4kitchen.fragment.SupportFragment;
import singa.tech.fresh4kitchen.model.Navigation;
import singa.tech.fresh4kitchen.util.Constant;
import singa.tech.fresh4kitchen.util.MyDialogView;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    Context ctx;
    ImageView menu_iv, noti_iv;
    RelativeLayout cart_ll;
    ListView listView;
    TextView header_tv, search_tv, counte_tv;
    TextView nav_name_tv, nav_mobile_tv;
    CircleImageView circleImageView;
    ArrayList<Navigation> nav_list;
    NavigationView navigationView;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initXml();
        setNavigationView();
        setHome();
    }

    private void setHome() {
        Utility.setPage(sessionManager, Constant.PAGE_HOME);
        HomeFragment fragment = new HomeFragment(ctx);
        Utility.setMainFragment(fragment, ctx);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (sessionManager.getData(SessionManager.KEY_PAGE_NAME).equals(Constant.PAGE_HOME)) {
                MyDialogView.exitDialog(this);
            } else {
                setHome();
            }
        }
    }

    private void setNavigationView() {
        nav_list = new ArrayList<>();
        nav_list.add(new Navigation(R.drawable.nav_home, "Home"));
        nav_list.add(new Navigation(R.drawable.fruits, "Fruits"));
        nav_list.add(new Navigation(R.drawable.nav_veg, "Vegetables"));
        nav_list.add(new Navigation(R.drawable.nav_namkeen, "Namkeen"));
        nav_list.add(new Navigation(R.drawable.myorder, "My Orders"));
        nav_list.add(new Navigation(R.drawable.ic_notifications, "Notification"));
        nav_list.add(new Navigation(R.drawable.whatsapp, "Whatsapp Support"));
        nav_list.add(new Navigation(R.drawable.ic_feedback_black_24dp, "Feedback & Support"));
        nav_list.add(new Navigation(R.drawable.nav_about, "About Us "));
        nav_list.add(new Navigation(R.drawable.logout, "Logout"));
        nav_list.add(new Navigation(R.drawable.logout, "App version " + BuildConfig.VERSION_NAME));

        NavigationAdapter adapter = new NavigationAdapter(ctx, nav_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    private void initXml() {
        ctx = this;
        header_tv = findViewById(R.id.tv_main_header);
        search_tv = findViewById(R.id.tv_main_search);
        listView = findViewById(R.id.lv_main_navlist);
        menu_iv = findViewById(R.id.iv_main_menu);
        noti_iv = findViewById(R.id.iv_main_notification);
        counte_tv = findViewById(R.id.tv_main_counter);
        cart_ll = findViewById(R.id.rl_main_cart);
        navigationView = findViewById(R.id.nav_view);

        sessionManager = new SessionManager(ctx);

        noti_iv.setOnClickListener(this);
        cart_ll.setOnClickListener(this);
        menu_iv.setOnClickListener(this);
        search_tv.setOnClickListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        circleImageView = headerLayout.findViewById(R.id.iv_nav_userimage);
        nav_name_tv = headerLayout.findViewById(R.id.tv_nav_name);
        nav_mobile_tv = headerLayout.findViewById(R.id.tv_nav_mobile);
        nav_name_tv.setText(sessionManager.getData(SessionManager.KEY_NAME));
        nav_mobile_tv.setText(sessionManager.getData(SessionManager.KEY_MOBILE));

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, ProfileActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Fragment fragment = null;
        if (i == 0) {
            // home
            setHome();
        } else if (i == 1) {
            // fruits
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new CategoryProductsFragment(ctx, "0");
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 2) {
            // veg
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new CategoryProductsFragment(ctx, "1");
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 3) {
            // organic
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new CategoryProductsFragment(ctx, "2");
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 4) {
            // my order
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new MyOrderFragment(ctx);
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 5) {
            // notification
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new NotificationFragment(ctx);
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 6) {
            // whatsapp
            Utility.openWhatsApp("+918989998292", "Hello", this);
        } else if (i == 7) {
            // support feedback
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new SupportFragment(ctx);
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 8) {
            // about
            Utility.setPage(sessionManager, Constant.PAGE_OTHER);
            fragment = new AboutUsFragment(ctx);
            Utility.setMainFragment(fragment, ctx);
        } else if (i == 9) {
            // logout
            MyDialogView.logoutDialog(this, sessionManager);
        }
        onBackPressed();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_main_menu:
                openDrawer();
                break;

            case R.id.iv_main_notification:
                Utility.setPage(sessionManager, Constant.PAGE_OTHER);
                NotificationFragment fragment1 = new NotificationFragment(ctx);
                Utility.setMainFragment(fragment1, ctx);
                break;

            case R.id.tv_main_search:
                Utility.setPage(sessionManager, Constant.PAGE_OTHER);
                SearchFragment fragment = new SearchFragment(ctx);
                Utility.setMainFragment(fragment, ctx);
                break;

            case R.id.rl_main_cart:
                startActivity(new Intent(ctx, CartActivity.class));
                break;
        }
    }

    private void openDrawer() {
        Utility.hideKeyboard(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void setCounter() {
        if (Utility.isNullOrBlank(sessionManager.getData(SessionManager.KEY_COUNTER))) {
            counte_tv.setText("0");
        } else {
            counte_tv.setText(sessionManager.getData(SessionManager.KEY_COUNTER));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCounter();
        nav_name_tv.setText(sessionManager.getData(SessionManager.KEY_NAME));
        nav_mobile_tv.setText(sessionManager.getData(SessionManager.KEY_MOBILE));
        String image = sessionManager.getData(SessionManager.KEY_USER_IMAGE);
        if (!Utility.isNullOrBlank(image)) {
            Utility.loadImage(ctx, image, circleImageView);
        } else {
            circleImageView.setImageResource(R.drawable.profile);
        }
    }
}
