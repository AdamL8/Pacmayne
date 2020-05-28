package components;

import static configs.GameConfig.GAME_TILE_HEIGHT_COUNT;
import static configs.GameConfig.GAME_TILE_WIDTH_COUNT;
import static configs.GameConfig.TILE_SIZE;

import entities.Direction;

public class MoveComponent implements IComponent {
	private int tileX;
	private int tileY;
	private final int spawnX;
	private final int spawnY;
	private double x;
	private double y;
	private Direction direction;
	private Direction awaitingDirection;
	private final Direction spawnDirection;
	private double moveIncrementer = TILE_SIZE / 5.0;
	private double awaitingSpeed = 0;
	private boolean canTurn = false;
	private boolean inTunnel = false;
	private boolean passedGate = false;

	private boolean passingGate = false;
	public boolean isPassingGate() {
		return passingGate;
	}

	public void setPassingGate(boolean passingGate) {
		this.passingGate = passingGate;
	}

	private final boolean canPassTunnel;
	private final boolean canPassGate;
	private boolean canMoveWhenAble = true;
	
	public MoveComponent(double x, double y, Direction direction, boolean canPassTunnel, boolean canPassGate) {
		this.tileX = (int)x;
		this.tileY = (int)y;
		this.spawnX = (int)x;
		this.spawnY = (int)y;
		this.x = x * TILE_SIZE;
		this.y = y * TILE_SIZE;
		this.direction = direction;
		this.spawnDirection = direction;
		this.awaitingDirection = Direction.NONE;
		this.canPassTunnel = canPassTunnel;
		this.canPassGate = canPassGate;
	}
	
	public void resetPosition() {
		tileX = spawnX;
		tileY = spawnY;
		x = (double) spawnX * TILE_SIZE;
		y = (double) spawnY * TILE_SIZE;
		direction = spawnDirection;
		awaitingDirection = Direction.NONE;
	}
	
	public void passTunnel() {
		switch(direction)
		{
		case DOWN:
			y = 0;
			tileY = 0;
			break;
		case LEFT:
			tileX = GAME_TILE_WIDTH_COUNT;
			x = (double) GAME_TILE_WIDTH_COUNT * TILE_SIZE;
			break;
		case RIGHT:
			x = 0;
			tileX = 0;
			break;
		case UP:
			tileY = GAME_TILE_HEIGHT_COUNT;
			y = (double) GAME_TILE_HEIGHT_COUNT * TILE_SIZE;
			break;
		default:
			break;
		}
	}
	
	public void moveOneFrameBySpeed()
	{
		if (!canMoveWhenAble) {
			return;
		}
		
		canTurn = false;
		switch(direction)
		{
		case DOWN:
			y+=moveIncrementer;
			if(isNewTile(y)) {
				reviewSpeed();
				++tileY;
				canTurn = true;
			}
			break;
		case LEFT:
			x-=moveIncrementer;
			if(isNewTile(x)) {
				reviewSpeed();
				--tileX;
				canTurn = true;
			}
			break;
		case RIGHT:
			x+=moveIncrementer;
			if(isNewTile(x)) {
				reviewSpeed();
				++tileX;
				canTurn = true;
			}
			break;
		case UP:
			y-=moveIncrementer;
			if(isNewTile(y)) {
				reviewSpeed();
				--tileY;
				canTurn = true;
			}
			break;
		default:
			break;
		}
	}

	private void reviewSpeed() {
		if(awaitingSpeed != 0) {
			moveIncrementer = awaitingSpeed;
			awaitingSpeed = 0;
		}
	}
	
	private boolean isNewTile(double position) {
		return position % TILE_SIZE == 0;
	}
	
	public void setCanMoveWhenAble(boolean canMove) {
		this.canMoveWhenAble = canMove;
	}
	
	public boolean getCanMoveWhenAble() {
		return this.canMoveWhenAble;
	}
	
	public int getTileX() {
		return tileX;
	}


	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setPassedGate(boolean passedGate) {
		this.passedGate = passedGate;
	}

	public boolean getPassedGate() {
		return this.passedGate;
	}
	
	public int getTileY() {
		return tileY;
	}	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}


	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if ((passedGate && canPassGate) || !canPassGate) {
			this.direction = direction;
		}		
	}

	public Direction getAwaitingDirection() {
		return awaitingDirection;
	}

	public void setAwaitingDirection(Direction awaitingDirection) {
		this.awaitingDirection = awaitingDirection;
	}

	public double getMoveIncrementer() {
		return moveIncrementer;
	}	
	
	public void updateDirection() {
		setDirection(awaitingDirection);
		awaitingDirection = Direction.NONE;
	}
	
	public boolean isInTunnel() {
		return inTunnel;
	}

	public void setInTunnel(boolean inTunnel) {
		this.inTunnel = inTunnel;
	}

	public boolean canTurn() {
		return canTurn;
	}
	
	public boolean canPassTunnel() {
		return canPassTunnel;
	}
	
	public boolean canPassGate() {
		return canPassGate;
	}
	
	public void setIsFast(boolean isFast) {
		awaitingSpeed = isFast ? TILE_SIZE / 4.0 : TILE_SIZE / 5.0;
	}
	
}
