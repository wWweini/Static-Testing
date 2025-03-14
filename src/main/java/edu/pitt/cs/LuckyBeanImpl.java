package edu.pitt.cs;

import java.util.Random;

/**
 * Code by @author Wonsun Ahn. Copyright Summer 2024.
 * 
 * <p>
 * Lucky beans have a 50/50 chance of going right or left. The formula to
 * calculate the direction is: rand.nextInt(2). If the return value is 0, the
 * bean goes left. If the return value is 1, the bean goes right.
 */

public class LuckyBeanImpl implements Bean {

	// TODO: Add member methods and variables as needed
	private int xPos;   
    private int yPos;  
    private Random rand; 
	private int slotCount;

	/**
	 * Constructor - creates a bean in either luck mode or skill mode.
	 * 
	 * @param slotCount the number of slots in the machine
	 * @param rand      the random number generator
	 */
	LuckyBeanImpl(int slotCount, Random rand) {
		// TODO: Implement
		this.slotCount = slotCount;
        this.rand = rand;
        reset();
	}

	public int getXPos() {
		// TODO: Implement
		return xPos;
	}

	public int getYPos() {
		// TODO: Implement
		return yPos;
	}

	/**
	 * Resets the bean.
	 */
	public void reset() {
		// TODO: Implement
		this.xPos = 0;  
        this.yPos = 0;
	}

	/**
	 * Increment ypos by 1. If bean goes right, increment xpos by 1. If bean goes
	 * left, do nothing to xpos. If the return value of rand.nextInt(2) is 0, the
	 * bean goes left. Otherwise, the bean goes right. If resulting xpos or ypos are
	 * greater than or equal to slotCount throw BeanOutOfBoundsException.
	 * 
	 * @throws BeanOutOfBoundsException
	 */
	public void advanceStep() throws BeanOutOfBoundsException {
		// TODO: Implement
		// Move down one position
        yPos++;
        
        // For positions after the first row, determine left/right movement
        if (yPos > 0) {  // Only move left/right after first row
            if (rand.nextInt(2) == 1) {
                xPos++;
            }
        }
        
        // Check if bean has moved out of bounds
        if (xPos >= slotCount || yPos >= slotCount) {
            throw new BeanOutOfBoundsException();
        }
	}
}