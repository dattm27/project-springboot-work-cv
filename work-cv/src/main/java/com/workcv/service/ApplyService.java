package com.workcv.service;

import com.workcv.model.Apply;

public interface ApplyService {
	Apply save(Apply apply);
	Apply getApply(int user_id, int job_id);
}
