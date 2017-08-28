package com.ryanliang.knockknock;

import java.net.*;
import java.util.Collections;
import java.util.List;
import java.io.*;

/**
 * KnockKnockProtocol class defines the customized network communication protocols between the server and the client. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KnockKnockProtocol{
	private static final int WAITING = 0;
	private static final int SENTKNOCKKNOCK = 1;
	private static final int SENTCLUE = 2;
	private static final int ANOTHER = 3;

	private final int NUMJOKES;

	private int state = WAITING;
	private int currentJoke = 0;
	
	private KKModellable model;
	private List<KKJoke> kkJokeList;
	
	//private final String whoIsThere = "Who's there?";
	private final String whoIsThere = "a";

	/**
	 * This is the only constructor defined for this class.
	 */
	public KnockKnockProtocol(){
		model = new KKModel();
		kkJokeList = model.getListOfKKJokes();
		Collections.shuffle(kkJokeList);
		NUMJOKES = kkJokeList.size();
	}
	
	/**
	 * This method process the customized network communication protocols between the server and the client.
	 */
	public String processInput(String theInput) {
		String theOutput = null;

		if (state == WAITING) {
			theOutput = "Knock! Knock!";
			state = SENTKNOCKKNOCK;
		} else if (state == SENTKNOCKKNOCK) {
			if (theInput.equalsIgnoreCase(whoIsThere)) {
				theOutput = kkJokeList.get(currentJoke).getClue();
				state = SENTCLUE;
			} else {
				theOutput = "You're supposed to say \"" + whoIsThere + "\"! " +
						"Try again. Knock! Knock!";
			}
		} else if (state == SENTCLUE) {
			if (theInput.equalsIgnoreCase(kkJokeList.get(currentJoke).getClue() + " who?")) {
				theOutput = kkJokeList.get(currentJoke).getAnswer() + " Want another? (y/n)";
				state = ANOTHER;
			} else {
				theOutput = "You're supposed to say \"" + 
						kkJokeList.get(currentJoke).getClue() + 
						" who?\"" + 
						"! Try again. Knock! Knock!";
				state = SENTKNOCKKNOCK;
			}
		} else if (state == ANOTHER) {
			if (theInput.equalsIgnoreCase("y")) {
				theOutput = "Knock! Knock!";
				if (currentJoke == (NUMJOKES - 1))
					currentJoke = 0;
				else
					currentJoke++;
				state = SENTKNOCKKNOCK;
			} else {
				theOutput = "Bye";
				state = WAITING;
			}
		}
		return theOutput;
	}
}
