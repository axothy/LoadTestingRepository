package ru.axothy.service;

import org.springframework.stereotype.Service;
import ru.axothy.model.db.Town;
import ru.axothy.repository.TownRepository;

@Service
public class TownService {
    private final TownRepository townRepository;

    public TownService(TownRepository townRepository) {
        this.townRepository = townRepository;
    }

    public Town getTownById(Long id) {
        return townRepository.findById(id).get();
    }
}
