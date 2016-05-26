package com.npc.cmx.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

/**
 * Created by CMX on 2016/5/24.
 */

/**
 *管理整个crime清单Fragment布局，同时被CrimeListActivity所管理
 * 在这里初始化加载了整个crime清单,布局以及相应的显示信息
 * 包括RecycleView，RecycleView的ViewHolder、RecycleView.Adapter
 * */

public class CrimeListFragment extends Fragment{
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE= "subtitle";

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //let the FragmentManager know that
        //CrimeListFragment needs to receive menu callbacks.
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycle_view);
        //setLayoutManager（new LinearLayoutManager(getActivity())）
        //设置了一个线性布局管理器，用来表明以垂直或水平滚动列表方式显示项目。
        //GridLayoutManager： 在网格中显示项目。
        //StaggeredGridLayoutManager： 在分散对齐网格中显示项目。。
        //回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，这种机制避免了创建过多的View和频繁的调用
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();//第一次更新UI显示信息

        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());//激活CrimeLab对CrimeList的初始化
        List<Crime> crimes = crimeLab.getCrimes();//获取初始化好的crime集合
        if (mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    //采用内部类的方式来自定义RecycleView的适配器，RecycleView控制适配器，适配器控制ViewHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;
        //在构造方法里获取CrimeList
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        /**is called by the RecyclerView
         * when it needs a new View to display an item.*/
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //这里的View生成自Android的标准库
            //View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);//ViewHolder控制的布局是list_item_crime
            //把这个View传给ViewHolder
            return new CrimeHolder(view);
        }

        /**This method will bind a ViewHolder’s View to your model object.*/
        //将VIewHolder和模型（MCV）绑定
        @Override
        public void onBindViewHolder(CrimeHolder holder,int position){
            Crime crime = mCrimes.get(position);
            //通过加载到的模型对象属性来更新ViewHolder的组件
//            holder.mTitleTextView.setText(crime.getTitle());
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }
    }

    //内部类方式创建自定义的ViewHolder，在ViewHolder中加载布局细节
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solve_check_box);
        }

        //通过这个方法使用crime的属性来初始化布局
        private void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //设置点击事件启动Crime详细页面CrimePagerActivity
        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }
    }

    //选择在onResume()更新UI的原因是
    //无法确定离开清单时清单是否被完全隐藏，若完全隐藏重建时会调用onStart和onResume
    //若未完全隐藏则只会调用onResume
    //所以onResume最保险
    @Override
    public void onResume() {
        super.onResume();
        updateUI();//每次回到列表清单时都要更新列表信息
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        //非强制要求使用父类，但是建议使用
        //it is only a convention –
        // the base Fragment implementation of this method does nothing.
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitltItem = menu.findItem(R.id.menu_item_show_subtitle);

        if (mSubtitleVisible){
            subtitltItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitltItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }
}
