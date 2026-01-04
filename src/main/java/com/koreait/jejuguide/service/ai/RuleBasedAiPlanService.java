package com.koreait.jejuguide.service.ai;

import com.koreait.jejuguide.dto.ai.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

/** 규칙 기반 더미 구현체. 나중에 LLM 연동 서비스로 대체 가능 */
@Service
@Primary
public class RuleBasedAiPlanService implements AiPlanService {

    private static final Map<String, List<String>> attractionsByStyle = new HashMap<>();
    static {
        attractionsByStyle.put("nature", Arrays.asList("한라산", "성산일출봉", "천지연폭포", "주상절리대", "비자림", "용머리해안", "섭지코지"));
        attractionsByStyle.put("culture", Arrays.asList("제주민속촌", "성읍민속마을", "만장굴", "김녕미로공원", "제주돌문화공원", "항파두리 항몽유적지"));
        attractionsByStyle.put("food", Arrays.asList("제주 흑돼지 맛집", "전복요리 전문점", "갈치조림 맛집", "해산물 뷔페", "오메기떡 카페", "제주 한라봉 농장"));
        attractionsByStyle.put("activity", Arrays.asList("제주 승마체험", "스쿠버다이빙", "ATV 체험", "카약", "패러글라이딩", "서핑", "짚라인"));
    }

    @Override
    public PlanResponse generatePlan(PlanRequest req) {
        int days = Math.max(1, Math.min(7, req.getDays()));
        String style = Optional.ofNullable(req.getStyle()).orElse("all");
        String companion = Optional.ofNullable(req.getCompanion()).orElse("couple");
        String budget = Optional.ofNullable(req.getBudget()).orElse("standard");

        PlanResponse res = new PlanResponse();
        res.setDays(days);
        res.setStyle(style);
        res.setCompanion(companion);
        res.setBudget(budget);
        res.setPlans(generateTripPlan(days, style, companion, budget));
        return res;
    }

    private java.util.List<DayPlan> generateTripPlan(int days, String style, String companion, String budget){
        java.util.List<DayPlan> result = new java.util.ArrayList<>();
        java.util.List<String> styles = "all".equals(style) ? java.util.Arrays.asList("nature","culture","food","activity") : java.util.Arrays.asList(style);

        for (int d=1; d<=days; d++){
            String curStyle = styles.get((d-1) % styles.size());
            java.util.List<String> attractions = attractionsByStyle.getOrDefault(curStyle, java.util.Collections.singletonList("제주 명소"));

            DayPlan dp = new DayPlan();
            dp.setDay(d);
            dp.setTitle("Day " + d + " - " + getDayTheme(curStyle, companion));
            dp.setActivities(new java.util.ArrayList<>());

            if (d == 1){
                dp.getActivities().add(Activity.of("09:00","제주 공항 도착","렌터카 픽업 및 여행 시작","이동"));
            } else {
                dp.getActivities().add(Activity.of("08:00", getBreakfastPlace(budget), "제주 전통 조식으로 하루를 시작", "식사"));
            }

            dp.getActivities().add(Activity.of(d==1?"10:30":"09:30",
                    attractions.get(0),
                    getActivityDescription(curStyle, "morning", companion),
                    getStyleName(curStyle)));

            dp.getActivities().add(Activity.of("12:30", getLunchPlace(budget, curStyle), "제주 로컬 맛집에서 점심 식사", "식사"));

            String second = attractions.size()>1? attractions.get(1):"제주 관광지";
            dp.getActivities().add(Activity.of("14:30", second, getActivityDescription(curStyle, "afternoon", companion), getStyleName(curStyle)));

            dp.getActivities().add(Activity.of("17:00","제주 오션뷰 카페","아름다운 바다를 보며 휴식 시간","휴식"));

            dp.getActivities().add(Activity.of("19:00", getDinnerPlace(budget, d), "제주의 신선한 해산물과 특산 요리","식사"));

            if (d == days){
                dp.getActivities().add(Activity.of("21:00", "숙소 체크아웃 및 공항 이동", "제주 여행 마무리, 다음 방문을 기약하며", "이동"));
            } else {
                dp.getActivities().add(Activity.of("21:00", "숙소 휴식", getAccommodation(budget, companion), "숙박"));
            }

            result.add(dp);
        }
        return result;
    }

