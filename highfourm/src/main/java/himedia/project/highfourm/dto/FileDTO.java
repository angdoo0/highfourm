package himedia.project.highfourm.dto;

import java.util.UUID;

import himedia.project.highfourm.entity.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class FileDTO {
	private Long fileId;
	private String orderId;
	private String originalName;
	private String changedName;
	private String fileType;
	private String fileSize;
	private String filePath;
	
	public File toEntity() {
		return File.builder()
				.originalName(this.originalName)
				.changedName(this.changedName)
				.fileType(this.fileType)
				.fileSize(this.fileSize)
				.filePath(this.filePath)
				.build();
	}
}
