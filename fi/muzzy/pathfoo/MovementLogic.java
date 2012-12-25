package fi.muzzy.pathfoo;

import cern.colt.list.IntArrayList;

public class MovementLogic implements MovementLogicInterface {
	MovementMap map;
	int width, height, depth;

	public MovementLogic(MovementMap map) {
		this.map = map;
		width = map.width();
		height = map.height();
		depth = map.depth();
	}

	// Expected maximum path length used as multiplier for tiebreaking purposes
	private final static int maxPath = 10000;

	public int heuristic(int start, int current, int end) {
		int x0 = start%width;
		int y0 = start%(width*height)/width;
		int z0 = start/(width*height);
		int x1 = current%width;
		int y1 = current%(width*height)/width;
		int z1 = current/(width*height);
		int x2 = end%width;
		int y2 = end%(width*height)/width;
		int z2 = end/(width*height);
		
		int dx = Math.abs(x2-x1);
		int dy = Math.abs(y2-y1);
		int dz = Math.abs(z2-z1);
		// diagonal distance
		int h1 = Math.max(Math.max(dx, dy), dz);
		int h2 = Math.abs( (x1-x2)*(y0-y2) - (x0-x2)*(y1-y2) );
		int h = (55 * h1);
		// add tiebreaker, prefers straight paths
		return h * maxPath + h2*maxPath/(width*height);
	}

	public int distance(int idx1, int idx2) {
		// prefer stepping into road tiles
		if ((map.getFlags(idx2) & MovementMap.ROAD) != 0) {
			return 55*maxPath;
		} else {
			return 100*maxPath;
		}
	}

	protected void consider(IntArrayList list, int idx) {
		if ((map.getFlags(idx) & MovementMap.WALKABLE) != 0) {
			list.add(idx);
		}
	}

	public int[] getNeighbors(int idx) {
		int x = idx%width;
		int y = idx%(width*height)/width;
		int z = idx/(width*height);

		IntArrayList neighbors = new IntArrayList();
		if (x>0) consider(neighbors, idx-1);
		if (y>0) consider(neighbors, idx-width);
		if (x<width-1) consider(neighbors, idx+1);
		if (y<height-1) consider(neighbors, idx+width);
		if (x>0 && y>0) consider(neighbors, idx-1-width);
		if (y>0 && x<width-1) consider(neighbors, idx-width+1);
		if (x<width-1 && y<height-1) consider(neighbors, idx+1+width);
		if (y<height-1 && x>0) consider(neighbors, idx+width-1);
		// TODO z-axis ... what's the s() function in obfuscated code?

		neighbors.trimToSize();
		return neighbors.elements();
	}
}
