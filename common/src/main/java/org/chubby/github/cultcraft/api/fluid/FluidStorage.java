package org.chubby.github.cultcraft.api.fluid;

public abstract class FluidStorage {
    private int fluidAmount;
    private int maxFluidAmount;
    private int maxReceive;
    private int maxInsert;

    public FluidStorage(int fluidAmount, int maxFluidAmount) {
        this(fluidAmount, maxFluidAmount, 50, 50);
    }

    public FluidStorage(int fluidAmount, int maxFluidAmount, int maxReceive, int maxInsert) {
        this.fluidAmount = fluidAmount;
        this.maxFluidAmount = maxFluidAmount;
        this.maxReceive = maxReceive;
        this.maxInsert = maxInsert;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    public int getMaxFluidAmount() {
        return maxFluidAmount;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxInsert() {
        return maxInsert;
    }

    public void setFluidAmount(int amount) {
        this.fluidAmount = Math.max(0, Math.min(amount, maxFluidAmount));
        onContentsChanged();
    }

    /**
     * Insert fluid into this storage
     * @param amount Amount to insert
     * @param simulate If true, no actual insertion occurs
     * @return Amount actually inserted
     */
    public int insertFluid(int amount, boolean simulate) {
        if (amount <= 0) return 0;

        int toInsert = Math.min(amount, maxInsert);
        int availableSpace = maxFluidAmount - fluidAmount;
        int inserted = Math.min(toInsert, availableSpace);

        if (!simulate && inserted > 0) {
            fluidAmount += inserted;
            onContentsChanged();
        }

        return inserted;
    }

    /**
     * Extract fluid from this storage
     * @param amount Amount to extract
     * @param simulate If true, no actual extraction occurs
     * @return Amount actually extracted
     */
    public int extractFluid(int amount, boolean simulate) {
        if (amount <= 0) return 0;

        int toExtract = Math.min(amount, maxReceive);
        int extracted = Math.min(toExtract, fluidAmount);

        if (!simulate && extracted > 0) {
            fluidAmount -= extracted;
            onContentsChanged();
        }

        return extracted;
    }

    /**
     * Check if this storage can receive fluid
     */
    public boolean canReceive() {
        return maxInsert > 0 && fluidAmount < maxFluidAmount;
    }

    /**
     * Check if this storage can provide fluid
     */
    public boolean canExtract() {
        return maxReceive > 0 && fluidAmount > 0;
    }

    /**
     * Get the available space in this storage
     */
    public int getAvailableSpace() {
        return maxFluidAmount - fluidAmount;
    }

    /**
     * Check if storage is full
     */
    public boolean isFull() {
        return fluidAmount >= maxFluidAmount;
    }

    /**
     * Check if storage is empty
     */
    public boolean isEmpty() {
        return fluidAmount <= 0;
    }

    /**
     * Called when the contents change. Override to add custom behavior
     * (e.g., marking block entity as dirty, syncing to clients)
     */
    protected void onContentsChanged() {
      }

    /**
     * Create a basic FluidStorage instance
     */
    public static FluidStorage create(int fluidAmount, int maxFluidAmount, int maxReceive, int maxInsert) {
        return new FluidStorage(fluidAmount, maxFluidAmount, maxReceive, maxInsert) {
        };
    }

    /**
     * Create a basic FluidStorage instance with default transfer rates
     */
    public static FluidStorage create(int maxFluidAmount) {
        return create(0, maxFluidAmount, 50, 50);
    }
}