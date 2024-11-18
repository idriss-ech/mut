package com.example.mutuelle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentReferenceRepository extends JpaRepository<MedicamentReference, String> {
    MedicamentReference findByCode(String code);
}