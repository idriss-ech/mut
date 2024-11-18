package com.example.mutuelle;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DossierItemProcessor implements ItemProcessor<Dossier, Dossier> {

    @Autowired
    private MedicamentReferenceRepository medicamentReferenceRepository;

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        System.out.println("Processing item: " + dossier);

        // Calcul du remboursement de la consultation (exemple 70%)
        Double montantRemboursement = dossier.getPrixConsultation() * 0.7;

        // Calcul du remboursement des médicaments
        for (Traitement traitement : dossier.getTraitements()) {
            if (traitement.getExiste()) {
                // Chercher le médicament dans la base de données
                MedicamentReference medicament = medicamentReferenceRepository.findByCode(traitement.getCodeBarre());
                if (medicament != null) {
                    // Calcul du remboursement du médicament (exemple 80%)
                    montantRemboursement += traitement.getPrixMedicament() * (Double.parseDouble(medicament.getTauxRemboursement().replace("%", "")) / 100);
                }
            }
        }

        // Affecter le montant du remboursement total au dossier
        dossier.setMontantRemboursement(montantRemboursement);
        return dossier;
    }
}

