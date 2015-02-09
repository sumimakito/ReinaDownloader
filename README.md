ReinaDownloader
===============

Multi-thread downloading library for Android projects.

> 

#### Todo List:

[x] Multi-thread downloading. 
[x] Broken-point continuingly-transferring support. 
[x] Callback interfaces. 
[x] Handling Exceptions. 
[] Multi-task management. 
[] Synchronized communication between threads.
[] More detailed callback interfaces.

#### KNOWN BUG

> Too many threads might cause unpredictable exceptions such as losing track of threads or even worse.

* Please keep the number of your download threads less than eight(five is recommended).

#### NOTICE

* If you initialized the DownloadTask in an Activity, you will lose communication with downloading tasks after the Activity destroied.

> To solve this issue, we recommend you to create a List<DownloadTask> to save these instances inside.

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
import sumimakito.android.reina.lib.downloader.*;
```

##### Get Instance

```java
private DownloadTask task;

...

task = new DownloadTask(this, dlUrl, savDir, threads, new DownloadProgressListener(){
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

#### Download Sample APK

* Download from [Dropbox]( https://www.dropbox.com/s/3h99f578dongraw/ReinaDownloaderDemo.apk).
* Download from [Google Drive](https://docs.google.com/file/d/0B_-0A4yjEnvMWDh2S0MzbzZkUm8/edit?usp=docslist_api).

###### &copy;2014-2015 SumiMakito.

###### About Myself: http://me.keep.moe/
