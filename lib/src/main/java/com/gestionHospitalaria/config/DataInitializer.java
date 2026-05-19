package com.gestionHospitalaria.config;

import com.gestionHospitalaria.entity.Enfermero;
import com.gestionHospitalaria.entity.Farmaceutico;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Recepcionista;
import com.gestionHospitalaria.repository.EnfermeroRepository;
import com.gestionHospitalaria.repository.FarmaceuticoRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import com.gestionHospitalaria.repository.RecepcionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @Autowired
    private FarmaceuticoRepository farmaceuticoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        encodePasswordsPacientes();
        encodePasswordsMedicos();
        encodePasswordsRecepcionistas();
        encodePasswordsEnfermeros();
        encodePasswordsFarmaceuticos();
    }

    private void encodePasswordsPacientes() {
        for (Paciente p : pacienteRepository.findAll()) {
            if (p.getPassword() != null && !p.getPassword().startsWith("$2")) {
                p.setPassword(passwordEncoder.encode(p.getPassword()));
                pacienteRepository.save(p);
            }
        }
    }

    private void encodePasswordsMedicos() {
        for (Medico m : medicoRepository.findAll()) {
            if (m.getPassword() != null && !m.getPassword().startsWith("$2")) {
                m.setPassword(passwordEncoder.encode(m.getPassword()));
                medicoRepository.save(m);
            }
        }
    }

    private void encodePasswordsRecepcionistas() {
        for (Recepcionista r : recepcionistaRepository.findAll()) {
            if (r.getPassword() != null && !r.getPassword().startsWith("$2")) {
                r.setPassword(passwordEncoder.encode(r.getPassword()));
                recepcionistaRepository.save(r);
            }
        }
    }

    private void encodePasswordsEnfermeros() {
        for (Enfermero e : enfermeroRepository.findAll()) {
            if (e.getPassword() != null && !e.getPassword().startsWith("$2")) {
                e.setPassword(passwordEncoder.encode(e.getPassword()));
                enfermeroRepository.save(e);
            }
        }
    }

    private void encodePasswordsFarmaceuticos() {
        for (Farmaceutico f : farmaceuticoRepository.findAll()) {
            if (f.getPassword() != null && !f.getPassword().startsWith("$2")) {
                f.setPassword(passwordEncoder.encode(f.getPassword()));
                farmaceuticoRepository.save(f);
            }
        }
    }
}