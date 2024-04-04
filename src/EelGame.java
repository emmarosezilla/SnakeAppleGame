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
        eelies = new Eel[20];
        egg = new Egg();

        for (int x = 0; x < eelies.length; x++) {
            eelies[x] = new Eel(x * 20 + 100, 100);
            eelies[x].pic = Toolkit.getDefaultToolkit().getImage("eelbody.png");
            eelies[x].isAlive = false;
        }
        eelies[0].isAlive = true;
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

        for (int x = eelies.length - 1; x > 0; x--) {


            if (eelies[0].dx < 0) {
               eelies[x].xpos=eelies[x-1].xpos + 20;;
                eelies[x].ypos=eelies[x-1].ypos;
            }

            else if (eelies[0].dx > 0){
                eelies[x].xpos=eelies[x-1].xpos - 20;;
                eelies[x].ypos=eelies[x-1].ypos;
            }

            else if (eelies[0].dy < 0){
                eelies[x].xpos=eelies[x-1].xpos;;
                eelies[x].ypos=eelies[x-1].ypos + 20;
            }

            else{
                eelies[x].xpos=eelies[x-1].xpos;;
                eelies[x].ypos=eelies[x-1].ypos  - 20;
            }


            eelies[x].eelhit = new Rectangle (eelies[x].xpos, eelies[x].ypos, eelies[x].width,eelies[x].height);
        }
        eelies[0].movies();

        egg.move();


    }

    public void collision() {

        for (int x = 0; x < eelies.length; x++) {
            if (eelies[x].eelhit.intersects(egg.hitbox) && eelies[x].isAlive && egg.isAlive ==true &&egg.isCrashing == false) {
                egg.isCrashing = true;
                eelies[numEels].isAlive = true;
                numEels++;
                eatEggSound.play();
                egg.isAlive = false;
                egg = new Egg();
                egg.pic = Toolkit.getDefaultToolkit().getImage("egg.png");



                if (eelies[x].eelhit.intersects(egg.hitbox) == false){
                    eatEggSound.play();
                    egg.isCrashing = false;
                    sharkie.isCollided = true;
                    eelies[numEels].isAlive = false;
                    egg.isAlive = true;
                }
            }


        }
        for (int y = 0; y < eelies.length; y++) {

            if (eelies[y].eelhit.intersects(sharkie.sharkhit) && eelies[y].isAlive && sharkie.isAlive == true) {
                System.out.println("YOU WERE EATEN");
                eelies[numEels].isAlive = false;
                numEels--;
                sharkEatSound.play();
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


                for (int x = 0; x < eelies.length; x++) {
                    if (eelies[x].isAlive == true) {
                        g.drawImage(eelies[x].pic, eelies[x].xpos, eelies[x].ypos, eelies[x].width, eelies[x].height, null);
                    }
                }
                g.drawRect(eelies[0].eelhit.x, eelies[0].eelhit.y, eelies[0].eelhit.width, eelies[0].eelhit.height);

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