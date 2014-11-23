package sumimakito.android.reina.lib.downloader;

import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;

public final class DownloadTask implements Runnable
 {
	private String path;
	private File saveDir;
	private FileDownloader loader;
	private Context context;
	private DownloadProgressListener downloadProgressListener;
	private int threads;

	public DownloadTask(Context ctx, String path, File saveDir, int ths, DownloadProgressListener dpl) {
		this.context = ctx;
		this.path = path;
		this.saveDir = saveDir;
		this.downloadProgressListener = dpl;
		this.threads = ths;
	}
	
	public int getFileSize(){
		if(loader!=null) return loader.getFileSize();
		else return 0;
	}

	public void exit() {
		if (loader != null)
			loader.exit();
	}
	
	public void terminate(){
		if (loader != null)
			loader.terminate();
	}

	public void run(){
		try{
			loader = new FileDownloader(context, path,
										saveDir, threads);
			loader.download(downloadProgressListener);
		}catch(Exception e){
			downloadProgressListener.onDownloadError(e);
		}
	}
}
