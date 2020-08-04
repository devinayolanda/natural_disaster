package com.exa.panha.xcross_help.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.exa.panha.xcross_help.Asset.CartFragment;
import com.exa.panha.xcross_help.Asset.HistoryFragment;
import com.exa.panha.xcross_help.Asset.MySingleton;
import com.exa.panha.xcross_help.Asset.NewsFragment;
import com.exa.panha.xcross_help.Asset.ProfileFragment;
import com.exa.panha.xcross_help.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager;
    private static final String Setting_name = "Log_In";
    private BottomNavigationView mBtmView;
    private SharedPreferences pref;
    private int chc;
    public static String email_user;
    public static String password_user;
    public static String id_donatur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences(Setting_name, MODE_PRIVATE);
        id_donatur = pref.getString("id_donatur", "");
        email_user = pref.getString("user", "");
        password_user = pref.getString("password", "");
        BukaFragment(new NewsFragment(), null);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        fragmentManager = getSupportFragmentManager();
        mBtmView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBtmView.setOnNavigationItemSelectedListener(this);
        mBtmView.getMenu().findItem(R.id.news).setChecked(true);
        /*MySingleton.getInstance(this).addToRequestQueue(stringRequest);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get menu inflater.
        MenuInflater menuInflater = getMenuInflater();
        // Inflate the menu with custom menu items.
        menuInflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_notif:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void BukaFragment(Fragment fragment, Bundle datakirim) {
        fragment.setArguments(datakirim);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        chc = item.getItemId();
        for (int i = 0; i < mBtmView.getMenu().size(); i++) {
            MenuItem menuItem = mBtmView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.news: {
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.frag_layout, new NewsFragment())
                        .addToBackStack("Fragment News")
                        .commit();

           //     getSupportActionBar().setTitle("News");
            }
            break;
            case R.id.dbox: {
                CartFragment fragment = new CartFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user", pref.getString("user", "") );
                fragment.setArguments(bundle);
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.frag_layout, fragment)
                        .addToBackStack("Fragment Donation Box")
                        .commit();

           //     getSupportActionBar().setTitle("Donation Box");
            }
            break;
            case R.id.history: {
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.frag_layout, new HistoryFragment())
                        .addToBackStack("Fragment History")
                        .commit();

           //     getSupportActionBar().setTitle("History");
            }
            break;
            case R.id.profile: {
                ProfileFragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user", pref.getString("user", "") );
                bundle.putString("password", pref.getString("password", "") );
                fragment.setArguments(bundle);
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.frag_layout, fragment)
                        .addToBackStack("Fragment Profile")
                        .commit();

            //    getSupportActionBar().setTitle("Profile");
            }
            break;
        }
        return true;
    }
}
