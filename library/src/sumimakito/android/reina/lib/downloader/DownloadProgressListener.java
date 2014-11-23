package sumimakito.android.reina.lib.downloader;

public interface DownloadProgressListener {  
    public void onDownloadProgress(int size);  
	public void onDownloadError(Exception e);
	public void onDownloadFinish();
	public void onDownloadStart(int size);
}  
