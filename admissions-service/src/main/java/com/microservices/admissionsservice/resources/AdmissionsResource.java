package com.microservices.admissionsservice.resources;

import com.microservices.commons.model.DiseasesList;
import com.microservices.commons.model.EmployeesList;
import com.microservices.commons.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admissions")
public class AdmissionsResource {

    @Autowired
    private RestTemplate restTemplate;

    //A hardcoded list of patients
    List<Patient> patients = Arrays.asList(
            new Patient("P1", "Joseph", "Nigerian"),
            new Patient("P2", "Gabor", "Hungarian")
    );

    //getPatients() returns a list of patients
    @RequestMapping("/patients")
    public List<Patient> getPatients() {
        return patients;
    }

    //getPatientById() returns a patient with a given Id
    @RequestMapping("/patients/{Id}")
    public Patient getPatientById(@PathVariable("Id") String Id) {
        Patient p = patients.stream()
                .filter(patient -> Id.equals(patient.getId()))
                .findAny()
                .orElse(null);
        return p;
    }

    //getPhysicians calls the hr-service microservice to get list of physicians
    @RequestMapping("/physicians")
    public EmployeesList getPhysicians() {
        EmployeesList physicians =
                restTemplate.getForObject("http://hr-service/hr/employees", EmployeesList.class);
        return physicians;
    }

    //getDiseases calls the pathology-service to get list of diseases
    @RequestMapping("/diseases")
    public DiseasesList getDiseases() {
        DiseasesList diseases =
                restTemplate.getForObject("http://pathology-service/pathology/diseases", DiseasesList.class);
        return diseases;
    }
}
