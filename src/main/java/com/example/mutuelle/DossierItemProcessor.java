package com.example.mutuelle;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DossierItemProcessor implements ItemProcessor<Dossier, Dossier> {

    @Autowired
    private MedicamentReferenceRepository medicamentReferenceRepository;

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        try {
            System.out.println("Processing item: " + dossier);

            // Calcul du remboursement de la consultation (exemple 70%)
            Double montantRemboursement = dossier.getPrixConsultation() * 0.7;

            // Calcul du remboursement des médicaments
            for (Traitement traitement : dossier.getTraitements()) {
                traitement.setDossier(dossier); // Assure que chaque traitement a une référence au dossier
                if (traitement.getExiste()) {
                    MedicamentReference medicament = medicamentReferenceRepository.findByCode(traitement.getCodeBarre());
                    if (medicament != null) {
                        montantRemboursement += medicament.getPpv() * (Double.parseDouble(medicament.getTauxRemboursement().replace("%", "")) / 100);
                    } else {
                        throw new Exception("Médicament référentiel non trouvé pour le code barre : " + traitement.getCodeBarre());
                    }
                }
            }

            // Arrondir le montant de remboursement à deux décimales
            BigDecimal montantRemboursementArrondi = new BigDecimal(montantRemboursement).setScale(2, RoundingMode.HALF_UP);

            // Convertir en double sans erreurs de précision
            double montantRemboursementFinal = montantRemboursementArrondi.doubleValue();

            // Affecter le montant du remboursement total au dossier
            dossier.setMontantRemboursement(montantRemboursementFinal);
        } catch (Exception e) {
            System.err.println("Processing error: " + e.getMessage());
            return null; // Return null to skip this item
        }

        return dossier;
    }
}
