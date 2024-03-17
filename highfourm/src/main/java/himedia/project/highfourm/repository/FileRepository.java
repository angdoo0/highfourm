package himedia.project.highfourm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import himedia.project.highfourm.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
	Optional<File> findByOrdersOrderId(String orderId);
}
