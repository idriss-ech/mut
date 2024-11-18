package com.example.mutuelle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dossiers")
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomAssure;
    private String numeroAffiliation;
    private String immatriculation;
    private String lienParente;
    private Double montantTotalFrais;
    private Double prixConsultation;
    private Integer nombrePiecesJointes;
    private String nomBeneficiaire;
    private String dateDepotDossier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dossier", orphanRemoval = true)
    @JsonManagedReference
    private List<Traitement> traitements = new ArrayList<>();

    private Double montantRemboursement;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAssure() {
        return nomAssure;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public String getNumeroAffiliation() {
        return numeroAffiliation;
    }

    public void setNumeroAffiliation(String numeroAffiliation) {
        this.numeroAffiliation = numeroAffiliation;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getLienParente() {
        return lienParente;
    }

    public void setLienParente(String lienParente) {
        this.lienParente = lienParente;
    }

    public Double getMontantTotalFrais() {
        return montantTotalFrais;
    }

    public void setMontantTotalFrais(Double montantTotalFrais) {
        this.montantTotalFrais = montantTotalFrais;
    }

    public Double getPrixConsultation() {
        return prixConsultation;
    }

    public void setPrixConsultation(Double prixConsultation) {
        this.prixConsultation = prixConsultation;
    }

    public Integer getNombrePiecesJointes() {
        return nombrePiecesJointes;
    }

    public void setNombrePiecesJointes(Integer nombrePiecesJointes) {
        this.nombrePiecesJointes = nombrePiecesJointes;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public void setNomBeneficiaire(String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public String getDateDepotDossier() {
        return dateDepotDossier;
    }

    public void setDateDepotDossier(String dateDepotDossier) {
        this.dateDepotDossier = dateDepotDossier;
    }

    public List<Traitement> getTraitements() {
        return traitements;
    }

    public void setTraitements(List<Traitement> traitements) {
        this.traitements = traitements;
    }

    public Double getMontantRemboursement() {
        return montantRemboursement;
    }

    public void setMontantRemboursement(Double montantRemboursement) {
        this.montantRemboursement = montantRemboursement;
    }
}
