package cn.ilell.ihome.io;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.StrictMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//import android.text.InputType;
//import android.view.MotionEvent;
//import android.view.View.OnTouchListener;
@SuppressLint
({ "NewApi", "HandlerLeak" })
public class AudioClient {

	private String ser_local_ip = "192.168.0.82";  //static final
	private String ser_remote_ip = "115.159.23.237";  //static final
	private String ser_ip = "115.159.23.237";  //static final
	private DatagramSocket socket = null;
	private boolean running;

	//使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
	private InetAddress serverAddress = null;
	private int ser_port = 8081;
	private int listen_port = 18082;
	private long currentID;	//当前接收到的包的ID
	private Thread mThread_sock_to_audio;
	private Thread mThread_audio_to_sock;
	private int result;

	static final int frequency = 22050;
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
	static final int EncodingBitRate = AudioFormat.ENCODING_PCM_16BIT;
	int recBufSize,playBufSize;
	AudioRecord audioRecord;
	AudioTrack audioTrack;

	   
	public AudioClient() {


		//以下是所加的代码
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			 .detectDiskReads()
			 .detectDiskWrites()
			 .detectNetwork()
			 .penaltyLog()
			 .build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			 .detectLeakedSqlLiteObjects()
			 .detectLeakedClosableObjects()
			 .penaltyLog()
			 .penaltyDeath()
			 .build());


	}

	public int autoStart() {
		createAudioRecord();
		createAudioTrack();
		currentID = 0;
		//ser_ip = ser_local_ip;
		startAudioClient();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			if (currentID == 0) {
				try {
					serverAddress = InetAddress.getByName(ser_remote_ip);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			else
				return 1;	//connected local server
		}
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			if (currentID == 0) {
				running = false;
				/*try {
					mThread_audio_to_sock.stop();
					mThread_sock_to_audio.stop();
				} catch (Error error) {
					error.printStackTrace();
				}*/
				audioRecord.stop();
				audioTrack.stop();
				socket.close();
				return 0;	//connected failed
			}
			else
				return 2;	//connected remote server
		}
	}

	public void startAudioClient() {
		//IP = data_ip;
		//port = data_port;
		//创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
		//还需要使用这个端口号来receive，所以一定要记住
		try {
			socket = new DatagramSocket(listen_port);
			serverAddress = InetAddress.getByName(ser_local_ip);
		}  catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		mThread_sock_to_audio = new Thread(mRunable_sock_to_audio);
		mThread_sock_to_audio.start();
		mThread_audio_to_sock = new Thread(mRunable_audio_to_sock);
		mThread_audio_to_sock.start();
	}

	private Runnable mRunable_audio_to_sock = new Runnable() {
		@Override
		public void run() {
			Package pack = new Package();
			byte[] buf_audio = new byte[Package.BUFLEN];
			audioRecord.startRecording();//开始录制
			running = true;
			while (true) {
				synchronized (this) {
					if (running == false) {
						break;
					}
				}
				try {
					if (audioRecord.getRecordingState() == audioRecord.RECORDSTATE_STOPPED)
						return; //录音停止，退出线程
					result = audioRecord.read(buf_audio, 0, Package.BUFLEN); //read from audio
				} catch (Error error) {
					error.printStackTrace();
				}

				if(AudioRecord.ERROR_INVALID_OPERATION != result) {
					pack.setData(buf_audio);
					pack.setId(pack.getId()+1);
					//System.out.println("audio to sock:"+pack.getId());
					//创建一个DatagramPacket对象，用于发送数据。
					//参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号
					DatagramPacket dataPacket = new DatagramPacket(pack.getPackageByte(), pack.getPackageByte().length,
							serverAddress, ser_port);
					synchronized (this){
						if (socket.isClosed())
							break;
						try {
							socket.send(dataPacket);
						} catch (IOException e) {
							e.printStackTrace();
							break;
						}
					}
				}
			}
		}
	};
	private Runnable mRunable_sock_to_audio = new Runnable() {
		@Override
		public void run() {
			Package pack = new Package();
			byte[] buf_sock = new byte[Package.BUFLEN+8];
			audioTrack.play();//开始播放
			running = true;
			while (true) {
				synchronized (this) {
					if (running == false)
						break;
				}
				//参数一:要接受的data 参数二：data的长度
				DatagramPacket packet = new DatagramPacket(buf_sock, buf_sock.length);
				synchronized (this) {
					if (socket.isClosed())
						break;
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
						if (running == false)
							break;
					}
				}

				pack.analysisBuf(buf_sock);
				//System.out.println("sock to audio:"+pack.getId());
				synchronized (this) {
					if (pack.getId() > currentID) {
						currentID = pack.getId();
						try {
							if (audioTrack.getPlayState() == audioTrack.PLAYSTATE_STOPPED)
								return;	//播放停止，退出线程
							audioTrack.write(pack.getData(),0, Package.BUFLEN);
						}catch (Error error) {
							error.printStackTrace();
						}
					}
				}
			}
		}
	};

	public void createAudioRecord(){
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, EncodingBitRate);


		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, EncodingBitRate, recBufSize);
	}	//创建录音

	public void createAudioTrack(){
		playBufSize=AudioTrack.getMinBufferSize(frequency,
				channelConfiguration, EncodingBitRate);


		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelConfiguration, EncodingBitRate,
				playBufSize, AudioTrack.MODE_STREAM);
	}	//创建放音

	public void stop() {
		synchronized (this) {
			if (running){
				audioRecord.stop();
				audioTrack.stop();
				socket.close();
			}
		}
	}

}