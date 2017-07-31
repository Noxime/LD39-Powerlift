package us.noxim.powerlift;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class Character {

    public float posX, posY;

    public float scale = 8f;
    public float speed = 0.25f/60;

    public boolean direction = true; //false: left
    public int state = STATE_STANDING;
    public int holding = -1;

    public boolean lift = false;
    public boolean hand = false;

    public final static int STATE_STANDING = 0;
    public final static int STATE_RUNNING = 1;
    public final static int STATE_LIFTING = 2;
    public final static int STATE_RESTING = 3;

    public Texture standLeft, standRight;
    public Texture[] runRight, runLeft;

    public Texture barbellLL, barbellLH, barbellRL, barbellRH;
    public Texture dumbbellLL, dumbbellLH0, dumbbellLH1, dumbbellRL, dumbbellRH0, dumbbellRH1;
    public Texture squatRL, squatRH, squatLL, squatLH;

    public Character(float x, float y, String left, String right, String[] runR, String[] runL)
    {
        posX = x;
        posY = y;
        standLeft = new Texture(left);
        standRight = new Texture(right);

        runRight = new Texture[runR.length];
        for(int i = 0; i < runR.length; i++)
        {
            runRight[i] = new Texture(runR[i]);
        }
        runLeft = new Texture[runR.length];
        for(int i = 0; i < runR.length; i++)
        {
            runLeft[i] = new Texture(runL[i]);
        }

        barbellLL = new Texture("player_barbell_left_low.png");
        barbellLH = new Texture("player_barbell_left_high.png");
        barbellRL = new Texture("player_barbell_right_low.png");
        barbellRH = new Texture("player_barbell_right_high.png");

        dumbbellLL = new Texture("player_dumbbell_left_low.png");
        dumbbellLH0 = new Texture("player_dumbbell_left_high_0.png");
        dumbbellLH1 = new Texture("player_dumbbell_left_high_1.png");
        dumbbellRL = new Texture("player_dumbbell_right_low.png");
        dumbbellRH0 = new Texture("player_dumbbell_right_high_0.png");
        dumbbellRH1 = new Texture("player_dumbbell_right_high_1.png");

        squatLL = new Texture("player_squat_left_low.png");
        squatLH = new Texture("player_squat_left_high.png");
        squatRL = new Texture("player_squat_right_low.png");
        squatRH = new Texture("player_squat_right_high.png");
    }

    public void run(boolean dir)
    {
        direction = dir;
        if(direction)
        {
            posX += speed;
        }
        else
        {
            posX -= speed;
        }
    }


    public Texture getTexture()
    {
        switch (state)
        {
            case STATE_STANDING:
            {
                if(direction)
                    return standLeft;
                else
                    return standRight;
            }
            case STATE_RUNNING:
            {
                int i = (int)(TimeUtils.millis() % 500l / 125l);
                return direction ? runRight[i] : runLeft[i];
            }
            case STATE_LIFTING:
            {
                switch (holding)
                {
                    case Pickup.TYPE_BARBELL:
                    {
                        return lift ? direction ? barbellRH : barbellLH : direction ? barbellRL : barbellLL;
                    }
                    case Pickup.TYPE_DUMBBELL:
                    {
                        return lift ? direction ? hand ? dumbbellRH0 : dumbbellRH1 : hand ? dumbbellLH0 : dumbbellLH1 : direction ? dumbbellRL : dumbbellLL;
                    }
                    case Pickup.TYPE_SQUAT:
                    {
                        return lift ? direction ? squatRL : squatLL : direction ? squatRH : squatLH;
                    }
                }
            }
            case STATE_RESTING:
            {
                return standLeft;

            }
        }

        return null;
    }

}
