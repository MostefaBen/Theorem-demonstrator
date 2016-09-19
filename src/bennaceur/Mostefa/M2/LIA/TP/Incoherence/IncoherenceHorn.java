package bennaceur.Mostefa.M2.LIA.TP.Incoherence;


import java.util.ArrayList;
import java.util.Iterator;


//Algorithme de l'incoherence Horn
//algorithme specifique utilisé seulement quand toutes les clauses sont des clauses de Horn
public class IncoherenceHorn {

	private static  ArrayList<String> list_Clause = new ArrayList<String>();//pour les nouveaux clauses 
	private static  ArrayList<LEquation> list_term= new ArrayList<LEquation>();//pour les equations entre les termes
	private static  ArrayList<String> list_r = new ArrayList<String>();//pour collecter tous les resultates dans une liste
	private static String  TwoClauseReverse[] = new String[2] ;//pour les clause inverse (i.e p et Np)
	private static int secondpredclause,posvergulterm1;
	    
	
	//constructeur 
	public IncoherenceHorn(ArrayList<String> list_C) {

		list_Clause = list_C;

	}
	

	public ArrayList<String> getList_r() {
		return list_r;
	}
	
	
	// methode pour appliquer l'algorithme de incoherence 
	//remplacer E par (E U{r} ) ; la clause est deja supprimer dans la methode  ResolutionHorn()
	//si la nouvelle clause {r} est vide alors retourne incoherent (vrai) sinon retourne coherent(faux)  
	public boolean checkIncoherenceHorn() {

		boolean check = false;// false c-a-d coherent
		String clausResolutionHorn;
		list_r.clear();

		while (true) {

			clausResolutionHorn = ResolutionHorn();// pour la clause de
													// l'algoriteme de
													// Resolution
			System.out.println("\n\n ResolutionHorn= " + clausResolutionHorn);

			if (clausResolutionHorn == "VIDE") {// VIDE :c-a-d retourn (vide)
												// via l'algoriteme de
												// Resolution
				check = true;
				break;
			} else if (clausResolutionHorn == "k") {
				check = false;
				break;

			} else {

				list_Clause.add(clausResolutionHorn);
			}

			System.out.println("list_clause = " + list_Clause + "\n");
		}

		System.out.println("\nlist_clause = " + list_Clause);
		System.out.println("\n\n list_clause.size = " + list_Clause.size());
		return check;
	}
			
	
	//methode pour :
	//choisie deux clauses sous forme clause de Horn(clause p et clause c).
	//calculer la nouvelle clause r .
	//supprimer la clause {c}
	private String ResolutionHorn() {
		String clause = "VIDE";
		boolean check = false;
		boolean match = false;

		System.out.println("\n\n**************ResolutionHorn*************\n");

		while (match == false) {

			TwoClauseReverse[0] = null;
			TwoClauseReverse[1] = null;
			check = getTwoClause(); // pour retourner deux clauses inversé(i.e
									// p() et Np()Vq() )

			System.out.print("\n getTwoClause= " + check);
			System.out.print("\n TwoClauseReverse[0]= " + TwoClauseReverse[0]);
			System.out.print("\n TwoClauseReverse[1]= " + TwoClauseReverse[1]);

			if (check == true) {// exist deux clause inversé(negative et
								// positive)

				list_r.add("Clause N°1= " + TwoClauseReverse[0]
						+ "     Clause N°2= " + TwoClauseReverse[1]);

				String firstClause = TwoClauseReverse[0];
				String secondClause = ClauseUnit(TwoClauseReverse[1],
						secondpredclause);

				System.out.println("\n secondpredclause= " + secondpredclause);

				System.out.println("\n  firstClause = " + firstClause
						+ " secondClause = " + secondClause);

				posvergulterm1 = 0;
				list_term.clear();
				ExtraitEquaClause(firstClause, secondClause);// extraire les
																// equations
																// entre les
																// termes

				check = isAccepctExtrait();

				System.out.println("\n ischeck = " + check);

				if (check == false) {

					clause = "k";
					break;
				}

				list_r.add("Predicat Atomique N°1= " + firstClause
						+ "     Predicat Atomique N°2= " + secondClause);

				String t = "S=[ ";
				Iterator<LEquation> lter = list_term.iterator();
				while (lter.hasNext()) {
					LEquation lterm = (LEquation) lter.next();
					System.out.println("\n getTerm1= " + lterm.getTerm1()
							+ " getTerm2= " + lterm.getTerm2());
					t = t + lterm.getTerm1() + "  =  " + lterm.getTerm2()
							+ " ,";

				}

				t = t + " ]";
				list_r.add(t);

				check = AlgUnification();// appliquer l'algoriteme de
											// l'unification
				System.out.println("\n unification= " + check);

				String c = "vrai";
				if (check == false)
					c = "faux";
				list_r.add("Unification = " + c);

				t = "{ ";
				Iterator<LEquation> lter2 = list_term.iterator();
				while (lter2.hasNext()) {
					LEquation lterm = (LEquation) lter2.next();
					System.out.println("\n getTerm1= " + lterm.getTerm1()
							+ " getTerm2= " + lterm.getTerm2());
					t = t + lterm.getTerm1() + "  /  " + lterm.getTerm2()
							+ " , ";
				}
				t = t + " }";

				list_r.add("ơ=" + t);

				if (check == false) {
					clause = "k";
					break;
				}

				String secondD = getDclause(secondClause, TwoClauseReverse[1]);

				if (secondD != "k") {

					System.out.print("\n\n newClause avant=" + secondD);
					list_r.add("{" + TwoClauseReverse[0] + " , "
							+ TwoClauseReverse[1] + "} |= ơ(" + secondD + ")");

					clause = applySebt(secondD);

					System.out.print("\n\n newClause apres=" + clause);

					list_r.add("la resolvant calculé a partir de {C1,C2} est :");
					list_r.add("ơ(D)=" + clause);

					match = true;

				} else {// secondD =="k"

					list_r.add("la resolvant calculé a partir de {C1,C2} est :");
					list_r.add("ơ(D)== VIDE");

					clause = "VIDE";
					match = true;

				}

			} else {// aucun deux clauses inversé alors arreter la boucle
				match = true;
				clause = "k";
			}

		}// fin TQ

		System.out.print("\n\n NClause=" + clause);
		return clause;
	}
	
