import java.awt.Color;

public class Enemy extends ParentClass {
	private int health;
	
	public Enemy(){
		this.setName("Enemy");
		this.setCode(5);
		this.setPassable(0);
		this.health=100;
		this.setColor(Color.red);
		this.setWeightCost(600);
	}
}
