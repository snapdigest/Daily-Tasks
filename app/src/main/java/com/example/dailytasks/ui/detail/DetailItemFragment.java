package com.example.dailytasks.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.TransitionInflater;

import com.example.dailytasks.MainActivity;
import com.example.dailytasks.MyAdapter;
import com.example.dailytasks.R;
import com.example.dailytasks.RecyclerItem;
import com.example.dailytasks.ui.global.GlobalFragment;
import com.example.dailytasks.ui.home.HomeFragment;
import com.example.dailytasks.ui.success.SuccessFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import io.paperdb.Paper;

public class DetailItemFragment extends Fragment {
    private static final String EXTRA_TITLE = "task_item";
    private static final String EXTRA_TRANSITION_CARD = "transition_name";
    private static final String EXTRA_TRANSITION_TITLE = "transition_title";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_TAG = "fragment";
    public static String itemTransitionName;
    public static Bundle bundle;
    private RecyclerItem itemList;
    private ShareActionProvider shareActionProvider;
    MyAdapter adapter;
    HomeFragment homefrag;
    FloatingActionButton fab;
    TextView fab2;
    TextView gotosuccess;
    TextView textTitle;
    TextView textDescription;
    TextView textedit;
    TextView titleedit;
    String titletask;
    String titledesk;
    FragmentManager fm;
    public static final String TAG = DetailItemFragment.class.getSimpleName();

