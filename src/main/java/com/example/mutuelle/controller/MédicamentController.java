package com.example.mutuelle.controller;


import com.example.mutuelle.MedicamentReference;
import com.example.mutuelle.repesitory.MedicamentReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MédicamentController {

    @Autowired
    private MedicamentReferenceRepository medicamentReferenceRepository;

    @GetMapping("/medicaments")
    public List<MedicamentReference> getAllMedicaments() {
        return medicamentReferenceRepository.findAll();
    }
}