    private String getDayTheme(String style, String companion){
        java.util.Map<String, java.util.Map<String, String>> themes = new java.util.HashMap<>();
        themes.put("nature", java.util.Map.of("alone","자연 속 힐링","couple","로맨틱 자연 탐방","family","가족과 함께하는 자연","friends","자연 속 모험"));
        themes.put("culture", java.util.Map.of("alone","제주 문화 체험","couple","역사와 문화 여행","family","가족 문화 학습","friends","문화 탐방"));
        themes.put("food", java.util.Map.of("alone","제주 미식 투어","couple","로맨틱 미식 여행","family","가족 맛집 탐방","friends","친구들과 먹방 투어"));
        themes.put("activity", java.util.Map.of("alone","액티비티 체험","couple","커플 액티비티","family","가족 체험 활동","friends","스릴 넘치는 모험"));
        return themes.getOrDefault(style, java.util.Collections.emptyMap()).getOrDefault(companion, "제주 여행");
    }
    private String getBreakfastPlace(String budget){
        java.util.Map<String,String> m = java.util.Map.of("budget","제주 전통 시장 조식","standard","호텔 조식 뷔페","luxury","프리미엄 브런치 카페");
        return m.getOrDefault(budget, "제주 조식");
    }
    private String getLunchPlace(String budget, String style){
        java.util.Map<String, java.util.Map<String,String>> m = new java.util.HashMap<>();
        m.put("budget", java.util.Map.of("nature","제주 도시락","culture","올레국수","food","흑돼지 백반","activity","김밥천국"));
        m.put("standard", java.util.Map.of("nature","제주 한정식","culture","갈치조림 정식","food","전복돌솥밥","activity","해물라면"));
        m.put("luxury", java.util.Map.of("nature","프리미엄 한정식","culture","제주 파인다이닝","food","특급호텔 레스토랑","activity","고급 해산물 레스토랑"));
        return m.getOrDefault(budget, m.get("standard")).getOrDefault(style, "제주 맛집");
    }
    private String getDinnerPlace(String budget, int day){
        java.util.Map<String, java.util.List<String>> m = new java.util.HashMap<>();
        m.put("budget", java.util.Arrays.asList("제주 흑돼지 구이","해물뚝배기","갈치구이 정식","해산물 칼국수"));
        m.put("standard", java.util.Arrays.asList("프리미엄 흑돼지","전복 코스 요리","제주 오겹살","해산물 바비큐"));
        m.put("luxury", java.util.Arrays.asList("특급호텔 한식당","제주 프렌치 레스토랑","오마카세","럭셔리 다이닝"));
        java.util.List<String> list = m.getOrDefault(budget, m.get("standard"));
        return list.get((day-1) % list.size());
    }
    private String getAccommodation(String budget, String companion){
        java.util.Map<String, java.util.Map<String,String>> m = new java.util.HashMap<>();
        m.put("budget", java.util.Map.of("alone","제주 게스트하우스","couple","제주 프라이빗 펜션","family","제주 패밀리 펜션","friends","제주 풀빌라 (다인실)"));
        m.put("standard", java.util.Map.of("alone","제주 호텔 (스탠다드)","couple","오션뷰 리조트","family","패밀리 리조트","friends","제주 독채 펜션"));
        m.put("luxury", java.util.Map.of("alone","5성급 호텔 (스위트)","couple","럭셔리 리조트 (오션뷰)","family","프리미엄 패밀리 리조트","friends","럭셔리 풀빌라"));
        return m.getOrDefault(budget, m.get("standard")).getOrDefault(companion, "제주 숙소에서 휴식");
    }
    private String getActivityDescription(String style, String timeOfDay, String companion){
        java.util.Map<String, java.util.Map<String,String>> am = new java.util.HashMap<>();
        am.put("nature", java.util.Map.of("morning", "family".equals(companion) ? "가족과 함께 천천히 트레킹하며 제주의 자연을 만끽" : "청정 자연을 감상하며 힐링 타임",
                                "afternoon", "제주의 숨은 자연 명소 탐방"));
        am.put("culture", java.util.Map.of("morning","제주의 역사와 문화를 배우는 시간","afternoon","제주 전통 문화 체험 및 포토존"));
        am.put("food", java.util.Map.of("morning","제주 특산물 시장 구경 및 시식","afternoon","제주 대표 맛집 투어"));
        am.put("activity", java.util.Map.of("morning","family".equals(companion) ? "가족 모두가 즐길 수 있는 체험 활동" : "스릴 넘치는 액티비티 체험",
                                  "afternoon","제주에서만 가능한 특별한 체험"));
        return am.getOrDefault(style, java.util.Collections.emptyMap()).getOrDefault(timeOfDay, "제주 여행 즐기기");
    }
    private String getStyleName(String style){
        java.util.Map<String,String> m = java.util.Map.of("nature","자연","culture","문화","food","맛집","activity","액티비티");
        return m.getOrDefault(style, "관광");
    }
}
