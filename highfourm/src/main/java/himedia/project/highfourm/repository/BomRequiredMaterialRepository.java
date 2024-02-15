package himedia.project.highfourm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import himedia.project.highfourm.entity.RequiredMaterial;
import himedia.project.highfourm.entity.pk.RequiredMaterialPK;
import jakarta.persistence.Tuple;

public interface BomRequiredMaterialRepository extends JpaRepository<RequiredMaterial, RequiredMaterialPK> {
	@Query("SELECT rm.requriedMaterialPK.material.materialId AS materialId, " +
		   "rm.inputProcess AS inputProcess, " +
           "rm.requriedMaterialPK.material.materialName AS materialName, " +
           "rm.inputAmount AS inputAmount " +
           "FROM RequiredMaterial rm " +
           "WHERE rm.requriedMaterialPK.product.productId = :productId")
    List<Tuple> findBomRequiredMaterial(@Param("productId") String productId);
}
