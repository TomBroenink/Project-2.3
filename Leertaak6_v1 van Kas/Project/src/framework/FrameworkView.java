package framework;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.*;

public class FrameworkView 
{
	JFrame window;
	
	public FrameworkView(int height, int width, String title)
	{
		this.window = new JFrame(title);
		this.window.setSize(width, height);
		this.window.setLocationRelativeTo(null); //Center of the screen
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(false);
		this.window.setLayout(new GridBagLayout());
		this.window.setVisible(true);
	}
	
	public void add(Component component){
		this.window.add(component);
	}

	public void revalidate() {
		this.window.revalidate();
	}
	
	public void setLayout(LayoutManager lm){
		this.window.setLayout(lm);
	}
}
