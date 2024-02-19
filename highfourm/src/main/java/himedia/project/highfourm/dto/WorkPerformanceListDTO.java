package himedia.project.highfourm.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class WorkPerformanceListDTO {
	private Long workPerformanceId;
	private String productionPlanId;
	private LocalDate workDate;
	private String productId;
	private String productName;
	private Long productionAmount;
	private Long acceptanceAmount;
	private Long defectiveAmount;
	private Long workingTime;
	private String manager;
	private Long lotNo;
	private LocalDate validDate;
	private String note;
	
	public WorkPerformanceDTO toWorkPerformanceDTO() {
		return WorkPerformanceDTO
				.builder()
				.workPerformanceId(workPerformanceId)
				.productionPlanId(productionPlanId)
				.workDate(workDate)
				.productionAmount(productionAmount)
				.acceptanceAmount(acceptanceAmount)
				.defectiveAmount(defectiveAmount)
				.workingTime(workingTime)
				.manager(manager)
				.lotNo(lotNo)
				.validDate(validDate)
				.note(note)
				.build();
	}
}