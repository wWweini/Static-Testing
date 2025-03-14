package edu.pitt.cs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;



/**
 * Code by @author Wonsun Ahn.  Copyright Summer 2024.
 * 
 * <p>
 * BeanCounterLogic: The bean counter, also known as a quincunx or the Galton
 * box, is a device for statistics experiments named after English scientist Sir
 * Francis Galton. It consists of an upright board with evenly spaced nails (or
 * pegs) in a triangular form. Each bean takes a random path and falls into a
 * slot.
 *
 * <p>
 * Beans are dropped from the opening of the board. Every time a bean hits a
 * nail, it has a 50% chance of falling to the left or to the right. The piles
 * of beans are accumulated in the slots at the bottom of the board.
 * 
 * <p>
 * This class implements the core logic of the machine. The MainPanel uses the
 * state inside BeanCounterLogic to display on the screen.
 * 
 * <p>
 * Note that BeanCounterLogic uses a logical coordinate system to store the
 * positions of in-flight beans.For example, for a 4-slot machine:
 * 
 * <pre>
 *                      (0, 0)
 *               (0, 1)        (1, 1)
 *        (0, 2)        (1, 2)        (2, 2)
 *  (0, 3)       (1, 3)        (2, 3)       (3, 3)
 * [Slot0]       [Slot1]       [Slot2]      [Slot3]
 * </pre>
 */

public class BeanCounterLogicImpl implements BeanCounterLogic {
	// TODO: Add member methods and variables as needed.
	// You will need data structures to represent 1) beans that are waiting to fall
	// down in the top reservoir, 2) beans that are in-flight bouncing on pegs, and
	// 3) beans that have fallen into slots at the bottom.
	private int slotCount; 
    private Queue<Bean> remainingBeans;  // beans that are waiting to fall down in the top reservoir,
    private LinkedList<Bean> inFlightBeans;  // beans that are in-flight bouncing on pegs
    private LinkedList<Bean> slotBeans;  // beans that have fallen into slots at the bottom
    private int[] slots;  // beans in each slot
    private int[] xPositionMap; // track x position for in flight beans

	/**
	 * Constructor - creates the bean counter logic object that implements the core
	 * logic with the provided number of slots.
	 * 
	 * @param slotCount the number of slots in the machine
	 */
	BeanCounterLogicImpl(int slotCount) {
		// TODO: Implement
		this.slotCount = slotCount;
        this.remainingBeans = new LinkedList<>();
        this.inFlightBeans = new LinkedList<>();
        this.slotBeans = new LinkedList<>();
        this.slots = new int[slotCount];
        this.xPositionMap = new int[slotCount];
        
        //initialize xPositionMap
        for (int i = 0; i < slotCount; i++) {
            xPositionMap[i] = NO_BEAN_IN_YPOS;
        }
	}

	/**
	 * Returns the number of slots the machine was initialized with.
	 * 
	 * @return number of slots
	 */
	public int getSlotCount() {
		// TODO: Implement
		return slotCount;
	}
	
	/**
	 * Returns the number of beans remaining that are waiting to get inserted.
	 * 
	 * @return number of beans remaining
	 */
	public int getRemainingBeanCount() {
		// TODO: Implement
		return remainingBeans.size();
	}

	/**
	 * Returns the x-coordinate for the in-flight bean at the provided y-coordinate.
	 * 
	 * @param yPos the y-coordinate in which to look for the in-flight bean
	 * @return the x-coordinate of the in-flight bean; if no bean in y-coordinate, return NO_BEAN_IN_YPOS
	 */
	public int getInFlightBeanXPos(int yPos) {
		// TODO: Implement
		if (yPos < 0 || yPos >= xPositionMap.length) {
            return NO_BEAN_IN_YPOS;
        }
        return xPositionMap[yPos];
	}

	/**
	 * Returns the number of beans in the ith slot.
	 * 
	 * @param i index of slot
	 * @return number of beans in slot
	 */
	public int getSlotBeanCount(int i) {
		// TODO: Implement
		if (i < 0 || i >= slotCount) {
            return 0;
        }
        return slots[i];
	}

