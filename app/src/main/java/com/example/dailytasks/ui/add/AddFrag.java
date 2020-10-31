package com.example.dailytasks.ui.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.dailytasks.MainActivity;
import com.example.dailytasks.MyAdapter;
import com.example.dailytasks.R;
import com.example.dailytasks.RecyclerItem;
import com.example.dailytasks.ui.global.GlobalFragment;
import com.example.dailytasks.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import io.paperdb.Paper;


public class AddFrag extends Fragment {
    private TextInputLayout textInputZagol;
    private MyAdapter adapter;
    EditText editText1;
    EditText editText2;
    TextInputLayout textField;
    FloatingActionButton fab;
    int tofragment;
    FragmentManager fm;

    public static AddFrag newInstance() {
        AddFrag AddFragment = new AddFrag();
        return new AddFrag();
    }

    public AddFrag() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fm = getActivity().getSupportFragmentManager();
        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
        //    setTheme(R.style.DarkTheme);
        //} else {
         //   setTheme(R.style.LightTheme);
        //}
        //setContentView(R.layout.fragment_add);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tofragment = 0;
        fab = MainActivity.fab;
        fab.show();
        fab.setImageResource(R.drawable.ic_baseline_check_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String zagol = editText1.getText().toString();
                String task = editText2.getText().toString();
                if (zagol.isEmpty()) {
                    editText1.setError("Поле не может быть пустым");
                }
                else {
                    if (tofragment == 1) {
                        GlobalFragment.listItems.add(new RecyclerItem(zagol, task));
                        Paper.book().write("global", GlobalFragment.listItems);
                    }
                    else {
                        HomeFragment.listItems.add(new RecyclerItem(zagol, task));
                        Paper.book().write("item", HomeFragment.listItems);
                    }
                    //adapter.notifyItemInserted(HomeFragment.listItems.size() - 1);
                    Snackbar.make(MainActivity.container, "Задача добавлена", Snackbar.LENGTH_LONG)
                            .setAnchorView(fab)
                            .show();
                    fm.popBackStack();
                    // Toast.makeText(mContext, "Tasks add",Toast.LENGTH_LONG).show();

                }

            }
        });
        BottomNavigationView navView = MainActivity.navView;
        navView.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        editText1 = (EditText) view.findViewById(R.id.edittext1);
        editText2 = (EditText) view.findViewById(R.id.edittext2);
        textField = view.findViewById(R.id.textField1);
        RadioButton taskRadioButton = (RadioButton)view.findViewById(R.id.radioButtontask);
        RadioButton globalRadioButton = (RadioButton)view.findViewById(R.id.radioButtonglobal);
        taskRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tofragment = 0;
            }
        });
        globalRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tofragment = 1;
            }
        });
        textField.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zagol = editText1.getText().toString();
                String task = editText2.getText().toString();
                if (zagol.isEmpty()) {
                    editText1.setError("Поле не может быть пустым");
                }
                else {
                    HomeFragment.listItems.add(new RecyclerItem(zagol, task));
                    //adapter.notifyItemInserted(HomeFragment.listItems.size() - 1);
                    Snackbar.make(MainActivity.container, "Задача добавлена", Snackbar.LENGTH_LONG)
                            .setAnchorView(fab)
                            .show();
                    fm.popBackStack();
                    // Toast.makeText(mContext, "Tasks add",Toast.LENGTH_LONG).show();

                }
            }
            // Respond to end icon presses
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled (true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                //ActivityCompat.finishAfterTransition(getActivity());
                fm.popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //public void onClick2 (View view) {
    //    String zagol = editText1.getText().toString();
    //    String task = editText2.getText().toString();
     //   if (zagol.isEmpty()) {
     //       editText1.setError("Поле не может быть пустым");
     //   }
     //  else {
      //          HomeFragment.listItems.add(new RecyclerItem(zagol, task));
     //           //adapter.notifyItemInserted(HomeFragment.listItems.size() - 1);
      //          finish();
      //     // Toast.makeText(mContext, "Tasks add",Toast.LENGTH_LONG).show();

     //   }
    //}
}
