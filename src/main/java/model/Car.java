package model;

import ch.zhaw.pm2.racetrack.given.CarSpecification;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

/**
 * Class representing a car on the racetrack.<br/>
 * Uses {@link PositionVector} to store current position on the track grid and
 * current velocity vector.<br/>
 * Each car has an identifier character which represents the car on the
 * racetrack board.<br/>
 * Also keeps the state, if the car is crashed (not active anymore). The state
 * can not be changed back to uncrashed.<br/>
 * The velocity is changed by providing an acceleration vector.<br/>
 * The car is able to calculate the endpoint of its next position and on request
 * moves to it.<br/>
 */
public class Car implements CarSpecification {

	/** Car identifier used to represent the car on the track */
	private final char id;
	private PositionVector position;
	private PositionVector velocity;
	private MoveStrategy moveStrategy;
	private boolean isCrashed;
	private int lapCounter;

	/**
	 * Constructor for class Car
	 * 
	 * @param id            unique Car identification
	 * @param startPosition initial position of the Car
	 */
	public Car(char id, PositionVector startPosition) {
		this.id = id;
		position = startPosition;
		this.velocity = new PositionVector(0, 0);
		isCrashed = false;
		lapCounter = 0;
	}

	/**
	 * Returns the amount of laps this car has completed
	 * 
	 * @return int lapCounter
	 */
	public int getLapCounter() {
		return lapCounter;
	}

	/**
	 * Increments the lapCounter
	 */
	public void incrementLapCounter() {
		lapCounter++;
	}

	/**
	 * Decrements the lapCounter
	 */
	public void decrementLapCounter() {
		lapCounter--;
	}

	/**
	 * Returns the movestrategy
	 * 
	 * @return return MoveStrategy
	 */
	public MoveStrategy getMoveStrategy() {
		return moveStrategy;
	}

	/**
	 * Sets the movestrategy
	 * 
	 * @param moveStrategy to set
	 */
	public void setMoveStrategy(MoveStrategy moveStrategy) {
		this.moveStrategy = moveStrategy;
	}

	/**
	 * Returns Identifier of the car, which represents the car on the track
	 *
	 * @return identifier character
	 */
	@Override
	public char getId() {
		return this.id;
	}

	/**
	 * Returns a copy of the current position of the car on the track as a
	 * {@link PositionVector}
	 * 
	 * @return copy of the car's current position
	 */
	@Override
	public PositionVector getCurrentPosition() {
		return position;
	}

	/**
	 * Returns a copy of the velocity vector of the car as a
	 * {@link PositionVector}<br/>
	 * It should not be possible to change the cars velocity vector using this
	 * return value.
	 * 
	 * @return copy of car's velocity vector
	 */
	@Override
	public PositionVector getVelocity() {
		return this.velocity;
	}

	/**
	 * Return the position that will apply after the next move at the current
	 * velocity. Does not complete the move, so the current position remains
	 * unchanged.
	 *
	 * @return Expected position after the next move
	 */
	@Override
	public PositionVector getNextPosition() {
		return new PositionVector(this.position).add(velocity);
	}

	/**
	 * Add the specified amounts to this car's velocity.<br/>
	 * The only acceleration values allowed are -1, 0 or 1 in both axis<br/>
	 * There are 9 possible acceleration vectors, which are defined in
	 * {@link Direction}.<br/>
	 * Changes only velocity, not position.<br/>
	 *
	 * @param acceleration A Direction vector containing the amounts to add to the
	 *                     velocity in x and y dimension
	 */
	@Override
	public void accelerate(Direction acceleration) {
		this.velocity = this.velocity.add(acceleration.vector);
	}

	/**
	 * Update this Car's position based on its current velocity.
	 */
	@Override
	public void move() {
		this.position = this.position.add(this.velocity);
	}

	/**
	 * Mark this Car as being crashed at the given position.
	 *
	 * @param crashPosition position the car crashed.
	 */
	@Override
	public void crash(PositionVector crashPosition) {
		position = crashPosition;
		velocity = new PositionVector(0, 0);
		isCrashed = true;
	}

	/**
	 * Returns whether this Car has been marked as crashed.
	 *
	 * @return Returns true if crash() has been called on this Car, false otherwise.
	 */
	@Override
	public boolean isCrashed() {
		return isCrashed;
	}
}
