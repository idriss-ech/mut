package com.example.mutuelle;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ValidationProcessor implements ItemProcessor<Dossier, Dossier> {

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        try {
            // Validation des informations essentielles
            if (!StringUtils.hasText(dossier.getNomAssure())) {
                throw new Exception("Nom de l'assuré manquant");
            }
            if (!StringUtils.hasText(dossier.getNumeroAffiliation())) {
                throw new Exception("Numéro d'affiliation manquant");
            }
            if (dossier.getPrixConsultation() == null || dossier.getPrixConsultation() <= 0) {
                throw new Exception("Prix de la consultation invalide");
            }
            if (dossier.getMontantTotalFrais() == null || dossier.getMontantTotalFrais() <= 0) {
                throw new Exception("Montant total des frais invalide");
            }
            if (dossier.getTraitements() == null || dossier.getTraitements().isEmpty()) {
                throw new Exception("Liste des traitements manquante");
            }
        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
            return null; // Return null to skip this item
        }

        return dossier;
    }
}
