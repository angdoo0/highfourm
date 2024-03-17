package himedia.project.highfourm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.ProcessDTO;
import himedia.project.highfourm.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessService {
	private final ProcessRepository processRepository;
	
	public List<ProcessDTO> findByProductProductId(String productId){
		List<ProcessDTO> processList = processRepository.findByProductProductId(productId)
				.stream()
	            .map(process -> process.toProcessDTO())
	            .toList();
		return processList;
	}
}
