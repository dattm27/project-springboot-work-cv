package com.workcv.service;

import java.util.List;
import java.util.Optional;

import com.workcv.model.Apply;

public interface ApplyService {
	Apply save(Apply apply);
	Apply getApply(int user_id, int job_id);
	
	// lấy ra danh sách ứng viên của một công ty (dành cho nhà tuyển dụng)
	public List<Object[]> getApplicationDetailsByCompanyId(int companyId);
	
	//lấy ra danh sách các công việc đã ứng tuyển (đành cho ứng viên)
	public List<Object[]> getAppliedJobs(int user_id);
	
	Optional<Apply> getApplyById(int id);
}
