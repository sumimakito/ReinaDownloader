ReinaDownloader
===============

Multi-thread downloading library for Android projects.

#### Todo List:

* Multi-thread downloading. √
* Broken-point continuingly-transferring support. √
* Callback interfaces. √
* Handling Exceptions. √
* Multi-task management. 

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

##### Pause

```java
task.exit();
```

##### Terminate

```java
task.terminate();
```

#### Download Sample APK

* [Dropbox](https://www.dropbox.com/s/3h99f578dongraw/ReinaDownloaderDemo.apk)

(c)2014 SumiMakito.

About Myself: http://me.keep.moe/