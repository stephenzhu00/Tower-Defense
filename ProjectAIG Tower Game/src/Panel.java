import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable,KeyListener,MouseListener,MouseMotionListener {
	ParentClass [][] map = new ParentClass[30][40];
	Vector<ListEnemy> spawnerList = new Vector<>();
	Vector<ListEnemy> enemiesList = new Vector<>();
	final int height = 30;
	final int width = 40;
	final int xHome = 20;
	final int yHome = 25;
	Thread thread = new Thread(this);
	
	int gameRunning = 1;
	int mouseX = 2000;
	int mouseY = 2000;
	
	//shortest path
	int [][]weightCost = new int[height][width];
	final int []dirY = {1,-1,0,0};
	final int []dirX = {0,0,1,-1};
	int [][]dist = new int[height+2][width+2];
	boolean [][]visited = new boolean[height][width];
	Node[][] parent = new Node[height+2][width+2];
	//end of shortest path
	
	//spawn enemy
	int prevTime = (int) System.currentTimeMillis();
	int movePrevTime = (int) System.currentTimeMillis();
	int interval = 1000*3;
	Random rand = new Random();
	//end of spawn enemy
	
	public void initializeMap(){
		for(int i = 0 ; i < height; i++){
			Arrays.fill(weightCost[i], 0);
		}
		for(int i = 0 ; i < height; i++){
			for(int j = 0 ; j < width ; j++){
				if( i == 0 || j== 0 || i == height -1 || j == width-1){
					map[i][j] = new Wall();
				}else if(i == yHome && j == xHome){
					map[i][j] = new HomeLand();
				}else if(j == 1 || i == 1 || j == width-2){
					map[i][j] = new Spawner();
					spawnerList.add(new ListEnemy(i,j));
				}else {
					map[i][j] = new Tile();
				}
				weightCost[i][j]= map[i][j].getWeightCost();
			}
		}
	}
	
	
	public Panel(){
		initializeMap();
		addMouseListener(this);
		addMouseMotionListener(this);
		thread.start();
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.BLACK);
		for(int i =0 ; i < height; i++){
			for(int j =0 ; j < width ; j++){
				g.setColor(Color.BLACK);
				if(i == mouseX/20 && j == mouseY/20 && map[i][j].getPassable() ==1){
					int x = j;
					int y = i;
					g.setColor(new Color(255, 0, 255, 30));
					for(int Ay = y-5;Ay < y+6;Ay++){
						for(int Ax = x-5;Ax < x+6;Ax++){
							if(Ax-x+Ay-y+5 >= 0 && Ax-x+Ay-y-5 <= 0 && Ax-x+5-Ay+y >= 0 && Ax-x-5-Ay+y <= 0){
								g.fillRect(Ax*20, Ay*20, 20, 20);
							}
						}
					}
					int[] Tx = {x*20+0, x*20+10, x*20+20};
					int[] Ty = {y*20+20, y*20+0, y*20+20};
					g.setColor(new Color(0, 255, 0, 130));
					g.fillPolygon(Tx, Ty, 3);
				}else if(map[i][j].getName() == "Tower"){
					int x = j;
					int y = i;
					g.setColor(new Color(255, 0, 255, 60));
					for(int Ay = y-5;Ay < y+6;Ay++){
						for(int Ax = x-5;Ax < x+6;Ax++){
							if(Ax-x+Ay-y+5 >= 0 && Ax-x+Ay-y-5 <= 0 && Ax-x+5-Ay+y >= 0 && Ax-x-5-Ay+y <= 0){
								g.fillRect(Ax*20, Ay*20, 20, 20);
							}
						}
					}
					int[] Tx = {x*20+0, x*20+10, x*20+20};
					int[] Ty = {y*20+20, y*20+0, y*20+20};
					g.setColor(new Color(0, 255, 0));
					g.fillPolygon(Tx, Ty, 3);
				}else if(map[i][j].getName() == "Home"){
					int x = j;
					int y = i;
					int[] Hx = {x*20+0, x*20+0, x*20+10, x*20+20, x*20+20};
					int[] Hy = {y*20+20, y*20+10, y*20+0, y*20+10, y*20+20};
					g.setColor(new Color(0, 0, 255));
					g.fillPolygon(Hx, Hy, 5);
					g.setColor(Color.black);
				}else if(map[i][j].getName()=="Enemy"){
					int x = j;
					int y = i;
					g.setColor(new Color(255, 0, 0));
					g.fillOval(x*20, y*20, 19, 19);
					g.setColor(Color.black);
					g.drawRect(j*20, i*20, 20, 20);
				}else if(map[i][j].getName() == "Wall"){
					g.fillRect(j*20, i*20, 20, 20);
				}else if(map[i][j].getName() == "Floor"){
					g.drawRect(j*20, i*20, 20, 20);
				}else if(map[i][j].getName() == "Spawner"){
					int[] S1x = {j*20+0, j*20+2, j*20+20, j*20+18};
					int[] S1y = {i*20+2, i*20+0, i*20+18, i*20+20};
					int[] S2x = {j*20+0, j*20+2, j*20+20, j*20+18};
					int[] S2y = {i*20+18, i*20+20, i*20+2, i*20+0};
					g.setColor(new Color(255, 0, 0));
					g.fillPolygon(S1x, S1y, 4);
					g.fillPolygon(S2x, S2y, 4);
					g.setColor(Color.BLACK);
					g.drawRect(j*20, i*20, 20, 20);
				}
			}
		}
	}
	
	public void printPath(int currY , int currX){
		if(parent[currY][currX].x == -1){
			return;
		}
		Node curr = parent[currY][currX];
		//System.out.printf(curr.y + " " + curr.x + "->"); 
		if(curr.y == 25 && curr.x == 20){
			System.out.println("home");
		}else{
			System.out.println(curr.y +" " +  curr.x +"-> ganti");
		}
		printPath(curr.y , curr.x );
	}
	
	public void djikstra(int y , int x){
		for (Node[] nodes : parent) {
			Arrays.fill(nodes, new Node(-1,-1,-1));
		}
		for (int[] is : dist) {
			Arrays.fill(is, Integer.MAX_VALUE);
		}
		for (boolean[] bs : visited) {
			Arrays.fill(bs, false);
		}
		PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o1.cost-o2.cost;
			}
		});
		Node startNode = new Node(x,y,0);
		pq.add(startNode);
		dist[startNode.y][startNode.x] = 0 ;
		while(!pq.isEmpty()){
			int currX = pq.peek().x;
			int currY = pq.peek().y;
			int currCost = pq.peek().cost;
			pq.poll();
			if(visited[currY][currX] == true)continue;
			visited[currY][currX] = true;
			for(int i =0 ; i < 4 ; i++){
				int nextX = currX +dirX[i];
				int nextY = currY + dirY[i];
				if(nextX < 0 || nextY < 0 ) continue;
				if(nextX > width-1 || nextY > height -1)continue;
				//if(map[nextY][nextX].getName()=="Wall" )continue;
				if(dist[nextY][nextX] > dist[currY][currX]+weightCost[nextY][nextX]){
					dist[nextY][nextX] = dist[currY][currX] + weightCost[nextY][nextX];
					pq.add(new Node(nextX , nextY , dist[nextY][nextX]));
					parent[nextY][nextX] = new Node(currX , currY,dist[nextY][nextX] );
				}
			}
		}
	}
	
	@Override
	public void run() {
		while(true){
			if(gameRunning == 0){
				System.out.println("game paused");
				continue;
			}
			//spawn enemy
			int currentTime = (int) System.currentTimeMillis();
			if(currentTime - prevTime >= interval && spawnerList.size() > 0 ){
				prevTime = currentTime;
				int index = rand.nextInt(spawnerList.size());
				ListEnemy posNewEnemy = spawnerList.get(index);
				spawnerList.remove(index);
				map[posNewEnemy.getCoorY()][posNewEnemy.getCoorX()] = new Enemy();
				enemiesList.add(new ListEnemy(posNewEnemy.getCoorY(), posNewEnemy.getCoorX()));
				if(interval - 100 >= 100){
					interval -= 100;
				}
			}
			//enemy jalan
			if(currentTime - movePrevTime > 1000){
				movePrevTime = currentTime;
				Vector<ListEnemy> tempListEnemy = new Vector<>();
//				System.out.println("Running===");
				int enemiesListSize = enemiesList.size();
				for(int i =0; i < enemiesListSize ; i++){
					//current enemy position
					ListEnemy curr = enemiesList.get(i);
					int currY = curr.getCoorY();
					int currX = curr.getCoorX();
//					System.out.println("Current enemy position: "+currY+ " "+currX);
					
					djikstra(yHome, xHome);
					Node nextCoor= parent[currY][currX];
//					System.out.println(nextCoor.y+ " "+nextCoor.x);
					if(nextCoor.y != yHome || nextCoor.x != xHome && interval == 0){
						map[nextCoor.y][nextCoor.x] = map[currY][currX];
						tempListEnemy.add(new ListEnemy(nextCoor.y, nextCoor.x));
					}
					
					map[currY][currX] = new Tile();
				
				}
//				System.out.println("size: "+enemiesList.size());
				enemiesList= new Vector(tempListEnemy);
			}
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Pressed");
		if(arg0.getKeyCode() == KeyEvent.VK_P){
			gameRunning =(gameRunning+1)%2;
			System.out.println(gameRunning);
		}
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		System.out.println("Keyreleased ");
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	
	public void mouseClicked(MouseEvent e) {
		//test mouse
		this.mouseX = e.getY();
		this.mouseY = e.getX();
//		System.out.println("X : " + mouseX + "  Y : " + mouseY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouseX = e.getY();
		this.mouseY = e.getX();
	//	System.out.println("X : " + mouseX + "  Y : " + mouseY);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		this.mouseX = e.getY();
		this.mouseY = e.getX();
		System.out.println("PRessed at X: "+mouseX+" Y: "+mouseY);
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
}
