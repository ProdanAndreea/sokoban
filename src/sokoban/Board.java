package sokoban;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Board extends JPanel
{
	 	private final int OFFSET = 100;
	    private final int SPACE = 45; // 45x45px
	    private final int LEFT_COLLISION = 1;
	    private final int RIGHT_COLLISION = 2;
	    private final int TOP_COLLISION = 3;
	    private final int BOTTOM_COLLISION = 4;

	    private ArrayList<Actor> walls = new ArrayList<Actor>();
	    private ArrayList<Actor> boxes = new ArrayList<Actor>();
	    private ArrayList<Actor> areas = new ArrayList<Actor>();
	    private Player soko;
	    
	    private int w = 0;  /* board width */
	    private int h = 0;	/* board height */
	    private boolean completed = false;
	    
	    
	    /*	Map:
	     *  # - wall
	     *  * - box
	     *  @ - sokoban
	     *  . - the place to move a box
	     */
	    private String map =
					              "  #####      #####\n"
	    						+ "  #   #      #   #\n"
					            + "  #   ########   #\n"
					            + "  #     #        #\n"
					            + "  #     #        #\n"
					            + "###    ######    #######\n"
					            + "#@         *          .#\n"
					            + "###    ######         .#\n"
					            + " ## *                 .#\n"
					            + " ##      ##           .#\n"
					            + " ##      **          ###\n"
					            + " ##                  ###\n"
					            + " ##   ############   #\n"
					            + " #####################\n";
	/*				        
	    	      "    ######\n"
		            + "    ##   #\n"
		            + "    ##*  #\n"
		            + "  ####  *##\n"
		            + "  ##  * * #\n"
		            + "#### # ## #   ######\n"
		            + "##   # ## #####  ..#\n"
		            + "## *  *  @       ..#\n"
		            + "###### ### # ##  ..#\n"
		            + "    ##     #########\n"
		            + "    ########\n";
 */
	    public Board() {

	        addKeyListener(new KAdapter()); // to receive key events from this component. 
	        setFocusable(true);
	        initWorld();
	    }

	    public int getBoardWidth() {
	        return this.w;
	    }

	    public int getBoardHeight() {
	        return this.h;
	    }

	    /* initiate the world */
	    public final void initWorld() 
	    {      
	        int x = OFFSET;
	        int y = OFFSET;

	        for (int i = 0; i < map.length(); i++) 
	        {
	            char item = map.charAt(i);

	            if (item == '\n')
	            {
	                y += SPACE;
	                if (this.w < x) {
	                    this.w = x;
	                }
	                x = OFFSET;     
	            } 
	            else if (item == '#') 
	            {
	                walls.add(new Wall(x, y));
	                x += SPACE;
	            } 
	            else if (item == '*')
	            {
	                boxes.add(new Box(x, y));
	                x += SPACE;
	            }
	            else if (item == '.') 
	            {
	                areas.add(new Area(x, y));
	                x += SPACE;
	            } 
	            else if (item == '@') 
	            {
	                soko = new Player(x, y);
	                x += SPACE;
	            } 
	            else if (item == ' ') 
	            {
	                x += SPACE;
	            } 
	        }
	        
	        h = y;
	    }
	    
	    
	    /* draw the world on the window */
	    public void buildWorld(Graphics g) 
	    {

	        g.setColor(new Color(229, 229, 229));
	        g.fillRect(0, 0, this.getWidth(), this.getHeight());  /* specify the rectangle to be filled */
	        
	        /* include all the objects in the world */
	        ArrayList<Actor> world = new ArrayList<Actor>();
	        world.addAll(walls);
	        world.addAll(areas);
	        world.addAll(boxes);
	        world.add(soko);

	        /* draw the objects */
	        for (int i = 0; i < world.size(); i++) 
	        {
	            Actor item = (Actor) world.get(i);

	            if(world.get(i) instanceof Area)
	            {
	            	g.drawImage(item.getImage(), item.x(), item.y()-2, 45, 45, this);
	            	//System.out.println("area");
	            }
	            else if(world.get(i) instanceof Player)
	            {
	            	g.drawImage(item.getImage(), item.x(), item.y(), 45, 45, this);
            		//System.out.println("player");
	            }
	            else
	            	g.drawImage(item.getImage(), item.x(), item.y(), 46,46, this);

	            /* if the level is completed */
	            if (completed) 
	            {
	            	g.setColor(new Color(225, 100, 80));
	                g.setFont(new Font("SansSerif", Font.PLAIN, 45));
	                g.drawString("Completed !", 25, 45);
	            } 
	        }
	    }

	    @Override
	    public void paint(Graphics g) 
	    {
	        super.paint(g);
	        buildWorld(g);
	    }

	    
	    class KAdapter extends KeyAdapter 
	    {
	        @Override
	        public void keyPressed(KeyEvent e) 
	        {
	            if (completed) { return; }

	            int key = e.getKeyCode();

	            if (key == KeyEvent.VK_LEFT) 
	            {
	                if (checkWallCollision(soko, LEFT_COLLISION)) { return; }

	                if (checkBoxCollision(LEFT_COLLISION)) { return; }

	                soko.move(-SPACE, 0);
	            } 
	            else if (key == KeyEvent.VK_RIGHT)
	            {
	                if (checkWallCollision(soko, RIGHT_COLLISION)) { return; }

	                if (checkBoxCollision(RIGHT_COLLISION)) { return; }

	                soko.move(SPACE, 0);
	            } 
	            else if (key == KeyEvent.VK_UP) 
	            {
	                if (checkWallCollision(soko, TOP_COLLISION)) { return; }

	                if (checkBoxCollision(TOP_COLLISION)) { return; }

	                soko.move(0, -SPACE);
	            } 
	            else if (key == KeyEvent.VK_DOWN) 
	            {

	                if (checkWallCollision(soko, BOTTOM_COLLISION)) { return; }

	                if (checkBoxCollision(BOTTOM_COLLISION)) { return; }

	                soko.move(0, SPACE);
	            } 
	            else if (key == KeyEvent.VK_R) 
	            {
	            	restartLevel();
	            }

	            repaint();
	        }
	    }

	    private boolean checkWallCollision(Actor actor, int type) 
	    {
	    	 
	        if (type == LEFT_COLLISION) 
	        {
	            for (int i = 0; i < walls.size(); i++) 
	            {
	                Wall wall = (Wall) walls.get(i);
	                if (actor.isLeftCollision(wall)) 
	                {
	                    return true;
	                }
	            }
	            return false;
	        } 
	        else if (type == RIGHT_COLLISION) 
	        {
	            for (int i = 0; i < walls.size(); i++) 
	            {
	                Wall wall = (Wall) walls.get(i);
	                if (actor.isRightCollision(wall)) 
	                {
	                    return true;
	                }
	            }
	            return false;
	        } 
	        else if (type == TOP_COLLISION) 
	        {
	            for (int i = 0; i < walls.size(); i++) 
	            {
	                Wall wall = (Wall) walls.get(i);
	                if (actor.isTopCollision(wall)) 
	                {
	                    return true;
	                }
	            }
	            return false;
	        } 
	        else if (type == BOTTOM_COLLISION) 
	        {
	            for (int i = 0; i < walls.size(); i++) 
	            {
	                Wall wall = (Wall) walls.get(i);
	                if (actor.isBottomCollision(wall)) 
	                {
	                    return true;
	                }
	            }
	            return false;
	        }
	        return false;
	    }
	    
	    
	    
	    private boolean checkBox(int type, Box box)
	    {
	    	if(type == LEFT_COLLISION)
	    	{
		    	/* verify if it hits another box */
		    	for (int i = 0; i < boxes.size(); i++)
	            {
	                Box item = (Box) boxes.get(i);
	                if (!box.equals(item))
	                {  	
	                	if(type == LEFT_COLLISION)
	                	{
		                	if(box.isLeftCollision(item))
		                	{
		                		checkBox(LEFT_COLLISION, item);
		                		/* verify if the collided box have been moved, and if not, don't make any move */    
		                		if(box.isLeftCollision(item))
		                			return false;
		                		break;  /* the collided box have been found so there's no point to verify the others */
		                	}
	                	}	
	                }     	
	            }

		    	if (checkWallCollision(box, LEFT_COLLISION) == false) 
		    		box.move(-SPACE, 0);
		    	else return false;
	    	}
	    	else if (type == RIGHT_COLLISION)
	    	{
		    	for (int i = 0; i < boxes.size(); i++)
	            {
	                Box item = (Box) boxes.get(i);
	                if (!box.equals(item))
	                {                	
	                	if(type == RIGHT_COLLISION)
	                	{
		                	if(box.isRightCollision(item))
		                	{
		                		checkBox(RIGHT_COLLISION, item);
    
		                		if(box.isRightCollision(item))
		                			return false;
		                		break;  
		                	}
	                	}
	                }     	
	            }

		    	if (checkWallCollision(box, RIGHT_COLLISION) == false) 
		    		box.move(SPACE, 0);
		    	else return false;
	    	}
	    	else if (type == TOP_COLLISION)
	    	{
	    		for (int i = 0; i < boxes.size(); i++)
	            {
	                Box item = (Box) boxes.get(i);
	                if (!box.equals(item))
	                {     	
	                	if(type == TOP_COLLISION)
	                	{
		                	if(box.isTopCollision(item))
		                	{
		                		checkBox(TOP_COLLISION, item);
    
		                		if(box.isTopCollision(item))
		                			return false;
		                		break;  
		                	}
	                	}
	                }     	
	            }

		    	if (checkWallCollision(box, TOP_COLLISION) == false) 
		    		box.move(0, -SPACE);
		    	else return false;
	    	}
	    	else if (type == BOTTOM_COLLISION)
	    	{
	    		for (int i = 0; i < boxes.size(); i++)
	            {
	                Box item = (Box) boxes.get(i);
	                if (!box.equals(item))
	                {	
	                	if(type == BOTTOM_COLLISION)
	                	{
		                	if(box.isBottomCollision(item))
		                	{
		                		checkBox(BOTTOM_COLLISION, item);
    
		                		if(box.isBottomCollision(item))
		                			return false;
		                		break;  
		                	}
	                	}
	                }     	
	            }

		    	if (checkWallCollision(box, BOTTOM_COLLISION) == false) 
		    		box.move(0, SPACE);
		    	else return false;
	    	}
	    	
		    return true;     	
         }
	    	
	    
	    

	    /* checks if box collision */
	    /* returns true if it does */
	    private boolean checkBoxCollision(int type)
	    {
	    	Boolean flag;
	        if (type == LEFT_COLLISION)
	        {
	        	/* you don't know which box the sokoban hits => verify each box in part */
	            for (int i = 0; i < boxes.size(); i++)
	            {
	                Box box = (Box) boxes.get(i);
	                if (soko.isLeftCollision(box)) 
	                {                		
	                	flag = checkBox(LEFT_COLLISION, box);
	                	
	                	if(flag == false)
	                		return true;

	                    isCompleted();
	                }
	            }
	            return false;

	        }
	        else if (type == RIGHT_COLLISION) 
	        {
	            for (int i = 0; i < boxes.size(); i++) 
	            {
	                Box box = (Box) boxes.get(i);
	                if (soko.isRightCollision(box)) 
	                {
	                	flag = checkBox(RIGHT_COLLISION, box);
	                	
	                	if(flag == false)
	                		return true;

	                    isCompleted();                
	                }
	            }
	            return false;

	        } else if (type == TOP_COLLISION) 
	        {

	            for (int i = 0; i < boxes.size(); i++) 
	            {

	                Box box = (Box) boxes.get(i);
	                if (soko.isTopCollision(box)) 
	                {
	                	flag = checkBox(TOP_COLLISION, box);
	                	
	                	if(flag == false)
	                		return true;

	                    isCompleted();
	                }
	            }

	            return false;

	        } 
	        else if (type == BOTTOM_COLLISION) 
	        {
	            for (int i = 0; i < boxes.size(); i++) 
	            {
	                Box box = (Box) boxes.get(i);
	                if (soko.isBottomCollision(box)) 
	                {
	                	flag = checkBox(BOTTOM_COLLISION, box);
	                	
	                	if(flag == false)
	                		return true;

	                    isCompleted();
	                }
	            }
	        }
	        return false;
	    }

	    
	    public void isCompleted() 
	    {
	        int n = boxes.size();
	        int compl = 0;

	        for (int i = 0; i < n; i++)
	        {
	            Box box = (Box) boxes.get(i);
	            for (int j = 0; j < n; j++) 
	            {
	                Area area = (Area) areas.get(j);
	                if (box.x() == area.x() && box.y() == area.y()) 
	                {
	                    compl += 1;
	                }
	            }
	        }

	        if (compl == n)
	        {
	            completed = true;
	            repaint();
	        }
	    }

	    
	    public void restartLevel() 
	    {
	        areas.clear();
	        boxes.clear();
	        walls.clear();
	        initWorld();
	        if (completed) 
	        {
	            completed = false;
	        }
	    }
	    
}