	// méthode pour appliquer la substitution
	private static String applySebt(String newClause) {
		int numD = 0;
		String strTmp = "";
		char charTmp;

		while (numD < newClause.length()) {

			charTmp = newClause.charAt(numD);

			if ((charTmp != 'X') && (charTmp != 'Y') && (charTmp != 'Z')) {
				strTmp = strTmp + charTmp;
			} else {
				int numTmp = 0;
				while (numTmp < list_term.size()) {

					if (charTmp == NextChars(0, list_term.get(numTmp)
							.getTerm1())) {

						strTmp = strTmp + list_term.get(numTmp).getTerm2();
						break;
					}
					numTmp++;
				}

				if (numTmp >= list_term.size())
					strTmp = strTmp + charTmp;

			}
			numD++;
		}
		return strTmp;
	}
	
	// méthode spécial pour méthode de résolution pour retourner la  clause D = {c}-(Np)
	private static String getDclause(String DClause, String clause) {

		int posClause = clause.indexOf(DClause);
		int pos;
		pos = clause.indexOf('O');

		if (pos != -1) {

			if (pos < posClause) {

				clause = clause.substring(0, pos);

			} else {

				clause = clause.substring(pos + 1);
			}

		} else
			clause = "k";

		return clause;
	}


	//methode pour retourner les deux clauses p et c , p clause unitaire positive et
	//c clause est une clause contenant Np 
	private boolean getTwoClause() {
		boolean check = false;
		int num = 0, firstPred = 0, SecondPred = 0;

		while (num < list_Clause.size()) {

			String clause_p = getClause(num);
			if (clause_p == "k")
				break;

			int pos = clause_p.indexOf('O');

			if (pos == -1 && NextChars(0, clause_p) != 'N') {// ie:
																// clause_p=p(...)

				firstPred = givePred(clause_p, firstPred);
				System.out.print("\n firstPred= " + firstPred + " clause_p= "
						+ clause_p);

				if (firstPred == -1)// exit :c-a-d n'exist pas un symbole
									// predicatif
					break;

				int numTmp = 0;
				while (numTmp < list_Clause.size()) {

					String clause_c = getClause(numTmp);
					System.out.print("\n  clause_c= " + clause_c + " numTmp= "
							+ numTmp);

					if (clause_c == "k")
						break;

					if (numTmp != num) {
						SecondPred = 0;
						SecondPred = givePred(clause_c, SecondPred);

						if (SecondPred == -1)// exit :c-a-d n'existe pas un
												// symbole prédicatif
							break;

						while (SecondPred < clause_c.length()) {

							if ((NextChars(firstPred, clause_p) == NextChars(
									SecondPred, clause_c))
									&& NextChars((SecondPred - 1), clause_c) == 'N') {

								TwoClauseReverse[0] = clause_p;

								TwoClauseReverse[1] = clause_c;
								secondpredclause = SecondPred;

								list_Clause.remove(numTmp);
								check = true;
								break;
							}

							SecondPred = givePred(clause_c, SecondPred + 1);
							if (SecondPred == -1)
								break;
						}// Fin TQ

						System.out.print(" SecondPred= " + SecondPred + "\n");
						if (check == true)
							break;

					}// Fin if

					numTmp++;
				}// Fin TQ

			}// Fin if

			if (check == true)
				break;

			num++;
		}// Fin TQ

		return check;
	}
		
		
	// methode pour donner une clause chez la position numclause
	private static String getClause(int numclause) {

		String clause = "K";// k: c-a-d n'exist pas

		if (numclause < list_Clause.size())
			clause = list_Clause.get(numclause);

		return clause;
	}

