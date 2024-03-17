package himedia.project.highfourm.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import himedia.project.highfourm.dto.material.MaterialListResponseDTO;
import himedia.project.highfourm.dto.material.MaterialOrderEditFormDTO;
import himedia.project.highfourm.dto.material.MaterialOrderRequestDTO;
import himedia.project.highfourm.dto.material.MaterialOrderResponseDto;
import himedia.project.highfourm.dto.material.MaterialRequestDTO;
import himedia.project.highfourm.entity.MaterialHistory;
import himedia.project.highfourm.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MaterialController {
	
	private final MaterialService materialService;
	
	/**
	 * 원자재 리스트 조회
	 */
	@GetMapping("/api/materials/stock")
	public ResponseEntity<List<MaterialListResponseDTO>>  getMaterialList() {
	    
		List<MaterialListResponseDTO> materialList = materialService.MaterialList();
		
		return ResponseEntity.ok(materialList);
	}
	
	/**
	 * 원자재 등록
	 */
	@PostMapping("/api/materials/stock/new")
	public String addMaterial(@RequestBody MaterialRequestDTO material) {
		materialService.saveMaterial(material);
		return "redirect:/materials/stock";
	}
	
	
	/**
	 * 원자재 리스트 검색
	 */
	@GetMapping("/api/materials/stock/search")
	public ResponseEntity<List<MaterialListResponseDTO>> searchMaterialList(
								@RequestParam(value="searchType") String searchType, @RequestParam(value="search") String search) {
		List<MaterialListResponseDTO> searchMaterialList = new ArrayList<>();

		searchMaterialList = materialService.searchMaterial(searchType, search);
		return ResponseEntity.ok(searchMaterialList);
	}

	/**
	 * 수급내역 리스트 조회
	 */
	
	@GetMapping("/api/materials/order-history")
	public ResponseEntity<List<MaterialOrderResponseDto>> getdMaterialHistoryList() {
		List<MaterialOrderResponseDto> mateiralOderList =materialService.getMaterialOrderList();
		
		return ResponseEntity.ok(mateiralOderList);
	}
	
	/**
	 * 수급내역 검색
	 */
	@GetMapping("/api/materials/order-history/search")
	public ResponseEntity<List<MaterialOrderResponseDto>> searchMaterialOrderHistory(
								@RequestParam(value="searchType") String searchType, @RequestParam(value="search") String search) {
		List<MaterialOrderResponseDto> searchMaterialHistory = null;
		
		searchMaterialHistory = materialService.searchMaterialHistory(searchType, search);
		
			return ResponseEntity.ok(searchMaterialHistory);
		}
		
	/**
	 * 수급내역 등록
	 */
	@PostMapping("/api/materials/order-history/new")
	public String addMaterialHistory(@RequestBody MaterialOrderRequestDTO orderRequestDTO) {
		materialService.saveMaterialhistory(orderRequestDTO);
		return "redirect:http://localhost:3000/materials/order-history";
	}
	
	/**
	 * 입고내역 등록 페이지 조회
	 */
	@GetMapping("/api/materials/order-history/edit/{orderHistoryId}")
	public ResponseEntity<MaterialOrderEditFormDTO> getdMaterialHistory(@PathVariable(name ="orderHistoryId") Long orderHistoryId) {
	    MaterialOrderEditFormDTO editFormDTO = materialService.getMaterialhistoryInfo(orderHistoryId);
		
	    return ResponseEntity.ok(editFormDTO);
	}
	
	/**
	 * 입고내역 등록 
	 */
	@PostMapping("/api/materials/order-history/edit/{orderHistoryId}")
	public String editdMaterialHistory(@PathVariable(name ="orderHistoryId") Long orderHistoryId, 
										@RequestBody MaterialOrderEditFormDTO editDTO) {
		materialService.updateMaterialHistory(editDTO);
		
		return "redirect:http://localhost:3000/materials/order-history";
	}

}
