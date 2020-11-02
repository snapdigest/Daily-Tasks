package com.example.dailytasks.ui.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dailytasks.MainActivity;
import com.example.dailytasks.R;
import com.example.dailytasks.RecyclerItem;
import com.example.dailytasks.ui.global.GlobalFragment;
import com.example.dailytasks.ui.home.HomeFragment;
import com.example.dailytasks.ui.success.SuccessFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;


public class EditFragment extends Fragment {

    FloatingActionButton fab;
    TextView textedit;
    TextView titleedit;
    public static Bundle bundle;
    private RecyclerItem itemList;
    private static final String EXTRA_TITLE = "task_item";
    private static final String EXTRA_DESK = "desk_item";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_TAG = "tag";
    FragmentManager fm;

    public static EditFragment newInstance(int pos, RecyclerItem itemList, String tag) {
        EditFragment EditFragment = new EditFragment();
        bundle = new Bundle();
        //bundle.putString(EXTRA_TITLE, title);
        //bundle.putString(EXTRA_DESK, desk);
        bundle.putString(EXTRA_TAG, tag);
        bundle.putParcelable(EXTRA_TITLE, itemList);
        bundle.putInt(EXTRA_POSITION, pos);
        EditFragment.setArguments(bundle);
        return EditFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BottomNavigationView navView = MainActivity.navView;
        navView.setVisibility(View.GONE);
        fab = MainActivity.fab;
        fab.setImageResource(R.drawable.ic_baseline_check_24);
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemList = getArguments().getParcelable(EXTRA_TITLE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar3);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.edit));
        //String task = getArguments().getString(EXTRA_TITLE);
        //String desk = getArguments().getString(EXTRA_DESK);
        //textedit.setText(task);
        titleedit = (TextView) view.findViewById(R.id.titleedit1);
        titleedit.setText(itemList.getTitle());
        textedit = (TextView) view.findViewById(R.id.descriptionedit1);
        textedit.setText(itemList.getDescription());
        //titleedit.setText(desk);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                itemList.setTitle(titleedit.getText().toString());
                itemList.setDescription(textedit.getText().toString());
                String titleendedit = titleedit.getText().toString();
                String textendedit = textedit.getText().toString();
                int pos = bundle.getInt(EXTRA_POSITION);
                String fragment = bundle.getString(EXTRA_TAG);
                RecyclerItem item = new RecyclerItem(titleendedit,textendedit);
                if (fragment.equals("HomeFragment")) {
                    HomeFragment.listItems.remove(pos);
                    HomeFragment.listItems.add(pos, item);
                    Paper.book().write("item", HomeFragment.listItems);
                }
                if (fragment.equals("GlobalFragment")) {
                    GlobalFragment.listItems.remove(pos);
                    GlobalFragment.listItems.add(pos, item);
                    Paper.book().write("global", GlobalFragment.listItems);
                }
                Fragment DetailFragment = DetailItemFragment.newInstance(pos, itemList, null, fragment);
                fm.popBackStack();
                 }
        });
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
                //getFragmentManager().beginTransaction().remove(this).commit();
                fm.popBackStack();
                //getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}