package com.tmk2003.zuultesting;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteTests {

    /* Variables */
    @Rule public final WireMockRule mockUserService     = new WireMockRule(8081);     // Mocked User Service
    @Rule public final WireMockRule mockAdminService    = new WireMockRule(8082);     // Mocked Admin Service
    @Rule public final WireMockRule mockModService      = new WireMockRule(8083);     // Mocked Mod Service
    @LocalServerPort int port;                                                              // Current port for Zuul Gateway

    /* Final Variables */
    private final String TEST_BODY                      = "Tester";                         // Test response from mock services
    private final TestRestTemplate TEMPLATE             = new TestRestTemplate();           // Test rest template for routing

    /**
     * Helper for building localhost & port pathings
     * @param path relative path to route to
     * @return http://localhost:${port}${path}
     */
    private String routeBuilder(String path) {
        return "http://localhost:" + port + path;
    }

    /* User Routes */

    /**
     * Ensure the /User path routes to the User service
     */
    @Test
    public void whenPathingToUserRouteToUserService() {
        // When the mock user service get hit at its "home ('/')", lets return successful
        mockUserService.stubFor(get(urlEqualTo("/"))
                        .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/user"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockUserService.verify(1, getRequestedFor(urlPathEqualTo("/"))); // Ensure it was hit once
    }

    /**
     * Ensure the /User/** path routes to the User service and maintains /** extension
     */
    @Test
    public void whenPathingToUserWithExtensionsRouteToUserServiceWithExtensions() {

    }

    /**
     * Ensure the /User?x=y&w=z path routes to User service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToUserWithParametersRouteToUserServiceWithParameters() {

    }

    /**
     * Ensure the /User/**?x=y&w=z path routes to User service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToUserWithParametersAndExtensionsRouteToUserServiceWithParametersAndExtensions() {

    }

    /* Moderator Routes */

    /**
     * Ensure the /Moderator path routes to the admin service
     */
    @Test
    public void whenPathingToModeratorRouteToModeratorService() {

    }

    /**
     * Ensure the /Moderator/** path routes to the Moderator service and maintains /** extension
     */
    @Test
    public void whenPathingToModeratorWithExtensionsRouteToModeratorServiceWithExtensions() {

    }

    /**
     * Ensure the /Moderator?x=y&w=z path routes to Moderator service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToModeratorWithParametersRouteToModeratorServiceWithParameters() {

    }

    /**
     * Ensure the /Moderator/**?x=y&w=z path routes to Moderator service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToModeratorWithParametersAndExtensionsRouteToModeratorServiceWithParametersAndExtensions() {

    }

    /* Admin Routes */

    /**
     * Ensure the /admin path routes to the admin service
     */
    @Test
    public void whenPathingToAdminRouteToAdminService() {

    }

    /**
     * Ensure the /admin/** path routes to the admin service and maintains /** extension
     */
    @Test
    public void whenPathingToAdminWithExtensionsRouteToAdminServiceWithExtensions() {

    }

    /**
     * Ensure the /admin?x=y&w=z path routes to admin service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToAdminWithParametersRouteToAdminServiceWithParameters() {

    }

    /**
     * Ensure the /admin/**?x=y&w=z path routes to admin service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToAdminWithParametersAndExtensionsRouteToAdminServiceWithParametersAndExtensions() {

    }

}
