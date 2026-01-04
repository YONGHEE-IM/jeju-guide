package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.dto.ai.PlanRequest;
import com.koreait.jejuguide.dto.ai.PlanResponse;
import com.koreait.jejuguide.service.ai.AiPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AiPlanController {

    private final AiPlanService aiPlanService;

    public AiPlanController(AiPlanService aiPlanService) {
        this.aiPlanService = aiPlanService;
    }
    
    @GetMapping("/ai")
    public String aiRootRedirect() {
        return "redirect:/ai/plan";
    }

    @GetMapping("/ai/plan")
    public String aiPlannerPage() {
        return "ai/plan";
    }

    @PostMapping("/api/ai/plan")
    @ResponseBody
    public ResponseEntity<PlanResponse> generatePlan(@RequestBody PlanRequest req) {
        return ResponseEntity.ok(aiPlanService.generatePlan(req));
    }
}
