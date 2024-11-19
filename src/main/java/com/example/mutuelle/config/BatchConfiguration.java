package com.example.mutuelle.config;

import com.example.mutuelle.Dossier;
import com.example.mutuelle.processor.DossierItemProcessor;
import com.example.mutuelle.processor.ValidationProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.ExitStatus;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DossierItemProcessor dossierItemProcessor;

    @Autowired
    private ValidationProcessor validationProcessor;

    @Bean
    @StepScope
    public JsonItemReader<Dossier> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Dossier.class))
                .resource(new FileSystemResource(filePath))
                .name("dossierJsonReader")
                .build();
    }

    @Bean
    public JpaItemWriter<Dossier> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Dossier>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public CompositeItemProcessor<Dossier, Dossier> compositeProcessor() {
        List<ItemProcessor<Dossier, Dossier>> processors = new ArrayList<>();
        processors.add(validationProcessor);
        processors.add(dossierItemProcessor);

        CompositeItemProcessor<Dossier, Dossier> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(processors);

        return compositeProcessor;
    }

    @Bean
    public Step step1(JpaItemWriter<Dossier> writer, CompositeItemProcessor<Dossier, Dossier> processor) {
        return new StepBuilder("step1", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader(null))  // Reader set dynamically via job parameters
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListenerSupport() {
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        List<String> validationErrors = (List<String>) stepExecution.getExecutionContext().get("validationErrors");
                        if (validationErrors != null && !validationErrors.isEmpty()) {
                            // Log errors before sending
                            validationErrors.forEach(System.out::println);

                            // Ensure errors are stored in the JobExecution context
                            stepExecution.getJobExecution().getExecutionContext().put("validationErrors", validationErrors);
                        }
                        return stepExecution.getExitStatus();  // Retourne le ExitStatus correct
                    }
                })
                .build();
    }

    @Bean
    public Job importJob(Step step1, com.example.mutuelle.config.JobCompletionNotificationListener listener) {
        return new JobBuilder("importDossiersJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }
}
