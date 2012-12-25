package fi.muzzy.pathfoo;

public interface MovementLogicInterface {
	public int heuristic(int start, int current, int end);
	public int distance(int idx1, int idx2);
	public int[] getNeighbors(int idx);
}
