package io.github.externschool.planner.controller;

import io.github.externschool.planner.dto.ScheduleEventReq;
import io.github.externschool.planner.entity.User;
import io.github.externschool.planner.entity.schedule.ScheduleEvent;
import io.github.externschool.planner.entity.schedule.ScheduleEventType;
import io.github.externschool.planner.factories.RolesFactory;
import io.github.externschool.planner.factories.UserFactory;
import io.github.externschool.planner.factories.schedule.ScheduleEventFactory;
import io.github.externschool.planner.factories.schedule.ScheduleEventTypeFactory;
import io.github.externschool.planner.service.ScheduleEventTypeService;
import io.github.externschool.planner.service.ScheduleService;
import io.github.externschool.planner.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static io.github.externschool.planner.factories.UserFactory.USER_EMAIL;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {
    @Autowired WebApplicationContext webApplicationContext;
    @Mock private UserService userService;
    @MockBean private ScheduleService scheduleService;
    @MockBean private ScheduleEventTypeService eventTypeService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDisplayForm_whenCreateScheduleEventType() throws Exception {
        ScheduleEventType eventType = ScheduleEventTypeFactory.createScheduleEventType();
        eventType.addOwner(RolesFactory.createRoleEntity(RolesFactory.ROLE_ALLOWED_CREATE_EVENT));
        eventType.addParticipant(RolesFactory.createRoleEntity(RolesFactory.ROLE_NOT_ALLOWED_CREATE_EVENT));

        MultiValueMap modelMap = new LinkedMultiValueMap<>();
        modelMap.add("name", eventType.getName());
        modelMap.add("amountOfParticipants", eventType.getAmountOfParticipants());
        modelMap.add("owners", eventType.getOwners());
        modelMap.add("participants", eventType.getParticipants());

        when(eventTypeService.loadEventTypes()).thenReturn(Collections.singletonList(eventType));

        this.mockMvc.perform(get("/events/type/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ownersRoles", "participantsRoles", "eventType", "eventTypes"))
                .andExpect(view().name("event/event_type"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldReturnModelAndView_whenProcessNewEventTypeAddition() throws Exception {
        ScheduleEventType eventType = ScheduleEventTypeFactory.createScheduleEventType();

        when(this.eventTypeService.loadEventTypes()).thenReturn(Collections.singletonList(eventType));

        this.mockMvc.perform(get("/events/type/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ownersRoles", "participantsRoles", "eventType", "eventTypes"))
                .andExpect(view().name("event/event_type"));
    }

    @Test
    @WithMockUser(value = "admin@email.com", roles = "ADMIN")
    public void shouldRedirectToListEvent_afterSuccessfulCreateEventType() throws Exception {
        ScheduleEventReq req = ScheduleEventFactory.createScheduleEventReq();

        User user = UserFactory.createUser();
        when(userService.findUserByEmail(USER_EMAIL)).thenReturn(user);

        ScheduleEvent event = ScheduleEventFactory.createNewScheduleEventWithoutParticipants();
        when(scheduleService.createEvent(user, req)).thenReturn(event);

        mockMvc.perform(
                post("/events/type")
                        .param("title", req.getTitle())
                        .param("description", req.getDescription())
                        .param("location", req.getLocation())
                        .param("startOfEvent", req.getStartOfEvent().toString())
                        .param("endOfEvent", req.getEndOfEvent().toString())
                        .param("eventType", req.getEventType())
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));
    }

    // TODO add tests for the validation checking
}
