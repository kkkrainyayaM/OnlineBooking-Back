package by.project.onlinebooking.controllers;

import by.project.onlinebooking.dto.RouteDto;
import by.project.onlinebooking.entities.Route;
import by.project.onlinebooking.helpers.RouteGenerator;
import by.project.onlinebooking.repositories.RouteRepository;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RouteControllerIT {

    @Autowired
    RouteRepository routeRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:";
    private static final int MIN_SIZE = 1;
    private static final long ID = 1L;
    private static final long SEC_ID = 2L;

    @Test
    public void addRouteWithSuccessStatusTest() {
        RouteDto newRoute = RouteGenerator.generateDto();
        newRoute.setId( SEC_ID );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<RouteDto> request = new HttpEntity<>( newRoute, headers );

        ResponseEntity<RouteDto> responseEntity = this.restTemplate
                .postForEntity( BASE_URL + PORT + "/flights", request, RouteDto.class );


        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertNotNull( responseEntity.getBody() );
        val route = responseEntity.getBody();
        assertEquals( newRoute.getId(), route.getId() );
        assertEquals( newRoute.getDate(), route.getDate() );
        assertEquals( newRoute.getArrivalPoint(), route.getArrivalPoint() );
        assertEquals( newRoute.getArrivalTime(), route.getArrivalTime() );
        assertEquals( newRoute.getDepartureTime(), route.getDepartureTime() );
        assertEquals( newRoute.getDeparturePoint(), route.getDeparturePoint() );

        Optional<Route> op = routeRepository.findById( SEC_ID );
        assertTrue( op.isPresent() );

        routeRepository.deleteById( SEC_ID );
    }

    @Test
    public void deleteRouteWithSuccessStatusTest() {
        Route route = RouteGenerator.generate();
        routeRepository.save( route );
        System.out.println( routeRepository.findAll() );

        this.restTemplate.delete( BASE_URL + PORT + "/flights/{id}", SEC_ID );

        Optional<Route> op = routeRepository.findById( SEC_ID );
        assertFalse( op.isPresent() );

    }

    @Test
    public void updateRouteWithSuccessStatusTest() {
        Route route = RouteGenerator.generate();
        routeRepository.save( route );

        RouteDto routeDto = RouteGenerator.generateDto();
        routeDto.setId( SEC_ID );
        routeDto.setArrivalPoint( "new" );

        this.restTemplate.put( BASE_URL + PORT + "/flights", routeDto );

        assertEquals( routeDto.getArrivalPoint(), routeRepository.findById( SEC_ID ).get().getArrivalPoint() );

        routeRepository.deleteById( SEC_ID );
    }

    @Test
    public void getAllRoutesWithSuccessStatusTest() {
        ResponseEntity<List> responseEntity = this.restTemplate
                .getForEntity( BASE_URL + PORT + "/flights", List.class );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( MIN_SIZE, responseEntity.getBody().size() );
    }

    @Test
    public void getRouteWithSuccessStatusTest() {
        ResponseEntity<RouteDto> responseEntity = this.restTemplate
                .getForEntity( BASE_URL + PORT + "/flights/{id}", RouteDto.class, ID );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( ID, responseEntity.getBody().getId() );
    }
}
