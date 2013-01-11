package org.carelife.creader.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/** ���ԭ����Toast��ʾ��ʧ֮ǰ���ٴε���Toast.show()���н����� */
public class ToastUtil {
        private Toast toast = null;
        private Context context;
        private Handler handler = null;
        private static ToastUtil instance;
        private Runnable toastThread = new Runnable() {
                public void run() {
                        // ������count���Եر����ǲ��������µ�Toast.show()�Ľ����
//                        toast.setText(String.valueOf(showCount++) + "CustomToast");
                        toast.show();
                        // 3.3����ٶ���������Ϊ4s�Ļ����ῴ��Toast�Ƕ϶���������ʾ�ŵġ�
                        handler.postDelayed(toastThread, 3300);
                }
        };
        
        public static ToastUtil getInstance(Context context){
        	if (instance == null){
        		instance = new ToastUtil(context);
        	}
        	return instance;
        }

        private ToastUtil(Context context) {
                this.context = context;
                handler = new Handler(this.context.getMainLooper());
                toast = Toast.makeText(this.context, "", Toast.LENGTH_LONG);
        }
        
        public void setText(String text ,final long length) {
            toast.setText(text);
            showToast(length);
        }
        
        public void setText(String text) {
                toast.setText(text);
                showToast(1500);
        }
        
        public void showToast(final long length) {
        		stopToast();
                handler.post(toastThread);
                Thread timeThread = new Thread() {
                        public void run() {
                                try {
                                        Thread.sleep(length);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                                ToastUtil.this.stopToast();
                        }
                };
                timeThread.start();
        }

        public void stopToast() {
                // ɾ��Handler�����е��Դ���ȴ�����ϢԪ��ɾ��
                handler.removeCallbacks(toastThread);
                // ����������ʾ��Toast
                toast.cancel();
        }
}