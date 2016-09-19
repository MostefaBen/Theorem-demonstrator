package bennaceur.Mostefa.M2.LIA.TP.Incoherence;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

	class Interface extends JFrame implements ActionListener
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7732477970303360065L;
		private JToolBar barreOutils;
		private JButton B_d,B_r;	
		private ClausalHorn boite1;
		private Container contenu = getContentPane();
			
		public Interface()
		{
			
			setTitle("-: Démonstrateur du théorème"); 	
			
			Toolkit  tool=	getToolkit();//get size of screen
			int X=(int)(tool.getScreenSize().getWidth()-1005);
			setBounds(X, 0,1005,(int) tool.getScreenSize().getHeight()-38);
			setResizable(false);
			
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//if close alors exit

			JPanel panel = new panneau();
			panel.setLayout (null);
			contenu.add(panel);

			barreOutils = new JToolBar();
			contenu.add(barreOutils, "North");
			barreOutils.setVisible(true);
			
			B_d = new JButton ();
			B_d.setToolTipText("démonstration");
			B_d.addActionListener (this);
				
			B_r = new JButton ();
			B_r.setToolTipText("Afficher les résultats");
			B_r.addActionListener (this);

			// Ajout à la barre d'outils.
			barreOutils.add(B_d);
			barreOutils.add(B_r);
			
			boite1 = new ClausalHorn(0, 0, 350, (int) tool.getScreenSize().getHeight()-38, this);
				
			// Affichage de la fenêtre principale.
			setVisible(true);
			
		    boite1.setVoir(false);
		    boite1.setVoirRes(false);
		
			
			addWindowListener (new WindowAdapter()
					{	
			          public void windowClosing (WindowEvent e)
					  {
						 System.exit(0);
						
						}
					});			
		}


		public void actionPerformed (ActionEvent e)
		{
			Object source =	e.getSource();
			
				if (source == B_d)
				{
					
					boite1.setVoir(true);
					
				}
				if (source == B_r){
					
					boite1.setVoirRes(true);
				}	
				
		}
	}	
	class panneau extends JPanel
	{
		/**
		 * 
		 */
		private ImageIcon Ifond = new ImageIcon ("image/equation2.jpg");	
		private static final long serialVersionUID = 3756940188750902248L;
		Font largeFont = new Font("TimesRoman", Font.BOLD, 20);  
		

		public void paintComponent (Graphics g)
		{
			super.paintComponent(g);
			
			g.drawImage (Ifond.getImage(), 0, 0, null);
			g.setColor (Color.black);	
			g.drawRoundRect(70,25, 900, 600, 50, 50); 
			g.setFont(largeFont);
			g.setXORMode(Color.gray);
			
			g.drawString("-: Démonstrateur du théorème ".toUpperCase(), 150, 20);
			
		}
	}