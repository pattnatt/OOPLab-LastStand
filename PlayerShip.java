import greenfoot.*;
public class PlayerShip extends Actor
{
    public static int shipSizeX = 64;
    public static int shipSizeY = 49;
    private static int shipSpeed = 7;
    private static int shootEveryTick = 10;
    private static int currentTick = 0;
    
    public int remainInvincTick = 0;
    public boolean isDead = false;
    private int invincTick = 100;
    private int invincBlinkTick = 3;
    
    private GreenfootImage trans = new GreenfootImage("playerShip1_blue_invi.png");
    private GreenfootImage normal = new GreenfootImage("playerShip1_blue.png");
    private GreenfootSound deadSound = new GreenfootSound("PlayerDown.mp3");
    private GreenfootSound hitSound = new GreenfootSound("PlayerHit.mp3");
    
    public void act() 
    {
        movement();
        shoot();
        hitDetection();
        if(remainInvincTick > 0)
        {
            invincible();
        }
        checkDead();
    }
    
    private void movement()
    {
        int currentX = getX();
        int currentY = getY();
        World world = getWorld();
        
        if(Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a"))
        {
            currentX -= shipSpeed;
            if(currentX < (shipSizeX/2)) 
            {
                currentX = (shipSizeX/2);
            }
            setLocation(currentX, currentY);
        }
        else if(Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d"))
        {
            currentX += shipSpeed;
            if(currentX > (world.getWidth() - (shipSizeX/2))) 
            {
                currentX = world.getWidth() - (shipSizeX/2);
            }
            setLocation(currentX, currentY);
        }
    }
    
    private void shoot()
    {
        if(Greenfoot.isKeyDown("space")) 
        {
            if(currentTick == 0)
            {
                fire();
            }
            currentTick = (currentTick + 1) % shootEveryTick;
        }
        else
        {
            currentTick = 0;
        }
    }
    
    private void fire()
    {
        PlayerBullet bullet = new PlayerBullet();
        World world = getWorld();
        int xPosi = getX();
        int yPosi = (int)(getY()-(shipSizeY/2));
        if(bullet != null && world != null)
        {
            world.addObject(bullet, xPosi, yPosi);
        }
    }
    
    private void hitDetection()
    {
        EnemyBullet enemyBullet = (EnemyBullet) getOneIntersectingObject(EnemyBullet.class);
        if(enemyBullet != null)
        {
            World world = getWorld();
            world.removeObject(enemyBullet);
            if(remainInvincTick == 0) hitSound.play();
            hit(1);
        }
        
        EnemyShip enemyShip = (EnemyShip) getOneIntersectingObject(EnemyShip.class);
        if(enemyShip != null)
        {
            World world = getWorld();
            world.removeObject(enemyShip);
            if(remainInvincTick == 0)hitSound.play();
            hit(2);
        }
    }
    
    private void hit(int value)
    {
        if(remainInvincTick <= 0)
        {
            remainInvincTick = invincTick;
            
            MyWorld world = (MyWorld) getWorld();
            GameTracker tracker = world.getGameTracker();
            tracker.playerHit(value);
        }
    }
    
    private void invincible()
    {
        if(remainInvincTick <= 1) 
        {
            setImage(normal);
        }
        else if(remainInvincTick % invincBlinkTick == 0)
        {
            if(getImage() == normal)
            {
                setImage(trans);
            }
            else
            {
                setImage(normal);
            }
        }
        
        remainInvincTick = remainInvincTick -1;
    }
    
    void checkDead()
    {
        if(isDead == true)
        {   
            deadSound.play();
            World world = getWorld();
            world.removeObject(this);
        }
    }
}
