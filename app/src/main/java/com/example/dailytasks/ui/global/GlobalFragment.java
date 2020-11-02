package com.example.dailytasks.ui.global;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailytasks.ui.detail.DetailItemFragment;
import com.example.dailytasks.ItemClickListener;
import com.example.dailytasks.MainActivity;
import com.example.dailytasks.MyAdapter;
import com.example.dailytasks.R;
import com.example.dailytasks.RecyclerItem;
import com.example.dailytasks.ui.add.AddFrag;
import com.example.dailytasks.ui.home.ItemMoveCallback;
import com.example.dailytasks.ui.home.StartDragListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

import static com.example.dailytasks.R.id.recyclerviewglobal;

public class GlobalFragment extends Fragment implements StartDragListener, ItemClickListener {

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
    public static GlobalFragment newInstance() {
        return new GlobalFragment();
    }
    public static GlobalFragment newInstance(int pos, String action, String arg1, String arg2) {
        GlobalFragment GlobalFrag = new GlobalFragment();
        bundle = new Bundle();
        bundle.putInt(EXTRA_POSITION, pos);
        bundle.putString(EXTRA_ACTION, action);
        bundle.putString(EXTRA_ARG1, arg1);
        bundle.putString(EXTRA_ARG2, arg2);
        GlobalFrag.setArguments(bundle);
        return GlobalFrag;
    }

    public static final String TAG = GlobalFragment.class.getSimpleName();

    private GlobalViewModel dashboardViewModel;

    public static void createlistitem() {
        listItems = Paper.book().read("global");
        if (listItems == null) {
            listItems = new ArrayList<>();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_global, container, false);
        recyclerView = (RecyclerView) root.findViewById(recyclerviewglobal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //listItems.add(new RecyclerItem("Emma Wilson", "23 years old"));
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
            if (action == del){
                DeleteItem(pos);
            }
        }
        BottomNavigationView navView = MainActivity.navView;
        navView.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.resturnse));
        //setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade));
        //setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.enter));
        setHasOptionsMenu(true);
        listItems = Paper.book().read("global");
        if (listItems == null) {
            listItems = new ArrayList<>();
            //listItems.add(new RecyclerItem("Emma Wilson", "23 years old"));
            //listItems.add(new RecyclerItem("Lavery Maiss", "25 years old"));
            //listItems.add(new RecyclerItem("Lillie Watts", "35 years old"));
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled (false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Paper.book().write("global", listItems);
    }


    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    private void DeleteItem(final int pos) {
        final RecyclerItem item = adapter.getData().get(pos);
        listItems.remove(pos);
        adapter.notifyItemRemoved(pos);
        bundle.clear();
            Snackbar snackbar = Snackbar
                    .make(MainActivity.container, "Задача удалена", Snackbar.LENGTH_LONG);
            snackbar.setAnchorView(fab);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItems.add(pos, item);
                    adapter.notifyItemInserted(pos);
                    recyclerView.scrollToPosition(pos);
                    Paper.book().write("global", listItems);
                    // undo is selected, restore the deleted item
                    //mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.show();
        Paper.book().write("global", listItems);
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
