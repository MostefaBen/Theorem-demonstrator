package bennaceur.Mostefa.M2.LIA.TP.Incoherence;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



class ClausalHorn extends JFrame implements ActionListener
{
/**
	 * 
	 */
	private static final long serialVersionUID = 3099662141639179826L;
    private JDialog boite;
    private Result boite2;
    private Incoherence incoherence;
    private IncoherenceHorn  InHorn;
	private JTextField txt1;
	private JButton ok, ok2,ok3,ok4;
	private pan_moteur pan;
	private static String S;
	
	//automate (AFD) sous forme Tableau
	private static char[][] autoTab={
        {'a','b','c','X','Y','Z','(',')','O','E','p','q','r','s','f','g','h',',','N'},
		{'/','/','/','/','/','/','7','/','/','/','2','2','2','2','/','/','/','/','1'},
		{'/','/','/','/','/','/','/','/','/','/','2','2','2','2','/','/','/','/','/'},
		{'/','/','/','/','/','/','3','/','/','/','/','/','/','/','/','/','/','/','/'},
		{'4','4','4','4','4','4','/','/','/','/','/','/','/','/','5','5','5','/','/'},
		{'/','/','/','/','/','/','/','6','/','/','/','/','/','/','/','/','/','3','/'},
		{'/','/','/','/','/','/','3','/','/','/','/','/','/','/','/','/','/','/','/'},
		{'/','/','/','/','/','/','/','6','7','8','/','/','/','/','/','/','/','3','/'},
		{'/','/','/','/','/','/','/','/','/','/','2','2','2','2','/','/','/','/','1'},
		{'/','/','/','/','/','/','7','/','/','/','/','/','/','/','/','/','/','/','/'},
     };
	
	private static  ArrayList<String> list_Clause = new ArrayList<String>();//pour les clauses
	private static  ArrayList<String> list_result = new ArrayList<String>();//pour les resultates
	
	ClausalHorn(int x, int y, int lg, int ht, JFrame fen)
	{
		
		boite = new JDialog (fen, "-: Démonstrateur du théorème", false);
		boite.setBounds (x, y, lg, ht);
		
		pan = new pan_moteur();
		pan.setBackground (Color.darkGray);
		pan.setBounds (0, 0, lg, ht);
		pan.setLayout (null);
		boite.getContentPane().add (pan);		
		
		ok = new JButton ("Cheque");
		ok.setBounds (180, 80, 105, 25);
		pan.add (ok);
		ok.setEnabled (true);
		ok.addActionListener(this);					
		
		txt1 = new JTextField ("...", 20);
		txt1.setEditable (true);
		txt1.setBounds (180, 50, 130, 20);
		pan.add (txt1);
		
		ok2 = new JButton ("clausel ?");
		ok2.setBounds (180, 120, 105, 25);
		pan.add (ok2);
		ok2.setEnabled (false);
		ok2.addActionListener(this);	
		
		ok3 = new JButton ("Horn ?");
		ok3.setBounds (180, 160, 105, 25);
		pan.add (ok3);
		ok3.setEnabled (false);
		ok3.addActionListener(this);	
		
		ok4 = new JButton ("Incoherent ?");
		ok4.setBounds (180, 200, 105, 25);
		pan.add (ok4);
		ok4.setEnabled (false);
		ok4.addActionListener(this);	
		
		pan.updateUI();	
		
		// Création de la fenêtre.
		Toolkit  tool=	getToolkit();//get size of screen
		int X=(int)(tool.getScreenSize().getWidth()-1005);
		int Y=(int)(tool.getScreenSize().getHeight()-200);
		
		boite2 = new Result(list_result);
		boite2.setBounds(X,Y/2, 350, 400);	
		
	}
		
