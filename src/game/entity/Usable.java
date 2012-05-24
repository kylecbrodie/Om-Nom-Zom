package game.entity;

public interface Usable {

	public void use(Entity user);

	public boolean upgrade(Player player);

	public void setHighlighted(boolean hl);

	public boolean isHighlightable();

	public boolean isAllowedToCancel();
}
