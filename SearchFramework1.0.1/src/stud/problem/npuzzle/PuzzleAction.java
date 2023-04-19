package stud.problem.npuzzle;

import core.problem.Action;

public class PuzzleAction extends Action {
    private String dire;

    public void setDire(String dire) {
        this.dire = dire;
    }

    public String getDire() {
        return dire;
    }

    @Override
    public void draw() {
        System.out.println("->"+getDire());
    }

    @Override
    public int stepCost() {
        return 1;
    }
}