	public void actionPerformed (ActionEvent e)
	{
		
		S = txt1.getText().trim();
		
		if(S.length() == 0 || S.equals("..."))
			JOptionPane.showMessageDialog(this, "Entrez votre formule");	
		else{
			
			
			Object source =	e.getSource();
			
			boolean check = false;
			if (source == ok) {
				ok.setEnabled(false);

				list_result.clear();
				check = Check_Lexique(S);
				String c = "vrai";
				if (check == false)
					c = "faux";
				list_result.add("la formule est lexiqualement correct ? : "
						+ c);

				if (check == true) {
					check = Check_Syntaxe();
					c = "vrai";
					if (check == false){
						c = "faux";
						ok2.setEnabled(false);
						ok3.setEnabled(false);
						ok4.setEnabled(false);
					}else{
						
						ok2.setEnabled(true);
						ok3.setEnabled(true);
						ok4.setEnabled(true);
					}
					list_result.add("la formule  est syntaxiquement correct ? : "
							+ c);
					ok.setEnabled(true);
				}

			}
			
			
			if (source == ok2) {
				ok.setEnabled(true);
				ok2.setEnabled(false);

				check = Check_Syntaxe();
				if (check == true) {
					list_result.clear();
					String c = "vrai";

					list_result.add("la formule est sous  forme clausel ? : "
							+ c);
					
					list_result.add("liste de clause =");
					list_Clause.clear();
					ExtraitClause();// pour extracter les clauses
					int num = 0;
					while (num < list_Clause.size()) {

						list_result.add("             - "+list_Clause.get(num));

						num++;
					}
					
				}

			}
				
			
			
			if (source == ok3) {
				list_result.clear();
				ok3.setEnabled(false);
				check = Check_Syntaxe();
				if (check == true) {
					check = isHorne();

					String c = "vrai";
					if (check == false)
						c = "faux";

					list_result.add("la formule est sous form clause de Horn ? : "
									+ c);
				}
			}
			
			if (source == ok4) {
				
				list_result.clear();

				check = Check_Syntaxe();
				if (check == true) {
					
					list_Clause.clear();
					ExtraitClause();// pour extracter les clauses
					
					ok4.setEnabled(false);

					check = isHorne();
                    if(check==false){
                    	
                    	incoherence = new Incoherence(list_Clause);
                    	check = incoherence.checkIncoherence();
                    
                    	int num = 0;
    					while (num < incoherence.getList_r().size()) {

    						list_result.add(incoherence.getList_r().get(num));

    						num++;
    					}
                    
                    
                    }else{
                    	
                    	InHorn = new IncoherenceHorn(list_Clause);
                    	check = InHorn.checkIncoherenceHorn();
                    	
                    	int num = 0;
    					while (num < InHorn.getList_r().size()) {

    						list_result.add(InHorn.getList_r().get(num));

    						num++;
    					}
                    }
					

					String c = "vrai";
					if (check == false) {
						c = "faux ";

					}

					list_result.add("liste de clause = " + list_Clause);
					list_result.add(" l'ensemble de clauses est incoherent ? : "
							+ c);

				}

			}
			boite2.setResult(list_result);
		}	
		
	}	
///////////////////////////////////////////////////////////////////////////////////////////////////	

	// methode pour demontrer que la formule  est lexiqualment correct
	private boolean Check_Lexique(String S) {
		char charTmp='w';
		int numTmp = 0;
		boolean check = true;

		while (numTmp < S.length()) {
			charTmp = NextChars(numTmp, S);
			check = isExist(charTmp);

			if (check == false) {
				break;
			}

			numTmp++;
		}

		if(check==false)
			list_result.add("Erreur position : "+(numTmp+1)+"   -- charactere : "+charTmp);
		
		return check;
	}
		
	// methode pour demontrer que la formule ne contient que les termes et les
	// connecteurs et les symboles correcte
	private boolean isExist(char charTmp) {

		boolean check = false;
		char tab[] = { 'a', 'b', 'c', 'X', 'Y', 'Z', '(', ')', 'O', 'E', 'p',
				'q', 'r', 's', 'f', 'g', 'h', ',', 'N' };
		int numTmp = 0;

		while (numTmp < tab.length) {

			if (tab[numTmp] == charTmp) {

				check = true;
				break;
			} else
				numTmp++;

		}

		return check;
	}
				
/////////////////////////////////////////////////////////////////////////////////////////////////
		
	// methode pour demontrer que la formule est syntaxiqument correct
	private boolean Check_Syntaxe() {

		char charTmp = NextChars(0, S);// donner premier charactere
		boolean check = isStart(charTmp);

		return check;
	}

