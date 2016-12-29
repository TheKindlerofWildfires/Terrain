package graphics;

import maths.Vector3f;
import object.GameObject;
import particles.Particle;

/**
 * A handy box in which to put trees :)
 * @author Simon
 *
 */
public class TreeManager extends Instancer {

	/**
	 * Makes a new tree manager
	 * @param baseTree
	 * @param maxTrees
	 */
	public TreeManager(Particle baseTree, int maxTrees) {
		super(baseTree, maxTrees);
		this.active = true;
		baseObject.shader = ShaderManager.particleShader;
	}

	/**
	 * I dont think trees have to update?
	 * @param ellapsedTime time since last update tick
	 */
	public final void update(long ellapsedTime) {
		return;
	}

	/**
	 * adds a tree
	 * @param pos position of new tree
	 * @return true upon success, false if already full on trees
	 */
	public final boolean addTree(Vector3f pos) {
		if (objects.size() > maxObjects) {
			return false;
		} else {
			Particle newTree = new Particle(baseObject);
			newTree.placeAt(pos.x, pos.y, pos.z);
			objects.add(newTree);
			return true;
		}
	}
}
