package main;

import java.util.*;
import funcions.Put;
import funcions.Read;

public class BuscaMines {

	final static char TAPAT = 'X';
	public static boolean fijoc = false;
	public static char tmostrar[][];
	public static int tocult[][];
	public static int  ample, alt, qmines, opcio = 0;
	
	/***************************************************************/
	/************		FUNCIÓ PRINCIPAL		********************/
	/***************************************************************/
	public static void main(String[] args) {
		
		int colu, fila;
		
		do{
			minar();
			
			do{
				mostrarTauler(tmostrar);Put.ln("");
				if(qdestapades() || fijoc)
						opcio = 3;
				
				if(opcio != 3){
					Put.ln("1. Picar\n2. Posar bandera\n3. Ixir");
					opcio = Read.Int();
					
					switch(opcio){
					case 1:
						picar(demanarCol(), demanarFila());
						break;
					case 2:
						colu = demanarCol();
						fila = demanarFila();
						if(tmostrar[colu][fila] == 'P')
							tmostrar[colu][fila] = 'X';
						else
							if(tmostrar[colu][fila] == 'X')
								tmostrar[colu][fila] = 'P';
						break;
					case 3:
						Put.ln("Torna a jugar pronte!!!");
						break;
					default:
						Put.ln("Opció no vàlida.");					
					}
				}
			}while(opcio != 3);

			if(fijoc)
				Put.ln("Has perdut!!!");
			else
				Put.ln("Has guanyat!!!");
			
			Put.ln("Vols tornar a jugar(s)(n)?");
			int sino = Character.toLowerCase(Read.carac());
			if(sino == 'n'){
				opcio = 3;
				Put.ln("\nAdéu!!");
			}else
				if(sino == 's'){
					opcio = 0;
					fijoc = false;
				}
		}while(opcio != 3);
	}

	/***************************************************************/
	/************		INICI DE FUNCIONS		********************/
	/***************************************************************/
	/******************** Mostrar tauler ***************************/
	public static void mostrarTauler(char tauler[][]) {
		
		System.out.print("\n\n\n\n\n\n\n\n\n");
		Put.ln("***************************\n********BUSCAMINES*********\n***************************");
		pintarNumeros();
		
		for(int fila = 0; fila < tauler[0].length; fila++){
			if(fila < 10)
				System.out.print(" " + fila + " ");
			else
				System.out.print(fila + " ");
			for(int colu = 0; colu < tauler.length; colu++)
				if(colu == tauler.length - 1)
					System.out.println("|" + tauler[colu][fila] + "| " + fila);
				else
					if(tauler[colu][fila] == 0)
						System.out.print("|" + '_');
					else
						if(tauler[colu][fila] == 1)
							System.out.print("|" + '¤');
						else
							System.out.print("|" + tauler[colu][fila]);
		}
		
		pintarNumeros();
	}
	
	//////////////////////////////////////////////////////////////////////
	/*****	Destapa la posició demanada i adjacents si és valida	*****/
	public static void destapar(int colu, int fila) {
		
		if(!incorrecte(colu, fila) && !destapat(colu, fila)){
			if(qma(colu, fila) == 0){
				tmostrar[colu][fila] = '_';
				for(int columna = colu-1; columna <= colu+1; columna++)
					for(int fil = fila-1; fil <= fila+1; fil++)
						if(columna != colu || fil != fila)
							destapar(columna, fil);
			}
			else
				if(tmostrar[colu][fila] != TAPAT && !minat(colu, fila))
					tmostrar[colu][fila] = '_';
				else
					tmostrar[colu][fila] = (String.valueOf(qma(colu, fila))).charAt(0);
		}			
	}
	
	//////////////////////////////////////////////////////////////////////
	/************** Col·locar mines i initziatlitzar matrius ************/
	public static void minar() {
	
		Put.noln("Dis-me el número de columnes:\t");
		ample = Read.Int();
		Put.noln("Dis-me el número de files:\t");
		alt = Read.Int();
		tmostrar = new char [ample][alt];
		tocult = new int [ample][alt];
		
		do{
			Put.noln("Quantes mines vols posar?\t");
			qmines = Read.Int();
		}while(qmines > ample*alt);
		
		Random aleatori = new Random();
		int con = 0, filaleatoria, colaleatoria;
		
		while(con < qmines){
			
			filaleatoria = aleatori.nextInt(alt);
			colaleatoria = aleatori.nextInt(ample);
			if(tocult[colaleatoria][filaleatoria] != 1){
				tocult[colaleatoria][filaleatoria] = 1;
				con++;
			}
		}
		
		for(int i = 0; i < tmostrar.length; i++)
			for(int j = 0; j < tmostrar[0].length; j++)
				tmostrar[i][j] = TAPAT;
	}
	
