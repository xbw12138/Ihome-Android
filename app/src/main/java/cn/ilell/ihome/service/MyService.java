package cn.ilell.ihome.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import cn.ilell.ihome.MonitorActivity;
import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseData;

/**
 * Created by lhc35 on 2016/4/17.
 */
public class MyService extends Service {
    protected String serverIP = BaseData.IP;  //static final
    protected int serverPort = 5678;
    protected Socket serviceSocket;
    protected static DataOutputStream socketOut = null;
    protected static DataInputStream socketIn = null;
    protected String recvMsg;
    protected String sendMsg;
    /** Notification构造器 */
    protected NotificationCompat.Builder mBuilder;
    /** Notification管理 */
    protected NotificationManager mNotificationManager;
    /** Notification的ID */
    protected int notifyId_WARNING = 102;
    protected int notifyId_MESSAGE = 101;

    /**
     * 更新的回调接口
     */
    protected OnProgressListener onProgressListener;
    /**
     * 注册回调接口的方法，供外部调用
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    /** 显示通知栏点击跳转到指定Activity */
    public void showIntentActivityNotify(String title,String content,Class orderClass){
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(title);
        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(this, orderClass);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId_MESSAGE, mBuilder.build());
    }
    /** 显示常驻通知栏 */
    public void showCzNotify(String title,String content){
//		Notification mNotification = new Notification();//为了兼容问题，不用该方法，所以都采用BUILD方式建立
//		Notification mNotification  = new Notification.Builder(this).getNotification();//这种方式已经过时
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//		//PendingIntent 跳转动作
        Intent resultIntent = new Intent(this, MonitorActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,resultIntent, 0);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
        //设置通知  消息  图标
        mNotification.icon = R.mipmap.ic_launcher;
        //在通知栏上点击此通知后自动清除此通知
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;//FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
        //设置显示通知时的默认的发声、震动、Light效果
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        //设置发出消息的内容
        mNotification.tickerText = title;
        //设置发出通知的时间
        mNotification.when=System.currentTimeMillis();
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
//		mNotification.setLatestEventInfo(this, "常驻测试", "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息  ,这个方法现在已经不用了
        mNotificationManager.notify(notifyId_WARNING, mNotification);
    }
    /**
     * 初始化要用到的系统服务
     */
    protected void initService() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    /** 初始化通知栏 */
    protected void initNotify(){
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("")
                .setContentText("")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setTicker("")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher);
    }
    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性:
     * 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

