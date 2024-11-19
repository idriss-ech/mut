package com.example.mutuelle.repesitory;


import com.example.mutuelle.MedicamentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentReferenceRepository extends JpaRepository<MedicamentReference, Long> {
    MedicamentReference findByCode(String codeBarre);
}
