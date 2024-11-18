package com.example.mutuelle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "traitements")
public class Traitement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codeBarre;
    private Boolean existe;
    private String nomMedicament;
    private String typeMedicament;
    private Double prixMedicament;

    @ManyToOne
    @JoinColumn(name = "dossier_id")
    @JsonBackReference
    private Dossier dossier;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Boolean getExiste() {
        return existe;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    public String getTypeMedicament() {
        return typeMedicament;
    }

    public void setTypeMedicament(String typeMedicament) {
        this.typeMedicament = typeMedicament;
    }

    public Double getPrixMedicament() {
        return prixMedicament;
    }

    public void setPrixMedicament(Double prixMedicament) {
        this.prixMedicament = prixMedicament;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }
}
