package com.ssd.sthub.batch;

import com.ssd.sthub.service.GroupBuyingService;
import com.ssd.sthub.service.SecondhandService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final SecondhandService secondhandService;
    private final GroupBuyingService groupBuyingService;

    @Bean
    public Job processComplaintsJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("processComplaintsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step processComplaintsStep(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processComplaintsStep", jobRepository)
                .tasklet(tasklet, transactionManager).build();
    }

    @Bean
    public Tasklet tasklet() {
        return ((contribution, chunkContext) -> {
            secondhandService.deleteSecondhandByStatus();
            groupBuyingService.deleteGroupBuyingByStatus();
            return RepeatStatus.FINISHED;
        });
    }
}
