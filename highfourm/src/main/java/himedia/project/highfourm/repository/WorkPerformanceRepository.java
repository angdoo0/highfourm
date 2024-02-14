package himedia.project.highfourm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import himedia.project.highfourm.dto.WorkPerformanceListDTO;
import himedia.project.highfourm.entity.WorkPerformance;

public interface WorkPerformanceRepository extends JpaRepository<WorkPerformance, Long>{
	@Query("SELECT NEW himedia.project.highfourm.dto.WorkPerformanceListDTO(wf.workPerformanceId, " +
	        "pp.productionPlanId, wf.workDate, pp.product.productId, pp.product.productName, wf.productionAmount, " +
	        "wf.acceptanceAmount, wf.defectiveAmount, wf.workingTime, wf.manager, wf.lotNo, wf.validDate, wf.note) " +
	        "FROM WorkPerformance wf " +
	        "JOIN wf.productionPlan pp")
	List<WorkPerformanceListDTO> findList();

	@Query("SELECT NEW himedia.project.highfourm.dto.WorkPerformanceListDTO(wf.workPerformanceId, " +
            "pp.productionPlanId, wf.workDate, pp.product.productId, pp.product.productName, wf.productionAmount, " +
            "wf.acceptanceAmount, wf.defectiveAmount, wf.workingTime, wf.manager, wf.lotNo, wf.validDate, wf.note) " +
            "FROM WorkPerformance wf " +
            "JOIN wf.productionPlan pp " +
            "WHERE pp.productionPlanId LIKE %:productionPlanId%")
    List<WorkPerformanceListDTO> findByProductionPlanId(@Param("productionPlanId") String productionPlanId);
	
	@Query("SELECT NEW himedia.project.highfourm.dto.WorkPerformanceListDTO(wf.workPerformanceId, " +
            "pp.productionPlanId, wf.workDate, pp.product.productId, pp.product.productName, wf.productionAmount, " +
            "wf.acceptanceAmount, wf.defectiveAmount, wf.workingTime, wf.manager, wf.lotNo, wf.validDate, wf.note) " +
            "FROM WorkPerformance wf " +
            "JOIN wf.productionPlan pp " +
            "WHERE wf.manager LIKE %:manager%")
    List<WorkPerformanceListDTO> findByManager(@Param("manager") String manager);

    @Query("SELECT NEW himedia.project.highfourm.dto.WorkPerformanceListDTO(wf.workPerformanceId, " +
            "pp.productionPlanId, wf.workDate, pp.product.productId, pp.product.productName, wf.productionAmount, " +
            "wf.acceptanceAmount, wf.defectiveAmount, wf.workingTime, wf.manager, wf.lotNo, wf.validDate, wf.note) " +
            "FROM WorkPerformance wf " +
            "JOIN wf.productionPlan pp " +
            "WHERE pp.product.productName LIKE %:productName%")
    List<WorkPerformanceListDTO> findByProductName(@Param("productName") String productName);
}
