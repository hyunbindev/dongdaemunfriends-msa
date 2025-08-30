package com.baduk.baduk.service.blame;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.baduk.baduk.data.blame.BlameDTO;
import com.baduk.baduk.data.blame.BlameDTO.Response.Blame;
import com.baduk.baduk.data.common.CommonPageDTO;

public interface BlameService {
	public BlameDTO.Response.CreateBlame createBlame(BlameDTO.Request.CreateBlame dto);
	
	public void deleteBlame(String userUuid , String blameId);
	
	public BlameDTO.Response.Blame getBlame(String userUuid, String blameId ,Authentication authentication);
	
	public CommonPageDTO<Blame> getBlameList(int page, Authentication authentication);
}