package com.koreait.jejuguide.service.ai;

import com.koreait.jejuguide.dto.ai.PlanRequest;
import com.koreait.jejuguide.dto.ai.PlanResponse;

public interface AiPlanService {
    PlanResponse generatePlan(PlanRequest req);
}
