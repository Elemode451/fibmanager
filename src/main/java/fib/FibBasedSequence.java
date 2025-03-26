package fib;

public class FibBasedSequence {
    private int[] mem;
    private String name;
    private static int NUM_SEQUENCES = 1;
    
    public FibBasedSequence(int first, int second) {
        this("Sequence #" + NUM_SEQUENCES++, first, second);
    }
    
    public FibBasedSequence(String name, int first, int second) {
        this.name = name;

        this.mem = new int[FibManager.getNumElements() + 1];
        mem[0] = first;
        mem[1] = second;
        
        for(int i = 2; i <= FibManager.getNumElements(); i++) {
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

    @Override
    public String toString() {
        return name;
    }


    public int[] getMem() {
        return this.mem;
    }
    
    
}