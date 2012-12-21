Pathfoo A* Pathing
==================

Just a dummy pathfinding implementation in java.

The test.java runs pathfinding several times because during the first run the optimizer hasn't kicked in yet,
on my computer the first pathing takes 170ms and the later ones 24ms each, the open area road test takes 17ms.
The numbers are a little high but can be optimized further.

Requirements
------------

[Colt](http://acs.lbl.gov/software/colt/) is is used for bitvector and intarray code in the A* for performance reasons. 

The test application uses [lwjgl](http://www.lwjgl.org/) for visualization.
