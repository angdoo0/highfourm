package himedia.project.highfourm.entity;

import himedia.project.highfourm.dto.RequiredMaterialDTO;
import himedia.project.highfourm.entity.pk.RequiredMaterialPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@Entity
@Table(name = "required_material")
public class RequiredMaterial {
	
	@EmbeddedId
	private RequiredMaterialPK requriedMaterialPK;
	
	@Column(name = "input_process")
	private String inputProcess;
	
	@Column(name = "input_amount")
	private Long inputAmount;
	
	@Builder
	public RequiredMaterial(RequiredMaterialPK requriedMaterialPK, String inputProcess, Long inputAmount) {
		this.requriedMaterialPK = requriedMaterialPK;
		this.inputProcess = inputProcess;
		this.inputAmount = inputAmount;
	}
	
	public RequiredMaterialDTO toRequiredMaterialDTO() {
		return RequiredMaterialDTO
				.builder()
				.productId(requriedMaterialPK.getProduct().getProductId())
				.materialId(requriedMaterialPK.getMaterial().getMaterialId())
				.inputProcess(inputProcess)
				.inputAmount(inputAmount)
				.build();
	}
}
