package com.example.mutuelle;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DossierItemProcessor dossierItemProcessor;

    @Bean
    public JsonItemReader<Dossier> reader() {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Dossier.class))
                .resource(new ClassPathResource("dossiers.json"))
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
        processors.add(new ValidationProcessor());
        processors.add(dossierItemProcessor);

        CompositeItemProcessor<Dossier, Dossier> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(processors);

        return compositeProcessor;
    }

    @Bean
    public Step step1(JsonItemReader<Dossier> reader, JpaItemWriter<Dossier> writer, CompositeItemProcessor<Dossier, Dossier> processor) {
        return new StepBuilder("step1", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importJob(Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importDossiersJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }
}
