package com.example.mutuelle.repesitory;


import com.example.mutuelle.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long> {
    boolean existsByNumeroAffiliation(String numeroAffiliation);
}

