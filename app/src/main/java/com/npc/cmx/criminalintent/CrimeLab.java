package com.npc.cmx.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by CMX on 2016/5/24.
 */

/**
 * 这个类用于对多个crime对象的储存和管理
 * */
public class CrimeLab {
    private static CrimeLab sCrimeLab;//确保全局唯一的静态CrimeLab对象
    private List<Crime> mCrimes;//储存多个crime对象的List

    //此构造方法为private的，确保只能在本类中使用，通过调用静态方法get来激活这个构造方法
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    //通过get方法获得一个本类的单例对象后，通过此单例可以调用下面的两个实例方法
    public List<Crime> getCrimes(){
        return mCrimes;
    }//返回创建好的crime数组

    public Crime getCrime(UUID id){
        for (Crime crime: mCrimes){
            if (crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    //此静态方法会激活构造方法，联系相关活动来创建一个对象池
    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);//新建并返回静态ClimeLab，且只会有唯一的一个sCrimeLab
        }
        return sCrimeLab;//返回已经创建好的唯一ClimeLab
    }
}
