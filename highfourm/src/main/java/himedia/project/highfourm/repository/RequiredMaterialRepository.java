package himedia.project.highfourm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import himedia.project.highfourm.entity.RequiredMaterial;
import himedia.project.highfourm.entity.pk.RequiredMaterialPK;

public interface RequiredMaterialRepository extends JpaRepository<RequiredMaterial, RequiredMaterialPK>{

	@Query("SELECT rm FROM RequiredMaterial rm WHERE rm.requriedMaterialPK.product.productId = :productId")
    List<RequiredMaterial> findAllByProductId(String productId);

}