	// methode pour verifer est ce que la formule commence par une certaine
	// chars
	// si oui donc commence la verifaction du formule ,c-a-d la formule est sous
	// forme clausel ou
	// non a travers l' AFD
	private static boolean isStart(char charTmp) {

		char[] StartChars = { '(', 'p', 'q', 'r', 's', 'N' };
		int num = 0;
		boolean check = false;

		while (num < StartChars.length) {

			if (charTmp == StartChars[num]
					&& NextChars(S.length() - 1, S) == ')'
					&& checkArcs(S) == true) {
				check = true;
				break;
			}

			num++;
		}

		if (check == true)// garantie : le premier chars du chaine contient un
							// chars utile pour commence la verification
		{

			int StartC = colnChars(charTmp); // le num de colonne du chars

			if (StartC != -1) {

				int StartL = 1; // commence la verification de Deuxieme
								// ligne(0+1) de autoTab
				check = startPath(charTmp, StartC, StartL); // algorithme de
															// l'automate pour
															// verifier esq est
															// vrée ou non la
															// formule

			} else
				check = false;

		}else
		     list_result.add("Erreur :début de formule  different de [(,p,q,r,s,N] OU  manques des characteres ou des symboles ");
			
		return check;
	}
	
	// donne le chars suivant
	private static char NextChars(int numTmp, String copyS) {

		char charTmp = 'k'; // k : c-à-d la fin de chaine

		if (numTmp < copyS.length() && numTmp >= 0) {

			charTmp = copyS.charAt(numTmp);
		}

		return charTmp;
	}
		

	// méthode pour vérifier est ce que la formule contient le même nombre
	// des arcs ,c-a-d les arcs ouvre et les arcs fermer
	private static boolean checkArcs(String copyS) {

		int num = 0, numOpen = 0, numClose = 0;
		boolean check = false;

		while (num < copyS.length()) {

			if (NextChars(num, copyS) == '(') {
				numOpen++;
			} else if (NextChars(num, copyS) == ')') {
				numClose++;
			}
			num++;

		}
		if (numOpen == numClose)
			check = true;

		return check;
	}

	// methode pour retourne le num de colonne du chars dans autoTab
	private static int colnChars(char charTmp) {

		int i;
		for (i = 0; i < autoTab[0].length; i++) {
			if (charTmp == autoTab[0][i]) {
				break;
			}
		}

		if (i >= autoTab[0].length)// aucun assorti entre les chars
			i = -1;

		return i;// return le num de colonne de chars
	}
	

	// methode pour executer l'algorithme de verification
	private static boolean startPath(char chars, int StartC, int StartL) {

		boolean check = false, matche = true;
		int num = 1;// commnece la verification de deuxieme chars(.+a*)
		int etape = 1;// pour verifer tout les etaps effectues
		char charTmp;

		while (matche == true) {

			charTmp = NextChars(num, S);//maintenant donner le chars suivant
			System.out.print("\n charTmp= " + charTmp + "\n");

			if (charTmp != 'k') {// le reset du chaine(formule)

				boolean verf = true;
				int numNext = 0;

				char c = autoTab[StartL][StartC];
				Byte Bc = new Byte(c + "");
				if (c != '/')
					StartL = Bc.intValue() + 1; // +1 pour le decalage dans le
												// auToTab

				System.out.print("\n\n--StartL= " + StartL + "\n");

				while (verf == true) {
					char charsNext = compNextChars(StartL, numNext);// pour
																	// comparer
																	// les deux
																	// chars de
																	// autoTab
																	// et du
																	// chaine de
																	// formule

					if (charTmp == charsNext) {
						System.out.print("\n charsNext= " + charsNext);
						etape++;// etape: pour verification que cet chars est verifier ;

						num++;
						chars = charsNext;
						StartC = colnChars(charsNext);

						System.out.print("\n num= " + num + " |chars= " + chars
								+ " |StartC= " + StartC);
						verf = false; //pour arrêter la boucle

					} else {

						if (charsNext == 'k') {// la chaine de formule est faux

							verf = false;
							matche = false;
							check = false;
							
							list_result.add("Erreur position :  "+(num+1)+"   -- charactere : "+charTmp);

						} else {

							numNext = colnChars(charsNext) + 1;// pour donner le
																// chars suivant
						}

					}
				}

			} else {// charTmp=k :stop (la fin du chaine)

				matche = false;
			}
		}

		if (etape == S.length())// vérifier si tout les étapes sont effectuent
			check = true;

		return check;
	}

