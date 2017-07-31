package us.noxim.powerlift;

import com.badlogic.gdx.graphics.Texture;

public class Pickup {

    public static final int TYPE_BARBELL = 0;
    public static final int TYPE_DUMBBELL = 1;
    public static final int TYPE_SQUAT = 2;

    float posX, posY;
    int type = 0;
    Texture tex;

    public Pickup(float x, float y, int type)
    {
        posX = x;
        posY = y;
        this.type = type;

        switch (type)
        {
            case TYPE_BARBELL:
            {
                tex = new Texture("barbell.png");
                break;
            }
            case TYPE_DUMBBELL:
            {
                tex = new Texture("dumbbell.png");
                break;
            }
            case TYPE_SQUAT:
            {
                tex = new Texture("squat.png");
                break;
            }
        }
    }

}