	////////////////////////////////////////////////////////////////////
	/************		Comprova si posició està descoberta		*******/
	public static boolean destapat(int colu, int fila) {
	
		return (tmostrar[colu][fila] != TAPAT);
	}
	
	////////////////////////////////////////////////////////////////////
	/************	Comprova si posició demanada conté mina	***********/
	public static void picar(int colu, int fila) {
		
		if(!incorrecte(colu, fila))
			if(qmines == (ample*alt)-1){
				for(int i = 0; i < tocult.length; i++)
					for(int j = 0; j < tocult[0].length; j++)
						if(tocult[i][j] != tocult[colu][fila])
							tocult[i][j] = 1;
						else
							tocult[i][j] = 0;
				taulerFi();
				opcio = 3;
			}
			if(tocult[colu][fila] == 1){
				fijoc = true;
				Put.ln("Has picat sobre una mina.");
				taulerFi();
			}
			else
				destapar(colu, fila);
	}
	
	////////////////////////////////////////////////////////////////////
	/************		Comprova si posició correcta		***********/
	public static boolean incorrecte(int colu, int fila) {
		
		return ((colu < 0 || colu > tmostrar.length-1) || (fila < 0 || fila > tmostrar[0].length-1));
			
	}
	
	////////////////////////////////////////////////////////////////////
	/***************	Retorna quantitat mines adjacents	***********/
	public static int qma(int colu, int fila) {
		
		int qmina = 0;
		
		for(int columna = colu-1; columna <= colu+1; columna++)
			for(int fil = fila-1; fil <= fila+1; fil++)
				if(columna != colu || fil != fila)
					if(minat(columna, fil))
						qmina++;
		
		return qmina;
	}
	
	///////////////////////////////////////////////////////////////////
	/**************		Comprova si posició conté mina	**************/
	public static boolean minat(int colu, int fila) {
		
		if(!incorrecte(colu, fila))
			if(tocult[colu][fila] == 1)
				return true;
			else
				return false;
		else
			return false;
	}
	
	public static int demanarCol() {
		
		int col;
		do{
			Put.noln("Dis-me la columna:\t");
			col = Read.Int();
		}while(col > tmostrar.length-1 || col < 0);
			
		return col;
	}
	
	public static int demanarFila() {
		
		int fila;
		do{
			Put.noln("Dis-me la fila:\t");
			fila = Read.Int();
		}while(fila > tmostrar[0].length-1 || fila < 0);
		
		return fila;
	}
	
	/////////////////////////////////////////////////////////////////////
	/*******	Comprova si el joc acaba amb èxit	********************/
	public static boolean qdestapades() {
	
		int destapades = 0;
		
		for(int fila = 0; fila < tmostrar[0].length; fila++)
			for(int colu = 0; colu < tmostrar.length; colu++)
				if(tmostrar[colu][fila] != TAPAT)
					destapades++;
		
		return (destapades == (ample*alt)-qmines);
	}
	
	/////////////////////////////////////////////////////////////////////
	/**** Mostra el tauler completament destapat si acaba la partida ***/
	public static void taulerFi() {
		
		for(int i = 0; i < tmostrar[0].length; i++)
			for(int j = 0; j < tmostrar.length; j++)
				if(tocult[j][i] == 1)
					tmostrar[j][i] = '¤';
	}
	
	/////////////////////////////////////////////////////////////////////
	/*******	Col·loca els números al voltat del tauler	************/
	public static void pintarNumeros() {
		
		for(int colu = 0; colu < tmostrar.length; colu++)
			if(colu == 0)
				if(alt > 9)
					System.out.print("    " + colu);
				else
					System.out.print("   " + colu);
			else
				if(colu > 9)
					System.out.print(" " + colu%10);
				else
					System.out.print(" " + colu);
		
		System.out.println();
	}
}
