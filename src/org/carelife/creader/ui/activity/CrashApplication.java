package org.carelife.creader.ui.activity;

import org.carelife.creader.dao.UrlHelper;
import org.carelife.creader.util.CrashUtil;


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
        if(!UrlHelper.dbg){
        	CrashUtil crashUtil = CrashUtil.getInstance();  
            // ע��crashHandler  
            crashUtil.init(getApplicationContext());  
            // ������ǰû���͵ı���(��ѡ)  
            crashUtil.sendPreviousReportsToServer();
        }
          
    }  
}  