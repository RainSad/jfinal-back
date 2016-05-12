package cn.wawi.common.task;

public class TaskHelper1 implements Runnable{

	@Override
	public void run() {
		System.out.println("每隔2分钟执行任务......");
	}
}
