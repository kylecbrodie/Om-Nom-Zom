package game;

import game.Keys.Key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeyboardHandler implements KeyListener {

	private Map<Key, Integer> mappings = new HashMap<Key, Integer>();

	public KeyboardHandler(Keys keys) {
		// controls
		mappings.put(keys.up, KeyEvent.VK_W);
		mappings.put(keys.down, KeyEvent.VK_S);
		mappings.put(keys.left, KeyEvent.VK_A);
		mappings.put(keys.right, KeyEvent.VK_D);
		mappings.put(keys.sprint, KeyEvent.VK_SHIFT);

		// actions
		mappings.put(keys.attack, KeyEvent.VK_SPACE);
		mappings.put(keys.lookUp, KeyEvent.VK_UP);
		mappings.put(keys.lookDown, KeyEvent.VK_DOWN);
		mappings.put(keys.lookLeft, KeyEvent.VK_LEFT);
		mappings.put(keys.lookRight, KeyEvent.VK_RIGHT);
		
		//special
		mappings.put(keys.pause, KeyEvent.VK_ESCAPE);
		mappings.put(keys.screenShot, KeyEvent.VK_F2);
		mappings.put(keys.fullscreen, KeyEvent.VK_F11);
		mappings.put(keys.console, KeyEvent.VK_BACK_QUOTE);
	}
	
	public Integer getKeyEvent(Key key) {
		return mappings.get(key);
	}
	
	private void toggle(KeyEvent ke, boolean state) {
		Key key = null;
		Set<Key> keySet = mappings.keySet();
		for (Key _key : keySet) {
			if (mappings.get(_key) == ke.getKeyCode())
				key = _key;
		}
		if (key != null) {
			key.nextState = state;
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}

	@Override
	public void keyTyped(KeyEvent ke) {}
}