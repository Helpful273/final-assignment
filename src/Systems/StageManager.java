package Systems;
import Objects.*;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
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
    private boolean mouseDown = false;
    private int mouseX, mouseY = 0;
    private int mouseButton = 0;
    
    public StageManager(PApplet app) {
        this.app = app;
    }
    
    // fired upon startup
    public void awake() {
        stages.put("Menu", menu);
        stages.put("highscore", highscore);
        stages.put("combat1", combat1);
        stages.put("combat2", combat2);
        stages.put("combat3", combat3);
        
        // automatically put users to the start screen.
        switchStage("Menu");
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
    Below are where anonymous classes are created for each individual scene.
    NOTE: majority of the code is creating objects and making bullet patterns.
        I will NOT be explaining how bullet patterns are created because all of
        them are editing and tweaking values until satisfactory. The only thing
        that will be explained are the mechanisms involving GUI and data
        handling.
    */
    
    Stage menu = new Stage() {
        private RectangleObject startButton;
        private RectangleObject highscoreButton;
        
        public void awakeFn() {
            super.awakeFn();
            
            // create objects
            startButton = new RectangleObject(app, 650, 50);
            highscoreButton = new RectangleObject(app, 650, 125);
            
            // set properties
            startButton.setSize(250, 50);
            highscoreButton.setSize(250, 50);
        }
            
        public void update() {
            // check for mouse click
            if (!(mouseDown && mouseButton == PConstants.LEFT)) return;
            
            // navigate to stage based on which button was pressed.
            if (startButton.withinInBounds(mouseX, mouseY)) switchStage("combat1");
            if (highscoreButton.withinInBounds(mouseX, mouseY)) switchStage("highscore");
        }
            
        public void draw() {
            startButton.draw();
            highscoreButton.draw();
        }
    };
    
    Stage highscore = new Stage() {
        public void awakeFn() {
            
        }
        
        public void update() {
            
        }
        
        public void draw() {
            
        }
    };
    
    Stage combat1 = new Stage() {
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
            super.awakeFn();
            
            stageBoss = new Actor(app, "Assets/Characters/Character.PNG", 10000, 200, 200, 100);
            stageBoss.addStage(0.7);
            stageBoss.addStage(0.3);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/Character.PNG", 10, 400, 600, 10);
            player.setSpeed(5);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4:
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
                player.setSpeed(5);
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
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
        }
    };
    
    Stage combat2 = new Stage() {
        public void awakeFn() {
            
        }
        
        public void update() {
            
        }
        
        public void draw() {
            
        }
    };
    
    Stage combat3 = new Stage() {
        public void awakeFn() {
            
        }
        
        public void update() {
            
        }
        
        public void draw() {
            
        }
    };
}