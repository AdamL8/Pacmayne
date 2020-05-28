package entities;


import static configs.GameConfig.GAME_TILE_HEIGHT_COUNT;
import static configs.GameConfig.GAME_TILE_WIDTH_COUNT;

import components.MoveComponent;

public class Maze {
	
	private Tile[][] tiles = new Tile[GAME_TILE_HEIGHT_COUNT][GAME_TILE_WIDTH_COUNT];
	
	public void addTile(Tile tile, int row, int column)
	{
		if (tile != null && row >= 0 && row <= tiles.length && column >= 0 && column <= tiles[0].length)
		{
			tiles[row][column] = tile;
		}
	}
	
	public Tile getTile(int row, int column) {
		if (row >= 0 && row < tiles.length && column >= 0 && column < tiles[0].length)
		{
			return tiles[row][column];
		}
		return null;
	}
	
	public synchronized boolean isInSameCorridor(int beginY, int endY, int beginX, int endX, String axe) {
		if("y".equals(axe)) {
			while(beginY < endY) {
				if (tiles[beginY][beginX].isWall()) {
					return false;
				}
				beginY++;
			}
		} else if("x".equals(axe)) {
			while(beginX < endX) {
				if (tiles[beginY][beginX].isWall()) {
					return false;
				}
				beginX++;
			}
		}
		return true;
	}
	
	public CollisionType validateMove(MoveComponent move, Direction direction)
	{
		CollisionType collisionType = CollisionType.NONE;
		int x = move.getTileX();
		int y = move.getTileY();
		try {
			switch(direction)
			{
			case DOWN:
				if (y >= GAME_TILE_HEIGHT_COUNT - 1) {
					collisionType = CollisionType.OVERBOUND;
				} else if (tiles[y + 1][x].isWall()){
					collisionType = CollisionType.COLLIDEWALL;
				} else if(tiles[y + 1][x].isTunnel()) {
					collisionType = CollisionType.TUNNEL;
				} else if(tiles[y + 1][x].isGate()) {
					collisionType = CollisionType.GATE;
				} 
				break;
			case LEFT:
				if (x <= 0) {
					collisionType = CollisionType.OVERBOUND;
				} else if (tiles[y][x - 1].isWall()){
					collisionType = CollisionType.COLLIDEWALL;
				} else if(tiles[y][x - 1].isTunnel()) {
					collisionType = CollisionType.TUNNEL;
				} else if(tiles[y][x - 1].isGate()) {
					collisionType = CollisionType.GATE;
				} 
				break;
			case RIGHT:
				if (x >= GAME_TILE_WIDTH_COUNT - 1) {
					collisionType = CollisionType.OVERBOUND;
				}else if (tiles[y][x + 1].isWall()){
					collisionType = CollisionType.COLLIDEWALL;
				} else if(tiles[y][x + 1].isTunnel()) {
					collisionType = CollisionType.TUNNEL;
				} else if(tiles[y][x + 1].isGate()) {
					collisionType = CollisionType.GATE;
				}
				break;
			case UP:
				if (y <= 0) {
					collisionType = CollisionType.OVERBOUND;
				} else if (tiles[y - 1][x].isWall()){
					collisionType = CollisionType.COLLIDEWALL;
				} else if(tiles[y - 1][x].isTunnel()) {
					collisionType = CollisionType.TUNNEL;
				} else if(tiles[y - 1][x].isGate()) {
					collisionType = CollisionType.GATE;
				}  
				break;
			case NONE:
				break;
			default:
				break;
			}
		} catch(Exception e) {
			System.out.println("issue with position");
		}
		
		return collisionType;
	}
	
	public Tile[][] getTiles()
	{
		return tiles;
	}
	
	public void resetMap()
	{
		// TODO:
	}
}