    public DetailItemFragment() {
        // Required empty public constructor
    }
    public static DetailItemFragment newInstance(int pos, RecyclerItem itemList, String transitionName, String tag) {
        DetailItemFragment DetailItemFragment = new DetailItemFragment();
        bundle = new Bundle();
        bundle.putParcelable(EXTRA_TITLE, itemList);
        bundle.putString(EXTRA_TRANSITION_CARD, transitionName);
        bundle.putString(EXTRA_TAG, tag);
        bundle.putInt(EXTRA_POSITION, pos);
        //bundle.putString(EXTRA_TRANSITION_TITLE, transitionTitle);
        DetailItemFragment.setArguments(bundle);
        return DetailItemFragment;
    }
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.opense));
        //fm = getActivity().getSupportFragmentManager();
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fab = MainActivity.fab;
        String fragment = bundle.getString(EXTRA_TAG);
        if ((fragment.equals("HomeFragment")) || (fragment.equals("GlobalFragment"))) {
            fab.show();
        }
        else fab.hide();
        fab.setImageResource(R.drawable.ic_baseline_create_24);
        BottomNavigationView navView = MainActivity.navView;
        navView.setVisibility(View.GONE);
        return inflater.inflate(R.layout.recycler_menu, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemList = getArguments().getParcelable(EXTRA_TITLE);
        //RecyclerItem itemList = getArguments().getParcelable(EXTRA_TITLE);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_CARD);
        //String transitionTitle = getArguments().getString(EXTRA_TRANSITION_TITLE);

        textTitle = (TextView) view.findViewById(R.id.txtTitle2);
        textTitle.setText(itemList.getTitle());
        textDescription = (TextView) view.findViewById(R.id.txtDescription2);
        textDescription.setText(itemList.getDescription());
        fab2 = (TextView) view.findViewById(R.id.editbutton);
        gotosuccess = (TextView) view.findViewById(R.id.gotosuccess);
        CardView card = view.findViewById(R.id.card1);

            card.setTransitionName(transitionName);

        //BottomAppBar bottom_app_bar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(bottom_app_bar);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable descriptionBackground = view.findViewById(R.id.descrback).getBackground();
        descriptionBackground.setAlpha(20);
        Drawable titleBackground = view.findViewById(R.id.titleback).getBackground();
        titleBackground.setAlpha(20);
        textedit = (TextView) view.findViewById(R.id.descriptionedit);
        textedit.setText(itemList.getDescription());
        titleedit = (TextView) view.findViewById(R.id.titleedit);
        titleedit.setText(itemList.getTitle());
        final TextInputLayout textFieldtitle = view.findViewById(R.id.textFieldtitle);
        final TextInputLayout textFielddescr = view.findViewById(R.id.textFielddescr);
        //TextView share = view.findViewById(R.id.share);
        String fragment = bundle.getString(EXTRA_TAG);
        if (fragment.equals("SuccessFragment")) {
            gotosuccess.setVisibility(view.GONE);
        }
        else gotosuccess.setVisibility(view.VISIBLE);
        titletask = itemList.getTitle();
        titledesk = itemList.getDescription();
        /*share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, textDescription.getText().toString());
                intent.putExtra(Intent.EXTRA_TITLE, textTitle.getText().toString());
                String chooserTitle = getString(R.string.chooser);
                Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
                startActivity(chosenIntent);
            }
        });
         */
        gotosuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                int pos = bundle.getInt(EXTRA_POSITION);
                String fragment = bundle.getString(EXTRA_TAG);
                String del = "delete";
                String tosuc = "tosuc";
                if (fragment.equals("HomeFragment")) {
                    Fragment HomeFrag = HomeFragment.newInstance(pos, del, tosuc, null);
                }
                if (fragment.equals("GlobalFragment")) {
                    Fragment GlobFrag = GlobalFragment.newInstance(pos, del, tosuc, null);
                }
                SuccessFragment.listItems.add(new RecyclerItem(textTitle.getText().toString(), textDescription.getText().toString()));
                Paper.book().write("success", SuccessFragment.listItems);
                fm.popBackStack();
            }
        });
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                textTitle.setVisibility(view.GONE);
                textDescription.setVisibility(view.GONE);
                gotosuccess.setVisibility(view.GONE);
                fab2.setVisibility(view.VISIBLE);
                textFieldtitle.setVisibility(view.VISIBLE);
                textFielddescr.setVisibility(view.VISIBLE);
                fab.hide();
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v){
                        String titleendedit = titleedit.getText().toString();
                        String textendedit = textedit.getText().toString();
                        int pos = bundle.getInt(EXTRA_POSITION);
                        String fragment = bundle.getString(EXTRA_TAG);
                        String edit = "edit";
                        if (fragment.equals("HomeFragment")) {
                            Fragment HomeFrag = HomeFragment.newInstance(pos, edit, titleendedit, textendedit);
                        }
                        if (fragment.equals("GlobalFragment")) {
                            Fragment GlobFrag = GlobalFragment.newInstance(pos, edit, titleendedit, textendedit);
                        }
                        textTitle.setVisibility(v.VISIBLE);
                        textDescription.setVisibility(v.VISIBLE);
                        textFieldtitle.setVisibility(v.GONE);
                        textFielddescr.setVisibility(v.GONE);
                        fab2.setVisibility(v.GONE);
                        gotosuccess.setVisibility(v.VISIBLE);
                        //fab.setImageResource(R.drawable.ic_baseline_create_24);
                        textTitle.setText(titleendedit);
                        textDescription.setText(textendedit);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        fab.show();
                    }
                });
                //fab.setImageResource(R.drawable.ic_baseline_check_24);
                textFielddescr.setEndIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String titleendedit = titleedit.getText().toString();
                        String textendedit = textedit.getText().toString();
                        int pos = bundle.getInt(EXTRA_POSITION);
                        String fragment = bundle.getString(EXTRA_TAG);
                        String edit = "edit";
                        if (fragment.equals("HomeFragment")) {
                            Fragment HomeFrag = HomeFragment.newInstance(pos, edit, titleendedit, textendedit);
                        }
                        if (fragment.equals("GlobalFragment")) {
                            Fragment GlobFrag = GlobalFragment.newInstance(pos, edit, titleendedit, textendedit);
                        }
                        textTitle.setVisibility(v.VISIBLE);
                        textDescription.setVisibility(v.VISIBLE);
                        textFieldtitle.setVisibility(v.GONE);
                        textFielddescr.setVisibility(v.GONE);
                        //fab.setImageResource(R.drawable.ic_baseline_create_24);
                        textTitle.setText(titleendedit);
                        textDescription.setText(textendedit);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        fab.show();
                    }
                });
            }
        });
         */
        //textTitle.setTransitionName(transitionTitle);
        //startPostponedEnterTransition();
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                int pos = bundle.getInt(EXTRA_POSITION);
                String tagback = getArguments().getString(EXTRA_TAG);
                Fragment editFragment = EditFragment.newInstance(pos, itemList, tagback);
                getFragmentManager()
                        .beginTransaction()
                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addSharedElement(MainActivity.fab, ViewCompat.getTransitionName(MainActivity.fab))
                        .addToBackStack(TAG)
                        .replace(R.id.nav_host_fragment, editFragment)
                        .commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled (true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setShareActionIntent();
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void setShareActionIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textDescription.getText().toString());
        intent.putExtra(Intent.EXTRA_TITLE, textTitle.getText().toString());
        shareActionProvider.setShareIntent(intent);

        //String chooserTitle = getString(R.string.chooser);
        //Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
        //startActivity(chosenIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                //ActivityCompat.finishAfterTransition(getActivity());
                //getFragmentManager().beginTransaction().remove(this).commit();
                //getActivity().getSupportFragmentManager().beginTrasaction().remove(this).commit;
                int col = getFragmentManager().getBackStackEntryCount();
                fm.popBackStack();
                //FragmentManager manager = getChildFragmentManager();
                //manager.popBackStack();
                //getActivity().getFragmentManager().popBackStack();
                //getActivity().onBackPressed();
                return true;
            case R.id.delete:
                int pos = bundle.getInt(EXTRA_POSITION);
                String fragment = bundle.getString(EXTRA_TAG);
                String del = "delete";
                if (fragment.equals("HomeFragment")) {
                    Fragment HomeFrag = HomeFragment.newInstance(pos, del, null, null);
                }
                if (fragment.equals("GlobalFragment")) {
                    Fragment GlobFrag = GlobalFragment.newInstance(pos, del, null, null);
                }
                if (fragment.equals("SuccessFragment")) {
                    fab.show();
                    Fragment SucFrag = SuccessFragment.newInstance(pos, del, null, null);
                }
                //Fragment HomeFrag = HomeFragment.newInstance(pos, del, null, null);
                //getFragmentManager()
                //        .beginTransaction()
                //        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //        .addSharedElement(MainActivity.fab, ViewCompat.getTransitionName(MainActivity.fab))
                //        .replace(R.id.nav_host_fragment, HomeFrag)
                //        .commit();
                fm.popBackStack();
                //getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //super.onPause();
        fab.show();
        //getFragmentManager().beginTransaction().addToBackStack(TAG).commit();
    }
}