	// donner le chars suivant
	private static char NextChars(int numTmp, String copyS) {

		char charTmp = 'k'; // k : c-à-d la fin de chaine

		if (numTmp < copyS.length() && numTmp >= 0) {

			charTmp = copyS.charAt(numTmp);
		}

		return charTmp;
	}
	
	// methode pour donner des caractère predicatif qui apparu après la position
	// numero donner en parametre
	private static int givePred(String clause, int num) {

		int pos = -1;
		char chars;
		while (num < clause.length() && num >= 0) {

			chars = NextChars(num, clause);
			if (chars == 'p' || chars == 'q' || chars == 'r' || chars == 's') {

				pos = num;
				break;
			}

			num++;
		}

		return pos;
	}
		
		
	// méthode pour extraire une clause unitaire
	private static String ClauseUnit(String firstClause, int numpredclause) {

		int pos = firstClause.indexOf('O');

		if (pos != -1) {

			if (pos < numpredclause) {

				firstClause = firstClause.substring(numpredclause);

				int pos2 = firstClause.indexOf('O');

				if (pos2 != -1)
					firstClause = firstClause.substring(0, pos2);

			} else {

				firstClause = firstClause.substring(numpredclause, pos);
			}

		}

		if (NextChars(0, firstClause) == 'N')
			firstClause = firstClause.substring(1);

		return firstClause;
	}
	
	// méthode pour extraire tous les équations entre les termes
	private static void ExtraitEquaClause(String firstClause,
			String secondClause) {

		System.out.print(" \n\n ExtraitEquaClause \n");
		while (true) {

			System.out.print("\n\n 1firstClause  unit =" + firstClause
					+ "\n 1secondClause unit =" + secondClause
					+ "\n posvergulterm1= " + posvergulterm1);

			if (firstClause.length() == 0 || secondClause.length() == 0
					|| posvergulterm1 == -1)
				break;

			String term1 = giveTerm(firstClause);

			System.out.print("\n\n posvergulterm1 = " + posvergulterm1);

			if (posvergulterm1 != -1 && term1.length() != 0) {
				firstClause = firstClause.substring(posvergulterm1 + 1);
			}

			String term2 = giveTerm(secondClause);

			if (posvergulterm1 != -1 && term2.length() != 0) {
				secondClause = secondClause.substring(posvergulterm1 + 1);

			}

			System.out.print("\n\n term1= " + term1 + "  term2= " + term2);

			LEquation lterm = new LEquation(term1, term2);

			list_term.add(lterm);

		}
		System.out.print(" \n\n fin ExtraitEquaClause \n");
	}

	// methode pour retourner une terme
	private static String giveTerm(String Clauseunit) {

		String term = Clauseunit;
		int beg;
		System.out.print("\n\n Clauseunit= " + Clauseunit);
		int posVergclause1 = Clauseunit.indexOf(',');

		if (posVergclause1 == -1) {

			beg = 2;
			if (NextChars(0, Clauseunit) != 'p'
					&& NextChars(0, Clauseunit) != 'q'
					&& NextChars(0, Clauseunit) != 'r'
					&& NextChars(0, Clauseunit) != 's')
				beg = 0;

			term = Clauseunit.substring(beg, Clauseunit.length() - 1);

		} else {// posVergclause1 != -1

			beg = 0;
			while (posVergclause1 < Clauseunit.length()) {

				if (NextChars(0, Clauseunit) == 'p'
						|| NextChars(0, Clauseunit) == 'q'
						|| NextChars(0, Clauseunit) == 'r'
						|| NextChars(0, Clauseunit) == 's')
					beg = 2;

				term = Clauseunit.substring(beg, posVergclause1);

				if (checkArcs(term) == true || term.equals("X")
						|| term.equals("Y") || term.equals("Z")) {
					break;
				}

				posVergclause1++;
			}
		}
		System.out.print("\n\n term= " + term);
		posvergulterm1 = posVergclause1;
		return term;
	}
		
