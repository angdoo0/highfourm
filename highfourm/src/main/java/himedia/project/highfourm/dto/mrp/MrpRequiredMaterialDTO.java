package himedia.project.highfourm.dto.mrp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * @author 한혜림
 * 자재 소요량 산출 페이지 자재 소요 계획
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MrpRequiredMaterialDTO {

	private String productionPlanId;
	private Long productionPlanAmount;
	private String materialName;
	private String materialId;
	private Long inputAmount;
	private Long totalStock;
	private Long safetyStock;
	private Long orderAmount;
	
}
