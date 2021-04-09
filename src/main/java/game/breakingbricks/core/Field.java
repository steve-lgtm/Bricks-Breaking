package game.breakingbricks.core;

import java.util.Random;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final Tile[][] tiles;
    private final Tile[][] tilesRefresh;
    private GameState state = GameState.PLAYING;
    private int score;
    private int healthCount;
    private int breakColumns;

    public Field(int rowCount, int columnCount, int healthCount) {
        this.breakColumns = 0;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.healthCount = healthCount;
        tiles = new Tile[rowCount][columnCount];
        tilesRefresh = new Tile[rowCount][columnCount];
        generate();
    }

    public void generate() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j] == null) {
                    tiles[i][j] = new Fill();
                    tilesRefresh[i][j] = new Fill();
                }
                tiles[i][j].setColor(TileColor.values()[new Random().nextInt(TileColor.values().length)]);
                tiles[i][j].setState(TileState.COLORED);
                tilesRefresh[i][j].setState(TileState.COLORED);
            }
        }
    }

    public void refresh() {
        int breakCountT = 0;
        int breakCountTR = 0;
        //count braked columns in tilesRefresh
        for (int j = 0; j < columnCount; j++) {
            for (int s = 0; s < rowCount; s++) {
                if (tilesRefresh[s][j].getState() == TileState.BRAKED) {
                    breakCountTR++;
                }
            }
        }

        //setting tiles in tilesRefresh to colored
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tilesRefresh[i][j].setState(TileState.COLORED);
            }
        }

        //sort by columns
        for (int j = 0; j < columnCount; j++) {
            int f = rowCount - 1;
            for (int i = rowCount - 1; i >= 0; i--) {
                if (tiles[i][j].getState() == TileState.COLORED) {
                    tilesRefresh[f][j].setColor(tiles[i][j].getColor());
                    f--;
                }
            }
            for (int d = f; d >= 0; d--) {
                tilesRefresh[d][j].setState(TileState.BRAKED);
            }
        }

        //copy tilesRefresh to tiles
        for (int i = 0; i < rowCount; i++) {
            for (int s = 0; s < rowCount; s++) {
                tiles[s][i].setState(tilesRefresh[s][i].getState());
                tiles[s][i].setColor(tilesRefresh[s][i].getColor());
            }
        }

        //count braked columns in tiles
        for (int i = 0; i < columnCount; i++) {
            for (int s = 0; s < rowCount; s++) {
                if (tiles[s][i].getState() == TileState.BRAKED) {
                    breakCountT++;
                }
            }
        }

        //if is field clear generate new one
        if (breakCountT == rowCount * columnCount) {
            breakColumns = 0;
            generate();
        }

        //if select only one tile decrease health
        if (breakCountT - breakCountTR == 1) {
            healthCount--;
        }

        //sort by rows
        for (int j = 0; j < columnCount - breakColumns; j++) {
            int p = 0;
            for (int i = 0; i < rowCount; i++) {
                if (tiles[i][j].getState() == TileState.BRAKED) {
                    p++;
                }
            }
            if (p == rowCount) {
                for (int d = j; d < columnCount - 1; d++) {
                    for (int l = 0; l < rowCount; l++) {
                        tiles[l][d].setState(tiles[l][d + 1].getState());
                        tiles[l][d].setColor(tiles[l][d + 1].getColor());
                    }
                }
                j = 0;
                breakColumns++;
                for (int s = 0; s < rowCount; s++) {
                    tiles[s][columnCount - breakColumns].setState(TileState.BRAKED);
                }
            }
        }

        //sort by rows check
        for (int j = 0; j < columnCount - breakColumns; j++) {
            int p = 0;
            for (int i = 0; i < rowCount; i++) {
                if (tiles[i][j].getState() == TileState.BRAKED) {
                    p++;
                }
            }
            if (p == rowCount) {
                for (int d = j; d < columnCount - 1; d++) {
                    for (int l = 0; l < rowCount; l++) {
                        tiles[l][d].setState(tiles[l][d + 1].getState());
                        tiles[l][d].setColor(tiles[l][d + 1].getColor());
                    }
                }
            }
        }

        // score count
        score = score + 11 * (breakCountT - breakCountTR) + 2;
    }

    public void findSame(int row, int column) {
        if (row >= 0 && row < rowCount && column >= 0 && row < columnCount) {
            tiles[row][column].setState(TileState.BRAKED);
            if (row - 1 >= 0 && tiles[row - 1][column].getState() == TileState.COLORED) {
                if (tiles[row][column].getColor() == tiles[row - 1][column].getColor()) {
                    tiles[row - 1][column].setState(TileState.BRAKED);
                    findSame(row - 1, column);
                }
            }
            if (column - 1 >= 0 && tiles[row][column - 1].getState() == TileState.COLORED) {
                if (tiles[row][column].getColor() == tiles[row][column - 1].getColor()) {
                    tiles[row][column - 1].setState(TileState.BRAKED);
                    findSame(row, column - 1);
                }
            }
            if (row + 1 < rowCount && tiles[row + 1][column].getState() == TileState.COLORED) {
                if (tiles[row][column].getColor() == tiles[row + 1][column].getColor()) {
                    tiles[row + 1][column].setState(TileState.BRAKED);
                    findSame(row + 1, column);
                }
            }
            if (column + 1 < columnCount && tiles[row][column + 1].getState() == TileState.COLORED) {
                if (tiles[row][column].getColor() == tiles[row][column + 1].getColor()) {
                    tiles[row][column + 1].setState(TileState.BRAKED);
                    findSame(row, column + 1);
                }
            }
        }
    }

    public void selectTile(int row, int column) {
        if (tiles[row][column].getState() == TileState.COLORED) {
            findSame(row, column);
            refresh();
            if (healthCount == 0) {
                setState(GameState.FAILED);

            }
        } else {
            System.out.println("Already broken tile, choose another !");
        }
    }

    public int getScore() {
        return score;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getHealthCount() {
        return healthCount;
    }
}