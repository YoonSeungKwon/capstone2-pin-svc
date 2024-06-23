package yoon.capstone2.svc.pinService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yoon.capstone2.svc.pinService.controller.PinController;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Members;
import yoon.capstone2.svc.pinService.entity.Pin;
import yoon.capstone2.svc.pinService.enums.Category;
import yoon.capstone2.svc.pinService.enums.Colors;
import yoon.capstone2.svc.pinService.enums.Method;
import yoon.capstone2.svc.pinService.enums.Role;
import yoon.capstone2.svc.pinService.repository.MapRepository;
import yoon.capstone2.svc.pinService.repository.MemberRepository;
import yoon.capstone2.svc.pinService.repository.PinRepository;
import yoon.capstone2.svc.pinService.service.PinService;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
class PinServiceApplicationTests {

	@MockBean
	MockMvc mockMvc;

	@Autowired
	PinController pinController;

	@Autowired
	PinService pinService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PinRepository pinRepository;

	@Autowired
	MapRepository mapRepository;


	@Test
	@Transactional
	void contextLoads() {

		Members members = Members.builder()
				.email("test@test.co")
				.password("abcd1234")
				.phone("010-1234-5678")
				.role(Role.USER)
				.name("tester")
				.build();

		memberRepository.save(members);

		Maps map = Maps.builder()
				.title("map1")
				.lon(0)
				.lat(0)
				.isPrivate(true)
				.selectedDate(LocalDateTime.now())
				.colors(Colors.BLUE)
				.build();

		Pin pin = Pin.builder()
				.maps(map)
				.cost(10000)
				.category(Category.CAFE)
				.memo("test1")
				.file(null)
				.method(Method.CASH)
				.header("hi")
				.members(members)
				.build();

		map.getPins().add(pin);

		mapRepository.save(map);

	}

}
