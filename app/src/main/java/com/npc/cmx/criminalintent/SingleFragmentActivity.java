package com.npc.cmx.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by CMX on 2016/5/24.
 */

/**
 * 所有想要管理Fragment布局的活动都可以从这里继承，大量减少代码的重复量
 * 每个继承了SingleFragmentActivity的活动（必须）通过重写抽象方法createFragment()
 * 以此来获得自己的目标Fragment，从而供后续的事务对其进行加载
 * */

/**
 * 新版本的Activity自动包含控制fragment的功能
 * 为了在早期的版本中支持activity控制fragment，需要继承FragmentActivity
 * 使用前需要设置项目的支持库包括com.android.support:support-v4
 * 通过File-Project Structure-dependencies来添加依赖库会免去忘记Gradle同步带来的风险
 * */

/**
 * 由于需要使用ToolBar，所以父类改为AppCompatActivity，其为FragmentActivtiy的子类
 * 所以不用做其他改变
 * */

public abstract class SingleFragmentActivity extends AppCompatActivity{

    private Menu mMenu;

    //继承这个类的活动，通过重写这个方法来创建自己要加载的Fragment
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //返回实现类自己选择的Fragment
            fragment = createFragment();
            fm.beginTransaction()//这里返回一个FragmentManager对象
                    .add(R.id.fragment_container,fragment)//往fragment List 中添加一个fragment
                    .commit();//提交配置好的事务
        }
    }
}
