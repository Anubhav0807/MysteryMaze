package ui;

import static utilz.Constants.SizeConsts.SCALE;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class CustomButton extends JButton {
	
	public CustomButton(String label, int fontSize) {
		super(label);
		setFont(new Font("AriaLucida Console", Font.PLAIN, (int) (fontSize * SCALE)));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		setBackground(Color.gray);
		setForeground(Color.white);
		setFocusPainted(false); // Remove focus border
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(Color.lightGray);
				setForeground(Color.black);
				super.mouseEntered(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(Color.gray);
				setForeground(Color.white);
				super.mouseExited(e);
			}
		});
	}
}
