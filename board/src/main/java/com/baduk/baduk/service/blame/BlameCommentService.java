package com.baduk.baduk.service.blame;

import org.springframework.security.core.Authentication;

import com.baduk.baduk.data.blame.BlameCommentDTO;
import com.baduk.baduk.data.common.CommonPageDTO;

public interface BlameCommentService {
	public void createBlameComment(BlameCommentDTO.Request.createBlameComment createDTO, String authorUuid ,Authentication authentication);
	
	public void deleteBlameComment(String blameCommentId, String authorUuid);
	
	public CommonPageDTO<BlameCommentDTO.Response.BlameComment> getBlameCommentPage(String blameId, int page, Authentication authentication);
}