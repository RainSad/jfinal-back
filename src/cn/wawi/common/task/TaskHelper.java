package cn.wawi.common.task;


public class TaskHelper implements Runnable{

	@Override
	public void run() {
		System.out.println("每隔10秒执行任务......");
	}

}
