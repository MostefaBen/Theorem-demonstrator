package bennaceur.Mostefa.M2.LIA.TP.Incoherence;
import java.util.ArrayList;
import java.util.Iterator;



public class Incoherence {

	
	private static  ArrayList<String> list_Clause = new ArrayList<String>();//pour les clause
	private static  ArrayList<LEquation> list = new ArrayList<LEquation>();//pour les clauses echec(resolution)
	private static  ArrayList<LEquation> list_term= new ArrayList<LEquation>();//pour les equations entre les termes
    private static  ArrayList<String> list_reduct = new ArrayList<String>();//pour les equations les clauses(choisie) echec(reduction)
    private static  ArrayList<String> list_r = new ArrayList<String>();//pour collecter tous les resultates dans une liste
    private  static String  TwoClauseReverse[] = new String[2] ;//pour les clause inverse (i.e p et Np)
    private  static int firstpredclause,secondpredclause,posvergulterm1;
    
	public ArrayList<String> getList_r() {
		return list_r;
	}

	public Incoherence(ArrayList<String> list_C) {

		list_Clause = list_C;

	}

	// methode pour appliquer l'algorithme de incoherence
	public boolean checkIncoherence() {

		boolean check = false;// false c-a-d coherent
		String clausAlgoResolution, clausAlgoReduction;
		list_reduct.clear();
		list_r.clear();
		
		
		while (true) {

			clausAlgoResolution = algoResolution();// pour la clause de
													// l'algoriteme de
													// Resolution

			if (clausAlgoResolution == "VIDE") {// VIDE :c-a-d retourn (vide)
												// via l'algoriteme de
												// Resolution
				check = true;
				break;
			}
			clausAlgoReduction = algoReduction();// pour la clause de
													// l'algoriteme de reduction

			System.out.println("\n\n Resolution= " + clausAlgoResolution
					+ "  Reduction= " + clausAlgoReduction);

			if (clausAlgoResolution == "k" && clausAlgoReduction == "k") {
				check = false;
				break;
			}

			if (clausAlgoResolution != "k") {

				list_Clause.add(clausAlgoResolution);
			}

			if (clausAlgoReduction != "k") {

				list_Clause.add(clausAlgoReduction);
				list_reduct.add(clausAlgoReduction);// en cas de repétition de
													// choix(c-a-d meme
													// clause->meme resultate)

			}

			System.out.println("list_clause = " + list_Clause + "\n");
		}

		System.out.println("\nlist_clause = " + list_Clause);
		System.out.println("\n\n list_clause.size = " + list_Clause.size());
		return check;
	}
		

	// methode pour retourn une clause a travers l'algoriteme de Resolution
	private static String algoResolution() {
		String clause = "VIDE";
		boolean check = false;
		boolean match = false;

		System.out.println("\n\n*****************resolution************************\n");
		
		while (match == false) {
			
			TwoClauseReverse[0]=null;
			TwoClauseReverse[1]=null;
			check = giveTwoClauseReverse(); // pour retourner deux clauses
											// inversé(i.e p et Np)

			System.out.print("\n giveTwoClause= " + check);
			System.out.print("\n TwoClauseReverse[0]= " + TwoClauseReverse[0]);
			System.out.print("\n TwoClauseReverse[1]= " + TwoClauseReverse[1]);

			if (check == true) {// exist deux clause inversé

				boolean f = isExistTwoClause(TwoClauseReverse[0],
						TwoClauseReverse[1]);

				if (f == true) {

					clause = "k";
					break;
				}

				LEquation lterm1 = new LEquation(TwoClauseReverse[0],
						TwoClauseReverse[1]);
				list.add(lterm1);

				list_r.add("Clause N°1= " + TwoClauseReverse[0]
						+ "     Clause N°2= " + TwoClauseReverse[1]);

				String firstClause = ClauseUnit(TwoClauseReverse[0],
						firstpredclause);
				String secondClause = ClauseUnit(TwoClauseReverse[1],
						secondpredclause);

				System.out.println("\n  firstpredclause= " + firstpredclause
						+ " secondpredclause= " + secondpredclause);

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
					t = t + lterm.getTerm1() + "  =  " + lterm.getTerm2() + " ,";

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
					t = t + lterm.getTerm1() + "  /  " + lterm.getTerm2() + " , ";
				}
				t = t + " }";

				list_r.add("ơ=" + t);

				if (check == false) {
					clause = "k";
					break;
				}

				String firstD = getDclause(firstClause, TwoClauseReverse[0]);
				String secondD = getDclause(secondClause, TwoClauseReverse[1]);

				String newClause = "k";

				if (firstD != "k" && secondD != "k") {

					newClause = firstD + "O" + secondD;

				} else if (firstD == "k" || secondD == "k") {

					newClause = firstD != "k" ? firstD : secondD;

				}

				if (newClause != "k") {

					System.out.print("\n\n newClause avant=" + newClause);
					list_r.add("{" + TwoClauseReverse[0] + " , "
							+ TwoClauseReverse[1] + "} |= ơ(" + newClause + ")");
				
					clause = applySebt(newClause);
				
					System.out.print("\n\n newClause apres=" + clause);

					list_r.add("la resolvant calculé a partir de {C1,C2} est :");
					list_r.add("ơ(D V D')=" + clause);

					match = true;

				} else {// newClause =="k"

					list_r.add("la resolvant calculé a partir de {C1,C2} est :");
					list_r.add("ơ(D V D')== VIDE");

					clause = "VIDE";
					match = true;

				}

			} else {// aucun deux clauses inversé alors arreter la boucle
				match = true;
				clause = "k";
			}

		}// fin TQ

