package us.noxim.powerlift;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.org.apache.bcel.internal.generic.POP;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Tuple<X, Y>
{
    public X x;
    public Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}

class Popup
{
    String str;
    long startTime;
    long length;
    public Popup(String s, long time, long length)
    {
        str = s;
        startTime = time;
        this.length = length;
    }
}

public class Powerlift extends ApplicationAdapter {

    //Okay listen up, everything runs in 60fps so no deltatime, but no hfr. deal with it

	SpriteBatch batch;
	Texture background;
	Texture mainmenu;
	Texture gameover;
	Texture meterFull;
	Texture meterEmpty;
	BitmapFont font;
    ShapeRenderer shapeRenderer;

    Texture play, play2;
    Texture replay, replay2;

	Character player;

	List<Pickup> pickups;

	List<Tuple<Integer, Integer>> setList;
	List<String> doneList;

	Texture health, healthSR, healthSL, healthC, healthL, healthBL, healthBR;
	float hsr, hsl, hc, hl, hbl, hbr;

	public static float groundLevel = 0.1f;
	float powerLevel = 1f;

	Texture newSet;
	Texture rest;

	List<Popup> popups;

	Sound[] liftSounds;
	public static boolean step = false;
    Sound[] stepA;
    Sound[] stepB;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		font = new BitmapFont(Gdx.files.internal("8_bit.fnt"));
		shapeRenderer = new ShapeRenderer();

		player = new Character(0.2f, 0.1f, "player_standing_left.png", "player_standing_right.png",
                new String[] { "player_running_right_0.png",
                "player_running_right_1.png",
                "player_running_right_2.png",
                "player_running_right_3.png"},
                new String[] { "player_running_left_0.png",
                        "player_running_left_1.png",
                        "player_running_left_2.png",
                        "player_running_left_3.png"} );

		setList = new ArrayList<Tuple<Integer, Integer>>();
		doneList = new ArrayList<String>();

        pickups = new ArrayList<Pickup>();

        meterEmpty = new Texture("gains_meter_low.png");
        meterFull  = new Texture("gains_meter_high.png");

        health = new Texture("health_base.png");
        healthSL = new Texture("health_shoulder_left.png");
        healthSR = new Texture("health_shoulder_right.png");
        healthC = new Texture("health_chest.png");
        healthL = new Texture("health_legs.png");
        healthBL = new Texture("health_bicep_left.png");
        healthBR = new Texture("health_bicep_right.png");

        hsr = hsl = hc = hl = hbl = hbr = 0;

        newSet = new Texture("new_set.png");
        rest = new Texture("rest.png");

        popups = new ArrayList<Popup>();

        mainmenu = new Texture("mainmenu.png");
        gameover = new Texture("gameover.png");

        play = new Texture("play.png");
        play2 = new Texture("play_highlight.png");
        replay = new Texture("replay.png");
        replay2 = new Texture("replay_highlight.png");

        liftSounds = new Sound[]{
            Gdx.audio.newSound(Gdx.files.internal("lift_1.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_2.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_3.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_4.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_5.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_6.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_7.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_8.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_9.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_10.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_11.wav")),
                Gdx.audio.newSound(Gdx.files.internal("lift_12.wav")),
        };

        stepA = new Sound[]
                {
                        Gdx.audio.newSound(Gdx.files.internal("step_a_1.wav")),
                        Gdx.audio.newSound(Gdx.files.internal("step_a_2.wav")),
                        Gdx.audio.newSound(Gdx.files.internal("step_a_3.wav")),
                };

