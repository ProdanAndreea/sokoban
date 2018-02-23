package sokoban;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Box extends Actor 
{

    public Box(int x, int y)
    {
        super(x, y);
        URL loc = this.getClass().getResource("/images/box.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }

    public void move(int x, int y) 
    {
        this.setX(	this.x() + x  );
        this.setY(  this.y() + y  );
    }
}