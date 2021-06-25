package easytravel.util;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class ExtendedBukkitRunnable extends BukkitRunnable {
	
	private ArrayList<ThreadFinishListener> threadFinishListenerList = new ArrayList<ThreadFinishListener>();
	private Runnable runnable;
	private boolean isFinished = false;
	
	public ExtendedBukkitRunnable(Runnable runnableToExecute) {
		this.runnable = runnableToExecute;
	}

	@Override
	public void run() {
		this.isFinished = false;
		this.runnable.run();
		finish();
		this.isFinished = true;
	}
	
	public void addFinishListener(ThreadFinishListener f) {
		if (threadFinishListenerList.contains(f)) {
			return;
		}
		threadFinishListenerList.add(f);
	}
	
	public void removeFinishListenerList(ThreadFinishListener f) {
		threadFinishListenerList.remove(f);
	}
	
	private void finish() {
		for (ThreadFinishListener f : threadFinishListenerList) {
			f.onFinish();
		}
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}

}
