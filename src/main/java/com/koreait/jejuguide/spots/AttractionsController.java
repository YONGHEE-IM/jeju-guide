package com.koreait.jejuguide.spots;

import com.koreait.jejuguide.spots.dto.AttractionDetailDto;
import com.koreait.jejuguide.spots.dto.AttractionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class AttractionsController {

    // 2-1) 카드에 뿌릴 간단 목록 (원하는 만큼 계속 추가)
    private final List<AttractionDto> list = Arrays.asList(
        new AttractionDto("성산일출봉", "서귀포시 성산읍",
            "/images/attractions/seongsan.jpg", "자연",
            "유네스코 세계자연유산으로 지정된 화산 분화구."),
        new AttractionDto("한라산 국립공원", "제주시",
            "/images/attractions/hallasan.jpg", "자연",
            "한국에서 가장 높은 산. 다양한 등산 코스와 자연경관."),
        new AttractionDto("천지연 폭포", "서귀포시",
        	"/images/attractions/cheonjiyeon.jpg","폭포",
        	"서귀포시에 위치한 폭포. 제주도의 대표적인 폭포 중 하나다."),
        new AttractionDto("주상절리대", "서귀포시 중문동",
        	"/images/attractions/jusang.jpg", "폭포",
        	"화산활동으로 만들어진 독특한 바위 기둥들"),
        new AttractionDto("초콜릿 박물관", "서귀포시 대정읍",
            "/images/attractions/chocolatemuseum.jpg", "박물관",
            "초콜릿 박물관은 초콜릿의 전래역사와, 지구상의 모든 초콜릿에 대해 재미있게 살펴볼 수 있는 기초미각문화의 전당입니다."),
        new AttractionDto("용두암", "제주시 용두암길",
            "/images/attractions/dragonhead.jpg", "자연",
            "화산활동으로 만들어진 용머리 모양의 지형"),
        new AttractionDto("용머리해안", "서귀포시 안덕면",
                "/images/attractions/dragonheadbeach.jpg", "해안",
                "용머리해안은 서귀포시 안덕면에 위치한 해안가이다. 용머리해안은 산방산과 인접해 있다."),
        new AttractionDto("에코랜드", "제주시 조천읍",
                "/images/attractions/ecoland.jpg", "테마파크",
                "제주특별자치도 제주시 조천읍의 곶자왈에 위치한 철도 컨셉의 생태관광 테마파크이다."), 
        new AttractionDto("함덕 해수욕장", "제주시 조천읍",
                "/images/attractions/hamdeok.jpg", "해안",
                "제주시에 있는 해수욕장 중에서는 가장 대표적인 해수욕장이다."),
        new AttractionDto("제주 센트럴파크", "제주시 조천읍",
                "/images/attractions/jejucentralpark.jpg", "테마파크",
                "고카트 (도보관람도 가능)를 타면서 세계문화유산과 각 나라의 우수한 건축물을 관람할 수 있는 곳입니다."),
        new AttractionDto("국립제주박물관", "제주시 일주동로",
                "/images/attractions/jejunationalmuseum.jpg", "박물관",
                "제주의 오랜 역사와 제주 사람들의 이야기를 알 수 있는 박물관"),
        new AttractionDto("만장굴", "제주시 구좌읍",
                "/images/attractions/manjang.jpg", "자연",
                "유네스코 세계유산 및 대한민국의 천연기념물로 등록되어 있는 용암동굴"),
        new AttractionDto("성읍민속마을", "서귀포시 표선면",
                "/images/attractions/oldtown.jpg", "테마파크",
                "표선면 성읍리에 위치한 성읍마을은 옛 제주 마을의 모습이 그대로 유지되어 있는 곳이다."),
        new AttractionDto("섭지코지", "서귀포시 성산읍 고성리",
                "/images/attractions/seopji.jpg", "자연",
                "제주 동부 해안에 볼록 튀어나온 섭지코지는 성산일출봉을 배경으로 한 해안 풍경이 일품이다.")
        	
    );

    // 2-2) 상세 데이터: title을 키로 사용
    private final Map<String, AttractionDetailDto> detailByTitle =
        new HashMap<String, AttractionDetailDto>() {{
            put("성산일출봉", new AttractionDetailDto(
                "성산일출봉", "/images/attractions/seongsan.jpg", "서귀포시 성산읍", "자연",
                "약 5000년 전 바다 속에서 분화한 화산체. 분화구 둘레에서 일출 감상이 특히 유명합니다.",
                "일출 2시간 전~일몰 1시간 전", "성인 5,000원 / 청소년·어린이 2,500원"
            ));
            put("한라산 국립공원", new AttractionDetailDto(
                "한라산 국립공원", "/images/attractions/hallasan.jpg", "제주시", "자연",
                "영실·성판악 등 다양한 코스가 있으며, 계절마다 전혀 다른 풍경을 보여줍니다.",
                "연중무휴(코스별 상이)", "무료"
            ));
           put("천지연 폭포", new AttractionDetailDto(
        	   "천지연 폭포", "/images/attractions/cheonjiyeon.jpg", "서귀포시", "폭포",
        	   "높이 22m, 폭 12m의 웅장한 폭포로 '하늘과 땅이 만나 이룬 연못'이라는 의미를 가지고 있습니다. 폭포 주변은 난대림 지대로 관음죽, 담팔수 등 희귀식물이 자생하며, 천연기념물 제379호로 지정되어 있습니다. 야간에는 아름다운 조명으로 폭포를 비춰 낮과는 다른 환상적인 분위기를 연출합니다.",
        	   "오전 9시~22시까지 운영", "성인 2000원 / 청소년·어린이 1000원"));
           put("주상절리대", new AttractionDetailDto(
        	   "주상절리대","/images/attractions/jusang.jpg","서귀포시 중문동", "폭포",
        	   "제주도의 신비로운 화산활동이 빚어낸 걸작으로, 현무암 용암이 바다로 흘러들어 급격히 식으면서 형성된 4~6각형의 기둥 모양 암석입니다. 약 1.4km에 걸쳐 펼쳐진 주상절리는 마치 거대한 돌기둥을 쌓아놓은 듯한 장관을 이룹니다. 특히 파도가 거센 날에는 높이 20m까지 물보라가 치솟아 더욱 웅장한 모습을 감상할 수 있습니다.",
        	   "오전 9시~18시까지 운영", "성인 2000원 / 청소년·어린이 1000원"));
           put("초콜릿 박물관", new AttractionDetailDto(
        	   "초콜릿 박물관","/images/attractions/chocolatemuseum.jpg","서귀포시 대정읍","박물관",
        	   "초콜릿박물관은 인간과 함께 숨쉬며 살아온 초콜릿의 역사와 오늘날 유럽을 필두로 한 지구상의 모든 초콜릿에 대하여 잘 살펴보고 알 수 있도록 기초 문화의 전당역할과 더불어 과연 최고 품질의 초콜릿이 무엇인지, 그 맛이 어떠한지알 수 있게 한 'Real Chocolate Place'입니다.",
        	   "오전 10시 30분 ~ 17시까지 운영", "성인 8000원 / 어린이 6000원 / 6세미만, 70세 이상 무료"));
           put("용두암", new AttractionDetailDto(
               "용두암","/images/attractions/dragonhead.jpg","제주시 용두암길","자연",
               "용두암은 분출된 용암이 흐르고 난 뒤 남은 용암수로가 파도에 의해 침식되면서 형성된 지형으로 높이가 10m에 이른다. 용두암 주변도 이와 마찬가지로 형성된 지형이다. 한자 그대로 바위의 모습이 용의 머리와 닮았다 하여 용두암이라고 하며 한라산 신령의 옥구슬을 가지면 승천할 수 있다는 것을 안 용이 옥구슬을 몰래 훔쳐 하늘로 승천하려던 도중에 한라산 신령이 쏜 화살에 맞아 떨어져서 돌로 굳어졌다는 전설이 있다.",
               "연중무휴 24시간 영업", "무료"));
           put("용머리해안", new AttractionDetailDto(
                   "용머리해안","/images/attractions/dragonheadbeach.jpg","서귀포시 안덕면","해안",
                   "용머리해안은 마치 바다 속으로 들어가는 용의 머리를 닮았다 해서 용머리해안으로 불린다. 수 천 만년 동안 층층이 쌓인 사암층 암벽이 파도에 깎여 기묘한 절벽을 이루고 있다. 파도의 치여 비밀의 방처럼 움푹 패인 굴 방이나 암벽이 간직하고 있는 파도의 흔적은 기나긴 역사와 마주할 때의 웅장함을 느끼게 한다. 길이 30~50m의 절벽이 굽이 치듯 이어지는 장관은 CF와 영화의 배경으로도 촬영된 바 있다.",
                   "오전 9시 ~ 17시까지 운영", "성인 2000원 / 청소년·어린이 1000원"));
           put("에코랜드", new AttractionDetailDto(
                   "에코랜드","/images/attractions/ecoland.jpg","제주시 조천읍","테마파크",
                   "제주특별자치도 제주시 조천읍의 곶자왈에 위치한 철도 컨셉의 생태관광 테마파크이다. 용인시에 위치한 한국민속촌의 계열사며 국내에서 하이원추추파크를 제외하면 유일한 철도 테마파크이다. 국제적으로도 흔한 테마파크는 아니고 그나마 비슷한 것으로는 디즈니랜드가 있으며 제주도의 자연 친화성을 잘 살린, 많은 사람들이 방문하는 제주도의 대표적인 관광지다.",
                   "오전 8시 30분 ~ 20시 10분까지 운영", "성인 19000원 / 청소년 16000원 / 어린이 13000원 / 경로 17000원"));
           put("함덕 해수욕장", new AttractionDetailDto(
                   "함덕 해수욕장","/images/attractions/hamdeok.jpg","제주시 조천읍","해안",
                   "고운 백사장과 얕은 바다 속 패사층이 만들어내는 푸른빛 바다가 아름다운 해수욕장이다. 제주시에서 14㎞ 동쪽에 위치해 있고 시내버스도 자주 운행되어 관광객뿐 아니라 제주도민도 즐겨 찾는다. 경사도가 5° 정도로 아무리 걸어 들어가도 어른 허리에도 미치지 않을 만큼 수심이 얕아 가족 단위 피서객이 즐기기에 적당하고 검은 현무암과 아치형 다리, 바다로 이어지는 산책 데크까지 갖추어져 있어 제주의 푸른 바다를 관망하기에도 그만이다.",
                   "연중무휴 24시간 영업", "정보없음"));
           put("제주 센트럴파크", new AttractionDetailDto(
                   "제주 센트럴파크","/images/attractions/jejucentralpark.jpg","제주시 조천읍","테마파크",
                   "제주 센트럴파크는 도내 유일한 고카트 (도보관람도 가능)를 타면서 세계문화유산과 각 나라의 우수한 건축물을 관람할 수 있는 곳입니다. 한라산을 중심으로 중산간에 위치하여 사계절 아름다운 자연과 잘 어우러져 제주 센트럴파크만의 이국적인 풍경을 만날 수 있습니다.",
                   "오전 9시 ~ 18시까지 운영", "성인 1000원 / 청소년 7000원 / 어린이 5000원"));
           put("국립제주박물관", new AttractionDetailDto(
                   "국립제주박물관","/images/attractions/jejunationalmuseum.jpg","제주시 일주동로","박물관",
                   "국립제주박물관은 제주의 역사와 문화에 관련된 다양한 자료와 문화재를 수집, 보존, 연구, 전시, 교육하기 위하여 2001년 6월 15일 처음 문을 열었습니다.'섬문화'를 특성화 주제로 해양문화를 연구하며, 제주도민들과 함께하는 제주 지역사회 중심 문화기관으로서의 역할을 수행해 나갈 것입니다.",
                   "오전 9시 ~ 18시까지 운영", "무료"));
           put("만장굴", new AttractionDetailDto(
                   "만장굴","/images/attractions/manjang.jpg","제주시 구좌읍에","자연",
                   "한라산의 측화산(오름) 중 하나인 '거문오름'이 신생대 플리오세와 플라이스토세기 사이에 분화하면서, 그 용암이 지하를 뚫고 해안으로 향하는 과정에서 형성되었다. 김녕굴(김녕사굴, 705m)로 이어져 제주도 북동쪽 해저로 빠져나간다. 김녕굴의 경우 만장굴과 같은 동굴이었으나 낙반으로 인해 통로가 막히면서 분리되었다.동굴의 특성상 내부 온도가 외부 온도의 등락과는 상관없이 매우 일정하다. 일례로 2018년 7월 기록적인 폭염 속에도 만장굴은 13도를 유지하고 있었다.",
                   "오전 9시 ~ 18시까지 운영 / 매주 첫째주 수요일 휴관", "성인 4000원 / 군인·청소년·어린이 2000원"));
           put("성읍민속마을", new AttractionDetailDto(
                   "성읍민속마을","/images/attractions/oldtown.jpg","서귀포시 표선면","테마파크",
                   "이곳은 조선 태종 16년 성산읍 고성리에 설치된 정의현청이 세종 5년 이곳으로 옮겨진 후, 500여년간 현청 소재지였던 유서 깊은 마을이다. 정의현성 안에는 110호에 달하는 가옥이 있고 성 밖으로도 많은 가옥들이 존재한다. 수백 년 동안 도읍지였기 때문에 다양한 문화유산들이 있다.",
                   "오전 10시 ~ 17시까지 운영 / 매주 첫째주 수요일 휴관", "무료"));
           put("섭지코지", new AttractionDetailDto(
                   "섭지코지","/images/attractions/seopji.jpg","서귀포시 성산읍 고성리","자연",
                   "섭지코지의 섭지란, 재사(才士)가 많이 배출되는 지세라는 뜻이며, 코지는 육지에서 바다로 톡 튀어나온 '곶'을 뜻하는 제주 방언이다. 역사나 과학의 배경지식을 갖고 보면 섭지코지를 더욱 풍부하게 관망할 수 있다.  ",
                   "연중무휴", "무료"));
          
           
           
           
        		   
        }};

     // ✅ 목록(페이징) 
        @GetMapping("/spots")
        public String spots(
                @RequestParam(name = "page", defaultValue = "1") int page,
                Model model) {

            final int pageSize = 12;               // 4 x 3
            int total = list.size();
            int totalPages = (int) Math.ceil((double) total / pageSize);
            if (totalPages == 0) totalPages = 1;
            page = Math.max(1, Math.min(page, totalPages)); // 범위 보정

            int from = (page - 1) * pageSize;
            int to   = Math.min(from + pageSize, total);
            List<AttractionDto> pageItems = list.subList(from, to);

            model.addAttribute("attractions", pageItems);
            model.addAttribute("page", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("total", total);

            return "spots/index";
        }

        // (기존) 상세 — 그대로 유지 (파라미터 이름 명시 필수)
        @GetMapping("/spots/detail")
        public String spotDetail(@RequestParam("title") String title, Model model) {
            AttractionDetailDto d = detailByTitle.get(title);
            if (d == null) {
                model.addAttribute("message", "존재하지 않는 관광지입니다.");
                return "spots/notfound :: content";
            }
            model.addAttribute("d", d);
            return "spots/detail :: content";
        }
    
}
