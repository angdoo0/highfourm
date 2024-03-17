package himedia.project.highfourm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.WorkPerformanceDTO;
import himedia.project.highfourm.dto.WorkPerformanceListDTO;
import himedia.project.highfourm.entity.MaterialStock;
import himedia.project.highfourm.entity.ProductionPlan;
import himedia.project.highfourm.entity.RequiredMaterial;
import himedia.project.highfourm.repository.MaterialStockRepository;
import himedia.project.highfourm.repository.OrdersRepository;
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
	private final OrdersRepository ordersRepository;
	
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
	
	@Transactional
	public void saveWorkPerformanceAndUpdateMaterialStock(WorkPerformanceListDTO workPerformancelistDTO) {
		//원자재 재고 업데이트
		Long productionAmount = workPerformancelistDTO.getProductionAmount();
		Optional<ProductionPlan> productionPlan = productionPlanRepository.findById(workPerformancelistDTO.getProductionPlanId());
		List<RequiredMaterial> requiredMaterial = requiredMaterialRepository.findAllByProductId(workPerformancelistDTO.getProductId());
		//주문 상태 체크 
		String productionPlanId = workPerformancelistDTO.getProductionPlanId();
		String orderId = productionPlanRepository.findById(productionPlanId).get().getOrders().getOrderId();
		List<ProductionPlan> productionPlanList = productionPlanRepository.findByOrdersOrderId(orderId);
		
		if (productionPlan.isPresent()) {
	        workPerformanceRepository.save(workPerformancelistDTO.toWorkPerformanceDTO().toEntity(productionPlan.get()));
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
	    } else {
	        // ProductionPlan이 존재하지 않을 경우 처리 로직
	    	log.info("생산 계획이 없음");
	    }
		
		boolean isOrderUpdated = true;
		for(ProductionPlan plan : productionPlanList) {
			Long productionAmountSum= 0l;
			List<WorkPerformanceListDTO> workPerfomanceList = workPerformanceRepository.findByProductionPlanId(plan.getProductionPlanId());
			for(WorkPerformanceListDTO work : workPerfomanceList) {
				productionAmountSum += work.getAcceptanceAmount();
			}
			if(plan.getProductionPlanAmount() > productionAmountSum) {
				isOrderUpdated = false;
				break;
			}
		}
		if(isOrderUpdated) {
			ordersRepository.updateEndingStateToY(orderId);
		}
		
	}
	public List<WorkPerformanceListDTO> search(String searchType, String search) {
		List<WorkPerformanceListDTO> workPerformanceList = new ArrayList<WorkPerformanceListDTO>();
		
		if(searchType.equals("생산 계획 코드")) {
			workPerformanceList = workPerformanceRepository.findByProductionPlanId(search);
		} else if(searchType.equals("담당자")) {
			workPerformanceList = workPerformanceRepository.findByManager(search);
		} else if(searchType.equals("생산품명")) {
			workPerformanceList = workPerformanceRepository.findByProductName(search);
		}
		return workPerformanceList;
	}
}
