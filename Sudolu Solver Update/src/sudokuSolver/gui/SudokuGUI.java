package sudokuSolver.gui;

import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import sudokuSolver.code.DataModel;

/**
 * <code>SudokuGUI</code> creates a graphical user interface that represents a sudoku puzzle.
 * <p>
 * <code>SudokuGUI</code> has methods which are used to gather information by the GUI and send them to <code>DataModel</code> to be analyzed.
 * 
 * @author Zachary Moore 
 *
 */
public class SudokuGUI implements Runnable {

	/**
	 * Represents the DataModel to send GUI data to.
	 */
	private DataModel _model;

	/**
	 * 2-D array of JFormattedTextFields to limit user input and accept numbers.
	 */
	private JFormattedTextField[][] _grid;

	/**
	 * The GUI currently worked on to send to DataModel.
	 */
	private SudokuGUI _sudokuGUI;

	/**
	 * 2-D int array which represents the data within the GUI.
	 */
	private int[][] _representation;

	private int _gridSize;

	private int _actualSize;

	private JPanel[][] _panels;

	private JPanel _bigPanel;
	/**
	 * Initializes all instance variables.
	 * @param dataModel The dataModel to send information to
	 */
	public SudokuGUI (DataModel dataModel) {
		String message = JOptionPane.showInputDialog(null , "How big would you like your grid to be?\n Enter number to designate size\n ex. 14 = 14 x 14 grid");
		_actualSize = Integer.parseInt(message);
		_gridSize = (int) Math.sqrt(Integer.parseInt(message));
		_grid = new JFormattedTextField[_actualSize][_actualSize];
		_model = dataModel;
		_sudokuGUI = this;
		_representation = new int[_actualSize][_actualSize];
		_panels = new JPanel[_gridSize][_gridSize];
		_bigPanel = new JPanel();
	}



	/**
	 * Sets up the GUI for Sudoku. Uses <code>JFormattedTextField</code> in order to limit user's inputs to numbers.  
	 * Uses a switch statement to create borders. <code>BorderFactory</code> creates <code>MatteBorder</code> which is needed as a result of pixel issues with borders in <code>GridLayout</code>.
	 */
	@Override
	public void run() {
		JFrame window = new JFrame("Sudoku Game");
		_bigPanel.setLayout(new GridLayout( _gridSize+1, _gridSize,0,0));
		populatePanels();
		JButton solveButton = new JButton("Solve");
		solveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_sudokuGUI.getData();
				_model.setGUI(_sudokuGUI);
				if(_model.isDuplicatesAll(_representation)){
					JOptionPane.showMessageDialog(null , "Duplicates found in:\n\nRows\nColumns\nOr Squares", "Illegal Inputs" , JOptionPane.WARNING_MESSAGE);
				}
				else{	
					_model.solve(_representation,_sudokuGUI);
				}
			}
		});

		_bigPanel.add(solveButton);
		JLabel instructions = new JLabel("<HTML><body><H1>Instructions</H1><p>Input your sudoku numbers into the text fields" + "<br>Click the solve button to solve the given inputs!<p></body></HTML>");
		_bigPanel.add(instructions);
		window.add(_bigPanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	public void populatePanels(){
		for(int row = 0; row < _gridSize; row ++){
			for(int col = 0; col < _gridSize; col++){
				_panels[row][col] = new JPanel();
				_panels[row][col].setLayout(new GridLayout(_gridSize,_gridSize,0,0));
				_panels[row][col].setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.black));
				populateInnerPanels(row,col);
				_bigPanel.add(_panels[row][col]);
			}
		}
	}

	public void populateInnerPanels(int rowPos, int colPos){
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(_actualSize);
		formatter.setAllowsInvalid(false);

		for(int row = 0; row <_gridSize; row ++){
			for(int col = 0; col<_gridSize; col ++){
				JFormattedTextField numberTextField = new JFormattedTextField(formatter);
				numberTextField.setBorder(BorderFactory.createLineBorder(Color.black));
				numberTextField.addFocusListener(new FocusListener(){


					@Override
					public void focusGained(FocusEvent e) {
						numberTextField.setText("");
					}
					//FIX
					@Override
					public void focusLost(FocusEvent e) {

					}
				});
	
				_panels[rowPos][colPos].add(numberTextField);

			}
		}
	}

	/**
	 * Gathers numbers from the JFormattedTextFields in the GUI.
	 * @return A 2-D int array that represents the GUI
	 */
	public int[][] getData(){
		for(int row= 0; row < _gridSize;row++){
			for(int col = 0; col< _gridSize; col++){
				for(int i = 0; i < _actualSize; i ++){
					JFormattedTextField f = (JFormattedTextField) _panels[row][col].getComponent(i);
					String q = f.getText().trim();
					if(!q.isEmpty()){
						_representation[row+(i%3)][col+(i%3)] = Integer.parseInt(f.getText().trim());

					}
				}

			}

		}	

		return _representation;
	}	

	/**
	 * Sets the GUI to the given 2-D int array.
	 * @param updatedNumbers The 2-D int array to set the GUI to
	 */
	public void setGUI(int[][] updatedNumbers){

		for(int row = 0; row< _gridSize; row++){
			for(int col = 0; col <_gridSize; col++){
				for(int i = 0; i < _actualSize; i ++){
				JFormattedTextField f = (JFormattedTextField) _panels[row][col].getComponent(i);
				f.setText(Integer.toString(updatedNumbers[row][col]));
				}

			}
		}
	}

	public int getGridSize(){
		return _actualSize;
	}
}
