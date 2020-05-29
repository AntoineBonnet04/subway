package asgn2;

public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
    
    public void dance() {
    	System.out.println("The tracks are moving!");
    	
    	for (int i = 0; i<this.networkLines.length; i++) { 
    		this.networkLines[i].shuffleLine(); //shuffles all lines in networkLines
    	}}
    
    public void undance() {
    	for (int i = 0; i<this.networkLines.length; i++) { 
    		this.networkLines[i].sortLine(); //unshuffles all lines in networkLines
    	}}
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	
    	TrainLine curLine = null; //use this variable to store the current line.
    	TrainStation curStation = null; //use this variable to store the current station. 
    	
    	int hoursCount = 0;
    	System.out.println("Departing from "+startStation);
    	
    	// Obtain departure station and line object from the name strings from input
    	
    	try {curLine = getLineByName(startLine);}
    	catch (Exception LineNotFoundException) 
    	{System.out.println("The current line could not be found in the network");}
    	
    	curStation = curLine.findStation(startStation);
    	TrainStation previous = null; 
    	
    	//iterate over the train network starting at StartStation, updating current pos.
    	while(curStation.getName() != endStation) {
    	
    		TrainStation next = curLine.travelOneStation(curStation, previous);
    		previous = curStation;
    		curStation = next;
    		hoursCount += 1; //  Station changes take one hour
    		
    		
    		if (! curStation.getLine().equals(curLine)) { // Line transfer takes 1 hour
    			curLine = curStation.getLine();
    			}
    		
    		if (hoursCount == 168) {
    			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
    			return hoursCount;
    			}
    		
    		//prints an update on your current location in the network.
	    	System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
	    	System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
	    	System.out.println("=============================================");
	    	
	    	// Network dances every 2 hours after every second transfer
	    	if ((hoursCount)% 2 == 0) {dance();} 
    		
    		}
    		
	    	System.out.println("Arrived at destination after "+hoursCount+" hours!");
	    	return hoursCount;
    }
    
    
    public TrainLine getLineByName(String lineName) throws LineNotFoundException{
    	for (int i = 0; i< this.networkLines.length; i++) {
    		if (this.networkLines[i].getName() == lineName) {
    			return this.networkLines[i];}}
    	throw new LineNotFoundException("No such train line can be found on this network."); 
    }
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}