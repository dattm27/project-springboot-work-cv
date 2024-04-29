package com.workcv.service;

import java.util.List;

import com.workcv.model.Apply;
import com.workcv.model.ApplyInfo;

public interface ApplyService {
	Apply save(Apply apply);
	Apply getApply(int user_id, int job_id);
	public List<Object[]> getApplicationDetailsByCompanyId(int companyId);
}
