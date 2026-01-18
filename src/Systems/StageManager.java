package Systems;
import Objects.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 *
 * @author Helpful273
 */
public class StageManager {
    private static int KEY_Z = 90; 
    
    // properties
    private HashMap<String, Stage> stages = new HashMap<>();;
    private Stage stage = null;
    
    // core
    private Random random = new Random();
    private PApplet app;
    private HashSet<Integer> keysPressed = new HashSet<>();
    private boolean mouseDown, clickSwitch = false;
    
    private int mouseX, mouseY = 0;
    private int mouseButton = 0;
    private int score = 0;
    
    public StageManager(PApplet app) {
        this.app = app;
    }
    
    // fired upon startup
    public void awake() {
        stages.put("menu", menu);
        stages.put("highscore", highscore);
        
        stages.put("intro", intro);
        stages.put("intermission1", intermission1);
        stages.put("intermission2", intermission2);
        stages.put("ending", ending);
        
        stages.put("combat1", combat1);
        stages.put("combat2", combat2);
        stages.put("combat3", combat3);
        
        stages.put("death", death);
        
        // automatically put users to the start screen.
        switchStage("menu");
    }
    
    /*
    The functions below acts as a communication layer between the manager and
    each individual stage.
    */
    public void update() {
        stage.update();
    }
    
    public void draw() {
        stage.draw();
    }
    // comm functions layer end.
    
    /*
    The functions below handle and manage the inputs from mouse and keyboard
    and updates respective values that can be used in individual stages
    */
    public void keyDown(int keyCode) {
        keysPressed.add(keyCode);
    }
    
    public void keyUp(int keyCode) {
        keysPressed.remove(keyCode);
    }
    
    public void mouseDown(int mouseX, int mouseY, int mouseButton) {
        mouseDown = true;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
    }
    
    public void mouseUp() {
        mouseDown = false;
        clickSwitch = false;
    }
    // input handler end.
    
    /*
    Switches the current stage to another.
    @param newStage The name of the stage.
    */
    public void switchStage(String newStage) {
        stage = stages.get(newStage);
        stage.awakeFn();
    }
    
