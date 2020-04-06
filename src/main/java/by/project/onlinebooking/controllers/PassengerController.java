package by.project.onlinebooking.controllers;

import by.project.onlinebooking.dto.PassengerDto;
import by.project.onlinebooking.services.PassengerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @ApiOperation(value = "Get list of passengers of route")
    @GetMapping("/flights/{id}/passengers")
    public List<PassengerDto> getPassengersOfRoute(@PathVariable long id) {
        return passengerService.getAllByRouteId( id );//?
    }

    @ApiOperation(value = "Add a passenger")
    @PostMapping("/passengers")
    public PassengerDto newPassenger(@RequestBody PassengerDto passenger) {
        return passengerService.add( passenger );
    }

    @ApiOperation(value = "Delete a passenger by user id")
    @DeleteMapping("/passengers/user/{id}")
    public void deletePassengerByUserId(@PathVariable(value = "id") long id) {
        passengerService.deleteByUserId( id );
    }

    @ApiOperation(value = "Delete a passenger by flight id")
    @DeleteMapping("/passengers/flight/{id}")
    public void deletePassengerByRouteId(@PathVariable(value = "id") long id) {
        passengerService.deleteByRouteId( id );
    }

    @ApiOperation(value = "Update a passenger")
    @PutMapping("/passengers")
    public PassengerDto updatePassenger(@Valid @RequestBody PassengerDto passenger) {
        return passengerService.update( passenger );
    }
}
