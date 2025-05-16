package com.ricardthegreat.holdmetight.carry;

public class CarryPosition {
    /*
     * xymult,vertoffset, and left right move are named poorly so here is a bad explanation of them
     * xymult - moves the carried person towards and away from the carriers chest
     * leftrightmove - moves the carried person to the left or right relative to the way the carriers chest is facing
     * vertoffset - moves the carried person up and down
     */
    

    //this is poor etiqutte and should be private with getters
    public String posName;
    public int RotationOffset;
    public double xymult;
    public double vertOffset;
    public double leftRightMove;
    public boolean headLink;

    public CarryPosition(String posName, int RotationOffset, double xymult, double vertOffset, double leftRightMove, boolean headLink){
        this.posName = posName;
        this.RotationOffset = RotationOffset;
        this.xymult = xymult;
        this.vertOffset = vertOffset;
        this.leftRightMove = leftRightMove;
        this.headLink = headLink;
    }
}
