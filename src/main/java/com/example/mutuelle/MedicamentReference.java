package com.example.mutuelle;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicaments_referentiels")  // Utilise le nom de la table existante
public class MedicamentReference {

    @Id
    private String code; // Le code-barre du médicament
    private String nom; // Nom du médicament
    private String dci1; // DCI (Dénomination Commune Internationale) du médicament
    private String dosage1; // Dosage du médicament
    private String uniteDosage1; // Unité du dosage
    private String forme; // Forme du médicament
    private String presentation; // Présentation du médicament
    private Double ppv; // Prix de vente public
    private Double ph; // Prix hors-taxe
    private Double prixBr; // Prix brut
    private String princepsGenerique; // Indique si c'est un princeps ou un générique
    private String tauxRemboursement; // Taux de remboursement, par exemple "80%"

    // Getters et Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDci1() {
        return dci1;
    }

    public void setDci1(String dci1) {
        this.dci1 = dci1;
    }

    public String getDosage1() {
        return dosage1;
    }

    public void setDosage1(String dosage1) {
        this.dosage1 = dosage1;
    }

    public String getUniteDosage1() {
        return uniteDosage1;
    }

    public void setUniteDosage1(String uniteDosage1) {
        this.uniteDosage1 = uniteDosage1;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Double getPpv() {
        return ppv;
    }

    public void setPpv(Double ppv) {
        this.ppv = ppv;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getPrixBr() {
        return prixBr;
    }

    public void setPrixBr(Double prixBr) {
        this.prixBr = prixBr;
    }

    public String getPrincepsGenerique() {
        return princepsGenerique;
    }

    public void setPrincepsGenerique(String princepsGenerique) {
        this.princepsGenerique = princepsGenerique;
    }

    public String getTauxRemboursement() {
        return tauxRemboursement;
    }

    public void setTauxRemboursement(String tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }
}
