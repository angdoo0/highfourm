package himedia.project.highfourm.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.material.MaterialListResponseDTO;
import himedia.project.highfourm.dto.material.MaterialOrderEditFormDTO;
import himedia.project.highfourm.dto.material.MaterialOrderListDTO;
import himedia.project.highfourm.dto.material.MaterialOrderRequestDTO;
import himedia.project.highfourm.dto.material.MaterialOrderResponseDto;
import himedia.project.highfourm.dto.material.MaterialRequestDTO;
import himedia.project.highfourm.entity.Material;
import himedia.project.highfourm.entity.MaterialHistory;
import himedia.project.highfourm.entity.MaterialStock;
import himedia.project.highfourm.entity.StockManagement;
import himedia.project.highfourm.repository.MaterialHistoryRepository;
import himedia.project.highfourm.repository.MaterialRepository;
import himedia.project.highfourm.repository.MaterialStockRepository;
import himedia.project.highfourm.repository.StockManagementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author 신지은 
 * 원자재 관련 service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {

	private final MaterialRepository materialRepository;
	private final MaterialHistoryRepository historyRepository;
	private final MaterialStockRepository stockRepository;
	private final StockManagementRepository managementRepository;


	// 원자재 등록
	public void saveMaterial(MaterialRequestDTO material) {
		materialRepository.save(material.toEntityFirst());
		
		Optional<StockManagement> stockManagement = managementRepository.findById(material.getManagementId());
		if(stockManagement.isPresent())
			stockRepository.save(material.toEntitySecond(stockManagement.get()));
	}
	
	// 원자재 검색
	public List<MaterialListResponseDTO> searchMaterial(String searchType, String search ) {
		List<MaterialListResponseDTO> materialListResponseDTOs = new ArrayList();
		
		if(searchType.equals("자재코드")) {
			materialListResponseDTOs = stockRepository.findMaterialByMaterialId(search);
		}else if(searchType.equals("자재명")){
			materialListResponseDTOs =  stockRepository.findMaterialByMaterialName(search);
		}else if(searchType.equals("재고관리 방식")){
			materialListResponseDTOs = stockRepository.findMaterialByManagement(search);
		}else {
			materialListResponseDTOs = new ArrayList<>();
		}
		return materialListResponseDTOs;
	}

	// 원자재 리스트 조회
	public List<MaterialListResponseDTO> MaterialList() {
		return stockRepository.findMaterialList();
	}

	// 수급내역 등록
	public void saveMaterialhistory(MaterialOrderRequestDTO orderRequestDTO) {
		Optional<Material> material = materialRepository.findById(orderRequestDTO.getMaterialId());
		
		if (material.isPresent()) {
			historyRepository.save(orderRequestDTO.toEntity(material.get()));
		}
	}

	// 수급내역 조회
	public List<MaterialOrderResponseDto> getMaterialOrderList() {
		
	    List<MaterialOrderListDTO> materialOrderListDTOs = historyRepository.findAllWithMaterialFields();

	    // MaterialOrderListDTO를 MaterialOrderResponseDto로 변환
	    List<MaterialOrderResponseDto> materialOrderResponseDtos = materialOrderListDTOs.stream()
	            .map(orderListDto -> MaterialOrderResponseDto.toOrderResponseDTO(orderListDto, materialOrderListDTOs))
	            .collect(Collectors.toList());

	    return materialOrderResponseDtos;
	}
	
	// 수급내역검색
	public List<MaterialOrderResponseDto> searchMaterialHistory(String searchType, String search) {
		List<MaterialOrderListDTO> materialOrderListDTOs;
		
		if(searchType.equals("자재코드")) {
			materialOrderListDTOs = historyRepository.findMaterialHistoryByMaterialId(search);
		}else if(searchType.equals("자재명")){
			materialOrderListDTOs =  historyRepository.findMaterialHistoryByMaterialName(search);
		}else if(searchType.equals("발주일")){
			materialOrderListDTOs = historyRepository.findMaterialHistoryByOrderDate(search);
		}else if(searchType.equals("입고일")){
			materialOrderListDTOs =  historyRepository.findMaterialHistoryByInboundDate(search);
		}else {
			 materialOrderListDTOs = new ArrayList<>();
		}
		
		List<MaterialOrderResponseDto> materialOrderResponseDtos = materialOrderListDTOs.stream()
				.map(orderListDto -> MaterialOrderResponseDto.toOrderResponseDTO(orderListDto, materialOrderListDTOs))
				.collect(Collectors.toList());
		return materialOrderResponseDtos;
	}
	
	// 입고내역 등록 form 조회
	public MaterialOrderEditFormDTO getMaterialhistoryInfo(Long materialHistoryId) {
		Optional<MaterialHistory> materialHistory = historyRepository.findById(materialHistoryId);
		
		//MaterialHistory를 MaterialOrderResponseDTO로 변환
		MaterialHistory material = materialHistory.get();
			
		return MaterialOrderEditFormDTO.builder()
				.materialHistoryId(material.getMaterialHistoryId())
				.orderDate(material.getOrderDate())
				.recievingDate(material.getRecievingDate())
				.materialId(material.getMaterial().getMaterialId())
				.standard(material.getStandard())
				.supplier(material.getSupplier())
				.inboundAmount(material.getInboundAmount())
				.orderAmount(material.getOrderAmount())
				.unitPrice(material.getUnitPrice())
				.note(material.getNote())
				.build();
		
	}
	
	//입고내역 등록
	@Transactional
	public void updateMaterialHistory(MaterialOrderEditFormDTO editDTO) {
		
		//수급내역 조회
		MaterialHistory materialHistory = historyRepository.findById(editDTO.getMaterialHistoryId())
		        .orElseThrow();
		
		//자재 재고에 총 재고량을 가져오기 위한 materialStock 조회
		Optional<MaterialStock> materialStock = stockRepository.findById(editDTO.getMaterialId());
		
		materialHistory.updateMaterialHistory(editDTO.getInboundAmount(), materialStock.get().getTotalStock(), editDTO.getRecievingDate(), editDTO.getNote());
		
		//변수 선언
		Long totalMaterial = 0L;
		
		// material_stock 테이블 total_stock에 입고량 반영 
		if(materialStock.isPresent()) {
			totalMaterial = materialStock.get().getTotalStock() + editDTO.getInboundAmount();
			materialStock.get().updateMaterialStock(totalMaterial);
			stockRepository.save(materialStock.get());
		}
		
	}
	
}
