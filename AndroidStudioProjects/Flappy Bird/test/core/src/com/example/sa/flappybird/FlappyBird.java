package com.example.sa.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] bird;
	Texture toptube;
	Texture bottomtube;
	int flapstate=0;
	int score =0;
	int scoringtube=0;
	BitmapFont font;
	float birdY=0;
	float velocity=0;
	int gamestate=0;
	float gravity=1.4f;
	float gap=500;
	Texture gameover;
	float maxtubeoffset;
	Random randomnumbergenerator;
	int tubenumber=4;
	Circle birdcircle;
	ShapeRenderer shapeRenderer;
	float[] tubeoffset=new float[tubenumber];
	float tubevelocity=5;
	float[] tubex=new float[tubenumber];
	float distancebettubes;
	Rectangle[] toptuberectangle;
	Rectangle[] bottomtuberectangle;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		shapeRenderer=new ShapeRenderer();
		birdcircle=new Circle();
		bird = new Texture[2];
		gameover=new Texture("index.png");
		font =new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().scale(10);
		bird[0]=new Texture("bird.png");
		bird[1]=new Texture("bird2.png");
		toptube=new Texture ("toptube.png");
		maxtubeoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		bottomtube=new Texture("bottomtube.png");
		distancebettubes=Gdx.graphics.getWidth()*5/6;
		toptuberectangle=new Rectangle[tubenumber];
		bottomtuberectangle=new Rectangle[tubenumber];

		randomnumbergenerator=new Random();
	start_game();
	}
	public void start_game()
	{
		birdY=Gdx.graphics.getHeight()/2-bird[flapstate].getHeight()/2;
		for(int i=0;i<tubenumber;i++)
		{
			tubeoffset[i]=(randomnumbergenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubex[i]=Gdx.graphics.getWidth()*1.25f-toptube.getWidth()/2+i*distancebettubes;
			toptuberectangle[i]=new Rectangle();
			bottomtuberectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate==1) {
			if(tubex[scoringtube] < Gdx.graphics.getWidth()/2)
			{
				score++;
				if(scoringtube < tubenumber-1)
				{
					scoringtube++;

				}
				else
				{
					scoringtube=0;
				}
			}
			if(Gdx.input.justTouched()) {
				velocity -= 40;
					}
			for(int i=0;i<tubenumber;i++) {
				if(tubex[i]<-toptube.getWidth())
				{
					tubex[i]+=tubenumber*distancebettubes;
					tubeoffset[i]=(randomnumbergenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);


				}
				else {
					tubex[i] -= tubevelocity;

				}

				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
				toptuberectangle[i]=new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
				bottomtuberectangle[i]=new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
			}



			if(birdY>0 && (birdY+bird[flapstate].getHeight()/2)<Gdx.graphics.getHeight()){
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}
			else
			{
				gamestate=2;
			}
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Touched", "yes");
			}

		}
		else if(gamestate==0)
		{
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}
		}
		else if(gamestate==2)
		{
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2,gameover.getWidth(),gameover.getHeight());
		if(Gdx.input.justTouched())
		{
			gamestate=1;
			start_game();
			score=0;
			scoringtube=0;
			velocity=0;
		}
		}

		if(flapstate==0)
		{
			flapstate=1;
		}
		else
		{
			flapstate=0;

		}


				batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdY);
				font.draw(batch,String.valueOf(score),100,150);
		batch.end();
		birdcircle.set(Gdx.graphics.getWidth()/2,birdY+bird[flapstate].getHeight()/2,bird[flapstate].getWidth()/2-4);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
		for(int i =0; i<tubenumber;i++) {

			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
			if(Intersector.overlaps(birdcircle,toptuberectangle[i]) || Intersector.overlaps(birdcircle,bottomtuberectangle[i]))
			{
				gamestate=2;
				Gdx.app.log("game","over");
			}
		}

		shapeRenderer.end();
	}

	
//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
