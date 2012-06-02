package game;

import java.util.ArrayList;
import java.util.List;

public class Keys {
	public final class Key {
		public final String name;
		public boolean nextState = false;
		public boolean wasDown = false;
		public boolean isDown = false;

		public Key(String name) {
			this.name = name;
			all.add(this);
		}

		public void tick() {
			wasDown = isDown;
			isDown = nextState;
		}

		public boolean wasPressed() {
			return !wasDown && isDown;
		}

		public boolean wasReleased() {
			return wasDown && !isDown;
		}

		public void release() {
			nextState = false;
		}
	}

	private List<Key> all = new ArrayList<Key>();

	public Key up = new Key("up");
	public Key down = new Key("down");
	public Key left = new Key("left");
	public Key right = new Key("right");
	
	public Key sprint = new Key("sprint");
	public Key attack = new Key("attack");
	
    public Key lookUp = new Key("lookUp");
    public Key lookDown = new Key("lookDown");
    public Key lookLeft = new Key("lookLeft");
    public Key lookRight = new Key("lookRight");
    
	public Key pause = new Key("pause");
	public Key fullscreen = new Key("fullscreen");
	public Key screenShot = new Key("screenShot");
	public Key console = new Key("console");
	
	public void tick() {
		for (Key key : all)
			key.tick();
	}

	public void release() {
		for (Key key : all)
			key.release();
	}

	public List<Key> getAll() {
		return all;
	}
	
	public void addKey(Key k) {
		all.add(k);
	}
	
	public void addKey(Key k, int i) {
		all.add(i, k);
	}
	
	public void removeKey(int i) {
		all.remove(i);
	}
	
	public void removeKey(Key k) {
		all.remove(k);
	}
}