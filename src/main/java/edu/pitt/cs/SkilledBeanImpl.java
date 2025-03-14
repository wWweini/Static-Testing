package edu.pitt.cs;

import java.util.Random;

/**
 * Code by @author Wonsun Ahn. Copyright Summer 2024.
 * 
 * <p>
 * Each bean is assigned a skill level according to a normal distribution with
 * average SKILL_AVERAGE and standard deviation SKILL_STDEV. The formula to
 * calculate the skill level is:
 * 
 * <pre>
 * SKILL_AVERAGE = (double) (SLOT_COUNT-1) * 0.5
 * SKILL_STDEV = (double) Math.sqrt(SLOT_COUNT * 0.5 * (1 - 0.5))
 * SKILL_LEVEL = (int) Math.round(rand.nextGaussian() * SKILL_STDEV + SKILL_AVERAGE)
 * </pre>
 * 
 * <p>
 * Make sure you bound SKILL_LEVEL to be between 0 and SLOT_COUNT-1, if the
 * randomly generated SKILL_LEVEL exceeds those bounds. If SKILL_LEVEL < 0, then
 * you can bound it to 0, and if SKILL_LEVEL > SLOT_COUNT-1, then you can bound
 * it to SLOT_COUNT-1.
 * 
 * <p>
 * A SKILL_LEVEL of SLOT_COUNT-1 means the bean always makes the "right" choices
 * (pun intended), always going right when a peg is encountered, resulting in it
 * falling into slot number SLOT_COUNT-1. A SKILL_LEVEL of 0 means that the bean
 * will always go left, resulting it falling into slot 0. For the in-between
 * SKILL_LEVELs, the bean will initially go right and then left. For example,
 * for a bean with SKILL_LEVEL SLOT_COUNT-3, the bean will initially go right
 * SLOT_COUNT-3 times, and then left 2 times, falling into slot SLOT_COUNT-3.
 * 
 */

public class SkilledBeanImpl implements Bean {

	// TODO: Add member methods and variables as needed
	private int slotCount;
    private int xPos;
    private int yPos;
    private int skillLevel;
    private int rightMovesRemaining;

	/**
	 * Constructor - creates a bean in skilled mode. It also assigns a skill level
	 * to the bean according to a normal distribution as described above using rand.
	 * 
	 * @param slotCount the number of slots in the machine
	 * @param rand      the random number generator
	 */
	SkilledBeanImpl(int slotCount, Random rand) {
		// TODO: Implement
		this.slotCount = slotCount;
        
        //calculate skill level using normal distribution
        double skillAverage = (double)(slotCount - 1) * 0.5;
        double skillStdev = Math.sqrt(slotCount * 0.5 * (1 - 0.5));
        int calculatedSkill = (int) Math.round(rand.nextGaussian() * skillStdev + skillAverage);
        
        //skill level between 0 and slotCount-1
        this.skillLevel = Math.min(Math.max(calculatedSkill, 0), slotCount - 1);
        
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
        this.rightMovesRemaining = this.skillLevel;
	}

	/**
	 * Increment ypos by 1. If bean goes right, increment xpos by 1. If bean goes
	 * left, do nothing to xpos. The bean initially always goes right SKILL_LEVEL
	 * times and then always goes left. If resulting xpos or ypos are greater than
	 * or equal to slotCount throwBeanOutOfBoundsException.
	 * 
	 * @throws BeanOutOfBoundsException
	 */
	public void advanceStep() throws BeanOutOfBoundsException {
		// TODO: Implement
		yPos++;
        
        // Move right if we still have right moves remaining
        if (rightMovesRemaining > 0) {
            xPos++;
            rightMovesRemaining--;
        }
        // Otherwise move left (xPos stays the same)
        
        // Check if bean is out of bounds
        if (xPos >= slotCount || yPos >= slotCount) {
            throw new BeanOutOfBoundsException();
        }
	}

}