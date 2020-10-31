package com.example.dailytasks.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailytasks.ui.detail.DetailItemFragment;
import com.example.dailytasks.ItemClickListener;
import com.example.dailytasks.MainActivity;
import com.example.dailytasks.MyAdapter;
import com.example.dailytasks.RecyclerItem;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import androidx.lifecycle.ViewModelProviders;

import com.example.dailytasks.R;
import com.example.dailytasks.ui.add.AddFrag;
import com.example.dailytasks.ui.global.GlobalFragment;
import com.example.dailytasks.ui.success.SuccessFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.example.dailytasks.R.id.recyclerviewhome;


public class HomeFragment extends Fragment implements StartDragListener, ItemClickListener{

    public MyAdapter adapter;
    ItemTouchHelper touchHelper;
    FloatingActionButton fab;
    public RecyclerView recyclerView;
    public static Bundle bundle;
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_ACTION = "action";
    private static final String EXTRA_ARG1 = "title";
    private static final String EXTRA_ARG2 = "description";
    public static List<RecyclerItem> listItems;
    SharedPreferences sPref;
    private static final String THEME = "";
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public static HomeFragment newInstance(int pos, String action, String arg1, String arg2) {
        HomeFragment HomeFrag = new HomeFragment();
        bundle = new Bundle();
        bundle.putInt(EXTRA_POSITION, pos);
        bundle.putString(EXTRA_ACTION, action);
        bundle.putString(EXTRA_ARG1, arg1);
        bundle.putString(EXTRA_ARG2, arg2);
        HomeFrag.setArguments(bundle);
        return HomeFrag;
    }

    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) root.findViewById(recyclerviewhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (listItems == null) {
            //listItems = new ArrayList<>();
            //listItems.add(new RecyclerItem("Emma Wilson", "23 years old"));
            //listItems.add(new RecyclerItem("Lavery Maiss", "25 years old"));
            //listItems.add(new RecyclerItem("Lillie Watts", "35 years old"));
        } else {
            //listItems = Paper.book().read("item");
        }
        fab = MainActivity.fab;
        fab.setImageResource(R.drawable.ic_baseline_post_add_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                //Intent intent = new Intent(getContext(), AddFrag.class);
                //startActivity(intent);
                Fragment AddFragment = AddFrag.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addSharedElement(MainActivity.fab, ViewCompat.getTransitionName(MainActivity.fab))
                        .addToBackStack(TAG)
                        .replace(R.id.nav_host_fragment, AddFragment)
                        .commit();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        double d=1.1;
        float f=2.2F;
        f= (float) (d*2);
        float max = 0;
        if (d>f) max=(float)d; else max=f;

        adapter = new MyAdapter(listItems,this, this);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(adapter);
        touchHelper  = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        if(bundle != null){
            String action = bundle.getString(EXTRA_ACTION);
            final int pos = bundle.getInt(EXTRA_POSITION);
            String del = "delete";
            String edit = "edit";
            //switch (action) {
            //    case "delete":
            //        DeleteItem(pos);
            //    case "edit":
            //        EditItem(pos);
            //}
            if (action == del){
                DeleteItem(pos);
            }
            if (action == edit){
                EditItem(pos);
            }
        }
        BottomNavigationView navView = MainActivity.navView;
        navView.setVisibility(View.VISIBLE);
        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return root;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.resturnse));
        //setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade));
        //setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.enter));
        setHasOptionsMenu(true);
        listItems = Paper.book().read("item");
        GlobalFragment.createlistitem();
        SuccessFragment.createlistitem();
        if (listItems == null) {
            listItems = new ArrayList<>();
            //listItems.add(new RecyclerItem("Emma Wilson", "23 years old"));
            //listItems.add(new RecyclerItem("Lavery Maiss", "25 years old"));
            //listItems.add(new RecyclerItem("Lillie Watts", "35 years old"));
        }
        else {

        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled (false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //inflater.inflate(R.menu.theme_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
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
                        ed.commit();
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        ed.putString(THEME, "day");
                        ed.commit();
                    }
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Paper.book().write("item", HomeFragment.listItems);
    }



    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    private void DeleteItem(final int pos) {
        final RecyclerItem item = adapter.getData().get(pos);
        String arg1 = bundle.getString(EXTRA_ARG1);
        String tosuc = "tosuc";
        listItems.remove(pos);
        adapter.notifyItemRemoved(pos);
        bundle.clear();
        if (arg1 == tosuc) {
            Snackbar snackbar = Snackbar
                    .make(MainActivity.container, "Отлично! Задача выполнена", Snackbar.LENGTH_LONG);
            snackbar.setAnchorView(fab);
            snackbar.show();
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(MainActivity.container, "Задача удалена", Snackbar.LENGTH_LONG);
            snackbar.setAnchorView(fab);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItems.add(pos, item);
                    adapter.notifyItemInserted(pos);
                    recyclerView.scrollToPosition(pos);
                    Paper.book().write("item", listItems);
                    // undo is selected, restore the deleted item
                    //mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.show();
        }
        Paper.book().write("item", listItems);
    }

    private void EditItem(final int pos) {
        String title = bundle.getString(EXTRA_ARG1);
        String text = bundle.getString(EXTRA_ARG2);
        RecyclerItem item = new RecyclerItem(title,text);
        listItems.remove(pos);
        adapter.notifyItemRemoved(pos);
        listItems.add(pos, item);
        adapter.notifyItemInserted(pos);
        recyclerView.scrollToPosition(pos);
        bundle.clear();
        Paper.book().write("item", listItems);
    }

    @Override
    public void onItemClick(int pos, RecyclerItem itemList, CardView shareItemView) {
        Fragment DetailFragment = DetailItemFragment.newInstance(pos, itemList, ViewCompat.getTransitionName(shareItemView), TAG);
        getFragmentManager()
                .beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addSharedElement(shareItemView, ViewCompat.getTransitionName(shareItemView))
                .addSharedElement(MainActivity.fab, ViewCompat.getTransitionName(MainActivity.fab))
                .addToBackStack(TAG)
                .replace(R.id.nav_host_fragment, DetailFragment)
                .commit();
    }
}
