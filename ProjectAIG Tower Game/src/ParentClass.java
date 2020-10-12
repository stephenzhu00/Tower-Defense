import java.awt.Color;

public class ParentClass {
	protected String name;
	protected int code;
	protected int height;
	protected int width;
	protected int passable;
	protected int weightCost;
	protected Color color = Color.BLACK;
	
	public ParentClass(){
		this.height = 30;
		this.width = 40;
		this.passable = 0;
		this.name="parent";
		this.code=-1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeight() {
		return height;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getPassable() {
		return passable;
	}
	public void setPassable(int passable) {
		this.passable = passable;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getWeightCost() {
		return weightCost;
	}
	public void setWeightCost(int weightCost) {
		this.weightCost = weightCost;
	}
	
}