	/**
	 * Calculates the average slot number of all the beans in slots.
	 * 
	 * @return Average slot number of all the beans in slots.
	 */
	public double getAverageSlotBeanCount() {
		// TODO: Implement
		if (slotBeans.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        double totalBeans = 0.0;
        
        for (int i = 0; i < slotCount; i++) {
            sum += i * slots[i];
            totalBeans += slots[i];
        }
        
        return sum / totalBeans;
	
	}

	/**
	 * Removes the lower half of all beans currently in slots, keeping only the
	 * upper half. If there are an odd number of beans, remove (N-1)/2 beans, where
	 * N is the number of beans. So, if there are 3 beans, 1 will be removed and 2
	 * will be remaining.
	 */
	public void upperHalf() {
		// TODO: Implement
		int totalBeans = slotBeans.size();
        if (totalBeans <= 1) return;
        
        int beansToRemove = (totalBeans - 1) / 2;
        for (int i = 0; i < beansToRemove; i++) {
            slotBeans.remove(0);
        }
        
        //recalculate slots array
        Arrays.fill(slots, 0);
        for (Bean bean : slotBeans) {
            slots[bean.getXPos()]++;
        }
	}

	/**
	 * Removes the upper half of all beans currently in slots, keeping only the
	 * lower half.  If there are an odd number of beans, remove (N-1)/2 beans, where
	 * N is the number of beans. So, if there are 3 beans, 1 will be removed and 2
	 * will be remaining.
	 */
	public void lowerHalf() {
		// TODO: Implement
		int totalBeans = slotBeans.size();
        if (totalBeans <= 1) return;
        
        int beansToRemove = (totalBeans - 1) / 2;
        for (int i = 0; i < beansToRemove; i++) {
            slotBeans.remove(slotBeans.size() - 1);
        }
        
        //recalculate slots array
        Arrays.fill(slots, 0);
        for (Bean bean : slotBeans) {
            slots[bean.getXPos()]++;
        }
	}

	/**
	 * A hard reset. Initializes the machine with the passed beans. The machine
	 * starts with one bean at the top.
	 * 
	 * @param beans array of beans to add to the machine
	 */
	public void reset(Bean[] beans) {
		// TODO: Implement
		//clear all 
		remainingBeans.clear();
		inFlightBeans.clear();
		slotBeans.clear();
		Arrays.fill(slots, 0);
		Arrays.fill(xPositionMap, NO_BEAN_IN_YPOS);
	
		//check for valid beans array
		if (beans != null && beans.length > 0) {
			for (Bean bean : beans) {
				if (bean != null) {
					bean.reset();
					remainingBeans.add(bean);
				}
			}
			//add the start bean
			if (!remainingBeans.isEmpty()) {
				Bean firstBean = remainingBeans.remove();
				inFlightBeans.add(firstBean);
				xPositionMap[0] = firstBean.getXPos();
			}
		}
	}

	/**
	 * Repeats the experiment by scooping up all beans in the slots and all beans
	 * in-flight and adding them into the pool of remaining beans. As in the
	 * beginning, the machine starts with one bean at the top.
	 */
	public void repeat() {
		// TODO: Implement
		for (Bean bean : slotBeans) {
			bean.reset();
			remainingBeans.add(bean);
		}
	
		for (Bean bean : inFlightBeans) {
			bean.reset();
			remainingBeans.add(bean);
		}
	
		//clear in-flightBeans and slotBeans
		inFlightBeans.clear();
		slotBeans.clear();
		Arrays.fill(slots, 0);
		Arrays.fill(xPositionMap, NO_BEAN_IN_YPOS);
	
		//start with the first bean from remainingBeans
		if (!remainingBeans.isEmpty()) {
			Bean firstBean = remainingBeans.remove();
			inFlightBeans.add(firstBean);
			xPositionMap[0] = firstBean.getXPos();
		}
	}

	/**
	 * Advances the machine one step. All the in-flight beans fall down one step to
	 * the next peg. A new bean is inserted into the top of the machine if there are
	 * beans remaining.
	 * 
	 * @return whether there has been any status change. If there is no change, that
	 *         means the machine is finished.
	 */
	public boolean advanceStep() throws BeanOutOfBoundsException {
		//if no beans left then no changes
		if (remainingBeans.isEmpty() && inFlightBeans.isEmpty()) {
			return false;
		}
	
		//update positions of in-flight beans
		for (int i = inFlightBeans.size() - 1; i >= 0; i--) {
			Bean currentBean = inFlightBeans.get(i);
	
			int currentYPos = currentBean.getYPos();
			int currentXPos = currentBean.getXPos();
	
			//if the bean is at the bottom row, move it to a slot
			if (currentYPos == slotCount - 1) {
				slots[currentXPos]++;
				slotBeans.add(currentBean);
				inFlightBeans.remove(i);
				xPositionMap[currentYPos] = NO_BEAN_IN_YPOS;
			} else {
				//advance the bean one step downward
				currentBean.advanceStep();
				int newYPos = currentYPos + 1;
				int newXPos = currentBean.getXPos();
	
				//update x position
				xPositionMap[currentYPos] = NO_BEAN_IN_YPOS;
				xPositionMap[newYPos] = newXPos;
			}
		}
	
		//add new bean from the top if there are remainingBeans
		if (!remainingBeans.isEmpty()) {
			Bean newBean = remainingBeans.remove();
			newBean.reset();
			inFlightBeans.addFirst(newBean);
			xPositionMap[0] = newBean.getXPos();
		}

		//changed
		return true;
	}

	/**
	 * Number of spaces in between numbers when printing out the state of the machine.
	 * Make sure the number is odd (even numbers don't work as well).
	 */
	private final int xspacing = 3;

	/**
	 * Calculates the number of spaces to indent for the given row of pegs.
	 * 
	 * @param yPos the y-position (or row number) of the pegs
	 * @return the number of spaces to indent
	 */
	private int getIndent(int yPos) {
		int rootIndent = (getSlotCount() - 1) * (xspacing + 1) / 2 + (xspacing + 1);
		return rootIndent - (xspacing + 1) / 2 * yPos;
	}

	/**
	 * Constructs a string representation of the bean count of all the slots.
	 * 
	 * @return a string with bean counts for each slot
	 */
	public String getSlotString() {
		StringBuilder bld = new StringBuilder();
		Formatter fmt = new Formatter(bld);
		String format = "%" + (xspacing + 1) + "d";
		for (int i = 0; i < getSlotCount(); i++) {
			fmt.format(format, getSlotBeanCount(i));
		}
		fmt.close();
		return bld.toString();
	}

	/**
	 * Constructs a string representation of the entire machine. If a peg has a bean
	 * above it, it is represented as a "1", otherwise it is represented as a "0".
	 * At the very bottom is attached the slots with the bean counts.
	 * 
	 * @return the string representation of the machine
	 */
	@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
		    value="VA_FORMAT_STRING_USES_NEWLINE", 
		    justification="I know we should be using %n instead of \n, but JPF for some reason does not like %n")
	public String toString() {
		StringBuilder bld = new StringBuilder();
		Formatter fmt = new Formatter(bld);
		for (int yPos = 0; yPos < getSlotCount(); yPos++) {
			int xBeanPos = getInFlightBeanXPos(yPos);
			for (int xPos = 0; xPos <= yPos; xPos++) {
				int spacing = (xPos == 0) ? getIndent(yPos) : (xspacing + 1);
				String format = "%" + spacing + "d";
				if (xPos == xBeanPos) {
					fmt.format(format, 1);
				} else {
					fmt.format(format, 0);
				}
			}
			fmt.format("\n");
		}
		fmt.close();
		return bld.toString() + getSlotString();
	}

