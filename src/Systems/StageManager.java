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
    
    public void awake() {
        stages.put("Menu", menu);
        stages.put("highscore", highscore);
        stages.put("combat1", combat1);
        switchStage("Menu");
    }
    
    public void update() {
        stage.update();
    }
    
    public void draw() {
        stage.draw();
    }
    
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
    
    public void switchStage(String newStage) {
        stage = stages.get(newStage);
        stage.awakeFn();
    }
    
    Stage menu = new Stage() {
        private RectangleObject startButton;
        private RectangleObject highscoreButton;
        
        public void awakeFn() {
            super.awakeFn();
            
            startButton = new RectangleObject(app, 650, 50);
            highscoreButton = new RectangleObject(app, 650, 125);
            
            startButton.setSize(250, 50);
            highscoreButton.setSize(250, 50);
        }
            
        public void update() {
            if (!(mouseDown && mouseButton == PConstants.LEFT)) return;
            
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
        private int shootDelay = 2;
        
        private int tick1 = 0;
        private int tick2 = 520;
        private int tickDelay1 = 5;
        private int tickDelay2 = 520;
        private int spawnAmount1 = 8;
        private int rotationSpeed1 = 1;
        private int rotation1 = 0;
        private void pattern1() {
            rotation1 += rotationSpeed1;
            tick1++;
            tick2++;
            
            if (tick2 >= tickDelay2) {
                tick2 = 0;
                
                stageBoss.moveTo(Math.abs(stageBoss.x - app.width), stageBoss.y, 3);
            }
            
            if (tick1 >= tickDelay1) {
                tick1 = 0;
                
                for (int i = 0; i < spawnAmount1; i++) {
                    Bullet bullet = bossBullets.spawnBullet(stageBoss.x, stageBoss.y);
                    bullet.setRotation(rotation1 + i * 360/spawnAmount1);
                }
            }
        }
        
        private void pattern2() {
            
        }
        
        private void pattern3() {
            
        }
        
        public void awakeFn() {
            super.awakeFn();
            
            stageBoss = new Actor(app, "Assets/Characters/Character.PNG", 10000, 200, 200, 100);
            stageBoss.addStage(0.8);
            stageBoss.addStage(0.3);
            stageBoss.addStage(0.1);
            
            player = new Actor(app, "Assets/Characters/Character.PNG", 3, 400, 600, 10);
            player.setSpeed(5);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            switch(stageBoss.getStage()) {
                case 1: pattern1();
                case 2: pattern2();
                case 3: pattern3();
            }
            
            // inputs
            int moveX = 0;
            int moveY = 0;
            double diminFactor = 1;
                
            if (keysPressed.contains(PConstants.LEFT)) moveX -= 1;
            if (keysPressed.contains(PConstants.RIGHT)) moveX += 1;
            if (keysPressed.contains(PConstants.UP)) moveY -= 1;
            if (keysPressed.contains(PConstants.DOWN)) moveY += 1;
            if (keysPressed.contains(PConstants.SHIFT)) {
                player.setSpeed(2);
            } else {
                player.setSpeed(5);
            }
            if (keysPressed.contains(KEY_Z)) {
                shootTick++;
                
                if (shootTick >= shootDelay) {
                    shootTick = 0;
                
                    Bullet bullet = playerBullets.spawnBullet(player.x, player.y);
                    bullet.setRotation(270);
                    bullet.setSpeed(5);
                    bullet.setColour(new int[] {255, 255, 0});
                    bullet.setDamage(10);
                }
            }
            if (moveX != 0 && moveY != 0) diminFactor = Math.sqrt(2);
                
            player.x += moveX * player.getSpeed() / diminFactor;
            player.y += moveY * player.getSpeed() / diminFactor;
            
            // updaters
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
    
    Stage combat4 = new Stage() {
        public void awakeFn() {
            
        }
        
        public void update() {
            
        }
    
        public void draw() {
                
        }
    };
}