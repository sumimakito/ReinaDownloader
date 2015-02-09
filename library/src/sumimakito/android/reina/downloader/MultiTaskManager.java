package sumimakito.android.reina.downloader;
import android.content.*;
import java.io.*;
import java.util.*;
import sumimakito.android.reina.downloader.callback.*;
import sumimakito.android.reina.downloader.core.*;
import sumimakito.android.reina.downloader.util.*;

public class MultiTaskManager
{
	private HashMap<String, DownloadTask> mTasks;
	private DownloadCallbacks pCallbacks;

	private File defaultPath=null;

	private Context mContext;

	public MultiTaskManager(Context context, DownloadCallbacks callbacks)
	{
		this.mContext = context;
		this.mTasks = new HashMap<String, DownloadTask>();
		this.pCallbacks = callbacks;
	}

	public void setDefaultPath(File path)
	{
		this.defaultPath = path;
	}

	public String newTask(String dlUrl, File savDir, int threads, boolean startImmediately)
	{
		String taskId =MD5Sum.get(dlUrl);
		if (mTasks.containsKey(taskId))
		{
			DownloadTask task = mTasks.get(taskId);
			if (task != null)
			{
				if (task.getStatus() == DownloadTask.STATUS_EXCEPTION_THROWN
					|| task.getStatus() == DownloadTask.STATUS_TERMINATED)
				{
					mTasks.remove(taskId);
				}
			}
		}
		else
		{
			DownloadTask task = new DownloadTask(mContext, dlUrl, savDir, threads, pCallbacks);
			if (startImmediately)
			{
				new Thread(task).start();
			}
			mTasks.put(taskId, task);
		}
		return taskId;
	}

	public String newTask(String dlUrl, int threads, boolean startImmediately) throws Exception
	{
		if (defaultPath != null)
		{
			String taskId =MD5Sum.get(dlUrl);
			if (mTasks.containsKey(taskId))
			{
				DownloadTask task = mTasks.get(taskId);
				if (task != null)
				{
					if (task.getStatus() == DownloadTask.STATUS_EXCEPTION_THROWN
						|| task.getStatus() == DownloadTask.STATUS_TERMINATED)
					{
						mTasks.remove(taskId);
					}
				}
			}
			else
			{
				DownloadTask task = new DownloadTask(mContext, dlUrl, defaultPath, threads, pCallbacks);
				if (startImmediately)
				{
					new Thread(task).start();
				}
				mTasks.put(taskId, task);
			}
			return taskId;
		}
		else
		{
			throw new Exception("You have not set a default path to save downloaded files.");
		}
	}

	public void start(String taskId) throws Exception
	{
		if (mTasks.containsKey(taskId))
		{
			DownloadTask task = mTasks.get(taskId);
			if (task.getStatus() != DownloadTask.STATUS_EXCEPTION_THROWN)
			{
				if (task.getStatus() == DownloadTask.STATUS_SUSPENDED)
				{
					throw new Exception("This task is suspended, please use resume() method. (taskId:" + taskId + ")");
				}
				else if (task.getStatus() == DownloadTask.STATUS_STARTED)
				{
					throw new Exception("This task is already started. (taskId:" + taskId + ")");
				}
				else
				{
					//Prepared
					//Terminated
					new Thread(task).start();
				}
			}
			else
			{
				//This may cause exception again.
				new Thread(task).start();
			}
		}
		else
		{
			throw new Exception("No task matches the given taskId:" + taskId);
		}
	}

	public void resume(String taskId) throws Exception
	{
		if (mTasks.containsKey(taskId))
		{
			DownloadTask task = mTasks.get(taskId);
			if (task.getStatus() != DownloadTask.STATUS_EXCEPTION_THROWN)
			{
				if (task.getStatus() == DownloadTask.STATUS_SUSPENDED)
				{
					new Thread(task).start();
				}
				else if (task.getStatus() == DownloadTask.STATUS_STARTED)
				{
					throw new Exception("This task is already started. (taskId:" + taskId + ")");
				}
				else if (task.getStatus() == DownloadTask.STATUS_TERMINATED)
				{
					throw new Exception("This task is already terminated, please use start() method. (taskId:" + taskId + ")");
				}
				else
				{
					//Prepared
					throw new Exception("This task is just prepared, please use start() method to start download. (taskId:" + taskId + ")");
				}
			}
			else
			{
				throw new Exception("This task has thrown an exception, please use start() method to restart task. (taskId:" + taskId + ")");
			}
		}
		else
		{
			throw new Exception("No task matches the given taskId:" + taskId);
		}
	}

	public void suspend(String taskId) throws Exception
	{
		if (mTasks.containsKey(taskId))
		{
			DownloadTask task = mTasks.get(taskId);
			if (task.getStatus() != DownloadTask.STATUS_EXCEPTION_THROWN)
			{
				if (task.getStatus() == DownloadTask.STATUS_SUSPENDED)
				{
					throw new Exception("This task is already suspended. (taskId:" + taskId + ")");
				}
				else if (task.getStatus() == DownloadTask.STATUS_TERMINATED)
				{
					throw new Exception("This task is already terminated. (taskId:" + taskId + ")");
				}
				else if (task.getStatus() == DownloadTask.STATUS_PREPARED)
				{
					throw new Exception("This task is already terminated. (taskId:" + taskId + ")");
				}
				else
				{
					//Started
					new Thread(task).start();
				}
			}
			else
			{
				throw new Exception("This task has thrown an exception, please use start() method to restart task. (taskId:" + taskId + ")");
			}
		}
		else
		{
			throw new Exception("No task matches the given taskId:" + taskId);
		}
	}

	public void terminate(String taskId) throws Exception
	{
		if (mTasks.containsKey(taskId))
		{
			DownloadTask task = mTasks.get(taskId);
			if (task.getStatus() != DownloadTask.STATUS_EXCEPTION_THROWN)
			{
				if (task.getStatus() == DownloadTask.STATUS_PREPARED)
				{
					throw new Exception("This task is just prepared. (taskId:" + taskId + ")");
				}
				else if (task.getStatus() == DownloadTask.STATUS_TERMINATED)
				{
					throw new Exception("This task is already terminated, please use start() method. (taskId:" + taskId + ")");
				}
				else
				{
					//Started
					//Prepared
					task.terminate();
				}
			}
			else
			{
				throw new Exception("This task has thrown an exception, please use start() method to restart task. (taskId:" + taskId + ")");
			}
		}
		else
		{
			throw new Exception("No task matches the given taskId:" + taskId);
		}
	}

	public List<DownloadTask> getAllTasksList()
	{
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();
		Iterator iter = mTasks.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			list.add((DownloadTask)entry.getValue());
		}
		return list;
	}
}