package fi.muzzy.util;

public class IntPriorityQueue extends cern.colt.list.IntArrayList {

	// One entry for value, one for score
			
	public void queue(int value, int score) {
		int idx = size;
		// dummy items to be overwritten
		if (size+2 >= elements.length)
			ensureCapacity(size+2);
		size+=2;
		siftUp(idx, value, score);
	}
	
	private void siftDown(int idx, int value, int score) {
		int half = size/4;
		while (idx/2 < half) {
			int child = ((idx/2)*2+1)*2;
			int right = child+2;
			if (right < size && elements[child+1] > elements[right+1]) {
				child = right;
			}
			if (score <= elements[child+1])
				break;
				
			elements[idx] = elements[child];
			elements[idx+1] = elements[child+1];
			idx = child;
		}
		elements[idx] = value;
		elements[idx+1] = score;
	}
	
	private void siftUp(int idx, int value, int score) {
		// until top is reached or heap shape is satisfied, bubble up
		while (idx>0) {
			int parent = (((idx/2)-1)/2)*2;
			int parentScore = elements[parent+1];
			if (parentScore <= score) break;
			elements[idx] = elements[parent];
			elements[idx+1] = elements[parent+1];
			idx = parent;
		}
		// found a place for new value
		elements[idx] = value;
		elements[idx+1] = score;
	}
	
	public int getNext() {
		if (size == 0) throw new IllegalStateException("Queue empty");
		int retVal = elements[0];
		// grab the last value
		int value = elements[size-2];
		int score = elements[size-1];
		size -= 2;
		if (size == 0)
			return retVal;
		siftDown(0, value, score);

		return retVal;
	}
	
	public int indexOfValue(int value) {
		for (int i=0; i<size; i+=2)
			if (elements[i] == value)
				return i;
		throw new IllegalStateException("Value not found");
	}
	
	public boolean containsValue(int value) {
		for (int i=0; i<size; i+=2)
			if (elements[i] == value)
				return true;
		return false;
	}
	
	public void changeScore(int value, int score) {
		int i = indexOfValue(value);
		if (i==size) {
			// replacing last element
			siftUp(size-2, value, score);
		} else {
			size-=2;
			int tmpVal = elements[size];
			int tmpScore = elements[size+1];
			siftDown(i, tmpVal, tmpScore);
			if (elements[i] == tmpVal) {
				siftUp(i, tmpVal, tmpScore);
			}
			queue(value, score);
		}
	}
}
