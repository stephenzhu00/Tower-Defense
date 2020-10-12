import java.awt.Color;

public class Tile extends ParentClass{
	public Tile(){
		this.setName("Floor");
		this.setCode(1);
		this.setWeightCost(1);
		this.setColor(Color.BLACK);
		this.setPassable(1);
	}
}

