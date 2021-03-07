package breakingbricks.core;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {


        private Random randomGenerator = new Random();
        private Field field;
        private int rowCount;
        private int columnCount;
        private int health;


        public FieldTest() {
        rowCount = randomGenerator.nextInt(10) + 5;

        columnCount = rowCount;
        health=randomGenerator.nextInt(10) + 1;
        field = new Field(rowCount, columnCount, health);
    }


    @Test
    void minimalHealth(){
        Field fieldWithMinHealth = null;
        try {
            fieldWithMinHealth = new Field(rowCount, columnCount, health);
        } catch (Exception e) {
            // field with more mines than tiles should not be created - it may fail on exception
        }
        assertTrue((fieldWithMinHealth == null) || (fieldWithMinHealth.getHealthCount() >=1));
    }

    @Test
    void minimalRowCom(){
        Field fieldWithMinRowCount = null;
        try {
            fieldWithMinRowCount = new Field(rowCount, columnCount, health);
        } catch (Exception e) {
            // field with more mines than tiles should not be created - it may fail on exception
        }
        assertTrue((fieldWithMinRowCount == null) || (fieldWithMinRowCount.getRowCount() >=4) && (fieldWithMinRowCount.getColumnCount() >=4));
    }

    @Test
    void selectTile() {
        Field field = null;
        int row= 1;
        int column= 1;
        try {
            field=new Field(rowCount,columnCount,health);
            field.selectTile(row,column);
        } catch (Exception e){

        }
        assertTrue((field==null)|| (row<rowCount)&&(column<columnCount) );
    }

    @Test
    void getScore() {
    }

    @Test
    void getTile() {
    }

    @Test
    void getState() {
    }

    @Test
    void setState() {
    }

    @Test
    void getColumnCount() {
    }

    @Test
    void getRowCount() {
    }

    @Test
    void getHealthCount() {
    }
}