	/**
	 * Prints usage information.
	 */
	public static void showUsage() {
		System.out.println("Usage: java BeanCounterLogic slot_count bean_count <luck | skill> [debug]");
		System.out.println("Example: java BeanCounterLogic 10 400 luck");
		System.out.println("Example: java BeanCounterLogic 20 1000 skill debug");
	}
	
	/**
	 * Auxiliary main method. Runs the machine in text mode with no bells and
	 * whistles. It simply shows the slot bean count at the end.
	 * 
	 * @param args commandline arguments; see showUsage() for detailed information
	 */
	public static void main(String[] args) {
		boolean debug;
		boolean luck;
		int slotCount = 0;
		int beanCount = 0;

		if (args.length != 3 && args.length != 4) {
			showUsage();
			return;
		}

		try {
			slotCount = Integer.parseInt(args[0]);
			beanCount = Integer.parseInt(args[1]);
		} catch (NumberFormatException ne) {
			showUsage();
			return;
		}
		if (beanCount < 0) {
			showUsage();
			return;
		}

		if (args[2].equals("luck")) {
			luck = true;
		} else if (args[2].equals("skill")) {
			luck = false;
		} else {
			showUsage();
			return;
		}

		if (args.length == 4 && args[3].equals("debug")) {
			debug = true;
		} else {
			debug = false;
		}

		// Create the internal logic
		BeanCounterLogic logic = BeanCounterLogic.createInstance(InstanceType.IMPL, slotCount);;
		// Create the beans (in luck mode)
		Bean[] beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = Bean.createInstance(InstanceType.IMPL, slotCount, luck, new Random());
		}
		// Initialize the logic with the beans
		logic.reset(beans);

		if (debug) {
			System.out.println(logic.toString());
		}

		// Perform the experiment
		while (true) {
			try {
				if (!logic.advanceStep()) {
					break;
				}
			} catch (BeanOutOfBoundsException ex) {
				System.out.println("Bean went out of bounds unexpectedly.  Shutting down.");
				break;
			}
			if (debug) {
				System.out.println(logic.toString());
			}
		}
		// display experimental results
		System.out.println("Slot bean counts:");
		System.out.println(logic.getSlotString());
	}
}
