import cern.colt.list.IntArrayList;

class MovementLogic {
	MovementMap map;
	int width, height, depth;
	
	public MovementLogic(MovementMap map) {
		this.map = map;
		width = map.width();
		height = map.height();
		depth = map.depth();
	}
	
	public int heuristic(int idx1, int idx2) {
		int x1 = idx1%width;
		int y1 = idx1%(width*height)/width;
		int z1 = idx1/(width*height);
		int x2 = idx2%width;
		int y2 = idx2%(width*height)/width;
		int z2 = idx2/(width*height);
		// assume roads available for the whole path
		return 5 * Math.max(Math.max(Math.abs(x2-x1), Math.abs(y2-y1)), Math.abs(z2-z1));
	}
	
	public int distance(int idx1, int idx2) {
		if ((map.getFlags(idx1) & MovementMap.ROAD) != 0) {
			return 5;
		} else {
			return 10;
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
