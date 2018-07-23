package io.github.externschool.planner.service;

import io.github.externschool.planner.dto.PersonDTO;
import io.github.externschool.planner.entity.User;
import io.github.externschool.planner.entity.VerificationKey;
import io.github.externschool.planner.entity.profile.Person;
import io.github.externschool.planner.repository.VerificationKeyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationKeyServiceImpl implements VerificationKeyService {
    private VerificationKeyRepository keyRepository;

    public VerificationKeyServiceImpl(VerificationKeyRepository verificationKeyRepository) {
        this.keyRepository = verificationKeyRepository;
    }

    @Override
    public VerificationKey findKeyById(Long id) {
        return keyRepository.getById(id);
    }

    @Override
    public List<VerificationKey> findAll() {
        return keyRepository.findAll();
    }

    @Override
    public VerificationKey saveOrUpdateKey(VerificationKey verificationKey) {
        return keyRepository.save(verificationKey);
    }

    @Override
    public void deleteById(Long id) {
        keyRepository.deleteById(id);
    }

    @Override
    public PersonDTO setNewKeyToDTO(PersonDTO personDTO) {
        VerificationKey newKey = keyRepository.save(new VerificationKey());
        VerificationKey oldKey = personDTO.getVerificationKey();
        if (oldKey != null) {
            User oldUser = oldKey.getUser();
            Person oldPerson = oldKey.getPerson();
            if (oldUser != null) {
                oldUser.removeVerificationKey();
                oldUser.addVerificationKey(newKey);
            }
            if (oldPerson != null) {
                oldPerson.removeVerificationKey();
                oldPerson.addVerificationKey(newKey);
            }
            deleteById(oldKey.getId());
        }
        personDTO.setVerificationKey(newKey);

        return personDTO;
    }
}
