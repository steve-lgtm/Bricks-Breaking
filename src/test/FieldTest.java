package test;

import game.breakingbricks.core.Field;
import game.breakingbricks.core.GameState;
import game.breakingbricks.core.TileState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldTest {

    public FieldTest() {
    }

    @Test
    public void selectTileTest() {
        Field field;
        field = new Field(9, 9, 5);
        field.generate();
        field.selectTile(0, 0);
        assertTrue(field.getScore() > 0);
    }

    @Test
    public void selectTileTest5() {
        Field field;
        int count = 0;
        field = new Field(9, 9, 5);
        field.generate();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field.getTile(i, j).getState() == TileState.COLORED) {
                    field.selectTile(i, j);
                    if (field.getTile(i, j).getState() == TileState.BRAKED)
                        count++;
                    if (count == 5)
                        break;
                }
            }
            if (count == 5)
                break;
        }
        assertTrue(field.getScore() >= 65);
    }

    @Test
    public void selectBrokenTileTest() {
        Field field;
        field = new Field(9, 9, 5);
        field.generate();
        field.findSame(1, 1);
        field.selectTile(1, 1);
        assertEquals(field.getTile(1, 1).getState(), TileState.BRAKED);
    }

    @Test
    public void minHealthGameOverTest() {
        Field field;
        int count = 0;
        field = new Field(9, 9, 5);
        field.generate();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field.getTile(i, j).getState() == TileState.COLORED) {
                    field.selectTile(i, j);
                    if (field.getTile(i, j).getState() == TileState.BRAKED)
                        count++;
                    if (field.getHealthCount() == 0)
                        break;
                }
            }
            if (field.getHealthCount() == 0)
                break;
        }
        assertEquals(field.getState(), GameState.FAILED);
    }
}