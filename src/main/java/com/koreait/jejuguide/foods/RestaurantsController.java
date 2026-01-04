package com.koreait.jejuguide.foods;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/foods")
public class RestaurantsController {

    private final java.util.List<RestaurantDto> DATA = new java.util.ArrayList<>(java.util.List.of(
        RestaurantDto.of("숙성도 제주본점","흑돼지","제주시 연동",
            "오랜 시간 숙성시켜 뛰어난 맛을 자랑하는 흑돼지 전문 고깃집",
            "blackpork_main.jpg", java.util.List.of("blackpork_1.jpg","blackpork_2.jpg","blackpork_3.jpg"),
            "11:30–22:00/라스트 오더 21:20","960 숙성 뼈등신(350g) 39,000원"),
        RestaurantDto.of("칠본가","흑돼지","제주시 서천길",
                "당일 공수한 제주산 흑돼지를 연탄불에 구워 맛볼 수 있는 고깃집",
                "blackpork2_main.jpg", java.util.List.of("blackpork2_1.jpg","blackpork2_2.jpg","blackpork2_3.jpg"),
                "11:30–22:00","칠본가 근고기(600g) 66,000원"),
        RestaurantDto.of("춘심이네 본점","갈치조림","서귀포시 중문",
                "제주산 갈치로 각종 요리를 선보이는 '맛있는 녀석들' 출연 음식점",
                "fish_main.jpg", java.util.List.of("fish_1.jpg","fish_2.jpg"),
                "10:30–20:50/주문 마감 20:30","통갈치구이(2인) 89,000원"),
        RestaurantDto.of("갈치왕 중문점","갈치조림","서귀포시 중문",
                "신선한 큼지막한 제주산 은갈치로 다양한 음식을 선보이는 맛집",
                "fish2_main.jpg", java.util.List.of("fish2_1.jpg","fish2_2.jpg"),
                "10:30–21:30","통갈치 해물찜 100,000원(2인)/150,000원(4인)"),
        RestaurantDto.of("자매 국수 본점","고기국수","제주시 항골남길",
                "쫄깃한 흑돼지 수육을 올린 국수로 인기 있는 체인 음식점",
                "noodle_main.jpg", java.util.List.of("noodle_1.webp","noodle_2.webp"),
                "09:00-14:20/16:10-18:00","고기 국수 10,000원/비빔 국수 10,000원"),
        RestaurantDto.of("올래국수","고기국수","제주시 귀아랑길24올래국수",
                "'수요미식회'에 소개된, 고기 국수 단 하나의 메뉴로 승부하는 맛집",
                "noodle2_main.webp", java.util.List.of("noodle2_1.webp","noodle2_2.webp","noodle2_3.webp"),
                "08:30-15:00/주문 마감 14:50","고기 국수 10,000원"),
        RestaurantDto.of("명진전복","전복죽","제주시 구좌읍",
                "신선한 전복과 영양 가득한 돌솥밥이 조화를 이루는 제주도의 인기 전복죽 맛집",
                "porridge_main.webp", java.util.List.of("porridge_1.webp","porridge_2.webp"),
                "09:30-20:30/주문 마감 20:00","전복죽 13,000원/전복돌솥밥 16,000원"),
        RestaurantDto.of("도두해녀의집","전복죽","제주시 도두항길",
                "신선한 전복죽과 물회가 인기 있는 제주공항 근처에 위치한 해산물 전문 식당",
                "porridge2_main.webp", java.util.List.of("porridge2_1.webp","porridge2_2.webp","porridge2_3.webp"),
                "10:00-20:00/브레이크타임 15:30-17:00/라스트오더 19:30","전복죽 14,000원/물회(특) 17,000원"),
        RestaurantDto.of("미영이네 식당","횟집","서귀포시 대정읍",
                "비린내 없이 신선한 고등어회로 유명한 맛집",
                "livefish_main.webp", java.util.List.of("livefish_1.webp","livefish_2.webp","livefish_3.webp"),
                "11:30-22:00/라스트오더 20:30/매주 수요일 정기휴무","고등어회 + 탕 (대) 95,000원/(소) 70,000원"),
        RestaurantDto.of("모살물","횟집","제주시 삼무로",
                "객주리가 들어간 조림과 모둠 회를 맛볼 수 있는 로컬 식당",
                "livefish2_main.webp", java.util.List.of("livefish2_1.webp","livefish2_2.webp"),
                "16:30-00:00","모둠회 20,000(간단)/30,000(소)/40,000(중)/50,000(대)"),
        RestaurantDto.of("더 클리프","카페","서귀포시 중문",
                "네온사인으로 빛나는 화려한 인테리어를 자랑하는 오션 뷰 펍 겸 음식점",
                "cafe_main.webp", java.util.List.of("cafe_1.webp","cafe_2.webp","cafe_3.webp"),
                "10:00-00:00/공휴일 10:00-26:00","더 킆리프 피자 29,000원/흑돼지 멜젓 파스타 18,000원"),
        RestaurantDto.of("카페구할구 제주","카페","제주시 구좌읍",
                "제주 감성을 제대로 느낄 수 있는 제주도 이색 카페",
                "cafe2_main.webp", java.util.List.of("cafe2_1.webp","cafe2_2.webp","cafe2_3.webp","cafe2_4.webp","cafe2_5.webp"),
                "11:00-18:00/공휴일 11:00-19:00","구할구커피 8,000원/말차말차 9,000원")
       
    ));

    @GetMapping
    public String list(@RequestParam(name = "page", defaultValue="0") int page,
                       @RequestParam(name = "size", defaultValue="12") int size,
                       Model model){
        int total = DATA.size();
        int from = Math.min(page*size, total);
        int to = Math.min(from+size, total);
        model.addAttribute("restaurants", DATA.subList(from, to));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int)Math.ceil((double)total/size));
        return "foods/index";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(name = "title") String title, Model model) {
        RestaurantDto r = DATA.stream()
            .filter(d -> d.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
        model.addAttribute("r", r);
        return "foods/detail :: content";
    }


}
