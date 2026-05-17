package tech.skullprogrammer.bguard.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.repository.SupplyPointRepository;

@Service
public class SupplyPointService {

    private final SupplyPointRepository supplyPointRepository;

    @Autowired
    public SupplyPointService(SupplyPointRepository repository) {
        this.supplyPointRepository = repository;
    }

    public SupplyPoint getSupplyPointById(Long id){
        return supplyPointRepository.findById(id).orElse(null);
    }
}
