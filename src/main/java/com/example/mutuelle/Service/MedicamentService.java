package com.example.mutuelle.Service;


import com.example.mutuelle.MedicamentReference;
import com.example.mutuelle.repesitory.MedicamentReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentService {

    @Autowired
    private MedicamentReferenceRepository medicamentReferenceRepository;

    public List<MedicamentReference> getAllMedicaments() {
        return medicamentReferenceRepository.findAll();
    }
}

