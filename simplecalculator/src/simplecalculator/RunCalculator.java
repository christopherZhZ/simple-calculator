package simplecalculator;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RunCalculator {
	
	JFrame frame;
	JTextField field;
	
	private static final Font BUTFT = new Font("Segoe UI", Font.PLAIN, 100);
	private static final Font SHOWFT = new Font("Segoe UI", Font.ITALIC, 116);
	private static final Color OPERCL = new Color(161, 192, 255);
	//private static final Color PUSHCL = new Color(253, 182, 75);
	
	private boolean empFlag = true;
	private boolean errFlag = false;
	private boolean operFlag = false;
	private boolean secVarNewDig = false;
	private String onScreen = "0";
	private String curOper = null;
	private int opCnt = 0;
	private int val1 = 0;
	private int val2 = 0;
	
	//private JButton colored_op = null;
	
	public static void main(String[] args) {
		RunCalculator gui = new RunCalculator();
		gui.setUpGui();
	}
	
	public void setUpGui() {
		frame = new JFrame("My Calculator");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		field = new JTextField(30);
		field.setFont(SHOWFT);
		field.setHorizontalAlignment(JTextField.RIGHT);
		field.setCaretColor(field.getBackground());
		field.setDocument(new CalculatorLimit(9));
		field.setText(onScreen);
		field.setEditable(false);
		
		JButton butAC = new JButton(), 
				butNeg = new JButton(), 
				butPerc = new JButton(),
				butAdd = new JButton(),
				butMinus = new JButton(),
				butMult = new JButton(), 
				butDiv = new JButton(),
				butEq = new JButton(),
				butDot = new JButton();
		setBut(butAC, "AC");
		butAC.addActionListener(new ACListener());
		setBut(butNeg, "¡À");
		setBut(butPerc, "%");
		setBut(butDot, ".");
		butNeg.addActionListener(new Op1VButListener());
		butPerc.addActionListener(new Op1VButListener());
		butDot.addActionListener(new Op1VButListener());
		setBut(butAdd, "+");
		setBut(butMinus, "-");
		setBut(butMult, "¡Á");
		setBut(butDiv, "¡Â");
		butAdd.addActionListener(new OpButListener());
		butMinus.addActionListener(new OpButListener());
		butMult.addActionListener(new OpButListener());
		butDiv.addActionListener(new OpButListener());
		setBut(butEq, "=");
		butEq.addActionListener(new EqButListener());
		
		
		ArrayList<JButton> numsBut = new ArrayList<JButton>();
		int i = 0;
		while (i <= 9) {
			numsBut.add(new JButton());
			JButton but = numsBut.get(i);
			setBut(but, String.valueOf(i));
			if (i == 0)
				but.addActionListener(new ZeroButListener());
			else 
				but.addActionListener(new NumButListener());
			++i;
		}	
		
		JPanel pan = new JPanel();
		Dimension panDim = new Dimension(1200, 1500);
		pan.setPreferredSize(panDim);
		pan.setMaximumSize(panDim);
		pan.add(butAC);
		pan.add(butNeg);
		//pan.add(butPerc);
		pan.add(butDiv);
		pan.add(numsBut.get(7));
		pan.add(numsBut.get(8));
		pan.add(numsBut.get(9));
		pan.add(butMult);
		pan.add(numsBut.get(4));
		pan.add(numsBut.get(5));
		pan.add(numsBut.get(6));
		pan.add(butMinus);
		pan.add(numsBut.get(1));
		pan.add(numsBut.get(2));
		pan.add(numsBut.get(3));
		pan.add(butAdd);
		pan.add(numsBut.get(0));
		//pan.add(butDot);
		pan.add(butEq);
		
		frame.getContentPane().add(BorderLayout.NORTH, field);
		frame.getContentPane().add(BorderLayout.CENTER, pan);
		frame.setSize(1300, 1800);
		frame.setVisible(true);
	}
	
	public static void setBut(JButton but, String item) {
		but.setText(item);
		but.setFont(BUTFT);
		if (item == "+" || item == "-" || item == "¡Á"
				|| item == "¡Â" || item == "=") {
			but.setBackground(OPERCL);
		}
		if (item.equals("0") || item.equals("=") || item.equals("AC"))
			but.setPreferredSize(new Dimension(600, 300));
		else
			but.setPreferredSize(new Dimension(300, 300));
	}
	
	class CalculatorLimit extends PlainDocument {
		private static final long serialVersionUID = 1L;
		
		private int limit;
		
		public CalculatorLimit(int limit) {
			super();
			this.limit = limit;
		}
		
		@Override public void insertString
		(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) return;
			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
	
	class ACListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			onScreen = "0";
			field.setText(onScreen);
			curOper = null;
			empFlag = true;
			operFlag = false;
			errFlag = false;
			secVarNewDig = false;
			opCnt = 0;
			val1 = 0;
			val2 = 0;
		}
	}
	
	class OpButListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (errFlag) return;
			
			String oper = ((JButton) e.getSource()).getActionCommand();
			
			if (operFlag && val2 == 0) {
				curOper = oper;
				return;
			}
			
			if (empFlag) {
				empFlag = false;
				operFlag = true;
				curOper = oper;
				return;
			}
			
			switch (oper) {
			case "+": case "-": case "¡Á": case "¡Â": 
				operFlag = true;
				secVarNewDig = false;
				++opCnt;
				if (opCnt > 1) {	// there are more than 2 operators
					boolean add = curOper.equals("+");
					boolean minus = curOper.equals("-");
					boolean mult = curOper.equals("¡Á");
					boolean div = curOper.equals("¡Â");
					if (add) val1 += val2;
					if (minus) val1 -= val2;
					if (mult) val1 *= val2;
					if (div) {
						if (val2 == 0) {
							field.setText("Error!");
							errFlag = true;
							break;
						}
						val1 /= val2;
					}
					onScreen = String.valueOf(val1);
					field.setText(onScreen);
					val2 = Integer.parseInt(onScreen);// put here??
				} else {
					val2 = val1;
				}
				curOper = oper;
				break;
			default:
				field.setText("Invalid!");
				errFlag = true;
				break;
			}
		}
	}

	class EqButListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (errFlag || empFlag) return;
			
			if (!empFlag && !operFlag) return;
			
			boolean add = curOper.equals("+");
			boolean minus = curOper.equals("-");
			boolean mult = curOper.equals("¡Á");
			boolean div = curOper.equals("¡Â");
			
			opCnt = 0;
			operFlag = false;
			curOper = null;
			secVarNewDig = false;
			
			if (add) val1 += val2;
			if (minus) val1 -= val2;
			if (mult) val1 *= val2;
			if (div) {
				if (val2 == 0) {
					onScreen = "Error";
					field.setText(onScreen);
					errFlag = true;
					return;
				}
				val1 /= val2;
			}
			onScreen = String.valueOf(val1);
			field.setText(onScreen);
			//p
		}
	}
	
	// 1 variable operator button listener (¡À)
	class Op1VButListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (errFlag) return;
			
			String oper = ((JButton) e.getSource()).getActionCommand();
			
			switch (oper) {
			case "%": case ".": 
				break;	// optional
			case "¡À":
				if (empFlag) break;
				int screen = Integer.parseInt(onScreen);
				onScreen = String.valueOf(-screen);
				field.setText(onScreen);
				if (val2 == screen) {
					val2 *= -1;
					//
				} else if (val1 == screen) {
					val1 *= -1;
				}
				break;
			default:
				field.setText("Invalid!");
				errFlag = true;
				break;
			}
		}
	}
	
	class NumButListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (errFlag) return;

			String input = ((JButton) e.getSource()).getActionCommand();
			int inval = Integer.parseInt(input);
			
			if (empFlag) {
				onScreen = input;
				field.setText(onScreen);
				val1 = inval;
				empFlag = false;
				return;
			}
			
			if (!operFlag && !empFlag) {	// append a new digit
				onScreen += input;
				field.setText(onScreen);
				val1 = Integer.parseInt(onScreen);
				return;
			}
			
			if (val2 == val1 && !secVarNewDig) {
				secVarNewDig = true;
				val2 = inval;
				onScreen = input;
				field.setText(onScreen);
				return;
			}
			
			//if (operFlag && val2 != val1) {
				onScreen += input;
				field.setText(onScreen);
				val2 = Integer.parseInt(onScreen);
				return;
			//}
		}
	}
	
	class ZeroButListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (errFlag || empFlag) return;
			
			int curONS = Integer.parseInt(onScreen);
			if (curONS == 0) return;
			
			if (!operFlag) {
				onScreen += "0";
				field.setText(onScreen);
				val1 = Integer.parseInt(onScreen);
				return;
			}
			
			if (operFlag && !secVarNewDig) {
				onScreen = "0";
				field.setText(onScreen);
				val2 = 0;
				return;
			}
			
			onScreen += "0";
			field.setText(onScreen);
			val2 = Integer.parseInt(onScreen);
		}
	}
	
}
