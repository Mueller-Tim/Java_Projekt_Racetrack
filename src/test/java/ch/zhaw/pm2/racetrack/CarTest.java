package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Car;
import model.Direction;
import model.PositionVector;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The CarTest class contains jUnit tests which test the functionality of the Car class.
 */
public class CarTest {

    private Car car;
    private final char initCharValue = '$';
    private final int xInitValuePosition = 14;
    private final int yInitValuePosition = 15;


    @BeforeEach
    void initializeCarTest() {
        this.car = new Car(initCharValue, new PositionVector(xInitValuePosition, yInitValuePosition));
    }

    /**
     * description: test every velocity change possible in the game
     * equivalence class: 1
     * initial condition: initialized car object with a velocity of (2,1)
     * type: positive test
     * input: Direction
     * output: changed velocity
     */
    @Test
    void accelerateCar() {
        this.car.accelerate(Direction.DOWN);
        this.car.accelerate(Direction.RIGHT);
        this.car.accelerate(Direction.RIGHT);
        this.car.accelerate(Direction.NONE);
        assertEquals(new PositionVector(2, 1), this.car.getVelocity(), "new velocity = (2,1) + (0,0) = (0,0)");
        this.car.accelerate(Direction.DOWN);
        assertEquals(new PositionVector(2, 2), this.car.getVelocity(), "new velocity = (2,1) + (0,1) = (2,2)");
        this.car.accelerate(Direction.DOWN_RIGHT);
        assertEquals(new PositionVector(3, 3), this.car.getVelocity(),"new velocity = (2,2) + (1,1) = (3,3)");
        this.car.accelerate(Direction.RIGHT);
        assertEquals(new PositionVector(4, 3), this.car.getVelocity(),"new velocity = (3,3) + (1,0) = (4,3)");
        this.car.accelerate(Direction.UP_RIGHT);
        assertEquals(new PositionVector(5, 2), this.car.getVelocity(),"new velocity = (4,3) + (1,-1) = (5,2)");
        this.car.accelerate(Direction.UP);
        assertEquals(new PositionVector(5, 1), this.car.getVelocity(),"new velocity = (5,2) + (0,-1) = (5,1)");
        this.car.accelerate(Direction.UP_LEFT);
        assertEquals(new PositionVector(4, 0), this.car.getVelocity(),"new velocity = (5,1) + (-1,-1) = (4,0)");
        this.car.accelerate(Direction.LEFT);
        assertEquals(new PositionVector(3, 0), this.car.getVelocity(),"new velocity = (4,0) + (-1,0) = (3,0)");
        this.car.accelerate(Direction.DOWN_LEFT);
        assertEquals(new PositionVector(2, 1), this.car.getVelocity(),"new velocity = (3,0) + (-1,1) = (2,1)");
    }

    /**
     * description: test car movement with a certain velocity
     * equivalence class: 1
     * initial condition: initialized car object
     * type: positive test
     * input: car acceleration
     * output: moved car position
     */
    @Test
    void moveCar() {
        this.car.accelerate(Direction.DOWN_RIGHT);
        this.car.accelerate(Direction.DOWN_RIGHT);
        this.car.accelerate(Direction.DOWN);
        this.car.accelerate(Direction.NONE);
        this.car.accelerate(Direction.LEFT);
        this.car.accelerate(Direction.UP_RIGHT);
        this.car.move();
        assertEquals(new PositionVector(xInitValuePosition + 2, yInitValuePosition + 2), car.getCurrentPosition());
    }


    /**
     * description: calculate next position of car with current velocity without moving
     * equivalence class: 1
     * initial condition: initialized car object
     * type: positive test
     * input: car acceleration
     * output: calculated next car position
     */
    @Test
    void calculateNextPosition() {
        this.car.accelerate(Direction.DOWN_RIGHT);
        this.car.accelerate(Direction.DOWN_RIGHT);
        this.car.accelerate(Direction.DOWN);
        this.car.accelerate(Direction.NONE);
        this.car.accelerate(Direction.LEFT);
        this.car.accelerate(Direction.UP_RIGHT);
        assertEquals(new PositionVector(xInitValuePosition + 2, yInitValuePosition + 2), car.getNextPosition());
        assertEquals(new PositionVector(xInitValuePosition, yInitValuePosition), car.getCurrentPosition());
    }


    /**
     * description: tests if all data fields are changed to show the crashed status
     * equivalence class: 1
     * initial condition: initialized car object
     * type: positive test
     * input: car crash on the crash position
     * output: sets the velocity to (0,0), isCrashed to true and position to the crash position
     */
    @Test
    void crash(){
        PositionVector crashPosition = new PositionVector(3, 7);
        PositionVector crashVelocity = new PositionVector(0,0);
        car.crash(crashPosition);
        assertTrue(car.isCrashed());
        assertEquals(crashPosition, car.getCurrentPosition());
        assertEquals(crashVelocity, car.getVelocity());
    }


    /**
     * description: tests if the lapCounter increment and decrement method are working.
     * equivalence class: 1
     * initial condition: initialized car object
     * type: positive test
     * input: car increment and decrement his lap counter
     * output: after the lap increments, the is one count higher
     *         and after the decrement it's  one count lower
     */
    @Test
    void lapCounter(){
        int lapCounter = car.getLapCounter();
        car.incrementLapCounter();
        assertEquals(++lapCounter, car.getLapCounter());
        car.decrementLapCounter();
        assertEquals(--lapCounter, car.getLapCounter());
    }
}
