package com.npc.cmx.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by CMX on 2016/5/24.
 */

/**
 * 这里是用于管理CrimeListFragment的Activity,其布局为一个fragment容器activity_fragment
 * 大多数加载代码封装在SingleFragmentActivity中，
 * 这里主要是要获得加载的目标Fragment--->CrimeListFragment
 * */

public class CrimeListActivity extends SingleFragmentActivity {

    /**If you have other methods in your CrimeListActivity,
     *  such as onCreate, remove them.
     *  Let SingleFragmentActivity do its job,
     *  to keep CrimeListActivity simple.*/
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
