package com.desiremc.hcf.tasks;

import com.desiremc.core.utils.BlockColumn;

import java.util.function.Predicate;

/**
 * Used to search for a particular condition of a {@link BlockColumn}.
 *
 * @author Michael Ziluck
 */
public abstract class SpiralBlockSearch implements Runnable
{

    private Predicate<? super BlockColumn> predicate;

    private BlockColumn cursor;

    private int count = 0;

    private int max = 1000;

    private int length = 1;

    private SpiralDirection direction;

    /**
     * Constructs a new SpiralBlockSearch that starts at the given location and searches for the given condition. Will
     * loop up to 1,000 times.
     *
     * @param cursor the starting point.
     * @param predicate the condition to fulfill.
     */
    public SpiralBlockSearch(BlockColumn cursor, Predicate<? super BlockColumn> predicate)
    {
        this.cursor = cursor.clone();
        this.predicate = predicate;
        this.direction = SpiralDirection.NEGATIVE_X;
    }

    /**
     * Constructs a new SpiralBlockSearch that starts at the given location and searches for the given condition.
     *
     * @param cursor the starting point.
     * @param predicate the condition to fulfill.
     * @param max the max amount of iterations to search for.
     */
    public SpiralBlockSearch(BlockColumn cursor, Predicate<? super BlockColumn> predicate, int max)
    {
        this.cursor = cursor.clone();
        this.predicate = predicate;
        this.direction = SpiralDirection.NEGATIVE_X;
    }

    @Override
    public void run()
    {
        while (!predicate.test(cursor))
        {
            if (count == length)
            {
                count = 0;
                length++;
                direction = direction.nextDirection();
            }
            switch (direction)
            {
                case NEGATIVE_X:
                    cursor.setX(cursor.getX() - 1);
                    break;
                case POSITIVE_Z:
                    cursor.setZ(cursor.getZ() + 1);
                    break;
                case POSITIVE_X:
                    cursor.setX(cursor.getX() + 1);
                    break;
                case NEGATIVE_Z:
                    cursor.setZ(cursor.getZ() - 1);
                    break;
            }
            count++;
            if (count >= max)
            {
                cursor = null;
                onOverflow();
                return;
            }
        }
        onSuccess(cursor);
    }

    public abstract void onSuccess(BlockColumn column);

    public abstract void onOverflow();

    private static enum SpiralDirection
    {
        NEGATIVE_Z,
        POSITIVE_X,
        POSITIVE_Z,
        NEGATIVE_X;

        public SpiralDirection nextDirection()
        {
            if (ordinal() == values().length - 1)
            {
                return values()[0];
            }
            else
            {
                return values()[ordinal() + 1];
            }
        }
    }

}