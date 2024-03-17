package himedia.project.highfourm.dto.material;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MaterialOrderResponseDto {
	private Long materialHistoryId;
	private String orderDate;
	private String recievingDate;
	private String materialId;
	private String materialName;
	private String standard;
	private String unit;
	private String supplier;
	private Long restStock; // 이월 재고 -> 입고 등록시 stock 테이블 total_stock 총재고량 ||없으면 0
	private Long materialInventory; // 재고량 ->이월 재고량(total_stock) + 입고량 || 없으면 0
	private Long usedAmount; // 사용량 1. 이전 재고량(직전material_inventory - 이월 재고량total_stock) ||없으면 0
	private Long inboundAmount;
	private Long orderAmount;
	private Long unitPrice;
	private Long totalPrice; // 총금액
	private String note;

	public static MaterialOrderResponseDto toOrderResponseDTO(MaterialOrderListDTO orderListDTO,
																List<MaterialOrderListDTO> materialOrderListDTOs) {

		Long restStock = calculateRestStock(orderListDTO);
		Long usedAmount = calculateUsedAmount(orderListDTO, materialOrderListDTOs);
		Long totalPrice = calculateTotalPrice(orderListDTO);
		Long materialInventory = calculateMaterialInventory(orderListDTO);

		return MaterialOrderResponseDto.builder()
				.materialHistoryId(orderListDTO.getMaterialHistoryId())
				.orderDate(orderListDTO.getOrderDate())
				.recievingDate(orderListDTO.getRecievingDate())
				.materialId(orderListDTO.getMaterialId())
				.materialName(orderListDTO.getMaterialName())
				.standard(orderListDTO.getStandard())
				.unit(orderListDTO.getUnit())
				.supplier(orderListDTO.getSupplier())
				.restStock(restStock) // totalStock을 materialInventory에 저장
				.materialInventory(materialInventory)
				.usedAmount(usedAmount).inboundAmount(orderListDTO.getInboundAmount())
				.orderAmount(orderListDTO.getOrderAmount())
				.unitPrice(orderListDTO.getUnitPrice())
				.totalPrice(totalPrice)
				.note(orderListDTO.getNote())
				.build();
	}

	// 이월 재고량
	private static Long calculateRestStock(MaterialOrderListDTO orderListDTO) {
		if (orderListDTO.getTotalStock() == 0) {
			return null;
		}
		return orderListDTO.getMaterialInventory();
	}
	
	// 재고량
	private static Long calculateMaterialInventory(MaterialOrderListDTO orderListDTO) {
		if (orderListDTO.getRecievingDate() == null) {
			return null;
		}
		return orderListDTO.getMaterialInventory() + orderListDTO.getInboundAmount();
	}


	// 사용량
	private static Long calculateUsedAmount(MaterialOrderListDTO orderListDTO,
												List<MaterialOrderListDTO> materialOrderListDTOs) {
		if (orderListDTO.getTotalStock() == 0) {
			return null;
		}

		// DB에서 해당 MaterialId에 대한 모든 재고 이력 조회
		List<MaterialOrderListDTO> filteredHistory = materialOrderListDTOs.stream()
				.filter(dto -> dto.getMaterialId().equals(orderListDTO.getMaterialId())
						&& dto.getMaterialHistoryId() < orderListDTO.getMaterialHistoryId())
				.collect(Collectors.toList());
		
		//이전 재고이력이 없으면 사용량은 null
		if (filteredHistory.isEmpty()) {
			return null;
		}
		
		// materialHistoryId가 가장 큰 객체 찾기
		MaterialOrderListDTO maxHistoryDTO = filteredHistory.stream()
				.max(Comparator.comparing(MaterialOrderListDTO::getMaterialHistoryId)).orElse(null);
		/**
		 * maxHistoryDTO가 null이 아닌 경우, 즉 이전 이력이 존재하는 경우에는 해당 이력의 총 재고량(getTotalStock())을 maxHistoryInventory 변수에 할당
		 *그러나 maxHistoryDTO가 null인 경우, 즉 이전 이력이 없는 경우에는 0을 maxHistoryInventory 변수에 할당, 이중 체크
		 */
		// 가장 큰 materialHistoryId의 재고량
		Long maxHistoryInventory = maxHistoryDTO != null ? maxHistoryDTO.getTotalStock() : 0;

		// 사용량 계산: 가장 큰 materialHistoryId의 재고량 - 이월 재고량
		return maxHistoryInventory - orderListDTO.getTotalStock();
	}

	// 총 금액
	private static Long calculateTotalPrice(MaterialOrderListDTO orderListDTO) {
		if (orderListDTO.getInboundAmount() == null) {
			return null;
		}
		return orderListDTO.getInboundAmount() * orderListDTO.getUnitPrice();
	}
}
