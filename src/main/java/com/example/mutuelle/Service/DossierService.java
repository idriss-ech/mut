package com.example.mutuelle.Service;


import com.example.mutuelle.Dossier;
import com.example.mutuelle.repesitory.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DossierService {

    @Autowired
    private DossierRepository dossierRepository;

    public List<Dossier> getAllDossiers() {
        return dossierRepository.findAll();
    }

    public void deleteAllDossiers() { dossierRepository.deleteAll(); }
}
