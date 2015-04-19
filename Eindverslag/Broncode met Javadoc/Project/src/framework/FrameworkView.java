package framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.*;

/**
 * The Class FrameworkView.
 */
public class FrameworkView 
{
	
	/** The window. */
	public static JFrame window;
	
	/**
	 * Instantiates a new framework view.
	 *
	 * @param height the height
	 * @param width the width
	 * @param title the title
	 */
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
	
	/**
	 * Adds the.
	 *
	 * @param component the component
	 */
	public void add(Component component){
		this.window.add(component);
	}

	/**
	 * Revalidate.
	 */
	public void revalidate() {
		this.window.revalidate();
	}
	
	/**
	 * Sets the layout.
	 *
	 * @param lm the new layout
	 */
	public void setLayout(LayoutManager lm){
		this.window.setLayout(lm);
	}
}