		//méthode pour vérifier est ce que les deux clauses accept extraire des termes
		private static boolean isAccepctExtrait() {
				boolean check =true;
				int num=0;
				String term1 ,term2;
				char chars1,chars2;
				
				while(num < list_term.size()){
					
					 term1 = list_term.get(num).getTerm1();
					 term2 = list_term.get(num).getTerm2();
					
					 chars1 =NextChars(0,term1 );
					 chars2 =NextChars(0,term2 );
					
					
					if(  ((chars1=='X' || chars1 =='Y' || chars1=='Z')||(chars1=='f' || chars1 =='g' || chars1=='h' )|| (chars1=='a' || chars1 =='b' || chars1=='c')) && ((chars2=='X' || chars2 =='Y' || chars2=='Z' )||(chars2=='f' || chars2 =='g' || chars2=='h' )||(chars2=='a' || chars2 =='b' || chars2=='c' ))  ){
						
						if( ((chars1=='X' || chars1 =='Y' || chars1=='Z')&&(term1.length()==1)) && ((chars2=='X' || chars2 =='Y' || chars2=='Z')&& (term2.length()==1)) ){
							num++;
						}else if( ((chars1=='X' || chars1 =='Y' || chars1=='Z')&&(term1.length()==1)) && ((chars2=='f' || chars2 =='g' || chars2=='h') && (checkArcs(term2)==true)) ){
						    num++;
						}else if( ((chars1=='f' || chars1 =='g' || chars1=='h' )&&(checkArcs(term1)==true)) && ((chars2=='f' || chars2 =='g' || chars2=='h') && (checkArcs(term2)==true)) ){
							num++;
						}else if( ((chars1=='f' || chars1 =='g' || chars1=='h' )&&(checkArcs(term1)==true)) && ((chars2=='X' || chars2 =='Y' || chars2=='Z')&& (term2.length()==1)) ){
						    num++;
						}else if( ((chars1=='a' || chars1 =='b' || chars1=='c')&&(term1.length()==1)) && ((chars2=='a' || chars2 =='b' || chars2=='c' )&& (term2.length()==1)) ){
						    num++;
						}else if( ((chars1=='a' || chars1 =='b' || chars1=='c')&&(term1.length()==1)) && ((chars2=='X' || chars2 =='Y' || chars2=='Z')&& (term2.length()==1)) ){
							num++;
						}else if( ((chars1=='a' || chars1 =='b' || chars1=='c')&&(term1.length()==1)) && ((chars2=='f' || chars2 =='g' || chars2=='h') && (checkArcs(term2)==true))  ){
							num++;
						}else if( ((chars1=='X' || chars1 =='Y' || chars1=='Z')&&(term1.length()==1)) && ((chars2=='a' || chars2 =='b' || chars2=='c' )&& (term2.length()==1)) ){
							num++;
						}else if( ((chars1=='f' || chars1 =='g' || chars1=='h' )&&(checkArcs(term1)==true)) && ((chars2=='a' || chars2 =='b' || chars2=='c' )&& (term2.length()==1)) ){	
							num++;
						}else{
							check=false;
						    break;
						   }
					
					}else{
						check=false;
					    break;
					   }
				}
				return check;
			}
		
	// methode pour verifier le nombre des arcs '(' et ')' dans une clause 
	private static boolean checkArcs(String copyS) {

		int num = 0, n = 0;
		boolean check = false;

		while (num < copyS.length()) {

			if (NextChars(num, copyS) == '(') {
				n++;
			} else if (NextChars(num, copyS) == ')') {
				n--;
			}
			num++;

		}
		if (n == 0)
			check = true;

		return check;
	}
		
