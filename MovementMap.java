import java.util.SortedSet;
import java.util.Set;
import java.util.*;
import cern.colt.map.OpenIntIntHashMap;
import cern.colt.list.IntArrayList;
import cern.colt.bitvector.BitVector;

class MovementMap {
	public static final byte WALKABLE = 1;
	public static final byte ROAD = 2;
	
	protected int width, height, depth;
	protected byte[] data;
		
	public MovementMap(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		data = new byte[width*height*depth];
	}
	
	public MovementMap() {
		this(200, 200, 20);
	}
	
	public void enableFlags(int x, int y, int z, byte flags) {
		int idx = x+y*width+z*width*height;
		data[idx] |= flags;
	}

	public void disableFlags(int x, int y, int z, byte flags) {
		int idx = x+y*width+z*width*height;
		data[idx] &= ~flags;
	}
	
	public int width() { return width; }
	public int height() { return height; }
	public int depth() { return depth; }
	
	public byte getFlags(int idx) {
		return data[idx];
	}
	
	public byte getFlags(int x, int y, int z) {
		int idx = x+y*width+z*width*height;
		return data[idx];
	}
	
	/** findPath performs A* pathfinding based on given MovementLogic class */
	
	public int[] findPath(MovementLogic logic, int x1, int y1, int z1, int x2, int y2, int z2) {
		int startIdx = x1 + y1*width + z1*width*height;
		int endIdx = x2 + y2*width + z2*width*height;
		if (startIdx == endIdx) {
			int[] path = new int[1];
			path[0] = startIdx;
			return path;
		}

		long startTime = System.nanoTime();

		IntPriorityQueue open = new IntPriorityQueue();
		BitVector closed = new BitVector(width*height*depth);
		BitVector openVec = new BitVector(width*height*depth);
				
		OpenIntIntHashMap cameFrom = new OpenIntIntHashMap();
		open.queue(startIdx, logic.heuristic(startIdx, startIdx, endIdx));
		openVec.set(startIdx);
		OpenIntIntHashMap gScore = new OpenIntIntHashMap(); // cost from beginning to idx
		int tmp_counter = 0;
		int maxOpen = 0;
		
		while (!open.isEmpty()) {
			tmp_counter ++;
			// find the lowest open fScore
			int current = open.getNext();
			// If we've reached the end, construct and return path
			if (current == endIdx) {
				IntArrayList path = new IntArrayList();
				do {
					path.add(current);
					current = cameFrom.get(current);
				} while (current != startIdx);
				path.trimToSize();
				path.reverse();
				System.out.println(tmp_counter);
				System.out.println((System.nanoTime() - startTime) / 1000000000.0);				
				return path.elements();
			}
			// move current from open to closed 
			openVec.clear(current);
			closed.set(current);
			int[] neighbors = logic.getNeighbors(current);
			for(int neighborIdx=0; neighborIdx<neighbors.length; neighborIdx++) {
				int neighbor = neighbors[neighborIdx];
				// if neighbor in closedset continue
				if (closed.getQuick(neighbor))
					continue;
				int tmpG = gScore.get(current) + logic.distance(current, neighbor); 
				// if neighbor not in openset OR tmpG < gScore[neighbor]
				// disregard new paths with equal score, first one of same length wins to allow heuristic to choose prettiest path
				if (!openVec.getQuick(neighbor) || tmpG < gScore.get(neighbor))
				{
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tmpG);
					int score = tmpG + logic.heuristic(startIdx, neighbor, endIdx);
					
					// if neighbor not in openset THEN add neighbor to openset
					if (!openVec.getQuick(neighbor)) {
						open.queue(neighbor, score);
						openVec.set(neighbor);
					} else {
						// find score and replace
						open.changeScore(neighbor, score);
					}
				}
			}
		}
		
		// return failure
		return new int[0];
	}
}
