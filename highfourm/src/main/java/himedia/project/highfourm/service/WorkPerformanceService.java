package himedia.project.highfourm.service;

import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.WorkPerformanceDTO;
import himedia.project.highfourm.dto.WorkPerformanceListDTO;
import himedia.project.highfourm.entity.MaterialStock;
import himedia.project.highfourm.entity.ProductionPlan;
import himedia.project.highfourm.entity.RequiredMaterial;
import himedia.project.highfourm.entity.WorkPerformance;
import himedia.project.highfourm.repository.MaterialStockRepository;
import himedia.project.highfourm.repository.ProductionPlanRepository;
import himedia.project.highfourm.repository.RequiredMaterialRepository;
import himedia.project.highfourm.repository.WorkPerformanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkPerformanceService {
	private final WorkPerformanceRepository workPerformanceRepository;
	private final ProductionPlanRepository productionPlanRepository;
	private final MaterialStockRepository materialStockRepository;
	private final RequiredMaterialRepository requiredMaterialRepository;
	
	public List<WorkPerformanceListDTO> findList() {
		List<WorkPerformanceListDTO> resultList = workPerformanceRepository.findList();
		return resultList;
	}
	public List<WorkPerformanceDTO> findAll() {
		List<WorkPerformanceDTO> resultList = workPerformanceRepository.findAll()
				.stream().map(workPerformance -> 
					workPerformance.toWorkPerformanceDTO()).collect(Collectors.toList());
		return resultList;
	}
	
	public List<WorkPerformanceListDTO> findByProductionPlanId(String productionPlanId) {
		List<WorkPerformanceListDTO> resultList = workPerformanceRepository.findByProductionPlanId(productionPlanId);
		return resultList;
	}
	
	public List<WorkPerformanceListDTO> findByManager(String manager) {
		List<WorkPerformanceListDTO> resultList = workPerformanceRepository.findByManager(manager);
		return resultList;
	}
	
	public List<WorkPerformanceListDTO> findByProductName(String productName) {
		List<WorkPerformanceListDTO> resultList = workPerformanceRepository.findByProductName(productName);
		return resultList;
	}
	
	@Transactional
	public void saveWorkPerformanceAndUpdateMaterialStock(WorkPerformanceDTO workPerformanceDTO) {
		Long productionAmount = workPerformanceDTO.getProductionAmount();
		Optional<ProductionPlan> productionPlan = productionPlanRepository.findById(workPerformanceDTO.getProductionPlanId());
		List<RequiredMaterial> requiredMaterial = requiredMaterialRepository.findAllByProductId(productionPlan.get().getProduct().getProductId());
		
		if (productionPlan.isPresent()) {
	        workPerformanceRepository.save(workPerformanceDTO.toEntity(productionPlan.get()));
	        for (RequiredMaterial material : requiredMaterial) {
                // 소요량 * 생산량
                Long requiredAmount = material.getInputAmount() * productionAmount;

                // 원자재재고 업데이트
                Optional<MaterialStock> materialStock = materialStockRepository.findById(material.getRequriedMaterialPK().getMaterial().getMaterialId());
                if (materialStock.isPresent()) {
                    Long currentStock = materialStock.get().getTotalStock();
                    Long updatedStock = currentStock - requiredAmount;
                    materialStock.get().updateMaterialStock(updatedStock);
                }
            }
	        //materialStock.get().updateMaterialStock(materialStock.get().getTotalStock() - productionAmount);
	    } else {
	        // ProductionPlan이 존재하지 않을 경우 처리 로직
	    	log.info("생산 계획이 없음");
	    }
	}
}
