import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EelGame implements Runnable, KeyListener {
    final int WIDTH = 1000;
    final int HEIGHT = 700;
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;
    // public Character eelie;
    public Character sharkie;
    public boolean eelAndShark;
    public Image backgroundPic;
    public Image sharkPic;
    public Image startPic;
    public Eel[] eelies;
    public int numEels;
    public Egg egg;
    public Egg[] eggies;
    public SoundFile eatEggSound;
    public SoundFile sharkEatSound;
    public boolean gamePlaying = false;
    public boolean gameOver = false;

    public static void main(String[] args) {
        EelGame ex = new EelGame();
        new Thread(ex).start();
    }

    public EelGame() {
        setUpGraphics();
        canvas.addKeyListener(this);
        sharkie = new Character("sharkie", 200, 300);
        backgroundPic = Toolkit.getDefaultToolkit().getImage("background.png");
        sharkPic = Toolkit.getDefaultToolkit().getImage("shark.png");
        startPic = Toolkit.getDefaultToolkit().getImage("start image.png");
        eelies = new Eel[50];
        egg = new Egg();
        eelies[0] = new Eel(300, 100);

        for (int x = 1; x < eelies.length; x++) {
            eelies[x] = new Eel(eelies[x-1].right.x, eelies[x-1].right.y);
            //System.out.println(x * 50 + 300);
            System.out.println(eelies[x].xpos);
            eelies[x].pic = Toolkit.getDefaultToolkit().getImage("eelbody.png");
            eelies[x].isAlive = false;
        }
//        for (int x = 0; x < eelies.length; x++) {
//            System.out.println(eelies[x].xpos + ", " + eelies[x].ypos);
//        }
        eelies[0].isAlive = true;
        eelies[0].pic = Toolkit.getDefaultToolkit().getImage("eelbody.png");
        numEels = 1;

        egg.pic = Toolkit.getDefaultToolkit().getImage("egg.png");

        // for (int x = 0; x < eggies.length; x++) {
        //  eggies[x] = new Egg(x * 50 + 100, 300);
        //   eggies[x].pic = Toolkit.getDefaultToolkit().getImage("egg.png");
        //  eggies[x].dx = (int)(Math.random()*5);
        //   eggies[x].dy = (int)(Math.random()*5);
        //  }

        eatEggSound = new SoundFile("Comical Pop and Swirl.wav");
        sharkEatSound = new SoundFile("Chomp Sound.wav");

    }

    public void run() {
        while (true) {
            moveThings();
            collision();
            render();
            pause(10);
        }
    }

    public void moveThings() {
        sharkie.move();
        eelies[0].movies();

        for (int x = eelies.length - 1; x > 0; x--) {
            eelies[x].xpos = eelies[x - 1].xpos;
            eelies[x].ypos = eelies[x - 1].ypos;
            eelies[x].eelhit = new Rectangle(eelies[x].xpos,eelies[x].ypos,eelies[x].width,eelies[x].height);
//        for (int x = 1; x < eelies.length; x++){
//            if (eelies[x-1].dx < 0) {//moving left
//                System.out.println("left");
//
//                eelies[x].xpos=eelies[x-1].right.x;
//                eelies[x].ypos=eelies[x-1].right.y;
//                eelies[x].dx = eelies[x-1].dx;
//                eelies[x].dy = eelies[x-1].dy;
//            }
//
//            else if (eelies[x-1].dx > 0){
//                System.out.println("right");
//                eelies[x].xpos=eelies[x-1].left.x;
//                eelies[x].ypos=eelies[x-1].left.y;
//                eelies[x].dx = eelies[x-1].dx;
//                eelies[x].dy = eelies[x-1].dy;
//            }
//
//            else if (eelies[x-1].dy < 0){
//                System.out.println("up");
//               eelies[x].xpos=eelies[x-1].above.x;
//                eelies[x].ypos=eelies[x-1].above.y;
//                eelies[x].dx = eelies[x-1].dx;
//                eelies[x].dy = eelies[x-1].dy;
//            }
//
//            else{
//                System.out.println("down");
//                eelies[x].xpos=eelies[x-1].below.x;
//                eelies[x].ypos=eelies[x-1].below.y;
//                eelies[x].dx = eelies[x-1].dx;
//                eelies[x].dy = eelies[x-1].dy;
//            }
//            eelies[x].followerRectangleUpdate();
//            System.out.println(x+":"+eelies[x].xpos+ " , " +eelies[x].ypos);
//
//
//            eelies[x].eelhit = new Rectangle (eelies[x].xpos, eelies[x].ypos, eelies[x].width,eelies[x].height);
//        }

        }

        egg.move();


    }

    public void collision() {

        for (int x = 0; x < eelies.length; x++) {
            if (eelies[x].eelhit.intersects(egg.hitbox) && eelies[x].isAlive && egg.isAlive ==true &&egg.isCrashing == false) {
                egg.isCrashing = true;
                eelies[numEels].isAlive = true;
                numEels++;
                eatEggSound.play();
                System.out.println("egggggggg" + numEels);
                egg.isAlive = false;
                egg = new Egg();
                egg.pic = Toolkit.getDefaultToolkit().getImage("egg.png");
            }

                if (eelies[x].eelhit.intersects(egg.hitbox) == false){
                    eatEggSound.play();
                    egg.isCrashing = false;
                  //  sharkie.isCollided = true;
                    //eelies[numEels-1].isAlive = false;
                    egg.isAlive = true;
                }

        }
        for (int y = 0; y < eelies.length; y++) {

            if (eelies[y].eelhit.intersects(sharkie.sharkhit) && eelies[y].isAlive && sharkie.isAlive == true && sharkie.isCollided == false) {
                System.out.println("YOU WERE EATEN");
                eelies[numEels].isAlive = false;
                sharkie.isCollided = true;
                numEels--;
                sharkEatSound.play();
            }

            if (eelies[y].eelhit.intersects(sharkie.sharkhit) == false){
                sharkie.isCollided = false;
            }
        }

        for (int z = 0; z < eelies.length; z++) {
            if (eelies[0].eelhit.intersects(eelies[z].eelhit) == false && eelies[z].isAlive ) {
                eelies[numEels].isAlive = false;

            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.drawImage(backgroundPic, 0, 0, 1100, 800, null);
        // g.drawImage(eelPic, eelie.xpos,eelie.ypos, eelie.width, eelie.height,null);

        if(numEels >0) {
            g.setColor(new Color(40, 92, 132));
            g.setFont(new Font("Hey Comic", Font.PLAIN, 20));
            g.drawString("Points: " + numEels, 900, 30);

            if (gamePlaying == false) {
                g.drawImage(backgroundPic, 0, 0, 1100, 800, null);
                g.setColor(new Color(40, 92, 132));
                g.setFont(new Font("Hey Comic", Font.PLAIN, 40));
                g.drawString("PRESS THE SPACEBAR TO BEGIN!", 220, 100);
                g.drawImage(startPic, 420, 140, 150, 160, null);

            } else if (gamePlaying == true && gameOver == false) {
                g.drawImage(sharkPic, sharkie.xpos, sharkie.ypos, sharkie.width, sharkie.height, null);


                for (int x = 0; x< eelies.length; x++) {
                    if (eelies[x].isAlive == true) {
                        g.drawImage(eelies[x].pic, eelies[x].xpos, eelies[x].ypos, eelies[x].width, eelies[x].height, null);
                        g.drawRect(eelies[x].eelhit.x, eelies[x].eelhit.y, eelies[x].eelhit.width, eelies[x].eelhit.height);
                        System.out.println(x);
                    }
//                    g.drawImage(eelies[x].pic, eelies[x].xpos, eelies[x].ypos, eelies[x].width, eelies[x].height, null);
//                    System.out.println(eelies[x].xpos+", "+eelies[x].ypos);
//                    System.out.println(x);

                }
//                g.drawImage(eelies[0].pic, eelies[0].xpos, eelies[0].ypos, eelies[0].width, eelies[0].height, null);
//                System.out.println("00000"+eelies[0].xpos+", "+eelies[0].ypos);
//                g.drawRect(eelies[0].eelhit.x, eelies[0].eelhit.y, eelies[0].eelhit.width, eelies[0].eelhit.height);
//                g.drawRect(eelies[0].above.x, eelies[0].above.y, eelies[0].above.width, eelies[0].above.height);
//                g.drawRect(eelies[0].below.x, eelies[0].below.y, eelies[0].below.width, eelies[0].below.height);
//                g.drawRect(eelies[0].right.x, eelies[0].right.y, eelies[0].right.width, eelies[0].right.height);
//                g.drawRect(eelies[0].left.x, eelies[0].left.y, eelies[0].left.width, eelies[0].left.height);

                if (egg.isAlive == true) {
                    g.drawImage(egg.pic, egg.xpos, egg.ypos, egg.width, egg.height, null);
                }
//            }
            } else {
            }
        }
        else {
            g.drawImage(backgroundPic, 0, 0, 1100, 800, null);
            g.setColor(new Color(40, 92, 132));
            g.setFont(new Font("Hey Comic", Font.PLAIN, 40));
            g.drawString("You lose! Try again!", 350, 100);

        }

        g.dispose();
        bufferStrategy.show();
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        int keyCode = e.getKeyCode();
        if (keyCode == 87){
            eelies[0].upIsPressed = true;
        }
        if (keyCode == 83){
            eelies[0].downIsPressed = true;
        }
        if (keyCode == 68){
            eelies[0].rightIsPressed = true;
        }
        if (keyCode == 65){
            eelies[0].leftIsPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        int keyCode = e.getKeyCode();
        if (keyCode == 87) {
            eelies[0].upIsPressed = false;
        }
        if (keyCode == 83){
            eelies[0].downIsPressed = false;
        }
        if (keyCode == 68){
            eelies[0].rightIsPressed = false;
        }
        if (keyCode == 65){
            eelies[0].leftIsPressed = false;
        }

        if (keyCode == 32){
            gamePlaying = true;
        }
    }

}