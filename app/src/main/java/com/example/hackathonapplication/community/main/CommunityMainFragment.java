package com.example.hackathonapplication.community.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathonapplication.R;
import com.example.hackathonapplication.community.board.CommunityCommentFragment;
import com.example.hackathonapplication.community.category.CommunityCategoryFragment;
import com.example.hackathonapplication.community.board.Post;
import com.example.hackathonapplication.community.board.PostAdapter;
import com.example.hackathonapplication.myhome.MyPostFragment;
import com.example.hackathonapplication.sqlite.BoardDbOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommunityMainFragment extends Fragment {
    private ViewGroup viewGroup;
    private Context context;
    private ArrayList<Post> dataSet;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PostAdapter adapter;
    private FloatingActionButton plusButton;
    private FloatingActionButton writeButton;
    private FloatingActionButton mypostButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private CommunityCategoryFragment ccf;
    private Animation fab_open, fab_close;
    private Boolean openFlag = false;

    private ImageButton categoryButtonTrot;
    private ImageButton categoryButtonHiking;
    private ImageButton categoryButtonFishing;
    private ImageButton categoryButtonPlant;
    private ImageButton categoryButtonExercise;

    private String id;
    private String profile;
    private String writer;
    private String like;
    private String comment;
    private String contents;
    private String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.community_main_fragment, container, false);
        context = container.getContext();
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        initView();

        return viewGroup;
    }

    private void initView() {

        setDataSet();
        recyclerView = viewGroup.findViewById(R.id.rv_post);
        adapter = new PostAdapter(context, dataSet,fragmentManager);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ccf = new CommunityCategoryFragment();

        categoryButtonTrot = viewGroup.findViewById(R.id.ib_categoryButton_trot);
        categoryButtonHiking = viewGroup.findViewById(R.id.ib_categoryButton_hiking);
        categoryButtonPlant= viewGroup.findViewById(R.id.ib_categoryButton_plant);
        categoryButtonExercise = viewGroup.findViewById(R.id.ib_categoryButton_exercise);
        categoryButtonFishing = viewGroup.findViewById(R.id.ib_categoryButton_fishing);
        plusButton = viewGroup.findViewById(R.id.fab_plus);
        writeButton = viewGroup.findViewById(R.id.fab_write);
        mypostButton = viewGroup.findViewById(R.id.fab_mypost);

        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context,R.anim.fab_close);
        writeButton.setVisibility(View.INVISIBLE);
        mypostButton.setVisibility(View.INVISIBLE);
        writeButton.setClickable(false);
        mypostButton.setClickable(false);

        categoryButtonTrot.setOnClickListener(v->onClick(v));
        categoryButtonHiking.setOnClickListener(v->onClick(v));
        categoryButtonPlant.setOnClickListener(v->onClick(v));
        categoryButtonExercise.setOnClickListener(v->onClick(v));
        categoryButtonFishing.setOnClickListener(v->onClick(v));
        plusButton.setOnClickListener(v->onClick(v));
        writeButton.setOnClickListener(v->onClick(v));
        mypostButton.setOnClickListener(v->onClick(v));

    }

    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_categoryButton_trot:
                replaceBundleFragment(ccf,"트로트");
                break;
            case R.id.ib_categoryButton_hiking:
                replaceBundleFragment(ccf,"등산");
                break;
            case R.id.ib_categoryButton_plant:
                replaceBundleFragment(ccf,"식물");
                break;
            case R.id.ib_categoryButton_exercise:
                replaceBundleFragment(ccf,"운동");
                break;
            case R.id.ib_categoryButton_fishing:
                replaceBundleFragment(ccf,"낚시");
                break;
            case R.id.fab_plus:
                anim();
                break;
            case R.id.fab_write:
                anim();
                replaceFragment(new CommunityWriteFragment(),"2");
                break;
            case R.id.fab_mypost:
                anim();
                replaceFragment(new MyPostFragment(),"MyPost");
                break;
        }

    }

    public void replaceFragment(Fragment fragment,String backstack) {
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(backstack);
        transaction.commit();
    }

    public void setDataSet() {
        dataSet = new ArrayList<>();
        BoardDbOpenHelper dbOpenHelper = new BoardDbOpenHelper(context);
        dbOpenHelper.open();
        dbOpenHelper.create();

        Cursor cursor = dbOpenHelper.sortColumnDesc("postdate");
        while(cursor.moveToNext()) {

            id = cursor.getString(cursor.getColumnIndex("_id"));
            profile = cursor.getString(cursor.getColumnIndex("profile"));
            writer = cursor.getString(cursor.getColumnIndex("writer"));
            contents = cursor.getString(cursor.getColumnIndex("contents"));
            date = cursor.getString(cursor.getColumnIndex("postdate"));
            like = cursor.getString(cursor.getColumnIndex("like"));
            comment = cursor.getString(cursor.getColumnIndex("comment"));

            dataSet.add(new Post(id,profile,writer,"이경배",contents,date,like,comment));
        }

        dbOpenHelper.close();
    }

    public void anim() {

        if (openFlag) {
            writeButton.startAnimation(fab_close);
            mypostButton.startAnimation(fab_close);
            writeButton.setClickable(false);
            mypostButton.setClickable(false);
            openFlag = false;
        } else {
            writeButton.startAnimation(fab_open);
            mypostButton.startAnimation(fab_open);
            writeButton.setClickable(true);
            mypostButton.setClickable(true);
            openFlag = true;
        }
    }

    public void replaceBundleFragment(CommunityCategoryFragment ccf,String categoryname) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryname", categoryname);
        ccf.setArguments(bundle);
        replaceFragment(ccf,"1");
    }

}
