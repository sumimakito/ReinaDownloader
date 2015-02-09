package sumimakito.android.reina.downloader.core;

import android.content.*;
import android.net.*;
import android.util.*;
import java.io.*;
import java.net.*;
import sumimakito.android.reina.downloader.*;

public class DownloadThread extends Thread {
	private static final String TAG = "DownloadThread";
	private File saveFile;
	private URL downUrl;
	private int block;
	private int threadId = -1;
	private int downLength;
	private boolean finish = false;
	private FileDownloader downloader;
	private boolean isError=false;

	private ConnectivityManager connManager;

	public DownloadThread(FileDownloader downloader, URL downUrl,
						  File saveFile, int block, int downLength, int threadId, Context ctx) {
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downLength = downLength;
		connManager= (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE); 
	}

	@Override
	public void run() {
		if (downLength < block) {
			try {
				HttpURLConnection http = (HttpURLConnection) downUrl
					.openConnection();
				http.setConnectTimeout(5000);
				http.setRequestMethod("GET"); 
				http.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer", downUrl.toString());
				http.setRequestProperty("Charset", "UTF-8");
				int startPos = block * (threadId - 1) + downLength;
				int endPos = block * threadId - 1;
				http.setRequestProperty("Range", "bytes=" + startPos + "-"
										+ endPos);
				http.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				http.setRequestProperty("Connection", "Keep-Alive"); 
				InputStream inStream = http.getInputStream();
				byte[] buffer = new byte[1024];
				int offset = 0;
				print("Thread-" + this.threadId
					  + " download from pos:" + startPos);
				RandomAccessFile threadfile = new RandomAccessFile(
					this.saveFile, "rwd");
				threadfile.seek(startPos);
				while (!isError && !downloader.getExit()
					   && (offset = inStream.read(buffer, 0, 1024)) != -1) {
					if(connManager.getActiveNetworkInfo()!=null){
						if(!connManager.getActiveNetworkInfo().isConnected()){
							throw new Exception("Network loss.");
						}
					}else{
						throw new Exception("Network loss.");
					}
					
					threadfile.write(buffer, 0, offset);
					downLength += offset;
					downloader.update(this.threadId, downLength);
					downloader.append(offset); 
				}
				threadfile.close();
				inStream.close();
				print("Thread-" + this.threadId + " download finished.");
				this.finish = true;
			} catch (Exception e) {
				isError=true;
				this.downLength = -1;
				print("Thread-" + this.threadId + " exception:" + e);
			}
		}
	}

	private static void print(String msg) {
		Log.i(TAG, msg);
	}
	
	public boolean isError(){
		return isError;
	}

	public boolean isFinish() {
		return finish;
	}

	public long getDownLength() {
		return downLength;
	}
}

