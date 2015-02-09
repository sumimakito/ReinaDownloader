ReinaDownloader
===============

Multi-thread downloading library for Android projects.

#### Todo List:

* 🌕 Multi-thread downloading. 
* 🌕 Broken-point continuingly-transferring support. 
* 🌕 Callback interfaces. 
* 🌕 Handling Exceptions. 
* 🌕 Multi-task management. 
* 🌑 Synchronized communication between threads.
* 🌑 --More detailed callback interfaces.--

#### What's new in v1.1.0?

* Multi-task Manager: Helps you manage tasks efficiently.
* Optimized code structure.

#### How to update to v1.1.0?

> Because the code structure is changed, you must update your code in order to use the new version library.

* Change imported package name: sumimakito.android.reina.lib.downloader → sumimakito.android.reina.downloader

* Change listener: DownloadProgressListener → DownloadCallbacks

* DownloadCallbacks return a String taskId in each callback method.

* If you still want to use single task downloader, the DownloadTask is located in package: sumimakito.android.reina.downloader.core

* If you want to try out the new Multi-task Manager, find your way in usage section. :)

#### Known bug

> Too many threads might cause unpredictable exceptions such as losing track of threads or even worse.

* Please keep the number of your download threads less than eight(five is recommended).

#### Attention

* If you initialized the DownloadTask/Multi-task Manager in an Activity, you will lose communication with downloading tasks after the Activity destroied.

> To solve this issue, we recommend you to create a List<DownloadTask> (or create Multi-task Manager in Service) to save these instances inside.

#### Usages

##### Permissions Requirement

*Declare these permissions in Manifest first.*

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
<uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

##### Import

```java
import sumimakito.android.reina.downloader.*;
```

##### Get Instance

* Multi-task Manager:

 * Get instance
 ```java
 MultiTaskManager mtm = new MultiTaskManager(context, new DownloadCallbacks(){
			 @Override
 			public void onDownloadProgress(String taskId, int size)
	 		{
 				// TODO: Implement this method
	 		}
 			@Override
	 		public void onDownloadException(String taskId, Exception e)
 			{
		 		// TODO: Implement this method
 			}
 			@Override
 			public void onDownloadFinish(String taskId)
 			{
	 			// TODO: Implement this method
 			}
		 	@Override
 			public void onDownloadStart(String taskId, int size)
	 		{
 				// TODO: Implement this method
	 		}
	 	});
 ```

 * APIs
  * Set default download path
  ```java
  public void setDefaultPath(File path);
  ```
  
  * New task
  ```java
  public String newTask(String dlUrl, File savDir, int threadNum, boolean startImmediately);
  ```
  >If you have set a default download path, you can use the code below to add new tasks.
  ```java
  public String newTask(String dlUrl, int threadNum, boolean startImmediately);
  ```
   * dlUrl: Url to download file from.
   * savDir: Where should downloaded file save to.(This can be ignored when a default download path is set.)
   * threadNum: How many threads will be used for download.
   * startImmediately: If true, task will start automatically after added.
   > This method will return a String taskId, you can use taskId to manage a single task.
  
  * Manage a single task.
  ```java
  //Start a task
  public void start(String taskId);
  //Resume a task
  public void resume(String taskId);
  //Suspend a task
  public void start(String taskId);
  //Terminate a task
  public void start(String taskId);
  ```
 > All method called from Multi-task Manager need to be surrounded in try/catch block. You can get all detailed exception information using exception.getMessage().
 
* Single task:

 * Get instance
```java
DownloadTask task = new DownloadTask(this, dlUrl, savDir, threads, new DownloadProgressListener(){
	@Override
	public void onDownloadStart(int length)
	{
		//Call when download is started.
		Log.i("[dpl_callback]", "------download started!");
		Log.i("[dpl_callback]", "------file length:" + length);
	}
	@Override
	public void onDownloadFinish()
	{
		//Call when download is finished.
		Log.i("[dpl_callback]", "------download finished!");
	}
	@Override
	public void onDownloadError(Exception e)
	{
		//Call when an exception is thrown.
		Log.e("[dpl_callback]", "------download error:" + e.getMessage());
	}
	@Override
	public void onDownloadProgress(int size)
	{
		//Call when download progress is changed.
		Log.i("[dpl_callback]", "------downloaded size:" + size);
	}
});
```

> Examples below are for single task management.

##### Start download

```java
new Thread(task).start();
```

> NOTICE: You can get the total size of the file inside the callback interface "onDownloadStart(int size)".

##### Pause

```java
task.exit();
```

##### Resume

*Same as starting a new download task. You can also use an exist instance to resume downloading progress.*

```java
new Thread(task).start();
```

*If you had already TERMINATED the task, downloading will start from scratch.*

##### Terminate

```java
task.terminate();
```

#### Download Sample APK(Old version)

* Download from [Dropbox]( https://www.dropbox.com/s/3h99f578dongraw/ReinaDownloaderDemo.apk).
* Download from [Google Drive](https://docs.google.com/file/d/0B_-0A4yjEnvMWDh2S0MzbzZkUm8/edit?usp=docslist_api).

###### Copyright(c)2014-2015 Sumi Makito.
###### Licensed under GNU GPL v3. Read [License](LISENSE).
###### Blog: http://w.keep.moe/
###### About myself: http://me.keep.moe/