	// cheque de chars suivant
	private static char compNextChars(int StartL, int numNext) {

		char charTmp = 'k';

		for (int j = numNext; j < autoTab[1].length; j++) {

			if (autoTab[StartL][j] != '/') {

				charTmp = autoTab[0][j];
				break;
			}

		}

		return charTmp;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////	

	// methode pour verifier est ce que la formule est une clause de horne ou non
	private static boolean isHorne() {
		boolean check = false, match = true;
		int indEnd = 0;
		String sub = null, copyS = S;

		while (match == true) {
			indEnd = indexofET(copyS);// pour retourner la position de chars (ET) logique

			System.out.print("\n indEnd =" + indEnd);

			sub = giveClause(indEnd, copyS);// pour retourner a chaque fois un
											// nouveau clause

			System.out.print("\n\nclause =" + sub + "\n");

			check = checkSubString(sub);// pour verifier est ce que la clause retourné
										// est clause de horn ou non
										// si oui alors continue sinon
										// retourne false

			System.out.print("\ncheck =" + check + "\n");

			if (check == true) {

				if (indEnd != copyS.length()) {

					copyS = copyS.substring(indEnd + 1);// +1 pour echapper de
														// chars (ET)

				} else {

					match = false;// pour arrêter la boucle
				}

			} else {

				match = false;
			}

		}

		return check;
	}

	// methode pour donner la position de chaque (ET) logique
	private static int indexofET(String copyS) {

		int indEnd;
		indEnd = copyS.indexOf('E');

		if (indEnd == -1) {

			indEnd = copyS.length();

		}

		return indEnd;
	}
		
	// méthode pour donner tous les clauses
	private static String giveClause(int indEnd, String copyS) {

		int indBeg = 0;// toujour commence de premier chars
		String sub;

		sub = copyS.substring(indBeg, indEnd);

		return sub;
	}
		
	// methode pour verifier est ce que le sous-String est sous forme Horn ou non
	private static boolean checkSubString(String sub) {

		int num = 0, count = 0, countN = 0;
		boolean check = false;

		while (num < sub.length()) {

			if (NextChars(num, sub) == 'p' || NextChars(num, sub) == 'q'
					|| NextChars(num, sub) == 'r' || NextChars(num, sub) == 's') {

				count++;

			} else if (NextChars(num, sub) == 'N') {

				countN++;

			}

			num++;
		}

		if (((count - countN) == 0) || ((count - countN) == 1)) {
			check = true;
		}

		return check;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//méthode pour extraire et ajouter les clause dans une liste
	private static void ExtraitClause() {

		String copyS = S, clause;
		int indEnd = 0;
		boolean check = true;

		while (check == true && copyS.length() != 0) {

			indEnd = indexofET(copyS);// retourner la position de chars (ET) logique

			clause = giveClause(indEnd, copyS);// retourner une clause

			if (NextChars(0, clause) == '(') {

				clause = clause.substring(1, clause.length() - 1);// supprimer
																	// les
																	// arcs
																	// au debut
																	// et au fin
			}

			list_Clause.add(clause);// ajouter une clause

			if (indEnd != copyS.length()) {// c-a-d index est dans la fin du
											// chain ou non
				copyS = copyS.substring(indEnd + 1);// +1 pour echapper le chars
													// (ET) logique

			} else {

				check = false;
			}

		}

		//list_result.add("liste de clause = " + list_Clause);
		System.out.print(list_Clause);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	void setVoir(boolean B) {
		if (B) {
			boite.setVisible(true);
		} else {
			boite.setVisible(false);
		}
	}



	// methode pour afficher le result
	void setVoirRes(boolean B) {
		if (B) {
			boite2.setVoir(true);
		} else {
			boite2.setVoir(false);
		}
	}

}

class pan_moteur extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon Ifond = new ImageIcon("image/equation.jpg");

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(Ifond.getImage(), 0, 0, null);

		g.setColor(Color.white);
		g.drawString("  -:enter la formule ".toUpperCase(), 10, 30);
		g.setColor(Color.red);
		g.drawLine(10, 40, 320, 40);

		g.setColor(Color.lightGray);
		g.drawRoundRect(10, 10, 320, 240, 50, 50);
		g.setColor(Color.white);
		g.drawString("-: formule :".toUpperCase(), 15, 60);

		g.drawString("instruction:".toUpperCase(), 20, 300);
		g.drawString("SVP entre seulment une formule de symbole exist au :",
				20, 330);
		g.drawString("X,Y,Z : variable", 20, 350);
		g.drawString("a,b,c : constant", 20, 370);
		g.drawString("f,g,h : symbole de forme fonctionnel", 20, 390);
		g.drawString("p,q,r,s : symbole de forme prédicatif", 20, 410);
		g.drawString("O :   OU logique", 20, 430);
		g.drawString("E :   ET logique", 20, 450);
		g.drawString("N :   Non logique", 20, 470);
		g.drawString("autre symbole  : (  )  ,", 20, 490);

	}
}