        stepB = new Sound[]
                {
                        Gdx.audio.newSound(Gdx.files.internal("step_b_1.wav")),
                        Gdx.audio.newSound(Gdx.files.internal("step_b_2.wav")),
                        Gdx.audio.newSound(Gdx.files.internal("step_b_3.wav")),
                };
	}

	List<Tuple<Integer, Integer>> makeNewSet(int level)
    {
        List<Tuple<Integer, Integer>> res = new ArrayList<Tuple<Integer, Integer>>();

        switch (level)
        {
            case 0:
            {

                //res.add(new Tuple<Integer, Integer>(5, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(5, Pickup.TYPE_DUMBBELL));
                break;
            }
            case 1:
            {
                res.add(new Tuple<Integer, Integer>(3, Pickup.TYPE_SQUAT));
                res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_BARBELL));
                //res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_DUMBBELL));
                break;
            }
            case 2:
            {
                res.add(new Tuple<Integer, Integer>(5, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_DUMBBELL));
                break;
            }
            case 3:
            {
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_SQUAT));
                break;
            }
            case 4:
            {
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_DUMBBELL));
                //res.add(new Tuple<Integer, Integer>(0, Pickup.TYPE_SQUAT));
                break;
            }
            case 5:
            {
                res.add(new Tuple<Integer, Integer>(5, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_SQUAT));
                break;
            }
            case 6:
            {
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_SQUAT));
                break;
            }
            case 7: {
                //res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(100, Pickup.TYPE_DUMBBELL));
                //res.add(new Tuple<Integer, Integer>(0, Pickup.TYPE_SQUAT));
                break;
            }
            case 8:
            {
                res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_BARBELL));
                //res.add(new Tuple<Integer, Integer>(, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_SQUAT));
                break;
            }
            case 9:
            {
                res.add(new Tuple<Integer, Integer>(40, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(80, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_SQUAT));
                break;
            }
            case 10:
            {
                res.add(new Tuple<Integer, Integer>(60, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(25, Pickup.TYPE_SQUAT));
                break;
            }
            case 11:
            {
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_SQUAT));
                break;
            }
            case 12:
            {
                //res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_BARBELL));
                //res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(30, Pickup.TYPE_SQUAT));
                break;
            }
            case 13:
            {
                res.add(new Tuple<Integer, Integer>(80, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(80, Pickup.TYPE_DUMBBELL));
                //res.add(new Tuple<Integer, Integer>(0, Pickup.TYPE_SQUAT));
                break;
            }
            case 14:
            {
                res.add(new Tuple<Integer, Integer>(100, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(100, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(60, Pickup.TYPE_SQUAT));
                break;
            }
            case 15:
            {
                res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_SQUAT));
                break;
            }
            case 16:
            {
                //res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(150, Pickup.TYPE_DUMBBELL));
                //res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_SQUAT));
                break;
            }
            case 17:
            {
                res.add(new Tuple<Integer, Integer>(150, Pickup.TYPE_BARBELL));
                //res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_DUMBBELL));
                //res.add(new Tuple<Integer, Integer>(10, Pickup.TYPE_SQUAT));
                break;
            }
            case 18:
            {
                //res.add(new Tuple<Integer, Integer>(20, Pickup.TYPE_BARBELL));
                //res.add(new Tuple<Integer, Integer>(15, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(150, Pickup.TYPE_SQUAT));
                break;
            }
            case 19:
            {
                res.add(new Tuple<Integer, Integer>(300, Pickup.TYPE_BARBELL));
                res.add(new Tuple<Integer, Integer>(300, Pickup.TYPE_DUMBBELL));
                res.add(new Tuple<Integer, Integer>(300, Pickup.TYPE_SQUAT));
                break;
            }
            default:
            {
                if(new Random().nextBoolean())
                    res.add(new Tuple<Integer, Integer>(new Random().nextInt(10 * (level - 17) * 10), Pickup.TYPE_BARBELL));
                if(new Random().nextBoolean())
                    res.add(new Tuple<Integer, Integer>(new Random().nextInt(10 * (level - 17) * 10), Pickup.TYPE_DUMBBELL));
                if(new Random().nextBoolean())
                    res.add(new Tuple<Integer, Integer>(new Random().nextInt(10 * (level - 17) * 10), Pickup.TYPE_SQUAT));
                break;
            }
        }


        return res;
    }

	long milliTime;
	boolean hover = false;
	int lvl = 0;
	long sleepStart = 0;
	long stepTime = 0;

	public void update() {
        milliTime = (long)(t * 1000);


        if(setList.size() == 0) //Make a new set
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            {
                if(player.posX * Gdx.graphics.getWidth() < 24*8)
                {
                    doneList = new ArrayList<String>();
                    setList = makeNewSet(lvl);
                    lvl++;

                    int ii = 0;
                    for(Tuple<Integer, Integer> t : setList)
                    {
                        float b = ii / (float)setList.size();
                        b = b * 0.5f + 0.25f;
                        pickups.add(new Pickup(b, 0.1f, t.y));
                        ii++;
                    }
                }
            }
        }

        if(player.posX * Gdx.graphics.getWidth() > Gdx.graphics.getWidth() - 24*12)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            {
                if(player.state != Character.STATE_RESTING)
                {
                    player.state = Character.STATE_RESTING;
                    sleepStart = milliTime;
                    popups.add(new Popup("Resting *2 recovery", milliTime, 7500));
                }
            }
        }


        if(Gdx.input.getX() > player.posX * Gdx.graphics.getWidth() && Gdx.input.getX() < player.posX * Gdx.graphics.getWidth() + 24*8)
        {
            if(Gdx.graphics.getHeight() - Gdx.input.getY() > player.posY * Gdx.graphics.getHeight() && Gdx.graphics.getHeight() - Gdx.input.getY() < player.posY * Gdx.graphics.getHeight() + 24*8)
            {
                if(!hover)
                {
                    if(new Random().nextFloat() > 0.95f)
                        popups.add(new Popup("U mirin bro?", milliTime, 1250));
                    hover = true;
                }
            }
            else
            {
                hover = false;
            }
        }
        else
        {
            hover = false;
        }

        if(powerLevel < 0.1f)
        {
            popups.add(new Popup("Gains in danger!", milliTime, 1));
        }

        if(player.state != Character.STATE_RESTING) {

            if(hsl > .85f || hsr > .85f || hbl > .85f || hbr > .85f || hl > .85f || hc > .85f)
                popups.add(new Popup("Health in danger!", milliTime, 1));



            switch (player.holding) {
                case -1: //Holding nothing
                {
                    player.state = Character.STATE_STANDING;

                    player.speed = 0.25f / 60;
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        player.state = Character.STATE_RUNNING;
                        player.run(false);
                        if(milliTime - 250 > stepTime)
                        {
                            step = !step;
                            long x = step ? stepA[new Random().nextInt(stepA.length)].play() : stepB[new Random().nextInt(stepB.length)].play();
                            stepTime = milliTime;
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                        player.state = Character.STATE_RUNNING;
                        player.run(true);
                        if(milliTime - 250 > stepTime)
                        {
                            step = !step;
                            long x = step ? stepA[new Random().nextInt(stepA.length)].play() : stepB[new Random().nextInt(stepB.length)].play();
                            stepTime = milliTime;
                        }
                    }

                    //Try to pick shit up, cos thats what pickups are for
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        for (Pickup p : pickups) {
                            if (Math.abs(p.posX - player.posX) < 0.05f) {
                                player.holding = p.type;
                                player.state = Character.STATE_LIFTING;
                                pickups.remove(p);
                                break;
                            }
                        }
                    }

                    break;
                }
                case Pickup.TYPE_BARBELL: {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                        player.holding = -1;
                        player.state = Character.STATE_STANDING;
                        pickups.add(new Pickup(player.posX, 0.1f, Pickup.TYPE_BARBELL));
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.A))
                        player.direction = false;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D))
                        player.direction = true;

                    //Throw yo shit away
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        liftSounds[new Random().nextInt(liftSounds.length)].play();

                        hsl += 0.025f;
                        hsr += 0.025f;
                        hbl += 0.01f;
                        hbr += 0.01f;
                        hc += 0.005f;
                        hl += 0.015f;
                        powerLevel += 0.002f;

                        for (Tuple<Integer, Integer> set : setList) {
                            if (set.x > 0) {
                                if (set.y == player.holding) {
                                    set.x--;
                                    if (set.x == 0) {
                                        System.out.println("Worked out");
                                        player.holding = -1; //TODO: Throw effect
                                        player.state = Character.STATE_STANDING;
                                        powerLevel += 0.3;
                                        setList.remove(set);
                                        doneList.add("Barbell");
                                        popups.add(new Popup("Bonus 3 gains", milliTime, 1000));
                                        break;
                                    }
                                }
                            }
                        }
                    } else //Just to give a little 1 frame length animation
                    {
                        player.lift = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    }

                    break;
                }
                case Pickup.TYPE_DUMBBELL: {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                        player.holding = -1;
                        player.state = Character.STATE_STANDING;
                        pickups.add(new Pickup(player.posX, 0.1f, Pickup.TYPE_DUMBBELL));
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.hand = true;
                        player.direction = false;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.direction = true;
                        player.hand = false;
                    }

                    //Throw yo shit away
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

                        liftSounds[new Random().nextInt(liftSounds.length)].play();

                        if(player.hand)
                            hsl += 0.01f;
                        else
                            hsr += 0.01f;
                        if(player.hand)
                            hbl += 0.02f;
                        else
                            hbr += 0.02f;
                        hc += 0.00f;
                        hl += 0.00f;
                        powerLevel += 0.003f;


                        for (Tuple<Integer, Integer> set : setList) {
                            if (set.x > 0) {
                                if (set.y == player.holding) {
                                    set.x--;
                                    if (set.x == 0) {
                                        System.out.println("Worked out");
                                        player.holding = -1; //TODO: Throw effect
                                        player.state = Character.STATE_STANDING;
                                        powerLevel += 0.2f;
                                        setList.remove(set);
                                        doneList.add("Dumbbells");
                                        popups.add(new Popup("Bonus 2 gains", milliTime, 1000));
                                        break;
                                    }
                                }
                            }
                        }
                    } else //Just to give a little 1 frame length animation
                    {
                        player.lift = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    }

                    break;
                }
                case Pickup.TYPE_SQUAT: {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                        player.holding = -1;
                        player.state = Character.STATE_STANDING;
                        pickups.add(new Pickup(player.posX, 0.1f, Pickup.TYPE_SQUAT));
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.A))
                        player.direction = false;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D))
                        player.direction = true;

                    //Throw yo shit away
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

                        liftSounds[new Random().nextInt(liftSounds.length)].play();

                        hsl += 0.001f;
                        hsr += 0.001f;
                        hbl += 0.00f;
                        hbr += 0.00f;
                        hc += 0.01f;
                        hl += 0.03f;
                        powerLevel += 0.003f;

                        for (Tuple<Integer, Integer> set : setList) {
                            if (set.x > 0) {
                                if (set.y == player.holding) {
                                    set.x--;
                                    if (set.x == 0) {
                                        System.out.println("Worked out");
                                        player.holding = -1; //TODO: Throw effect
                                        player.state = Character.STATE_STANDING;
                                        powerLevel += 0.2;
                                        setList.remove(set);
                                        doneList.add("Squats");
                                        popups.add(new Popup("Bonus 2 gains", milliTime, 1000));
                                        break;
                                    }
                                }
                            }
                        }
                    } else //Just to give a little 1 frame length animation
                    {
                        player.lift = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    }

                    break;
                }

            }
        }
        else //Bruhh we sleepin in 'ere
        {
            //Heal twice as fast
            hsl = Math.max(Math.min(hsl - 0.0003f, 10), 0);
            hsr = Math.max(Math.min(hsr - 0.0003f, 10), 0);
            hbr = Math.max(Math.min(hbr - 0.0005f, 10), 0);
            hbl = Math.max(Math.min(hbl - 0.0005f, 10), 0);
            hc = Math.max(Math.min(hc - 0.0002f, 10), 0);
            hl = Math.max(Math.min(hl - 0.0005f, 10), 0);

            //Weaken 1.5 as fast
            powerLevel -= 0.5f/60f/60f; //


            if(sleepStart + 7500 < milliTime)
            {
                //Done with sleep
                player.state = Character.STATE_STANDING;
            }
        }
    }

    boolean clicked = false;

    void mainMenu()
    {
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(mainmenu, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(Gdx.input.getX() > 0.35f * Gdx.graphics.getWidth() && Gdx.input.getX() < 0.65f *  Gdx.graphics.getWidth() &&
                Gdx.input.getY() > 0.55f * Gdx.graphics.getHeight() && Gdx.input.getY() < 0.75f *  Gdx.graphics.getHeight())
        {
            batch.draw(play2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            {
                if(!clicked) {
                    gameState = STATE_PLAYING;
                    t = 0;
                    lvl = 0;
                    milliTime = 0;
                    powerLevel = 1f;
                    setList = new ArrayList<Tuple<Integer, Integer>>();
                    doneList = new ArrayList<String>();

                    pickups = new ArrayList<Pickup>();
                    clicked = true;

                    hsr = hsl = hc = hl = hbl = hbr = 0;
                    create();
                    return;
                }
            }
            else
                clicked = false;
        }
        else
        {
            batch.draw(play, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        batch.end();
    }

    long endTime = 0;

    void gameOver()
    {
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(gameover, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.getData().setScale(1.5f);
        font.setColor(0.1f, 0.1f, 0.1f, 1);
        font.draw(batch, "Lifted for " + (endTime / 1000 / 60) + "min and " + ((endTime / 1000)%60) + "sec",Gdx.graphics.getWidth() / 2 - 380, Gdx.graphics.getHeight() * (1f/2f) + 60);

        if(Gdx.input.getX() > 0.35f * Gdx.graphics.getWidth() && Gdx.input.getX() < 0.65f *  Gdx.graphics.getWidth() &&
                Gdx.input.getY() > 0.55f * Gdx.graphics.getHeight() && Gdx.input.getY() < 0.75f *  Gdx.graphics.getHeight())
        {
            batch.draw(replay2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            {
                if(!clicked)
                {
                    gameState = STATE_MAIN_MENU;
                    clicked = true;
                }
            }
            else
                clicked = false;
        }
        else
        {
            batch.draw(replay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        batch.end();
    }

    float t = 0;

	int gameState = 1;
	int STATE_PLAYING = 0;
	int STATE_MAIN_MENU = 1;
	int STATE_GAME_OVER = 2;

	@Override
	public void render () {

	    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
	        Gdx.app.exit();
        //System.exit(0);

        if(gameState == STATE_MAIN_MENU)
        {
            mainMenu();
        }
        else if(gameState == STATE_GAME_OVER)
        {
            gameOver();
        }

        if(gameState != STATE_PLAYING)
            return;

        t += 1/60f; //Fixed timestep of 16 2/3 milli

        powerLevel -= 1f/60f/60f; //1 minute by default

        if(powerLevel <= 0)
        {
            //Game fucking over, bro
            System.out.println("Game over");
            gameState = STATE_GAME_OVER;
            endTime = milliTime;
        }

        if(hsl > 1f || hsr > 1 || hbl > 1 || hbr > 1 || hc > 1 || hl > 1)
        {
            gameState = STATE_GAME_OVER;
            endTime = milliTime;
        }

        powerLevel = Math.max(Math.min(powerLevel, 1f), 0f);

        hsl = Math.max(Math.min(hsl - 0.0002f, 1152), 0);
        hsr = Math.max(Math.min(hsr - 0.0002f, 1512), 0);
        hbr = Math.max(Math.min(hbr - 0.0004f, 12345), 0);
        hbl = Math.max(Math.min(hbl - 0.0004f, 12354), 0);
        hc = Math.max(Math.min(hc - 0.0001f, 154), 0);
        hl = Math.max(Math.min(hl - 0.00025f, 1543), 0);

		update();

		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		{
            font.getData().setScale(1);

		    float w = Gdx.graphics.getWidth();
		    float h = Gdx.graphics.getHeight();
			//Draw fullscreen wallpaper
			batch.draw(background, 0, 0, w, h);

            batch.draw(rest, w - 24*8 - 0.01f*w, 0.07f * h, 24*8, 24*8);

            Texture pTex = player.getTexture();
			batch.draw(pTex, player.posX * w, player.posY * h, pTex.getWidth() * player.scale, pTex.getHeight() * player.scale);

			for(Pickup p : pickups)
            {
			    batch.draw(p.tex, p.posX * w, p.posY * h, p.tex.getWidth() * player.scale, p.tex.getHeight() * player.scale);
            }

            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(100, h - 100, 240, -(setList.size() + doneList.size()  + 1) * font.getLineHeight());
            shapeRenderer.end();

            batch.begin();

            font.setColor(0.1f, 0.8f, 0.1f, 1);
            font.getData().setScale(0.75f);

			int i = 0;
			for(String s : doneList)
            {
                font.draw(batch, s, 110, h - font.getLineHeight() * i - 110);
                i++;
            }

            font.setColor(0.1f, 0.1f, 0.1f, 1f);

            for(Tuple<Integer, Integer> s : setList)
            {
                String str = "wat tf";
                switch (s.y)
                {
                    case Pickup.TYPE_BARBELL:
                    {
                        str = "Barbell";
                        break;
                    }
                    case Pickup.TYPE_DUMBBELL:
                    {
                        str = "Dumbbell";
                        break;
                    }
                    case Pickup.TYPE_SQUAT:
                    {
                        str = "Squats";
                        break;
                    }
                }

                str = s.x + "* " + str;
                font.draw(batch, str, 110, h - font.getLineHeight() * i - 110);
                i++;
            }


            float j = meterFull.getWidth() * player.scale;
            float k = meterFull.getHeight() * player.scale;
            TextureRegion tr = new TextureRegion(meterFull, 0, (int)Math.floor(meterFull.getHeight() * (1 - powerLevel)), meterFull.getWidth(), (int)Math.ceil(meterFull.getHeight() * powerLevel));
            batch.draw(meterEmpty, w - 100 - j, h - 100 - k, j, k);
            batch.draw(tr, w - 100 -j, h - 100 - k, j, (int)(k * powerLevel) / (int)player.scale * (int)player.scale);

            String str = "Pathetic";
            if(powerLevel > 0.8f)
            {
                str = "Mad gains";
            }
            else if(powerLevel > 0.5f)
            {
                str = "No homo bro";
            }
            else if(powerLevel > 0.3f)
            {
                str = "U even lift";
            }
            else if(powerLevel > 0.1f)
            {
                str = "String arms";
            }

            font.getData().setScale(1f);
            font.draw(batch, str, w - 100 - j, h - 120 - k);


            //Health status
            batch.draw(health, (w-j) / 2, h - 100 - k, j, k);

            batch.setColor(1, 1, 1, hsl);
            batch.draw(healthSL, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, hsr);
            batch.draw(healthSR, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, hbl);
            batch.draw(healthBL, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, hbr);
            batch.draw(healthBR, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, hc);
            batch.draw(healthC, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, hl);
            batch.draw(healthL, (w-j) / 2, h - 100 - k, j, k);
            batch.setColor(1, 1, 1, 1);

            //Shoulders
            font.getData().setScale(0.5f);
            font.draw(batch, "Shoulder: " + (int)(hsr*100), (w-j)/2+190,h+61-k);
            font.draw(batch, "Shoulder: " + (int)(hsl*100), (w-j)/2-120,h+61-k);
            //Biceps
            font.draw(batch, "Bicep: " + (int)(hbr*100), (w-j)/2+200,h-18-k);
            font.draw(batch, "Bicep: " + (int)(hbl*100), (w-j)/2-90,h-18-k);
            //Chest and legs
            font.draw(batch, "Chest: " + (int)(hc*100), (w-j)/2+174,h+93-k);
            font.draw(batch, "Legs: " + (int)(hl*100), (w-j)/2-60,h-92-k);

            if(setList.size() == 0) //Done with workout
            {

                batch.draw(newSet, 0.01f * w, 0.07f * h, j, k);
            }




            List<Popup> rem = new ArrayList<Popup>();

            i = 0;
            for(Popup p : popups)
            {
                if(p.startTime + p.length < milliTime)
                {
                    System.out.println("asd");
                    rem.add(p);
                    continue;
                }

                font.getData().setScale(2f);
                GlyphLayout gl = new GlyphLayout();
                gl.setText(font, p.str);
                font.draw(batch, p.str, w/2f - gl.width/2, h*(1f/3f) - i*gl.height*1.2f);
                i++;

            }

            popups.removeAll(rem);

            font.getData().setScale(1f);
            font.draw(batch, "Set list", 130, h - 50);
            font.draw(batch, "Health",  w/2-80, h- 50);
            font.draw(batch, "Power", w-280, h - 50);

            font.getData().setScale(0.5f);
            font.setColor(.9f, .9f, .9f, 1);

            font.draw(batch, "Move with A and D,                    Interact with SPACE,                    drop with SHIFT", 220, h-12);

            font.setColor(.1f, .1f, .1f, 1);



            for (Pickup p : pickups) {
                if (Math.abs(p.posX - player.posX) < 0.05f) {
                    str = "wt tf";
                    switch(p.type)
                    {
                        case Pickup.TYPE_DUMBBELL:
                        {
                            str = "Dumbbell";
                            break;
                        }
                        case Pickup.TYPE_BARBELL:
                        {
                            str = "Barbell";
                            break;
                        }
                        case Pickup.TYPE_SQUAT:
                        {
                            str = "Squats";
                            break;
                        }
                    }

                    font.draw(batch, str, p.posX * w + 50, 0.07f * h);
                    break;
                }
            }

		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
