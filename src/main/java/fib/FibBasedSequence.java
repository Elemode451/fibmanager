package fib;

public class FibBasedSequence {
    private int[] mem;
    private String name;
    private static int NUM_SEQUENCES = 1;
    private int BASE_CASES = 2; // will add more than 2 base cases later
    static final String DELIMITER = "::";
    
    public FibBasedSequence(int first, int second) {
        this("Sequence #" + NUM_SEQUENCES++, new int[] { first, second });
    }
    
    public FibBasedSequence(String name, int first, int second) {
        this(name, new int[] { first, second });
    }    

    public FibBasedSequence(int[] baseCases) {  
        this("Sequence #" + NUM_SEQUENCES++, baseCases);
    }

    public FibBasedSequence(String name, int[] baseCases) {
        this.name = name;
        this.BASE_CASES = baseCases.length;
        this.mem = new int[FibManager.getNumElements() + 1];
        for(int i = 0; i < baseCases.length; i++) {
            mem[i] = baseCases[i];
        }
        
        for(int i = BASE_CASES; i <= FibManager.getNumElements(); i++) {
            mem[i] = recurrence(i, mem);
        }     
    }

    private int recurrence(int i, int[] arr) {
        return arr[i - 1] + arr[i - 2];
    }


    public void regenerate() {
        if(FibManager.getNumElements() > mem.length) {
            int[] update = new int[FibManager.getNumElements()];

            for(int i = 0; i < mem.length; i++) {
                update[i] = mem[i];
            }

            for(int i = mem.length; i < FibManager.getNumElements(); i++) {
                update[i] = recurrence(i, update);
            }

            mem = update;
        }
    }

    public double find(int idx) {
        return mem[idx - 1]; // 1st fib number at 0
    }

    public void prettyPrint() {
        System.out.print("[ ");
        if(!name.isEmpty()) {
            System.out.print(this.name + " ");
        }
        for(int i = 0; i < mem.length; i++) {
            System.out.print(mem[i] + " ");
        }
        System.out.print("]");
        System.out.println();
    }

    public String getSequence() {
        StringBuilder build = new StringBuilder();

        for(int i = 0; i < FibManager.getNumElements(); i++) {
            build.append(mem[i] + " ");
        }

        return build.toString();
    }

    public String toString() {
        return name;
    }

    public String serialize() {
        StringBuilder build = new StringBuilder();
        build.append(name);
        build.append(DELIMITER);

        for(int i = 0; i < BASE_CASES; i++) {
            build.append(mem[i]);
            build.append(DELIMITER);
        }

        return build.toString();
    }


    public int[] getMem() {
        return this.mem;
    }
    
    
}