package com.duowei.kitchen_barbecue.tools;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017-06-26.
 */

public class PreferenceUtils {
    private static Context context;
    private final SharedPreferences.Editor mEdit;
    private final SharedPreferences mPreferences;

    private PreferenceUtils(Context context){
       this.context=context;
        mPreferences = context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        mEdit = mPreferences.edit();
   }
    private static PreferenceUtils pfu=null;
    public static PreferenceUtils  getInstance(Context context){
        if(pfu==null){
            pfu=new PreferenceUtils(context);
        }
        return pfu;
    }
    public void setServiceIp(String key, String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getServiceIp(String key, String value){
        return mPreferences.getString(key,value);
    }
    public void setKetchen(String key, String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getKetchen(String key, String value){
        return mPreferences.getString(key,value);
    }

    public void setPrintStytle(String key, String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getPrintStytle(String key, String value){
        return mPreferences.getString(key,value);
    }
    public void setListColums(String key, String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getListColums(String key, String value){
        return mPreferences.getString(key,value);
    }

    public void setPrinterIp(String key, String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getPrinterIp(String key, String value){
        return mPreferences.getString(key,value);
    }

   public void setColor1(String key, int value){
       mEdit.putInt(key,value);
       mEdit.commit();
   }
   public int getColor1(String key, int value){
       return mPreferences.getInt(key,value);
   }

    public void setColor2(String key, int value){
        mEdit.putInt(key,value);
        mEdit.commit();
    }
    public int getColor2(String key, int value){
        return mPreferences.getInt(key,value);
    }

    public void setColor3(String key, int value){
        mEdit.putInt(key,value);
        mEdit.commit();
    }
    public int getColor3(String key, int value){
        return mPreferences.getInt(key,value);
    }

    public void setColor4(String key, int value){
        mEdit.putInt(key,value);
        mEdit.commit();
    }
    public int getColor4(String key, int value){
        return mPreferences.getInt(key,value);
    }
}
