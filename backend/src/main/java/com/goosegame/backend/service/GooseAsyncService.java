package com.goosegame.backend.service;

import com.goosegame.backend.model.Goose;
import com.goosegame.backend.repository.GooseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GooseAsyncService {
    private final GooseRepository gooseRepository;

    @Autowired
    public GooseAsyncService(GooseRepository gooseRepository) {
        this.gooseRepository = gooseRepository;
    }

    @Async
    public void workWithGeese(List<Goose> geese) {
        for (Goose goose : geese) {
            if (goose.getHunger() == 100) {
                goose.changeHealth(-5);
            }
            goose.changeHunger(5);
            if (goose.getIll()) {
                goose.changeHealth(-5);
            }
            gooseRepository.save(goose);
        }
    }

}
