package com.example.mutuelle;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

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
    public JdbcBatchItemWriter<Dossier> writer() {
        return new JdbcBatchItemWriterBuilder<Dossier>()
                .dataSource(dataSource)
                .sql("INSERT INTO dossiers (nom_assure, numero_affiliation, immatriculation, lien_parente, " +
                        "montant_total_frais, prix_consultation, nombre_pieces_jointes, nom_beneficiaire, " +
                        "date_depot_dossier, montant_remboursement) " +
                        "VALUES (:nomAssure, :numeroAffiliation, :immatriculation, :lienParente, " +
                        ":montantTotalFrais, :prixConsultation, :nombrePiecesJointes, :nomBeneficiaire, " +
                        ":dateDepotDossier, :montantRemboursement)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step step1(JsonItemReader<Dossier> reader, JdbcBatchItemWriter<Dossier> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader)
                .processor(dossierItemProcessor)
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