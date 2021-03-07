package breakingbricks.core;

public abstract class Tile {
    private TileState state = TileState.COLORED;
    private TileColor color = TileColor.RED;

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public TileColor getColor() {
        return color;
    }

    public void setColor(TileColor color) {
        this.color = color;
    }

}
