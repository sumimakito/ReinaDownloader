package sumimakito.android.reina.downloader.core;

import android.content.*;
import java.io.*;
import sumimakito.android.reina.downloader.*;
import sumimakito.android.reina.downloader.callback.*;
import sumimakito.android.reina.downloader.util.*;

public final class DownloadTask implements Runnable
{
	public static final int STATUS_PREPARED = 0x0;
	public static final int STATUS_STARTED = 0x1;
	public static final int STATUS_SUSPENDED = 0x2;
	public static final int STATUS_TERMINATED = 0x3;
	public static final int STATUS_EXCEPTION_THROWN = 0x4;
	
	private String path;
	private File saveDir;
	private FileDownloader loader;
	private Context context;
	private DownloadCallbacks downloadCallbacks;
	private int threads;
	private int currentStatus = STATUS_PREPARED;

	private String taskId;

	public DownloadTask(Context ctx, String path, File saveDir, int ths, DownloadCallbacks cbk) {
		this.context = ctx;
		this.path = path;
		this.saveDir = saveDir;
		this.downloadCallbacks = cbk;
		this.threads = ths;
		this.taskId = MD5Sum.get(path);
	}
	
	public String getTaskId(){
		return this.taskId;
	}
	
	public int getFileSize(){
		if(this.loader!=null) return this.loader.getFileSize();
		else return 0;
	}

	public void suspend() {
		if (this.loader != null){
			this.loader.suspend();
			this.currentStatus=STATUS_SUSPENDED;
		}
	}
	
	public void terminate(){
		if (this.loader != null){
			this.loader.terminate();
			this.currentStatus=STATUS_TERMINATED;
		}
	}
	
	public int getStatus(){
		return this.currentStatus;
	}

	public void run(){
		try{
			this.loader = new FileDownloader(taskId, context, path,
										saveDir, threads);
			this.loader.download(downloadCallbacks);
			this.currentStatus=STATUS_STARTED;
		}catch(Exception e){
			this.downloadCallbacks.onDownloadException(taskId, e);
			this.currentStatus=STATUS_EXCEPTION_THROWN;
		}
	}
}
