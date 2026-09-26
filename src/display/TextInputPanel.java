package display;

import javax.swing.*;

public class TextInputPanel extends JPanel {
	private static final long serialVersionUID = 4715422469507146421L;
	
	private JTextField inputField;
	
	public TextInputPanel(String prompt, String txt) {
		JLabel promptPanel = new JLabel(prompt);
		add(promptPanel);
		
		inputField = new JTextField(10);
		inputField.setText(txt);
		add(inputField);
	}
	
	public String getText() {
		return inputField.getText();
	}
}
