package Element;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;

public class Grid {
	ArrayList<Cell[]> grid;

	final int ROW = 25;
	final int COLUMN = 25;

	JPanel jGrid;
	JLabel jL[][];
	String tableHeader[] = new String[25];


	public Grid() {
		newJGrid();
		newGrid(ROW, COLUMN);;
	}


	public JPanel getJGrid(){
		return jGrid;
	}

	public void newJGrid(){
		jL = new JLabel[ROW+1][COLUMN+1];


		// Create Header[]
		for(int i = 0 ; i < 25 ; i++){
			char ch = (char)(65 + i);
			tableHeader[i] = String.valueOf(ch);
		}

		GridLayout gl = new GridLayout(26, 26);
		EtchedBorder eborder =  new EtchedBorder();
		jGrid = new JPanel(gl);
		for(int i = 0 ; i < ROW+1; i++){
			for(int j = 0; j < COLUMN+1; j++){
				JLabel label = new JLabel();
				if(i == 0 && j != 0)
					label.setText(tableHeader[j-1]);
				else if(j == 0 && i!= 0)
					label.setText(String.valueOf(i));

				label.setSize(10,10);
				label.setBorder(eborder);

				jL[i][j] = label;
				jGrid.add(jL[i][j]);
			}
		}
	}

	public void newGrid(int r, int c) {
		grid = new ArrayList<Cell[]>();
		for (int j = 0; j < r; j++) {
			Cell[] tempList = new Cell[c];
			for (int i = 0; i < c; i++) {
				tempList[i] = new Cell();
			}
			grid.add(tempList);
		}
	}


	public void setCellState(int x, int y, int state){
		grid.get(y)[x].setState(state);
		if(state == 2){
			jL[y][x].setText("O");
			jL[y][x].setBackground(Color.BLUE);
		}else if(state == 0){
			jL[y][x].setText("X");
			jL[y][x].setBackground(Color.RED);
		}
	}

	public void setCellName(int x, int y, String shipname){
		grid.get(y)[x].setShipName(shipname);
	}
	public String getCellName(int x, int y){
		return grid.get(y)[x].getShipName();
	}
	public int getCellState(int x, int y){
		int result = -1;
		try {
			result = grid.get(y)[x].getState();
		}catch (ArrayIndexOutOfBoundsException e){
	e.printStackTrace();
		}
		return result;
	}
	public ArrayList<Cell[]> getGrid(){ return grid; }

	public void setGrid(ArrayList<Cell[]> g){ this.grid = g; }
	public void delGrid() { grid = null; }
}
