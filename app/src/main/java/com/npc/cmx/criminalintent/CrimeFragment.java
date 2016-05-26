package com.npc.cmx.criminalintent;

//import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.Date;
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
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDataButton;
    private CheckBox mSolvedCheckBox;

    //通过这个方法可以传入指定的crime ID，通过id加载拥有不同信息的fragment
    public static CrimeFragment newInstance(UUID crimeId){
        //类似Activity一样有一个Bundle，包含键值对信息，每个键值对称为argument
        Bundle args = new Bundle();
        //Object对象必须是序列化对象才能在活动间传递
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        //把携带了键值对信息的Bundle分配给fragment
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 遇到用户改变系统配置或你的app后台后被系统回收而导致fragment重建，
     * 变量内容内容丢失。而在使用argument方法时，则内容不会丢失，在fragment重建后，通过getArguments可以找回。
     * */

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

            //这个方法被调用，说明在s字符串中，从start位置开始的count个字符即将被长度为after的新文本所取代。在这个方法里面改变s，会报错。
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            //这个方法被调用，说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错。
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            //这个方法被调用，那么说明s字符串的某个地方已经被改变。
            @Override
            public void afterTextChanged(Editable s) {
                //This space intentionally left blank
            }
        });

        mDataButton = (Button)v.findViewById(R.id.crime_date);
        //日期格式化的一种方法
        //mDataButton.setText(DateFormat.format("EEEE, MMM d, yyyy",mCrime.getDate()));
        updateDate();
        mDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode!= Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDataButton.setText(mCrime.getDate().toString());
    }
}
