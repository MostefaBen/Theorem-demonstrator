package bennaceur.Mostefa.M2.LIA.TP.Incoherence;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


class Result extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private pan_Result pan;
	private JScrollPane defil;
	private Dimension d;
	
	public Result(ArrayList<String> list) {
		
		setTitle("-: Les résultats");
		setBounds(45, 115, 400, 621);

		// Création du panneau.
		pan = new pan_Result(list);

		// Création de la barre de défilement.
		defil = new JScrollPane(pan);
		getContentPane().add(defil);
	}
	
	
	
	
	// Cette fonction permet de rendre visible ou non le JFrame.
	void setVoir(boolean B) {
		if (B) {
			setVisible(true);
		} else {
			setVisible(false);
		}
	}
	
	void setResult(ArrayList<String> list) {
		pan.setResult(list);
	}

}


class pan_Result extends JPanel
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static  ArrayList<String> list_r = new ArrayList<String>();

	public pan_Result(ArrayList<String> list) {
		list_r = list;
	}

	// Fonction appelée lors d'un rafraichissement.
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Fonction d'écriture.
		Ecrire(list_r, g);
	}

	public void setResult(ArrayList<String> list) {
		list_r = list;
	}
	
	
	void Ecrire(ArrayList<String> list, Graphics g) {
		int xligne = 30;
		int yligne = 20;

		g.setColor(Color.black);
		g.drawString(".: Les résultats :", xligne, yligne);
		xligne = xligne + 20;
		yligne = yligne + 20;

		g.setColor(Color.white);
		xligne = 50;

		g.drawString(
				"------------------------------------------------------------",
				xligne, yligne);
		yligne = yligne + 20;

		g.setColor(Color.black);

		if (list.size() != 0) {

			int num = 0;
			while (num < list.size()) {

				g.drawString(list.get(num), xligne, yligne);
				yligne = yligne + 30;

				num++;
			}

		} else {

			xligne = 70;

			g.setColor(Color.red);
			g.drawString("Aucun résultat !", xligne, yligne);
			yligne = yligne + 20;

		}

		// Mise à l'échelle de la JScrollPane.
		setPreferredSize(new Dimension(250, yligne));
		revalidate();

	}

}