package com.example.mutuelle.repesitory;


import com.example.mutuelle.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraitementRepository extends JpaRepository<Traitement, Long> {
}
