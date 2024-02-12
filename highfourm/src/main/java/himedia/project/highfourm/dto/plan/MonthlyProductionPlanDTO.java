package himedia.project.highfourm.dto.plan;

import himedia.project.highfourm.entity.MonthlyProductionPlan;
import himedia.project.highfourm.entity.pk.MonthlyProductionPlanPK;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyProductionPlanDTO {
	private String month;
	private String productionPlanId;
	private Long productionAmount;
	
	public static MonthlyProductionPlanDTO fromEntity(MonthlyProductionPlan monthlyProductionPlan) {
		return MonthlyProductionPlanDTO.builder()
				.month(monthlyProductionPlan.getMonthlyProductPlanPK().getMonth())
				.productionPlanId(monthlyProductionPlan.getMonthlyProductPlanPK().getProductionPlanId())
				.productionAmount(monthlyProductionPlan.getProductionAmount())
				.build();
	}
	
	public MonthlyProductionPlan toEntity() {
	    return MonthlyProductionPlan.builder()
	            .monthlyProductPlanPK(new MonthlyProductionPlanPK(this.month, this.productionPlanId))
	            .productionAmount(this.productionAmount)
	            .build();
	}
	
}