		if (check == false)
			clause = "k";

		System.out.print("\n\n NClause=" + clause);
		return clause;
	}
		
	// methode pour donne deux clause invers par exemaple Np , p
	private static boolean giveTwoClauseReverse() {

		String fisrtclause, Secondclause;
		int num = 0, firstPred = 0, SecondPred = 0, numclause = 1;// commence de
																	// deuxieme
																	// clause
																	// dans la
																	// liste

		boolean check = false;

		while (num < list_Clause.size()) {

			fisrtclause = getClause(num);

			if (fisrtclause == "K")
				break;

			firstPred = 0;
			while (firstPred < fisrtclause.length()) {

				firstPred = givePred(fisrtclause, firstPred);

				System.out.print("\n firstPred= " + firstPred
						+ " fisrtclause= " + fisrtclause);

				if (firstPred == -1)// exit :c-a-d n'exist pas un symbole
									// predicatif
					break;

				if (NextChars((firstPred - 1), fisrtclause) == 'N') {// i.e :Np

					numclause = 0;

					while (numclause < list_Clause.size()) {// parcour pour la
															// clause suivant

						Secondclause = getClause(numclause);
						System.out.print("\n  Secondclause= " + Secondclause
								+ " numclause= " + numclause);

						if (Secondclause == "K"
								|| isExistTwoClause(fisrtclause, Secondclause) == true
								|| numclause == num)// si exit les deux clause
													// alors il retourner le meme
													// nauveau clause
							numclause++;
						else {
							SecondPred = 0;
							SecondPred = givePred(Secondclause, SecondPred);

							if (SecondPred == -1)// exit :c-a-d n'exist pas un
													// symbole predicatif
								break;

							while (SecondPred < Secondclause.length()) {

								if ((NextChars(firstPred, fisrtclause) == NextChars(
										SecondPred, Secondclause))
										&& NextChars((SecondPred - 1),
												Secondclause) != 'N') {

									TwoClauseReverse[0] = fisrtclause;
									firstpredclause = firstPred;

									TwoClauseReverse[1] = Secondclause;
									secondpredclause = SecondPred;

									check = true;

									break;

								}
								SecondPred = givePred(Secondclause,
										SecondPred + 1);
								if (SecondPred == -1)
									break;

							}

							System.out.print(" SecondPred= " + SecondPred
									+ "\n");

							if (SecondPred != -1 && check == true) {
								break;

							}

							numclause++;
						}

					}

				} else {// le chars != N (i.e p)

					numclause = 0;

					while (numclause < list_Clause.size()) {// parcour pour la
															// clause suivant

						Secondclause = getClause(numclause);

						System.out.print("\n  Secondclause= " + Secondclause
								+ " numclause= " + numclause);

						if (Secondclause == "K"
								|| isExistTwoClause(fisrtclause, Secondclause) == true
								|| numclause == num) {// si exit les deux clause
														// alors il return le
														// meme nauveau clause

							numclause++;

						} else {

							SecondPred = 0;
							SecondPred = givePred(Secondclause, SecondPred);

							while (SecondPred < Secondclause.length()) {

								if ((NextChars(firstPred, fisrtclause) == NextChars(
										SecondPred, Secondclause))
										&& NextChars(SecondPred - 1,
												Secondclause) == 'N') {

									TwoClauseReverse[0] = fisrtclause;
									firstpredclause = firstPred;

									TwoClauseReverse[1] = Secondclause;
									secondpredclause = SecondPred;
									check = true;

									break;

								}

								SecondPred = givePred(Secondclause,
										SecondPred + 1);

								if (SecondPred == -1)
									break;

							}

							System.out.print(" SecondPred= " + SecondPred
									+ "\n");

							if (SecondPred != -1 && check == true) {
								break;

							}

							numclause++;
						}
					}

				}

				if (SecondPred != -1 && check == true) {
					break;

				}

				firstPred++;// le chars predicative suivant
			}

			if (SecondPred != -1 && check == true) {
				break;

			}

			num++;
		}

		return check;
	}

	// methode pour donner une clause chez la position numclause
	private static String getClause(int numclause) {

		String clause = "K";// k: c-a-d n'exist pas

		if (numclause < list_Clause.size())
			clause = list_Clause.get(numclause);

		return clause;
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

	// méthode pour vérifier est ce que les clauses en paramètre sont exist dans
	// la liste de clauses qui déjà passé precedement
	private static boolean isExistTwoClause(String fisrtclause,
			String secondclause) {
		boolean check = false;

		int num = 0;

		while (num < list.size()) {

			if (fisrtclause.equals(list.get(num).getTerm1())) {

				if (secondclause.equals(list.get(num).getTerm2())) {
					check = true;
					break;
				}

			} else if (fisrtclause.equals(list.get(num).getTerm2())) {

				if (secondclause.equals(list.get(num).getTerm1())) {
					check = true;
					break;
				}

			}

			num++;

		}

			System.out.println("\n isExistTwoClause= "+check);
			return check;
		}
		
	// méthode pour retourner une clause à travers l'algorithme de Réduction
	private static String algoReduction() {
		String clause = null;
		boolean check = true;

		System.out.println("\n\n*******************Reduction*********************\n");
		System.out.println("\n\n list_reduct.size()= " + list_reduct.size());

		if (list_reduct.size() != 0) {

			int num = 0;
			while (num < list_Clause.size()) {

				clause = getClauseReduct(num);

				if (clause == "k")
					break;

				check = isExistClauseInE(clause, list_reduct);

				if (check == false) {
					break;
				}
				num++;
			}

			System.out.println(list_reduct);
			System.out.println("\n\n num =" + num + " list_reduct.size() ="
					+ list_reduct.size());

			if (num >= list_Clause.size())
				clause = "k";

		} else {// c-a-d premier appel à cette méthode

			clause = getClauseReduct(0);

		}

		System.out.println("\n\n getClauseReduct= " + clause + "  check = "
				+ check + "\n\n");

		if (clause != "k") {

			list_reduct.add(clause);

			String firstClauseRed = ClauseUnit(clause, firstpredclause);
			String secondClauseRed = ClauseUnit(clause, secondpredclause);
			System.out.print("\n\n firstClauseRed  unit =" + firstClauseRed
					+ "\n secondClauseRed unit =" + secondClauseRed);
			System.out.println("\n firstpredclause=" + firstpredclause
					+ "  secondpredclause=" + secondpredclause);

			list_term.clear();
			posvergulterm1 = 0;
			ExtraitEquaClause(firstClauseRed, secondClauseRed);// extraire les
																// equations
																// entre les
																// termes

			check = isAccepctExtrait();
			System.out.println("\n isAccepctExtrait= " + check);

			if (check == true) {

				list_r.add("Clause reductible= " + clause);

				list_r.add("Predicat Atomique N°1= " + firstClauseRed
						+ "     Predicat Atomique N°2= " + secondClauseRed);

				String t = "S= [";
				Iterator<LEquation> lter = list_term.iterator();
				while (lter.hasNext()) {
					LEquation lterm = (LEquation) lter.next();
					System.out.println("\n getTerm1= " + lterm.getTerm1()
							+ " getTerm2= " + lterm.getTerm2());
					t = t + lterm.getTerm1() + " = " + lterm.getTerm2()
							+ "  ,  ";

				}

				t = t + " ]";
				list_r.add(t);

				check = AlgUnification();// appliquer l'algoriteme de
											// l'unification
				System.out.println("\n unification= " + check);

				String sc = "vrai";
				if (check == false)
					sc = "faux";
				list_r.add("Unification= " + sc);

				t = "ơ={  ";
				Iterator<LEquation> lter2 = list_term.iterator();
				while (lter2.hasNext()) {
					LEquation lterm = (LEquation) lter2.next();
					System.out.println("\n getTerm1= " + lterm.getTerm1()
							+ " getTerm2= " + lterm.getTerm2());
					t = t + lterm.getTerm1() + "   /   " + lterm.getTerm2()
							+ "  ,  ";
				}
				t = t + " }";
				list_r.add(t);

				if (check == true) {

					String c = ClauseUnit(clause, firstpredclause);
					String clauseRed = getDclause(c, clause);
					System.out.println("\n\n clauseRed= " + clauseRed);

					clause = applySebt(clauseRed);

					System.out.println("\n\n clause aprés substitution= "
							+ clause);
					list_r.add("ơ(  " + clauseRed + "   ) =   " + clause);
				}

			}

		}

		if (check == false)
			clause = "k";

		return clause;
	}

	// methode pour donne une clause reductible
	private static String getClauseReduct(int num) {
		int numclauseRed;
		String clauseRed = "k";
		boolean checkClause, check = false;
		int pos1, pos2;

		while (num < list_Clause.size()) {

			clauseRed = list_Clause.get(num);
			checkClause = isClauseRed(clauseRed);

			if (checkClause == false)
				break;

			pos1 = givePred(clauseRed, 0);
			if (pos1 == -1)
				break;

			numclauseRed = pos1 + 1;

			while (numclauseRed < clauseRed.length()) {

				pos2 = givePred(clauseRed, numclauseRed);
				if (pos2 == -1)
					break;

				if (NextChars(pos1, clauseRed) == NextChars(pos2, clauseRed)) {

					if ((pos1 == 0 && NextChars(pos2 - 1, clauseRed) == 'O')
							|| (pos1 != 0 && NextChars(pos1 - 1, clauseRed) == NextChars(
									pos2 - 1, clauseRed))) {
						firstpredclause = pos1;
						secondpredclause = pos2;
						check = true;
						break;
					}
				}

				numclauseRed++;
			}

			if (check == true)
				break;

			num++;
		}

		if (check == false)
			clauseRed = "k";

		return clauseRed;
	}
		
	// methode est ce que la clause que donner en parametre est accpet
	// reductibilité
	private static Boolean isClauseRed(String clauseRed) {
		boolean check = false;

		int pos = clauseRed.indexOf('O');

		if (pos != -1) {

			check = true;
		}

		return check;
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
	
	// méthode pour extraire tout les équations entre les termes
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

		//methode pour appliquer l'algorithme de l'unification
		private static boolean AlgUnification() {
			
			int numTerm=0;
			boolean check =false;
			String term1 ,term2;
			char chars1,chars2;
			
			while( check == false){//TQ le system n'est pas resolu

				System.out.println("\n numTerm = "+numTerm);
				term1 = list_term.get(numTerm).getTerm1();
				term2 = list_term.get(numTerm).getTerm2();
				
				chars1=NextChars(0, term1);
				chars2=NextChars(0, term2);
				
				System.out.println("\n 1unif term1 ="+term1 +" term2= "+term2 );
				
				if(term1.equals(term2)){//X=X 
					
					list_term.remove(numTerm);
						//systeme resolu et le cas de p(X)E(Np(X)) -> X=X -> suprimer X -> []
					if(list_term.size() == 0 ){
							check =true;
							break;
					}
					
				}else if((chars1=='f' || chars1=='g' || chars1=='h') && (chars2=='f' || chars2=='g' || chars2=='h') && (chars1 != chars2) ){//f!=g
					
					check= false;
					break;
					
				}else if((chars1=='f' || chars1=='g' || chars1=='h') && (chars2=='f' || chars2=='g' || chars2=='h') && (chars1 == chars2)){//f=f
					
					list_term.remove(numTerm);

					//boolean match  = isAccepctExtrait(term1,term2);
					//if(match == true){
						
						term1 ="p"+term1.substring(1);
						term2 ="p"+term2.substring(1);
						posvergulterm1=0;
						ExtraitEquaClause(term1,term2);//extraire les equations entre les termes
						
						numTerm = list_term.size();//pour repeter la boucle et verifier de premier  terme
					//}
				}else if(chars1 !='X' && chars1 !='Y' && chars1 !='Z' ){//t=X
					
					String exchange =list_term.get(numTerm).getTerm1();
					list_term.get(numTerm).setTerm1(list_term.get(numTerm).getTerm2());
					list_term.get(numTerm).setTerm2(exchange);
				    
					term1 = list_term.get(numTerm).getTerm1();
					term2 = list_term.get(numTerm).getTerm2();
					if(checkAppart(term1, term2) == true){
						check = false;
						break;
					}
				
				}else if(checkAppart(term1, term2) == true){//X=t et X appartient dans t 
					
					check = false;
					break;
					
				}else if(checkAppart(term1, term2) == false){//X=t et X n'appartient pas dans t 
					
					remplaceTerm(term1,term2,numTerm);
				}
				
				numTerm++;
			
				if(numTerm >= list_term.size())
					numTerm=0;
				
				System.out.println("\n 2unif term1 ="+term1 +" term2= "+term2 );
				
				check =SystemResol();

				System.out.println(" SystemResol check = "+check);
				
				
				if(check == true){

					boolean match=SeresheSamefirstTerm();
					if(match==true){
						check=false;
					    break;	
					}
				
				}
				
					
				String t="S=[ ";
				Iterator<LEquation> lter = list_term.iterator();
				while (lter.hasNext()) {
						LEquation lterm = (LEquation) lter.next();
						t=t+lterm.getTerm1()+"  =  "+lterm.getTerm2()+" ,";
			   	 }
				
				t=t+" ]";	
				list_r.add(t);
				
				
				
				
		  }
			return check;
		}


	// méthode pour vérifier est ce que le système est résolu
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
		
	// methode pour verifier le dernier chars
	private static boolean checkArcs(String copyS) {

		int num = 0, n=0;
		boolean check = false;

		while (num < copyS.length()) {

			if (NextChars(num, copyS) == '(') {
				n++;
			} else if (NextChars(num, copyS) == ')') {
				n--;
			}
			num++;

		}
		if (n==0)
		   check = true;

		return check;
	}
				
	// méthode spécial pour méthode de résolution pour retourner le D clause
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

	// méthode pour appliquer la substitution
	private static String applySebt(String newClause) {
		int numD = 0;
		String strTmp = "";
		char charTmp;
		System.out.println("\n applySebt Clause =" + newClause);

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

	// méthode return boolean est ce que terme2 appartient à terme1
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

	// méthode pour remplacer le terme 1 par terme 2 partout dans ensemble
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
	
	// méthode pour vérifier est ce que exist une variable dans deux positions
	// différents
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

	// methode pour vérifier est ce que la clause passé en parametre exist deja
	// dans la liste des clauses
	private static boolean isExistClauseInE(String clause,
			ArrayList<String> list) {
		int num = 0;
		boolean check = false;
		while (num < list.size()) {

			if (clause.equals(list.get(num))) {
				check = true;
				break;
			}
			num++;
		}
		return check;
	}
				
	// méthode pour extraire seulement une clause unitaire
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
				
	// donner le chars suivant
	private static char NextChars(int numTmp, String copyS) {

		char charTmp = 'k'; // k : c-à-d la fin de chaine

		if (numTmp < copyS.length() && numTmp >= 0) {

			charTmp = copyS.charAt(numTmp);
		}

		return charTmp;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////	
	
	

}