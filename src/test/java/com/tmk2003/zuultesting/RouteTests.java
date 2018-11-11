package com.tmk2003.zuultesting;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final String TEST_PARAM_RESULT              = "test_search_param";              // Test response from mock services
    private final String TEST_PARAM_NAME                = "FireHouse";                      // Test response from mock services
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
        // When the mock user service get hit at its /horse endpoint, lets return successful
        mockUserService.stubFor(get(urlEqualTo("/horse"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/user/horse"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockUserService.verify(1, getRequestedFor(urlPathEqualTo("/horse"))); // Ensure it was hit once
    }

    /**
     * Ensure the /User?x=y&w=z path routes to User service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToUserWithParametersRouteToUserServiceWithParameters() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        //
        // !Important: you must use urlPathEqualTo or urlPathMatching to specify the path,
        // as urlEqualTo or urlMatching will attempt to match the whole request URL,
        // including the query parameters
        mockUserService.stubFor(
                get(urlPathEqualTo("/"))
                .withQueryParam(TEST_PARAM_NAME, equalTo(TEST_PARAM_RESULT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(routeBuilder("/user"))
                .queryParam(TEST_PARAM_NAME, TEST_PARAM_RESULT);

        ResponseEntity<String> response = TEMPLATE.getForEntity(builder.toUriString(), String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockUserService.verify(1, getRequestedFor(urlPathEqualTo("/"))); // Ensure it was hit once
    }

    /**
     * Ensure the /User/**?x=y&w=z path routes to User service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToUserWithParametersAndExtensionsRouteToUserServiceWithParametersAndExtensions() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        //
        // !Important: you must use urlPathEqualTo or urlPathMatching to specify the path,
        // as urlEqualTo or urlMatching will attempt to match the whole request URL,
        // including the query parameters
        mockUserService.stubFor(
                get(urlPathEqualTo("/horse"))
                        .withQueryParam(TEST_PARAM_NAME, equalTo(TEST_PARAM_RESULT))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "text/json")
                                .withStatus(200)
                                .withBody(TEST_BODY)));

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(routeBuilder("/user/horse"))
                .queryParam(TEST_PARAM_NAME, TEST_PARAM_RESULT);

        ResponseEntity<String> response = TEMPLATE.getForEntity(builder.toUriString(), String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockUserService.verify(1, getRequestedFor(urlPathEqualTo("/horse"))); // Ensure it was hit once
    }

    /* Moderator Routes */

    /**
     * Ensure the /Moderator path routes to the admin service
     */
    @Test
    public void whenPathingToModeratorRouteToModeratorService() {
        // When the mock user service get hit at its "home ('/')", lets return successful
        mockModService.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/mod"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockModService.verify(1, getRequestedFor(urlPathEqualTo("/"))); // Ensure it was hit once
    }

    /**
     * Ensure the /Moderator/** path routes to the Moderator service and maintains /** extension
     */
    @Test
    public void whenPathingToModeratorWithExtensionsRouteToModeratorServiceWithExtensions() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        mockModService.stubFor(get(urlEqualTo("/horse"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/mod/horse"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockModService.verify(1, getRequestedFor(urlPathEqualTo("/horse"))); // Ensure it was hit once
    }

    /**
     * Ensure the /Moderator?x=y&w=z path routes to Moderator service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToModeratorWithParametersRouteToModeratorServiceWithParameters() {
        // TODO
        Assert.fail();
    }
-
    /**
     * Ensure the /Moderator/**?x=y&w=z path routes to Moderator service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToModeratorWithParametersAndExtensionsRouteToModeratorServiceWithParametersAndExtensions() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        //
        // !Important: you must use urlPathEqualTo or urlPathMatching to specify the path,
        // as urlEqualTo or urlMatching will attempt to match the whole request URL,
        // including the query parameters
        mockModService.stubFor(
                get(urlPathEqualTo("/horse"))
                        .withQueryParam(TEST_PARAM_NAME, equalTo(TEST_PARAM_RESULT))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "text/json")
                                .withStatus(200)
                                .withBody(TEST_BODY)));

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(routeBuilder("/mod/horse"))
                .queryParam(TEST_PARAM_NAME, TEST_PARAM_RESULT);

        ResponseEntity<String> response = TEMPLATE.getForEntity(builder.toUriString(), String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockModService.verify(1, getRequestedFor(urlPathEqualTo("/horse"))); // Ensure it was hit once
    }

    /* Admin Routes */

    /**
     * Ensure the /admin path routes to the admin service
     */
    @Test
    public void whenPathingToAdminRouteToAdminService() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        mockAdminService.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/admin"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockAdminService.verify(1, getRequestedFor(urlPathEqualTo("/"))); // Ensure it was hit once
    }

    /**
     * Ensure the /admin/** path routes to the admin service and maintains /** extension
     */
    @Test
    public void whenPathingToAdminWithExtensionsRouteToAdminServiceWithExtensions() {
        // When the mock user service get hit at its /horse endpoint, lets return successful
        mockAdminService.stubFor(get(urlEqualTo("/horse"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody(TEST_BODY)));

        ResponseEntity<String> response = TEMPLATE.getForEntity(routeBuilder("/admin/horse"),String.class);

        assertNotNull(response);                                                       // Response exists
        assertEquals(TEST_BODY, response.getBody());                                   // It get the body
        assertEquals(HttpStatus.OK, response.getStatusCode());                         // It was successful
        mockAdminService.verify(1, getRequestedFor(urlPathEqualTo("/horse"))); // Ensure it was hit once
    }

    /**
     * Ensure the /admin?x=y&w=z path routes to admin service and maintains the ?x=y&w=z parameters
     */
    @Test
    public void whenPathingToAdminWithParametersRouteToAdminServiceWithParameters() {
        // TODO
        Assert.fail();
    }

    /**
     * Ensure the /admin/**?x=y&w=z path routes to admin service and maintains the ?x=y&w=z parameters and /** extension
     */
    @Test
    public void whenPathingToAdminWithParametersAndExtensionsRouteToAdminServiceWithParametersAndExtensions() {
        // TODO
        Assert.fail();
    }

}
