package himedia.project.highfourm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.project.highfourm.dto.mrp.MrpProductionPlanDTO;
import himedia.project.highfourm.dto.mrp.MrpRequiredMaterialDTO;
import himedia.project.highfourm.service.MrpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 한혜림
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MrpController {

	private final MrpService service;

	/**
	 * 자재 소요량 산출 페이지
	 */
	@GetMapping("/api/mrp")
	public ResponseEntity<Map<String, Object>> mrp() {
		Map<String, Object> responseMap = new HashMap<>();

		List<MrpProductionPlanDTO> productionPlanList = service.findByProductionPlans();

		responseMap.put("plan", productionPlanList);
		
		return ResponseEntity.ok(responseMap);
	}

	/**
	 * 생산 계획, 자재 소요 계획 조회
	 */
	@GetMapping("/api/mrp/{productionPlanId}")
	public ResponseEntity<Map<String, Object>> mrpDetail(@PathVariable(name = "productionPlanId") String productionPlanId) {

		Map<String, Object> responseMap = new HashMap<>();

		List<MrpProductionPlanDTO> productionPlanList = service.findByProductionPlans();
		responseMap.put("plan", productionPlanList);

		List<MrpRequiredMaterialDTO> requiredMaterialList = service.findByMaterials(productionPlanId);
		responseMap.put("requiredMaterial", requiredMaterialList);

		return ResponseEntity.ok(responseMap);
	}
	
	/**
	 * 생산 계획 검색
	 */
	@GetMapping("/api/mrp/search")
	public ResponseEntity<Map<String, Object>> mrpSearch(@RequestParam(value = "searchType") String searchType,
			@RequestParam(value = "search") String search) {
		
		Map<String, Object> responseMap = new HashMap<>();

		List<MrpProductionPlanDTO> result = service.search(searchType, search);
		responseMap.put("plan", result);
		
		return ResponseEntity.ok(responseMap);
	}
	
	/**
	 * 생산 계획 클릭시 자재 소요 계획 검색 페이지 
	 */
	@GetMapping("/api/mrp/{productionPlanId}/search")
	public ResponseEntity<Map<String, Object>> mrpSearchDetail(@PathVariable(name = "productionPlanId") String productionPlanId, @RequestParam(value = "searchType") String searchType,
			@RequestParam(value = "search") String search) {

		Map<String, Object> responseMap = new HashMap<>();

		List<MrpProductionPlanDTO> result = service.search(searchType, search);
		responseMap.put("plan", result);

		List<MrpRequiredMaterialDTO> requiredMaterialList = service.findByMaterials(productionPlanId);
		responseMap.put("requiredMaterial", requiredMaterialList);

		return ResponseEntity.ok(responseMap);
	}

}
