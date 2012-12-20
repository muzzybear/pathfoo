import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class test {

	public static void main(String[] params) {
		MovementMap map = new MovementMap(200,200,20);


		for(int i=0; i<200; i++) {
			for (int j=0; j<200; j++) {
				map.enableFlags(i, j, 0, MovementMap.WALKABLE);
			}
		}
		/*
		for(int i=1; i<199; i++) {
			map.disableFlags(i, 1, 0, MovementMap.WALKABLE);
			map.disableFlags(i, 198, 0, MovementMap.WALKABLE);
			map.disableFlags(1, i, 0, MovementMap.WALKABLE);
		}
		*/

		for(int i=1; i<199; i++) {
			map.disableFlags(i-1, 1, 0, MovementMap.WALKABLE);
			map.disableFlags(i+1, 198, 0, MovementMap.WALKABLE);
		}

		for(int i=40; i<160; i++) {
			map.enableFlags(i, 40, 0, MovementMap.ROAD);
			map.enableFlags(i, 160, 0, MovementMap.ROAD);
			map.enableFlags(40, i, 0, MovementMap.ROAD);
			map.enableFlags(160, i, 0, MovementMap.ROAD);
		}

		MovementLogic logic = new MovementLogic(map);

		int[] path = map.findPath(logic, 0,0,0, 199,199,0);
		path = map.findPath(logic, 0,0,0, 197,199,0);
		path = map.findPath(logic, 0,0,0, 196,199,0);
		path = map.findPath(logic, 0,0,0, 195,199,0);
		path = map.findPath(logic, 0,0,0, 194,199,0);
		
		path = map.findPath(logic, 30,2,0, 100,195,0);

		System.out.println("foofoo");

		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			System.out.println("fail: "+ e);
		}
		System.out.println("mmkay");

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		// Origo in top left
		GL11.glOrtho(0, 400, 300, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		int[] foo = path.clone();
		Arrays.sort(foo);
		
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glBegin(GL11.GL_QUADS);
			for (int y=0; y<200; y++) {
				for (int x=0; x<200; x++) {
					byte flags = map.getFlags(x,y,0);
					if ((flags & MovementMap.WALKABLE) != 0) {
						int idx = x+y*200;
						if (Arrays.binarySearch(foo, idx)>=0) {
							// Part of path
							if ((flags & MovementMap.ROAD) != 0)
								GL11.glColor3f(0.5f,0.9f,0.3f);
							else
								GL11.glColor3f(0.3f,0.7f,0.2f);
						} else {
							if ((flags & MovementMap.ROAD) != 0) {
								// Road
								GL11.glColor3f(0.5f,0.4f,0.3f);
							} else {
								// Not road
								GL11.glColor3f(0.3f,0.3f,0.5f);
							}
						}
					} else {
						// Not walkable
						GL11.glColor3f(0.7f,0.2f,0.2f);
					}
					GL11.glVertex2f(x,y);
					GL11.glVertex2f(x+1,y);
					GL11.glVertex2f(x+1,y+1);
					GL11.glVertex2f(x,y+1);
				}
			}
			GL11.glEnd();

			Display.update();
		}
		Display.destroy();
	}
}
