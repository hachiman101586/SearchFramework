package s17020031029.problem.npuzzle;

import core.problem.Action;
import core.problem.State;
import core.runner.HeuristicType;
import core.solver.heuristic.Predictor;
import java.util.ArrayList;
import java.util.EnumMap;

public class PuzzleState extends State {
    private int[][] puzzle;
    private int size;
    private int xpos;
    private int ypos;

    public void setSize(int size) {
        this.size = size;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int getSize() {
        return size;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = new int[this.getSize()][this.getSize()];
        for(int i=0;i<this.getSize();i++) {
            //System.arraycopy(puzzle[i],0,this.puzzle[i],0,getSize());
            for(int j=0;j<this.getSize();j++)
            {
                this.puzzle[i][j] = puzzle[i][j];
                if(puzzle[i][j] == 0) {
                    xpos = i;
                    ypos = j;
                }//标记0的位置
            }
        }
    }

    @Override
    public void draw() {
        for (int i = 0; i < this.getSize(); i++)
            System.out.print("+---");
        System.out.println("+");
        for (int i = 0; i < this.getSize(); i++) {
            for (int j = 0; j < this.getSize(); j++) {
                if (this.puzzle[i][j] != 0 && this.puzzle[i][j] < 10)
                    System.out.print("| " + this.puzzle[i][j] + " ");
                else if(this.puzzle[i][j] == 0)
                    System.out.print("| # ");
                else
                    System.out.print("|" + this.puzzle[i][j] + " ");
            }
            System.out.println("|");
            for (int j = 0; j < this.getSize(); j++)
                System.out.print("+---");
            System.out.println("+");
        }
    }

    @Override
    public State next(Action action) {
        PuzzleState st = new PuzzleState();
        st.setSize(this.getSize());
        st.setPuzzle(this.getPuzzle());
        int[][] state = st.getPuzzle();
        String dire = ((PuzzleAction)action).getDire();
        int i = getXpos(),j = getYpos();
        if (dire.equals("up")) {
            state[i][j] = state[i - 1][j];
            state[i - 1][j] = 0;
        } else if (dire.equals("down")) {
            state[i][j] = state[i + 1][j];
            state[i + 1][j] = 0;
        } else if (dire.equals("left")) {
            state[i][j] = state[i][j - 1];
            state[i][j - 1] = 0;
        } else if (dire.equals("right")) {
            state[i][j] = state[i][j + 1];
            state[i][j + 1] = 0;
        }
        st.setPuzzle(state);
        return st;
    }

    @Override
    public Iterable<? extends Action> actions() {
        ArrayList<PuzzleAction> actions = new ArrayList<>();
        String[] list = {"up","down","left","right"};
        for(String str : list)
        {
            PuzzleAction action = new PuzzleAction();
            action.setDire(str);
            actions.add(action);
        }
        return actions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

       if (obj instanceof PuzzleState || obj instanceof State) {
            PuzzleState another = (PuzzleState) obj;
            //两个Node对象的状态相同，则认为是相同的
            for(int i=0;i<getSize();i++)
                for(int j=0;j<getSize();j++) {
                    if (this.getPuzzle()[i][j] != another.getPuzzle()[i][j])
                        return false;
                }
            return true;
        }
       return false;
    }

    @Override
    public int hashCode()
    {
        int num=0;
        for(int i=0;i<getSize();i++)
            for(int j=0;j<getSize();j++)
                num += (i*3+j)*this.getPuzzle()[i][j];
        return num%8;
    }

    //枚举映射，存放不同类型的启发函数
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(HeuristicType.MISPLACED,
                (state, goal) -> ((PuzzleState)state).misplaced((PuzzleState)goal));
        predictors.put(HeuristicType.MANHATTAN,
                (state, goal) -> ((PuzzleState)state).manhattan((PuzzleState)goal));
        predictors.put(HeuristicType.DISJOINT_PATTERN,
                (state, goal) -> ((PuzzleState)state).disjoint_pattern((PuzzleState)goal));
    }
    public static Predictor predictor(HeuristicType type){
        Predictor predictor= predictors.get(type);    //map方法获取键为type的值
        return predictor;
    }
    //3个启发函数
    private int misplaced(PuzzleState goal){
        int num = 0;
        int[][] st = getPuzzle(), go = goal.getPuzzle();
        for (int i = 0; i < getSize(); i++)
            for (int j = 0; j < getSize(); j++) {
                if (st[i][j] != go[i][j] && go[i][j] != 0)
                    num++;
            }
        return num;
    }

    private int manhattan(PuzzleState goal){
        int num=0,j;
        int[][] st = getPuzzle(), go = goal.getPuzzle();
        for (int ig = 0; ig < goal.getSize(); ig++)
            for (int jg = 0; jg < goal.getSize(); jg++) {
                for (int i = 0; i < getSize(); i++) {
                    for (j = 0; j < getSize(); j++) {
                        if (st[i][j] == go[ig][jg] && go[ig][jg] != 0) {
                            num += Math.abs(i - ig) + Math.abs(j - jg);
                            break;
                        }
                        else if(go[ig][jg] == 0)
                            break;
                    }
                    if (j != getSize())
                        break;
                }
            }
        return num;
    }

    private int disjoint_pattern(PuzzleState goal){return 0;}
}
