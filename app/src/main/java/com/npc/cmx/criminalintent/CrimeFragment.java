package com.npc.cmx.criminalintent;

//import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

//import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;


/**
 * Created by CMX on 2016/5/23.
 */
/**
 * 这个是管理fragment_crime.xml（crime详细信息布局）的Fragment类
 * 同时被CrimeActivity所管理
 * */
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDataButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //不要在Fragment的onCreate方法中创建其视图，而是在onCreateView中进行
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        //这里inflate的第三个参数不是saveInstanceState
        //填入false表示不立即把Fragment添加给父视图，我们会在Activity的代码中添加
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        //注意这里需要通过View对象来使用findViewById
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //This one too
            }
        });

        mDataButton = (Button)v.findViewById(R.id.crime_date);
        //日期格式化的一种方法
        //mDataButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mCrime.getDate()));
        mDataButton.setText(mCrime.getDate().toString());
        //暂时禁止使用此按钮并灰掉它
        mDataButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //根据CheckBox的选取状态设置crime的解决状态
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }
}
