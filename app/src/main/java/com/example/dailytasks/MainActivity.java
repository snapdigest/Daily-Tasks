package com.example.dailytasks;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import com.example.dailytasks.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String THEME = "";
    public static FloatingActionButton fab;
    public static CoordinatorLayout container;
    public static BottomNavigationView navView;
    private TextInputLayout textInputZagol;
    public MyAdapter adapter;
    Switch switchAB;
    int backPressedQ = 0;
    SharedPreferences sPref;
    private CompoundButton.OnCheckedChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        sPref = getPreferences(MODE_PRIVATE);
        String themed = sPref.getString(THEME, "night");
        if (themed.equals("day")) {
        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.LightTheme);
            //container.setBackgroundColor(Color.parseColor("#121212"));
        } else {
            setTheme(R.style.DarkTheme);
            //container.setBackgroundColor(Color.parseColor("#CFCFCF"));
        }
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean themes = SP.getBoolean("theme", true);
        if (themes) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        container = findViewById(R.id.container);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_global, R.id.navigation_success)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.navigation_success || destination.getId() == R.id.navigation_add) {
                    navView.setVisibility(View.GONE);
                }
                else {
                    navView.setVisibility(View.VISIBLE);
        }
        }
        });
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        textInputZagol = findViewById(R.id.edittext1);
        fab = findViewById(R.id.floatingActionButton);
        Context context = getApplicationContext();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals("theme")) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            Boolean themes = SP.getBoolean("theme", true);
            if (themes) {
                setTheme(R.style.DarkTheme);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                setTheme(R.style.LightTheme);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        // TODO Проверять общие настройки, ключевые параметры и изменять UI
        // или поведение приложения, если потребуется.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.theme_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sPref = getPreferences(MODE_PRIVATE);
        String themed = sPref.getString(THEME, "night");
        switch(item.getItemId()) {
            case R.id.switchtheme:
                SharedPreferences.Editor ed = sPref.edit();
                /*
                if (themed.equals("night")) {
                    ed.putString(THEME, "day");
                    ed.commit();
                    setTheme(R.style.LightTheme);
                } else {
                    ed.putString(THEME, "night");
                    ed.commit();
                    setTheme(R.style.DarkTheme);
                }
                 */
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ed.putString(THEME, "night");
                    ed.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ed.putString(THEME, "day");
                    ed.apply();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getPrimaryNavigationFragment().getChildFragmentManager().getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

}
