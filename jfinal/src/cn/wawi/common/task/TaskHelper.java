package cn.wawi.common.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskHelper implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("5分钟定时任务......");		
	}

}
