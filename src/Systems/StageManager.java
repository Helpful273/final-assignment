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
            if (startButton.withinInBounds(mouseX, mouseY)) switchStage("combat3");
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
            player.setSpeed(6);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4: switchStage("combat2");
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
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
        }
    };
    
    Stage combat2 = new Stage() {
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
            super.awakeFn();
            
            stageBoss = new Actor(app, "Assets/Characters/Character.PNG", 25000, 400, 200, 100);
            stageBoss.addStage(0.8);
            stageBoss.addStage(0.4);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/Character.PNG", 10, 400, 600, 10);
            player.setSpeed(6);
            
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
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
        }
    };
    
    Stage combat3 = new Stage() {
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
        
        private void pattern3() {
            
        }
        
        private void pattern4() {
            
        }
        
        private void pattern5() {
            
        }
        
        private void pattern6() {
            
        }
        
        private void pattern7() {
            
        }
        
        public void awakeFn() {
            super.awakeFn();
            
            stageBoss = new Actor(app, "Assets/Characters/Character.PNG", 50000, 400, 200, 100);
            stageBoss.addStage(0.95);
            stageBoss.addStage(0.8);
            stageBoss.addStage(0.65);
            stageBoss.addStage(0.4);
            stageBoss.addStage(0.25);
            stageBoss.addStage(0.1);
            stageBoss.addStage(0);
            
            player = new Actor(app, "Assets/Characters/Character.PNG", 10, 400, 600, 10);
            player.setSpeed(6);
            
            bossBullets = new BulletPool(app);
            playerBullets = new BulletPool(app);
        }
        
        public void update() {
            switch(stageBoss.getStage()) {
                case 1: pattern1(); break;
                case 2: pattern2(); break;
                case 3: pattern3(); break;
                case 4: pattern4(); break;
                case 5: pattern5(); break;
                case 6: pattern6(); break;
                case 7: pattern7(); break;
                case 8:
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
            stageBoss.draw();
            player.draw();
            player.drawHitbox();
            bossBullets.draw();
            playerBullets.draw();
        }
    };
}