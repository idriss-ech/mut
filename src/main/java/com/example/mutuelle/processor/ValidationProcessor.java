package com.example.mutuelle.processor;

import com.example.mutuelle.Dossier;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationProcessor implements ItemProcessor<Dossier, Dossier> {

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        List<String> validationErrors = new ArrayList<>();

        if (!StringUtils.hasText(dossier.getNomAssure())) {
            validationErrors.add("Nom de l'assuré manquant pour le dossier avec le numéro d'affiliation: " +
                    (dossier.getNumeroAffiliation() != null ? dossier.getNumeroAffiliation() : "[Numéro d'affiliation manquant]"));
        }
        if (!StringUtils.hasText(dossier.getNumeroAffiliation())) {
            validationErrors.add("Numéro d'affiliation manquant pour le dossier: " +
                    (dossier.getNomAssure() != null ? dossier.getNomAssure() : "[Nom de l'assuré manquant]"));
        }
        if (dossier.getPrixConsultation() == null || dossier.getPrixConsultation() <= 0) {
            validationErrors.add("Prix de la consultation invalide pour le dossier avec le numéro d'affiliation: " +
                    (dossier.getNumeroAffiliation() != null ? dossier.getNumeroAffiliation() : "[Numéro d'affiliation manquant]"));
        }
        if (dossier.getMontantTotalFrais() == null || dossier.getMontantTotalFrais() <= 0) {
            validationErrors.add("Montant total des frais invalide pour le dossier avec le numéro d'affiliation: " +
                    (dossier.getNumeroAffiliation() != null ? dossier.getNumeroAffiliation() : "[Numéro d'affiliation manquant]"));
        }
        if (dossier.getTraitements() == null || dossier.getTraitements().isEmpty()) {
            validationErrors.add("Liste des traitements manquante pour le dossier avec le numéro d'affiliation: " +
                    (dossier.getNumeroAffiliation() != null ? dossier.getNumeroAffiliation() : "[Numéro d'affiliation manquant]"));
        }

        if (!validationErrors.isEmpty()) {
            StepContext stepContext = StepSynchronizationManager.getContext();
            List<String> existingErrors = (List<String>) stepContext.getStepExecution().getExecutionContext().get("validationErrors");
            if (existingErrors == null) {
                existingErrors = new ArrayList<>();
            }
            existingErrors.addAll(validationErrors);
            stepContext.getStepExecution().getExecutionContext().put("validationErrors", existingErrors);
            return null; // Skip this item if validation fails
        }

        return dossier;
    }
}
