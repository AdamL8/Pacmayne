package entities;

public class Tile {
	
	private final int x;
	private final int y;
	private TileType type;
	
	public Tile(int x, int y, TileType type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public boolean isWall() {
		return type == TileType.WALL;
	}
	
	public boolean isTunnel() {
		return type == TileType.TUNNEL;
	}
	
	public boolean isGate() {
		return type == TileType.GATE;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
