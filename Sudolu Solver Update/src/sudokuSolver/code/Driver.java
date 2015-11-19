package sudokuSolver.code;

import sudokuSolver.gui.SudokuGUI;
import javax.swing.SwingUtilities;
/**
 * Driver class which creates a <code>SudokuGUI</code> and a <code>DataModel</code> and then links them together.
 * @author Zachary Moore
 *
 */
public class Driver {

	/**
	 * Creates a <code>SudokuGUI</code> and a <code>DataModel</code> and then links them together.
	 * @param args
	 */
	public static void main(String[] args) {
		DataModel dataModel = new sudokuSolver.code.DataModel();
		SudokuGUI gui = new SudokuGUI(dataModel);
		SwingUtilities.invokeLater(gui);
	}

}