    /*
    Write current score to a file
    */
    public void writeScore() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("score.txt", true));
            writer.printf("%d\n", score); // write score
            writer.close();
        } catch (IOException e) {} // catch error
        
        score = 0;
    }
    
    /*
    Below are where anonymous classes are created for each individual scene.
    NOTE: majority of the code is creating objects and making bullet patterns.
        I will NOT be explaining how bullet patterns are created because all of
        them are editing and tweaking values until satisfactory. The only thing
        that will be explained are the mechanisms involving GUI and data
        handling.
    */
    
    Stage menu = new Stage() {
        private RectangleObject background;
        private RectangleObject startButton;
        private RectangleObject highscoreButton;
        
        public void awakeFn() {
            super.awakeFn();
            
            // create objects
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/menu.png");
            startButton = new RectangleObject(app, 650, 50);
            highscoreButton = new RectangleObject(app, 650, 125);
            
            // set properties
            startButton.setSize(250, 50);
            highscoreButton.setSize(250, 50);
        }
            
        public void update() {
            // check for mouse click
            if (!(mouseDown && mouseButton == PConstants.LEFT)) return;
            if (!clickSwitch) {
                clickSwitch = true;
                // navigate to stage based on which button was pressed.
                if (startButton.withinInBounds(mouseX, mouseY)) switchStage("intro");
                if (highscoreButton.withinInBounds(mouseX, mouseY)) switchStage("highscore");
            }   
        }
            
        public void draw() {
            background.draw();
            startButton.draw();
            highscoreButton.draw();
            app.textAlign(PConstants.CENTER);
            app.fill(255);
            app.textSize(50);
            app.text("Start", startButton.x, startButton.y + 25);
            app.text("Highscores", highscoreButton.x, highscoreButton.y + 25);
        }
    };
    
    Stage highscore = new Stage() {
        private RectangleObject back;
        private ArrayList<Integer> scores;
        
        private void input() {
            try {
                Scanner fileReader = new Scanner(new File("score.txt"));
                while (fileReader.hasNext()) {
                    Integer item = Integer.parseInt(fileReader.nextLine());
                    scores.add(item);
                }
                fileReader.close();
            } catch (IOException e) {}
            
            scores.sort( (num1, num2) -> { 
                return num2.compareTo(num1);
            });
            
        }
        
        public void awakeFn() {
            // flush all previous scores stored
            if (scores != null) {
                scores.clear();
                input();
            }
            
            super.awakeFn();
            
            scores = new ArrayList<>();
            back = new RectangleObject(app, 400, 400);
            back.setSize(800, 800);
            input();
        }
        
        public void update() {
            if (!(mouseDown && mouseButton == PConstants.LEFT)) return;
            if (!clickSwitch) {
                clickSwitch = true;
                if (back.withinInBounds(mouseX, mouseY)) switchStage("menu");
            }
        }
        
        public void draw() {
            app.textAlign(PConstants.CENTER);
            app.fill(0);
            app.textSize(50);
            
            app.text("Score 1: " + scores.get(0), 400, 200);
            app.text("Score 2: " + scores.get(1), 400, 400);
            app.text("Score 3: " + scores.get(2), 400, 600);
        }
    };
    
    Stage death = new Stage() {
        private RectangleObject background;
        
        public void awakeFn() {
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/death.png");
        }
        
        public void update() {
            // navigate back to menu when clicked
            if (!(mouseDown && mouseButton == PConstants.LEFT)) return;
            if (!clickSwitch) {
                clickSwitch = true;
            
                if (background.withinInBounds(mouseX, mouseY)) {
                    writeScore();
                    switchStage("menu");
                }
            }
        }
        
        public void draw() {
            background.draw();
        }
    };
    
    Stage intro = new Stage() {
        private RectangleObject background;
        private Actor emperor, ox, rat, cat;
        private int state = 0;
        
        public void awakeFn() {
            state = 0;
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/nakayamaTrackWithWater.png");
            
            emperor = new Actor(app, "Assets/Characters/Character.PNG", 0, -1000, -1000, 0);
            ox = new Actor(app, "Assets/Characters/ox.png", 0, -1000, -1000, 0);
            rat = new Actor(app, "Assets/Characters/rat.png", 0, -1000, -1000, 0);
            cat = new Actor(app, "Assets/Characters/cat.png", 0, -1000, -1000, 0);
        }
        
        public void update() {
            switch(state) {
                case 0:
                    emperor.moveToInstant(200, 400);
                    rat.moveToInstant(600, 500);
                    cat.moveToInstant(600, 300);
                    break;
                case 4:
                    emperor.moveToInstant(-1000, -1000);
                    ox.moveToInstant(200, 400);
                    break;
                case 11:
                    switchStage("combat1");
            }
            
            // increment state
            if (!(mouseDown && mouseButton == PConstants.LEFT) && background.withinInBounds(mouseX, mouseY)) {
                if (!clickSwitch) {
                    clickSwitch = true;
                    state++;
                }
            }
        }
        
        public void draw() {
            background.draw();
            emperor.draw();
            rat.draw();
            cat.draw();
            ox.draw();
            
            switch(state) {
                case 1:
                    app.textAlign(PConstants.CENTER);
                    app.fill(0);
                    app.textSize(20);
                    app.text("Jade Emperor: I'm holding a great race and \nI will decide the order of the 12 Months on the results.", 400, 750);
                    break;
                case 2:
                    app.text("Cat & Rat: Heh you know what I'm thinking right?", 400, 750);
                    break;
                case 3:
                    app.text("Jade Emperor: The track is a 2800m (Long) Right-Handed Swim track, \ngood luck to all contestants.", 400, 750);
                    break;
                case 4:
                    app.text("Rat: Hey Ox let us ride your back", 400, 750);
                    break;
                case 5:
                    app.text("Ox: Hell no", 400, 750);
                    break;
                case 7:
                    app.text("Rat: Why.", 400, 750);
                    break;
                case 8:
                    app.text("Ox: Hell no", 400, 750);
                    break;
                case 10:
                    app.text("Rat: Too bad.", 400, 750);
                    break;
            }
        }
    };
    
    Stage intermission1 = new Stage() {
        private RectangleObject background;
        private Actor rabbit, ox, rat, cat;
        private int state = 0;
        
        public void awakeFn() {
            state = 0;
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/water.png");
            
            rabbit = new Actor(app, "Assets/Characters/rabbit.png", 0, -1000, -1000, 0);
            ox = new Actor(app, "Assets/Characters/ox.png", 0, -1000, -1000, 0);
            rat = new Actor(app, "Assets/Characters/rat.png", 0, -1000, -1000, 0);
            cat = new Actor(app, "Assets/Characters/cat.png", 0, -1000, -1000, 0);
        }
        
        public void update() {
            switch(state) {
                case 0:
                    ox.moveToInstant(400, 700);
                    rat.moveToInstant(400, 400);
                    cat.moveToInstant(400, 500);
                    break;
                case 4:
                    rabbit.moveToInstant(400, 200);
                    break;
                case 10:
                    switchStage("combat2");
            }
            
            // increment state
            if (!(mouseDown && mouseButton == PConstants.LEFT) && background.withinInBounds(mouseX, mouseY)) {
                if (!clickSwitch) {
                    clickSwitch = true;
                    state++;
                }
            }
        }
        
        public void draw() {
            background.draw();
            ox.draw();
            cat.draw();
            rat.draw();
            rabbit.draw();
            
            switch(state) {
                case 1:
                    app.textAlign(PConstants.CENTER);
                    app.fill(255);
                    app.textSize(20);
                    app.text("Ox: Why are you guys so heavy...", 400, 750);
                    break;
                case 2:
                    app.text("Cat: Zip it and chop chop get to it.", 400, 750);
                    break;
                case 3:
                    app.text("Rat: Who's that up ahead?", 400, 750);
                    break;
                case 4:
                    app.text("Ox: Just ignore them I can't handle anymore weight or I'll sink...", 400, 750);
                    break;
                case 6:
                    app.text("Rabbit: ...", 400, 750);
                    break;
                case 7:
                    app.text("Rabbit: You aren't just gonna leave me here right?", 400, 750);
                    break;
                case 9:
                    app.text("Rabbit: COME BACK!11!", 400, 750);
                    break;
            }
        }
    };
    
    Stage intermission2 = new Stage() {
        private RectangleObject background;
        private Actor ox, rat, cat;
        private int state = 0;
        
        public void awakeFn() {
            state = 0;
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/water.png");
            
            ox = new Actor(app, "Assets/Characters/ox.png", 0, -1000, -1000, 0);
            rat = new Actor(app, "Assets/Characters/rat.png", 0, -1000, -1000, 0);
            cat = new Actor(app, "Assets/Characters/cat.png", 0, -1000, -1000, 0);
        }
        
        public void update() {
            switch(state) {
                case 0:
                    ox.moveToInstant(400, 700);
                    rat.moveToInstant(400, 400);
                    cat.moveToInstant(400, 500);
                    break;
                case 6:
                    cat.moveTo(400, 200, 1);
                    rat.moveTo(400, 650, 1);
                    break;
                case 7:
                    cat.moveToInstant(400, 200);
                    rat.moveToInstant(400, 650);
                    break;
                case 8:
                    switchStage("combat3");
            }
            
            // increment state
            if (!(mouseDown && mouseButton == PConstants.LEFT) && background.withinInBounds(mouseX, mouseY)) {
                if (!clickSwitch) {
                    clickSwitch = true;
                    state++;
                }
            }
        }
        
        public void draw() {
            background.draw();
            ox.draw();
            cat.draw();
            rat.draw();
            
            switch(state) {
                case 1:
                    app.textAlign(PConstants.CENTER);
                    app.fill(255);
                    app.textSize(20);
                    app.text("Ox & Cat & Rat: Waw! I can see the end!", 400, 750);
                    break;
                case 3:
                    app.text("Rat: Well...", 400, 750);
                    break;
                case 5:
                    app.text("Rat: ...", 400, 750);
                    break;
                case 6:
                    app.text("Rat: Heh... Cya... *pushes cat off*", 400, 750);
                    break;
                case 7:
                    app.text("Cat: Bro what did I do.", 400, 750);
                    break;
            }
        }
    };
    
    Stage ending = new Stage() {
        private RectangleObject background;
        private Actor emperor, ox, rat;
        private int state = 0;
        
        public void awakeFn() {
            state = 0;
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/nakayamaTrackWithWater.png");
            
            emperor = new Actor(app, "Assets/Characters/Character.PNG", 0, -1000, -1000, 0);
            ox = new Actor(app, "Assets/Characters/ox.png", 0, -1000, -1000, 0);
            rat = new Actor(app, "Assets/Characters/rat.png", 0, -1000, -1000, 0);
        }
        
        public void update() {
            switch(state) {
                case 0:
                    emperor.moveToInstant(200, 400);
                    break;
                case 6:
                    rat.moveToInstant(600, 350);
                    break;
                case 7:
                    ox.moveToInstant(600, 450);
                    break;
                case 10:
                    switchStage("menu");
            }
            
            // increment state
            if (!(mouseDown && mouseButton == PConstants.LEFT) && background.withinInBounds(mouseX, mouseY)) {
                if (!clickSwitch) {
                    clickSwitch = true;
                    state++;
                }
            }
        }
        
        public void draw() {
            background.draw();
            emperor.draw();
            ox.draw();
            rat.draw();
            
            switch(state) {
                case 1:
                    app.textAlign(PConstants.CENTER);
                    app.fill(0);
                    app.textSize(20);
                    app.text("Jade Emperor: Its the final straight!", 400, 750);
                    break;
                case 2:
                    app.text("Jade Emperor: The Rat and Ox are head to head!", 400, 750);
                    break;
                case 3:
                    app.text("Jade Emperor: The Rat pulls through!", 400, 750);
                    break;
                case 4:
                    app.text("Jade Emperor: The Rat is still going strong!", 400, 750);
                    break;
                case 5:
                    app.text("Jade Emperor: They're leading by 1 length!", 400, 750);
                    break;
                case 6:
                    app.text("Jade Emperor: AND THE RAT PULLS THROUGH!", 400, 750);
                    break;
                case 8:
                    app.text("Rat: Heh its just too easy.", 400, 750);
                    break;
                case 9:
                    app.textSize(50);
                    app.text("the end.", 400, 400);
                    app.textSize(20);
                    app.text("100000 pts", 400, 500);
                    break;
            }
        }
    };
    
    Stage combat1 = new Stage() {
        private RectangleObject background;
        private Actor stageBoss;
        private BulletPool bossBullets;
        private Actor player;
        private BulletPool playerBullets;
        private int shootTick = 0;
        private final int shootDelay = 2;
        
        // global pattern properties
        private int bossShootTick = 0;
        private int bossMoveTick = 520;
        
        // pattern 1 properties
        private final int pattern1_shootDelay = 10;
        private final int pattern1_moveDelay = 520;
        private final int pattern1_spawnAmount = 3;
        private void pattern1() {
            stageBoss.setRotation(stageBoss.getRotation() + 1);
            
            // mover
            if (bossMoveTick >= pattern1_moveDelay) {
                bossMoveTick = 0;
                
                stageBoss.moveTo(Math.abs(stageBoss.x - app.width), stageBoss.y, 3);
            }
            
            // bullet spiral pattern
            if (bossShootTick >= pattern1_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < pattern1_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotation(stageBoss.getRotation() + i * 360/pattern1_spawnAmount);
                    bullet.setRadius(30);
                }
            }
        }
        // pattern 2 properties
        private boolean pattern2_init = false;
        private final int pattern2_shootDelay = 5;
        private final int pattern2_moveDelay = 100;
        private void pattern2() {
            // initer
            if (!pattern2_init) {
                pattern2_init = true;
                stageBoss.moveTo(400, 200, 1);
                stageBoss.setRotation(0);
            }
            
            // mover
            if (bossMoveTick >= pattern2_moveDelay) {
                bossMoveTick = 0;
                
                stageBoss.moveTo(random.nextInt(200, 600), random.nextInt(175, 225), 0.5);
            }
            
            // stream pattern
            if (bossShootTick >= pattern2_shootDelay) {
                bossShootTick = 0;
                
                Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet.setRotationFromTarget(player.x, player.y);
                bullet.setRadius(random.nextInt(20, 30));
            }
        }
        
        // pattern 3 properties
        private boolean pattern3_init = false;
        private final int pattern3_shootDelay = 15;
        private final int pattern3_spawnAmount = 6;
        private void pattern3() {
            // initer
            if (!pattern3_init) {
                pattern3_init = true;
                stageBoss.moveTo(400, 200, 1);
            }
            
            stageBoss.setRotation(stageBoss.getRotation() + 5);
            
            // bullet spiral pattern
            if (bossShootTick >= pattern3_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < pattern3_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotation(stageBoss.getRotation() + i * 360/pattern3_spawnAmount);
                    bullet.setRadius(30);
                    bullet.setSpeed(2);
                    bullet.setRotationSpeed(-1);
                }
            }
        }
        
        public void awakeFn() {
            if (stageBoss != null)
                stageBoss.setHealth(10000);
            
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/grass.png");
            
            stageBoss = new Actor(app, "Assets/Characters/ox.png", 10000, 200, 200, 300);
            stageBoss.addStage(0.7);
            stageBoss.addStage(0.3);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/rat.png", 5, 400, 600, 10) {
                public void deathHook() {
                    switchStage("death");
                }
            };
            player.setSpeed(6);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            score += 10;
            
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4: switchStage("intermission1");
            }
            
            // inputs
            int moveX = 0;
            int moveY = 0;
            double diminFactor = 1;
            
            // checks if the movement keys are pressed.
            if (keysPressed.contains(PConstants.LEFT)) moveX -= 1;
            if (keysPressed.contains(PConstants.RIGHT)) moveX += 1;
            if (keysPressed.contains(PConstants.UP)) moveY -= 1;
            if (keysPressed.contains(PConstants.DOWN)) moveY += 1;
            
            // slows player speed if shift is held
            if (keysPressed.contains(PConstants.SHIFT)) {
                player.setSpeed(2);
            } else {
                player.setSpeed(6);
            }
            
            // shoot bullet if z is held.
            if (keysPressed.contains(KEY_Z)) {
                shootTick++;
                
                if (shootTick >= shootDelay) {
                    shootTick = 0;
                
                    Bullet bullet = playerBullets.spawnBullet(player.x, player.y);
                    bullet.setRotation(270);
                    bullet.setSpeed(5);
                    bullet.setColour(new int[] {255, 255, 0});
                    bullet.setDamage(20);
                }
            }
            
            // diminish diagonal movement by the square root of 2 to prevent
            // faster movement.
            if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
            
            // moves player based on input direction and speed.
            player.x += moveX * player.getSpeed() / diminFactor;
            player.y += moveY * player.getSpeed() / diminFactor;
            
            // updaters
            bossShootTick++;
            bossMoveTick++;
            
            stageBoss.checkCollision(playerBullets);
            player.checkCollision(bossBullets);
            stageBoss.update();
            bossBullets.update();
            playerBullets.update();
        }
        
        public void draw() {
            background.draw();
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
            
            app.fill(255);
            app.textSize(15);
            app.text("Boss HP: " + stageBoss.getHealth(), player.x, player.y + 15);
            app.text("Boss phase: " + stageBoss.getStage(), player.x, player.y + 30);
        }
    };
    
    Stage combat2 = new Stage() {
        private RectangleObject background;
        private Actor stageBoss;
        private BulletPool bossBullets;
        private Actor player;
        private BulletPool playerBullets;
        private int shootTick = 0;
        private final int shootDelay = 2;
        
        // global pattern properties
        private int bossShootTick = 0;
        private int bossShootTick2 = 0;
        private int bossMoveTick = 0;
        
        // pattern 1 properties
        private final int pattern1_shootDelay = 20;
        private final int pattern1_spawnAmount = 2;
        private void pattern1() {
            // supreme calamitous (see terraria calamity) pattern
            if (bossShootTick >= pattern1_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < pattern1_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(1, random.nextInt(1, 799));
                    bullet.setRadius(random.nextInt(20, 30));
                    bullet.setSpeed(random.nextDouble(1, 1.25));
                }
                
                for (int i = 0; i < pattern1_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(799, random.nextInt(1, 799));
                    bullet.setRotation(180);
                    bullet.setRadius(20);
                }
            }
        }
        // pattern 2 properties
        private final int pattern2_shootDelay = 20;
        private final int pattern2_shootDelay2 = 50;
        private final int pattern2_spawnAmount = 5;
        private final int pattern2_moveDelay = 100;
        private void pattern3() { // (PATTERN 2 AND 3 ARE SWAPPED FOR BALANCE REASONS)
            // mover
            if (bossMoveTick >= pattern2_moveDelay) {
                bossMoveTick = 0;
                
                stageBoss.moveTo(random.nextInt(200, 600), random.nextInt(175, 225), 0.5);
            }
            
            // bullet rain pattern
            if (bossShootTick >= pattern2_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < pattern2_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(random.nextInt(1, 799), 1);
                    bullet.setRotation(random.nextInt(80, 100));
                    bullet.setRadius(random.nextInt(5, 10));
                    bullet.setSpeed(random.nextDouble(1, 1.25));
                }
            }
            
            // bullet shotgun
            if (bossShootTick2 >= pattern2_shootDelay2) {
                bossShootTick2 = 0;
                
                Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet.setRotationFromTarget(player.x, player.y);
                bullet.setRadius(random.nextInt(30, 40));
            }
        }
        
        // pattern 3 properties
        private boolean pattern3_init = false;
        private final int pattern3_shootDelay = 50;
        private final int pattern3_spawnAmount = 6;
        private final int pattern3_shootDelay2 = 5;
        private final int pattern3_spawnAmount2 = 3;
        private void pattern2() { // (PATTERN 2 AND 3 ARE SWAPPED FOR BALANCE REASONS)
            // initer
            if (!pattern3_init) {
                pattern3_init = true;
                stageBoss.moveTo(400, 200, 1);
                stageBoss.setRotation(0);
            }
            
             stageBoss.setRotation(stageBoss.getRotation() + 1);
            
            if (bossShootTick >= pattern3_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < pattern3_spawnAmount; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotation(stageBoss.getRotation() + i * 360/pattern3_spawnAmount);
                    bullet.setRadius(150);
                }
            }
            
            if (bossShootTick2 >= pattern3_shootDelay2) {
                bossShootTick2 = 0;
                
                for (int i = 0; i < pattern3_spawnAmount2; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotation(stageBoss.getRotation() + i * 360/pattern3_spawnAmount2);
                    bullet.setRadius(20);
                }
            }
        }
        
        public void awakeFn() {
            if (stageBoss != null)
                stageBoss.setHealth(25000);
            
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/water.png");
            
            stageBoss = new Actor(app, "Assets/Characters/rabbit.png", 25000, 400, 200, 170);
            stageBoss.addStage(0.8);
            stageBoss.addStage(0.4);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/rat.png", 5, 400, 600, 10) {
                public void deathHook() {
                    switchStage("death");
                }
            };
            player.setSpeed(6);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            score += 10;
            
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4: switchStage("intermission2");
            }
            
            // inputs
            int moveX = 0;
            int moveY = 0;
            double diminFactor = 1;
            
            // checks if the movement keys are pressed.
            if (keysPressed.contains(PConstants.LEFT)) moveX -= 1;
            if (keysPressed.contains(PConstants.RIGHT)) moveX += 1;
            if (keysPressed.contains(PConstants.UP)) moveY -= 1;
            if (keysPressed.contains(PConstants.DOWN)) moveY += 1;
            
            // slows player speed if shift is held
            if (keysPressed.contains(PConstants.SHIFT)) {
                player.setSpeed(2);
            } else {
                player.setSpeed(6);
            }
            
            // shoot bullet if z is held.
            if (keysPressed.contains(KEY_Z)) {
                shootTick++;
                
                if (shootTick >= shootDelay) {
                    shootTick = 0;
                
                    Bullet bullet = playerBullets.spawnBullet(player.x, player.y);
                    bullet.setRotation(270);
                    bullet.setSpeed(5);
                    bullet.setColour(new int[] {255, 255, 0});
                    bullet.setDamage(20);
                }
            }
            
            // diminish diagonal movement by the square root of 2 to prevent
            // faster movement.
            if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
            
            // moves player based on input direction and speed.
            player.x += moveX * player.getSpeed() / diminFactor;
            player.y += moveY * player.getSpeed() / diminFactor;
            
            // updaters
            bossShootTick++;
            bossShootTick2++;
            bossMoveTick++;
            
            stageBoss.checkCollision(playerBullets);
            player.checkCollision(bossBullets);
            stageBoss.update();
            bossBullets.update();
            playerBullets.update();
        }
        
        public void draw() {
            background.draw();
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
            
            app.fill(255);
            app.textSize(15);
            app.text("Boss HP: " + stageBoss.getHealth(), player.x, player.y + 5);
            app.text("Boss phase: " + stageBoss.getStage(), player.x, player.y + 20);
        }
    };
    
    Stage combat3 = new Stage() {
        private RectangleObject background;
        private Actor stageBoss;
        private BulletPool bossBullets;
        private Actor player;
        private BulletPool playerBullets;
        private int shootTick = 0;
        private final int shootDelay = 2;
        
        // global pattern properties
        private int bossShootTick = 0;
        private int bossShootTick2 = 0;
        private int bossShootTick3 = 0;
        private int bossMoveTick = 0;
        
        private final int pattern1_shootDelay = 10;
        private final int pattern1_moveDelay = 100;
        private void pattern1() {
            stageBoss.setRotation(stageBoss.getRotation() + 1);
            
            if (bossMoveTick >= pattern1_moveDelay) {
                bossMoveTick = 0;
                
                stageBoss.moveTo(random.nextInt(200, 600), random.nextInt(175, 225), 0.5);
            }
            
            if (bossShootTick >= pattern1_shootDelay) {
                bossShootTick = 0;
                
                // boundary pattern
                Bullet bullet = bossBullets.spawnBullet(1, 1);
                Bullet bullet2 = bossBullets.spawnBullet(799, 1);
                bullet.setRadius(30);
                bullet2.setRadius(30);
                bullet.setRotation(90);
                bullet2.setRotation(90);
                bullet.setSpeed(3);
                bullet2.setSpeed(3);
                bullet.setRotationSpeed(-1);
                bullet2.setRotationSpeed(1);
                
                // sweep
                for (int i = 0; i < 4; i++) {
                    Bullet bullet3 = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet3.setRotation(stageBoss.getRotation() + i*90);
                    bullet3.setRotationSpeed(-1);
                    bullet3.setSpeed(1.5);
                    bullet3.setRadius(20);
                }
            }
        }
        
        private final int pattern2_shootDelay = 10;
        private final int pattern2_moveDelay = 120;
        private int pattern2_moveState = 0;
        private void pattern2() {
            stageBoss.setRotation(stageBoss.getRotation() + 1);
            
            if (bossMoveTick >= pattern2_moveDelay) {
                bossMoveTick = 0;
                
                pattern2_moveState++;
                if (pattern2_moveState > 3) pattern2_moveState = 1;
                
                switch(pattern2_moveState) {
                    case 1: stageBoss.moveTo(400, 100, 1); break;
                    case 2: stageBoss.moveTo(100, 200, 1); break;
                    case 3: stageBoss.moveTo(700, 200, 1); break;
                }
            }
            
            if (bossShootTick >= pattern2_shootDelay) {
                bossShootTick = 0;
                
                // player border pattern
                Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                Bullet bullet2 = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet.setRadius(15);
                bullet2.setRadius(15);
                bullet.setRotationFromTarget(player.x, player.y);
                bullet.setRotation(bullet.getRotation() - 60);
                bullet2.setRotationFromTarget(player.x, player.y);
                bullet2.setRotation(bullet2.getRotation() + 60);
                bullet.setSpeed(1.5);
                bullet2.setSpeed(1.5);
                bullet.setRotationSpeed(1);
                bullet2.setRotationSpeed(-1);
                
                // tracker pattern
                Bullet bullet3 = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet3.setRotationFromTarget(player.x, player.y);
                
                // boundary pattern
                Bullet bullet4 = bossBullets.spawnBullet(1, 1);
                Bullet bullet5 = bossBullets.spawnBullet(799, 1);
                bullet4.setRadius(30);
                bullet5.setRadius(30);
                bullet4.setRotation(90);
                bullet5.setRotation(90);
                bullet4.setSpeed(3);
                bullet5.setSpeed(3);
                bullet4.setRotationSpeed(-1);
                bullet5.setRotationSpeed(1);
            }
        }
        
        private final int pattern3_shootDelay = 30;
        private final int pattern3_moveDelay = 60;
        private void pattern3() {
            if (bossMoveTick >= pattern3_moveDelay) {
                bossMoveTick = 0;
                stageBoss.moveTo(random.nextInt(325, 475), random.nextInt(100, 250), 1);
            }
            
            if (bossShootTick >= pattern3_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < 5; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotationFromTarget(player.x, player.y);
                    bullet.setSpeed(1+i*0.2);
                }
            }
        }
        
        // charge...
        private boolean pattern4_init = false;
        private final int pattern4_shootDelay = 50;
        private void pattern4() {
            if (!pattern4_init) {
                pattern4_init = true;
                stageBoss.moveTo(400, 100, 1);
            }
            
            if (bossShootTick >= pattern4_shootDelay) {
                bossShootTick = 0;
                
                Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet.setRotationFromTarget(player.x, player.y);
            }
        }
        
        // chargeeeeee...
        private final int pattern5_shootDelay = 100;
        private void pattern5() {
            if (bossShootTick >= pattern5_shootDelay) {
                bossShootTick = 0;
                
                Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                bullet.setRotationFromTarget(player.x, player.y);
            }
        }
        
        // ...
        private void pattern6() {}
        
        private final int pattern7_shootDelay = 150;
        private final int pattern7_shootDelay2 = 20;
        private final int pattern7_shootDelay3 = 400;
        
        // final attack
        private void pattern7() {
            stageBoss.setRotation(stageBoss.getRotation() + 1);
            
            if (bossShootTick >= pattern7_shootDelay) {
                bossShootTick = 0;
                
                for (int i = 0; i < 5; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotationFromTarget(player.x, player.y);
                    bullet.setRadius(50);
                    bullet.setSpeed(1+i*0.2);
                }
            }
            
            if (bossShootTick2 >= pattern7_shootDelay2) {
                bossShootTick2 = 0;
                
                for (int i = 0; i < 6; i++) {
                    Bullet bullet3 = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet3.setRotation(stageBoss.getRotation() + i*90);
                    bullet3.setRadius(30);
                }
                
                for (int i = 0; i < 5; i++) {
                    Bullet bullet = bossBullets.spawnBullet(random.nextInt(1, 799), 799);
                    bullet.setRotation(random.nextInt(250, 290));
                    bullet.setRadius(random.nextInt(5, 10));
                    bullet.setSpeed(random.nextDouble(0.5, 1));
                }
            }
            
            if (bossShootTick3 >= pattern7_shootDelay3) {
                bossShootTick3 = 0;
                
                int randomX = random.nextInt(1, 799);
                int randomRot = random.nextInt(80, 100);
                
                for (int i = 0; i < 10; i++) {
                    Bullet bullet = bossBullets.spawnBullet(randomX, 1);
                    bullet.setRotation(randomRot);
                    bullet.setRadius(300);
                    bullet.setSpeed(0.5 + i*0.2);
                }
            }
        }
        
        public void awakeFn() {
            if (stageBoss != null)
                stageBoss.setHealth(50000);
            
            super.awakeFn();
            
            background = new RectangleObject(app, 400, 400);
            background.setImage("Assets/Backgrounds/water.png");
            
            stageBoss = new Actor(app, "Assets/Characters/cat.png", 50000, 400, 200, 200);
            stageBoss.addStage(0.95);
            stageBoss.addStage(0.8);
            stageBoss.addStage(0.65);
            stageBoss.addStage(0.6);
            stageBoss.addStage(0.45);
            stageBoss.addStage(0.2);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/rat.png", 5, 400, 600, 10) {
                public void deathHook() {
                    switchStage("death");
                }
            };
            player.setSpeed(6);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            score += 10;
            
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4: pattern4(); break;
                case 5: pattern5(); break;
                case 6: pattern6(); break;
                case 7: pattern7(); break;
                case 8: switchStage("ending");
            }
            
            // inputs
            int moveX = 0;
            int moveY = 0;
            double diminFactor = 1;
            
            // checks if the movement keys are pressed.
            if (keysPressed.contains(PConstants.LEFT)) moveX -= 1;
            if (keysPressed.contains(PConstants.RIGHT)) moveX += 1;
            if (keysPressed.contains(PConstants.UP)) moveY -= 1;
            if (keysPressed.contains(PConstants.DOWN)) moveY += 1;
            
            // slows player speed if shift is held
            if (keysPressed.contains(PConstants.SHIFT)) {
                player.setSpeed(2);
            } else {
                player.setSpeed(6);
            }
            
            // shoot bullet if z is held.
            if (keysPressed.contains(KEY_Z)) {
                shootTick++;
                
                if (shootTick >= shootDelay) {
                    shootTick = 0;
                
                    Bullet bullet = playerBullets.spawnBullet(player.x, player.y);
                    bullet.setRotation(270);
                    bullet.setSpeed(5);
                    bullet.setColour(new int[] {255, 255, 0});
                    bullet.setDamage(20);
                }
            }
            
            // diminish diagonal movement by the square root of 2 to prevent
            // faster movement.
            if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
            
            // moves player based on input direction and speed.
            player.x += moveX * player.getSpeed() / diminFactor;
            player.y += moveY * player.getSpeed() / diminFactor;
            
            // updaters
            bossShootTick++;
            bossShootTick2++;
            bossShootTick3++;
            bossMoveTick++;
            
            stageBoss.checkCollision(playerBullets);
            player.checkCollision(bossBullets);
            stageBoss.update();
            bossBullets.update();
            playerBullets.update();
        }
        
        public void draw() {
            background.draw();
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
            
            app.fill(255);
            app.textSize(15);
            app.text("Boss HP: " + stageBoss.getHealth(), player.x, player.y + 5);
            app.text("Boss phase: " + stageBoss.getStage(), player.x, player.y + 20);
        }
    };
}