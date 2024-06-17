package com.ssd.sthub.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final Tasklet tasklet;

    // 매일 12시 배치 작업 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void runJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        log.info("Scheduling Start");
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();
        jobLauncher.run(batchConfig.processComplaintsJob(jobRepository, batchConfig.processComplaintsStep(jobRepository,tasklet , transactionManager)),
                jobParameters);
    }
}
