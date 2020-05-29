import java.util.Arrays;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {
	
		int i = 1;
		TrainStation current = this.leftTerminus;
		while (current.getRight() != null) {
			i++; current = current.getRight(); 	}
		return i;
	}
	
	public void reverseDirection() {
		this.goingRight = ! this.goingRight;
	}
	
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException {
		try {
			current = this.findStation(current.getName());
		}
		catch (Exception StationNotFoundException) {
			System.out.println("The current station cannot be found on the train line.");
		}
		if (previous == null){ // If initial station
			if (current.hasConnection){
				return current.getTransferStation();}
			else { return getNext(current);}
			}
	
		// transfer if current has connection but previous doesn't
		if ((current.hasConnection) && !(previous.hasConnection) ) {
			return current.getTransferStation();} 
		
		else {return getNext(current);}
	}

	
	public TrainStation getNext(TrainStation station) throws StationNotFoundException {
		
		station = this.findStation(station.getName());
	
		if (this.goingRight) {  					//if going right
			if (station.isRightTerminal()) {		 //if right terminal
				this.reverseDirection();
				return station.getLeft();} 			//Switch direction
			
			else {return station.getRight();}
			}
		else  {								//if going left    
			if (station.isLeftTerminal()) {	//if left terminal
					
					this.reverseDirection();
					return station.getRight();} 	// Switch direction
			
			else { 
				return station.getLeft();}
		
			}	
	}


	public TrainStation findStation(String name) throws StationNotFoundException {
		
		if (this.rightTerminus.getName() == name) {return this.rightTerminus;} // 
		
		TrainStation current = this.leftTerminus;
		
		while (! current.isRightTerminal()) {
			if (current.getName() == name) {return current;}
			current = current.getRight();}
		
		throw new StationNotFoundException("No such train station exists on this line.");
	}
	
	public void swap (int j) {
		// swap train stations at index j and index j+1 from lineMap
		TrainStation tmp = this.lineMap[j];
		this.lineMap[j] = this.lineMap[j+1];
		this.lineMap[j+1] = tmp;
	}
	
	public void sortLine() { 
		
		// Bubble Sort:
		int n = this.getSize();
		for (int i = 0; i < n; i++) { 
			
			for (int j = 0; j< n-1; j++) {
				
				String name1 = this.lineMap[j].getName();
				String name2 = this.lineMap[j+1].getName();
				// Check if name1 and name2 are in alphabetical order
				if (name1.compareTo(name2) > 0) {swap(j);}
		}}
		
		for (int q = 0; q < n; q++) {	//Reset fields of stations
			this.lineMap[q].setNonTerminal();
			this.lineMap[q].setRight(null);
			this.lineMap[q].setLeft(null);
		}
		
		this.leftTerminus = this.lineMap[0];  // Set left terminal
		this.lineMap[0].setLeftTerminal();
	
		if (n>1){ 
			this.rightTerminus = this.lineMap[n-1];// Set right terminal
			this.lineMap[n-1].setRightTerminal();
			
			for (int p = 0; p < n-1; p++) {
				// Update right/left of each train
				this.lineMap[p].setRight(this.lineMap[p+1]);
				this.lineMap[n-1-p].setLeft(this.lineMap[n-2-p]); 
		}}}

	public TrainStation[] getLineArray() {
		// Assume line length is greater or equal to 2
		int length = this.getSize(); // initialize length
		TrainStation current = this.leftTerminus;  //start with left terminal
		
		TrainStation[] array = new TrainStation[length];
		
		for (int i = 0; i < length; i++) {
			array[i] = current;
			current = current.getRight();}
		
		this.lineMap = array;
		return array;
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {
		
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		
		
		int n = shuffledArray.length; 
		
		for (int q = 0; q < n; q++) {	//Reset fields of stations
			this.lineMap[q].setNonTerminal();
			this.lineMap[q].setRight(null);
			this.lineMap[q].setLeft(null);
		}
		
		this.leftTerminus = shuffledArray[0];  // Set left terminal
		this.lineMap[0].setLeftTerminal();
	
		if (n>1){ // If at least 2 stations in line
			this.rightTerminus = shuffledArray[n-1];// Set right terminal
			this.lineMap[n-1].setRightTerminal();
			
			for (int p = 0; p < n-1; p++) {
				// Update right/left of each train
				this.lineMap[p].setRight(shuffledArray[p+1]);
				this.lineMap[n-1-p].setLeft(shuffledArray[n-2-p]); 
		}}}
	

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
