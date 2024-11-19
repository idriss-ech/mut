package com.example.mutuelle.controller;


import com.example.mutuelle.Dossier;
import com.example.mutuelle.Service.DossierService;
import com.example.mutuelle.repesitory.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DossierController {

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierService dossierService;
    @GetMapping("/dossiers")
    public List<Dossier> getAllDossiers() {
        return dossierRepository.findAll();
    }


    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllDossiers() {
            try {
                dossierService.deleteAllDossiers();
                return new ResponseEntity<>("Tous les dossiers ont été supprimés avec succès.", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Une erreur s'est produite lors de la suppression des dossiers.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


}
