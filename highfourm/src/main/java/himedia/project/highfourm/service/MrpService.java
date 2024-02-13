package himedia.project.highfourm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.mrp.MrpProductionPlanDTO;
import himedia.project.highfourm.dto.mrp.MrpRequiredMaterialDTO;
import himedia.project.highfourm.repository.MrpRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author 한혜림
 */
@RequiredArgsConstructor
@Service
public class MrpService {

	private final MrpRepository repository;
	
	public List<MrpProductionPlanDTO> findByProductionPlans() {
		return repository.findByProductionPlans();
	}
	
	public List<MrpRequiredMaterialDTO> findByMaterials(String productionPlanId) {
		return repository.findByMaterials(productionPlanId);
	}
	
	/**
	 * Mrp Production Plan 검색
	 */
	public List<MrpProductionPlanDTO> search(String searchType, String search) {
		List<MrpProductionPlanDTO> result = new ArrayList<MrpProductionPlanDTO>();
		
		if (searchType.equals("생산계획 코드")) {
			result = repository.findByProductionPlanID(search);
		} else if (searchType.equals("품번")) {
			result = repository.findByProductId(search);
		} else if (searchType.equals("품명")) {
			result = repository.findByProductName(search);
		} else if (searchType.equals("납기일")) {
			result = repository.findByDueDate(search);
		}
		
		return result;
	}
}
