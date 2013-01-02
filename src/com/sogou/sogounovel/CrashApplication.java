package com.sogou.sogounovel;

import com.sogou.constdata.ConstData;
import com.sogou.util.CrashUtil;

import android.app.Application;  

/** 
 * �ڿ���Ӧ��ʱ�����Activity�򽻵�����Applicationʹ�õľ���Խ����ˡ� 
 * Application����������Ӧ�ó����ȫ��״̬�ģ�����������Դ�ļ��� 
 * ��Ӧ�ó���������ʱ��Application�����ȴ�����Ȼ��Ż�������(Intent)������Ӧ��Activity����Service�� 
 * �ڱ��Ľ���Application��ע��δ�����쳣�������� 
 */  
public class CrashApplication extends Application {  
    @Override  
    public void onCreate() {  
        super.onCreate();
        if(!ConstData.dbg){
        	CrashUtil crashUtil = CrashUtil.getInstance();  
            // ע��crashHandler  
            crashUtil.init(getApplicationContext());  
            // ������ǰû���͵ı���(��ѡ)  
            crashUtil.sendPreviousReportsToServer();
        }
          
    }  
}  