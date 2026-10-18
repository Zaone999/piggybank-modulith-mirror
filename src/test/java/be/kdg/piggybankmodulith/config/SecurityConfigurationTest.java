package be.kdg.piggybankmodulith.config;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.adapter.in.rest.PiggybankController;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import be.kdg.piggybankmodulith.family.port.in.ListPiggybanksQuery;
import be.kdg.piggybankmodulith.family.port.in.WithdrawUseCase;
import be.kdg.piggybankmodulith.grandparents.adapter.in.rest.GrandchildController;
import be.kdg.piggybankmodulith.grandparents.port.in.GrandchildQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PiggybankController.class, GrandchildController.class})
@Import(SecurityConfiguration.class)
class SecurityConfigurationTest {

    private static final SimpleGrantedAuthority PARENT = new SimpleGrantedAuthority("ROLE_parent");
    private static final SimpleGrantedAuthority GRANDPARENT = new SimpleGrantedAuthority("ROLE_grandparent");

    private final UUID owner = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePiggybankUseCase createPiggybankUseCase;
    @MockitoBean
    private DepositUseCase depositUseCase;
    @MockitoBean
    private WithdrawUseCase withdrawUseCase;
    @MockitoBean
    private ListPiggybanksQuery listPiggybanksQuery;
    @MockitoBean
    private GrandchildQuery grandchildQuery;

    @Test
    void withoutATokenEveryRequestIsUnauthorized() throws Exception {
        mockMvc.perform(get("/piggybanks"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/grandchildren"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void aParentListsThePiggybanks() throws Exception {
        mockMvc.perform(get("/piggybanks").with(jwt().authorities(PARENT)))
                .andExpect(status().isOk());
    }

    @Test
    void aGrandparentDoesNotListThePiggybanks() throws Exception {
        mockMvc.perform(get("/piggybanks").with(jwt().authorities(GRANDPARENT)))
                .andExpect(status().isForbidden());
    }

    @Test
    void aTokenWithoutARoleIsForbidden() throws Exception {
        mockMvc.perform(get("/piggybanks").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void parentsAndGrandparentsDeposit() throws Exception {
        when(depositUseCase.deposit(any()))
                .thenReturn(Piggybank.restore(PiggybankId.create(), GrandchildId.of(owner), Money.TEN));

        mockMvc.perform(deposit().with(jwt().authorities(PARENT)))
                .andExpect(status().isOk());
        mockMvc.perform(deposit().with(jwt().authorities(GRANDPARENT)))
                .andExpect(status().isOk());
    }

    @Test
    void onlyAParentWithdraws() throws Exception {
        mockMvc.perform(post("/piggybanks/{ownerId}/withdrawals", owner)
                        .with(jwt().authorities(GRANDPARENT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "amount": 10 }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void onlyAGrandparentListsTheGrandchildren() throws Exception {
        mockMvc.perform(get("/grandchildren").with(jwt().authorities(GRANDPARENT)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/grandchildren").with(jwt().authorities(PARENT)))
                .andExpect(status().isForbidden());
    }

    private MockHttpServletRequestBuilder deposit() {
        return post("/piggybanks/{ownerId}/deposits", owner)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "amount": 10 }
                        """);
    }
}