	// methode pour appliquer l'algorithme de l'unification
	private static boolean AlgUnification() {

		int numTerm = 0;
		boolean check = false;
		String term1, term2;
		char chars1, chars2;

		while (check == false) {// TQ le system n'est pas resolu

			System.out.println("\n numTerm = " + numTerm);
			term1 = list_term.get(numTerm).getTerm1();
			term2 = list_term.get(numTerm).getTerm2();

			chars1 = NextChars(0, term1);
			chars2 = NextChars(0, term2);

			System.out.println("\n 1unif term1 =" + term1 + " term2= " + term2);

			if (term1.equals(term2)) {// X=X

				list_term.remove(numTerm);
				// systeme resolu et le cas de p(X)E(Np(X)) -> X=X -> suprimer X
				// -> []
				if (list_term.size() == 0) {
					check = true;
					break;
				}

			} else if ((chars1 == 'f' || chars1 == 'g' || chars1 == 'h')
					&& (chars2 == 'f' || chars2 == 'g' || chars2 == 'h')
					&& (chars1 != chars2)) {// f!=g

				check = false;
				break;

			} else if ((chars1 == 'f' || chars1 == 'g' || chars1 == 'h')
					&& (chars2 == 'f' || chars2 == 'g' || chars2 == 'h')
					&& (chars1 == chars2)) {// f=f

				list_term.remove(numTerm);

				term1 = "p" + term1.substring(1);
				term2 = "p" + term2.substring(1);
				posvergulterm1 = 0;
				ExtraitEquaClause(term1, term2);// extraire les equations entre
												// les termes
				check = isAccepctExtrait();
				System.out.println("\n isAccepctExtrait = " + check);
				if (check == false) 
					break;
				
				numTerm = list_term.size();// pour repeter la boucle et verifier
											// de premier terme

			} else if (chars1 != 'X' && chars1 != 'Y' && chars1 != 'Z') {// t=X

				String exchange = list_term.get(numTerm).getTerm1();
				list_term.get(numTerm).setTerm1(
						list_term.get(numTerm).getTerm2());
				list_term.get(numTerm).setTerm2(exchange);

				term1 = list_term.get(numTerm).getTerm1();
				term2 = list_term.get(numTerm).getTerm2();
				if (checkAppart(term1, term2) == true) {
					check = false;
					break;
				}

			} else if (checkAppart(term1, term2) == true) {// X=t et X
															// appartient dans t

				check = false;
				break;

			} else if (checkAppart(term1, term2) == false) {// X=t et X
															// n'appartient pas
															// dans t

				remplaceTerm(term1, term2, numTerm);
			}

			numTerm++;

			if (numTerm >= list_term.size())
				numTerm = 0;


			check = SystemResol();

			System.out.println(" SystemResol check = " + check);

			if (check == true) {

				boolean match = SeresheSamefirstTerm();
				if (match == true) {
					check = false;
					break;
				}

			}

			String t = "S=[ ";
			Iterator<LEquation> lter = list_term.iterator();
			while (lter.hasNext()) {
				LEquation lterm = (LEquation) lter.next();
				t = t + lterm.getTerm1() + "  =  " + lterm.getTerm2() + " ,";
			}

			t = t + " ]";
			list_r.add(t);

		}
		return check;
	}

	// méthode retourn un boolean,  est ce que terme2 appartient à terme1 ou non 
	private static boolean checkAppart(String term1, String term2) {
		int num = 0;
		boolean check = false;
		while (num < term2.length()) {

			if (NextChars(0, term1) == NextChars(num, term2)) {
				check = true;
				break;
			}
			num++;
		}

		return check;
	}
				
	// méthode pour remplacer le terme 1 par terme 2 partout dans l'ensemble
	// des equations entre les termes
	private static void remplaceTerm(String term1, String term2, int numTerm) {
		int num = 0;

		while (num < list_term.size()) {

			if (list_term.get(num).getTerm1() == term1 && numTerm != num) {

				list_term.get(numTerm).setTerm1(term2);

			} else if (list_term.get(num).getTerm2() == term1 && numTerm != num) {

				list_term.get(numTerm).setTerm2(term2);
			}

			num++;
		}

	}

	// méthode pour vérifier est ce que le système est résolu ou non 
	private static boolean SystemResol() { 
		boolean check = false;
		int num = 0;
		char chars;
		System.out.println("\n ***************SystemResol*************");
		while (num < list_term.size()) {

			chars = NextChars(0, list_term.get(num).getTerm1());

			if (chars == 'X' || chars == 'Y' || chars == 'Z') {
				check = true;
			} else {
				check = false;
				break;
			}
			num++;
		}

		return check;
	}

	// méthode pour vérifier est ce que exist une variable répeter  dans substitution 
	private static boolean SeresheSamefirstTerm() {
		boolean check = false;
		int num1 = 0, num2;
		String term1, term2;
		System.out.println("\n ***********SeresheSamefirstTerm************");

		while (num1 < list_term.size()) {
			term1 = list_term.get(num1).getTerm1();

			num2 = 0;
			while (num2 < list_term.size()) {

				term2 = list_term.get(num2).getTerm1();
				if (term1.equals(term2) && num2 != num1) {
					check = true;
					break;
				}

				num2++;
			}

			if (check == true)
				break;

			num1++;
		}

		System.out.println("\n SeresheSamefirstTerm check = " + check);
		return check;
	}

}
