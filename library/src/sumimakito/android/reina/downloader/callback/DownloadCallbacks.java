package sumimakito.android.reina.downloader.callback;

public interface DownloadCallbacks {  
    public void onDownloadProgress(String taskId, int size);  
	public void onDownloadException(String taskId, Exception e);
	public void onDownloadFinish(String taskId);
	public void onDownloadStart(String taskId, int size);
}  
